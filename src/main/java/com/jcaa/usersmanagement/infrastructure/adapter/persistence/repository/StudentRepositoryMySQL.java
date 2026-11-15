package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteStudentPort;
import com.jcaa.usersmanagement.application.port.out.GetAllStudentsPort;
import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveStudentPort;
import com.jcaa.usersmanagement.application.port.out.UpdateStudentPort;
import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.StudentPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.StudentPersistenceMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Adaptador de salida (JDBC puro, sin ORM) que guarda los estudiantes en MySQL.
 *
 * <p>Implementa los mismos puertos de salida que {@link StudentRepositoryMemory}, por eso los
 * casos de uso pueden usar uno u otro sin cambiar una sola línea.
 */
@RequiredArgsConstructor
public final class StudentRepositoryMySQL
    implements SaveStudentPort,
        UpdateStudentPort,
        GetStudentByIdPort,
        GetAllStudentsPort,
        DeleteStudentPort {

  private static final String SQL_INSERT =
      "INSERT INTO students (id, name, group_name, created_at, updated_at) "
          + "VALUES (?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE students SET name = ?, group_name = ?, updated_at = NOW() WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT id, name, group_name FROM students WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT id, name, group_name FROM students ORDER BY name ASC";

  private static final String SQL_DELETE = "DELETE FROM students WHERE id = ?";

  private final Connection connection;

  @Override
  public StudentModel save(final StudentModel student) {
    final StudentPersistenceDto dto = StudentPersistenceMapper.fromModelToDto(student);
    executeSave(dto);
    return findByIdOrFail(student.getId());
  }

  @Override
  public StudentModel update(final StudentModel student) {
    final StudentPersistenceDto dto = StudentPersistenceMapper.fromModelToDto(student);
    executeUpdate(dto);
    return findByIdOrFail(student.getId());
  }

  @Override
  public Optional<StudentModel> getById(final StudentId studentId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, studentId.value());
      try (final ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return Optional.empty();
        }
        return Optional.of(StudentPersistenceMapper.fromResultSetToModel(resultSet));
      }
    } catch (final SQLException exception) {
      throw PersistenceException.becauseStudentFindByIdFailed(studentId.value(), exception);
    }
  }

  @Override
  public List<StudentModel> getAll() {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
        final ResultSet resultSet = statement.executeQuery()) {
      return StudentPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw PersistenceException.becauseStudentFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final StudentId studentId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, studentId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseStudentDeleteFailed(studentId.value(), exception);
    }
  }

  private void executeSave(final StudentPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.name());
      statement.setString(3, dto.group());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseStudentSaveFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final StudentPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.name());
      statement.setString(2, dto.group());
      statement.setString(3, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseStudentUpdateFailed(dto.id(), exception);
    }
  }

  private StudentModel findByIdOrFail(final StudentId studentId) {
    return getById(studentId)
        .orElseThrow(() -> StudentNotFoundException.becauseIdWasNotFound(studentId.value()));
  }
}

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentGroup;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/** JDBC is mocked, so no real database is required to run these tests. */
@DisplayName("StudentRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentRepositoryMySQLTest {

  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private StudentRepositoryMySQL repository;

  @BeforeEach
  void setUp() throws SQLException {
    repository = new StudentRepositoryMySQL(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
  }

  private void rowReturns(final String id, final String name, final String group)
      throws SQLException {
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("id")).thenReturn(id);
    when(resultSet.getString("name")).thenReturn(name);
    when(resultSet.getString("group_name")).thenReturn(group);
  }

  @Test
  @DisplayName("getById() returns the student when the row exists")
  void shouldReturnStudentWhenRowExists() throws SQLException {
    rowReturns("2024001", "Laura Martinez", "G1");

    final StudentModel result = repository.getById(new StudentId("2024001")).orElseThrow();

    assertEquals("Laura Martinez", result.getName().value());
    assertEquals("G1", result.getGroup().value());
    verify(statement).setString(1, "2024001");
  }

  @Test
  @DisplayName("getById() returns empty when there is no row")
  void shouldReturnEmptyWhenNoRow() throws SQLException {
    when(resultSet.next()).thenReturn(false);

    assertTrue(repository.getById(new StudentId("404")).isEmpty());
  }

  @Test
  @DisplayName("save() inserts the row and returns the stored student")
  void shouldInsertAndReturnStudent() throws SQLException {
    rowReturns("2024001", "Laura Martinez", "G1");
    final StudentModel laura =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Laura Martinez"), new StudentGroup("G1"));

    final StudentModel result = repository.save(laura);

    assertEquals(laura, result);
    // El id se usa dos veces: en el INSERT y en el SELECT que devuelve el estudiante guardado.
    verify(statement, times(2)).setString(1, "2024001");
    verify(statement).setString(2, "Laura Martinez");
    verify(statement).setString(3, "G1");
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("update() changes name and group using the id in the WHERE clause")
  void shouldUpdateStudent() throws SQLException {
    rowReturns("2024001", "Laura Gomez", "G2");
    final StudentModel changed =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Laura Gomez"), new StudentGroup("G2"));

    final StudentModel result = repository.update(changed);

    assertEquals(changed, result);
    verify(statement).setString(1, "Laura Gomez");
    verify(statement).setString(2, "G2");
    verify(statement).setString(3, "2024001");
  }

  @Test
  @DisplayName("delete() executes the DELETE with the student id")
  void shouldDeleteStudent() throws SQLException {
    repository.delete(new StudentId("2024001"));

    verify(statement).setString(1, "2024001");
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("getAll() maps every row to a student")
  void shouldListAllStudents() throws SQLException {
    when(resultSet.next()).thenReturn(true, true, false);
    when(resultSet.getString("id")).thenReturn("1", "2");
    when(resultSet.getString("name")).thenReturn("Carlos Perez", "Laura Martinez");
    when(resultSet.getString("group_name")).thenReturn("G1", "G2");

    final List<StudentModel> result = repository.getAll();

    assertEquals(2, result.size());
    assertEquals("Carlos Perez", result.get(0).getName().value());
  }

  @Test
  @DisplayName("wraps SQLException in PersistenceException")
  void shouldWrapSqlException() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

    assertThrows(PersistenceException.class, () -> repository.getAll());
    assertThrows(PersistenceException.class, () -> repository.delete(new StudentId("1")));
    assertThrows(
        PersistenceException.class, () -> repository.getById(new StudentId("1")));
  }
}

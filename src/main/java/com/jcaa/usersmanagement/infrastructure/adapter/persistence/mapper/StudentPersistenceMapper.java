package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentGroup;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.StudentPersistenceDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StudentPersistenceMapper {

  public StudentPersistenceDto fromModelToDto(final StudentModel student) {
    return new StudentPersistenceDto(
        student.getId().value(), student.getName().value(), student.getGroup().value());
  }

  public StudentPersistenceDto fromResultSetToDto(final ResultSet resultSet) throws SQLException {
    return new StudentPersistenceDto(
        resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("group_name"));
  }

  public StudentModel fromDtoToModel(final StudentPersistenceDto dto) {
    return new StudentModel(
        new StudentId(dto.id()), new StudentName(dto.name()), new StudentGroup(dto.group()));
  }

  public StudentModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromDtoToModel(fromResultSetToDto(resultSet));
  }

  public List<StudentModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<StudentModel> students = new ArrayList<>();
    while (resultSet.next()) {
      students.add(fromResultSetToModel(resultSet));
    }
    return students;
  }
}

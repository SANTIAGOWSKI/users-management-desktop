package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateStudentCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetStudentByIdQuery;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentGroup;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StudentApplicationMapper {

  public StudentModel fromCreateCommandToModel(final CreateStudentCommand command) {
    return StudentModel.create(
        new StudentId(command.id()),
        new StudentName(command.name()),
        new StudentGroup(command.group()));
  }

  public StudentId fromGetStudentByIdQueryToStudentId(final GetStudentByIdQuery query) {
    return new StudentId(query.id());
  }
}

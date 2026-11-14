package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateStudentCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteStudentCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateStudentCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetStudentByIdQuery;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateStudentRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateStudentRequest;
import java.util.List;

public final class StudentDesktopMapper {

  private StudentDesktopMapper() {}

  public static CreateStudentCommand toCreateCommand(final CreateStudentRequest request) {
    return new CreateStudentCommand(request.id(), request.name(), request.group());
  }

  public static UpdateStudentCommand toUpdateCommand(final UpdateStudentRequest request) {
    return new UpdateStudentCommand(request.id(), request.name(), request.group());
  }

  public static DeleteStudentCommand toDeleteCommand(final String id) {
    return new DeleteStudentCommand(id);
  }

  public static GetStudentByIdQuery toGetByIdQuery(final String id) {
    return new GetStudentByIdQuery(id);
  }

  public static StudentResponse toResponse(final StudentModel student) {
    return new StudentResponse(
        student.getId().value(), student.getName().value(), student.getGroup().value());
  }

  public static List<StudentResponse> toResponseList(final List<StudentModel> students) {
    return students.stream().map(StudentDesktopMapper::toResponse).toList();
  }
}

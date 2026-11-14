package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateStudentUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteStudentUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllStudentsUseCase;
import com.jcaa.usersmanagement.application.port.in.GetStudentByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateStudentUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateStudentRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateStudentRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.StudentDesktopMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class StudentController {

  private final CreateStudentUseCase createStudentUseCase;
  private final UpdateStudentUseCase updateStudentUseCase;
  private final DeleteStudentUseCase deleteStudentUseCase;
  private final GetStudentByIdUseCase getStudentByIdUseCase;
  private final GetAllStudentsUseCase getAllStudentsUseCase;

  public List<StudentResponse> listAllStudents() {
    final var students = getAllStudentsUseCase.execute();
    return StudentDesktopMapper.toResponseList(students);
  }

  public StudentResponse findStudentById(final String id) {
    final var query = StudentDesktopMapper.toGetByIdQuery(id);
    final var student = getStudentByIdUseCase.execute(query);
    return StudentDesktopMapper.toResponse(student);
  }

  public StudentResponse createStudent(final CreateStudentRequest request) {
    final var command = StudentDesktopMapper.toCreateCommand(request);
    final var student = createStudentUseCase.execute(command);
    return StudentDesktopMapper.toResponse(student);
  }

  public StudentResponse updateStudent(final UpdateStudentRequest request) {
    final var command = StudentDesktopMapper.toUpdateCommand(request);
    final var student = updateStudentUseCase.execute(command);
    return StudentDesktopMapper.toResponse(student);
  }

  public void deleteStudent(final String id) {
    final var command = StudentDesktopMapper.toDeleteCommand(id);
    deleteStudentUseCase.execute(command);
  }
}

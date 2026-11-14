package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateStudentUseCase;
import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateStudentPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateStudentCommand;
import com.jcaa.usersmanagement.application.service.mapper.StudentApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateStudentService implements UpdateStudentUseCase {

  private final UpdateStudentPort updateStudentPort;
  private final GetStudentByIdPort getStudentByIdPort;
  private final Validator validator;

  @Override
  public StudentModel execute(final UpdateStudentCommand command) {
    validateCommand(command);
    final StudentId studentId = new StudentId(command.id());
    ensureStudentExists(studentId);
    final StudentModel studentToUpdate = StudentApplicationMapper.fromUpdateCommandToModel(command);
    return updateStudentPort.update(studentToUpdate);
  }

  private void validateCommand(final UpdateStudentCommand command) {
    final Set<ConstraintViolation<UpdateStudentCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureStudentExists(final StudentId studentId) {
    getStudentByIdPort
        .getById(studentId)
        .orElseThrow(() -> StudentNotFoundException.becauseIdWasNotFound(studentId.value()));
  }
}

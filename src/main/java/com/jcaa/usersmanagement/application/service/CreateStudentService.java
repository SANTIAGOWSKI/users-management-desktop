package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateStudentUseCase;
import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveStudentPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateStudentCommand;
import com.jcaa.usersmanagement.application.service.mapper.StudentApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.StudentAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateStudentService implements CreateStudentUseCase {

  private final SaveStudentPort saveStudentPort;
  private final GetStudentByIdPort getStudentByIdPort;
  private final Validator validator;

  @Override
  public StudentModel execute(final CreateStudentCommand command) {
    validateCommand(command);
    final StudentId studentId = new StudentId(command.id());
    ensureIdIsNotTaken(studentId);
    final StudentModel studentToSave = StudentApplicationMapper.fromCreateCommandToModel(command);
    return saveStudentPort.save(studentToSave);
  }

  private void validateCommand(final CreateStudentCommand command) {
    final Set<ConstraintViolation<CreateStudentCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureIdIsNotTaken(final StudentId studentId) {
    getStudentByIdPort
        .getById(studentId)
        .ifPresent(
            ignored -> {
              throw StudentAlreadyExistsException.becauseIdAlreadyExists(studentId.value());
            });
  }
}

package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveStudentPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateStudentCommand;
import com.jcaa.usersmanagement.domain.exception.StudentAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentGroup;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import com.jcaa.usersmanagement.infrastructure.config.ValidatorProvider;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CreateStudentService")
@ExtendWith(MockitoExtension.class)
class CreateStudentServiceTest {

  @Mock private SaveStudentPort saveStudentPort;
  @Mock private GetStudentByIdPort getStudentByIdPort;

  private CreateStudentService service;

  @BeforeEach
  void setUp() {
    service =
        new CreateStudentService(
            saveStudentPort, getStudentByIdPort, ValidatorProvider.buildValidator());
  }

  @Test
  @DisplayName("saves the student when the id is not taken")
  void shouldSaveStudentWhenIdIsNotTaken() {
    final CreateStudentCommand command = new CreateStudentCommand("2024001", "Laura Martinez", "G1");
    final StudentModel expected =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Laura Martinez"), new StudentGroup("G1"));
    when(getStudentByIdPort.getById(new StudentId("2024001"))).thenReturn(Optional.empty());
    when(saveStudentPort.save(any(StudentModel.class))).thenReturn(expected);

    final StudentModel result = service.execute(command);

    assertEquals(expected, result);
    verify(saveStudentPort).save(expected);
  }

  @Test
  @DisplayName("throws StudentAlreadyExistsException when the id already exists")
  void shouldThrowWhenIdAlreadyExists() {
    final CreateStudentCommand command = new CreateStudentCommand("2024001", "Laura Martinez", "G1");
    final StudentModel existing =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Otra Persona"), new StudentGroup("G2"));
    when(getStudentByIdPort.getById(new StudentId("2024001"))).thenReturn(Optional.of(existing));

    assertThrows(StudentAlreadyExistsException.class, () -> service.execute(command));

    verify(saveStudentPort, never()).save(any(StudentModel.class));
  }

  @Test
  @DisplayName("throws ConstraintViolationException when the name is too short")
  void shouldThrowWhenNameIsTooShort() {
    final CreateStudentCommand command = new CreateStudentCommand("2024001", "Al", "G1");

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));

    verify(saveStudentPort, never()).save(any(StudentModel.class));
  }
}

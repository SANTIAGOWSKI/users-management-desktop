package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateStudentPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateStudentCommand;
import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
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

@DisplayName("UpdateStudentService")
@ExtendWith(MockitoExtension.class)
class UpdateStudentServiceTest {

  @Mock private UpdateStudentPort updateStudentPort;
  @Mock private GetStudentByIdPort getStudentByIdPort;

  private UpdateStudentService service;

  @BeforeEach
  void setUp() {
    service =
        new UpdateStudentService(
            updateStudentPort, getStudentByIdPort, ValidatorProvider.buildValidator());
  }

  @Test
  @DisplayName("updates the student when it exists")
  void shouldUpdateStudentWhenItExists() {
    final UpdateStudentCommand command = new UpdateStudentCommand("2024001", "Laura Gomez", "G2");
    final StudentModel current =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Laura Martinez"), new StudentGroup("G1"));
    final StudentModel updated =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Laura Gomez"), new StudentGroup("G2"));
    when(getStudentByIdPort.getById(new StudentId("2024001"))).thenReturn(Optional.of(current));
    when(updateStudentPort.update(any(StudentModel.class))).thenReturn(updated);

    final StudentModel result = service.execute(command);

    assertEquals(updated, result);
    verify(updateStudentPort).update(updated);
  }

  @Test
  @DisplayName("throws StudentNotFoundException when the student does not exist")
  void shouldThrowWhenStudentDoesNotExist() {
    final UpdateStudentCommand command = new UpdateStudentCommand("9999", "Laura Gomez", "G2");
    when(getStudentByIdPort.getById(new StudentId("9999"))).thenReturn(Optional.empty());

    assertThrows(StudentNotFoundException.class, () -> service.execute(command));

    verify(updateStudentPort, never()).update(any(StudentModel.class));
  }

  @Test
  @DisplayName("throws ConstraintViolationException when the group is blank")
  void shouldThrowWhenGroupIsBlank() {
    final UpdateStudentCommand command = new UpdateStudentCommand("2024001", "Laura Gomez", " ");

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));

    verify(updateStudentPort, never()).update(any(StudentModel.class));
  }
}

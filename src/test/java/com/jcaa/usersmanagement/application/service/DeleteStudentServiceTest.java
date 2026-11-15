package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.DeleteStudentPort;
import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteStudentCommand;
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

@DisplayName("DeleteStudentService")
@ExtendWith(MockitoExtension.class)
class DeleteStudentServiceTest {

  @Mock private DeleteStudentPort deleteStudentPort;
  @Mock private GetStudentByIdPort getStudentByIdPort;

  private DeleteStudentService service;

  @BeforeEach
  void setUp() {
    service =
        new DeleteStudentService(
            deleteStudentPort, getStudentByIdPort, ValidatorProvider.buildValidator());
  }

  @Test
  @DisplayName("deletes the student when it exists")
  void shouldDeleteStudentWhenItExists() {
    final StudentId id = new StudentId("2024001");
    final StudentModel current =
        StudentModel.create(id, new StudentName("Laura Martinez"), new StudentGroup("G1"));
    when(getStudentByIdPort.getById(id)).thenReturn(Optional.of(current));

    service.execute(new DeleteStudentCommand("2024001"));

    verify(deleteStudentPort).delete(id);
  }

  @Test
  @DisplayName("throws StudentNotFoundException when the student does not exist")
  void shouldThrowWhenStudentDoesNotExist() {
    when(getStudentByIdPort.getById(new StudentId("9999"))).thenReturn(Optional.empty());

    assertThrows(
        StudentNotFoundException.class, () -> service.execute(new DeleteStudentCommand("9999")));

    verify(deleteStudentPort, never()).delete(any(StudentId.class));
  }

  @Test
  @DisplayName("throws ConstraintViolationException when the id is blank")
  void shouldThrowWhenIdIsBlank() {
    assertThrows(
        ConstraintViolationException.class, () -> service.execute(new DeleteStudentCommand(" ")));

    verify(deleteStudentPort, never()).delete(any(StudentId.class));
  }
}

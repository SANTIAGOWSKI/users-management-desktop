package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetStudentByIdQuery;
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

@DisplayName("GetStudentByIdService")
@ExtendWith(MockitoExtension.class)
class GetStudentByIdServiceTest {

  @Mock private GetStudentByIdPort getStudentByIdPort;

  private GetStudentByIdService service;

  @BeforeEach
  void setUp() {
    service = new GetStudentByIdService(getStudentByIdPort, ValidatorProvider.buildValidator());
  }

  @Test
  @DisplayName("returns the student when it exists")
  void shouldReturnStudentWhenItExists() {
    final StudentModel student =
        StudentModel.create(
            new StudentId("2024001"), new StudentName("Laura Martinez"), new StudentGroup("G1"));
    when(getStudentByIdPort.getById(new StudentId("2024001"))).thenReturn(Optional.of(student));

    assertEquals(student, service.execute(new GetStudentByIdQuery("2024001")));
  }

  @Test
  @DisplayName("throws StudentNotFoundException when the student does not exist")
  void shouldThrowWhenStudentDoesNotExist() {
    when(getStudentByIdPort.getById(new StudentId("9999"))).thenReturn(Optional.empty());

    assertThrows(
        StudentNotFoundException.class, () -> service.execute(new GetStudentByIdQuery("9999")));
  }

  @Test
  @DisplayName("throws ConstraintViolationException when the id is blank")
  void shouldThrowWhenIdIsBlank() {
    assertThrows(
        ConstraintViolationException.class, () -> service.execute(new GetStudentByIdQuery("")));
  }
}

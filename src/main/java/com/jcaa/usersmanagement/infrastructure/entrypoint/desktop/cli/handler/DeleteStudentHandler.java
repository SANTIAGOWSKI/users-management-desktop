package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.StudentController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteStudentHandler implements OperationHandler {

  private final StudentController studentController;
  private final ConsoleIO console;

  @Override
  public void handle() {
    final String id = console.readRequired("Student ID to delete: ");

    try {
      studentController.deleteStudent(id);
      console.println("  Student deleted successfully.");
    } catch (final StudentNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}

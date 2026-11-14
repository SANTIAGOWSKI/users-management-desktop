package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.StudentResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.StudentController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateStudentRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateStudentHandler implements OperationHandler {

  private final StudentController studentController;
  private final ConsoleIO console;
  private final StudentResponsePrinter printer;

  @Override
  public void handle() {
    final String id = console.readRequired("Student ID (matricula): ");
    final String name = console.readRequired("New name             : ");
    final String group = console.readRequired("New group            : ");

    try {
      final StudentResponse updated =
          studentController.updateStudent(new UpdateStudentRequest(id, name, group));
      console.println("\n  Student updated successfully.");
      printer.print(updated);
    } catch (final StudentNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}

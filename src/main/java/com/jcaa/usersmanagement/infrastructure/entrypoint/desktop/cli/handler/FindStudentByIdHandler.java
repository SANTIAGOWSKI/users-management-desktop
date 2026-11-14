package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.StudentNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.StudentResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.StudentController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FindStudentByIdHandler implements OperationHandler {

  private final StudentController studentController;
  private final ConsoleIO console;
  private final StudentResponsePrinter printer;

  @Override
  public void handle() {
    final String id = console.readRequired("Student ID: ");

    try {
      final StudentResponse student = studentController.findStudentById(id);
      printer.print(student);
    } catch (final StudentNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}

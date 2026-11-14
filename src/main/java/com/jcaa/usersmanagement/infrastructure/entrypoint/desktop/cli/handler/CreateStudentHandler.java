package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.StudentAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.StudentResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.StudentController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateStudentRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateStudentHandler implements OperationHandler {

  private final StudentController studentController;
  private final ConsoleIO console;
  private final StudentResponsePrinter printer;

  @Override
  public void handle() {
    final String id = console.readRequired("Student ID (matricula): ");
    final String name = console.readRequired("Name                 : ");
    final String group = console.readRequired("Group                : ");

    try {
      final StudentResponse created =
          studentController.createStudent(new CreateStudentRequest(id, name, group));
      console.println("\n  Student created successfully.");
      printer.print(created);
    } catch (final StudentAlreadyExistsException exception) {
      console.println("  Error: " + exception.getMessage());
    }
  }
}

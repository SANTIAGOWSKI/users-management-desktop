package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.StudentResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.StudentController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListStudentsHandler implements OperationHandler {

  private final StudentController studentController;
  private final StudentResponsePrinter printer;

  @Override
  public void handle() {
    final List<StudentResponse> students = studentController.listAllStudents();
    printer.printList(students);
  }
}

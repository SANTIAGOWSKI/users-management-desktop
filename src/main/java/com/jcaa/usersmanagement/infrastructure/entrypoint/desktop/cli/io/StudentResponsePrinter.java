package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.StudentResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class StudentResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-10s : %s%n";

  private final ConsoleIO console;

  public void print(final StudentResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID", response.id());
    console.printf(ROW_FORMAT, "Name", response.name());
    console.printf(ROW_FORMAT, "Group", response.group());
    console.println(SEPARATOR);
  }

  public void printList(final List<StudentResponse> students) {
    if (students.isEmpty()) {
      console.println("  No students found.");
      return;
    }
    console.printf("%n  Total: %d student(s)%n", students.size());
    students.forEach(this::print);
  }
}

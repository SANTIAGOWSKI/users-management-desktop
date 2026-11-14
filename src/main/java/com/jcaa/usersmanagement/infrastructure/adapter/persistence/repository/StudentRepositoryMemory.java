package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteStudentPort;
import com.jcaa.usersmanagement.application.port.out.GetAllStudentsPort;
import com.jcaa.usersmanagement.application.port.out.GetStudentByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveStudentPort;
import com.jcaa.usersmanagement.application.port.out.UpdateStudentPort;
import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class StudentRepositoryMemory
    implements SaveStudentPort,
        GetStudentByIdPort,
        UpdateStudentPort,
        DeleteStudentPort,
        GetAllStudentsPort {

  private final Map<StudentId, StudentModel> students = new LinkedHashMap<>();

  @Override
  public StudentModel save(final StudentModel student) {
    students.put(student.getId(), student);
    return student;
  }

  @Override
  public Optional<StudentModel> getById(final StudentId studentId) {
    return Optional.ofNullable(students.get(studentId));
  }

  @Override
  public StudentModel update(final StudentModel student) {
    students.put(student.getId(), student);
    return student;
  }

  @Override
  public void delete(final StudentId studentId) {
    students.remove(studentId);
  }

  @Override
  public List<StudentModel> getAll() {
    return new ArrayList<>(students.values());
  }
}

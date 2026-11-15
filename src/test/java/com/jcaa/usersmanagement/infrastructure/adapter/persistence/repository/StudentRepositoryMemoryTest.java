package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.domain.model.StudentModel;
import com.jcaa.usersmanagement.domain.valueobject.StudentGroup;
import com.jcaa.usersmanagement.domain.valueobject.StudentId;
import com.jcaa.usersmanagement.domain.valueobject.StudentName;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StudentRepositoryMemory")
class StudentRepositoryMemoryTest {

  private StudentRepositoryMemory repository;

  @BeforeEach
  void setUp() {
    repository = new StudentRepositoryMemory();
  }

  private static StudentModel student(final String id, final String name, final String group) {
    return StudentModel.create(new StudentId(id), new StudentName(name), new StudentGroup(group));
  }

  @Test
  @DisplayName("save() stores the student and getById() finds it")
  void shouldSaveAndFindStudent() {
    final StudentModel laura = student("1", "Laura Martinez", "G1");

    repository.save(laura);

    assertEquals(laura, repository.getById(new StudentId("1")).orElseThrow());
  }

  @Test
  @DisplayName("getById() returns empty when the student does not exist")
  void shouldReturnEmptyWhenStudentDoesNotExist() {
    assertTrue(repository.getById(new StudentId("404")).isEmpty());
  }

  @Test
  @DisplayName("update() replaces the stored student")
  void shouldUpdateStudent() {
    repository.save(student("1", "Laura Martinez", "G1"));
    final StudentModel changed = student("1", "Laura Gomez", "G2");

    repository.update(changed);

    assertEquals(changed, repository.getById(new StudentId("1")).orElseThrow());
  }

  @Test
  @DisplayName("delete() removes the student")
  void shouldDeleteStudent() {
    repository.save(student("1", "Laura Martinez", "G1"));

    repository.delete(new StudentId("1"));

    assertTrue(repository.getById(new StudentId("1")).isEmpty());
  }

  @Test
  @DisplayName("getAll() lists every stored student")
  void shouldListAllStudents() {
    final StudentModel laura = student("1", "Laura Martinez", "G1");
    final StudentModel carlos = student("2", "Carlos Perez", "G1");
    repository.save(laura);
    repository.save(carlos);

    final List<StudentModel> all = repository.getAll();

    assertEquals(List.of(laura, carlos), all);
  }
}

package com.example.repository;

import com.example.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByIdAsc();

    Optional<Task> findTaskByContent(final String content);

    Optional<Task> findTaskByContentAndIdIsNot(final String content, long id);

}

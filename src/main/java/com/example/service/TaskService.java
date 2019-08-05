package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByIdAsc();
    }

    public Task createTask(final Task task) {
        taskRepository.findTaskByContent(task.getContent())
                .ifPresent(smth -> {
                    throw new IllegalArgumentException("Task with content '" + smth.getContent() + "' already exists!");
                });

        return taskRepository.save(task);
    }

    public Task updateTask(final Long taskId, final Task taskDetails) {
        taskRepository.findTaskByContentAndIdIsNot(taskDetails.getContent(), taskId)
                .ifPresent(smth -> {
                    throw new IllegalArgumentException("Task with content '" + smth.getContent() + "' already exists!");
                });

        return taskRepository.findById(taskId)
                .map(oldTask -> {
                    oldTask.setContent(taskDetails.getContent());
                    oldTask.setPriority(taskDetails.getPriority());
                    oldTask.setCreationTime(taskDetails.getCreationTime());
                    oldTask.setRemoved(taskDetails.isRemoved());
                    return taskRepository.save(oldTask);
                }).orElseThrow(() -> new ResourceNotFoundException("Task with id = " + taskId + " not found."));
    }

    public void deleteTask(final Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id = " + taskId + " not found."));

        taskRepository.delete(task);
    }
}

package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.model.dto.TaskDTO;
import com.example.model.mapper.TaskMapper;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskDTO> getAllTasks() {
        return TaskMapper.INSTANCE.taskToTaskDTO(taskRepository.findAllByOrderByIdAsc());
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + userId + " not found."));
    }

    private TaskDTO saveTask(TaskDTO taskDTO) {
        return TaskMapper.INSTANCE.taskToTaskDTO(
                taskRepository.save(
                        TaskMapper.INSTANCE.taskDTOToTask(
                                taskDTO,
                                findUserById(taskDTO.getUserId())
                        )));
    }

    public TaskDTO createTask(final TaskDTO taskDTO) {
        taskRepository.findTaskByContent(taskDTO.getContent())
                .ifPresent(smth -> {
                    throw new IllegalArgumentException("Task with content '" + smth.getContent() + "' already exists!");
                });

        return saveTask(taskDTO);
    }

    public TaskDTO updateTask(final TaskDTO taskDTO) {
        taskRepository.findTaskByContentAndIdIsNot(taskDTO.getContent(), taskDTO.getId())
                .ifPresent(smth -> {
                    throw new IllegalArgumentException("Task with content '" + smth.getContent() + "' already exists!");
                });

        return saveTask(taskDTO);
    }

    public void deleteTask(final Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id = " + taskId + " not found."));

        taskRepository.deleteById(taskId);
    }
}

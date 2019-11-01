package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.controller.dto.TaskDTO;
import com.example.controller.dto.TaskFilterRequest;
import com.example.controller.mapper.TaskMapper;
import com.example.model.specification.TaskSpecification;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final UserRepository userRepository;

    public List<TaskDTO> getAllTasks(final TaskFilterRequest request, final Sort sort) {
            return taskMapper.taskToTaskDTO(
                taskRepository.findAll(TaskSpecification.getSpecification(request), sort));
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + userId + " not found."));
    }

    private TaskDTO saveTask(TaskDTO taskDTO) {
        return taskMapper.taskToTaskDTO(
                taskRepository.save(
                        taskMapper.taskDTOToTask(
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

        return saveTask(
                taskRepository.findById(taskDTO.getId())
                        .map(task -> {
                            taskDTO.setCreationTime(task.getCreationTime());
                            return taskDTO;
                        })
                        .orElseThrow(
                                () -> new IllegalArgumentException("Task with id " + taskDTO.getId() + " not found.")));
    }

    public void deleteTask(final Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id = " + taskId + " not found."));

        taskRepository.deleteById(taskId);
    }
}

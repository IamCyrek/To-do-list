package com.example.controller;

import com.example.controller.dto.TaskDTO;
import com.example.controller.dto.TaskFilterRequest;
import com.example.service.TaskService;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.example.configuration.Constants.API_TASKS;

@RestController
@RequestMapping(API_TASKS)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks(@Valid TaskFilterRequest request, @SortDefault(sort = "id") Sort sort) {
        return taskService.getAllTasks(request, sort);
    }

    @PostMapping
    public TaskDTO createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @PutMapping
    public TaskDTO updateTask(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.updateTask(taskDTO);
    }

    @DeleteMapping
    public Map<String, Boolean> deleteTask(@RequestParam(value = "id") Long taskId) {
        taskService.deleteTask(taskId);

        return ControllerUtils.responseBuilder("deleted", Boolean.TRUE);
    }
}

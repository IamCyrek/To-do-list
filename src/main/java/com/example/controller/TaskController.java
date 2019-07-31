package com.example.controller;

import com.example.model.Task;

import com.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping
    public Task updateTask(@RequestParam(value = "id") Long taskId,
                           @Valid @RequestBody Task taskDetails) {
        return taskService.updateTask(taskId, taskDetails);
    }

    @DeleteMapping
    public Map<String, Boolean> deleteTask(@RequestParam(value = "id") Long taskId) {
        taskService.deleteTask(taskId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}

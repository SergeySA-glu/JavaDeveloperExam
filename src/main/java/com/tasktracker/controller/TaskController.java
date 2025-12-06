package com.tasktracker.controller;

import com.tasktracker.model.dto.TaskDTO;
import com.tasktracker.model.entity.Task;
import com.tasktracker.model.entity.TaskStatus;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.service.TaskService;
import com.tasktracker.model.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Задачи", description = "Управление задачами пользователя")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByEmail(email).orElseThrow();
    }

    @GetMapping
    @Operation(summary = "Получить все задачи текущего пользователя")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        User user = getCurrentUser();
        List<TaskDTO> tasks = taskService.getUserTasks(user.getId());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @Operation(summary = "Создать задачу для текущего пользователя")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO dto) {
        User user = getCurrentUser();
        Task task = taskService.createTask(dto, user);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу у текущего пользователя")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO dto) {
        User user = getCurrentUser();
        Task task = taskService.updateTask(id, dto, user);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу у текущего пользователя")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        User user = getCurrentUser();
        taskService.deleteTask(id, user);
        return ResponseEntity.ok("Task deleted");
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Обновить статус задачи у текущего пользователя")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        User user = getCurrentUser();
        TaskStatus status = TaskStatus.valueOf(request.get("status"));
        Task task = taskService.updateTaskStatus(id, status, user);
        return ResponseEntity.ok(task);
    }
}

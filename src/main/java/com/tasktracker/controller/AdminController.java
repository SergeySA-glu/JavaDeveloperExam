package com.tasktracker.controller;

import com.tasktracker.model.dto.TaskDTO;
import com.tasktracker.model.entity.Task;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.service.TaskService;
import com.tasktracker.model.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Админ", description = "Эндпойнты только для админа")
public class AdminController {

    @Autowired
    private UserService userService;

    private TaskService taskService;

    @GetMapping("/users")
    @Operation(summary = "Получить всех пользователей")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/tasks")
    @Operation(summary = "Получить все задачи")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/users/{userId}/tasks")
    @Operation(summary = "Получить все задачи по id пользователя")
    public ResponseEntity<?> getUserTasks(@PathVariable Long userId){
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<TaskDTO> tasks = taskService.getUserTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    public ResponseEntity<?> getStatistics(){
        Map<String, Object> stats = Map.of(
                "tasksByStatus", taskService.getAllTasksStatistics(),
                "totalTasks", taskService.getAllTasks()
        );
        return ResponseEntity.ok(stats);
    }
}

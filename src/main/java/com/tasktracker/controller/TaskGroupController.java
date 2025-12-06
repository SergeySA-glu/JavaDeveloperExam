package com.tasktracker.controller;

import com.tasktracker.model.dto.TaskGroupDTO;
import com.tasktracker.model.entity.TaskGroup;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.service.TaskGroupService;
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
@RequestMapping("/api/task-groups")
@Tag(name = "Группы задач", description = "Управление группами задач пользователей")
public class TaskGroupController {

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private UserService userService;

    private User getCurrentUser(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByEmail(email).orElseThrow();
    }

    @GetMapping
    @Operation(summary = "Получить группы задач текущего пользователя")
    public ResponseEntity<List<TaskGroupDTO>> getGroups() {
        User user = getCurrentUser();
        List<TaskGroupDTO> groups = taskGroupService.getUserGroups(user.getId());
        return ResponseEntity.ok(groups);
    }

    @PostMapping
    @Operation(summary = "Создать новую группу задач для текущего пользователя")
    public ResponseEntity<?> createGroup(@RequestBody Map<String, String> request) {
        User user = getCurrentUser();
        TaskGroup group = taskGroupService.createGroup(request.get("name"), user);
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Переименовать группу текщего пользователя по id группы")
    public ResponseEntity<?> updateGroup(@PathVariable Long id, @RequestBody Map<String, String> request) {
        User user = getCurrentUser();
        TaskGroup group = taskGroupService.updateGroup(id, request.get("name"), user);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить группу текщего пользователя по id")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        User user = getCurrentUser();
        taskGroupService.deleteGroup(id, user);
        return ResponseEntity.ok("Группа удалена");
    }
}

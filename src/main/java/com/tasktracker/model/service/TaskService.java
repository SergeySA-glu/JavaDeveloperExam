package com.tasktracker.model.service;

import com.tasktracker.model.dto.TaskDTO;
import com.tasktracker.model.entity.Task;
import com.tasktracker.model.entity.TaskStatus;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.repository.TaskRepository;
import com.tasktracker.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(TaskDTO dto, User user){
        Task task = Task.builder()
                .user(user)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TaskStatus.PLANNED)
                .build();
        return taskRepository.save(task);
    }

    public List<TaskDTO> getUserTasks(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Task updateTask(Long taskId, TaskDTO dto, User user){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getStatus() != null){
            task.setStatus(dto.getStatus());
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, User user){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        taskRepository.delete(task);
    }

    public Task updateTaskStatus(Long taskId, TaskStatus status, User user){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public List<Map<String, Object>> getAllTasksStatistics() {
        return taskRepository.getAllTasksStatistics();
    }

    public List<Map<String, Object>> getTaskStatistics(Long userId){
        return taskRepository.getTaskStatisticByUser(userId);
    }

    private TaskDTO toDTO(Task task){
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getTaskGroup() != null ? task.getTaskGroup().getId() : null,
                task.getCreatedAt().toString(),
                task.getUpdatedAt().toString()
        );
    }
}

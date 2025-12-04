package com.tasktracker.model.service;

import com.tasktracker.model.dto.TaskGroupDTO;
import com.tasktracker.model.entity.TaskGroup;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.repository.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    public TaskGroup createGroup(String name, User user) {
        TaskGroup group = TaskGroup.builder()
                .name(name)
                .user(user)
                .build();
        return taskGroupRepository.save(group);
    }

    public List<TaskGroupDTO> getUserGroups(Long userId){
        return taskGroupRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskGroup updateGroup(Long groupId, String name, User user){
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }

        group.setName(name);
        return taskGroupRepository.save(group);
    }

    public void deleteGroup(Long groupId, User user) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }

        taskGroupRepository.delete(group);
    }

    private TaskGroupDTO toDTO(TaskGroup group){
        return new TaskGroupDTO(
                group.getId(),
                group.getName(),
                group.getCreatedAt().toString()
        );
    }
}

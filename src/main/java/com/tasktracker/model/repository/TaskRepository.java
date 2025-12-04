package com.tasktracker.model.repository;

import com.tasktracker.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndTaskGroupId(Long userId, Long groupId);

    @Query("SELECT NEW map(t.status as status, COUNT(t) as count) " +
            "FROM Task t WHERE t.user.id = :userId GROUP BY t.status")
    List<Map<String, Object>> getTaskStatisticByUser(@Param("userId") Long userId);

    @Query("SELECT NEW map(t.status as status, COUNT(t) as count) " +
            "FROM Task t GROUP BY t.status")
    List<Map<String, Object>> getAllTasksStatistics();

    @Query("SELECT NEW map(g.name as groupName, COUNT(t) as count) " +
            "FROM Task t LEFT JOIN t.taskGroup g " +
            "WHERE t.user.id = :userId GROUP BY g.name, g.id")
    List<Map<String, Object>> getTaskStatisticsByGroups(@Param("userId") Long userId);

    @Query(value="SELECT t.id, t.title, t.status, u.email FROM tasks t " +
            "JOIN users u ON t.user_id=u.id " +
            "ORDER BY t.created_at DESC", nativeQuery=true)
    List<Map<String, Object>> getAllTasksWithUserInfo();
}

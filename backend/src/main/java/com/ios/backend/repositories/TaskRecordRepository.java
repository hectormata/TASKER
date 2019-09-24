package main.java.com.ios.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.java.com.ios.backend.entities.TaskRecord;

@Repository
public interface TaskRecordRepository extends CrudRepository<TaskRecord, Long> {

  @Query("SELECT t FROM TaskRecord t WHERE t.user=:id")
  List<TaskRecord> getTaskRecordByUser(@Param("id") long user);
  
  List<TaskRecord> findByProgramAndUser(long program, long user);
  
  TaskRecord findByProgramAndUserAndTask(long program, long user, long task);
}

package main.java.com.ios.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.java.com.ios.backend.entities.Task;

@Repository
public interface TaskRespository extends CrudRepository<Task, Long> {
  List<Task> findByProgram(long program);
}

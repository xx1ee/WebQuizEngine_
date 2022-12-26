package engine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<engine.models.Task, Long> {
    engine.models.Task findTaskById(Long id);
}

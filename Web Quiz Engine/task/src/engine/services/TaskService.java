package engine.services;

import engine.models.Task;
import engine.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository userRepository) {
        this.taskRepository = userRepository;
    }

    public Task findTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
    public ArrayList<Task> findAll() {
        return (ArrayList<Task>) taskRepository.findAll();
    }
    public Task save(Task toSave) {
        return taskRepository.save(toSave);
    }
    public Long count() {
        return taskRepository.count();
    }
}

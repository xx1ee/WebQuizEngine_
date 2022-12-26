package engine.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import engine.exeptions.IdNotFoundException;
import engine.exeptions.IsNotTheAuthorException;
import engine.models.Answer;
import engine.models.Task;
import engine.models.User;
import engine.repositories.UserRepository;
import engine.services.TaskService;
import engine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;

    @GetMapping("quizzes/{id}")
    public Task returnTask(@PathVariable Long id) {
        engine.models.Task quizById = null;
        quizById = taskService.findTaskById(id);
        if (quizById == null)
            throw new IdNotFoundException();
        return quizById;
    }

    @GetMapping("quizzes")
    public ArrayList<Task> returnTasks() throws JsonProcessingException {
        return taskService.findAll();
    }

    @PostMapping("quizzes")
    public Task postAddress(@RequestBody @Valid @NotNull Task answer, @AuthenticationPrincipal User user) throws JsonProcessingException {
        answer.setId(taskService.count());
        Task createdTask = taskService.save(new engine.models.Task(
                answer.getId(), answer.getTitle(),
                answer.getText(), answer.getOptions(), answer.getAnswer()));
        createdTask.setUser(user);
        return createdTask;
    }
    @DeleteMapping("quizzes/{id}")
    public HttpStatus deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (taskService.findTaskById((long) (id)) == null) {
            throw new IdNotFoundException();
        }
        engine.models.Task quizById = taskService.findTaskById(id);
        var user2 = userService.loadUserByUsername(quizById.getUser().getEmail());
        if (user.getUsername().equals(user2.getUsername())) {
            taskService.deleteTaskById(id);
            return HttpStatus.NO_CONTENT;
        } else {
            throw new IsNotTheAuthorException();
        }
    }

    @PostMapping("quizzes/{id}/solve")
    public Answer taskSolve(@PathVariable Long id, @RequestBody engine.models.taskSolve answer) throws JsonProcessingException {
        if (taskService.findTaskById((long) (id)) == null) {
            throw new IdNotFoundException();
        }
        if (answer.getAnswer() == null) {
            answer.setAnswer(List.of());
        }
        if (taskService.findTaskById((long) (id)).getAnswer() == null) {
            taskService.findTaskById((long) (id)).setAnswer(List.of());
        }
            if ((answer.getAnswer().equals(taskService.findTaskById((long) (id)).getAnswer()))) {
                return new Answer(true, "Congratulations, you're right!");
            } else return new Answer(false, "Wrong answer! Please, try again.");

    }
    @PostMapping(path = "register")
    public void register(@Valid @RequestBody User user) {
        userService.registerNewUser(user.getEmail(), user.getPassword());
    }
}

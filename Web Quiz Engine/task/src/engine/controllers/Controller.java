package engine.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import engine.models.*;
import engine.repos.CompletedRepository;
import engine.repos.TaskRepository;
import engine.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompletedRepository completedRepository;
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("register")
    public void registerUser(@RequestBody @Valid @NotNull User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @PostMapping("quizzes")
    public Task postAddress(@RequestBody @Valid @NotNull Task answer, Principal user) throws JsonProcessingException {
        User user1 = userRepository.findUserByEmail(user.getName());
        Task createdTask = taskRepository.save(new Task(
                answer.getId(), answer.getTitle(),
                answer.getText(), answer.getOptions(), answer.getAnswer(), user1));
        return createdTask;
    }

    @GetMapping("quizzes/{id}")
    public Task getQuiz(@PathVariable long id) {
        var quizById = taskRepository.findById(id);
        if (quizById.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return quizById.get();
    }

    @GetMapping("quizzes")
    public Page<Task> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return taskRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }
    @GetMapping("quizzes/completed")
    public Page<CompletedResponce> getCompletedQuizzes(Principal user, @RequestParam(defaultValue = "0")  Integer page) {
        Pageable paging = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "completedAt"));
        return completedRepository.findByEmail(user.getName(), paging).map(Controller :: convertCompletionToDto);
    }
    public static CompletedResponce convertCompletionToDto(Completed completion) {
        CompletedResponce completionDTO = new CompletedResponce();
        completionDTO.setQuizId(completion.getQuiz().getId());
        completionDTO.setCompletedAt(completion.getCompletedAt());
        return completionDTO;
    }
    @PostMapping("quizzes/{id}/solve")
    public Answer taskSolve(@PathVariable Long id, @RequestBody taskSolve answer, Principal user) throws JsonProcessingException {
        if (taskRepository.findById((long) (id)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (answer.getAnswer() == null) {
            answer.setAnswer(List.of());
        }
        if (taskRepository.findById((long) (id)).get().getAnswer() == null) {
            taskRepository.findById((long) (id)).get().setAnswer(List.of());
        }
        User user1 = userRepository.findUserByEmail(user.getName());
        if ((answer.getAnswer().equals(taskRepository.findById((long) (id)).get().getAnswer()))) {
            completedRepository.save(Completed.createCompletion(user1, taskRepository.findById((long) (id)).get()));
            return new Answer(true, "Congratulations, you're right!");
        } else return new Answer(false, "Wrong answer! Please, try again.");

    }

    @DeleteMapping("quizzes/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Task quizById;
            try {
                quizById = taskRepository.findById(id).get();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            try {
                if (user.getEmail().equals(quizById.getUser().getEmail())) {
                    taskRepository.delete(quizById);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
    }
}

package engine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Entity
@Table
public class Task {
    @Id
    @Column
    private Long id;
    @Column
    @NotBlank
    private String title;
    @Column
    @NotBlank
    private String text;
    @Column
    @NotNull
    @Size(min = 2)
    @ElementCollection
    @CollectionTable(name = "options", joinColumns = @JoinColumn(name = "task_id"))
    private List<String> options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ElementCollection
    @CollectionTable(name = "answer", joinColumns = @JoinColumn(name = "task_id"))
    private List<Integer> answer = null;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    //Adding the name
    @JoinColumn(name = "user_id")
    private User user;

    public Task(){};
    public Task(Long id, String title, String text, List<String> options, List<Integer> answer) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer == null ? List.of() : answer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public User getUser() {
        return this.user;
    }

    public List<Integer> getAnswer() {
        return this.answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

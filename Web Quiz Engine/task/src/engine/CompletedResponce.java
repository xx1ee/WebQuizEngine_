package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CompletedResponce {
    @JsonProperty("id")
    private Long quizId;
    private LocalDateTime completedAt;

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
}

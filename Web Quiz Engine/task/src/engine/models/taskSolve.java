package engine.models;

import java.util.List;

public class taskSolve {
    private List<Integer> answer = List.of();
    taskSolve(){};

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }
}

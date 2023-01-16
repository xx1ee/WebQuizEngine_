package engine;

public class Answer {
    private boolean success;
    private String feedback;

    public Answer(){};

    public Answer(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }
}

package Models;

public class SecurityQuestion {
    private String question, answer;

    public SecurityQuestion(String question, String answer){
        setQuestion(question);
        setAnswer(answer);
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

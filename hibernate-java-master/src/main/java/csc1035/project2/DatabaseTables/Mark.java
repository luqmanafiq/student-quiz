package csc1035.project2.DatabaseTables;

import javax.persistence.*;

/**
 * Marks class used to store a mark for questions in a specific quiz submission.
 */
@Entity
@Table(name = "tblMark")
@IdClass(MarkPK.class)
public class Mark {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SubmissionID", nullable = false)
    private QuizSubmission submissionID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question questionID;

    @Column(name = "Score", nullable = false)
    private Integer score;

    @Column(name = "UserAnswer", nullable = false)
    private String userAnswer;

    public Mark() {
    }

    public Mark(QuizSubmission submission, Question question, int score, String userAnswer) {
        this.submissionID = submission;
        this.questionID = question;
        this.score = score;
        this.userAnswer = userAnswer;
    }

    public QuizSubmission getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(QuizSubmission submissionID) {
        this.submissionID = submissionID;
    }

    public Question getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Question questionID) {
        this.questionID = questionID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
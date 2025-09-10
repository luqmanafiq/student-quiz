package csc1035.project2.DatabaseTables;

import javax.persistence.*;
import java.time.Instant;

/**
 * QuizSubmission identifies a submission of a quiz.
 */
@Entity
@Table(name = "tblQuizSubmission")
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SubmissionID", nullable = false)
    private Integer id;

    @Column(name = "DateOfSubmission", nullable = false)
    private Instant dateOfSubmission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Username", nullable = false)
    private User username;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuizID", nullable = false)
    private Quiz quizID;

    public QuizSubmission() {
    }

    public QuizSubmission(Instant dateOfSubmission, User username, Quiz quizID) {
        this.dateOfSubmission = dateOfSubmission;
        this.username = username;
        this.quizID = quizID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(Instant dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public Quiz getQuizID() {
        return quizID;
    }

    public void setQuizID(Quiz quizID) {
        this.quizID = quizID;
    }

}
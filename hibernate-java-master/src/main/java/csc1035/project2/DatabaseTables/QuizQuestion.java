package csc1035.project2.DatabaseTables;

import javax.persistence.*;

/**
 * QuizQuestion links a question to a specific quiz and places it in a specific order.
 */
@Entity
@Table(name = "tblQuizQuestion")
@IdClass(QuizQuestionPK.class)
public class QuizQuestion {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuizID", nullable = false)
    private Quiz quizID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question questionID;

    @Column(name = "OrderIndex", nullable = false)
    private int orderIndex;

    public QuizQuestion() {
    }

    public QuizQuestion(Quiz quiz, Question question, Integer orderIndex) {
        this.quizID = quiz;
        this.questionID = question;
        this.orderIndex = orderIndex;
    }

    public Quiz getQuizID() {
        return quizID;
    }

    public void setQuizID(Quiz quizID) {
        this.quizID = quizID;
    }

    public Question getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Question questionID) {
        this.questionID = questionID;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
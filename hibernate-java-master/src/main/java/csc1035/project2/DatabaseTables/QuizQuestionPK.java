package csc1035.project2.DatabaseTables;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for the QuizQuestion class; responsible for correctly hashing the primary key values.
 */
public class QuizQuestionPK implements Serializable {

    private int questionID;
    private int quizID;

    public QuizQuestionPK() {}

    public QuizQuestionPK(Question question, Quiz quiz) {
        this.questionID = question.getId();
        this.quizID = quiz.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizQuestionPK quizQuestionPK = (QuizQuestionPK) o;
        return Objects.equals(questionID, quizQuestionPK.questionID) &&
                Objects.equals(quizID, quizQuestionPK.quizID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionID, quizID);
    }

}
package csc1035.project2.DatabaseTables;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * MarkPK creates the composite key for the Mark class (table entity) between the submissionID and questionID.
 */
public class MarkPK implements Serializable {
    private Integer submissionID;
    private Integer questionID;

    public MarkPK() {}

    public MarkPK(Question question, QuizSubmission submission) {
        this.questionID = question.getId();
        this.submissionID = submission.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkPK markPK = (MarkPK) o;
        return Objects.equals(submissionID, markPK.submissionID) &&
                Objects.equals(questionID, markPK.questionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionID, submissionID);
    }

}
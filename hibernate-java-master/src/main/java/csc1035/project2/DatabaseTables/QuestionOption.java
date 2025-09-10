package csc1035.project2.DatabaseTables;

import javax.persistence.*;

/**
 * QuestionOption representing an option for a question if it is multiple choice.
 */
@Entity
@Table(name = "tblQuestionOption")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestionOptionID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question questionID;

    @Column(name = "QuestionOption", nullable = false)
    private String questionOption;

    public QuestionOption() {
    }

    public QuestionOption(Question questionID, String questionOption) {
        this.questionID = questionID;
        this.questionOption = questionOption;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Question getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Question questionID) {
        this.questionID = questionID;
    }

    public String getQuestionOption() {
        return questionOption;
    }

    public void setQuestionOption(String questionOption) {
        this.questionOption = questionOption;
    }

}
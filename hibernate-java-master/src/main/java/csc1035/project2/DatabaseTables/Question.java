package csc1035.project2.DatabaseTables;

import csc1035.project2.QuestionMarkTuple;

import javax.persistence.*;

/**
 * Question class used to respresent a question in the database. Can be used in quizzes.
 */
@Entity
@Table(name = "tblQuestion")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestionID", nullable = false)
    private Integer id;

    @Column(name = "Question", nullable = false)
    private String question;

    @Column(name = "Answer", nullable = false)
    private String answer;

    @Column(name = "MaximumMarks", nullable = false)
    private Integer maximumMarks;

    @Column(name = "QuestionType", nullable = false, length = 15)
    private String questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TopicName")
    private Topic topicName;

    public Question() {
    }

    public Question(String question, String answer, Integer maximumMarks, String questionType, Topic topicName) {
        this.question = question;
        this.answer = answer;
        this.maximumMarks = maximumMarks;
        this.questionType = questionType;
        this.topicName = topicName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getMaximumMarks() {
        return maximumMarks;
    }

    public void setMaximumMarks(Integer maximumMarks) {
        this.maximumMarks = maximumMarks;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Topic getTopicName() {
        return topicName;
    }

    public void setTopicName(Topic topicName) {
        this.topicName = topicName;
    }

    // checks if answer is correct; 0 is returned for incorrect; maximumMark of the question is returned for correct
    public int returnMark (String answer) {
        if (this.getAnswer().toLowerCase().equals(answer.toLowerCase())) { // case insensitive
            return this.getMaximumMarks();
        } else {
            return 0;
        }
    }
}
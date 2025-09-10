package csc1035.project2;

import csc1035.project2.DatabaseTables.Question;

/**
 * Class used to represent a question, the mark the user got for the question and the users answer to that question.
 * Instantiated objects of this class are used when submitting quizzes to the database.
 */
public class QuestionMarkTuple {
    private Question _question;
    private int _marksReceived;
    private String _userAnswer;

    /**
     * Constructor for creating an instance of the QuestionMarkTuple.
     * @param question The question answered.
     * @param marksReceived The mark the user received when answering the question.
     * @param userAnswer The user's answer to the question.
     */
    public QuestionMarkTuple(Question question, int marksReceived, String userAnswer) {
        this._question = question;
        this._marksReceived = marksReceived;
        this._userAnswer = userAnswer;
    }

    public Question getQuestion() {
        return _question;
    }

    public void setQuestion(Question _question) {
        this._question = _question;
    }

    public int getMarksReceived() {
        return _marksReceived;
    }

    public void setMarksReceived(int _marksReceived) {
        this._marksReceived = _marksReceived;
    }

    public String get_userAnswer() {
        return _userAnswer;
    }

    public void set_userAnswer(String _userAnswer) {
        this._userAnswer = _userAnswer;
    }
}

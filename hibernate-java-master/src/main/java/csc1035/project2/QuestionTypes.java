package csc1035.project2;

import java.util.ArrayList;

import csc1035.project2.DatabaseTables.Question;


class MCQ extends Question {
    private ArrayList<String> optionList = new ArrayList<>();

    public ArrayList <String> getOptionList() {
        return this.optionList;
    }
    public void setOptionList(ArrayList<String> questionList) {
        this.optionList = questionList;
    }
    public void createQuestionOption(String option) {
        this.optionList.add(option);
    }
    public String readQuestionOption(int index) {
        return this.optionList.get(index);
    }
    public void deleteQuestionOption(int index) {
        this.optionList.remove(index);
    }
    public void deleteAllQuestionOptions() {
        this.optionList.clear();
    }
    public void updateQuestionOption(int index, String option) {
        this.optionList.set(index, option);
    }
}
class SAQ extends Question {

}
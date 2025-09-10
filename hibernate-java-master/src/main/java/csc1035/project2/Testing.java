package csc1035.project2;

import csc1035.project2.DatabaseTables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for testing the database (subsequently DatabaseIO class) and adding sample data to the database.
 *
 * PLEASE remove any records from the database before running this file.
 */
public class Testing {
    private static void AddUsers() {
        System.out.println("\nTest to see if users can be added to the database. The users should only be added to the " +
                "database if they have a unique name (case-insensitve).\n");
        String[] users = new String[]{"pam","jim","michael","dwight","angela","creed","kevin","kelly","oscar", "oScAr"};
        for(String username: users){
            System.out.println(String.format("Add user to database test: adding user '%s'. Result: '%s'",
                    username, DatabaseIO.addUser(username) != null ? "Successfully added" : "Failed to add"));
        }
    }

    private static void AddTopics() {
        System.out.println("\nTest to see if topics can be added to the database. The topics should only be added to the " +
                "database if they have a unique name (case-insensitve).\n");
        String[] topics = new String[]{"Biology", "Computer Science", "History", "Math", "English", "EnGliSh"};
        for(String topic: topics) {
            System.out.println(String.format("Add topic to database test: adding topic '%s'. Result: '%s'",
                    topic, DatabaseIO.addTopic(new Topic(topic, "dummy description for testing."))
                            != null ? "Successfully added" : "Failed to add"));
        }
    }

    private static void AddQuizzes() {
        System.out.println("\nTest to see if quizzes can be added to the database. The quizzes should only be added to the " +
                "database if the same user has not already created a quiz with the same name (a quiz must have " +
                "a unique user and quizName pair).\n");
        String[] quizNames = new String[]{"Quiz 1", "Quiz 2", "Quiz 3", "Quiz 3"};
        List<User> users = DatabaseIO.getAllUsers();
        List<Quiz> addedQuizzes = new ArrayList<>();
        for(String quizName: quizNames) {
            User u = users.get(new Random().nextInt(users.size()));
            Quiz addedQuiz = DatabaseIO.addQuiz(new Quiz(u, quizName));
            System.out.println(String.format("Add quiz to database test: adding quiz '%s' for user '%s'. Result: '%s'",
                    quizName, u.getUsername(), addedQuiz != null ? "Successfully added" : "Failed to add"));
            if(addedQuiz != null) {
                addedQuizzes.add(addedQuiz);
            }
        }
    }

    private static void AddSCQ() {
        System.out.println("\nTest to see if SCQ's (single choice questions) can be added to the database.\n");
        List<Question> questions = new ArrayList<>();
        List<Topic> topics = DatabaseIO.getAllTopics();
        questions.add(new Question("What is the capital of France?", "Paris", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the highest mountain in the world?", "Mount Everest", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("Who is the current president of the United States?", "Joe Biden", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the symbol for sodium in the periodic table?", "Na", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the largest mammal in the world?", "Blue whale", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the boiling point of water in Celsius?", "100", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the currency of Japan?", "Yen", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the highest grossing movie of all time?", "Avatar", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the name of the longest river in South America?", "Amazon River", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        questions.add(new Question("What is the speed of light in meters per second?", "299792458", 1, "SAQ", topics.get(new Random().nextInt(topics.size()))));
        for(var question: questions) {
            System.out.println(String.format("Add single choice questions to database test: " +
                    "question: '%s', answer: '%s', marks: %s, questionType: '%s', topic: '%s', RESPONSE: '%s'",
                    question.getQuestion(), question.getAnswer(), question.getMaximumMarks(), question.getQuestionType(),
                    question.getTopicName().getId(), DatabaseIO.addQuestion(question) != null ? "Successfully added" :
                            "Failed to add"));
        }
    }

    private static void AddMCQWithOptions() {
        System.out.println("\nTest to see if MCQs (multiple choice questions) and their respective question options " +
                "can be added to the database. MCQ should only be added to the database if they belong to a valid " +
                "quiz and the question options for the MCQ should only be added if the MCQ question object exists " +
                "in the database and the question type field is set to 'MCQ'.\n");
        List<Topic> topics = DatabaseIO.getAllTopics();
        List<QuestionOption> questionOptions = new ArrayList<>();
        Question q = DatabaseIO.addQuestion(new Question("What is the capital of France?", "Paris", 1, "MCQ", topics.get(new Random().nextInt(topics.size()))));
        System.out.println(String.format("Add multiple choice questions to database test: " +
                        "question: '%s', answer: '%s', marks: %s, questionType: '%s', topic: '%s', RESPONSE: '%s'",
                q.getQuestion(), q.getAnswer(), q.getMaximumMarks(), q.getQuestionType(),
                q.getTopicName().getId(), q != null ? "Successfully added" :
                        "Failed to add"));
        if(q != null) {
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Paris")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "London")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Rome")));
        }
        q = DatabaseIO.addQuestion(new Question("What is the largest organ in the human body?", "Skin", 1, "MCQ", topics.get(new Random().nextInt(topics.size()))));
        System.out.println(String.format("Add multiple choice questions to database test: " +
                        "question: '%s', answer: '%s', marks: %s, questionType: '%s', topic: '%s', RESPONSE: '%s'",
                q.getQuestion(), q.getAnswer(), q.getMaximumMarks(), q.getQuestionType(),
                q.getTopicName().getId(), q != null ? "Successfully added" :
                        "Failed to add"));
        if(q != null) {
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Skin")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Liver")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Brain")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Heart")));
        }

        q = DatabaseIO.addQuestion(new Question("Which planet in our solar system is closest to the sun?", "Mercury", 1, "MCQ", topics.get(new Random().nextInt(topics.size()))));
        System.out.println(String.format("Add multiple choice questions to database test: " +
                        "question: '%s', answer: '%s', marks: %s, questionType: '%s', topic: '%s', RESPONSE: '%s'",
                q.getQuestion(), q.getAnswer(), q.getMaximumMarks(), q.getQuestionType(),
                q.getTopicName().getId(), q != null ? "Successfully added" :
                        "Failed to add"));
        if(q != null) {
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Mars")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Mercury")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Venus")));
            questionOptions.add(DatabaseIO.addQuestionOption(new QuestionOption(q, "Jupiter")));
        }
        for(QuestionOption qo: questionOptions) {
            System.out.println(String.format("Add question option to database test: questionID: '%s'" +
                    ", value: '%s', RESPONSE: '%s'", qo.getQuestionID().getId(), qo.getQuestionOption(), qo != null ?
                    "Successfully added" : "Failed to add"));
        }
    }

    private static void AddQuizQuestions() {
        System.out.println("\nTest to see if quiz questions can be added to the database. The quiz questions should " +
                "only be added to the database if the quiz and question exists and the quiz question has not " +
                "already been added to the quiz (can't add a question to the quiz more than once).\n");
        List<Quiz> quizzes = DatabaseIO.getAllQuizzes();
        List<Question> questions = DatabaseIO.getAllQuestions();
        for(Quiz quiz: quizzes) {
            for(int i = 0; i < 3; i++) {
                Question randQuestion = questions.get(new Random().nextInt(questions.size()));
                System.out.println(String.format("Add QuizQuestion to database test: Quiz: '%s', Question: '%s'," +
                                " Response: '%s'", quiz.getQuizName(), randQuestion.getQuestion(),
                        DatabaseIO.addQuizQuestion(new QuizQuestion(quiz, randQuestion, -1)) != null ?
                                "Successfully added" : "Failed to add"));
            }
        }
    }

    private static void AddQuizSubmissions() {
        System.out.println("\nTest to see if quiz submissions and marks can be added to the database. The quiz submission" +
                " should only be added to the database if they have a unique user and quiz. Marks should only be added " +
                "to the database and created if the question and answer are valid.\n");
        List<Quiz> allQuizzes = DatabaseIO.getAllQuizzes();
        List<User> allUsers = DatabaseIO.getAllUsers();
        Random rand = new Random();
        for(Quiz q: allQuizzes) {
            List<Question> questions = DatabaseIO.getQuestionsFromQuiz(q.getId());
            for(User u: allUsers) {
                System.out.println(String.format("\nDoing quiz '%s' for user '%s':\n", q.getQuizName(), u.getUsername()));
                List<QuestionMarkTuple> marksForQuiz = new ArrayList<>();
                for(Question qu: questions) {
                    String answer = "INCORRECT ANSWER SAMPLE";
                    if(rand.nextBoolean()) {answer = qu.getAnswer();}
                    QuestionMarkTuple tempQuestionMarkTuple = DatabaseIO.markQuestionAnswer(qu, answer);
                    marksForQuiz.add(tempQuestionMarkTuple);
                    System.out.println(String.format("Submitting answer '%s' for question '%s'. Correct answer was '%s'." +
                            " Mark received: '%s'. Max marks for that question: '%s'.", answer, qu.getQuestion(),
                            qu.getAnswer(), tempQuestionMarkTuple.getMarksReceived(), qu.getMaximumMarks()));
                }
                QuizSubmission tempQuizSubmission = DatabaseIO.submitQuizResults(marksForQuiz, u, q);
                System.out.println(String.format("Submitted quiz '%s' for user '%s'. RESPONSE: '%s'", q.getQuizName(),
                        u.getUsername(), tempQuizSubmission != null ? "Successfully added" : "Failed to add"));
            }
        }
    }

    /**
     * Populates the database with sample values.
     */
    public static void PopulateDatabase() {
        AddUsers();
        AddTopics();
        AddQuizzes();
        AddSCQ();
        AddMCQWithOptions();
        AddQuizQuestions();
        AddQuizSubmissions();
    }

    public static void main(String[] args) {
        PopulateDatabase();
    }
}

package csc1035.project2;

import java.time.Instant;
import java.util.*;

import csc1035.project2.DatabaseTables.*;

import javax.xml.crypto.Data;

public class UserIO {
    static Scanner scan = new Scanner(System.in);
    static User user;
    public static void main(String[] args) {
        user = promptUsername();
        while (true) {
            menu();
        }
    }
    
    private static User promptUsername() {
        User userToReturn = null;
        System.out.println("What is your username:");
        String username = stringValidInput();
        if(username.equalsIgnoreCase("sys")) {
            System.out.println("This user is reserved and can only be accessed by the system. Please choose another username.");
            return promptUsername();
        }
        if(DatabaseIO.checkUserExists(username.trim())) {
            return DatabaseIO.getUser(username.trim());
        }
        else {
            System.out.println("This user does not currently exist, would you like to create a new user with this name (Y)es or (N)o?");
            Boolean validAnswer = false;
            while (!validAnswer) {
                String userAnswer = scan.nextLine();
                if(userAnswer.equalsIgnoreCase("Y")) {
                    User addedUser = DatabaseIO.addUser(username.trim());
                    if(addedUser != null) {
                        System.out.println("Added user to the database!");
                        validAnswer = true;
                        userToReturn = addedUser;
                    }
                    else {
                        System.out.println("Error adding user to the database, try again.");
                        return promptUsername();
                    }
                }
                else if(userAnswer.equalsIgnoreCase("N")) {
                    return promptUsername();
                }
                else {
                    System.out.println("invalid option choice!");
                    System.out.println("Would you like to create a new user with this name (Y)es or (N)o?");
                }
            }
            return userToReturn;
        }
    }

    private static void menu() {
        System.out.println("Main menu\nSelect an option:\n"
        + "1. Search for a quiz to play\n" //submenu
        + "2. Choose a topic of questions to play\n"
        + "3. List questions\n" //submenu
        + "4. Import a list of questions\n"
        + "5. Export a list of questions\n"
        + "6. CRUD a quiz\n"
        + "7. CRUD a question\n"
        + "8. Randomly generate a quiz\n" //submenu
        + "9. Produce a short report\n"
        + "10. Review incorrectly answered questions\n"
        + "11. Exit\n");
  
        switch (menuValidInput(1, 11)) {
            case 1:
                //Search for a quiz to play
                playQuiz();
                break;
            case 2:
                playTopicQuiz();
                break;
            case 3:
                listSubmenu();
                break;
            case 4:
                importFromCSV();
                break;
            case 5:
                exportToCSV();
                break;
            case 6:
                crudQuizSubmenu();
                break;
            case 7:
                crudQuestionSubmenu();
                break;
            case 8:
                randomQuizGenSubmenu();
                break;
            case 9:
                produceReportOnQuizzes();
                break;
            case 10:
                reviewIncorrectlyAnsweredQuestions();
                break;
            case 11:
                System.exit(0);;
                break;
            default:
                System.out.println("This is not a valid option\n");
                break;
        }
    }

    private static int getUserOption(String[] userOptions, String question) {
        Boolean isValidChoice = false;
        int userChoiceToReturn = 0;
        while(!isValidChoice) {
            System.out.println(question);
            for (int i = 0; i < userOptions.length; i++) {
                System.out.println(String.format("%s - %s", i, userOptions[i]));
            }
            String userInput = scan.nextLine();
            try {
                int userMenuChoice = Integer.parseInt(userInput);
                if(userMenuChoice >= 0 && userMenuChoice < userOptions.length) {
                    userChoiceToReturn = userMenuChoice;
                    isValidChoice = true;
                }
                else {
                    System.out.println("That menu choice is invalid! Please try again.");
                }
            }
            catch(Exception e) {
                System.out.println("That menu choice is invalid! Please try again.");
            }
        }
        return userChoiceToReturn;
    }

    private static Quiz makeQuizForIncorrectQuestions(boolean forUser) {
        List<Question> questions = new ArrayList<>();
        if(forUser){questions = DatabaseIO.getAllQuestionsUserIncorrectlyAnsweredEver(user);}
        else {
            for(User u: DatabaseIO.getAllUsers()) {
                List<Question> tempQuestions = DatabaseIO.getAllQuestionsUserIncorrectlyAnsweredEver(u);
                for(Question q: tempQuestions) {
                    if(!questions.contains(q)) {
                        questions.add(q);
                    }
                }
            }
        }
        Quiz quiz = new Quiz(user, String.format("%s-incorrectQuiz", Instant.now()));
        DatabaseIO.addQuiz(quiz);
        for(Question q: questions) {
            DatabaseIO.addQuizQuestion(new QuizQuestion(quiz, q, -1));
        }
        return quiz;
    }

    private static void produceReportOnQuizzes() {
        System.out.println(String.format("\nStatistics for you ('%s')", user.getUsername()));
        showIncorrectlyAnsweredQuizStatistics(true);
        System.out.println("\nStatistics for all users");
        showIncorrectlyAnsweredQuizStatistics(false);
    }

    private static void showIncorrectlyAnsweredQuizStatistics(boolean forUser) {
        List<Mark> marks = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        if(!forUser){
            marks = DatabaseIO.getAllMarks();
            for(Mark mark: marks) {
                if(!questions.contains(mark.getQuestionID())) {
                    questions.add(mark.getQuestionID());
                }
            }
        }
        else {
            List<Mark> allMarks = DatabaseIO.getAllMarks();
            for(Mark mark: allMarks) {
                if(DatabaseIO.checkQuizSubmissionExists(mark.getSubmissionID().getId())) {
                    if(mark.getSubmissionID().getUsername().equals(user)) {
                        marks.add(mark);
                        if(!questions.contains(mark.getQuestionID())) {
                            questions.add(mark.getQuestionID());
                        }
                    }
                }
            }
        }
        for(Question question: questions) {
            int answeredCorrectly = 0;
            int answeredIncorrectly = 0;
            int totalAmount = 0;
            double percentageCorrectlyAnswered = 0.0;
            for(Mark mark: marks) {
                if(mark.getQuestionID().equals(question)) {
                    if(mark.getScore() > 0) {answeredCorrectly++;}
                    else{answeredIncorrectly++;}
                    totalAmount++;
                }
            }
            percentageCorrectlyAnswered = (Double.valueOf(answeredCorrectly)/totalAmount)*100;
            System.out.println(String.format("\nQuestion (id: %s) - '%s'\n " +
                            "- times answered: %s\n " +
                            "- times answered correctly: %s\n " +
                            "- times answered incorrectly: %s\n " +
                            "- percentage answered correctly: %s", question.getId(), question.getQuestion(),
                    totalAmount ,answeredCorrectly, answeredIncorrectly, percentageCorrectlyAnswered));
        }
    }

    private static void reviewIncorrectlyAnsweredQuestions() {
        int userChoice = getUserOption(new String[]{
                String.format("Take a quiz on all questions you ('%s') have incorrectly answered in the past.", user.getUsername()),
                String.format("See a list of all questions you ('%s') have incorrectly answered.", user.getUsername()),
                String.format("See statistics on how many times you ('%s') got a question right in comparison to getting them wrong.", user.getUsername()),
                "See statistics on how many times a question was answered correctly in comparison to being answered wrong.",
                String.format("Take a quiz on all questions incorrectly answered by all users in the past.")
        }, "What would you like to do about the questions answered incorrectly in the past? ");
        Quiz quiz;
        switch (userChoice){
            case(0):
                quiz = makeQuizForIncorrectQuestions(true);
                playAndSubmitQuiz(quiz);
                break;
            case(1):
                    List<Question> incorrectlyAnsweredQuestions = DatabaseIO.getAllQuestionsUserIncorrectlyAnsweredEver(user);
                    for(Question question: incorrectlyAnsweredQuestions) {
                        printQuestion(question);
                    }
                break;
            case(2):
                showIncorrectlyAnsweredQuizStatistics(true);
                break;
            case(3):
                showIncorrectlyAnsweredQuizStatistics(false);
                break;
            case(4):
                quiz = makeQuizForIncorrectQuestions(false);
                playAndSubmitQuiz(quiz);
                break;
        }
    }

    private static Quiz generateTopicQuiz(Topic topic) {
        Quiz quiz = DatabaseIO.addQuiz(new Quiz(DatabaseIO.getUser("sys"), topic.getId()));
        return updateTopicQuiz(quiz, topic);
    }

    private static Quiz updateTopicQuiz(Quiz quiz, Topic topic) {
        List<Question> topicQuestions = DatabaseIO.getQuestionsBasedOnTopic(topic);
        List<Question> quizQuestions = DatabaseIO.getQuestionsFromQuiz(quiz.getId());
        for(Question tq: topicQuestions) {
            if(!quizQuestions.contains(tq)) {
                DatabaseIO.addQuizQuestion(new QuizQuestion(quiz, tq, -1));
            }
        }
        return quiz;
    }

    private static Quiz selectTopicQuiz(Topic topic) {
        List<Quiz> topicQuizzes = DatabaseIO.getQuizzesBasedOnUser(DatabaseIO.getUser("sys"));
        //go through questions and go through topic quiz
        //if a question is not in the topic then add it to the quiz
        Quiz selectedQuiz = null;
        for(Quiz quiz: topicQuizzes) {
            if(quiz.getQuizName().equalsIgnoreCase(topic.getId())) {
                selectedQuiz = quiz;
            }
        }
        if(selectedQuiz != null) {
            return updateTopicQuiz(selectedQuiz, topic);
        }
        else {
            return generateTopicQuiz(topic);
        }
    }

    private static boolean playTopicQuiz() {
        List<Topic> topics = DatabaseIO.getAllTopics();
        if(!DatabaseIO.checkUserExists("sys")) {
            DatabaseIO.addUser("sys");
        }
        if(topics.isEmpty()) {
            System.out.println("There does not seem to be any topics in the database. " +
                    "Please add questions with a topic.");
            return true;
        }
        String[] topicChoice = new String[topics.size()];
        Integer[] amountOfQuestionsForTopic = new Integer[topics.size()];
        Arrays.fill(amountOfQuestionsForTopic, 0);
        List<Question> allQuestions = DatabaseIO.getAllQuestions();
        for(Question question: allQuestions) {
            int index = topics.indexOf(question.getTopicName());
            amountOfQuestionsForTopic[index] += 1;
        }
        for(int i = 0; i < topics.size(); i++) {
            topicChoice[i] = String.format("'%s' - %s question(s)", topics.get(i).getId(), amountOfQuestionsForTopic[i]);
        }
        int userTopicChoice = getUserOption(topicChoice, "Select a topic:");
        Topic chosenTopic = topics.get(userTopicChoice);
        Quiz selectedQuiz = selectTopicQuiz(chosenTopic);
        playAndSubmitQuiz(selectedQuiz);
        return true;
    }

    private static void playQuiz() {
        int userQuizOption = getUserOption(new String[]{"quizzes created by you.", "quizzes created by everyone."},
                String.format("Would you like to select quizzes created by you ('%s') or from all quizzes " +
                        "in the database?", user.getUsername()));
        List<Quiz> quizSelection = new ArrayList<>();
        switch(userQuizOption) {
            case(0):
                //list only users quizzes
                List<Quiz> allQuizzes = DatabaseIO.getAllQuizzes();
                for(var quiz: allQuizzes) {
                    if(quiz.getUsername().equals(user)) {
                        quizSelection.add(quiz);
                    }
                }
                break;
            case(1):
                //list all quizzes
                quizSelection = DatabaseIO.getAllQuizzes();
                break;
        }
        String[] quizOptions = new String[quizSelection.size()];
        for(int i = 0; i < quizSelection.size(); i++) {
            quizOptions[i] = String.format("'%s' - '%s'", quizSelection.get(i).getUsername().getUsername(),
                    quizSelection.get(i).getQuizName());
        }
        if(quizOptions.length == 0) {
            System.out.println("No quizzes could be found!");
        }
        else {
            int quizChoice = getUserOption(quizOptions, "Please select a quiz to play:");
            Quiz quizToPlay = quizSelection.get(quizChoice);
            playAndSubmitQuiz(quizToPlay);
        }
    }

    private static List<QuestionMarkTuple> questionAnswerLoop(List<Question> questionsToAnswer) {
        List<QuestionMarkTuple> questionMarkTuples = new ArrayList<>();
        for(Question question: questionsToAnswer) {
            if(question.getQuestionType().equalsIgnoreCase("SAQ")) {
                System.out.println(String.format("%s", question.getQuestion()));
                String userAnswer = scan.nextLine();
                questionMarkTuples.add(DatabaseIO.markQuestionAnswer(question, userAnswer));
            }
            else if(question.getQuestionType().equalsIgnoreCase("MCQ")) {
                List<QuestionOption> optionsForQuestion = DatabaseIO.getQuestionOptionsForQuestion(question);
                String[] questionOptions = new String[optionsForQuestion.size()];
                for(int i = 0; i < optionsForQuestion.size(); i++) {
                    questionOptions[i] = optionsForQuestion.get(i).getQuestionOption();
                }
                int userOption = getUserOption(questionOptions,question.getQuestion());
                String userAnswer = questionOptions[userOption];
                questionMarkTuples.add(DatabaseIO.markQuestionAnswer(question, userAnswer));
            }
        }
        return questionMarkTuples;
    }

    private static void displayMarks(List<QuestionMarkTuple> marks, List<Question> questions) {
        int totalScore = 0;
        int totalPossibleMarks = 0;
        for(int i = 0; i < marks.size(); i++) {
            System.out.println(String.format("Question %s - '%s'\nYour answer: '%s'\nCorrect answer: " +
                            "'%s'\nScore: %s/%s", i, questions.get(i).getQuestion(), marks.get(i).get_userAnswer(),
                    questions.get(i).getAnswer(), marks.get(i).getMarksReceived(), questions.get(i).getMaximumMarks()));
            totalPossibleMarks += questions.get(i).getMaximumMarks();
            totalScore += marks.get(i).getMarksReceived();
        }
        System.out.println(String.format("Your final score for that quiz was %s/%s",
                totalScore, totalPossibleMarks));
    }


    /**
     *
     * @param quizToPlay
     * @return null if no questions in a quiz or a quiz does not exist.
     */
    private static QuizSubmission playAndSubmitQuiz(Quiz quizToPlay) {
        List<QuestionMarkTuple> questionsAndMarks = new ArrayList<>();
        System.out.println(String.format("You are now playing '%s' created by '%s'.",
                quizToPlay.getQuizName(), quizToPlay.getUsername().getUsername()));
        if(!DatabaseIO.checkQuizExists(String.valueOf(quizToPlay.getId()))) {
            System.out.println("Error! it seems the chosen quiz does not exist.");
            return null;
        }
        List<Question> questionsToAnswer = DatabaseIO.getQuestionsFromQuiz(quizToPlay.getId());
        if(questionsToAnswer.isEmpty()) {
            System.out.println("This quiz is empty.");
            return null;
        }
        questionsAndMarks = questionAnswerLoop(questionsToAnswer);
        QuizSubmission submission = DatabaseIO.submitQuizResults(questionsAndMarks, user, quizToPlay);
        System.out.println("Quiz complete and submitted!");
        displayMarks(questionsAndMarks, questionsToAnswer);
        //TEST THIS
        return submission;
    }

    private static void importFromCSV() {
        // unsure of necessary syntax for filepath, if it should be absolute or relative
        System.out.println("What is the name of the file you want to import?\n"
        + "It should be a csv file and include the .csv extension. The filename is case-insensitive");
        String filePath = scan.nextLine();
        List<Question> questionList = DatabaseIO.importQuestionsFromCSV(filePath);
        if (questionList != null) {
            for (Question i : questionList) {
                DatabaseIO.addQuestion(i);
            }
            System.out.println("File read, and the questions were added to the database");
        }
        else {
            System.out.println("File does not exist, or was not read");
        }
    }

    private static void exportToCSV() {
        System.out.println("What is the name of the file you want to create? (.csv extension is not necessary):");
        String fileName = stringValidInput();
        System.out.println("What directory do you want to store the file in?");
        String filePath = stringValidInput();
        System.out.println("Add a question to the file using its ID. You will be repeatedly prompted until you enter nothing");
        List<Question> questionList = new ArrayList<Question>();
        while (true) {
            System.out.println("Enter ID");
            String input = scan.nextLine();
            if (input.length() == 0) {
                break;
            }
            questionList.add(DatabaseIO.getQuestion(input));
        }
        System.out.println("Question list has been created");
        System.out.println("Exporting to CSV");
        int status = DatabaseIO.exportQuestionsToCSV(filePath, fileName, questionList);
        switch (status) {
            case 0:
                System.out.println("Success!");
                break;
            case 1:
                System.out.println("Directory does not exist");
                break;
            case 2:
                System.out.println("Writing to file failed");
                break;
        }          
    }

    private static void crudQuestionSubmenu() {
        System.out.println("[Question CRUD submenu]\nSelect an option:\n"
        + "1. Create a question"
        + "2. Read a question"
        + "3. Update a question"
        + "4. Delete a question");

        switch(menuValidInput(1, 4)) {
            case 1:
                createQuestion();
                break;
            case 2:
                System.out.println("Enter a question ID:");
                printQuestion(DatabaseIO.getQuestion(String.valueOf(positiveIntegerValidInput())));
                break;
            case 3:
                System.out.println("Enter a question ID:");
                int ID = positiveIntegerValidInput();
                Question tempQU = DatabaseIO.getQuestion(String.valueOf(ID));
                System.out.println("Update which field of question [" + ID + "]?"
                + "1. Question string\n"
                + "2. Answer string\n"
                + "3. Score\n"
                + "4. Question type\n"
                + "5. Question topic");
                switch (menuValidInput(1, 5)) {
                    case 1:
                        System.out.println("Enter new question string");
                        tempQU.setQuestion(stringValidInput());
                        break;
                    case 2:
                        System.out.println("Enter new answer string");
                        tempQU.setAnswer(stringValidInput());
                        break;
                    case 3:
                        System.out.println("Enter new score");
                        tempQU.setMaximumMarks(positiveIntegerValidInput());
                        break;
                    case 4:
                        System.out.println("Enter new question type");
                        tempQU.setQuestionType(chooseType());
                        break;
                    case 5:
                        System.out.println("Use an existing topic? (Y/N)");
                        if (scan.nextLine().toLowerCase().equals("y")) {
                            tempQU.setTopicName(DatabaseIO.getTopic(chooseTopic()));
                        }
                        else {
                            System.out.println("Enter a new topic name:");
                            tempQU.setTopicName(DatabaseIO.getTopic(stringValidInput()));
                            System.out.println("Enter a description for the new topic");
                            DatabaseIO.addTopic(tempQU.getTopicName().getId(), scan.nextLine());
                        }
                        break;
                }
                DatabaseIO.updateQuestion(tempQU);
                break;
            case 4:
                System.out.println("Enter a question ID:");
                DatabaseIO.purgeQuestionFromDatabase(positiveIntegerValidInput());
                System.out.println("Question has been deleted");
                break;
        }
    }

    private static void createQuestion() {
        System.out.println("[Creating a question]");
        System.out.println("Question string:");
        String quString = stringValidInput();
        String quType = chooseType();
        String quTopic;
        System.out.println("Use an existing topic? (Y/N)");
        if (scan.nextLine().toLowerCase().equals("y")) {
            quTopic = chooseTopic();
        }
        System.out.println("Enter a new topic name:");
        quTopic = stringValidInput();
        System.out.println("Enter a description for the new topic");
        Topic topic = DatabaseIO.addTopic(quTopic, scan.nextLine());

        System.out.println("Score:");
        int quScore = positiveIntegerValidInput();

        System.out.println("Answer string:");
        String quAnswer = stringValidInput();
        Question question = new Question(quString, quAnswer, quScore, quType, topic);
        Question dbQuestion = DatabaseIO.addQuestion(question);

        if (quType.equals("MCQ")) {
            DatabaseIO.addQuestionOption(new QuestionOption(dbQuestion, quAnswer));
            System.out.println("How many extra options do you want to add?");
            for (int i = 0; i < positiveIntegerValidInput(); i++) {
                System.out.println("Enter option string:");
                DatabaseIO.addQuestionOption(new QuestionOption(dbQuestion, stringValidInput()));
                System.out.println("Option added to database");
            }
        }

        System.out.println("To add this question to a quiz, use the CRUD quiz menu option");
                
    }

    private static int positiveIntegerValidInput() { // prompts for user input until an integer greater than 0 is entered
        boolean success = false;
        int number = 0;
        while (!success) {
            try {
                number = scan.nextInt();
                if (number > 0) {
                    System.out.println("Input accepted");
                    scan.nextLine();
                    success = true;
                } else {
                    scan.nextLine();
                    System.out.println("Error - integer must be greater than 0 \nTry again: ");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Error - input was not an integer \nTry again: ");
            }
        }
        return number;
    }
    
    private static void crudQuizSubmenu() {
        System.out.println("[Quiz CRUD submenu]\nSelect an option:\n"
        + "1. Create a quiz"
        + "2. Read a quiz"
        + "3. Update a quiz"
        + "4. Delete a quiz");

        switch (menuValidInput(1, 4)) {
            case 1:
                break;
            case 2:
                System.out.println("Enter a quiz ID: ");
                Quiz temp = DatabaseIO.getQuiz(String.valueOf(positiveIntegerValidInput()));
                System.out.println("ID: " + temp.getId()
                + "\nName: " + temp.getQuizName()
                + "\nUsername: " + temp.getUsername()
                + "\nQuestions:");
                for (Question i : DatabaseIO.getQuestionsFromQuiz(temp.getId())) {
                    printQuestion(i);
                }
                break;
            case 3:
                break;
            case 4:
                System.out.println("Enter quiz ID: ");
                DatabaseIO.removeQuiz(String.valueOf(positiveIntegerValidInput()));
                System.out.println("Quiz removed");
                break;
        }
    }
    
    private static void listSubmenu() {
        System.out.println("[Question Lister submenu]\nSelect an option:\n"
        + "1. List all questions"
        + "2. List all questions by type"
        + "3. List all questions by topic");
        switch(menuValidInput(1, 3)) {
            case 1:
                List<Question> QuestionList1 = DatabaseIO.getAllQuestions();
                for (Question i : QuestionList1) {
                    printQuestion(i);
                }      
                break;
            case 2:               
                String typeInput = chooseType();

                List<Question> QuestionList2 = DatabaseIO.getAllQuestions();
                for (Question i : QuestionList2) {
                    if (i.getQuestionType().toLowerCase() == typeInput.toLowerCase())
                    printQuestion(i);
                }   
                break;
            case 3:
                String topicInput = chooseTopic();

                List<Question> QuestionList3 = DatabaseIO.getAllQuestions();
                for (Question i : QuestionList3) {
                    if (i.getTopicName().getId().toLowerCase() == topicInput.toLowerCase()) {
                        printQuestion(i);
                    }               
                }   
                break;
        }
    }

    private static void randomQuizGenSubmenu() {
        System.out.println("[Random Quiz Generator submenu]\n"
        + "How many questions?:\n"
        + "1. 5\n"
        + "2. 10\n"
        + "3. 15\n"
        + "4. 20\n");
        
        int questionCount = menuValidInput(1, 4) * 5;

        System.out.println("Choose a specific topic?: (Y/N)");
        String response = scan.nextLine();
        String topic = "ALL";
        if (response.toLowerCase().equals("y")) {
            topic = chooseTopic();
        }

        System.out.println("Choose a specific question type?: (Y/N)");
        response = scan.nextLine();
        String type = "ALL";
        if (response.toLowerCase().equals("y")) {
            type = chooseType();
        }

        System.out.println("Use only questions you have answered incorrectly before? (Y/N)");
        response = scan.nextLine();
        boolean wronglyAnsweredQus = false;
        if (response.toLowerCase().equals("y")) {
            wronglyAnsweredQus = true;
        }
        Quiz generatedQuiz = generateQuiz(questionCount, topic, type, wronglyAnsweredQus);
        System.out.println("Quiz generated!\n"
        + "Name: " + generatedQuiz.getQuizName() + "\n"
        + "Username: " + generatedQuiz.getUsername() + "\n"
        + "ID: " + generatedQuiz.getId());
    }

    private static Quiz generateQuiz(int questionCount, String topic, String type, boolean wronglyAnsweredQus) {
        Quiz generator = DatabaseIO.addQuiz(new Quiz(user, "RNG:"+user+"|QUcount:"+questionCount+"|Topic:"+topic+"|Type:"+type+"|HistoricalBadAnswer:"+wronglyAnsweredQus));
        ArrayList<Question> validQuestions = new ArrayList<Question>();
        List<Question> possibleQuestions;
        
        if (wronglyAnsweredQus) {
            possibleQuestions = DatabaseIO.getAllQuestionsUserIncorrectlyAnsweredEver(user);
        }
        else {
            possibleQuestions = DatabaseIO.getAllQuestions();
        }

        for (Question i : possibleQuestions) {
            if ( (i.getTopicName().getId() == topic || topic == "ALL") && (i.getQuestionType() == type || type == "ALL") ) {
                validQuestions.add(i);
            }
        }

        Random rng = new Random();
        for (int i = 0; i < questionCount; i++)
        {
            int random = rng.nextInt(validQuestions.size());
            DatabaseIO.addQuizQuestion(new QuizQuestion(generator, validQuestions.get(random), i));
            validQuestions.remove(random);
        }

        return generator;
    }
    
    private static String chooseTopic() {
        System.out.println("Choose a topic:");
        List<Topic> TopicList = DatabaseIO.getAllTopics();
        for (Topic i : TopicList) {
            System.out.println(i.getId());
        }
        return scan.nextLine();
    }

    private static String chooseType() {
        System.out.println("Choose a question type: \nMCQ \nSAQ");
        return scan.nextLine();
    }

    private static void printQuestion(Question qu) {
        System.out.println("ID: [" +qu.getId()+ "] \n"
                    + "Question: [" +qu.getQuestion()+ "]\n"
                    + "Type: [" +qu.getQuestionType()+ "]\n"
                    + "Topic: [" +qu.getTopicName()+ "]\n"
                    + "Marks: [" +qu.getMaximumMarks()+ "]\n"
                    + "Answer: [" +qu.getAnswer()+ "]\n");
    }

    private static String stringValidInput() { // prompts for string input, accepts any non-empty string
        String text = "";
        boolean success = false;
        while (!success) {
            text = scan.nextLine();
            if (text.length() > 0) {
                success = true;
            } else {
                System.out.println("Error - no characters typed \nTry again: ");
            }
        }
        return text;
    }

    private static int menuValidInput(int minInput, int maxInput) { // prompts for user input until an integer greater than 0 is entered
        boolean success = false;
        int number = -1;
        while (!success) {
            try {
                number = scan.nextInt();
                if (number >= minInput && number <= maxInput) {
                    System.out.println("Input accepted");
                    scan.nextLine();
                    success = true;
                } else {
                    scan.nextLine();
                    System.out.println("Error - integer must be greater than "+minInput+" and less than "+maxInput+"\nTry again: ");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Error - input was not an integer \nTry again: ");
            }
        }
        return number;
    }

}
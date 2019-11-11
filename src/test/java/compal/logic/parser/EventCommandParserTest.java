package compal.logic.parser;

import compal.logic.command.EventCommand;
import compal.logic.command.exceptions.CommandException;
import compal.model.tasks.Task;
import compal.model.tasks.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static compal.logic.parser.CommandParserTestUtil.assertParseFailure;
import static compal.logic.parser.CommandParserTestUtil.assertParseSuccess;

//@@author yueyeah
public class EventCommandParserTest {
    private EventCommandParser parser = new EventCommandParser();

    private ArrayList<Task> taskArrListMain = new ArrayList<>();
    private ArrayList<Task> taskArrListDup = new ArrayList<>();

    private TaskList taskListMain = new TaskList();
    private TaskList taskListDup = new TaskList();

    @BeforeEach
    void setUp() {
        this.taskListMain.setArrList(taskArrListMain);
        this.taskListDup.setArrList(taskArrListDup);
    }

    /**
     * TESTS FOR INVALID INPUT BELOW.
     */
    @Test
    void parse_missingTokenDate_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/start 1300"
                + "/end 1500 "
                + "/priority high",
                CommandParser.MESSAGE_MISSING_DATE_ARG);
    }

    @Test
    void parse_excessiveDatesAfterTokenDate_failure() {
        assertParseFailure(parser, "CS2113T Lecture "
                + "/date 10/10/2019 11/10/2019 12/10/2019 13/10/2019 14/10/2019 15/10/2019 16/10/2019 17/10/2019"
                + "/start 1700"
                + "/end 1800"
                + "/priority high",
                CommandParser.MESSAGE_EXCESSIVE_DATES);
    }

    @Test
    void parse_missingDateAfterTokenDate_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date "
                + "/start 1300 "
                + "/end 1500 "
                + "/priority high",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parse_invalidDateFormatAfterTokenDate_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 2/5/2020 "
                + "/start 1300 "
                + "/end 1500 "
                + "/priority low",
                CommandParser.MESSAGE_INVALID_DATE_FORMAT);
    }

    @Test
    void parser_missingPriorityAfterTokenPriority_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/start 1300 "
                + "/end 1500 "
                + "/priority ",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parser_missingTokenStartTime_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/end 1500 "
                + "/priority high",
                CommandParser.MESSAGE_MISSING_START_TIME_ARG);
    }

    @Test
    void parser_missingStartTimeAfterTokenStartTimeInMiddleOfInput_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/start "
                + "/end 1500 "
                + "/priority high",
                CommandParser.MESSAGE_INVALID_TIME_FORMAT);
    }

    @Test
    void parser_missingStartTimeAfterTokenStartTimeAtEndOfInput_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/end 1500 "
                + "/priority high "
                + "/start ",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parser_missingTokenEndTime_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/start 1300 "
                + "/priority high",
                CommandParser.MESSAGE_MISSING_END_TIME_ARG);
    }

    @Test
    void parser_missingEndTimeAfterTokenEndTimeInMiddleOfInput_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/start 1300 "
                + "/end "
                + "/priority high",
                CommandParser.MESSAGE_INVALID_TIME_FORMAT);
    }

    @Test
    void parser_missingEndTimeAfterTokenEndTimeAtEndOfInput_failure() {
        assertParseFailure(parser, "CS2113T Exam "
                + "/date 23/11/2019 "
                + "/start 1300 "
                + "/priority high "
                + "/end ",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parser_missingAnyTokenAfterDescription_failure() {
        assertParseFailure(parser, "", CommandParser.MESSAGE_MISSING_TOKEN);
    }

    @Test
    void parser_missingDescriptionBeforeAnyToken_failure() {
        assertParseFailure(parser, " /date 23/11/2019 "
                + "/start 1300 "
                + "/end 1500 "
                + "/priority high",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parser_missingIntervalAfterTokenInterval_failure() {
        assertParseFailure(parser, "CS2113T Lecture "
                + "/date 10/11/2019 "
                + "/start 1700 "
                + "/end 1800 "
                + "/priority high "
                + "/interval",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parser_missingFinalDateAfterTokenFinalDateInMiddleOfInput_failure() {
        assertParseFailure(parser, "CS2113T Lecture"
                + "/date 10/11/2019 "
                + "/final-date "
                + "/start 1700 "
                + "/end 1800 ",
                CommandParser.MESSAGE_INVALID_DATE_FORMAT);
    }

    @Test
    void parser_missingFinalDateAfterTokenFinalDateAtEndOfInput_failure() {
        assertParseFailure(parser, "CS2113T Lecture"
                + "/date 10/11/2019 "
                + "/start 1700 "
                + "/end 1800 "
                + "/final-date",
                CommandParser.MESSAGE_MISSING_INPUT);
    }

    @Test
    void parser_invalidFinalDateFormatAfterTokenFinalDate_failure() {
        assertParseFailure(parser, "CS2113T Lecture "
                + "/date 10/11/2019 "
                + "/start 1700 "
                + "/end 1800 "
                + "/priority high "
                + "/final-date 1/12/2019",
                CommandParser.MESSAGE_INVALID_DATE_FORMAT);
    }

    /**
     * TESTS FOR VALID INPUT BELOW.
     */
    @Test
    void parser_validDateStartTimeEndTime_success() throws CommandException {
        String testDescription = "CS2113T Lecture";
        String testDateString =  "10/11/2019";
        ArrayList<String> testDateStringList = new ArrayList<>();
        testDateStringList.add(testDateString);
        String testFinalDateString = "10/11/2019";
        String testStartTime = "1700";
        String testEndTime = "1800";
        Task.Priority testPriority = Task.Priority.low;
        int testInterval = 7;
        EventCommand testEventCommand = new EventCommand(testDescription, testDateStringList, testPriority,
                testStartTime, testEndTime, testFinalDateString, testInterval);
        String testEventCommandParserInput = testDescription + " /date " + testDateString + " /start " + testStartTime
                + " /end " + testEndTime;
        assertParseSuccess(parser, testEventCommandParserInput, testEventCommand.commandExecute(taskListDup),
                taskListMain);
    }

    @Test
    void parser_validMultipleDatesStartTimeEndTime_success() throws CommandException {
        String testDescription = "CS2113T Lecture";
        String testFirstDateString = "10/11/2019";
        String testSecondDateString = "11/11/2019";
        ArrayList<String> testDateStringList = new ArrayList<>();
        testDateStringList.add(testFirstDateString);
        testDateStringList.add(testSecondDateString);
        String testFinalDateString = "11/11/2019";
        String testStartTime = "1700";
        String testEndTime = "1800";
        Task.Priority testPriority = Task.Priority.low;
        int testInterval = 7;
        EventCommand testEventCommand = new EventCommand(testDescription, testDateStringList, testPriority,
                testStartTime, testEndTime, testFinalDateString, testInterval);
        String testEventCommandParserInput = testDescription + " /date " + testFirstDateString + " "
                + testSecondDateString + " /start " + testStartTime + " /end " + testEndTime;
        assertParseSuccess(parser, testEventCommandParserInput, testEventCommand.commandExecute(taskListDup),
                taskListMain);
    }

    @Test
    void parser_validDateStartTimeEndTimePriority_success() throws CommandException {
        String testDescription = "CS2113T Lecture";
        String testDateString = "10/11/2019";
        ArrayList<String> testDateStringList = new ArrayList<>();
        testDateStringList.add(testDateString);
        String testFinalDateString = "10/11/2019";
        String testStartTime = "1700";
        String testEndTime = "1800";
        String testPriorityString = "high";
        Task.Priority testPriority = Task.Priority.valueOf(testPriorityString.toLowerCase());
        int testInterval = 7;
        EventCommand testEventCommand = new EventCommand(testDescription, testDateStringList, testPriority,
                testStartTime, testEndTime, testFinalDateString, testInterval);
        String testEventCommandParserInput = testDescription + " /date " + testDateString + " /start " + testStartTime
                + " /end " + testEndTime + " /priority " + testPriorityString;
        assertParseSuccess(parser, testEventCommandParserInput, testEventCommand.commandExecute(taskListDup),
                taskListMain);
    }

    @Test
    void parser_validDateStartTimeEndTimeFinalDate_success() throws CommandException {
        String testDescription = "CS2113T Lecture";
        String testDateString = "10/11/2019";
        ArrayList<String> testDateStringList = new ArrayList<>();
        testDateStringList.add(testDateString);
        String testFinalDateString = "01/01/2020";
        String testStartTime = "1700";
        String testEndTime = "1800";
        int testInterval = 7;
        Task.Priority testPriority = Task.Priority.low;
        EventCommand testEventCommand = new EventCommand(testDescription, testDateStringList, testPriority,
                testStartTime, testEndTime, testFinalDateString, testInterval);
        String testEventCommandParserInput = testDescription + " /date " + testDateString + " /start " + testStartTime
                + " /end " + testEndTime + " /final-date " + testFinalDateString;
        assertParseSuccess(parser, testEventCommandParserInput, testEventCommand.commandExecute(taskListDup),
                taskListMain);
    }

    @Test
    void parser_validDateStartTimeEndTimeFinalDateInterval_success() throws CommandException {
        String testDescription = "CS2113T Lecture";
        String testDateString = "10/11/2019";
        ArrayList<String> testDateStringList = new ArrayList<>();
        testDateStringList.add(testDateString);
        String testFinalDateString = "01/01/2020";
        String testStartTime = "1700";
        String testEndTime = "1800";
        String testIntervalString = "9";
        int testInterval = 9;
        Task.Priority testPriority = Task.Priority.low;
        EventCommand testEventCommand = new EventCommand(testDescription, testDateStringList, testPriority,
                testStartTime, testEndTime, testFinalDateString, testInterval);
        String testEventCommandParserInput = testDescription + " /date " + testDateString + " /start " + testStartTime
                + " /end " + testEndTime + " /final-date " + testFinalDateString + " /interval " + testIntervalString;
        assertParseSuccess(parser, testEventCommandParserInput, testEventCommand.commandExecute(taskListDup),
                taskListMain);
    }
}

package compal.logic.command;

import compal.logic.parser.exceptions.ParserException;
import compal.model.tasks.Deadline;
import compal.model.tasks.Event;
import compal.model.tasks.Task;
import compal.model.tasks.TaskList;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//@@author SholihinK
class ListCommandTest {
    private ArrayList<Task> taskArrListMain = new ArrayList<>();
    private ArrayList<Task> taskArrListEmpty = new ArrayList<>();

    private TaskList taskListMain = new TaskList();
    private TaskList taskListEmpty = new TaskList();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Event event1 = new Event("Event 1", Task.Priority.medium, "01/10/2019", "01/10/2019", "1400", "1500");
        Event event2 = new Event("Event 2", Task.Priority.medium, "01/10/2019", "01/10/2019", "1400", "1500");
        event2.markAsDone();
        taskArrListMain.add(event1);
        taskArrListMain.add(event2);

        Deadline deadline1 = new Deadline("Deadline 1", Task.Priority.high, "01/10/2019", "1500");
        Deadline deadline2 = new Deadline("Deadline 2", Task.Priority.high, "01/10/2019", "1500");
        deadline2.markAsDone();

        taskArrListMain.add(deadline1);
        taskArrListMain.add(deadline2);

        this.taskListMain.setArrList(taskArrListMain);
        this.taskListEmpty.setArrList(taskArrListEmpty);
    }

    @Test
    void execute_listDeadline_success() {
        String expected = new ListCommand("deadline").commandExecute(taskListMain).feedbackToUser;
        String tested = listStub(taskListMain, "D");
        Assertions.assertEquals(expected, tested);
    }

    @Test
    void execute_listEvent_success() {
        String expected = new ListCommand("event").commandExecute(taskListMain).feedbackToUser;
        String tested = listStub(taskListMain, "E");
        Assertions.assertEquals(expected, tested);
    }

    @Test
    void execute_emptyList_success() {
        String expected = new ListCommand("deadline").commandExecute(taskListEmpty).feedbackToUser;
        String tested = listStub(taskListEmpty, "D");
        Assertions.assertEquals(expected, tested);
    }


    private String listStub(TaskList taskList, String type) {
        String listPrefix = "Here are the tasks in your list sorted by chronological order: \n";
        String prefixType = "";
        if (type.equals("D")) {
            prefixType = "deadline";
        } else if (type.equals("E")) {
            prefixType = "event";
        }

        String listPrefixTwo = "Here are the stored " + prefixType + " in your list:\n\n";
        String listEmpty = "Looks like there is nothing to list for this command!\n";

        ArrayList<Task> toList = taskList.getArrList();


        StringBuilder finalList;
        if (type.isEmpty()) {
            finalList = new StringBuilder(listPrefix);
        } else {
            finalList = new StringBuilder(listPrefixTwo);
        }

        for (Task t : toList) {
            if (type.isEmpty()) {
                String taskString = t.toString() + "\n";
                finalList.append(taskString);
            } else {
                if (t.getSymbol().equals(type)) {
                    String taskString = t.toString() + "\n";
                    finalList.append(taskString);
                }
            }
        }

        if (finalList.toString().equals(listPrefixTwo)) {
            finalList = new StringBuilder(listEmpty);
        }

        return finalList.toString();
    }


}
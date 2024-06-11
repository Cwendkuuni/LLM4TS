package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testMain() {
        String[] args = {"testInputFile.txt"};
        Main.main(args);
        // Add assertions here to check the expected output or state after the execution of main method
    }

    @Test
    public void testProcessInstruction_out() {
        Main.instruction = new String[]{"out 5"};
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals("5", System.out.getHistory(0));
        // Add more test cases for other opcodes here
    }

    @Test
    public void testProcessInstruction_mov() {
        Main.instruction = new String[]{"mov 1 2"};
        Variable.setVariableValue("1", 10);
        Variable.setVariableValue("2", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(10, Variable.getVariableValue("2"));
        // Add more test cases for different input values
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.instruction = new String[]{"mov 1 2", "jmp 1", "mov 3 4", " halt"};
        Variable.setVariableValue("1", 0);
        Variable.setVariableValue("2", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]); // mov 1 2
        Main.processInstruction(Main.instruction[Main.iPtr]); // jmp 1
        assertEquals(5, Variable.getVariableValue("1")); // Should jump to the label with value 1 (assumed to be the second instruction)
        // Add more test cases for different jump targets and input values
    }

    @Test
    public void testProcessInstruction_exception() {
        Main.instruction = new String[]{"mov 1 2", "mov a 2", "halt"};
        try {
            Main.processInstruction(Main.instruction[Main.iPtr]);
            fail("Expected an exception");
        } catch (Exception e) {
            // Add assertions to check the expected exception type and message here
        }
        Main.processInstruction(Main.instruction[++Main.iPtr]); // mov 2 halt
    }

    // Add more test cases here for other methods and edge cases
}
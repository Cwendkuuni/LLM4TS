package jipa;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    private static int[] initialVariableValues = { 0, 1, 2, 3, 4, 5 };
    private static String[] instructions = new String[10];
    private static Main main;

    @BeforeClass
    public static void setup() {
        main = new Main();
        instructions = new String[] {
                "mov 1 0",
                "mov 2 1",
                "mov 3 2",
                "mov 4 3",
                "mov 5 4",
                "inc 1",
                "dec 2",
                "add 1 2",
                "out 1",
                "halt"
        };
        main.loadInstructions("test.txt");
    }

    @Test
    public void testProcessInstruction_mov() {
        main.iPtr = 0;
        main.processInstruction(instructions[0]);
        assertEquals(1, main.Variable.getVariableValue("0"));
        assertEquals(0, main.Variable.getVariableValue("1"));

        main.resetInstructions();
        main.iPtr = 0;
        main.processInstruction(instructions[1]);
        assertEquals(2, main.Variable.getVariableValue("0"));
        assertEquals(1, main.Variable.getVariableValue("1"));
    }

    @Test
    public void testProcessInstruction_inc() {
        main.iPtr = 6;
        main.processInstruction(instructions[6]);
        assertEquals(2, main.Variable.getVariableValue("1"));
    }

    @Test
    public void testProcessInstruction_dec() {
        main.iPtr = 7;
        main.processInstruction(instructions[7]);
        assertEquals(1, main.Variable.getVariableValue("2"));
    }

    @Test
    public void testProcessInstruction_add() {
        main.iPtr = 8;
        main.processInstruction(instructions[8]);
        assertEquals(3, main.Variable.getVariableValue("1"));

        main.resetInstructions();
        main.iPtr = 0;
        main.processInstruction(instructions[0]);
        main.processInstruction(instructions[1]);
        main.processInstruction(instructions[2]);
        main.processInstruction(instructions[3]);
        main.processInstruction(instructions[4]);
        main.processInstruction(instructions[5]);
        main.processInstruction(instructions[6]);
        main.processInstruction(instructions[7]);
        main.processInstruction(instructions[8]);
        main.processInstruction(instructions[9]);
        assertEquals(15, main.Variable.getVariableValue("1"));
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstruction_invalidOpcode() {
        main.iPtr = 0;
        main.processInstruction("invalidOpCode");
    }

    @Test
    public void testLoadInstructions() {
        main.loadInstructions("");
        assertEquals(0, main.TOTAL_INSTRUCTIONS);

        main.loadInstructions("test.txt");
        assertEquals(10, main.TOTAL_INSTRUCTIONS);
        assertArrayEquals(instructions, main.instruction);
    }

    @Test
    public void testGetFileName() {
        String fileName = main.getFileName();
        assertEquals("", fileName);

        main.resetInstructions();
        fileName = main.getFileName();
        assertEquals("Enter the full path to the file or type q to quit :", fileName);

        main.loadInstructions("test.txt");
        fileName = main.getFileName();
        assertEquals("", fileName);
    }

    @Test
    public void testRemoveComment() {
        String instruction = "mov 1 0 // this is a comment";
        String expectedInstruction = "mov 1 0";
        assertEquals(expectedInstruction, main.removeComment(instruction));
    }
}
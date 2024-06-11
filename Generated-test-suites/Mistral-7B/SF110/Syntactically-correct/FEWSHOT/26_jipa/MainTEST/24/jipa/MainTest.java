package jipa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        Main main = new Main();
        main.instruction = new String[]{"mov a 0", "inc a"};
        main.iPtr = 0;

        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(0, main.Variable.getVariableValue("a"));

        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(1, main.Variable.getVariableValue("a"));
    }

    @Test
    public void testLoadInstructions() {
        Main main = new Main();

        String fileName = "test.txt";
        String expectedInstruction = "mov a 0";
        List<String> instructions = Arrays.asList(expectedInstruction.split(" "));

        main.loadInstructions(fileName);
        assertEquals(1, main.TOTAL_INSTRUCTIONS);
        assertEquals(expectedInstruction, main.instruction[0]);
    }

    @Test
    public void testRemoveComment() {
        String lineWithComment = "mov a 5 -- this is a comment";
        String expectedLine = "mov a 5";

        assertEquals(expectedLine, Main.removeComment(lineWithComment));
    }

    @Test
    public void testSplitArguments() {
        String original = "mov a b c 'test'\" 'string'";
        String[] expectedArgs = {"b", "c", "test", "string"};

        assertArrayEquals(expectedArgs, Main.splitArguments(original));
    }

    @Test
    public void testGetStringValue() {
        String original = "mov a 'test string'";
        String expectedValue = "test string";

        assertEquals(expectedValue, Main.getStringValue(original));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(Main.isNumeric("5"));
        assertFalse(Main.isNumeric("a"));
    }

    @Test
    public void testGetJumpValue() {
        String label1 = "LABEL1";
        String label2 = "LABEL2";
        Main main = new Main();
        main.Label.addLabel(label1, 1);
        main.Label.addLabel(label2, 3);

        assertEquals(1, main.getJumpValue(label1));
        assertEquals(2, main.getJumpValue(label2));
    }
}
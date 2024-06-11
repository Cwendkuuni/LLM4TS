package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    private static final String FIXTURE_FILE = "testData.txt";
    private static final String VALID_INSTRUCTION = "mov A 10";
    private static final String VALID_LABEL = "label1:";
    private static final String INVALID_INSTRUCTION = "invalidOp";
    private static final String MISSING_ARGUMENT = "mov A";
    private static final String NUMBER_ARGUMENT = "mov A 123";
    private static final String STRING_ARGUMENT = "mov A \"test\"";
    private static final String EMPTY_INSTRUCTIONS_FILE = "";
    private static final String MISSING_FILE = "missingFile.txt";
    private static final String NON_EXISTENT_LABEL = "nonExistentLabel:";

    @Before
    public void setup() {
        Main.resetInstructions();
    }

    @Test
    public void testLoadInstructionsWithValidFile() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        assertEquals(10, Main.instruction.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructionsWithEmptyFile() {
        Main.loadInstructions(EMPTY_INSTRUCTIONS_FILE);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructionsWithMissingFile() {
        Main.loadInstructions(MISSING_FILE);
    }

    @Test
    public void testLoadInstructionsWithInvalidInstruction() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        assertFalse(Main.processInstruction(INVALID_INSTRUCTION));
    }

    @Test
    public void testProcessInstructionWithValidInstruction() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        assertTrue(Main.processInstruction(VALID_INSTRUCTION));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionWithMissingArgument() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        assertFalse(Main.processInstruction(MISSING_ARGUMENT));
    }

    @Test
    public void testProcessInstructionWithNumberArgument() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        Main.iPtr = 0;
        Main.processInstruction(NUMBER_ARGUMENT);
        assertEquals(123, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstructionWithStringArgument() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        Main.iPtr = 0;
        Main.processInstruction(STRING_ARGUMENT);
        assertEquals("test", Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstructionWithLabel() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        Main.iPtr = 0;
        Main.processInstruction(VALID_LABEL);
        assertEquals(0, Main.iPtr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionWithNonExistentLabel() {
        assertTrue(Main.loadInstructions(FIXTURE_FILE));
        Main.iPtr = 0;
        Main.processInstruction(NON_EXISTENT_LABEL);
    }
}
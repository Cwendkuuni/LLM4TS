package jipa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {
    private static final String VALID_INSTRUCTION_FILE = "validInstructions.txt";
    private static final String INVALID_INSTRUCTION_FILE = "invalidInstructions.txt";
    private static final String PROGRAM_NAME = "Main";
    private static final int EXPECTED_VALID_INSTRUCTIONS_COUNT = 10;
    private static final int EXPECTED_INVALID_INSTRUCTIONS_COUNT = 5;
    private static final int[] EXpectedValues = {1, 2, 3, 4, 5};
    private static final String[] EXpectedLabels = {"Label1", "Label2", "Label3"};
    private static final Variable variable = new Variable();

    @Before
    public void setup() {
        resetInstructions();
    }

    @Test
    public void testGetFileName() {
        String fileName = getFileName();
        Assert.assertFalse(fileName.isEmpty());
    }

    @Test
    public void testResetInstructions() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        resetInstructions();
        Assert.assertArrayEquals(new String[0], Main.instruction);
    }

    @Test
    public void testLoadInstructionsWithEmptyFile() {
        loadInstructions("");
        Assert.assertArrayEquals(new String[0], Main.instruction);
    }

    @Test
    public void testLoadInstructionsWithInvalidFile() {
        loadInstructions(INVALID_INSTRUCTION_FILE);
        Assert.assertArrayEquals(new String[0], Main.instruction);
    }

    @Test(expected = Exception.class)
    public void testLoadInstructionsWithNullFile() {
        loadInstructions(null);
    }

    @Test
    public void testLoadInstructionsWithValidFile() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        Assert.assertArrayEquals(Main.instruction, getExpectedInstructions());
        Assert.assertEquals(EXPECTED_VALID_INSTRUCTIONS_COUNT, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testProcessInstructionWithOutOpcode() {
        loadInstructions(VALID_INSTRUCTION_File);
        String instructionWithoutOpcode = Main.instruction[0];
        processInstruction(instructionWithoutOpcode);
        Assert.assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstructionWithValidOpcode() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        processInstruction(Main.instruction[0]);
        Assert.assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstructionWithInvalidOpcode() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        String invalidOpcode = "INVALID_OPCODE";
        processInstruction(invalidOpcode);
        Assert.assertEquals(0, Main.iPtr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionWithInvalidArgumentNumber() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        String instructionWithInvalidArgumentNumber = Main.instruction[0] + " INVALID_ARGUMENT_NUMBER";
        processInstruction(instructionWithInvalidArgumentNumber);
    }

    @Test
    public void testProcessInstructionWithValidArguments() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        processInstruction(Main.instruction[0]);
        testVariablesValues();
    }

    @Test
    public void testProcessInstructionWithInvalidArguments() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        String instructionWithInvalidArguments = Main.instruction[0] + " INVALID_ARGUMENT_NUMBER INVALID_LABEL";
        processInstruction(instructionWithInvalidArguments);
        Assert.assertEquals(0, Main.iPtr);
    }

    @Test
    public void testGetStringValueWithEmptyString() {
        Assert.assertEquals("", getStringValue(""));
    }

    @Test
    public void testGetStringValueWithStringContainingQuotes() {
        String stringWithQuotes = "\"String with quotes\"";
        Assert.assertEquals(stringWithQuotes, getStringValue(stringWithQuotes));
    }

    @Test
    public void testGetStringValueWithStringWithoutQuotes() {
        Assert.assertEquals("String without quotes", getStringValue("String without quotes"));
    }

    @Test
    public void testIsNumericWithNumericString() {
        Assert.assertTrue(isNumeric("123"));
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        Assert.assertFalse(isNumeric("Label1"));
    }

    @Test
    public void testGetJumpValueWithValidLabel() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        int jumpValue = getJumpValue("Label1");
        Assert.assertEquals(1, jumpValue);
    }

    @Test
    public void testGetJumpValueWithInvalidLabel() {
        loadInstructions(VALID_INSTRUCTION_FILE);
        int jumpValue = getJumpValue("INVALID_LABEL");
        Assert.assertEquals(-1, jumpValue);
    }

    @Test
    public void testReadValueWithValidInput() {
        InputStream inputStream = new ByteArrayInputStream("123\n".getBytes());
        System.setIn(inputStream);
        int value = readValue();
        Assert.assertEquals(123, value);
    }

    @Test
    public void testReadValueWithInvalidInput() {
        InputStream inputStream = new ByteArrayInputStream("InvalidInput\n".getBytes());
        System.setIn(inputStream);
        int value = readValue();
        Assert.assertEquals(0, value);
    }

    private void testVariablesValues() {
        for (int i = 0; i < EXpectedValues.length; ++i) {
            Assert.assertEquals(EXpectedValues[i], variable.getVariableValue(Integer.toString(i)));
        }
    }

    private String[] getExpectedInstructions() {
        return new String[]{"mov", "Label1, 0", "set", "0, 1", "inc", "0", "jnz", "1, Label2", "out", "1", "jmp", "0"};
    }
}
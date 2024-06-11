package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MainTest {

    @Test
    public void testLoadInstructionsWithEmptyArg() {
        boolean result = Main.loadInstructions("");
        assertTrue(result);
    }

    @Test
    public void testLoadInstructionsWithInvalidFileName() {
        boolean result = Main.loadInstructions("invalidFile.txt");
        assertFalse(result);
    }

    @Test
    public void testLoadInstructionsWithValidFileName() {
        String fileName = "testInstructions.txt";
        String expectedInstructions[] = { "mov", "x, 0", "add", "x, 1", "inc", "x", "out" };
        Main.loadInstructions(fileName);
        String[] actualInstructions = Main.instruction;
        assertArrayEquals(expectedInstructions, actualInstructions);
    }

    @Test
    public void testGetFileName() {
        String result = Main.getFileName();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testResetInstructions() {
        String[] instructions = new String[5];
        for (int i = 0; i < 5; ++i) {
            instructions[i] = "test";
        }
        Main.instruction = instructions;
        Main.resetInstructions();
        String[] actualInstructions = Main.instruction;
        assertArrayEquals(new String[0], actualInstructions);
    }

    @Test
    public void testSplitArgumentsWithSingleArgument() {
        String original = "mov x 5";
        String[] expectedArgs = { "x", "5" };
        String[] actualArgs = Main.splitArguments(original);
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test
    public void testSplitArgumentsWithMultipleArguments() {
        String original = "mov x y z";
        String[] expectedArgs = { "x", "y", "z" };
        String[] actualArgs = Main.splitArguments(original);
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test
    public void testSplitArgumentsWithQuotes() {
        String original = "mov x \"foo bar\"";
        String[] expectedArgs = { "x", "foo bar" };
        String[] actualArgs = Main.splitArguments(original);
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test
    public void testGetValueWithNumericOperand() {
        int result = Main.getValue("5");
        assertEquals(5, result);
    }

    @Test
    public void testGetValueWithVariableOperand() {
        Main.Variable.setVariableValue("x", 5);
        int result = Main.getValue("x");
        assertEquals(5, result);
    }

    @Test
    public void testGetJumpValueWithLabel() {
        Main.Label.addLabel("startLabel", 10);
        int result = Main.getJumpValue("startLabel");
        assertEquals(10, result);
    }

    @Test
    public void testGetJumpValueWithInvalidLabel() {
        int result = Main.getJumpValue("invalidLabel");
        assertEquals(-1, result);
    }

    @Test
    public void testReadValue() {
        String input = "5";
        int result = Main.readValue();
        assertEquals(5, result);
    }

    @Test
    public void testGetStringValueWithQuotes() {
        String input = "\"foo bar\"";
        String result = Main.getStringValue(input);
        assertEquals("foo bar", result);
    }

    @Test
    public void testGetStringValueWithoutQuotes() {
        String input = "foo";
        String result = Main.getStringValue(input);
        assertEquals("foo", result);
    }

    @Test
    public void testIsNumericWithNumericString() {
        boolean result = Main.isNumeric("5");
        assertTrue(result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        boolean result = Main.isNumeric("foo");
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithMixedString() {
        boolean result = Main.isNumeric("foo5");
        assertFalse(result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        boolean result = Main.isNumeric("");
        assertFalse(result);
    }
}

package jipa;

import org.junit.*;
import static org.junit.Assert.*;

public class MainTest {
    // Tests for Main class methods

    // Test main method
    @Test
    public void testMain() {
        String[] args = {"inputFile.asm"};
        Main.main(args);
        assertEquals(Main.TOTAL_INSTRUCTIONS, Main.iPtr);
    }

    // Test processInstruction method
    @Test
    public void testProcessInstruction() {
        Main.instruction = new String[]{"mov a 0", "inc a"};
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(0, Main.Variable.getVariableValue("a"));
        Main.processInstruction(Main.instruction[Main.iPtr++]);
        assertEquals(1, Main.Variable.getVariableValue("a"));
    }

    // Test loadInstructions with empty file
    @Test(expected = RuntimeException.class)
    public void testLoadInstructionWithEmptyFile() {
        Main.loadInstructions("");
    }

    // Test loadInstructions with incorrect format
    @Test(expected = RuntimeException.class)
    public void testLoadInstructionWithIncorrectFormat() {
        Main.loadInstructions("inc a 42"); // Missing space between opcode and operands.
    }

    // Test loadInstructions with blank lines
    @Test
    public void testLoadInstructionWithBlankLines() {
        Main.loadInstructions("mov a 0\nhalt");
        assertEquals(1, Main.iPtr);
    }

    // Test loadInstructions with invalid opcode
    @Test(expected = RuntimeException.class)
    public void testLoadInstructionWithInvalidOpcode() {
        Main.loadInstructions("xoo a 0"); // Invalid opcode.
    }

    // Test loadInstructions with missing operands
    @Test(expected = RuntimeException.class)
    public void testLoadInstructionWithMissingOperands() {
        Main.loadInstructions("mov a"); // Missing operand.
    }

    // Test loadInstructions with duplicate labels
    @Test(expected = RuntimeException.class)
    public void testLoadInstructionWithDuplicateLabels() {
        Main.loadInstructions("label1: mov a 0\nlabel1: add b 1");
    }

    // Test getValue with invalid variable
    @Test
    public void testGetValueWithInvalidVariable() {
        Main.Variable.addVariable("invalidVar");
        try {
            Main.getValue("invalidVar");
            fail("Expected a RuntimeException.");
        } catch (RuntimeException e) {
            // Expected result.
        }
    }

    // Test getValue with out-of-range variable
    @Test
    public void testGetValueWithOutOfRangeVariable() {
        Main.Variable.addVariable("a");
        Main.Variable.addVariable("b");
        Main.Variable.addVariable("z");
        assertEquals(0, Main.getValue("a"));
        assertEquals(0, Main.getValue("b"));
        try {
            Main.getValue("z");
            fail("Expected a RuntimeException.");
        } catch (RuntimeException e) {
            // Expected result.
        }
    }

    // Test getJumpValue with invalid label
    @Test(expected = RuntimeException.class)
    public void testGetJumpValueWithInvalidLabel() {
        Main.loadInstructions("label1: mov a 0\nlabel2: jmp label3");
        try {
            Main.getJumpValue("label2");
            fail("Expected a RuntimeException.");
        } catch (RuntimeException e) {
            // Expected result.
        }
    }

    // Test getJumpValue with non-existent label
    @Test
    public void testGetJumpValueWithNonExistentLabel() {
        Main.loadInstructions("mov a 0");
        try {
            Main.getJumpValue("label1");
            fail("Expected a RuntimeException.");
        } catch (RuntimeException e) {
            // Expected result.
        }
    }
}
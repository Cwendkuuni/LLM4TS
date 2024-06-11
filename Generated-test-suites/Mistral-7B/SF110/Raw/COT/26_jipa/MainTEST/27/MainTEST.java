package jipa;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {

    @Test
    public void testLoadInstructionsWithValidFileName() {
        Main.loadInstructions("testFile.asm");
        assertEquals(10, Main.TOTAL_INSTSTRUCTIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructionsWithInvalidFileName() {
        Main.loadInstructions("");
    }

    @Test
    public void testLoadInstructionsWithEmptyFileName() {
        Main.loadInstructions("");
        assertEquals(0, Main.TOTAL_INSTSTRUCTIONS);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructionsWithNonExistentFileName() {
        Main.loadInstructions("nonExistentFile.asm");
    }

    @Test
    public void testProcessInstructionWithHaltOpcode() {
        Main.instruction = new String[] { "halt" };
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, System.exitCode());
    }

    @Test
    public void testProcessInstructionWithOutOpcode() {
        Main.instruction = new String[] { "out", "Hello World" };
        System.out = new ByteArrayOutputStream();
        Main.processInstruction(Main.instruction[0]);
        assertEquals("Hello World", ((ByteArrayOutputStream) System.out).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testProcessInstructionWithNullOperand() {
        Main.instruction = new String[] { "mov", "nullVariable", null };
        Main.processInstruction(Main.instruction[0]);
    }

    @Test
    public void testProcessInstructionWithValidOperands() {
        Main.instruction = new String[] { "mov", "a", "5" };
        Main.processInstruction(Main.instruction[0]);
        assertEquals(5, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstructionWithEmptyOperands() {
        Main.instruction = new String[] { "mov", "", "" };
        Main.processInstruction(Main.instruction[0]);
        assertNull(Main.Variable.getVariableValue(""));
    }

    // Add more test cases for other methods and edge cases as needed

}
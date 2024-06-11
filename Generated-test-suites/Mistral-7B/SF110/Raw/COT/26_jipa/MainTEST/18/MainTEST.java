package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    

    @Test
    public void testLoadInstructionsWithValidFileName() {
        String fileName = "test.txt";
        boolean result = Main.loadInstructions(fileName);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructionsWithEmptyFileName() {
        boolean result = Main.loadInstructions("");
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructionsWithInvalidFileName() {
        String fileName = "invalid_file.name";
        boolean result = Main.loadInstructions(fileName);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testLoadInstructionsWithQ() {
        boolean result = Main.loadInstructions("q");
        assertEquals(false, result);
    }

    @Test
    public void testProcessInstructionWithOutOpcode() {
        String instruction = "print Hello";
        Main.processInstruction(instruction);
        assertEquals("Hello", System.out.getHistory(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessInstructionWithInvalidOpcode() {
        String instruction = "invalid_opcode";
        Main.processInstruction(instruction);
        fail("Expected IllegalStateException but got: null");
    }

    @Test
    public void testProcessInstructionWithHaltOpcode() {
        String instruction = "halt";
        Main.processInstruction(instruction);
        // The program should terminate after executing this instruction
        fail("Program did not terminate");
    }

    // Add test cases for other opcodes and their respective edge cases

    
}
package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        Main.Variable.setVariableValue("x", 5);
        Main.processInstruction("mov x y");
        assertEquals(5, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_mov_empty() {
        Main.processInstruction("mov ");
        // No operation, should not change the state
        assertEquals(0, Main.Variable.getVariableValue("x"));
        assertEquals(0, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_mov_invalid() {
        Main.processInstruction("mov a b");
        // Invalid opcode, should not change the state
        assertEquals(0, Main.Variable.getVariableValue("x"));
        assertEquals(0, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.Variable.setVariableValue("x", 5);
        Main.processInstruction("inc x");
        assertEquals(6, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_inc_empty() {
        Main.processInstruction("inc ");
        // No operand, should not change the state
        assertEquals(0, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_inc_invalid() {
        Main.processInstruction("inc a");
        // Invalid opcode, should not change the state
        assertEquals(0, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_out() {
        Main.processInstruction("out x");
        String output = System.out.getForeignOutput();
        assertEquals("5", output);
        System.out.reset();
    }

    @Test
    public void testProcessInstruction_out_empty() {
        Main.processInstruction("out ");
        String output = System.out.getForeignOutput();
        assertEquals("", output);
        System.out.reset();
    }

    @Test
    public void testProcessInstruction_out_invalid() {
        Main.Variable.setVariableValue("x", 5);
        Main.processInstruction("out y");
        String output = System.out.getForeignOutput();
        assertEquals("", output);
        System.out.reset();
    }

    // Add more test cases here for other instructions like in, set, dec, jmp, jnz, etc.

    @Test
    public void testLoadInstructions_file() {
        String fileName = "testData.txt";
        Main.loadInstructions(fileName);
        assertNotNull(Main.instruction);
        assertTrue(Main.instruction.length > 0);
    }

    @Test
    public void testLoadInstructions_empty() {
        Main.loadInstructions("");
        assertNotNull(Main.instruction);
        assertTrue(Main.instruction.length == 0);
    }

    @Test(expected = Exception.class)
    public void testLoadInstructions_invalidFile() {
        Main.loadInstructions("invalidFile.txt");
    }

    @Test
    public void testResetInstructions() {
        Main.loadInstructions("testData.txt");
        Main.resetInstructions();
        assertNotNull(Main.instruction);
        assertTrue(Main.instruction.length == 0);
        for (String instruction : Main.instruction) {
            assertEquals("", instruction);
        }
    }

    @Test
    public void testGetFileName() {
        String input = "Enter the full path to the file or type q to quit :";
        Main.print(input);
        String response = "testData.txt";
        assertEquals(response, Main.getFileName());
    }

    @Test
    public void testGetFileName_empty() {
        Main.processInstruction("getFileName");
        String response = "";
        assertEquals(response, Main.getFileName());
    }

    @Test
    public void testGetFileName_quit() {
        Main.processInstruction("getFileName");
        Main.processInstruction("q");
        String response = "";
        assertEquals(response, Main.getFileName());
    }

    // Add more test cases here to cover other methods in the Main class like removeComment, splitArguments, getValue, getJumpValue, readValue, getStringValue, isNumeric, etc.

}
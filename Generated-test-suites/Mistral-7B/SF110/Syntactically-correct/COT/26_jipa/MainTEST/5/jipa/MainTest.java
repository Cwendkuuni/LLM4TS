package jipa;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class MainTest {
    

    // Test for main method
    @Test
    public void testMain() {
        String args[] = {"testFile.asm"};
        Main.main(args);
        // Add assertions here to check the expected behavior of the main method
    }

    // Test for loadInstructions method
    @Test
    public void testLoadInstructions() {
        String fileName = "testFile.asm";
        assertTrue(Main.loadInstructions(fileName));
        // Add assertions here to check the behavior of the loadInstructions method
    }

    // Test for processInstruction method with valid operands
    @Test
    public void testProcessInstructionValidOperands() {
        String instruction = "mov A 0";
        Main.processInstruction(instruction);
        assertEquals(0, Main.Variable.getVariableValue("A"));
        // Add more assertions here for other valid instructions and their expected behavior
    }

    // Test for processInstruction method with invalid opcode
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionInvalidOpcode() {
        String instruction = "invalidOpcode A";
        Main.processInstruction(instruction);
        // No need to add assertions here as an exception should be thrown
    }

    // Test for processInstruction method with invalid number of operands
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionInvalidNumberOfOperands() {
        String instruction = "mov A ";
        Main.processInstruction(instruction);
        // No need to add assertions here as an exception should be thrown
    }

    // Test for processInstruction method with invalid variable name
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionInvalidVariableName() {
        String instruction = "mov %%INVALID% A";
        Main.processInstruction(instruction);
        // No need to add assertions here as an exception should be thrown
    }

    // Test for processInstruction method with invalid data type
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionInvalidDataType() {
        String instruction = "mov A 10.5";
        Main.processInstruction(instruction);
        // No need to add assertions here as an exception should be thrown
    }

    // Test for processInstruction method with invalid jump value
    @Test(expected = IndexOutOfBoundsException.class)
    public void testProcessInstructionInvalidJumpValue() {
        Main.instruction = new String[10];
        String instruction = "jmp 12";
        Main.processInstruction(instruction);
        // No need to add assertions here as an exception should be thrown
    }

    // Test for getFileName method
    @Test
    public void testGetFileName() {
        String fileName = Main.getFileName();
        // Add assertions here to check the behavior of the getFileName method
    }

    
}
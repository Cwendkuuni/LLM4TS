package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testLoadInstructionsWithValidFile() {
        String fileName = "testFile.txt";
        Main.loadInstructions(fileName);
        assertEquals(7, Main.instruction.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructionsWithEmptyFile() {
        Main.loadInstructions("");
    }

    @Test
    public void testLoadInstructionsWithInvalidFile() {
        String fileName = "invalidFile.txt";
        Main.loadInstructions(fileName);
        assertEquals(0, Main.instruction.length);
    }

    @Test
    public void testProcessInstructionWithHaltOpCode() {
        String instruction = "halt";
        Main.processInstruction(instruction);
        fail("Expected System.exit(0) to be called");
    }

    @Test
    public void testProcessInstructionWithOutOpCode() {
        String instruction = "out x";
        String expectedOutput = "x";
        System.out.flush();
        Main.processInstruction(instruction);
        assertEquals(expectedOutput, System.out.getBuffer().toString());
    }

    @Test
    public void testProcessInstructionWithInOpCode() {
        String instruction = "in x";
        int expectedValue = 5;
        int actualValue = Main.readValue();
        assertEquals(expectedValue, actualValue);
        String operand = "x";
        Variable.setVariableValue(operand, actualValue);
    }

    @Test
    public void testProcessInstructionWithMovOpCode() {
        String opcode = "mov";
        String sourceOperand = "x";
        String destinationOperand = "y";
        int expectedValue = 5;
        Variable.setVariableValue(sourceOperand, expectedValue);
        Main.processInstruction(opcode + " y " + sourceOperand);
        assertEquals(expectedValue, Variable.getVariableValue(destinationOperand));
        assertEquals(0, Variable.getVariableValue(sourceOperand));
    }

    @Test
    public void testProcessInstructionWithJmpOpCode() {
        String instruction = "jmp 5";
        int expectedJumpValue = 5;
        Main.iPtr = 0;
        Main.processInstruction(instruction);
        assertEquals(expectedJumpValue, Main.iPtr);
    }

    @Test
    public void testProcessInstructionWithJzOpCode() {
        String opcode = "jz";
        String sourceOperand = "z";
        String destinationLabel = "label";
        int expectedJumpValue = 5;
        Variable.setVariableValue(sourceOperand, 0);
        Main.processInstruction(opcode + " " + sourceOperand + " " + destinationLabel);
        Main.processInstruction("jmp " + destinationLabel);
        assertEquals(expectedJumpValue, Main.iPtr);

        Variable.setVariableValue(sourceOperand, 5);
        Main.processInstruction(opcode + " " + sourceOperand + " " + destinationLabel);
        assertEquals(Main.iPtr, 0);
    }

    @Test
    public void testProcessInstructionWithInvalidOpCode() {
        String instruction = "invalidOpCode x";
        Main.processInstruction(instruction);
        fail("Expected an exception to be thrown");
    }
}
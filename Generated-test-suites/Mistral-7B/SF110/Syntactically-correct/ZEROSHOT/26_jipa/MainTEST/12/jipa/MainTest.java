package jipa;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    @BeforeClass
    public static void setUp() {
        // Initialize the Main class with some test data
        // You may need to modify this code to suit your specific test scenario
        Main.TOTAL_INSTRUCTIONS = 10;
        Main.instruction = new String[10];
        Main.instruction[0] = "mov x 0";
        Main.instruction[1] = "set y 5";
        Main.instruction[2] = "inc x";
        Main.instruction[3] = "inc x";
        Main.instruction[4] = "mov z x";
        Main.instruction[5] = "mov a y";
        Main.instruction[6] = "add a z";
        Main.instruction[7] = "out a";
        Main.instruction[8] = "jmp 0"; // Infinite loop for testing purposes
        Main.instruction[9] = "halt";
        Main.iPtr = 0;
    }

    @Test
    public void testProcessInstruction_mov() {
        String opcode = "mov";
        String[] operand = {"x", "0"};
        processInstructionAndCheck(opcode, operand, 0, 0);
    }

    @Test
    public void testProcessInstruction_set() {
        String opcode = "set";
        String[] operand = {"y", "5"};
        processInstructionAndCheck(opcode, operand, 5, null);
    }

    @Test
    public void testProcessInstruction_inc() {
        String opcode = "inc";
        String operand = "x";
        processInstructionAndCheck(opcode, null, 1, 0);
    }

    @Test
    public void testProcessInstruction_add() {
        String opcode = "add";
        String[] operand = {"a", "z"};
        processInstructionAndCheck(opcode, operand, 0, 0); // Assuming z has a value before this instruction
    }

    @Test
    public void testProcessInstruction_out() {
        String opcode = "out";
        String operand = "a";
        processInstructionAndCheck(opcode, null, 0, 5); // Assuming a has a value before this instruction
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstruction_invalidOpcode() {
        String opcode = "invalid";
        processInstructionAndCheck(opcode, null, 0, 0);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstruction_missingOperand() {
        String opcode = "inc";
        processInstructionAndCheck(opcode, null, 0, 0);
    }

    @Test
    public void testProcessInstruction_jmp() {
        String opcode = "jmp";
        String label = "testLabel";
        processInstructionAndJumpToLabel(opcode, label);
    }

    @Test
    public void testProcessInstruction_jnz() {
        String opcode = "jnz";
        String label1 = "label1";
        String label2 = "label2";
        int expectedIPtr = 4; // The instruction index after the jnz instruction
        processInstructionAndJumpToLabelIfConditionMet(opcode, "x", 0, label1, label2, expectedIPtr);
    }

    private void processInstructionAndCheck(String opcode, String[] operand, int expectedValue1, Integer expectedValue2) {
        processInstruction(opcode, operand);
        switch (opcode.toLowerCase()) {
            case "mov":
                assertEquals(expectedValue1, Variable.getVariableValue(operand[0]));
                break;
            case "set":
                assertEquals(expectedValue2, Variable.getVariableValue(operand[0]));
                break;
            case "inc":
                assertEquals(expectedValue1 + 1, Variable.getVariableValue(operand[0]));
                break;
            case "add":
                int value1 = Variable.getVariableValue(operand[0]);
                int value2 = Variable.getVariableValue(operand[1]);
                assertEquals(value1 + value2, Variable.getVariableValue(operand[0]));
                break;
            case "out":
                String expectedOutput = (expectedValue1 + "").equals("") ? "Integer value not provided for out instruction" : Integer.toString(expectedValue1);
                assertEquals(expectedOutput, System.out.getHistory().get(0));
                break;
            default:
                fail("Unexpected instruction processed: " + opcode);
        }
    }

    private void processInstructionAndJumpToLabel(String opcode, String label) {
        processInstruction(opcode, null);
        assertEquals(Integer.parseInt(label), Main.iPtr);
    }

    private void processInstructionAndJumpToLabelIfConditionMet(String opcode, String variable, int expectedValue, String label1, String label2, int expectedIPtr) {
        Variable.setVariableValue(variable, expectedValue);
        processInstruction(opcode, null);
        assertEquals(expectedIPtr, Main.iPtr);
    }

    private void processInstruction(String opcode, String[] operand) {
        Main.processInstruction(opcode, operand);
    }
}
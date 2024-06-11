package jipa;

import org.junit.Test;
import org.junit.Assert.*;
import static jipa.Main.opcodes.*;

public class MainTest {

    @Test
    public void testLoadInstructions() {
        String fileName = "testInstructions.txt";
        boolean result = Main.loadInstructions(fileName);
        assertTrue(result);
    }

    @Test
    public void testLoadInstructionsEmptyFileName() {
        boolean result = Main.loadInstructions("");
        assertFalse(result);
    }

    @Test
    public void testLoadInstructionsInvalidFileName() {
        String fileName = "testInstructions.txtq";
        boolean result = Main.loadInstructions(fileName);
        assertFalse(result);
    }

    @Test
    public void testProcessInstructionHalt() {
        String[] instructions = {"halt"};
        Main.instruction = instructions;
        Main.iPtr = 0;
        Main.main(new String[0]);
        assertEquals(0, Main.iPtr);
        assertTrue(System.exit.called());
    }

    @Test
    public void testProcessInstructionOut() {
        String[] instructions = {"out", "Hello"};
        Main.instruction = instructions;
        Main.iPtr = 0;
        Main.main(new String[0]);
        assertEquals("Hello\n", System.out.getInnerException().getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionOutInvalidOperand() {
        String[] instructions = {"out", "invalid"};
        Main.instruction = instructions;
        Main.iPtr = 0;
        Main.main(new String[0]);
    }

    @Test
    public void testProcessInstructionIn() {
        String[] instructions = {"in", "variableName"};
        String expectedValue = "testValue";
        Main.instruction = instructions;
        Main.iPtr = 0;
        Main.Variable.setVariableValue("variableName", 0);
        Main.main(new String[0]);
        assertEquals(expectedValue, Main.Variable.getVariableValue("variableName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionInInvalidOperand() {
        String[] instructions = {"in", "invalid"};
        Main.instruction = instructions;
        Main.iPtr = 0;
        Main.main(new String[0]);
    }

    @Test
    public void testProcessInstructionMov() {
        String[] instructions = {"mov", "sourceVariable", "destinationVariable"};
        String sourceValue = "sourceValue";
        String destinationValue = "destinationValue";
        Main.instruction = instructions;
        Main.Variable.setVariableValue("sourceVariable", sourceValue);
        Main.Variable.setVariableValue("destinationVariable", destinationValue);
        Main.iPtr = 0;
        Main.main(new String[0]);
        assertEquals(sourceValue, Main.Variable.getVariableValue("destinationVariable"));
        assertEquals(destinationValue, Main.Variable.getVariableValue("sourceVariable"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionMovInvalidOperands() {
        String[] instructions = {"mov", "sourceVariable", "destinationVariable"};
        Main.instruction = instructions;
        Main.iPtr = 0;
        Main.main(new String[0]);
    }

    // Add other test cases for the remaining methods in Main class

}
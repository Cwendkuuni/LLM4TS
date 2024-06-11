package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    
    
    @Test
    public void testLoadInstructionsWithValidFileName() {
        String fileName = "testInstructions.txt";
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
        String fileName = "testInstructionsq.txt";
        boolean result = Main.loadInstructions(fileName);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testProcessInstructionWithValidOpcodeAndNoOperands() {
        String instruction = "hal";
        Main.iPtr = 0;
        Main.processInstruction(instruction);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstructionWithValidOpcodeAndOneOperand() {
        String instruction = "out var0";
        Main.iPtr = 0;
        Main.instruction = new String[]{"out var0", "mov var1 1"};
        Variable.setVariableValue("var0", "Hello");
        Main.processInstruction(instruction);
        assertEquals("Hello", System.out.getHistogram().get(0).toString());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstructionWithInvalidOpcode() {
        String instruction = "invalid";
        Main.iPtr = 0;
        Main.processInstruction(instruction);
        fail("Expected ArrayIndexOutOfBoundsException but got: " + Main.iPtr);
    }

    @Test
    public void testProcessInstructionWithValidOpcodeAndTwoOperands() {
        String instruction = "mov var0 var1";
        Main.iPtr = 0;
        Main.instruction = new String[]{"mov var0 var1", "mov var1 1", "mov var0 0"};
        Variable.setVariableValue("var1", 5);
        Main.processInstruction(instruction);
        assertEquals(5, Variable.getVariableValue("var0"));
    }

    @Test(expected = NumberFormatException.class)
    public void testProcessInstructionWithInvalidOperandValue() {
        String instruction = "mov var0 var1";
        Main.iPtr = 0;
        Main.instruction = new String[]{"mov var0 var1", "mov var1 x", "mov var0 0"};
        Variable.setVariableValue("var1", "hello");
        try {
            Main.processInstruction(instruction);
            fail("Expected NumberFormatException but got: " + Variable.getVariableValue("var0"));
        } catch (NumberFormatException e) {
            // Expected
        }
    }
    
    
}
package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MainTest {

    @Test
    public void testLoadInstructions() {
        String[] instructions = Main.loadInstructions("test.txt");
        assertEquals(10, instructions.length);
    }

    @Test(expected = RuntimeException.class)
    public void testLoadInstructionsInvalidFile() {
        Main.loadInstructions(null);
    }

    @Test
    public void testProcessInstructionHalt() {
        Main.instruction = new String[] { "halt" };
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testProcessInstructionOut() {
        Main.instruction = new String[] { "out", "hello" };
        Main.processInstruction(Main.instruction[0]);
        assertEquals("hello", System.out.getHistogram().get(0).toString());
    }

    @Test
    public void testProcessInstructionIn() {
        Main.instruction = new String[] { "in", "x" };
        int value = Main.readValue();
        assertEquals(Character.toString((char) System.in.read()).toLowerCase(), Integer.toString(value));
    }

    @Test
    public void testProcessInstructionMov() {
        Main.instruction = new String[] { "mov", "y", "x" };
        int expectedX = 10;
        int expectedY = 20;
        Variable.setVariableValue("x", expectedX);
        Variable.setVariableValue("y", expectedY);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(expectedX, Variable.getVariableValue("x"));
        assertEquals(expectedY, Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstructionSet() {
        Main.instruction = new String[] { "set", "x", "10" };
        Variable.setVariableValue("x", 0);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(10, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstructionDel() {
        Main.instruction = new String[] { "del", "x" };
        Variable.removeVariable("x");
        Main.processInstruction(Main.instruction[0]);
        assertNull(Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstructionArr() {
        Main.instruction = new String[] { "arr", "array", "5" };
        Variable.addVariable("array");
        for (int i = 0; i < 5; ++i) {
            Variable.addVariable(Variable.getVariableName(Variable.getVariableCount() - 1) + Integer.toString(i + 1));
        }
        String[] expectedArray = new String[] { "array0", "array1", "array2", "array3", "array4" };
        String[] resultArray = new String[Variable.getVariableCount()];
        for (int i = 0; i < Variable.getVariableCount(); ++i) {
            resultArray[i] = Variable.getVariableName(i);
        }
        assertArrayEquals(expectedArray, resultArray);
    }

    @Test
    public void testProcessInstructionJmp() {
        Main.instruction = new String[] { "jmp", "5" };
        Main.iPtr = 1;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(6, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJnz() {
        Main.instruction = new String[] { "jnz", "0", "5" };
        Variable.setVariableValue("0", 5);
        Main.iPtr = 1;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(6, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJz() {
        Main.instruction = new String[] { "jz", "0", "5" };
        Variable.setVariableValue("0", 0);
        Main.iPtr = 1;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJe() {
        Main.instruction = new String[] { "je", "0", "5" };
        Variable.setVariableValue("0", 0);
        Variable.setVariableValue("1", 0);
        Main.iPtr = 1;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(6, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJne() {
        Main.instruction = new String[] { "jne", "0", "5" };
        Variable.setVariableValue("0", 0);
        Variable.setVariableValue("1", 1);
        Main.iPtr = 1;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstructionCmp() {
        Main.instruction = new String[] { "cmp", "1", "0", "compare" };
        Variable.setVariableValue("0", 1);
        Variable.setVariableValue("1", 1);
        Variable.setVariableValue("compare", 0);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Variable.getVariableValue("compare"));

        Variable.setVariableValue("compare", 0);
        Variable.setVariableValue("1", 2);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("compare"));
    }

}
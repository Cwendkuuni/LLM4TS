package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        Main.instruction = new String[]{"mov", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 10);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(10, Main.Variable.getVariableValue("B"));
        assertEquals(0, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mov_null() {
        Main.instruction = new String[]{"mov", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 10);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertNull(Main.Variable.getVariableValue("B"));
    }

    @Test
    public void testProcessInstruction_set() {
        Main.instruction = new String[]{"set", "A", "10"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 0);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(10, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_set_null() {
        Main.instruction = new String[]{"set", "A", "null"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 10);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertNull(Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.instruction = new String[]{"inc", "A"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(6, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_dec() {
        Main.instruction = new String[]{"dec", "A"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(4, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_add() {
        Main.instruction = new String[]{"add", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(8, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_sub() {
        Main.instruction = new String[]{"sub", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mul() {
        Main.instruction = new String[]{"mul", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(15, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_div() {
        Main.instruction = new String[]{"div", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 15);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(5, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mod() {
        Main.instruction = new String[]{"mod", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 15);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(0, Main.Variable.getVariableValue("A"));
        assertTrue(Main.Variable.getVariableValue("B") == 3 || Main.Variable.getVariableValue("B") == -3);
    }

    @Test
    public void testProcessInstruction_xor() {
        Main.instruction = new String[]{"xor", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(8, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_or() {
        Main.instruction = new String[]{"or", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(6, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_and() {
        Main.instruction = new String[]{"and", "A", "B"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.instruction = new String[]{"jmp", "5"};
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(5, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        Main.instruction = new String[]{"jz", "0"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);
        Main.Variable.setVariableValue("A", 0);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        Main.instruction = new String[]{"je", "1", "2"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.iPtr);
        Main.Variable.setVariableValue("B", 6);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        Main.instruction = new String[]{"jne", "1", "2"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
        Main.Variable.setVariableValue("B", 6);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmpIfZero() {
        Main.instruction = new String[]{"jnz", "1", "0"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
        Main.Variable.setVariableValue("A", 0);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmpIfNotZero() {
        Main.instruction = new String[]{"jz", "1", "0"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);
        Main.Variable.setVariableValue("A", 0);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmpIfGreater() {
        Main.instruction = new String[]{"ja", "1", "2"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.iPtr);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmpIfGreaterOrEqual() {
        Main.instruction = new String[]{"jae", "1", "2"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmpIfLess() {
        Main.instruction = new String[]{"jb", "1", "2"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmpIfLessOrEqual() {
        Main.instruction = new String[]{"jbe", "1", "2"};
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 5);
        Main.Variable.setVariableValue("B", 3);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
        Main.Variable.setVariableValue("B", 5);
        Main.processInstruction(Main.instruction[Main.iPtr]);
        assertEquals(2, Main.iPtr);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstruction_invalidOpcode() {
        Main.instruction = new String[]{"invalid", "A"};
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[Main.iPtr]);
    }

    @Test
    public void testLoadInstructions() {
        Main.loadInstructions("test.txt");
        assertEquals(10, Main.instruction.length);
        assertEquals("mov", Main.instruction[0]);
        assertEquals("A", Main.instruction[1]);
        assertEquals("B", Main.instruction[2]);
        assertEquals("set", Main.instruction[3]);
        assertEquals("C", Main.instruction[4]);
        assertEquals("5", Main.instruction[5]);
        assertEquals("jmp", Main.instruction[6]);
        assertEquals("1", Main.instruction[7]);
        assertEquals("0", Main.instruction[8]);
        assertEquals("", Main.instruction[9]);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructions_fileNotFound() {
        Main.loadInstructions("non_existent_file.txt");
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadInstructions_invalidNumberFormat() {
        Main.loadInstructions("test_invalid_number.txt");
    }

    @Test
    public void testGetStringValue() {
        assertEquals("\"Hello, World!\"", Main.getStringValue("\"Hello, World!\""));
        assertEquals("\"Hello\" and \"World\"", Main.getStringValue("\"Hello\" and \"World\""));
    }

    @Test
    public void testGetStringValue_null() {
        assertEquals("", Main.getStringValue(null));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(Main.isNumeric("1"));
        assertTrue(Main.isNumeric("10"));
        assertTrue(Main.isNumeric("100"));
        assertTrue(Main.isNumeric("-1"));
        assertTrue(Main.isNumeric("-10"));
        assertTrue(Main.isNumeric("-100"));
        assertFalse(Main.isNumeric("A"));
        assertFalse(Main.isNumeric("B"));
        assertFalse(Main.isNumeric("ABC"));
        assertFalse(Main.isNumeric(""));
    }
}
package jipa;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class MainTest {

    private Main main;

    @Before
    public void setup() {
        main = new Main();
    }

    @Test
    public void testOpcodes() {
        assertEquals(Main.opcodes.halt, Main.opcodes.toOpcode("halt"));
        assertEquals(Main.opcodes.mov, Main.opcodes.toOpcode("mov"));
        assertEquals(Main.opcodes.set, Main.opcodes.toOpcode("set"));
        // ... test other opcodes here
    }

    @Test
    public void testProcessInstruction_mov() {
        main.iPtr = 0;
        main.instruction = new String[] {"mov A 0", "inc A"};
        main.Variable = new Variable();
        main.Variable.setVariableValue("A", 5);
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(5, main.Variable.getVariableValue("A"));
        assertEquals(0, main.Variable.getVariableValue("0"));
    }

    @Test
    public void testProcessInstruction_inc() {
        main.iPtr = 1;
        main.instruction = new String[] {"mov A 0", "inc A"};
        main.Variable = new Variable();
        main.Variable.setVariableValue("A", 5);
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(6, main.Variable.getVariableValue("A"));
        assertEquals(0, main.Variable.getVariableValue("0"));
    }

    @Test
    public void testProcessInstruction_set() {
        main.iPtr = 0;
        main.instruction = new String[] {"set A 5", "mov B A"};
        main.Variable = new Variable();
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(5, main.Variable.getVariableValue("A"));
        assertEquals(5, main.Variable.getVariableValue("B"));
    }

    @Test
    public void testProcessInstruction_del() {
        main.iPtr = 0;
        main.instruction = new String[] {"set A 5", "del A"};
        main.Variable = new Variable();
        main.processInstruction(main.instruction[main.iPtr]);
        assertNull(main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        main.iPtr = 0;
        main.instruction = new String[] {"jmp 10", "mov A 5", "mov B A", "inc A"};
        main.Variable = new Variable();
        main.processInstruction(main.instruction[main.iPtr]);
        main.iPtr = 2;
        main.processInstruction(main.instruction[main.iPtr]);
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(6, main.Variable.getVariableValue("A"));
        assertEquals(5, main.Variable.getVariableValue("B"));
    }

    @Test
    public void testProcessInstruction_jnz() {
        main.iPtr = 0;
        main.instruction = new String[] {"set A 5", "jnz 10", "mov B A", "inc A", "mov C 10"};
        main.Variable = new Variable();
        main.processInstruction(main.instruction[main.iPtr]);
        main.processInstruction(main.instruction[main.iPtr]);
        main.processInstruction(main.instruction[main.iPtr]);
        main.iPtr = 4;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(6, main.Variable.getVariableValue("A"));
        assertEquals(5, main.Variable.getVariableValue("B"));
        assertEquals(10, main.Variable.getVariableValue("C"));
    }

    @Test
    public void testLoadInstructions_emptyFileName() {
        main.instruction = new String[0];
        main.loadInstructions("");
        assertTrue(main.instruction.length == 0);
    }

    @Test
    public void testLoadInstructions_validFileName() {
        main.instruction = new String[10];
        main.loadInstructions("test.txt");
        // Add assertions here to check the loaded instructions from the file.
    }

    @Test
    public void testLoadInstructions_invalidFileName() {
        main.instruction = new String[0];
        String fileName = "test";
        try {
            main.loadInstructions(fileName);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testGetStringValue_emptyString() {
        assertEquals("", main.getStringValue(""));
    }

    @Test
    public void testGetStringValue_stringWithQuotes() {
        assertEquals("foo", main.getStringValue("\"foo\""));
    }

    @Test
    public void testGetStringValue_stringWithQuotesInMiddle() {
        assertEquals("foo bar", main.getStringValue("\"foo\" bar"));
    }

    @Test
    public void testGetStringValue_stringWithoutQuotes() {
        assertEquals("foo", main.getStringValue("foo"));
    }

    @Test
    public void testGetStringValue_stringWithMultipleArguments() {
        assertEquals("foo bar baz", main.getStringValue("foo bar baz"));
    }

    @Test
    public void testIsNumeric_numericString() {
        assertTrue(main.isNumeric("123"));
    }

    @Test
    public void testIsNumeric_nonNumericString() {
        assertFalse(main.isNumeric("foo"));
    }

    @Test
    public void testIsNumeric_numericVariable() {
        main.Variable = new Variable();
        main.Variable.setVariableValue("A", 123);
        assertTrue(main.isNumeric("A"));
    }

    @Test
    public void testIsNumeric_nonNumericVariable() {
        main.Variable = new Variable();
        main.Variable.setVariableValue("A", "foo");
        assertFalse(main.isNumeric("A"));
    }
}
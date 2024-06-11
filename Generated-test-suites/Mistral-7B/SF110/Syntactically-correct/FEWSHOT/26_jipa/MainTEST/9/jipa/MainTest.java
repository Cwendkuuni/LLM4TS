package jipa;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
    private Main main;
    private Variable variable;

    @Before
    public void setup() {
        main = new Main();
        variable = new Variable();
        main.variable = variable;
    }

    @Test
    public void testProcessInstruction_halt() {
        main.processInstruction("halt");
        assertEquals(0, main.iPtr);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
        assertTrue(main.instruction.length == 0);
    }

    @Test
    public void testProcessInstruction_out() {
        main.instruction = new String[] { "out", "foo" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals("foo", System.out.getHistory(0));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_in() {
        main.instruction = new String[] { "in", "x" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(48, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_mov() {
        main.instruction = new String[] { "mov", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(48, variable.getVariableValue("x"));
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_cpy() {
        main.instruction = new String[] { "cpy", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(48, variable.getVariableValue("x"));
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_set() {
        main.instruction = new String[] { "set", "x", "5" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(5, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_del() {
        main.instruction = new String[] { "del", "x" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertNull(variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_arr() {
        main.instruction = new String[] { "arr", "arr", "3" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertSame(variable, main.variable.getVariable("arr"));
        assertEquals(3, main.variable.getVariableSize("arr"));

        main.instruction = new String[] { "set", "arr[0]", "1" };
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(1, main.variable.getVariableValue("arr[0]"));

        main.instruction = new String[] { "set", "arr[1]", "2" };
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(2, main.variable.getVariableValue("arr[1]"));
    }

    @Test
    public void testProcessInstruction_inc() {
        main.instruction = new String[] { "inc", "x" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(49, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_dec() {
        main.instruction = new String[] { "dec", "x" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(47, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_add() {
        main.instruction = new String[] { "add", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(53, variable.getVariableValue("x"));
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(54, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_sub() {
        main.instruction = new String[] { "sub", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(51, variable.getVariableValue("x"));
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(5, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_mul() {
        main.instruction = new String[] { "mul", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(243, variable.getVariableValue("x"));
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(12129, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_div() {
        main.instruction = new String[] { "div", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(0.5, variable.getVariableValue("x"), 0.001);
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(0.5, variable.getVariableValue("x"), 0.001);
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_mod() {
        main.instruction = new String[] { "mod", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(3, variable.getVariableValue("x"));
        assertEquals(4, variable.getVariableValue("y"));
        assertEquals(1, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_xor() {
        main.instruction = new String[] { "xor", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(49, variable.getVariableValue("x"));
        assertEquals(47, variable.getVariableValue("y"));
        assertEquals(4, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_or() {
        main.instruction = new String[] { "or", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(49, variable.getVariableValue("x"));
        assertEquals(48, variable.getVariableValue("y"));
        assertEquals(51, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_and() {
        main.instruction = new String[] { "and", "x", "y" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(0, variable.getVariableValue("x"));
        assertEquals(0, variable.getVariableValue("y"));
        assertEquals(0, variable.getVariableValue("x"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_jmp() {
        main.instruction = new String[] { "jmp", "3" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(3, main.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        main.instruction = new String[] { "jnz", "0", "5" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(5, main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        main.instruction = new String[] { "jz", "5", "2" };
        main.iPtr = 0;
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(2, main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        main.instruction = new String[] { "je", "1", "6" };
        main.iPtr = 0;
        main.variable.setVariableValue("x", 1);
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(6, main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        main.instruction = new String[] { "jne", "1", "6" };
        main.iPtr = 0;
        main.variable.setVariableValue("x", 2);
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        main.instruction = new String[] { "cmp", "1", "2", "z" };
        main.iPtr = 0;
        main.variable.setVariableValue("x", 1);
        main.processInstruction(main.instruction[main.iPtr]);
        assertEquals(0, variable.getVariableValue("z"));
        assertEquals(1, main.iPtr);
    }

    @Test
    public void testProcessInstruction_loadInstructions() {
        main.loadInstructions("test.txt");
        assertEquals(10, main.iPtr);
        assertEquals("arr", main.instruction[5]);
        assertEquals("mov", main.instruction[6]);
    }
}

class Variable {
    private Map<String, Integer> variables;

    public Variable() {
        variables = new HashMap<>();
    }

    public void setVariableValue(String name, int value) {
        variables.put(name, value);
    }

    public int getVariableValue(String name) {
        return variables.getOrDefault(name, 0);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public int getVariableSize(String name) {
        return variables.size();
    }
}
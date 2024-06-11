package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        Main.instruction = new String[]{"mov A 0"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mov_reverse() {
        Main.instruction = new String[]{"mov 0 A"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mov_sameVariable() {
        Main.instruction = new String[]{"mov A A"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_set() {
        Main.instruction = new String[]{"set A 5"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(5, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_set_nonExistentVariable() {
        Main.instruction = new String[]{"set X 5"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertNull(Variable.getVariableValue("X"));
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.instruction = new String[]{"inc A"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_dec() {
        Main.instruction = new String[]{"dec A"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(-1, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_add() {
        Main.instruction = new String[]{"add A B"};
        Variable.setVariableValue("B", 3);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(3, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_sub() {
        Main.instruction = new String[]{"sub A B"};
        Variable.setVariableValue("B", 3);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(-3, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mul() {
        Main.instruction = new String[]{"mul A B"};
        Variable.setVariableValue("B", 3);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(15, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_div() {
        Main.instruction = new String[]{"div A B"};
        Variable.setVariableValue("B", 3);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(5, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_mod() {
        Main.instruction = new String[]{"mod A B"};
        Variable.setVariableValue("B", 3);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(2, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_xor() {
        Main.instruction = new String[]{"xor A B"};
        Variable.setVariableValue("B", 3);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(2, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_or() {
        Main.instruction = new String[]{"or A B"};
        Variable.setVariableValue("B", 1);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_and() {
        Main.instruction = new String[]{"and A B"};
        Variable.setVariableValue("B", 0);
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.instruction = new String[]{"jmp 10"};
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(10, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        Main.instruction = new String[]{"jnz A 5"};
        Variable.setVariableValue("A", 3);
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(5, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        Main.instruction = new String[]{"jz A 5"};
        Variable.setVariableValue("A", 0);
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(5, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        Main.instruction = new String[]{"je A B"};
        Variable.setVariableValue("A", 3);
        Variable.setVariableValue("B", 3);
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        Main.instruction = new String[]{"jne A B"};
        Variable.setVariableValue("A", 3);
        Variable.setVariableValue("B", 4);
        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        Main.instruction = new String[]{"cmp A B"};
        Variable.setVariableValue("B", 3);
        Variable.setVariableValue("A", 3);
        Variable.setVariableValue("C", 0);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Variable.getVariableValue("C"));
    }

    @Test
    public void testProcessInstruction_cmp_reverse() {
        Main.instruction = new String[]{"cmp B A"};
        Variable.setVariableValue("B", 3);
        Variable.setVariableValue("A", 3);
        Variable.setVariableValue("C", 0);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("C"));
    }

    @Test
    public void testProcessInstruction_cmp_differentValues() {
        Main.instruction = new String[]{"cmp A B"};
        Variable.setVariableValue("B", 3);
        Variable.setVariableValue("A", 4);
        Variable.setVariableValue("C", 0);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("C"));
    }

    @Test
    public void testProcessInstruction_cmp_sameValue() {
        Main.instruction = new String[]{"cmp A A"};
        Variable.setVariableValue("A", 3);
        Variable.setVariableValue("C", 0);
        Main.processInstruction(Main.instruction[0]);
        assertEquals(0, Variable.getVariableValue("C"));
    }

    @Test
    public void testProcessInstruction_arr() {
        Main.instruction = new String[]{"arr A 2"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(2, Variable.countVariables());
        assertEquals("A0", Variable.getVariableName(0));
        assertEquals("A1", Variable.getVariableName(1));
    }

    @Test
    public void testProcessInstruction_arr_elements() {
        Main.instruction = new String[]{"arr A 2", "set A0 1", "set A1 2"};
        Variable.reset();
        Main.processInstructions(Main.instruction);
        assertEquals(2, Variable.countVariables());
        assertEquals(1, Variable.getVariableValue("A0"));
        assertEquals(2, Variable.getVariableValue("A1"));
    }

    @Test
    public void testProcessInstruction_arr_indexOutOfBounds() {
        Main.instruction = new String[]{"arr A 2", "set A2 1"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(2, Variable.countVariables());
        assertThrows(IndexOutOfBoundsException.class, () -> Main.processInstruction("set A2 1"));
    }

    @Test
    public void testProcessInstruction_var() {
        Main.instruction = new String[]{"var A"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Variable.countVariables());
        assertEquals("A", Variable.getVariableName(0));
    }

    @Test
    public void testProcessInstruction_var_conflictingVariableName() {
        Main.instruction = new String[]{"var A", "var A"};
        Variable.reset();
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Variable.countVariables());
        assertEquals("A", Variable.getVariableName(0));
        Main.processInstruction(Main.instruction[1]);
        assertEquals(2, Variable.countVariables());
        assertEquals("A1", Variable.getVariableName(1));
    }

    @Test
    public void testProcessInstruction_haltsExecution() {
        Main.instruction = new String[]{"halt"};
        Main.processInstruction(Main.instruction[0]);
    }

    @Test
    public void testProcessInstruction_in() {
        Main.instruction = new String[]{"in A"};
        System.setIn(new ByteArrayInputStream("5".getBytes()));
        Main.processInstruction(Main.instruction[0]);
        assertEquals(5, Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_out() {
        Main.instruction = new String[]{"out A"};
        System.out.println("Expected output");
        Main.processInstruction(Main.instruction[0]);
        assertEquals("Expected output", System.out.getHistory(0));
    }
}
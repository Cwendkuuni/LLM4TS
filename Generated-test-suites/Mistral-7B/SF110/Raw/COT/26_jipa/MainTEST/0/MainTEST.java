package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    

    @Test
    public void testProcessInstruction_mov() {
        Main.iPtr = 0;
        String[] instruction = new String[]{"mov A 0"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(0, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.iPtr = 0;
        String[] instruction = new String[]{"inc A"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.Variable.getVariableValue("A"));
    }

    @Test
    public void testProcessInstruction_add() {
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 2);
        Main.Variable.setVariableValue("B", 3);
        String[] instruction = new String[]{"add A B"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(5, Main.Variable.getVariableValue("A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_invalidOpcode() {
        Main.iPtr = 0;
        String[] instruction = new String[]{"invalidOp A B"};
        Main.processInstruction(instruction[Main.iPtr]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_outOfBoundsArrayAccess() {
        Main.iPtr = 0;
        String[] instruction = new String[]{"mov A B"};
        Main.Variable.setVariableValue("B", Main.Variable.TOTAL_VARIABLES + 1);
        Main.processInstruction(instruction[Main.iPtr]);
    }

    @Test(expected = NumberFormatException.class)
    public void testProcessInstruction_invalidValue() {
        Main.iPtr = 0;
        String[] instruction = new String[]{"mov A a"};
        Main.processInstruction(instruction[Main.iPtr]);
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.iPtr = 0;
        String[] instruction = new String[]{"jmp Label1"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 0);
        String[] instruction = new String[]{"jnz Label1"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);

        Main.Variable.setVariableValue("A", 1);
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 0);
        String[] instruction = new String[]{"jz Label1"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);

        Main.Variable.setVariableValue("A", 1);
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 1);
        Main.Variable.setVariableValue("B", 1);
        String[] instruction = new String[]{"je Label1"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);

        Main.Variable.setVariableValue("A", 1);
        Main.Variable.setVariableValue("B", 0);
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 1);
        Main.Variable.setVariableValue("B", 1);
        String[] instruction = new String[]{"jne Label1"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(0, Main.iPtr);

        Main.Variable.setVariableValue("A", 1);
        Main.Variable.setVariableValue("B", 0);
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        Main.iPtr = 0;
        Main.Variable.setVariableValue("A", 1);
        Main.Variable.setVariableValue("B", 1);
        String[] instruction = new String[]{"cmp A B"};
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(0, Main.Variable.getVariableValue("C"));

        Main.Variable.setVariableValue("A", 1);
        Main.Variable.setVariableValue("B", 0);
        Main.processInstruction(instruction[Main.iPtr]);
        assertEquals(1, Main.Variable.getVariableValue("C"));
    }

    
}
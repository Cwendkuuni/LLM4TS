package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        String[] instruction = {"halt"};
        Main.processInstruction(instruction[0]);
        assertEquals(0, Main.iPtr);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testProcessInstruction_out() {
        String[] instruction = {"out", "foo"};
        System.out.println("###Test OUT Instruction###");
        Main.processInstruction(instruction[0]);
        assertEquals("foo", Main.instruction[Main.iPtr]);
        assertEquals("foo", Main.instruction[Main.iPtr + 1]);
        System.out.println(Main.instruction[Main.iPtr + 1]);
        assertEquals(1, Main.iPtr + 2);
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instruction = {"in", "x"};
        int value = 5;
        when(mock(Variable.class).getVariableValue("x")).thenReturn(value);
        when(mock(Variable.class).setVariableValue("x", Integer.valueOf(42))).thenReturn(null);

        Main.processInstruction(instruction[0]);
        assertEquals(value, Main.instruction[Main.iPtr + 1].charAt(0) - 48);
        assertEquals("in", Main.instruction[Main.iPtr]);
        verify(mock(Variable.class)).setVariableValue("x", argThat(is(equalTo(Integer.valueOf(42)))));
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instruction1 = {"mov", "x", "y"};
        String[] instruction2 = {"mov", "y", "x"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 20);

        Main.processInstruction(instruction1[0]);
        assertEquals(20, Variable.getVariableValue("x"));
        assertEquals(10, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(10, Variable.getVariableValue("x"));
        assertEquals(20, Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] instruction1 = {"cpy", "x", "y"};
        String[] instruction2 = {"cpy", "y", "x"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 20);

        Main.processInstruction(instruction1[0]);
        assertEquals(10, Variable.getVariableValue("y"));
        assertEquals(10, Variable.getVariableValue("x"));

        Main.processInstruction(instruction2[0]);
        assertEquals(20, Variable.getVariableValue("y"));
        assertEquals(20, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_set() {
        String[] instruction = {"set", "x", "15"};
        Variable.setVariableValue("x", null);

        Main.processInstruction(instruction[0]);
        assertEquals(15, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_del() {
        String[] instruction = {"del", "x"};
        Variable.setVariableValue("x", "test");

        Main.processInstruction(instruction[0]);
        assertEquals(null, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] instruction = {"arr", "x", "3"};
        Variable.setVariableValue("x", null);

        Main.processInstruction(instruction[0]);
        assertEquals(3, Main.instruction.length - Main.iPtr);
        assertEquals("x0", Main.instruction[Main.iPtr + 1]);
        assertEquals("x1", Main.instruction[Main.iPtr + 2]);
        assertEquals("x2", Main.instruction[Main.iPtr + 3]);
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instruction = {"inc", "x"};
        Variable.setVariableValue("x", 5);

        Main.processInstruction(instruction[0]);
        assertEquals(6, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instruction = {"dec", "x"};
        Variable.setVariableValue("x", 5);

        Main.processInstruction(instruction[0]);
        assertEquals(4, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instruction1 = {"add", "x", "y"};
        String[] instruction2 = {"add", "x", "5"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 10);

        Main.processInstruction(instruction1[0]);
        assertEquals(15, Variable.getVariableValue("x"));
        assertEquals(10, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(10, Variable.getVariableValue("x"));
        assertEquals(5, Variable.getVariableValue("x") + Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instruction1 = {"sub", "x", "y"};
        String[] instruction2 = {"sub", "x", "5"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 5);

        Main.processInstruction(instruction1[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(10, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(5, Variable.getVariableValue("x") - Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instruction1 = {"mul", "x", "y"};
        String[] instruction2 = {"mul", "x", "5"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 10);

        Main.processInstruction(instruction1[0]);
        assertEquals(50, Variable.getVariableValue("x"));
        assertEquals(10, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(5 * 5, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instruction1 = {"div", "x", "y"};
        String[] instruction2 = {"div", "x", "5"};
        Variable.setVariableValue("x", 20);
        Variable.setVariableValue("y", 4);

        Main.processInstruction(instruction1[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(4, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(4, Variable.getVariableValue("x") / 5);
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instruction1 = {"mod", "x", "y"};
        String[] instruction2 = {"mod", "x", "5"};
        Variable.setVariableValue("x", 15);
        Variable.setVariableValue("y", 4);

        Main.processInstruction(instruction1[0]);
        assertEquals(1, Variable.getVariableValue("x"));
        assertEquals(4, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(5 % 5, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instruction1 = {"xor", "x", "y"};
        String[] instruction2 = {"xor", "x", "5"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 10);

        Main.processInstruction(instruction1[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(10, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(5 ^ 5, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instruction1 = {"or", "x", "y"};
        String[] instruction2 = {"or", "x", "5"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 4);

        Main.processInstruction(instruction1[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(4, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(5 | 5, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instruction1 = {"and", "x", "y"};
        String[] instruction2 = {"and", "x", "5"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 4);

        Main.processInstruction(instruction1[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(4, Variable.getVariableValue("y"));

        Main.processInstruction(instruction2[0]);
        assertEquals(5, Variable.getVariableValue("x"));
        assertEquals(5 & 5, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instruction1 = {"jmp", "3"};
        Main.instruction = new String[]{"mov", "x", "1", "jmp", "3", "mov", "x", "2"};
        Main.iPtr = 0;

        Main.processInstruction(instruction1[0]);
        assertEquals(3, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        String[] instruction1 = {"jnz", "x", "-1"};
        Variable.setVariableValue("x", 5);
        Main.instruction = new String[]{"mov", "x", "5", "jnz", "x", "-1", "mov", "x", "2"};
        Main.iPtr = 0;

        Main.processInstruction(instruction1[0]);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instruction1 = {"jz", "x", "-1"};
        Variable.setVariableValue("x", 0);
        Main.instruction = new String[]{"mov", "x", "5", "jz", "x", "-1", "mov", "x", "2"};
        Main.iPtr = 0;

        Main.processInstruction(instruction1[0]);
        assertEquals(5, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instruction1 = {"je", "x", "y"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 5);
        Main.instruction = new String[]{"mov", "x", "5", "mov", "y", "5", "je", "x", "y", "mov", "x", "2"};
        Main.iPtr = 0;

        Main.processInstruction(instruction1[0]);
        assertEquals(2, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instruction1 = {"jne", "x", "y"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 6);
        Main.instruction = new String[]{"mov", "x", "5", "mov", "y", "6", "jne", "x", "y", "mov", "x", "2"};
        Main.iPtr = 0;

        Main.processInstruction(instruction1[0]);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] instruction1 = {"cmp", "x", "y", "1"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 6);
        Main.instruction = new String[]{"mov", "x", "5", "mov", "y", "6", "cmp", "x", "y", "1", "mov", "x", "2"};
        Main.iPtr = 0;

        Main.processInstruction(instruction1[0]);
        assertEquals(0, Variable.getVariableValue("1"));

        Main.processInstruction(instruction1[0]);
        assertEquals(1, Variable.getVariableValue("1"));
    }

    @Test
    public void testLoadInstructions_validFileName() {
        String fileName = "test.txt";
        Main.loadInstructions(fileName);
        assertEquals("mov", Main.instruction[0]);
        assertEquals("x", Main.instruction[1]);
        assertEquals("1", Main.instruction[2]);
    }

    @Test
    public void testLoadInstructions_invalidFileName() {
        Main.loadInstructions("");
        assertEquals(0, Main.instruction.length);
    }

    @Test
    public void testLoadInstructions_emptyFileName() {
        Main.loadInstructions(" ");
        assertEquals(0, Main.instruction.length);
    }

    @Test
    public void testLoadInstructions_invalidFile() {
        String fileName = "test.txt:invalid";
        Main.loadInstructions(fileName);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
        assertEquals(0, Main.instruction.length);
    }

    @Test
    public void testLoadInstructions_validFileWithComments() {
        String fileName = "test.txt";
        Main.loadInstructions(fileName);
        assertEquals("mov", Main.instruction[0]);
        assertEquals("x", Main.instruction[1]);
        assertEquals("1", Main.instruction[2]);
        assertEquals(";this is a comment", Main.instruction[3]);
    }
}
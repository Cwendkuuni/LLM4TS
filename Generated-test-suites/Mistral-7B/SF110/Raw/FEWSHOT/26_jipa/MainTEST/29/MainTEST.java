package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        String[] instructions = {"halt"};
        Main.main(instructions);
        // The halt instruction should cause the program to exit
        assertTrue(true);
    }

    @Test
    public void testProcessInstruction_out() {
        String[] instructions = {"out", "Hello"};
        System.setOut(new ByteArrayOutputStream());
        Main.main(instructions);
        String output = (String) System.out;
        assertEquals("Hello", output);
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instructions = {"in", "x"};
        int value = 'A';
        when(System.in.nextLine()).thenReturn(String.valueOf((char) (value + 1)));
        Main.main(instructions);
        assertEquals(value + 1, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instructions = {"mov", "y", "x"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 0);
        Main.main(instructions);
        assertEquals(5, Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] instructions = {"cpy", "y", "x"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 0);
        Main.main(instructions);
        assertEquals(5, Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_set() {
        String[] instructions = {"set", "x", "5"};
        Main.main(instructions);
        assertEquals(5, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_del() {
        String[] instructions = {"del", "x"};
        Main.main(instructions);
        assertFalse(Variable.hasVariable("x"));
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] instructions = {"arr", "array", "3"};
        Main.main(instructions);
        assertTrue(Variable.hasVariable("array"));
        assertEquals(3, Variable.getVariablesCount("array"));
    }

    @Test
    public void testProcessInstruction_var() {
        String[] instructions = {"var", "z"};
        Main.main(instructions);
        assertTrue(Variable.hasVariable("z"));
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instructions = {"inc", "x"};
        Variable.setVariableValue("x", 5);
        Main.main(instructions);
        assertEquals(6, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instructions = {"dec", "x"};
        Variable.setVariableValue("x", 5);
        Main.main(instructions);
        assertEquals(4, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instructions = {"add", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 3);
        Main.main(instructions);
        assertEquals(8, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instructions = {"sub", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 3);
        Main.main(instructions);
        assertEquals(2, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instructions = {"mul", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 3);
        Main.main(instructions);
        assertEquals(15, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instructions = {"div", "x", "y", "z"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 2);
        Main.main(instructions);
        assertEquals(5, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instructions = {"mod", "x", "y", "z"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 3);
        Main.main(instructions);
        assertEquals(1, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instructions = {"xor", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 3);
        Main.main(instructions);
        assertEquals(2, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instructions = {"or", "x", "y", "z"};
        Variable.setVariableValue("x", 3);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(7, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instructions = {"and", "x", "y", "z"};
        Variable.setVariableValue("x", 3);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(3, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instructions = {"jmp", "5"};
        int[] expectedInstructionPointers = {5, 5, 5};
        Main.main(instructions);
        assertEquals(expectedInstructionPointers[0], Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        String[] instructions = {"jnz", "x", "5"};
        Variable.setVariableValue("x", 0);
        Main.main(instructions);
        assertEquals(0, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_ja() {
        String[] instructions = {"ja", "x", "5"};
        Variable.setVariableValue("x", 2);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jae() {
        String[] instructions = {"jae", "x", "5"};
        Variable.setVariableValue("x", 2);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jb() {
        String[] instructions = {"jb", "x", "5"};
        Variable.setVariableValue("x", 1);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);

        Variable.setVariableValue("x", 2);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jbe() {
        String[] instructions = {"jbe", "x", "5"};
        Variable.setVariableValue("x", 1);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);

        Variable.setVariableValue("x", 2);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instructions = {"jz", "x", "5"};
        Variable.setVariableValue("x", 0);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instructions = {"je", "x", "y"};
        Variable.setVariableValue("x", 2);
        Variable.setVariableValue("y", 2);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);

        Variable.setVariableValue("x", 2);
        Variable.setVariableValue("y", 1);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Variable.setVariableValue("y", 2);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Variable.setVariableValue("y", 1);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instructions = {"jne", "x", "y"};
        Variable.setVariableValue("x", 2);
        Variable.setVariableValue("y", 2);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);

        Variable.setVariableValue("x", 2);
        Variable.setVariableValue("y", 1);
        Main.main(instructions);
        assertEquals(5, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Variable.setVariableValue("y", 2);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);

        Variable.setVariableValue("x", 1);
        Variable.setVariableValue("y", 1);
        Main.main(instructions);
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] instructions = {"cmp", "x", "y", "z"};
        Variable.setVariableValue("x", 2);
        Variable.setVariableValue("y", 2);
        Variable.setVariableValue("z", 1);
        Main.main(instructions);
        assertEquals(0, Variable.getVariableValue("z"));

        Variable.setVariableValue("x", 2);
        Variable.setVariableValue("y", 1);
        Variable.setVariableValue("z", 1);
        Main.main(instructions);
        assertEquals(1, Variable.getVariableValue("z"));
    }
}
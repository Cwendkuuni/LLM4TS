package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        Main.instruction = new String[]{"mov", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("mov");
        assertEquals(5, Main.Variable.getVariableValue("b"));
        assertEquals(0, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_set() {
        Main.instruction = new String[]{"set", "a", "5"};
        Main.Variable.setVariableValue("a", 0);
        Main.processInstruction("set");
        assertEquals(5, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.instruction = new String[]{"inc", "a"};
        Main.Variable.setVariableValue("a", 5);
        Main.processInstruction("inc");
        assertEquals(6, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_dec() {
        Main.instruction = new String[]{"dec", "a"};
        Main.Variable.setVariableValue("a", 6);
        Main.processInstruction("dec");
        assertEquals(5, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_add() {
        Main.instruction = new String[]{"add", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("add");
        assertEquals(8, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_sub() {
        Main.instruction = new String[]{"sub", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("sub");
        assertEquals(2, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mul() {
        Main.instruction = new String[]{"mul", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("mul");
        assertEquals(15, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_div() {
        Main.instruction = new String[]{"div", "a", "b"};
        Main.Variable.setVariableValue("a", 10);
        Main.Variable.setVariableValue("b", 4);
        Main.processInstruction("div");
        assertEquals(3, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mod() {
        Main.instruction = new String[]{"mod", "a", "b"};
        Main.Variable.setVariableValue("a", 10);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("mod");
        assertEquals(1, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_xor() {
        Main.instruction = new String[]{"xor", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("xor");
        assertEquals(2, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_or() {
        Main.instruction = new String[]{"or", "a", "b"};
        Main.Variable.setVariableValue("a", 3);
        Main.Variable.setVariableValue("b", 5);
        Main.processInstruction("or");
        assertEquals(7, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_and() {
        Main.instruction = new String[]{"and", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("and");
        assertEquals(1, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.instruction = new String[]{"jmp", "10"};
        Main.processInstruction("jmp");
        assertEquals(10, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        Main.instruction = new String[]{"jnz", "10", "0"};
        Main.Variable.setVariableValue("0", 1);
        Main.processInstruction("jnz");
        assertEquals(10, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        Main.instruction = new String[]{"jz", "10", "1"};
        Main.Variable.setVariableValue("1", 1);
        Main.processInstruction("jz");
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        Main.instruction = new String[]{"je", "10", "1"};
        Main.Variable.setVariableValue("1", 1);
        Main.Variable.setVariableValue("0", 1);
        Main.processInstruction("je");
        assertEquals(10, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        Main.instruction = new String[]{"jne", "10", "1"};
        Main.Variable.setVariableValue("1", 1);
        Main.Variable.setVariableValue("0", 2);
        Main.processInstruction("jne");
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        Main.instruction = new String[]{"cmp", "a", "b"};
        Main.Variable.setVariableValue("a", 5);
        Main.Variable.setVariableValue("b", 3);
        Main.processInstruction("cmp");
        assertEquals(2, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testLoadInstructions() {
        Main.loadInstructions("test.txt");
        assertEquals(15, Main.instruction.length);
    }

    @Test
    public void testRemoveComment() {
        String line = "mov a, b;";
        assertEquals("mov a b", Main.removeComment(line));

        line = "mov a b;";
        assertEquals("mov a b", Main.removeComment(line));

        line = "mov a b//comment";
        assertEquals("mov a b", Main.removeComment(line));

        line = "mov a b//this is a multi-line comment";
        assertEquals("mov a b", Main.removeComment(line));

        line = "mov a b// this is a multi-line comment";
        assertEquals("mov a b", Main.removeComment(line));

        line = "mov a b // this is a multi-line comment";
        assertEquals("mov a b", Main.removeComment(line));

        line = "mov a b // this is a multi-line comment";
        assertEquals("mov a b", Main.removeComment(line));
    }
}
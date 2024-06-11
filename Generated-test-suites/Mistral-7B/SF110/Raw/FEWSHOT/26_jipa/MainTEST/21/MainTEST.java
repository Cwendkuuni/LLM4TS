package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        String[] instructions = {"halt"};
        Main.main(instructions);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testProcessInstruction_out() {
        String[] instructions = {"out", "Hello"};
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                assertEquals("Hello", s);
            }
        });
        Main.main(instructions);
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instructions = {"in", "x"};
        Main main = new Main();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("123".getBytes());
        System.setIn(inputStream);
        main.processInstruction("in, x");
        assertEquals(123, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instructions = {"mov", "a", "b"};
        Main main = new Main();
        main.Variable.setVariableValue("a", 42);
        main.processInstruction("mov, b, a");
        assertEquals(42, main.Variable.getVariableValue("b"));
        assertEquals(0, main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instructions = {"inc", "x"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 42);
        main.processInstruction("inc, x");
        assertEquals(43, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instructions = {"dec", "x"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 43);
        main.processInstruction("dec, x");
        assertEquals(42, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instructions = {"add", "x", "y", "3"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 42);
        main.Variable.setVariableValue("y", 3);
        main.processInstruction("add, x, y, 3");
        assertEquals(45, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instructions = {"sub", "x", "y", "3"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 45);
        main.Variable.setVariableValue("y", 3);
        main.processInstruction("sub, x, y, 3");
        assertEquals(42, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instructions = {"mul", "x", "y"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 4);
        main.Variable.setVariableValue("y", 2);
        main.processInstruction("mul, x, y");
        assertEquals(8, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instructions = {"div", "x", "y"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 8);
        main.Variable.setVariableValue("y", 2);
        main.processInstruction("div, x, y");
        assertEquals(4, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instructions = {"mod", "x", "y"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 8);
        main.Variable.setVariableValue("y", 2);
        main.processInstruction("mod, x, y");
        assertEquals(0, main.Variable.getVariableValue("x"));
        assertEquals(2, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instructions = {"xor", "x", "y"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 42);
        main.Variable.setVariableValue("y", 1);
        main.processInstruction("xor, x, y");
        assertEquals(41, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instructions = {"or", "x", "y"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 42);
        main.Variable.setVariableValue("y", 1);
        main.processInstruction("or, x, y");
        assertEquals(43, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instructions = {"and", "x", "y"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 42);
        main.Variable.setVariableValue("y", 1);
        main.processInstruction("and, x, y");
        assertEquals(1, main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instructions = {"jmp", "label1"};
        Main main = new Main();
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jmp, label1");
        assertEquals(10, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instructions = {"jz", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 0);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jz, label1");
        assertEquals(10, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instructions = {"je", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 3);
        main.Variable.setVariableValue("y", 3);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("je, label1");
        assertEquals(10, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instructions = {"jne", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 3);
        main.Variable.setVariableValue("y", 4);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jne, label1");
        assertEquals(1, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jg() {
        String[] instructions = {"jg", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 5);
        main.Variable.setVariableValue("y", 3);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jg, label1");
        assertEquals(10, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jge() {
        String[] instructions = {"jge", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 5);
        main.Variable.setVariableValue("y", 3);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jge, label1");
        assertEquals(10, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jl() {
        String[] instructions = {"jl", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 3);
        main.Variable.setVariableValue("y", 5);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jl, label1");
        assertEquals(1, main.Main.iPtr);
    }

    @Test
    public void testProcessInstruction_jle() {
        String[] instructions = {"jle", "label1"};
        Main main = new Main();
        main.Variable.setVariableValue("x", 3);
        main.Variable.setVariableValue("y", 5);
        main.Label.put("label1", 10);
        main.Main.iPtr = 0;
        main.processInstruction("jle, label1");
        assertEquals(1, main.Main.iPtr);
    }

    @Test
    public void testLoadInstructions() {
        String fileName = "test.txt";
        String[] instructions = {"halt"};
        Main main = new Main();
        main.loadInstructions(fileName);
        assertEquals(instructions[0], main.instruction[0]);
        assertEquals("test.txt", main.fileName);
    }
}
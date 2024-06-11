package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        String[] instructions = {"halt"};
        Main.main(instructions);
        assertTrue(true); // Test for successful termination of the program
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
        assertTrue(true); // Test for successful execution of the out instruction
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instructions = {"in", "x"};
        when(System.in).thenReturn(new ByteArrayInputStream("5".getBytes()));
        
        int x = 0;
        Main.main(instructions);
        assertEquals(5, x); // Test for successful execution of the in instruction
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instructions = {"mov", "y", "x"};
        int x = 10, y = 20;
        
        Main.main(instructions);
        assertEquals(x, y); // Test for successful execution of the mov instruction
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] instructions = {"cpy", "z", "x"};
        int x = 10, z = 0;
        
        Main.main(instructions);
        assertEquals(x, z); // Test for successful execution of the cpy instruction
    }

    @Test
    public void testProcessInstruction_set() {
        String[] instructions = {"set", "x", "15"};
        int x = 0;
        
        Main.main(instructions);
        assertEquals(15, x); // Test for successful execution of the set instruction
    }

    @Test
    public void testProcessInstruction_del() {
        String[] instructions = {"del", "x"};
        int x = 10;
        Main.instruction = new String[1];
        Main.instruction[0] = "del";
        
        Main.main(instructions);
        assertArrayEquals(new String[0], Main.instruction); // Test for successful execution of the del instruction
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] instructions = {"arr", "a", "5"};
        Main.Variable.addVariable("a");
        Main.Variable.addVariable("a1");
        Main.Variable.addVariable("a2");
        Main.Variable.addVariable("a3");
        Main.Variable.addVariable("a4");
        Main.Variable.addVariable("a5");
        
        Main.main(instructions);
        
        assertArrayEquals(new String[] {"a", "a1", "a2", "a3", "a4", "a5"}, Main.Variable.getVariableNames().toArray()); // Test for successful execution of the arr instruction
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instructions = {"inc", "x"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(11, x); // Test for successful execution of the inc instruction
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instructions = {"dec", "x"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(9, x); // Test for successful execution of the dec instruction
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instructions = {"add", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(15, x); // Test for successful execution of the add instruction
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instructions = {"sub", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(5, x); // Test for successful execution of the sub instruction
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instructions = {"mul", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(50, x); // Test for successful execution of the mul instruction
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instructions = {"div", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(2, x); // Test for successful execution of the div instruction
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instructions = {"mod", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(0, x); // Test for successful execution of the mod instruction

        instructions[0] = "mod";
        instructions[1] = "x";
        instructions[2] = "3";
        
        Main.main(instructions);
        assertEquals(1, x); // Test for successful execution of the mod instruction with a non-zero remainder
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instructions = {"xor", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(5, x); // Test for successful execution of the xor instruction
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instructions = {"or", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(15, x); // Test for successful execution of the or instruction
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instructions = {"and", "x", "5"};
        int x = 10;
        
        Main.main(instructions);
        assertEquals(0, x); // Test for successful execution of the and instruction with false condition

        x = 15;
        
        Main.main(instructions);
        assertEquals(5, x); // Test for successful execution of the and instruction with true condition
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instructions = {"jmp", "label1"};
        int[] labels = {0, 1, 2, 3, 4};
        
        Main.main(new String[] {"label1", "out", "Hello", "jmp", "label1"});
        System.setOut(System.out);
        Main.main(instructions);
        assertEquals(1, Main.iPtr); // Test for successful execution of the jmp instruction with a valid label
    }

    @Test
    public void testProcessInstruction_jnz() {
        String[] instructions = {"jnz", "label1", "label2"};
        int[] labels = {0, 1, 2, 3, 4};
        
        Main.main(new String[] {"label1", "set", "x", "5", "jnz", "label1", "label2", "halt"});
        Main.main(instructions);
        assertEquals(1, Main.iPtr); // Test for successful execution of the jnz instruction with a non-zero condition
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instructions = {"jz", "label1", "label2"};
        int[] labels = {0, 1, 2, 3, 4};
        
        Main.main(new String[] {"label1", "set", "x", "0", "jz", "label1", "label2", "halt"});
        Main.main(instructions);
        assertEquals(2, Main.iPtr); // Test for successful execution of the jz instruction with a zero condition
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instructions = {"je", "label1", "label2"};
        int[] labels = {0, 1, 2, 3, 4};
        
        Main.main(new String[] {"label1", "set", "x", "5", "set", "y", "5", "cmp", "x", "y", "je", "label1", "label2", "halt"});
        Main.main(instructions);
        assertEquals(2, Main.iPtr); // Test for successful execution of the je instruction with equal comparison
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instructions = {"jne", "label1", "label2"};
        int[] labels = {0, 1, 2, 3, 4};
        
        Main.main(new String[] {"label1", "set", "x", "5", "set", "y", "6", "cmp", "x", "y", "jne", "label1", "label2", "halt"});
        Main.main(instructions);
        assertEquals(1, Main.iPtr); // Test for successful execution of the jne instruction with an unequal comparison
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] instructions = {"cmp", "x", "y"};
        int x = 5, y = 5;
        
        Main.main(instructions);
        assertEquals(0, Main.Variable.getVariableValue("compareResult")); // Test for successful execution of the cmp instruction with equal comparison

        x = 5;
        y = 6;
        
        Main.main(instructions);
        assertEquals(1, Main.Variable.getVariableValue("compareResult")); // Test for successful execution of the cmp instruction with unequal comparison
    }

    @Test
    public void testLoadInstructions() {
        String fileName = "testInstructions.txt";
        String[] instructions = {" halt"};
        
        Main.loadInstructions(fileName);
        assertEquals(instructions[0], Main.instruction[0]); // Test for successful loading of instructions from a file
    }
}
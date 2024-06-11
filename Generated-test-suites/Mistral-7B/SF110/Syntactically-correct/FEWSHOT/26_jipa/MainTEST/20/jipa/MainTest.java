package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        String[] variables = new String[]{"x", "y"};
        Main.Variable.initVariables(variables);
        String instruction = "mov x y";
        Main.processInstruction(instruction);
        assertEquals(0, Main.Variable.getVariableValue("x"));
        assertEquals(Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] variables = new String[]{"x", "y"};
        Main.Variable.initVariables(variables);
        String instruction = "cpy x y";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("x"), Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] variables = new String[]{"x"};
        Main.Variable.initVariables(variables);
        String instruction = "inc x";
        Main.processInstruction(instruction);
        assertEquals(1, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] variables = new String[]{"x"};
        Main.Variable.initVariables(variables);
        String instruction = "dec x";
        Main.processInstruction(instruction);
        assertEquals(0, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_set() {
        String[] variables = new String[]{"x", "y"};
        Main.Variable.initVariables(variables);
        String instruction = "set x y";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_add() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "add x y z";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("x") + Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "sub x y z";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("x") - Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "mul x y z";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("x") * Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_div() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "div x y z";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("x") / Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "mod x y z";
        Main.processInstruction(instruction);
        assertEquals(Main.Variable.getVariableValue("x") % Main.Variable.getVariableValue("y"), Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "xor x y z";
        Main.processInstruction(instruction);
        int x = Main.Variable.getVariableValue("x");
        int y = Main.Variable.getVariableValue("y");
        int z = Main.Variable.getVariableValue("z");
        assertEquals(x ^ y, Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_or() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "or x y z";
        Main.processInstruction(instruction);
        int x = Main.Variable.getVariableValue("x");
        int y = Main.Variable.getVariableValue("y");
        int z = Main.Variable.getVariableValue("z");
        assertEquals(x | y, Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_and() {
        String[] variables = new String[]{"x", "y", "z"};
        Main.Variable.initVariables(variables);
        String instruction = "and x y z";
        Main.processInstruction(instruction);
        int x = Main.Variable.getVariableValue("x");
        int y = Main.Variable.getVariableValue("y");
        int z = Main.Variable.getVariableValue("z");
        assertEquals(x & y, Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instructions = {"mov x 5", "jmp 2", "mov y 10"};
        Main.loadInstructions(instructions);
        Main.processInstruction("mov x 5");
        Main.processInstruction("jmp 2");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(10, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_jnz() {
        String[] instructions = {"mov x 5", "mov y 0", "jnz 1 2"};
        Main.loadInstructions(instructions);
        Main.processInstruction("mov x 5");
        Main.processInstruction("mov y 0");
        Main.processInstruction("jnz 1 2");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(0, Main.Variable.getVariableValue("y"));

        String[] instructions2 = {"mov x 0", "mov y 5", "jnz 1 2"};
        Main.loadInstructions(instructions2);
        Main.processInstruction("mov x 0");
        Main.processInstruction("mov y 5");
        Main.processInstruction("jnz 1 2");
        assertEquals(5, Main.Variable.getVariableValue("y"));
        assertEquals(0, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_ja() {
        String[] instructions = {"mov x 5", "mov y 2", "mov z 1", "ja x y z"};
        Main.loadInstructions(instructions);
        Main.processInstruction("mov x 5");
        Main.processInstruction("mov y 2");
        Main.processInstruction("mov z 1");
        Main.processInstruction("ja x y z");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(1, Main.Variable.getVariableValue("z"));

        String[] instructions2 = {"mov x 2", "mov y 5", "mov z 1", "ja x y z"};
        Main.loadInstructions(instructions2);
        Main.processInstruction("mov x 2");
        Main.processInstruction("mov y 5");
        Main.processInstruction("mov z 1");
        Main.processInstruction("ja x y z");
        assertEquals(2, Main.Variable.getVariableValue("x"));
        assertEquals(1, Main.Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instructions = {"mov x 5", "mov y 0", "jz x y"};
        Main.loadInstructions(instructions);
        Main.processInstruction("mov x 5");
        Main.processInstruction("mov y 0");
        Main.processInstruction("jz x y");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(0, Main.Variable.getVariableValue("y"));

        String[] instructions2 = {"mov x 0", "mov y 5", "jz x y"};
        Main.loadInstructions(instructions2);
        Main.processInstruction("mov x 0");
        Main.processInstruction("mov y 5");
        Main.processInstruction("jz x y");
        assertEquals(0, Main.Variable.getVariableValue("x"));
        assertEquals(5, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instructions = {"mov x 5", "mov y 5", "je x y"};
        Main.loadInstructions(instructions);
        Main.processInstruction("mov x 5");
        Main.processInstruction("mov y 5");
        Main.processInstruction("je x y");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(5, Main.Variable.getVariableValue("y"));

        String[] instructions2 = {"mov x 5", "mov y 6", "je x y"};
        Main.loadInstructions(instructions2);
        Main.processInstruction("mov x 5");
        Main.processInstruction("mov y 6");
        Main.processInstruction("je x y");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(6, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instructions = {"mov x 5", "mov y 6", "jne x y"};
        Main.loadInstructions(instructions);
        Main.processInstruction("mov x 5");
        Main.processInstruction("mov y 6");
        Main.processInstruction("jne x y");
        assertEquals(5, Main.Variable.getVariableValue("x"));
        assertEquals(6, Main.Variable.getVariableValue("y"));

        String[] instructions2 = {"mov x 6", "mov y 5", "jne x y"};
        Main.loadInstructions(instructions2);
        Main.processInstruction("mov x 6");
        Main.processInstruction("mov y 5");
        Main.processInstruction("jne x y");
        assertEquals(6, Main.Variable.getVariableValue("x"));
        assertEquals(5, Main.Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] variables = new String[]{"x", "y"};
        Main.Variable.initVariables(variables);
        String instruction = "cmp x y";
        Main.processInstruction(instruction);
        int x = Main.Variable.getVariableValue("x");
        int y = Main.Variable.getVariableValue("y");
        int result = Main.Variable.getVariableValue("z");
        Main.Variable.setVariableValue("z", 0);
        String instruction2 = "jnz 1 2";
        Main.processInstruction(instruction2);
        assertEquals(x < y, result == 1);
    }

    @Test
    public void testLoadInstructions_whenFileExists() {
        String[] instructions = {"mov x 5", "mov y 10", "halt"};
        String fileName = "test.txt";
        try (Writer writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String instruction : instructions) {
                writer.write(instruction);
                writer.newLine();
            }
        } catch (IOException e) {
            fail("Error writing file.");
        }

        Main.loadInstructions(fileName);
        assertEquals(instructions.length, Main.TOTAL_INSTRUCTIONS);

        for (int i = 0; i < instructions.length; i++) {
            assertEquals(instructions[i], Main.instruction[i]);
        }

        String[] variables = new String[]{"x", "y"};
        Main.Variable.initVariables(variables);
        for (int i = 0; i < instructions.length; i++) {
            Main.processInstruction(Main.instruction[i]);
        }
        assertEquals(10, Main.Variable.getVariableValue("y"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructions_whenFileDoesNotExist() {
        Main.loadInstructions("non_existent_file.txt");
    }
}
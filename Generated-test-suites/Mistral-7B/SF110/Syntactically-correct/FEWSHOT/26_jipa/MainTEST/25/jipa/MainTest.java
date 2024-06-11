package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        String[] instructions = new String[]{"halt"};
        Main.main(instructions);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testProcessInstruction_out() {
        String[] instructions = new String[]{"out", "hello"};
        System.setIn(new ByteArrayInputStream(("".getBytes())));
        Main.main(instructions);
        assertEquals("hello", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instructions = new String[]{"in", "x"};
        Main.main(instructions);
        assertEquals("", System.console().readLine());
        assertEquals(48, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instructions = new String[]{"mov", "y", "x"};
        Main.main(instructions);
        assertEquals(48, Variable.getVariableValue("x"));
        assertEquals(48, Variable.getVariableValue("y"));
        assertEquals(0, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] instructions = new String[]{"cpy", "y", "x"};
        Main.main(instructions);
        assertEquals(48, Variable.getVariableValue("x"));
        assertEquals(48, Variable.getVariableValue("y"));
    }

    @Test
    public void testProcessInstruction_set() {
        String[] instructions = new String[]{"set", "x", "100"};
        Main.main(instructions);
        assertEquals(100, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_del() {
        String[] instructions = new String[]{"del", "x"};
        Main.main(instructions);
        assertNull(Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] instructions = new String[]{"arr", "a", "3"};
        Main.main(instructions);
        assertArrayEquals(new String[]{"a", "a0", "a1", "a2"}, Variable.getVariableNames());
    }

    @Test
    public void testProcessInstruction_var() {
        String[] instructions = new String[]{"var", "b"};
        Main.main(instructions);
        assertEquals(1, Variable.getVariableCount());
        assertArrayEquals(new String[]{"a", "b"}, Variable.getVariableNames());
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instructions = new String[]{"inc", "x"};
        Main.main(instructions);
        assertEquals(49, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instructions = new String[]{"dec", "x"};
        Main.main(instructions);
        assertEquals(47, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instructions = new String[]{"add", "x", "y", "z"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(15, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instructions = new String[]{"sub", "x", "y", "z"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(5, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instructions = new String[]{"mul", "x", "y", "z"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(50, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instructions = new String[]{"div", "x", "y", "z"};
        Variable.setVariableValue("x", 20);
        Variable.setVariableValue("y", 4);
        Main.main(instructions);
        assertEquals(5, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instructions = new String[]{"mod", "x", "y", "z"};
        Variable.setVariableValue("x", 20);
        Variable.setVariableValue("y", 4);
        Main.main(instructions);
        assertEquals(0, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instructions = new String[]{"xor", "x", "y", "z"};
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(15, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instructions = new String[]{"or", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 1);
        Main.main(instructions);
        assertEquals(6, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instructions = new String[]{"and", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 1);
        Main.main(instructions);
        assertEquals(1, Variable.getVariableValue("z"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instructions = new String[]{"jmp", "10"};
        String[] initialInstructions = {"mov", "x", "5", "inc", "x", "jmp", "5", "out", "x"};
        String[] instructionsWithJump = new String[initialInstructions.length + 1];
        System.arraycopy(initialInstructions, 0, instructionsWithJump, 0, initialInstructions.length);
        instructionsWithJump[initialInstructions.length] = "10";
        Main.main(instructionsWithJump);
        assertEquals("6", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_jnz() {
        String[] instructions = new String[]{"jnz", "0", "10"};
        String[] initialInstructions = {"mov", "x", "5", "inc", "x", "jnz", "0", "out", "x"};
        String[] instructionsWithJump = new String[initialInstructions.length + 1];
        System.arraycopy(initialInstructions, 0, instructionsWithJump, 0, initialInstructions.length);
        instructionsWithJump[initialInstructions.length] = "10";
        Main.main(instructionsWithJump);
        assertEquals("5", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instructions = new String[]{"jz", "0", "10"};
        String[] initialInstructions = {"mov", "x", "0", "jz", "10", "out", "x"};
        String[] instructionsWithJump = new String[initialInstructions.length + 1];
        System.arraycopy(initialInstructions, 0, instructionsWithJump, 0, initialInstructions.length);
        instructionsWithJump[initialInstructions.length] = "10";
        Main.main(instructionsWithJump);
        assertEquals("", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instructions = new String[]{"je", "x", "10"};
        String[] initialInstructions = {"mov", "x", "5", "mov", "y", "10", "je", "x", "y", "out", "x"};
        String[] instructionsWithJump = new String[initialInstructions.length + 1];
        System.arraycopy(initialInstructions, 0, instructionsWithJump, 0, initialInstructions.length);
        instructionsWithJump[initialInstructions.length] = "10";
        Main.main(instructionsWithJump);
        assertEquals("5", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instructions = new String[]{"jne", "x", "10"};
        String[] initialInstructions = {"mov", "x", "5", "mov", "y", "10", "jne", "x", "y", "out", "x"};
        String[] instructionsWithJump = new String[initialInstructions.length + 1];
        System.arraycopy(initialInstructions, 0, instructionsWithJump, 0, initialInstructions.length);
        instructionsWithJump[initialInstructions.length] = "10";
        Main.main(instructionsWithJump);
        assertEquals("", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] instructions = new String[]{"cmp", "x", "y", "z"};
        Variable.setVariableValue("x", 5);
        Variable.setVariableValue("y", 10);
        Main.main(instructions);
        assertEquals(0, Variable.getVariableValue("z"));
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 5);
        Main.main(instructions);
        assertEquals(1, Variable.getVariableValue("z"));
    }

    @Test
    public void testLoadInstructions_emptyInput() {
        String fileName = "";
        boolean result = Main.loadInstructions(fileName);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testLoadInstructions_invalidFile() {
        String fileName = "invalidFile.txt";
        boolean result = Main.loadInstructions(fileName);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
        assertTrue(fileName.equalsIgnoreCase("invalidFile.txt") || fileName.isEmpty());
    }

    @Test
    public void testLoadInstructions_nonExistentFile() {
        String fileName = "nonExistentFile.txt";
        boolean result = Main.loadInstructions(fileName);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
        assertTrue(fileName.equalsIgnoreCase("nonExistentFile.txt") || fileName.isEmpty());
    }

    @Test
    public void testLoadInstructions_validFile() {
        String fileName = "testInstructions.txt";
        String expectedInstructions[] = {"mov", "x", "5", "inc", "x", "jmp", "5", "out", "x"};
        boolean result = Main.loadInstructions(fileName);
        assertArrayEquals(expectedInstructions, Main.instruction);
        assertEquals(10, Main.TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testGetFileName() {
        String expected = "Enter the full path to the file or type q to quit :";
        System.out.println(expected);
        String result = Main.getFileName();
        assertEquals(expected, System.console().readLine());
    }
}
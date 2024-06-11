package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    private static final String TEST_FILE_NAME = "testInstructions.txt";
    private static final String[] TEST_ARGUMENTS = new String[]{"5"};
    private static final int[] TEST_EXPECTED_VALUES = {5, 10, 5, 15, 10};

    @Before
    public void setUp() {
        resetInstructions();
        loadInstructions(TEST_FILE_NAME);
    }

    @After
    public void tearDown() {
        resetInstructions();
    }

    @Test
    public void testMain() {
        main(TEST_ARGUMENTS);
        // This test does not make much sense as the main method does not return a value and does not take any output.
        // However, you can test if the instructions are processed correctly by checking the state of the variables.
        // Add other tests to check the variables values after running specific instructions.
    }

    @Test
    public void testProcessInstruction_mov() {
        processInstruction("mov a 5");
        assertEquals(5, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_inc() {
        processInstruction("inc a");
        assertEquals(6, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_dec() {
        processInstruction("dec a");
        assertEquals(5, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_add() {
        processInstruction("mov a 5");
        processInstruction("add a b");
        processInstruction("mov b 3");
        assertEquals(8, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_sub() {
        processInstruction("mov a 5");
        processInstruction("mov b 3");
        processInstruction("sub a b");
        assertEquals(2, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mul() {
        processInstruction("mov a 5");
        processInstruction("mov b 3");
        processInstruction("mul a b");
        assertEquals(15, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_div() {
        processInstruction("mov a 15");
        processInstruction("mov b 3");
        processInstruction("div a b");
        assertEquals(5, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mod() {
        processInstruction("mov a 15");
        processInstruction("mov b 3");
        processInstruction("mod a b");
        assertEquals(0, Variable.getVariableValue("a"));
        assertEquals(3, Variable.getVariableValue("Variable1"));
    }

    @Test
    public void testProcessInstruction_xor() {
        processInstruction("mov a 5");
        processInstruction("mov b 3");
        processInstruction("xor a b");
        assertEquals(2, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_or() {
        processInstruction("mov a 5");
        processInstruction("mov b 3");
        processInstruction("or a b");
        assertEquals(6, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_and() {
        processInstruction("mov a 5");
        processInstruction("mov b 3");
        processInstruction("and a b");
        assertEquals(1, Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        processInstruction("mov a 5");
        processInstruction("mov b 2");
        processInstruction("jmp label1");
        processInstruction("label1: mov c 10");
        assertEquals(10, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jnz() {
        processInstruction("mov a 5");
        processInstruction("mov b 0");
        processInstruction("jnz label1");
        processInstruction("mov c 10");
        processInstruction("label1: mov c 5");
        assertEquals(5, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jz() {
        processInstruction("mov a 5");
        processInstruction("mov b 0");
        processInstruction("jz label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_je() {
        processInstruction("mov a 5");
        processInstruction("mov b 5");
        processInstruction("je label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jne() {
        processInstruction("mov a 5");
        processInstruction("mov b 6");
        processInstruction("jne label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jmp_negative() {
        processInstruction("mov a -5");
        processInstruction("mov b 2");
        processInstruction("jmp label1");
        processInstruction("label1: mov c 10");
        processInstruction("mov a 3");
        assertEquals(10, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jnz_negative() {
        processInstruction("mov a -5");
        processInstruction("mov b 0");
        processInstruction("jnz label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        processInstruction("mov a 0");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jz_negative() {
        processInstruction("mov a -5");
        processInstruction("mov b 0");
        processInstruction("jz label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        processInstruction("mov a 6");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_je_negative() {
        processInstruction("mov a -5");
        processInstruction("mov b -5");
        processInstruction("je label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        processInstruction("mov a 6");
        processInstruction("mov b 6");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_jne_negative() {
        processInstruction("mov a -5");
        processInstruction("mov b -6");
        processInstruction("jne label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        processInstruction("mov a 6");
        processInstruction("mov b 6");
        assertEquals(0, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_cmp() {
        processInstruction("mov a 5");
        processInstruction("mov b 3");
        processInstruction("cmp a b");
        assertEquals(2, Variable.getVariableValue("a"));
        assertEquals(0, Variable.getVariableValue("Variable1"));
        processInstruction("jz label1");
        processInstruction("mov c 10");
        processInstruction("label1:");
        processInstruction("mov c 5");
        assertEquals(5, Variable.getVariableValue("c"));
    }

    @Test
    public void testProcessInstruction_cmp_equal() {
        processInstruction("mov a 5");
        processInstruction("mov b 5");
        processInstruction("cmp a b");
        processInstruction("mov c 10");
        processInstruction("jz label1");
        processInstruction("mov c 5");
        processInstruction("label1:");
        assertEquals(5, Variable.getVariableValue("c"));
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstruction_invalid_opcode() {
        processInstruction("invalid_opcode a 5");
    }
}
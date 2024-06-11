package jipa;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    static String[] instructions;
    static int iPtr;

    @BeforeClass
    public static void setUpClass() {
        String fileName = "testInstructions.txt";
        loadInstructions(fileName);
        iPtr = 0;
    }

    @Test
    public void testMain() {
        main(new String[0]);
        // Add assertions here to test the output of the program
    }

    @Test
    public void testProcessInstruction_halt() {
        String opcode = "halt";
        processInstruction(opcode);
        assertEquals(0, iPtr);
        assertEquals(0, TOTAL_INSTRUCTIONS);
    }

    @Test
    public void testProcessInstruction_out() {
        String instruction = "out 123";
        processInstruction(instruction);
        assertEquals("123", instructions[iPtr]);
        assertEquals(1, TOTAL_INSTRUCTIONS);
        System.out.println("Test out statement");
    }

    @Test
    public void testProcessInstruction_in() {
        String instruction = "in var1";
        processInstruction(instruction);
        assertEquals(0, Variable.getVariableValue("var1"));
        iPtr++;
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_mov_invalidDestination() {
        String instruction = "mov invalidVar var1";
        processInstruction(instruction);
    }

    @Test
    public void testProcessInstruction_mov() {
        String instruction = "mov var1 var2";
        processInstruction(instruction);
        assertEquals(Variable.getVariableValue("var1"), Variable.getVariableValue("var2"));
        iPtr++;
    }

    @Test
    public void testProcessInstruction_cpy() {
        String instruction = "cpy var1 var3";
        processInstruction(instruction);
        assertEquals(Variable.getVariableValue("var1"), Variable.getVariableValue("var3"));
        iPtr++;
    }

    @Test
    public void testProcessInstruction_set() {
        String instruction = "set var1 123";
        processInstruction(instruction);
        assertEquals(123, Variable.getVariableValue("var1"));
        iPtr++;
    }

    @Test
    public void testProcessInstruction_del() {
        String instruction = "del var1";
        processInstruction(instruction);
        assertNull(Variable.getVariableValue("var1"));
        iPtr++;
    }

    @Test
    public void testProcessInstruction_arr() {
        String instruction = "arr var1 3";
        processInstruction(instruction);
        assertNotNull(Variable.getVariableValue("var1"));
        assertEquals(4, Variable.getVariableValue("var1").length);
        iPtr++;
    }

    @Test
    public void testProcessInstruction_inc() {
        String instruction = "inc var1";
        processInstruction(instruction);
        assertEquals(Variable.getVariableValue("var1") + 1, Variable.getVariableValue("var1"));
        iPtr++;
    }

    @Test
    public void testProcessInstruction_dec() {
        String instruction = "dec var1";
        processInstruction(instruction);
        assertEquals(Variable.getVariableValue("var1") - 1, Variable.getVariableValue("var1"));
        iPtr++;
    }

    @Test
    public void testProcessInstruction_add() {
        String instruction = "set var1 5";
        processInstruction(instruction);
        processInstruction("add var1 var2");
        processInstruction("set var2 3");
        processInstruction("add var1 var2");
        assertEquals(8, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_sub() {
        String instruction = "set var1 7";
        processInstruction(instruction);
        processInstruction("set var2 3");
        processInstruction("sub var1 var2");
        assertEquals(4, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_mul() {
        String instruction = "set var1 3";
        processInstruction(instruction);
        processInstruction("set var2 4");
        processInstruction("mul var1 var2");
        assertEquals(12, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_div() {
        String instruction = "set var1 12";
        processInstruction(instruction);
        processInstruction("set var2 4");
        processInstruction("div var1 var2");
        assertEquals(3, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_mod() {
        String instruction = "set var1 12";
        processInstruction(instruction);
        processInstruction("set var2 3");
        processInstruction("mod var1 var2");
        assertEquals(3, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_xor() {
        String instruction = "set var1 10";
        processInstruction(instruction);
        processInstruction("set var2 5");
        processInstruction("xor var1 var2");
        assertEquals(5, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_or() {
        String instruction = "set var1 5";
        processInstruction(instruction);
        processInstruction("set var2 3");
        processInstruction("or var1 var2");
        assertEquals(6, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_and() {
        String instruction = "set var1 5";
        processInstruction(instruction);
        processInstruction("set var2 3");
        processInstruction("and var1 var2");
        assertEquals(1, Variable.getVariableValue("var1"));
        iPtr += 3;
    }

    @Test
    public void testProcessInstruction_jmp() {
        String instruction = "jmp label1";
        processInstruction(instruction);
        processInstruction("label1: jmp label2");
        processInstruction("label2: halt");
        assertEquals(0, iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        String instruction = "set var1 5";
        processInstruction(instruction);
        processInstruction("jnz label1 label2");
        processInstruction("label1: set var1 0");
        processInstruction("label2: halt");
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        String instruction = "set var1 0";
        processInstruction(instruction);
        processInstruction("jz label1 label2");
        processInstruction("label1: halt");
        processInstruction("label2: jmp label1");
        assertEquals(0, iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        String instruction = "set var1 3";
        processInstruction(instruction);
        processInstruction("set var2 3");
        processInstruction("je label1 label2");
        processInstruction("label1: halt");
        processInstruction("label2: halt");
        assertEquals(0, iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        String instruction = "set var1 3";
        processInstruction(instruction);
        processInstruction("set var2 4");
        processInstruction("jne label1 label2");
        processInstruction("label1: halt");
        processInstruction("label2: halt");
        assertEquals(1, iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        String instruction = "set var1 3";
        processInstruction(instruction);
        processInstruction("set var2 3");
        processInstruction("cmp var1 var2");
        processInstruction("jz label1");
        processInstruction("label1: halt");
        processInstruction("halt");
        assertEquals(0, iPtr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_invalidOpcode() {
        String instruction = "invalidOp var1";
        processInstruction(instruction);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_indexOutOfBounds() {
        String instruction = "mov var1 var2";
        processInstruction(instruction);
        processInstruction("mov var1 invalidIndex");
    }

    @Test(expected = NumberFormatException.class)
    public void testProcessInstruction_invalidArgument() {
        String instruction = "mov var1 arg";
        processInstruction(instruction);
    }
}
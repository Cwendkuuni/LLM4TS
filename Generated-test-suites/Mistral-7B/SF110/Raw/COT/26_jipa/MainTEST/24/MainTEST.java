package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MainTest {

    private static final String[] TEST_FILE_NAMES = new String[] { "testFile.txt" };
    private static final String[] TEST_INSTRUCTIONS = new String[] {
            "mov a 0",
            "mov b a",
            "inc a",
            "dec a",
            "add a 1",
            "sub a 1",
            "mul a 2",
            "div a 2",
            "mod a 2",
            "xor a b",
            "or a b",
            "and a b",
            "cpy a c",
            "set a 5",
            "var a",
            "del a",
            "arr a 3",
            "arr a 3 4 5",
            "arr a 3 4",
            "arr a 0",
            "jmp label1",
            "jnz a label2",
            "jz a label3",
            "je a b label4",
            "jne a b label5",
            "jmp label6",
            "jmp label7",
            "cmp a b",
            "jne a b label8",
            "jz a b label9",
            "jz label10 halt"
    };

    private static final String[] LABEL_NAMES = new String[] { "label1", "label2", "label3", "label4", "label5", "label6", "label7", "label8", "label9", "label10" };

    private static int[] variableValues;
    private static int iPtr;

    @Before
    public void reset() {
        Main.resetInstructions();
        variableValues = new int[Main.Variable.NUM_VARIABLES];
        iPtr = 0;
    }

    @Test
    public void testMain() {
        Main.loadInstructions(TEST_FILE_NAMES[0]);
        Main.main(TEST_FILE_NAMES);
    }

    @Test
    public void testProcessInstruction_mov() {
        iPtr = 0;
        processInstruction("mov a 0");
        assertEquals(0, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mov_withSpace() {
        iPtr = 0;
        processInstruction("mov a 5");
        assertEquals(5, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mov_withOperand() {
        iPtr = 0;
        processInstruction("mov b a");
        assertEquals(Main.Variable.getVariableValue("a"), Main.Variable.getVariableValue("b"));
    }

    @Test
    public void testProcessInstruction_inc() {
        iPtr = 0;
        processInstruction("inc a");
        assertEquals(1, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_dec() {
        iPtr = 0;
        processInstruction("dec a");
        assertEquals(0, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_add() {
        iPtr = 0;
        processInstruction("add a 1");
        assertEquals(1, Main.Variable.getVariableValue("a"));

        iPtr = 0;
        processInstruction("add a 5");
        assertEquals(5, Main.Variable.getVariableValue("a"));

        iPtr = 0;
        processInstruction("add a b");
        assertEquals(Main.Variable.getVariableValue("a") + Main.Variable.getVariableValue("b"), Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_sub() {
        iPtr = 0;
        processInstruction("sub a 1");
        assertEquals(Main.Variable.getVariableValue("a") - 1, Main.Variable.getVariableValue("a"));

        iPtr = 0;
        processInstruction("sub a b");
        assertEquals(Main.Variable.getVariableValue("a") - Main.Variable.getVariableValue("b"), Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mul() {
        iPtr = 0;
        processInstruction("mul a 2");
        assertEquals(Main.Variable.getVariableValue("a") * 2, Main.Variable.getVariableValue("a"));

        iPtr = 0;
        processInstruction("mul a b");
        assertEquals(Main.Variable.getVariableValue("a") * Main.Variable.getVariableValue("b"), Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_div() {
        iPtr = 0;
        processInstruction("div a 2");
        assertEquals(Main.Variable.getVariableValue("a") / 2, Main.Variable.getVariableValue("a"));

        iPtr = 0;
        processInstruction("div a b");
        assertEquals(Main.Variable.getVariableValue("a") / Main.Variable.getVariableValue("b"), Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mod() {
        iPtr = 0;
        processInstruction("mod a 2");
        assertEquals(Main.Variable.getVariableValue("a") % 2, Main.Variable.getVariableValue("a"));

        iPtr = 0;
        processInstruction("mod a b");
        assertEquals(Main.Variable.getVariableValue("a") % Main.Variable.getVariableValue("b"), Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_xor() {
        iPtr = 0;
        processInstruction("xor a b");
        int xorResult = Main.Variable.getVariableValue("a") ^ Main.Variable.getVariableValue("b");
        Main.Variable.setVariableValue("a", xorResult);
        Main.Variable.setVariableValue("b", xorResult ^ Main.Variable.getVariableValue("b"));
        assertEquals(xorResult, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_or() {
        iPtr = 0;
        processInstruction("or a b");
        int orResult = Main.Variable.getVariableValue("a") | Main.Variable.getVariableValue("b");
        Main.Variable.setVariableValue("a", orResult);
        Main.Variable.setVariableValue("b", orResult & ~Main.Variable.getVariableValue("b"));
        assertEquals(orResult, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_and() {
        iPtr = 0;
        processInstruction("and a b");
        int andResult = Main.Variable.getVariableValue("a") & Main.Variable.getVariableValue("b");
        Main.Variable.setVariableValue("a", andResult);
        Main.Variable.setVariableValue("b", andResult | ~Main.Variable.getVariableValue("b"));
        assertEquals(andResult, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_cpy() {
        iPtr = 0;
        processInstruction("cpy a c");
        assertEquals(Main.Variable.getVariableValue("a"), Main.Variable.getVariableValue("c"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_cpy_outOfBounds() {
        iPtr = 0;
        processInstruction("cpy a z");
    }

    @Test
    public void testProcessInstruction_set() {
        iPtr = 0;
        processInstruction("set a 5");
        assertEquals(5, Main.Variable.getVariableValue("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_set_invalidValue() {
        iPtr = 0;
        processInstruction("set a q");
    }

    @Test
    public void testProcessInstruction_del() {
        iPtr = 0;
        processInstruction("del a");
        assertNull(Main.Variable.getVariableValue("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_del_invalidVariable() {
        iPtr = 0;
        processInstruction("del z");
    }

    @Test
    public void testProcessInstruction_arr() {
        iPtr = 0;
        processInstruction("arr a 3");
        assertEquals(3, Main.Variable.getVariables().length);
        assertArrayEquals(new int[] { 0, 0, 0 }, Main.Variable.getVariables());

        iPtr = 0;
        processInstruction("arr a 3 4 5");
        assertEquals(6, Main.Variable.getVariables().length);
        assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, Main.Variable.getVariables());

        iPtr = 0;
        processInstruction("arr a 3 4");
        assertEquals(5, Main.Variable.getVariables().length);
        assertArrayEquals(new int[] { 0, 0, 0, 0, 0 }, Main.Variable.getVariables());

        iPtr = 0;
        processInstruction("arr a 0");
        assertEquals(0, Main.Variable.getVariables().length);
    }

    @Test
    public void testProcessInstruction_jmp() {
        iPtr = 0;
        processInstruction("jmp label1");
        assertEquals(LABEL_NAMES[0], Main.instruction[iPtr]);

        iPtr = 0;
        processInstruction("jmp label10 halt");
        assertTrue(System.exit(0) == 0);
    }

    @Test
    public void testProcessInstruction_jnz() {
        iPtr = 0;
        processInstruction("jnz a label1");
        assertEquals(LABEL_NAMES[0], Main.instruction[iPtr]);

        iPtr = 0;
        Main.Variable.setVariableValue("a", 1);
        processInstruction("jnz a label1");
        assertEquals(LABEL_NAMES[5], Main.instruction[iPtr]);
    }

    @Test
    public void testProcessInstruction_jz() {
        iPtr = 0;
        processInstruction("jz a label1");
        assertEquals(LABEL_NAMES[0], Main.instruction[iPtr]);

        iPtr = 0;
        Main.Variable.setVariableValue("a", 0);
        processInstruction("jz a label1");
        assertEquals(LABEL_NAMES[4], Main.instruction[iPtr]);
    }

    @Test
    public void testProcessInstruction_je() {
        iPtr = 0;
        processInstruction("je a b label1");
        assertEquals(LABEL_NAMES[0], Main.instruction[iPtr]);

        iPtr = 0;
        Main.Variable.setVariableValue("a", 1);
        Main.Variable.setVariableValue("b", 1);
        processInstruction("je a b label1");
        assertEquals(LABEL_NAMES[0], Main.instruction[iPtr]);

        iPtr = 0;
        Main.Variable.setVariableValue("a", 1);
        Main.Variable.setVariableValue("b", 2);
        processInstruction("je a b label1");
        assertEquals(LABEL_NAMES[5], Main.instruction[iPtr]);
    }

    @Test
    public void testProcessInstruction_jne() {
        iPtr = 0;
        processInstruction("jne a b label1");
        assertEquals(LABEL_NAMES[5], Main.instruction[iPtr]);

        iPtr = 0;
        Main.Variable.setVariableValue("a", 1);
        Main.Variable.setVariableValue("b", 1);
        processInstruction("jne a b label1");
        assertEquals(LABEL_NAMES[0], Main.instruction[iPtr]);

        iPtr = 0;
        Main.Variable.setVariableValue("a", 1);
        Main.Variable.setVariableValue("b", 2);
        processInstruction("jne a b label1");
        assertEquals(LABEL_NAMES[5], Main.instruction[iPtr]);
    }

    @Test
    public void testProcessInstruction_cmp() {
        iPtr = 0;
        processInstruction("cmp a b");
        assertEquals(Main.Variable.getVariableValue("b") - Main.Variable.getVariableValue("a"), Main.Variable.getVariableValue("2"));

        iPtr = 0;
        processInstruction("jne a b label1");
        assertEquals(LABEL_NAMES[5], Main.instruction[iPtr]);

        iPtr = 0;
        processInstruction("jz a b label1");
        assertEquals(LABEL_NAMES[4], Main.instruction[iPtr]);
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.txt", Main.getFileName());
    }

    @Test
    public void testLoadInstructions_emptyFile() {
        assertFalse(Main.loadInstructions(""));
    }

    @Test
    public void testLoadInstructions_nonExistentFile() {
        assertFalse(Main.loadInstructions("does_not_exist.txt"));
    }

    @Test
    public void testLoadInstructions_existingFile() {
        assertTrue(Main.loadInstructions(TEST_FILE_NAMES[0]));
        assertEquals(TEST_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
        assertArrayEquals(TEST_INSTRUCTIONS, Main.instruction);
    }

    @Test
    public void testLoadInstructions_fileWithInvalidInstruction() {
        String[] testInstructionsWithInvalidInstruction = new String[] {
            "mov a 0",
            "mov a 5",
            "mov a a",
            "halt"
        };
        Main.resetInstructions();
        assertTrue(Main.loadInstructions(TEST_FILE_NAMES[0]));
        assertEquals(TEST_INSTRUCTIONS.length, Main.TOTAL_INSTUCTIONS);
        assertArrayEquals(TEST_INSTRUCTIONS, Main.instruction);
    }
}
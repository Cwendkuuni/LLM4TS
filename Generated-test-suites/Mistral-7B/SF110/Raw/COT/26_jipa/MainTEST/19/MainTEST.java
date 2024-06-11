package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    private static final String TEST_FILE_NAME = "testInstructions.asm";
    private static final String VALID_INSTRUCTION_SET = "mov a 0\n" +
                                                       "mov b a\n" +
                                                       "inc b\n" +
                                                       "mov a b\n" +
                                                       "jmp :1\n";
    private static final String INVALID_INSTRUCTION_SET = "mov a b\n" +
                                                         "jmp :100\n"; // Invalid label
    private static final String MISSING_ARGUMENT_INSTRUCTION = "mov a\n";
    private static final String VARIABLE_NAME = "a";
    private static final int INITIAL_VALUE_A = 5;
    private static final int EXPECTED_VALUE_A = 6;
    private static final int EXPECTED_VALUE_B = 6;

    @Before
    public void setup() {
        System.setIn(new ByteArrayInputStream(VALID_INSTRUCTION_SET.getBytes()));
        Main.main(new String[0]);
    }

    @Test
    public void testLoadInstructionsWithValidFileName() {
        assertEquals(Main.TOTAL_INSTRUCTIONS, 5);
    }

    @Test
    public void testProcessInstructionMov() {
        assertEquals(Variable.getVariableValue(VARIABLE_NAME), INITIAL_VALUE_A);

        Main.processInstruction("mov a 0");
        assertEquals(Variable.getVariableValue(VARIABLE_NAME), 0);

        Main.processInstruction("mov b a");
        assertEquals(Variable.getVariableValue("b"), 0);

        Main.processInstruction("mov a b");
        assertEquals(Variable.getVariableValue(VARIABLE_NAME), EXPECTED_VALUE_B);
        assertEquals(Variable.getVariableValue("b"), EXPECTED_VALUE_A);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstructionInvalidOpcode() {
        Main.processInstruction("xyz");
    }

    @Test
    public void testProcessInstructionJmp() {
        int originalIPtr = Main.iPtr;

        Main.processInstruction("jmp :1");
        assertEquals(Main.iPtr, 1);

        Main.processInstruction("mov a 5"); // Set the value of variable a back to 5
        Main.processInstruction("mov b 4");

        Main.processInstruction("jmp :" + (originalIPtr + 2));
        assertEquals(Main.iPtr, originalIPtr + 2);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstructionInvalidJmpTarget() {
        Main.processInstruction("jmp :100");
    }

    @Test
    public void testProcessInstructionJnz() {
        Main.processInstruction("mov a 0");
        Main.processInstruction("mov b 5");

        Main.processInstruction("jnz :1");
        assertEquals(Main.iPtr, 1);

        Main.processInstruction("mov a 5");
        Main.processInstruction("mov b 0");

        Main.processInstruction("jnz :1");
        assertEquals(Main.iPtr, Main.TOTAL_INSTRUCTIONS);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstructionInvalidJnzCondition() {
        Main.processInstruction("mov a 0");
        Main.processInstruction("mov b 5");

        Main.processInstruction("jnz :100");
    }

    @Test
    public void testProcessInstructionJz() {
        Main.processInstruction("mov a 0");
        Main.processInstruction("mov b 5");

        Main.processInstruction("jz :1");
        assertEquals(Main.iPtr, Main.TOTAL_INSTRUCTIONS);

        Main.processInstruction("mov a 5");
        Main.processInstruction("mov b 0");

        Main.processInstruction("jz :1");
        assertEquals(Main.iPtr, 1);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstructionInvalidJzTarget() {
        Main.processInstruction("mov a 0");
        Main.processInstruction("mov b 5");

        Main.processInstruction("jz :100");
    }

    @Test
    public void testProcessInstructionJne() {
        Main.processInstruction("mov a 0");
        Main.processInstruction("mov b 5");

        Main.processInstruction("je :1");
        assertEquals(Main.iPtr, Main.TOTAL_INSTRUCTIONS);

        Main.processInstruction("mov a 5");
        Main.processInstruction("mov b 0");

        Main.processInstruction("jne :1");
        assertEquals(Main.iPtr, 1);
    }

    @Test(expected = AssertionError.class)
    public void testProcessInstructionInvalidJneTarget() {
        Main.processInstruction("mov a 0");
        Main.processInstruction("mov b 5");

        Main.processInstruction("jne :100");
    }

    @Test
    public void testProcessInstructionCmp() {
        Main.processInstruction("mov a 5");
        Main.processInstruction("mov b 5");

        Main.processInstruction("cmp a b");
        assertEquals(Variable.getVariableValue(" comparisonResult"), 0);

        Main.processInstruction("mov a 6");
        Main.processInstruction("mov b 5");

        Main.processInstruction("cmp a b");
        assertEquals(Variable.getVariableValue(" comparisonResult"), 1);
    }

    @Test
    public void testLoadInstructionsWithInvalidFileName() {
        Main.main(new String[0]);
        assertEquals(Main.iPtr, 0);
    }

    @Test
    public void testLoadInstructionsWithEmptyFileName() {
        Main.main(new String[0]);
        assertEquals(Main.iPtr, 0);
    }

    @Test
    public void testLoadInstructionsWithInvalidInstructionSet() {
        System.setIn(new ByteArrayInputStream(INVALID_INSTRUCTION_SET.getBytes()));

        Main.main(new String[0]);
        assertEquals(Main.iPtr, 0);
    }

    @Test
    public void testLoadInstructionsWithMissingArgumentInstruction() {
        System.setIn(new ByteArrayInputStream(MISSING_ARGUMENT_INSTRUCTION.getBytes()));

        Main.main(new String[0]);
        assertEquals(Main.iPtr, 0);
    }
}
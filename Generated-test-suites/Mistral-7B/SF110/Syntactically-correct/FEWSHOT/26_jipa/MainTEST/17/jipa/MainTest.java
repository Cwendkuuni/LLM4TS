package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testLoadInstructions() {
        String[] args = {"testFile.txt"};
        Main.main(args);
        assertEquals(Main.TOTAL_INSTRUCTIONS, 10); // Adjust the number according to the number of instructions in your test file

        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; ++i) {
            assertNotNull(Main.instruction[i]);
        }
    }

    @Test
    public void testProcessInstruction() {
        String[] args = {"testFile.txt"};
        Main.main(args);

        // Replace the following test cases with the instructions you want to test and their expected outcomes

        assertEquals(1, Variable.getVariableValue("result")); // Test for 'set' instruction
        assertEquals(2, Variable.getVariableValue("counter")); // Test for 'mov' instruction
        assertEquals(3, Variable.getVariableValue("counter")); // Test for 'inc' instruction
        assertEquals("Hello World", System.out.getOut().toString()); // Test for 'out' instruction
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstructionWithInvalidInstruction() {
        String[] args = {"testFile.txt"};
        Main.main(args);
        Main.processInstruction("invalidInstruction");
    }
}
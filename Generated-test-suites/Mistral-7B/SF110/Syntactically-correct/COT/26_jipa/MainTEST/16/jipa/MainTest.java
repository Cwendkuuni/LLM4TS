import static jipa.Main.opcodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testLoadInstructionsWithValidFileName() {
        String fileName = "test.txt";
        assertTrue(Main.loadInstructions(fileName));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadInstructionsWithEmptyFileName() {
        Main.loadInstructions("");
    }

    @Test
    public void testLoadInstructionsWithInvalidFileName() {
        String fileName = "test.txtq";
        assertFalse(Main.loadInstructions(fileName));
    }

    @Test
    public void testLoadInstructionsWithQ() {
        Main.loadInstructions("q");
        // You may need to add assertions here to check the expected behavior when the user types 'q'
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructionsWithInvalidFile() {
        String fileName = "test.non_existent_file";
        Main.loadInstructions(fileName);
    }

    @Test
    public void testProcessInstructionWithHaltingOpcode() {
        String instruction = "halt";
        Main.processInstruction(instruction);
        fail("Halting opcode should terminate the program");
    }

    @Test
    public void testProcessInstructionWithOutOpcode() {
        String instruction = "out x";
        String expectedOutput = "x";
        System.out.flush();
        Main.processInstruction(instruction);
        assertEquals(expectedOutput, System.out.getBuffer().toString());
        System.out.getBuffer().setLength(0);
    }

    @Test
    public void testProcessInstructionWithInOpcode() {
        String instruction = "in x";
        String input = "Hello World";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String expectedInput = input;
        Main.processInstruction("in x");
        assertEquals(expectedInput, Main.Variable.getVariableValue("x"));
        System.setIn(System.in);
    }

    // Add test cases for other opcodes (mov, cpy, set, del, arr, var, inc, dec, add, sub, mul, div, mod, xor, or, and, jmp, jnz, jz, je, ja, jae, jb, jbe, jne)
    // You may also need to test exception handling scenarios for opcodes (e.g., trying to access a non-existent variable, attempting to divide by zero, etc.)
}
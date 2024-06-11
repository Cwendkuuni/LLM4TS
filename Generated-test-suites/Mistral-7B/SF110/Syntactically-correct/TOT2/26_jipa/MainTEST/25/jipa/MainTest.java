package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    static final String[] instructions = {"mov a 0", "inc a", "out a"};

    @Before
    public void setup() {
        Main.resetInstructions();
        Main.loadInstructions("file.txt");
    }

    @Test
    public void testMovInstruction() {
        Main.instruction[0] = "mov a 1"; // Change the first instruction to test with a different variable
        Main.processInstruction(Main.instruction[0]);
        assertEquals(1, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testIncInstruction() {
        Main.processInstruction(Main.instruction[1]);
        assertEquals(2, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testOutInstruction() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                assertEquals("Expected output", s);
            }
        });
        Main.processInstruction(Main.instruction[2]);
        Main.processInstruction("out a");
        assertEquals("Expected output", Main.Variable.getVariableValue("a") + "\n");
    }

    @Test(expected = RuntimeException.class)
    public void testUnknownOpcode() {
        String[] unknownInstructions = {"xyz 0"};
        Main.loadInstructions("file.txt");
        for (String instruction : unknownInstructions) {
            Main.instruction[Main.iPtr] = instruction;
            Main.processInstruction(Main.instruction[Main.iPtr]);
        }
    }

    @Test
    public void testLoadInstructionsFileNotFound() {
        Main.instruction = new String[0];
        assertFalse(Main.loadInstructions("non_existent_file.txt"));
    }

    @Test
    public void testLoadInstructionsEmptyFile() {
        Main.instruction = new String[0];
        String emptyFileContent = "";
        Main.loadInstructions(emptyFileContent);
        assertTrue(Main.instruction.length == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyInputFile() {
        Main.loadInstructions("");
    }

    @Test
    public void testLoadInstructionsInvalidInputFormat() {
        String invalidInput = "mov a\ninc a";
        Main.loadInstructions(invalidInput);
        assertFalse(Main.instruction.length == 1);
    }
}
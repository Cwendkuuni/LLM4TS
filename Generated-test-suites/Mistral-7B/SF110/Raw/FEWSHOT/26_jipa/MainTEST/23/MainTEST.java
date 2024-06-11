package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testIsNumeric() {
        assertTrue(isNumeric("123"));
        assertTrue(isNumeric("0xff"));
        assertFalse(isNumeric("abc"));
        assertFalse(isNumeric(""));
        assertFalse(isNumeric("123a"));
    }

    @Test
    public void testGetStringValue() {
        assertEquals("\"foo\"", getStringValue("mov foo, 0"));
        assertEquals("\"bar\"", getStringValue("mov 0, bar"));
        assertEquals("\"foo bar\"", getStringValue("mov foo, bar"));
    }

    @Test
    public void testSplitArguments() {
        String[] args = splitArguments("mov a, b c d");
        assertEquals("b", args[0]);
        assertEquals("c", args[1]);
        assertEquals("d", args[2]);

        args = splitArguments("mov a \"foo bar\", b");
        assertEquals("foo bar", args[0]);
        assertEquals("b", args[1]);

        args = splitArguments("mov a , b");
        assertEquals(new String[]{}, args);

        args = splitArguments("mov a 1, b");
        assertEquals("1", args[0]);
        assertEquals(new String[]{}, args[1]);
    }

    @Test
    public void testLoadInstructions() throws Exception {
        String fileName = "test.txt";
        assertFalse(loadInstructions(fileName));

        String content = "mov a, b\n" +
                "mov c, a\n" +
                "jmp label\n" +
                "label: jmp halt";
        createFile(fileName, content);
        assertTrue(loadInstructions(fileName));
        assertEquals("mov a, b", Main.instruction[0]);
        assertEquals("mov c, a", Main.instruction[1]);
        assertEquals("jmp label", Main.instruction[2]);
        assertEquals("label: jmp halt", Main.instruction[3]);

        createFile(fileName, "");
        resetInstructions();
        assertTrue(loadInstructions(fileName));
        assertEquals(0, Main.instruction.length);
    }

    @Test
    public void testProcessInstruction() {
        Main.instruction = new String[]{"mov a, b", "mov c, a", "jmp label"};
        iPtr = 0;
        processInstruction(Main.instruction[iPtr]);
        assertEquals("b", Variable.getVariableValue("a"));
        assertEquals("mov a, b", Main.instruction[iPtr]);

        processInstruction(Main.instruction[++iPtr]);
        assertEquals("a", Variable.getVariableValue("c"));
        assertEquals("mov c, a", Main.instruction[iPtr]);

        processInstruction(Main.instruction[++iPtr]);
        assertNull(Variable.getVariableValue("a"));
        assertEquals("jmp label", Main.instruction[iPtr]);

        processInstruction(Main.instruction[++iPtr]);
        assertEquals("halt", Main.instruction[iPtr]);
    }

    private void createFile(String fileName, String content) throws Exception {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(content);
        }
    }

    private void resetInstructions() {
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; ++i) {
            Main.instruction[i] = "";
        }
    }
}
package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testMain() {
        String[] args = new String[]{"test.txt"};
        Main.main(args);
        // Add asserts here to check the program output or any variables that are expected to be modified.
    }

    @Test
    public void testProcessInstruction_halt() {
        Main.instruction = new String[]{"halt"};
        Main.iPtr = 0;
        Main.main(new String[0]);
        assertEquals(0, Main.iPtr);
        assertTrue(System.exit.called());
    }

    @Test
    public void testProcessInstruction_out() {
        Main.instruction = new String[]{"out", "Hello"};
        Main.iPtr = 0;
        Main.main(new String[0]);
        assertEquals("Hello", System.out.getHistory().get(0));
    }

    @Test
    public void testProcessInstruction_in() {
        Main.instruction = new String[]{"in", "x"};
        int value = 42;
        when(System.in.nextLine()).thenReturn(Integer.toString(value));
        Main.main(new String[0]);
        assertEquals(value, Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_mov() {
        Main.instruction = new String[]{"mov", "x", "y"};
        Variable.setVariableValue("x", 42);
        Variable.setVariableValue("y", 0);
        Main.main(new String[0]);
        assertEquals(42, Variable.getVariableValue("y"));
        assertEquals(0, Variable.getVariableValue("x"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_mov_invalidIndex() {
        Main.instruction = new String[]{"mov", "x", "z"};
        Variable.setVariableValue("x", 42);
        Main.main(new String[0]);
    }

    // Add more test cases for other methods in the class...

    @Test
    public void testLoadInstructions_validFile() {
        String fileName = "test.txt";
        String[] instructions = {"out", "Hello"};
        given(System.in.nextLine()).willReturn(fileName);
        try (FileInputStream fin = new FileInputStream(fileName)) {
            BufferedReader inputFile = new BufferedReader(new InputStreamReader(fin));
            LineNumberReader linecount = new LineNumberReader(new FileReader(fileName));
            when(linecount.getLineNumber()).thenReturn(instructions.length + 1);
            Main.loadInstructions(fileName);
            assertEquals(instructions.length, Main.TOTAL_INSTRUCTIONS);
            assertArrayEquals(instructions, Main.instruction);
            verifyNoMoreInteractions(inputFile);
            verifyNoMoreInteractions(linecount);
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructions_invalidFile() {
        String fileName = "non_existent_file.txt";
        given(System.in.nextLine()).willReturn(fileName);
        Main.loadInstructions(fileName);
    }

    // Add more test cases for other methods in the class...

}
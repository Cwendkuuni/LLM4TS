
package jipa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MainTest {
    private Variable mockVariable;

    @Before
    public void setup() {
        mockVariable = mock(Variable.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMain() throws Exception {
        String fileName = "testInstructions.txt";
        String expectedInstruction = "out, message";

        Main.main(new String[]{fileName});

        // Check if instruction was processed correctly
        assertEquals(expectedInstruction, Main.instruction[0]);

        // Check if halt instruction is reached and program exits
        Main.main(new String[]{fileName, "halt"});
        assertTrue(System.exit(0) != 0);
    }

    @Test
    public void testProcessInstruction_out() {
        String instruction = "out, message";
        String expectedMessage = "message";
        doNothing().when(mockVariable).setVariableValue(anyString(), anyInt());
        Main.processInstruction(instruction);
        assertEquals(expectedMessage, System.out.getHistogram().get(0));
    }

    @Test
    public void testProcessInstruction_in() {
        String instruction = "in, variableName";
        int expectedValue = 42;
        when(mockVariable.getVariableValue(eq("variableName"))).thenReturn(expectedValue);
        Main.processInstruction(instruction);
        assertEquals(expectedValue, Main.Variable.getVariableValue("variableName"));
    }

    // Add more test cases for other opcodes

    @Test
    public void testLoadInstructions_validFile() throws Exception {
        String fileName = "testInstructions.txt";
        String expectedInstruction = "out, message";
        Main.loadInstructions(fileName);
        assertEquals(expectedInstruction, Main.instruction[0]);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructions_invalidFile() throws Exception {
        String fileName = "nonExistentFile.txt";
        Main.loadInstructions(fileName);
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadInstructions_invalidArguments() throws Exception {
        String fileName = "invalidInstructions.txt";
        Main.loadInstructions(fileName);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructions_fileNotFound() throws Exception {
        String fileName = "nonExistentFile.txt";
        Main.loadInstructions(fileName);
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadInstructions_invalidArguments() throws Exception {
        String fileName = "invalidInstructions.txt";
        Main.loadInstructions(fileName);
    }
}

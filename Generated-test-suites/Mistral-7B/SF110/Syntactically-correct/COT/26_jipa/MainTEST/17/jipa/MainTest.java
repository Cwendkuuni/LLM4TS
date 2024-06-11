package jipa;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {

    @BeforeClass
    public static void setup() {
        // Perform any setup that needs to be done before the tests are run
    }

    

    // Basic test cases for public methods

    @Test
    public void testProcessInstruction_OpcodeIsHalt() {
        String[] instructions = {"halt"};
        Main.instruction = instructions;
        Main.iPtr = 0;

        Main.main(new String[0]);

        // Verify that the program has exited
        assertTrue(System.exit.called());
    }

    @Test
    public void testProcessInstruction_OpcodeIsOut() {
        String[] instructions = {"out", "Hello"};
        Main.instruction = instructions;
        Main.iPtr = 0;

        Main.main(new String[0]);

        // Verify that the message "Hello" has been printed to the console
        assertEquals("Hello", System.out.getHistogram().toString());
    }

    @Test
    public void testProcessInstruction_OpcodeIsIn() {
        Main.instruction = new String[]{"in", "inputVariable"};
        int inputValue = 42;
        when(System.in.nextLine()).thenReturn(Integer.toString(inputValue));

        Main.main(new String[0]);

        // Verify that the value of the input variable has been set to the input value
        assertEquals(inputValue, Variable.getVariableValue("inputVariable"));
    }

    @Test
    public void testProcessInstruction_OpcodeIsMov() {
        String[] instructions = {"mov", "sourceVariable", "destinationVariable"};
        int sourceValue = 10;
        Main.instruction = instructions;
        Variable.setVariableValue("sourceVariable", sourceValue);
        Main.iPtr = 0;

        Main.main(new String[0]);

        // Verify that the value of the destination variable has been set to the value of the source variable
        assertEquals(sourceValue, Variable.getVariableValue("destinationVariable"));
    }

    // Test cases for edge cases and exception handling

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_InvalidOpcode() {
        String[] instructions = {"invalidOpcode", "sourceVariable", "destinationVariable"};
        Main.instruction = instructions;
        Main.iPtr = 0;

        Main.main(new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_NegativeNumberOfOperands() {
        String[] instructions = {"arr", "-1"};
        Main.instruction = instructions;
        Main.iPtr = 0;

        try {
            Main.main(new String[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_EmptyVariableName() {
        String[] instructions = {"set", "", 1};
        Main.instruction = instructions;
        Main.iPtr = 0;

        try {
            Main.main(new String[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    @Test
    public void testProcessInstruction_VariableNotFound() {
        String[] instructions = {"cpy", "nonExistentVariable", "destinationVariable"};
        Main.instruction = instructions;
        Main.iPtr = 0;

        Main.main(new String[0]);

        // Verify that the value of the destination variable has not been changed
        assertEquals(0, Variable.getVariableValue("destinationVariable"));
    }

    @Test
    public void testProcessInstruction_VariableValueOverflow() {
        String[] instructions = {"inc", "variableWithMaxValue"};
        int maxValue = Integer.MAX_VALUE;
        Variable.setVariableValue("variableWithMaxValue", maxValue);
        Main.instruction = instructions;
        Main.iPtr = 0;

        Main.main(new String[0]);

        // Verify that the value of the variable has not been changed (it should still be the max value)
        assertEquals(maxValue, Variable.getVariableValue("variableWithMaxValue"));
    }

    @Test
    public void testProcessInstruction_InvalidJumpValue() {
        String[] instructions = {"jmp", "invalidLabel"};
        Main.instruction = instructions;
        Main.iPtr = 0;

        try {
            Main.main(new String[0]);
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException, but got: " + e.getMessage());
        }
    }

    @Test
    public void testProcessInstruction_InvalidComparison() {
        String[] instructions = {"cmp", "operand1", "operand2", "invalidLabel"};
        int operand1 = 10;
        int operand2 = 5;
        Main.instruction = instructions;
        Variable.setVariableValue("operand1", operand1);
        Variable.setVariableValue("operand2", operand2);
        Main.iPtr = 0;

        try {
            Main.main(new String[0]);
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got: " + e.getMessage());
        }
    }

    
}
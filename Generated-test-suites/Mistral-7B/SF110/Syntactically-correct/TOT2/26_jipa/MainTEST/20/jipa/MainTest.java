import org.junit.Test;
import static org.junit.Assert.*;
import jipa.Main;

public class MainTest {
    

    @Test
    public void testMainEmptyArg() {
        try {
            Main.main(new String[0]);
            fail("Expected an error or exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testLoadInstructionsFileNotFound() {
        String fileName = "";
        assertFalse("Expected loadInstructions() to fail", Main.loadInstructions(fileName));
    }

    @Test
    public void testLoadInstructionsInvalidFormat() {
        String fileName = "invalid_format.txt";
        assertFalse("Expected loadInstructions() to fail", Main.loadInstructions(fileName));
    }

    @Test
    public void testLoadInstructionsOutOfMemory() {
        String fileName = "very_large_file.txt";
        try {
            Main.loadInstructions(fileName);
            fail("Expected an error or exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testProcessInstructionInvalidOpcode() {
        Main.iPtr = 0;
        String instruction = "invalid_opcode";
        Main.processInstruction(instruction);
        assertEquals("Expected opcode not found", Main.opcodes.novalue, Main.opcodes.toOpcode(instruction.toLowerCase()));
    }

    @Test
    public void testProcessInstructionVariableNotFound() {
        Main.iPtr = 0;
        String instruction = "mov x y";
        String varX = "x";
        String varY = "z";
        Main.instruction = new String[]{"mov x y"};

        assertNotSame("Expected variable x not to be the same as variable z", varX, varY);
        Main.Variable.addVariable(varX);
        Main.Variable.addVariable(varY);

        Main.processInstruction(instruction);
        assertEquals("Expected variable x value to be unchanged", 0, Main.Variable.getVariableValue(varX));
    }

    @Test
    public void testProcessInstructionInvalidDataType() {
        Main.iPtr = 0;
        String instruction = "mov x y";
        String varX = "x";
        String varY = "10";
        Main.instruction = new String[]{"mov x y"};

        Main.Variable.addVariable(varX);
        Main.Variable.addVariable(varY);

        try {
            Main.processInstruction(instruction);
            fail("Expected an error or exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testProcessInstructionArithmeticOverflow() {
        Main.iPtr = 0;
        String instruction = "mov x y";
        String varX = "x";
        String varY = "2147483648"; // Integer.MAX_VALUE
        Main.instruction = new String[]{"mov x y"};

        Main.Variable.addVariable(varX);
        Main.Variable.addVariable(varY);

        try {
            Main.processInstruction(instruction);
            fail("Expected an error or exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testProcessInstructionNegativeIndex() {
        Main.iPtr = 0;
        String instruction = "mov x [y-1]";
        String varX = "x";
        String varY = "1";
        Main.instruction = new String[]{"mov x [y-1]"};

        Main.Variable.addVariable(varX);
        Main.Variable.addVariable(varY);

        try {
            Main.processInstruction(instruction);
            fail("Expected an error or exception");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testProcessInstructionUninitializedVariable() {
        Main.iPtr = 0;
        String instruction = "mov x y";
        String varX = "z";
        String varY = "1";
        Main.instruction = new String[]{"mov x y"};

        try {
            Main.processInstruction(instruction);
            fail("Expected an error or exception");
        } catch (NoSuchElementException e) {
            // Expected
        }

        Main.Variable.addVariable(varX);
        Main.Variable.addVariable(varY);

        Main.processInstruction(instruction);
        assertEquals("Expected variable x value to be set to variable y value", 1, Main.Variable.getVariableValue(varX));
    }

    @Test
    public void testGetStringValueNonNumericInput() {
        String input = "hello";
        assertEquals("Expected getStringValue() to return an empty string", "", Main.getStringValue(input));
    }

    @Test
    public void testProcessInstructionUnknownLabel() {
        Main.iPtr = 0;
        String instruction = "jmp label";
        String label = "unknown";
        Main.instruction = new String[]{"jmp label"};

        try {
            Main.processInstruction(instruction);
            fail("Expected an error or exception");
        } catch (Exception e) {
            // Expected
        }
    }

    
}
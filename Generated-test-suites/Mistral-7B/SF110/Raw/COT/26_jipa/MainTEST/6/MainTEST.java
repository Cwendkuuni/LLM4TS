package jipa;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

    @Test
    public void testLoadInstructions() {
        String fileName = "testFile.txt";
        Main.loadInstructions(fileName);
        Assert.assertArrayNotEquals(new String[0], Main.instruction);
        Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, Main.instruction.length);
    }

    @Test
    public void testProcessInstruction_halt() {
        String[] args = {"halt"};
        Main.main(args);
        Assert.assertEquals(0, System.exitCode()); // Assuming System.exitCode() is a method to get the exit code from System.exit()
    }

    @Test
    public void testProcessInstruction_out() {
        String[] args = {"out", "Hello"};
        Main.main(args);
        Assert.assertTrue(System.out.toString().contains("Hello"));
    }

    @Test
    public void testProcessInstruction_in() {
        String[] args = {"in", "x"};
        int value = 10;
        Main.Variable.setVariableValue("x", value);
        Main.main(args);
        int readValue = Main.readValue();
        Assert.assertEquals(value, readValue);
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] args1 = {"mov", "x", "y"};
        Main.Variable.setVariableValue("x", 10);
        Main.main(args1);
        Assert.assertEquals(10, Main.Variable.getVariableValue("y"));

        String[] args2 = {"mov", "y", "x"};
        Main.Variable.setVariableValue("y", 20);
        Main.main(args2);
        Assert.assertEquals(20, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] args = {"inc", "x"};
        Main.Variable.setVariableValue("x", 5);
        Main.main(args);
        Assert.assertEquals(6, Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_jmp() {
        String fileName1 = "instructions1.txt";
        String fileName2 = "instructions2.txt";
        Main.loadInstructions(fileName1);
        Main.iPtr = 0;
        Main.main(new String[0]);

        Main.loadInstructions(fileName2);
        int expectedIPtr = 5;
        Main.iPtr = 0;
        Main.main(new String[0]);
        Assert.assertEquals(expectedIPtr, Main.iPtr);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessInstruction_jmp_outOfBounds() {
        String fileName = "testFile.txt";
        Main.loadInstructions(fileName);
        Main.iPtr = Main.TOTAL_INSTRUCTIONS;
        Main.main(new String[0]);
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] args = {"arr", "myArray", "5"};
        Main.main(args);
        Assert.assertEquals(6, Main.Variable.getVariableValue("myArray").length);
    }

    @Test(expected = NumberFormatException.class)
    public void testProcessInstruction_arr_invalidNumber() {
        String[] args = {"arr", "myArray", "abc"};
        Main.main(args);
    }

    @Test
    public void testProcessInstruction_add() {
        String[] args1 = {"add", "x", "y", "z"};
        Main.Variable.setVariableValue("x", 5);
        Main.Variable.setVariableValue("y", 3);
        Main.main(args1);
        Assert.assertEquals(8, Main.Variable.getVariableValue("z"));

        String[] args2 = {"add", "x", "y", "x"};
        Main.Variable.setVariableValue("x", 5);
        Main.Variable.setVariableValue("y", 3);
        Main.main(args2);
        Assert.assertEquals(10, Main.Variable.getVariableValue("x"));
    }

    // Test other methods and their edge cases accordingly
}
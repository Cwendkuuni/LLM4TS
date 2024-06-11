package jipa;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"testFile.asm"};
        System.setIn(new ByteArrayInputStream(("testFile.asm\n").getBytes()));
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        Main.main(args);
        // Add assertions here to check the expected output
    }

    @Test
    public void testLoadInstructionsWithValidFile() {
        String fileName = "testFile.asm";
        byte[] instructions = new byte[100];
        for (int i = 0; i < instructions.length; i++) {
            instructions[i] = (byte) i;
        }
        System.setIn(new ByteArrayInputStream(("testFile.asm\n" + new String(instructions)).getBytes()));
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        Main.loadInstructions(fileName);
        // Add assertions here to check the loaded instructions
    }

    @Test
    public void testLoadInstructionsWithEmptyFile() {
        System.setIn(new ByteArrayInputStream("".getBytes()));
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        boolean result = Main.loadInstructions("");
        Assert.assertFalse(result);
    }

    @Test
    public void testLoadInstructionsWithInvalidFile() {
        System.setIn(new ByteArrayInputStream("invalidFile.txt\n".getBytes()));
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        boolean result = Main.loadInstructions("invalidFile.txt");
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessInstructionWithValidOpcodeAndOperands() {
        String instruction = "mov x y";
        Main.iPtr = 0;
        Main.instruction = new String[]{"mov x y"};
        Variable.reset();
        Main.processInstruction(instruction);
        int expectedY = 0;
        int actualY = Variable.getVariableValue("y");
        Assert.assertEquals(expectedY, actualY);
    }

    @Test
    public void testProcessInstructionWithValidOpcodeAndNoOperands() {
        String instruction = "out";
        Main.iPtr = 0;
        Main.instruction = new String[]{"out"};
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        Main.processInstruction(instruction);
        // Add assertions here to check the output
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionWithInvalidOpcode() {
        String instruction = "invalidOp";
        Main.iPtr = 0;
        Main.instruction = new String[]{"invalidOp"};
        Main.processInstruction(instruction);
    }

    @Test
    public void testVariableFunctions() {
        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test addVariable
        Main.processInstruction("var x");
        Assert.assertEquals(1, Variable.getVariableCount());
        Assert.assertEquals("x", Variable.getVariableName(0));

        // Test setVariableValue
        Main.processInstruction("set x 5");
        Assert.assertEquals(5, Variable.getVariableValue("x"));

        // Test removeVariable
        Main.processInstruction("del x");
        Assert.assertEquals(0, Variable.getVariableCount());

        // Test getVariableValue with existing variable
        Main.processInstruction("set x 10");
        Main.processInstruction("mov y x");
        Main.processInstruction("inc y");
        Assert.assertEquals(11, Variable.getVariableValue("y"));

        // Test getVariableValue with non-existent variable
        Main.processInstruction("mov z x");
        Assert.assertEquals(0, Variable.getVariableValue("z"));
    }

    @Test
    public void testArithmeticOperations() {
        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test add
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 3");
        Main.processInstruction("add x y");
        Assert.assertEquals(8, Variable.getVariableValue("x"));

        // Test sub
        Main.processInstruction("set x 8");
        Main.processInstruction("set y 3");
        Main.processInstruction("sub x y");
        Assert.assertEquals(5, Variable.getVariableValue("x"));

        // Test mul
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 3");
        Main.processInstruction("mul x y");
        Assert.assertEquals(15, Variable.getVariableValue("x"));

        // Test div
        Main.processInstruction("set x 10");
        Main.processInstruction("set y 3");
        Main.processInstruction("div x y");
        Assert.assertEquals(3, Variable.getVariableValue("x"));

        // Test mod
        Main.processInstruction("set x 10");
        Main.processInstruction("set y 3");
        Main.processInstruction("mod x y");
        Assert.assertEquals(1, Variable.getVariableValue("x"));

        // Test xor
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 3");
        Main.processInstruction("xor x y");
        Assert.assertEquals(4, Variable.getVariableValue("x"));

        // Test or
        Main.processInstruction("set x 3");
        Main.processInstruction("set y 5");
        Main.processInstruction("or x y");
        Assert.assertEquals(7, Variable.getVariableValue("x"));

        // Test and
        Main.processInstruction("set x 3");
        Main.processInstruction("set y 5");
        Main.processInstruction("and x y");
        Assert.assertEquals(1, Variable.getVariableValue("x"));
    }

    @Test
    public void testComparisonOperations() {
        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test jmp
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 3");
        Main.processInstruction("jmp 5");
        Assert.assertEquals(5, Main.iPtr);

        // Test jnz
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 5");
        Main.processInstruction("jnz 5");
        Assert.assertEquals(0, Main.iPtr);

        // Test jz
        Main.processInstruction("set x 0");
        Main.processInstruction("set y 3");
        Main.processInstruction("jz 5");
        Assert.assertEquals(3, Main.iPtr);

        // Test je
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 5");
        Main.processInstruction("je 5");
        Assert.assertEquals(0, Main.iPtr);

        // Test jne
        Main.processInstruction("set x 5");
        Main.processInstruction("set y 6");
        Main.processInstruction("jne 5");
        Assert.assertEquals(1, Main.iPtr);

        // Test jmp with invalid label
        Main.processInstruction("jmp invalidLabel");
        Assert.assertEquals(0, Main.iPtr);

        // Test jmp with valid label
        Main.processInstruction("set x 5");
        Main.processInstruction("jmp label1");
        Main.processInstruction("label1: set iPtr 10");
        Assert.assertEquals(10, Main.iPtr);

        // Test jmp with relative address
        Main.processInstruction("set x 5");
        Main.processInstruction("jmp 2");
        Assert.assertEquals(3, Main.iPtr);
    }

    @Test
    public void testInputOutputOperations() {
        System.setIn(new ByteArrayInputStream("Hello".getBytes()));
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test in
        Main.processInstruction("in x");
        Main.processInstruction("out y");
        String expectedOutput = "Hello";
        String actualOutput = "";
        while (Main.iPtr < Main.TOTAL_INSTRUCTIONS - 1) {
            Main.processInstruction(Main.instruction[Main.iPtr]);
            if (Main.instruction[Main.iPtr].equals("out")) {
                actualOutput += (char) (Variable.getVariableValue("x") & 0xFF);
            }
        }
        Assert.assertEquals(expectedOutput, actualOutput);

        // Test out
        Main.resetInstructions();
        Main.processInstruction("set x 5");
        Main.processInstruction("out");
        String expectedOutput2 = "5";
        String actualOutput2 = "";
        while (Main.iPtr < Main.TOTAL_INSTRUCTIONS - 1) {
            Main.processInstruction(Main.instruction[Main.iPtr]);
            if (Main.instruction[Main.iPtr].equals("out")) {
                actualOutput2 += (char) (Variable.getVariableValue("x") & 0xFF);
            }
        }
        Assert.assertEquals(expectedOutput2, actualOutput2);
    }

    @Test
    public void testLabelFunction() {
        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test label and jmp
        Main.processInstruction("label1: set x 5");
        Main.processInstruction("jmp label1");
        Assert.assertEquals(5, Variable.getVariableValue("x"));

        // Test label and jmp with relative address
        Main.processInstruction("label1: set x 5");
        Main.processInstruction("jmp 2");
        Assert.assertEquals(3, Main.iPtr);

        // Test label list size
        Main.processInstruction("label1: set x 5");
        Main.processInstruction("label2: set y 10");
        Main.processInstruction("jmp label1");
        List<String> labels = Label.getLabels();
        Assert.assertEquals(2, labels.size());
        Assert.assertTrue(labels.contains("label1"));
        Assert.assertTrue(labels.contains("label2"));
    }

    @Test
    public void testInvalidInstruction() {
        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test invalid instruction
        Main.processInstruction("invalidInstruction");
        Assert.assertEquals(0, Main.iPtr);
    }

    @Test
    public void testInvalidOpcode() {
        Main.iPtr = 0;
        Main.instruction = new String[10];

        // Test invalid opcode
        Main.processInstruction("invalidOpCode");
        Assert.assertEquals(0, Main.iPtr);
    }
}
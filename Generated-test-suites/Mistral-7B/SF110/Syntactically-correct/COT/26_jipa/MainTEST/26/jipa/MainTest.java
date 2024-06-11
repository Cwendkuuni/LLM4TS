package jipa;

import org.junit.Assert;
import org.junit.Test;

import static jipa.Main.opcodes.*;

public class MainTest {
    
    
    @Test
    public void testMain() {
        String[] args = new String[]{"testFile.jip"};
        Main.main(args);
    }

    @Test(expected = Exception.class)
    public void testLoadInstructionsInvalidFile() {
        Main.loadInstructions("");
    }

    @Test
    public void testLoadInstructionsValidFile() {
        String fileName = "testFile.jip";
        Main.loadInstructions(fileName);
        Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, 10); // Replace 10 with the actual number of instructions in your test file
    }

    @Test
    public void testProcessInstructionHalts() {
        String instruction = "halt";
        Main.processInstruction(instruction);
        Assert.assertEquals(System.exit.callCount, 1);
    }

    @Test
    public void testProcessInstructionOut() {
        String instruction = "out var1";
        Main.processInstruction(instruction);
        // Add assertion to check the output
    }

    @Test
    public void testProcessInstructionIn() {
        String instruction = "in var1";
        int value = 5;
        Main.Variable.setVariableValue("var1", value);
        Main.processInstruction(instruction);
        int readValue = Main.readValue();
        Assert.assertEquals(value, readValue);
    }

    @Test
    public void testProcessInstructionMov() {
        String instruction = "mov var1 var2";
        Main.processInstruction(instruction);
        int var1Value = Main.Variable.getVariableValue("var1");
        int var2Value = Main.Variable.getVariableValue("var2");
        Assert.assertEquals(var1Value, var2Value);
    }

    @Test(expected = Exception.class)
    public void testProcessInstructionInvalidOpcode() {
        String instruction = "invalidOpcode var1";
        Main.processInstruction(instruction);
    }

    @Test
    public void testProcessInstructionJmp() {
        String instruction = "jmp label1";
        Main.processInstruction(instruction);
        int iPtr = Main.iPtr;
        Main.processInstruction("label1:");
        Assert.assertEquals(iPtr + 1, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJnz() {
        String instruction = "mov var1 1";
        Main.processInstruction(instruction);
        Main.processInstruction("jnz label1 var1");
        int iPtr = Main.iPtr;
        Main.processInstruction("label1:");
        Assert.assertEquals(iPtr + 1, Main.iPtr);

        Main.processInstruction("mov var1 0");
        Main.processInstruction("jnz label1 var1");
        Assert.assertEquals(iPtr, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJz() {
        String instruction = "mov var1 1";
        Main.processInstruction(instruction);
        Main.processInstruction("jz label1 var1");
        int iPtr = Main.iPtr;
        Assert.assertEquals(iPtr, Main.iPtr);

        Main.processInstruction("mov var1 0");
        Main.processInstruction("jz label1 var1");
        int label1Index = Main.TOTAL_INSTRUCTIONS - 2; // Replace with the actual index of label1
        Assert.assertEquals(label1Index, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJae() {
        String instruction1 = "mov var1 3";
        Main.processInstruction(instruction1);
        Main.processInstruction("mov var2 2");
        Main.processInstruction("jae label1 var1 var2");
        int iPtr = Main.iPtr;
        Assert.assertEquals(iPtr + 1, Main.iPtr);

        String instruction2 = "mov var1 1";
        Main.processInstruction(instruction2);
        Main.processInstruction("jae label1 var1 var2");
        Assert.assertEquals(iPtr, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJb() {
        String instruction1 = "mov var1 2";
        Main.processInstruction(instruction1);
        Main.processInstruction("mov var2 3");
        Main.processInstruction("jb label1 var1 var2");
        int iPtr = Main.iPtr;
        Assert.assertEquals(iPtr + 1, Main.iPtr);

        String instruction2 = "mov var1 4";
        Main.processInstruction(instruction2);
        Main.processInstruction("jb label1 var1 var2");
        Assert.assertEquals(iPtr, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJne() {
        String instruction1 = "mov var1 1";
        Main.processInstruction(instruction1);
        Main.processInstruction("mov var2 2");
        Main.processInstruction("jne label1 var1 var2");
        int iPtr = Main.iPtr;
        Assert.assertEquals(iPtr, Main.iPtr);

        String instruction2 = "mov var1 2";
        Main.processInstruction(instruction2);
        Main.processInstruction("jne label1 var1 var2");
        int label1Index = Main.TOTAL_INSTRUCTIONS - 2; // Replace with the actual index of label1
        Assert.assertEquals(label1Index, Main.iPtr);
    }

    @Test
    public void testProcessInstructionCmp() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        Main.processInstruction("mov var2 3");
        String instruction2 = "cmp var1 var2";
        Main.processInstruction(instruction2);
        int var1Value = Main.Variable.getVariableValue("var1");
        int var2Value = Main.Variable.getVariableValue("var2");
        int cmpValue = Main.Variable.getVariableValue("cmpResult");
        Assert.assertEquals(var1Value > var2Value, cmpValue == 1);

        String instruction3 = "mov var1 3";
        Main.processInstruction(instruction3);
        Main.processInstruction("cmp var1 var2");
        Assert.assertEquals(var1Value <= var2Value, cmpValue == 0);
    }

    @Test
    public void testProcessInstructionAdd() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "add var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 8);
    }

    @Test
    public void testProcessInstructionSub() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "sub var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 2);
    }

    @Test
    public void testProcessInstructionMul() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "mul var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 15);
    }

    @Test
    public void testProcessInstructionDiv() {
        String instruction1 = "mov var1 10";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 2";
        Main.processInstruction(instruction2);
        String instruction3 = "div var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 5);
    }

    @Test
    public void testProcessInstructionMod() {
        String instruction1 = "mov var1 10";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "mod var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 1);
    }

    @Test
    public void testProcessInstructionXor() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "xor var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 2);
    }

    @Test
    public void testProcessInstructionOr() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "or var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 6);
    }

    @Test
    public void testProcessInstructionAnd() {
        String instruction1 = "mov var1 5";
        Main.processInstruction(instruction1);
        String instruction2 = "mov var2 3";
        Main.processInstruction(instruction2);
        String instruction3 = "and var1 var2";
        Main.processInstruction(instruction3);
        int var1Value = Main.Variable.getVariableValue("var1");
        Assert.assertEquals(var1Value, 1);
    }

    
}
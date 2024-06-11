package jipa.test;

import static org.junit.Assert.*;

import org.junit.Test;

import jipa.Main;
import jipa.Variable;

public class MainTest {

    @Test
    public void testLoadInstructions() {
        String fileName = "testFile.txt";
        Main.loadInstructions(fileName);
        assertEquals(5, Main.instruction.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInstructionsEmptyFile() {
        Main.loadInstructions("");
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInstructionsInvalidFile() {
        Main.loadInstructions("invalidFile.txt");
    }

    @Test
    public void testProcessInstructionHalt() {
        Main.processInstruction("halt");
        fail("Method halt should terminate the program.");
    }

    @Test
    public void testProcessInstructionOut() {
        Variable.addVariable("output");
        Main.processInstruction("out var output");
        assertEquals("", Variable.getVariableValue("output"));
        Main.processInstruction("out 42 var output");
        assertEquals("42", Variable.getVariableValue("output"));
    }

    @Test
    public void testProcessInstructionIn() {
        Variable.addVariable("input");
        Main.processInstruction("in var input");
        assertEquals(0, Variable.getVariableValue("input"));
        Main.processInstruction("in var input");
        assertEquals(42, Variable.processInstruction("in var input", 42));
        assertEquals(42, Variable.getVariableValue("input"));
    }

    @Test
    public void testProcessInstructionMov() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Main.processInstruction("mov var1 var2");
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        Main.processInstruction("mov var1 5");
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testProcessInstructionSet() {
        Variable.addVariable("var1");
        Main.processInstruction("set var1 42");
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionSetInvalidVariable() {
        Main.processInstruction("set invalid var 42");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionSetInvalidValue() {
        Variable.addVariable("var1");
        Main.processInstruction("set var1 invalid");
    }

    @Test
    public void testProcessInstructionDel() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Main.processInstruction("del var1");
        assertEquals(0, Main.instruction.length);
        assertEquals(2, Variable.getVariables().size());
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        Main.processInstruction("del var2");
        assertEquals(0, Main.instruction.length);
        assertEquals(0, Variable.getVariables().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionDelInvalidVariable() {
        Variable.addVariable("var1");
        Main.processInstruction("del invalid");
    }

    @Test
    public void testProcessInstructionArr() {
        Variable.addVariable("arr");
        Main.processInstruction("arr arr 3");
        assertEquals(3, Variable.getVariableValue("arr").length);
        assertEquals(0, Variable.getVariableValue("arr")[0]);
        assertEquals(0, Variable.getVariableValue("arr")[1]);
        assertEquals(0, Variable.getVariableValue("arr")[2]);
        Main.processInstruction("mov var1 arr 0");
        Main.processInstruction("mov 42 var1");
        assertEquals(42, Variable.getVariableValue("arr")[0]);
        assertEquals(0, Variable.getVariableValue("arr")[1]);
        assertEquals(0, Variable.getVariableValue("arr")[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionArrInvalidVariable() {
        Variable.addVariable("arr");
        Main.processInstruction("arr invalid 3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstructionArrInvalidSize() {
        Variable.addVariable("arr");
        Main.processInstruction("arr arr -1");
    }

    @Test
    public void testProcessInstructionJmp() {
        Main.processInstruction("jmp 1");
        fail("Method jmp should not be reached.");
    }

    @Test
    public void testProcessInstructionJnz() {
        Variable.addVariable("var1");
        Main.processInstruction("mov 0 var1");
        Main.processInstruction("jnz 2 var1 3");
        assertEquals(3, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJz() {
        Variable.addVariable("var1");
        Main.processInstruction("mov 0 var1");
        Main.processInstruction("jz 1 var1 2");
        assertEquals(2, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJae() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Main.processInstruction("mov 1 var1");
        Main.processInstruction("mov 2 var2");
        Main.processInstruction("jae 1 var1 var2 3");
        assertEquals(3, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJbe() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Main.processInstruction("mov 2 var1");
        Main.processInstruction("mov 1 var2");
        Main.processInstruction("jbe 1 var1 var2 3");
        assertEquals(3, Main.iPtr);
    }

    @Test
    public void testProcessInstructionJne() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Main.processInstruction("mov 1 var1");
        Main.processInstruction("mov 1 var2");
        Main.processInstruction("jne 1 var1 var2 3");
        assertEquals(3, Main.iPtr);
    }

    @Test
    public void testProcessInstructionCmp() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Main.processInstruction("mov 2 var1");
        Main.processInstruction("mov 1 var2");
        Main.processInstruction("cmp var1 var2 1");
        assertEquals(1, Variable.getVariableValue("1"));
        assertEquals(0, Variable.getVariableValue("1"));
        assertEquals(0, Main.iPtr);

        Main.processInstruction("mov 1 var1");
        Main.processInstruction("mov 2 var2");
        Main.processInstruction("cmp var1 var2 1");
        assertEquals(0, Variable.getVariableValue("1"));
        assertEquals(1, Main.iPtr);
    }

    @Test
    public void testProcessInstructionXor() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Main.processInstruction("mov 2 var1");
        Main.processInstruction("mov 3 var2");
        Main.processInstruction("mov 1 var3");
        Main.processInstruction("xor var1 var2 var3");
        assertEquals(6, Variable.getVariableValue("var3"));
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstructionOr() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Main.processInstruction("mov 1 var1");
        Main.processInstruction("mov 0 var2");
        Main.processInstruction("mov 1 var3");
        Main.processInstruction("or var1 var2 var3");
        assertEquals(2, Variable.getVariableValue("var3"));
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstructionAnd() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Main.processInstruction("mov 0 var1");
        Main.processInstruction("mov 1 var2");
        Main.processInstruction("mov 1 var3");
        Main.processInstruction("and var1 var2 var3");
        assertEquals(0, Variable.getVariableValue("var3"));
        assertEquals(0, Main.iPtr);

        Main.processInstruction("mov 1 var1");
        Main.processInstruction("mov 1 var2");
        Main.processInstruction("mov 1 var3");
        Main.processInstruction("and var1 var2 var3");
        assertEquals(1, Variable.getVariableValue("var3"));
        assertEquals(1, Main.iPtr);
    }
}
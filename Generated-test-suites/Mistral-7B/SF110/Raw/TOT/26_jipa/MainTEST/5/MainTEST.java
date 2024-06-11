package jipa.test;

import org.junit.Test;
import static org.junit.Assert.*;
import jipa.Main;
import jipa.Variable;

public class MainTest {

 private static final String TEST_FILE = "testFile.txt";
 private static final String VALID_INSTRUCTION_FILE = "validInstructions.txt";
 private static final String INVALID_INSTRUCTION_FILE = "invalidInstructions.txt";

 @Test
 public void testLoadInstructionsValidFile() {
 Main.loadInstructions(VALID_INSTRUCTION_FILE);
 assertEquals(10, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructionsEmptyFile() {
 Main.loadInstructions("");
 assertEquals(0, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructionsInvalidFile() {
 Main.loadInstructions(INVALID_INSTRUCTION_FILE);
 assertEquals(0, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testProcessInstructionOut() {
 String instruction = "out i0";
 Main.processInstruction(instruction);
 assertEquals("", System.out.getError().toString());
 assertEquals("", System.out.getError().toString());
 assertEquals("", System.out.println());
 }

 @Test
 public void testProcessInstructionIn() {
 String instruction = "in i0";
 Main.processInstruction(instruction);
 assertNotNull(Variable.getVariableValue("i0"));
 }

 @Test(expected = NumberFormatException.class)
 public void testProcessInstructionInvalidIn() {
 String instruction = "in i0 invalid";
 Main.processInstruction(instruction);
 }

 @Test
 public void testProcessInstructionMov() {
 String instruction = "mov i1 i2";
 Main.processInstruction(instruction);
 assertEquals(0, Variable.getVariableValue("i1"));
 assertEquals(0, Variable.getVariableValue("i2"));
 Variable.setVariableValue("i1", 5);
 Main.processInstruction(instruction);
 assertEquals(5, Variable.getVariableValue("i1"));
 assertEquals(5, Variable.getVariableValue("i2"));
 }

 @Test
 public void testProcessInstructionDel() {
 String instruction = "del i0";
 Variable.setVariableValue("i0", 5);
 Main.processInstruction(instruction);
 assertNull(Variable.getVariableValue("i0"));
 }

 @Test
 public void testProcessInstructionArr() {
 String instruction = "arr i0 3";
 Main.processInstruction(instruction);
 assertNotNull(Variable.getVariableValue("i0[0]"));
 assertNull(Variable.getVariableValue("i0[1]"));
 assertNull(Variable.getVariableValue("i0[2]"));
 }

 @Test
 public void testProcessInstructionVar() {
 String instruction = "var i3";
 Main.processInstruction(instruction);
 assertNotNull(Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionIncrement() {
 String instruction = "inc i0";
 Variable.setVariableValue("i0", 5);
 Main.processInstruction(instruction);
 assertEquals(6, Variable.getVariableValue("i0"));
 }

 @Test
 public void testProcessInstructionDecrement() {
 String instruction = "dec i0";
 Variable.setVariableValue("i0", 5);
 Main.processInstruction(instruction);
 assertEquals(4, Variable.getVariableValue("i0"));
 }

 @Test
 public void testProcessInstructionAddition() {
 String instruction = "add i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(8, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionSubtraction() {
 String instruction = "sub i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(2, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionMultiplication() {
 String instruction = "mul i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(15, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionDivision() {
 String instruction = "div i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 2);
 Main.processInstruction(instruction);
 assertEquals(2, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionModulus() {
 String instruction = "mod i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(2, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionXOR() {
 String instruction = "xor i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(2, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionOR() {
 String instruction = "or i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(6, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionAND() {
 String instruction = "and i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(1, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionComparison() {
 String instruction = "cmp i1 i2 i3";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(1, Variable.getVariableValue("i3"));
 }

 @Test
 public void testProcessInstructionJump() {
 String instruction = "jmp i0";
 Variable.setVariableValue("i0", 5);
 Main.processInstruction(instruction);
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfZero() {
 String instruction = "jnz i0 i1";
 Variable.setVariableValue("i0", 0);
 Main.processInstruction(instruction);
 assertEquals(1, Main.iPtr);
 Variable.setVariableValue("i0", 5);
 Main.processInstruction(instruction);
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfAbove() {
 String instruction = "ja i1 i2";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(5, Main.iPtr);
 Variable.setVariableValue("i1", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfAboveOrEqual() {
 String instruction = "jae i1 i2";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(5, Main.iPtr);
 Variable.setVariableValue("i1", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfBelow() {
 String instruction = "jb i1 i2";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 Variable.setVariableValue("i1", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfBelowOrEqual() {
 String instruction = "jbe i1 i2";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 Variable.setVariableValue("i1", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfEqual() {
 String instruction = "je i1 i2";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 5);
 Main.processInstruction(instruction);
 assertEquals(5, Main.iPtr);
 Variable.setVariableValue("i1", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpIfNotEqual() {
 String instruction = "jne i1 i2";
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 5);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 Variable.setVariableValue("i1", 5);
 Variable.setVariableValue("i2", 3);
 Main.processInstruction(instruction);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJumpToLabel() {
 String instruction = "jmp label1";
 Main.processInstruction("label1: i1 mov i2 5");
 Main.processInstruction("label1: i1 inc");
 Variable.setVariableValue("i2", 6);
 Main.processInstruction("label1: i1 jmp label2");
 Main.processInstruction("label2: i1 halt");
 Main.processInstruction("label2: i1 jmp label1");
 Main.processInstruction("label1: i1 jmp label1");
 Main.processInstruction("label0:");
 Main.main(new String[] { TEST_FILE });
 }
}
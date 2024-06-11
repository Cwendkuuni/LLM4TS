package jipa.test;

import org.junit.Test;
import static org.junit.Assert.*;
import jipa.Main;

public class MainTest {

 private static final String[] VALID_INSTRUCTIONS = {
 "mov A 0",
 "add A B",
 "out A",
 "jmp Label1"
 };

 private static final String[] INVALID_INSTRUCTIONS = {
 "mov A 0 B",
 "add A B C",
 "out X",
 "jmp LabelX"
 };

 @Test
 public void testLoadInstructions() {
 String fileName = "testInstructions.txt";
 Main.loadInstructions(fileName);
 assertEquals(VALID_INSTRUCTIONS.length, Main.instruction.length);
 for (int i = 0; i < VALID_INSTRUCTIONS.length; ++i) {
 assertEquals(VALID_INSTRUCTIONS[i], Main.instruction[i]);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFileName() {
 Main.loadInstructions("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidInstruction() {
 String fileName = "testInstructions.txt";
 Main.loadInstructions(fileName);
 Main.instruction[0] = "mov A 0 B";
 try {
 Main.processInstruction(Main.instruction[0]);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testProcessInstructionMov() {
 Main.processInstruction("mov A 0");
 assertEquals(0, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionOut() {
 Main.processInstruction("out A");
 assertEquals("0", System.out.getHistory(0));
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.iPtr = 0;
 Main.processInstruction("jmp Label1");
 assertEquals(VALID_INSTRUCTIONS[1].length(), Main.instruction.length);
 assertEquals("Label1", Main.instruction[Main.iPtr]);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testProcessInstructionJmpOutOfBound() {
 Main.iPtr = VALID_INSTRUCTIONS.length;
 Main.processInstruction("jmp Label1");
 fail("Expected ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testProcessInstructionJz() {
 Main.processInstruction("jz Label1");
 Main.processInstruction("mov A 1");
 Main.processInstruction("mov B 0");
 Main.processInstruction("jz Label2");
 Main.processInstruction("mov B 1");
 Main.processInstruction("jz Label1");
 Main.processInstruction("mov B 2");
 Main.processInstruction("jmp Label2");
 assertEquals(2, Variable.getVariableValue("B"));
 }

 @Test
 public void testProcessInstructionJne() {
 Main.processInstruction("jne Label1");
 Main.processInstruction("mov A 1");
 Main.processInstruction("mov B 1");
 Main.processInstruction("jne Label2");
 Main.processInstruction("mov A 2");
 Main.processInstruction("mov B 1");
 Main.processInstruction("jne Label1");
 Main.processInstruction("mov B 2");
 Main.processInstruction("jmp Label2");
 assertEquals(2, Variable.getVariableValue("B"));
 }

 @Test
 public void testProcessInstructionJmpWithInvalidLabel() {
 Main.processInstruction("jmp LabelX");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testProcessInstructionWithInvalidInstruction() {
 Main.processInstruction("mov A 0 B");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testLoadInstructionsWithInvalidInstructions() {
 String fileName = "testInstructionsInvalid.txt";
 Main.loadInstructions(fileName);
 assertEquals(INVALID_INSTRUCTIONS.length, Main.instruction.length);
 for (int i = 0; i < INVALID_INSTRUCTIONS.length; ++i) {
 assertEquals(INVALID_INSTRUCTIONS[i], Main.instruction[i]);
 }
 }

}
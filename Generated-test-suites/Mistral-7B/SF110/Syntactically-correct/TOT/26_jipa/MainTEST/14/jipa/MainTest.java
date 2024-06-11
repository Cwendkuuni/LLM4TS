package jipa;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {

 private static final String VALID_FILE_NAME = "testFile.txt";
 private static final String INVALID_FILE_NAME = "nonExistentFile.txt";
 private static final String EMPTY_FILE_NAME = "";
 private static final String Q_COMMAND = "q";

 @BeforeClass
 public static void setup() {
 // Initialize static variables for testing
 Main.TOTAL_INSTRUCTIONS = 0;
 Main.instruction = new String[0];
 Main.iPtr = 0;
 }

 @Test
 public void testLoadInstructionsWithValidFileName() {
 String fileName = VALID_FILE_NAME;
 boolean result = Main.loadInstructions(fileName);
 assertEquals(true, result);
 }

 @Test
 public void testLoadInstructionsWithEmptyFileName() {
 String fileName = EMPTY_FILE_NAME;
 boolean result = Main.loadInstructions(fileName);
 assertEquals(false, result);
 }

 @Test
 public void testLoadInstructionsWithInvalidFileName() {
 String fileName = INVALID_FILE_NAME;
 boolean result = Main.loadInstructions(fileName);
 assertEquals(false, result);
 }

 @Test
 public void testLoadInstructionsWithQCommand() {
 String fileName = Q_COMMAND;
 boolean result = Main.loadInstructions(fileName);
 assertEquals(false, result);
 }

 @Test
 public void testProcessInstructionMov() {
 String[] instruction = { "mov A 0" };
 Main.processInstruction(instruction[0]);
 assertEquals(0, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionInc() {
 String[] instruction = { "inc A" };
 Main.processInstruction(instruction[0]);
 assertEquals(1, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionAdd() {
 String[] instruction = { "add A 5" };
 Main.processInstruction(instruction[0]);
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionJmp() {
 String[] instruction = { "jmp Label1" };
 Main.iPtr = 0;
 Main.processInstruction(instruction[0]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJnz() {
 String[] instruction = { "jnz A 1" };
 Variable.setVariableValue("A", 1);
 Main.iPtr = 0;
 Main.processInstruction(instruction[0]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJz() {
 String[] instruction = { "jz A 1" };
 Variable.setVariableValue("A", 0);
 Main.iPtr = 0;
 Main.processInstruction(instruction[0]);
 assertEquals(0, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJne() {
 String[] instruction = { "jne A B" };
 Variable.setVariableValue("A", 1);
 Variable.setVariableValue("B", 1);
 Main.iPtr = 0;
 Main.processInstruction(instruction[0]);
 assertEquals(0, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJneWithDifferentValues() {
 String[] instruction = { "jne A B" };
 Variable.setVariableValue("A", 1);
 Variable.setVariableValue("B", 0);
 Main.iPtr = 0;
 Main.processInstruction(instruction[0]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstructionDel() {
 String[] instruction = { "del A" };
 Main.processInstruction(instruction[0]);
 assertEquals(0, Variable.getVariableValueNames().size());
 }

 @Test
 public void testProcessInstructionSet() {
 String[] instruction = { "set A 5" };
 Main.processInstruction(instruction[0]);
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionOut() {
 String[] instruction = { "out A" };
 Main.processInstruction(instruction[0]);
 assertTrue(System.out.toString().contains("5"));
 }

 @Test
 public void testProcessInstructionIn() {
 String[] instruction = { "in A" };
 Main.processInstruction(instruction[0]);
 assertEquals("", Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionArray() {
 String[] instruction = { "arr A 5" };
 Main.processInstruction(instruction[0]);
 assertEquals(5, Variable.getVariableValueNames().size());
 }

 @Test
 public void testProcessInstructionVar() {
 String[] instruction = { "var A" };
 Main.processInstruction(instruction[0]);
 assertEquals(1, Variable.getVariableValueNames().size());
 }

 @Test(expected = AssertionError.class)
 public void testProcessInstructionInvalidOpcode() {
 String[] instruction = { "inv A" };
 Main.processInstruction(instruction[0]);
 fail("An AssertionError should have been thrown but was not.");
 }
}
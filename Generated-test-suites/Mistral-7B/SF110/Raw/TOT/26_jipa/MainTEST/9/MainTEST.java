package jipa;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

 private static final String VALID_FILE_NAME = "testInstructions.txt";
 private static final String EMPTY_FILE_NAME = "";
 private static final String NON_EXISTENT_FILE_NAME = "nonExistentFile.txt";
 private static final String INVALID_FILE_NAME = "invalidFileName.java";
 private static final String[] VALID_INSTRUCTIONS = { "out 5", "mov x 0", "inc x", "jmp label1" };
 private static final String INVALID_INSTRUCTION = "invalidOpcode x 5";
 private static final String[] EMPTY_INSTRUCTIONS = {};
 private static final String[] MISSING_SEMICOLON_INSTRUCTIONS = { "mov x 5", "inc x 5" };

 @Test
 public void testLoadInstructionsValidFileName() {
 Main.loadInstructions(VALID_FILE_NAME);
 Assert.assertEquals(VALID_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsEmptyFileName() {
 Main.loadInstructions(EMPTY_FILE_NAME);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadInstructionsNonExistentFileName() {
 Main.loadInstructions(NON_EXISTENT_FILE_NAME);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFileName() {
 Main.loadInstructions(INVALID_FILE_NAME);
 }

 @Test
 public void testLoadInstructionsValidInstructions() {
 Main.loadInstructions(VALID_FILE_NAME);
 for (int i = 0; i < VALID_INSTRUCTIONS.length; ++i) {
 Main.processInstruction(Main.instruction[i]);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstructionInvalidOpcode() {
 Main.processInstruction(INVALID_INSTRUCTION);
 }

 @Test
 public void testProcessInstructionValidInstructions() {
 Main.loadInstructions(VALID_FILE_NAME);
 for (int i = 0; i < VALID_INSTRUCTIONS.length; ++i) {
 Main.processInstruction(Main.instruction[i]);
 }
 Assert.assertEquals(0, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstructionMissingSemicolonInstructions() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.instruction = MISSING_SEMICOLON_INSTRUCTIONS;
 for (int i = 0; i < MISSING_SEMICOLON_INSTRUCTIONS.length; ++i) {
 Main.processInstruction(Main.instruction[i]);
 }
 Assert.assertFalse("Expected an exception, but none was thrown", Main.instances().hasNextException());
 }

 @Test
 public void testProcessInstructionMissingSemicolonInstructionsWithException() {
 Main.instruction = MISSING_SEMICOLON_INSTRUCTIONS;
 Main.loadInstructions("");
 for (int i = 0; i < MISSING_SEMICOLON_INSTRUCTIONS.length; ++i) {
 Main.processInstruction(Main.instruction[i]);
 }
 Assert.assertEquals("Missing semicolon at end of instruction", Main.instances().nextException().getMessage());
 }
}
package jipa;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

 private static final String VALID_FILE_PATH = "testData/validInstructions.txt";
 private static final String INVALID_FILE_PATH = "testData/invalidFile.txt";
 private static final String EMPTY_FILE_PATH = "";

 @Test
 public void testLoadInstructionsWithValidFile() {
 int expectedTotalInstructions = 10;
 String[] expectedInstructions = new String[expectedTotalInstructions];
 String[] expectedArgs;

 Main.loadInstructions(VALID_FILE_PATH);

 Assert.assertEquals(expectedTotalInstructions, Main.TOTAL_INSTRUCTIONS);
 for (int i = 0; i < expectedTotalInstructions; ++i) {
 expectedInstructions[i] = "validInstruction_" + i;
 }

 Assert.assertArrayEquals("Instructions not loaded correctly", expectedInstructions, Main.instruction);
 }

 @Test(expected = Exception.class)
 public void testLoadInstructionsWithInvalidFile() {
 Main.loadInstructions(INVALID_FILE_PATH);
 }

 @Test
 public void testLoadInstructionsWithEmptyFile() {
 Main.loadInstructions(EMPTY_FILE_PATH);

 Assert.assertEquals(0, Main.TOTAL_INSTRUCTIONS);
 Assert.assertNull(Main.instruction);
 }

 @Test
 public void testProcessInstruction_set() {
 String variableName = "testVariable";
 int expectedValue = 42;
 Main.Variable.setVariableValue(variableName, 0);

 Main.processInstruction("set testVariable 42");

 Assert.assertEquals(expectedValue, Main.Variable.getVariableValue(variableName));
 }

 @Test
 public void testProcessInstruction_mov() {
 String sourceVariableName = "sourceVariable";
 String destinationVariableName = "destinationVariable";
 int expectedValue = 13;
 Main.Variable.setVariableValue(sourceVariableName, expectedValue);

 Main.processInstruction("mov destinationVariable " + sourceVariableName);

 Assert.assertEquals(expectedValue, Main.Variable.getVariableValue(destinationVariableName));
 }

 @Test
 public void testProcessInstruction_inc() {
 String variableName = "testVariable";
 int expectedValue = 2;
 Main.Variable.setVariableValue(variableName, 1);

 Main.processInstruction("inc testVariable");

 Assert.assertEquals(expectedValue, Main.Variable.getVariableValue(variableName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstruction_invalidOpcode() {
 Main.processInstruction("invalidOpcode testVariable");
 }

 @Test
 public void testProcessInstruction_jmp() {
 int expectedJumpIndex = 5;
 Main.iPtr = 0;

 Main.processInstruction("jmp 5");

 Assert.assertEquals(expectedJumpIndex, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jnz() {
 int expectedJumpIndex = 10;
 int expectedValue = 1;
 Main.Variable.setVariableValue("testVariable", expectedValue);
 Main.iPtr = 0;

 Main.processInstruction("jnz testVariable 10");

 Assert.assertEquals(expectedJumpIndex, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jz() {
 int expectedJumpIndex = 15;
 int expectedValue = 0;
 Main.Variable.setVariableValue("testVariable", expectedValue);
 Main.iPtr = 0;

 Main.processInstruction("jz testVariable 15");

 Assert.assertEquals(expectedJumpIndex, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jmpNegativeIndex() {
 int expectedJumpIndex = Main.TOTAL_INSTRUCTIONS + 5;
 Main.iPtr = 0;

 Main.processInstruction("jmp -5");

 Assert.assertEquals(expectedJumpIndex, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jmpOutOfRangeIndex() {
 int expectedJumpIndex = Main.TOTAL_INSTRUCTIONS + 10;
 Main.iPtr = 0;

 Main.processInstruction("jmp 100");

 Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, Main.iPtr);
 }
}
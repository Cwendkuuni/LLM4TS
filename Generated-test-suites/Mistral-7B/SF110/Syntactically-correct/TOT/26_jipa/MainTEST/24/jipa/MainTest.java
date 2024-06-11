package jipa;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

 private static final String[] INPUT_FILE_NAME = {"testData.txt"};
 private static final String[] VALID_INSTRUCTIONS = {
 "mov r1 r0",
 "inc r1",
 "dec r1",
 "mov r2 r1",
 "add r1 r2",
 "sub r1 r2",
 "mov r2 r3",
 "jz r1 r4"
 };
 private static final String[] INVALID_INSTRUCTIONS = {
 "mov r1 r1", // duplicate destination register
 "mov r1 r10", // invalid destination register
 "mov r1 r-1", // negative source register index
 "mov r1 r1 r2", // more than two operands
 "mov r1 r1 opcode", // missing destination register
 "mov r1 opcode r2", // missing source register
 "mov r1 r1 opcode opcode" // more than one opcode
 };

 @Test
 public void testLoadInstructions() {
 Main.loadInstructions(INPUT_FILE_NAME[0]);
 Assert.assertEquals(VALID_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
 }

 @Test(expected = RuntimeException.class)
 public void testLoadInstructionsInvalidFile() {
 Main.loadInstructions("");
 }

 @Test(expected = RuntimeException.class)
 public void testLoadInstructionsInvalidInstruction() {
 Main.loadInstructions("testDataInvalid.txt");
 }

 @Test
 public void testProcessInstructionMov() {
 Main.iPtr = 0;
 Main.processInstruction(VALID_INSTRUCTIONS[0]);
 Assert.assertEquals(0, Main.Variable.getVariableValue("r0"));
 Assert.assertEquals(0, Main.Variable.getVariableValue("r1"));
 Main.processInstruction(VALID_INSTRUCTIONS[0]);
 Assert.assertEquals(0, Main.Variable.getVariableValue("r0"));
 Assert.assertEquals(1, Main.Variable.getVariableValue("r1"));
 }

 @Test
 public void testProcessInstructionInc() {
 Main.iPtr = 0;
 Main.processInstruction(VALID_INSTRUCTIONS[1]);
 Assert.assertEquals(0, Main.Variable.getVariableValue("r1"));
 Main.processInstruction(VALID_INSTRUCTIONS[1]);
 Assert.assertEquals(1, Main.Variable.getVariableValue("r1"));
 }

 @Test
 public void testProcessInstructionDec() {
 Main.iPtr = 0;
 Main.processInstruction(VALID_INSTRUCTIONS[2]);
 Assert.assertEquals(1, Main.Variable.getVariableValue("r1"));
 Main.processInstruction(VALID_INSTRUCTIONS[2]);
 Assert.assertEquals(0, Main.Variable.getVariableValue("r1"));
 }

 @Test
 public void testProcessInstructionAdd() {
 Main.iPtr = 0;
 Main.processInstruction(VALID_INSTRUCTIONS[3]);
 Main.processInstruction(VALID_INSTRUCTIONS[4]);
 Assert.assertEquals(1, Main.Variable.getVariableValue("r1"));
 Assert.assertEquals(2, Main.Variable.getVariableValue("r2"));
 Main.processInstruction(VALID_INSTRUCTIONS[5]);
 Assert.assertEquals(3, Main.Variable.getVariableValue("r1"));
 }

 @Test
 public void testProcessInstructionSub() {
 Main.iPtr = 0;
 Main.processInstruction(VALID_INSTRUCTIONS[3]);
 Main.processInstruction(VALID_INSTRUCTIONS[6]);
 Assert.assertEquals(1, Main.Variable.getVariableValue("r1"));
 Assert.assertEquals(2, Main.Variable.getVariableValue("r2"));
 Main.processInstruction(VALID_INSTRUCTIONS[5]);
 Assert.assertEquals(0, Main.Variable.getVariableValue("r1"));
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.iPtr = 0;
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 1]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 2]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 3]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 4]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 5]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 6]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 7]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 8]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 9]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 10]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 11]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 12]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 13]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 14]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 15]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 16]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 17]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 18]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 19]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 20]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 21]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 22]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 23]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 24]);
 Main.processInstruction("jmp r2");
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 25]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 26]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 27]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 28]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 29]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 30]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 31]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 32]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 33]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 34]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 35]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 36]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 37]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 38]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 39]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 40]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 41]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 42]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 43]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 44]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 45]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 46]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 47]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 48]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 49]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 50]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 51]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 52]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 53]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 54]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 55]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 56]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 57]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 58]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 59]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 60]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 61]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 62]);
 Main.processInstruction(VALID_INSTRUCTIONS[VALID_INSTRUCTIONS.length - 63]);
 Main.processInstruction("jmp r1");
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionInvalidInstruction() {
 Main.iPtr = 0;
 Main.processInstruction(INVALID_INSTRUCTIONS[0]);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionInvalidInstruction2() {
 Main.iPtr = 0;
 Main.processInstruction(INVALID_INSTRUCTIONS[1]);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionInvalidInstruction3() {
 Main.iPtr = 0;
 Main.processInstruction(INVALID_INSTRUCTIONS[2]);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionInvalidInstruction4() {
 Main.iPtr = 0;
 Main.processInstruction(INVALID_INSTRUCTIONS[3]);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionInvalidInstruction5() {
 Main.iPtr = 0;
 Main.processInstruction(INVALID_INSTRUCTIONS[4]);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionInvalidInstruction6() {
 Main.iPtr = 0;
 Main.processInstruction(INVALID_INSTRUCTIONS[5]);
 }

 @Test
 public void testClearInstructions() {
 Main.resetInstructions();
 }
}
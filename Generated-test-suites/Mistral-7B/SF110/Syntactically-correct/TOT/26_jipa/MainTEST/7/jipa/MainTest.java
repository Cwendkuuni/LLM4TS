package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_NAME = "testFile.txt";
 private static final String INVALID_FILE_NAME = "";
 private static final String INVALID_INSTRUCTION_SET = "InvalidInstructionSet";
 private static final String[] VALID_INSTRUCTIONS = { "out 5", "mov a 0", "inc a" };
 private static final String[] INVALID_INSTRUCTIONS = { "mov a 5", "dec a", "set x 100" };
 private static final int[] VALID_ARGUMENTS = { 5, 0, 1 };
 private static final int[] INVALID_ARGUMENTS = { -1, 5, "a" };
 private static final int VALID_VALUE = 5;
 private static final int INVALID_VALUE = -1;
 private static final String VALID_STRING = "Hello World";
 private static final String INVALID_STRING = "";

 @Before
 public void setup() {
 Main.TOTAL_INSTRUCTIONS = 0;
 Main.instruction = new String[0];
 Main.iPtr = 0;
 }

 @Test
 public void testLoadInstructionsWithValidFileName() {
 Main.loadInstructions(VALID_FILE_NAME);
 assertEquals(VALID_INSTRUCTIONS.length, Main.instruction.length);
 for (int i = 0; i < VALID_INSTRUCTIONS.length; ++i) {
 assertEquals(VALID_INSTRUCTIONS[i], Main.instruction[i]);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsWithEmptyFileName() {
 Main.loadInstructions("");
 fail("LoadInstructions method should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsWithInvalidFileName() {
 Main.loadInstructions(INVALID_FILE_NAME);
 fail("LoadInstructions method should have thrown an IllegalArgumentException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsWithInvalidInstructionSet() {
 Main.loadInstructions(INVALID_INSTRUCTION_SET);
 fail("LoadInstructions method should have thrown an IllegalArgumentException.");
 }

 @Test
 public void testProcessInstructionWithOutOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(5, System.out.size());
 assertEquals("5\n", System.out.get(0));
 }

 @Test
 public void testProcessInstructionWithMovOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.processInstruction(Main.instruction[1]);
 assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithInOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 String input = "5";
 System.setIn(new ByteArrayInputStream(input.getBytes()));
 Main.processInstruction(Main.instruction[2]);
 assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstructionWithInvalidOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.processInstruction("invalidOpcode");
 fail("ProcessInstruction method should have thrown an IllegalArgumentException.");
 }

 @Test
 public void testProcessInstructionWithAddOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.processInstruction(Main.instruction[3]);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", 2);
 Main.processInstruction(Main.instruction[4]);
 assertEquals(VALID_VALUE + 2, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithSubOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE + 2);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[5]);
 assertEquals(VALID_VALUE, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithMulOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", 2);
 Main.processInstruction(Main.instruction[6]);
 assertEquals(VALID_VALUE * 2, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithDivOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE * 2);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[7]);
 assertEquals(VALID_VALUE, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithModOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE * 2);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[8]);
 assertEquals(VALID_VALUE % VALID_VALUE, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithXorOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[9]);
 assertEquals(VALID_VALUE ^ VALID_VALUE, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithOrOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[10]);
 assertEquals(VALID_VALUE | VALID_VALUE, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithAndOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[11]);
 assertEquals(VALID_VALUE & VALID_VALUE, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithJmpOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.processInstruction(Main.instruction[12]);
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJnzOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", 0);
 Main.processInstruction(Main.instruction[13]);
 assertEquals(4, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJzOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Main.processInstruction(Main.instruction[14]);
 assertEquals(6, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJeOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[15]);
 assertEquals(7, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJneOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", INVALID_VALUE);
 Main.processInstruction(Main.instruction[16]);
 assertEquals(8, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithCmpOpcode() {
 Main.loadInstructions(VALID_FILE_NAME);
 Variable.setVariableValue("a", VALID_VALUE);
 Variable.setVariableValue("b", VALID_VALUE);
 Main.processInstruction(Main.instruction[17]);
 assertEquals(0, Variable.getVariableValue("c"));
 Main.processInstruction(Main.instruction[18]);
 assertEquals(1, Variable.getVariableValue("c"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstructionWithInvalidJumpValue() {
 Main.loadInstructions(VALID_FILE_NAME);
 Main.processInstruction(Main.instruction[12]);
 Main.processInstruction(Main.instruction[19]);
 fail("ProcessInstruction method should have thrown an IllegalArgumentException.");
 }
}
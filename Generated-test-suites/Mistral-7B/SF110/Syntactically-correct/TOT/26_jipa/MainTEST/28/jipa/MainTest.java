package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_NAME = "validInstructions.txt";
 private static final String EMPTY_FILE_NAME = "emptyFile.txt";
 private static final String INVALID_FILE_NAME = "invalidFile.txt";
 private static final String[] VALID_INSTRUCTIONS = { "mov a 0", "inc a", "out a" };
 private static final String[] EMPTY_INSTRUCTIONS = { };
 private static final String[] INVALID_INSTRUCTIONS = { "mov a 0 a", "jmp z label" }; // non-existent label
 private static final String[] VARIABLES = { "a" };
 private Main underTest;

 @Before
 public void setUp() {
 underTest = new Main();
 }

 @Test
 public void testLoadInstructionsWithValidFileName() {
 underTest.loadInstructions(VALID_FILE_NAME);
 assertEquals(VALID_INSTRUCTIONS.length, underTest.iPtr);
 }

 @Test
 public void testLoadInstructionsWithEmptyFileName() {
 underTest.loadInstructions("");
 underTest.loadInstructions(EMPTY_FILE_NAME);
 assertEquals(0, underTest.iPtr);
 }

 @Test
 public void testLoadInstructionsWithInvalidFileName() {
 underTest.loadInstructions(INVALID_FILE_NAME);
 assertFalse(underTest.instruction[0].isEmpty()); // check if the instruction array is not empty
 }

 @Test
 public void testProcessInstructionWithValidInstruction() {
 underTest.loadInstructions(VALID_FILE_NAME);
 underTest.processInstruction(VALID_INSTRUCTIONS[0]);
 assertEquals(0, underTest.Variable.getVariableValue("a"));
 underTest.processInstruction(VALID_INSTRUCTIONS[1]);
 assertEquals(1, underTest.Variable.getVariableValue("a"));
 underTest.processInstruction(VALID_INSTRUCTIONS[2]);
 assertEquals("1\n", System.out.captureOutput().getErrors().toString());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testProcessInstructionWithInvalidInstructionIndex() {
 underTest.loadInstructions(VALID_FILE_NAME);
 underTest.processInstruction(Integer.toString(underTest.TOTAL_INSTRUCTIONS + 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstructionWithInvalidInstruction() {
 underTest.loadInstructions(INVALID_INSTRUCTIONS);
 underTest.processInstruction(INVALID_INSTRUCTIONS[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstructionWithNonExistentLabel() {
 underTest.loadInstructions(VALID_FILE_NAME);
 underTest.processInstruction("jmp label2");
 }
}
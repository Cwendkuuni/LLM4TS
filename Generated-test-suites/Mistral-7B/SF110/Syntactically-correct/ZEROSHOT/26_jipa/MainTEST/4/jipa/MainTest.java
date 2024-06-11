package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_PATH = "testFile.txt";
 private static final String INVALID_FILE_PATH = "nonexistentFile.txt";
 private static final String EMPTY_FILE_PATH = "";
 private static final String Q_COMMAND = "q";
 private static final String[] VALID_INSTRUCTIONS = new String[] {
 "mov a 0",
 "inc a",
 "out a",
 "jmp label1",
 "mov b a",
 "dec b",
 "jz label2"
 };
 private static final String[] INVALID_INSTRUCTIONS = new String[] {
 "mov a a", // same variable name
 "mov a b a", // multiple variables on the left side
 "mov a 10 a", // multiple variables on the right side
 "mov a 10 z", // unknown variable name
 "mov a 10 out", // invalid opcode
 "mov a 10 label1", // label before instruction
 "jmp label2 label1" // multiple labels in one instruction
 };
 private Variable variable;
 private Main main;

 @Before
 public void setUp() {
 variable = new Variable();
 main = new Main();
 }

 @Test
 public void testLoadInstructionsWithValidFilePath() {
 main.loadInstructions(VALID_FILE_PATH);
 assertEquals(VALID_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
 for (int i = 0; i < VALID_INSTRUCTIONS.length; i++) {
 assertEquals(VALID_INSTRUCTIONS[i], Main.instruction[i]);
 }
 }

 @Test(expected = RuntimeException.class)
 public void testLoadInstructionsWithInvalidFilePath() {
 main.loadInstructions(INVALID_FILE_PATH);
 }

 @Test
 public void testLoadInstructionsWithEmptyFilePath() {
 main.loadInstructions(EMPTY_FILE_PATH);
 assertEquals(0, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testProcessInstructionWithHaltsTheProgram() {
 main.loadInstructions(VALID_FILE_PATH);
 main.processInstruction(Main.instruction[0]);
 assertEquals(0, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithOutInstruction() {
 variable.setVariableValue("a", 42);
 main.loadInstructions(VALID_FILE_PATH);
 main.processInstruction(Main.instruction[3]);
 assertEquals("42", System.out.getHistory(0));
 }

 @Test
 public void testProcessInstructionWithInInstruction() {
 variable.setVariableValue("a", 42);
 main.loadInstructions(VALID_FILE_PATH);
 assertEquals(42, main.readValue());
 main.processInstruction(Main.instruction[5]);
 assertEquals(0, main.readValue());
 }

 @Test
 public void testProcessInstructionWithMovInstruction() {
 variable.setVariableValue("a", 42);
 variable.setVariableValue("b", 0);
 main.loadInstructions(VALID_FILE_PATH);
 main.processInstruction(Main.instruction[0]);
 main.processInstruction(Main.instruction[1]);
 main.processInstruction(Main.instruction[2]);
 assertEquals(42, variable.getVariableValue("a"));
 assertEquals(42, variable.getVariableValue("b"));
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionWithInvalidInstruction() {
 variable.setVariableValue("a", 42);
 main.loadInstructions(VALID_FILE_PATH);
 for (String instruction : INVALID_INSTRUCTIONS) {
 main.processInstruction(instruction);
 }
 }

 @Test
 public void testProcessInstructionWithLabelJumpInstruction() {
 main.loadInstructions(VALID_FILE_PATH);
 main.processInstruction(Main.instruction[6]);
 assertEquals(11, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithConditionalJumpInstruction() {
 main.loadInstructions(VALID_FILE_PATH);
 variable.setVariableValue("a", 0);
 main.processInstruction(Main.instruction[10]);
 assertEquals(12, Main.iPtr);
 variable.setVariableValue("a", 1);
 main.processInstruction(Main.instruction[10]);
 assertEquals(15, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithCompareInstruction() {
 main.loadInstructions(VALID_FILE_PATH);
 variable.setVariableValue("a", 1);
 variable.setVariableValue("b", 1);
 main.processInstruction(Main.instruction[17]);
 assertEquals(1, variable.getVariableValue("c"));
 variable.setVariableValue("a", 2);
 variable.setVariableValue("b", 1);
 main.processInstruction(Main.instruction[17]);
 assertEquals(0, variable.getVariableValue("c"));
 }
}
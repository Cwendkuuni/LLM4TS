package jipa;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

 private static final String VALID_FILE_NAME = "testFile.txt";
 private static final String INVALID_FILE_NAME = "";
 private static final String VALID_INSTRUCTION_SET = "mov A 0\ninc A\nout A\n";
 private static final String INVALID_INSTRUCTION_SET = "mov A 0\ninc A\nout X\n"; // X is not defined

 @Test
 public void testLoadInstructionsValidFile() {
 Main.loadInstructions(VALID_FILE_NAME);
 Assert.assertEquals(VALID_INSTRUCTION_SET.split("\\r?\\n"), Main.instruction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsEmptyFileName() {
 Main.loadInstructions("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFileName() {
 Main.loadInstructions(INVALID_FILE_NAME);
 }

 @Test
 public void testProcessInstructionMov() {
 Main.instruction = new String[]{"mov A 0"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals(0, Main.Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionInc() {
 Main.instruction = new String[]{"mov A 0", "inc A"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals(1, Main.Variable.getVariableValue("A"));
 }

 @Test(expected = NumberFormatException.class)
 public void testProcessInstructionInvalidInstruction_1() {
 Main.instruction = new String[]{"mov A 0", "mov X 0"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test(expected = NumberFormatException.class)
 public void testProcessInstructionInvalidInstruction_2() {
 Main.instruction = new String[]{"mov A 0", "mov A X"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstructionOut() {
 Main.instruction = new String[]{"mov A 0", "out A"};
 Main.iPtr = 0;
 System.out.flush();
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals("0", System.console().readLine());
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.instruction = new String[]{"mov A 0", "mov B 0", "jmp B", "mov C 1", "mov B 0", "mov D 2", "mov B C"};
 Main.iPtr = 0;
 for (int i = 0; i < 10; i++) {
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }
 Assert.assertEquals(2, Main.Variable.getVariableValue("D"));
 }
}
package jipa;

import org.junit.Assert;
import org.junit.Test;

public class MainTest {

 private static final String[] VALID_INSTRUCTION_FILES = {"instructions1.txt", "instructions2. MainTest.java:11: error: variable iPtr might not have been initialized
        while (true) {", "instructions3.txt"};
 private static final String[] INVALID_INSTRUCTION_FILES = {"instructions4.txt", "instructions5. MainTest.java:21: error: unreachable code: while (true) {", "instructions6.txt"};

 @Test
 public void testLoadInstructionsValidFile() {
 final String validFilePath = VALID_INSTRUCTION_FILES[0];
 Main.loadInstructions(validFilePath);
 Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsEmptyFile() {
 Main.loadInstructions("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFile() {
 Main.loadInstructions(INVALID_INSTRUCTION_FILES[0]);
 }

 @Test
 public void testProcessInstructionOut() {
 Main.TOTAL_INSTRUCTIONS = 1;
 Main.instruction[0] = "out 10";
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals("10", System.out.getHistogram().get(0));
 }

 @Test
 public void testProcessInstructionIn() {
 Main.TOTAL_INSTRUCTIONS = 1;
 Main.instruction[0] = "in a";
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.setVariableValue("a", 5);
 Main.iPtr++;
 Main.processInstruction("mov a 0");
 Main.iPtr++;
 Main.processInstruction("out a");
 String output = System.out.getHistogram().get(0);
 Assert.assertEquals("5", output);
 }

 @Test
 public void testProcessInstructionMov() {
 Main.TOTAL_INSTRUCTIONS = 2;
 Main.instruction[0] = "mov a b";
 Main.instruction[1] = "mov c a";
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.setVariableValue("b", 10);
 Main.setVariableValue("c", 10);
 Main.iPtr++;
 Main.processInstruction("mov a b");
 Main.iPtr++;
 Main.processInstruction("mov d a");
 Main.setVariableValue("d", 20);
 Main.iPtr++;
 Main.processInstruction("out d");
 String output = System.out.getHistogram().get(0);
 Assert.assertEquals("20", output);
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.TOTAL_INSTRUCTIONS = 7;
 Main.instruction[0] = "mov a 1";
 Main.instruction[1] = "mov b 2";
 Main.instruction[2] = "jmp 4";
 Main.instruction[3] = "out a";
 Main.instruction[4] = "mov a b";
 Main.instruction[5] = "out a";
 Main.instruction[6] = "mov b 3";
 Main.instruction[Main.TOTAL_INSTRUCTIONS - 1] = "mov a b";
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 String output = System.out.getHistogram().get(0);
 String expectedOutput = "2";
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstructionInvalidInstruction() {
 Main.TOTAL_INSTRUCTIONS = 1;
 Main.instruction[0] = "invalid";
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testMain() {
 Main.main(VALID_INSTRUCTION_FILES);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testMainInvalidInstructionFile() {
 Main.main(INVALID_INSTRUCTION_FILES[0]);
 }
}
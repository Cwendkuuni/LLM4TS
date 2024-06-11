package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String TEST_FILE = "testInstructions.txt";

 @Test
 public void testMain() throws Exception {
 String[] args = { TEST_FILE };
 Main.main(args);
 // Verify that the program has loaded and executed the instructions correctly
 assertEquals(10, Variable.getVariableValue("A"));
 assertEquals("Hello, World!", Variable.getVariableValue("output"));
 }

 @Test
 public void testLoadInstructionsEmptyFile() {
 Main.loadInstructions("");
 assertTrue(Main.instruction.length == 0);
 }

 @Test
 public void testLoadInstructionsInvalidFile() {
 Main.loadInstructions("invalidFile.txt");
 assertTrue(Main.instruction.length == 0);
 }

 @Test
 public void testLoadInstructionsInvalidInstruction() {
 String invalidInstruction = "invalidOpcode argument";
 Main.instruction = new String[] { invalidInstruction };
 Main.loadInstructions("test.txt");
 assertThrows(RuntimeException.class, () -> Main.processInstruction(invalidInstruction));
 }

 @Test
 public void testLoadInstructionsMissingInstruction() {
 String missingInstruction = "mov A 0";
 Main.instruction = new String[] { "mov A 1", "out message" };
 Main.loadInstructions("test.txt");
 assertThrows(RuntimeException.class, () -> Main.processInstruction(missingInstruction));
 }

 @Test
 public void testProcessInstructionOut() {
 Main.instruction = new String[] { "out message" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals("message\n", System.out.getHistory(0));
 }

 @Test
 public void testProcessInstructionIn() {
 Main.instruction = new String[] { "in A" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(5, System.in.read()); // Assume input is number 5 for testing
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionMov() {
 Main.instruction = new String[] { "mov A B", "mov B 10" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(10, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionSet() {
 Main.instruction = new String[] { "set A 5" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstructionDel() {
 Main.instruction = new String[] { "del A" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertTrue(Variable.containsVariable("A"));
 Main.processInstruction("del A");
 assertFalse(Variable.containsVariable("A"));
 }

 @Test
 public void testProcessInstructionArr() {
 Main.instruction = new String[] { "arr A 5", "mov B 3", "mov C A 0", "mov A B", "mov B A", "mov A C", "inc A", "mov D A", "mov A D", "inc A" };
 Main.iPtr = 0;
 for (int i = 0; i < 10; i++) {
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr++;
 }
 assertEquals(6, Variable.getVariableValue("A"));
 assertEquals(1, Variable.getVariableValue("A" + 1));
 assertEquals(2, Variable.getVariableValue("A" + 2));
 assertEquals(3, Variable.getVariableValue("A" + 3));
 assertEquals(4, Variable.getVariableValue("A" + 4));
 assertEquals(5, Variable.getVariableValue("A" + 5));
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.instruction = new String[] { "jmp testLabel" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.iPtr = 1;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 // Add testLabel instruction here
 assertEquals(10, Variable.getVariableValue("A"));
 }
}
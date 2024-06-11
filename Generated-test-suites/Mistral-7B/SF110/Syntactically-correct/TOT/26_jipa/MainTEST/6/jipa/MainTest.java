package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_NAME = "testFile.txt";
 private static final String INVALID_FILE_NAME = "does_not_exist.txt";
 private static final String EMPTY_FILE_NAME = "";
 private static final String QUIT_COMMAND = "q";

 @Test
 public void testLoadInstructionsWithValidFileName() {
 Main.loadInstructions(VALID_FILE_NAME);
 assertTrue(Main.TOTAL_INSTRUCTIONS > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsWithInvalidFileName() {
 Main.loadInstructions(INVALID_FILE_NAME);
 }

 @Test
 public void testLoadInstructionsWithEmptyFileName() {
 Main.loadInstructions(EMPTY_FILE_NAME);
 assertEquals(0, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructionsWithQuitCommand() {
 Main.loadInstructions(QUIT_COMMAND);
 }

 @Test
 public void testProcessInstructionWithHaltOpCode() {
 Main.instruction = new String[] { "halt" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(0, System.exitCode());
 }

 @Test
 public void testProcessInstructionWithOutOpCode() {
 Main.instruction = new String[] { "out", "Hello World!" };
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals("Hello World!", System.out.getHistory(0).getLine().trim());
 }

 @Test
 public void testProcessInstructionWithInOpCode() {
 Main.instruction = new String[] { "in", "input" };
 Main.Variable.setVariableValue("input", 42);
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(42, Main.Variable.getVariableValue("input"));
 }

 @Test
 public void testProcessInstructionWithMovOpCode() {
 Main.instruction = new String[] { "mov", "a", "b" };
 Main.Variable.setVariableValue("a", 42);
 Main.Variable.setVariableValue("b", 0);
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(42, Main.Variable.getVariableValue("b"));
 Main.Variable.setVariableValue("b", 0);
 }

 @Test
 public void testProcessInstructionWithAddOpCode() {
 Main.instruction = new String[] { "mov", "a", "5" };
 Main.instruction = new String[] { "add", "a", "b" };
 Main.Variable.setVariableValue("a", 42);
 Main.Variable.setVariableValue("b", 5);
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(47, Main.Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionWithJmpOpCode() {
 Main.instruction = new String[] { "mov", "a", "1" };
 Main.instruction = new String[] { "mov", "b", "2" };
 Main.instruction = new String[] { "jmp", " label1" };
 Main.instruction = new String[] { "label1", "mov", "c", "3" };
 Main.instruction = new String[] { "mov", "a", "1" };
 Main.instruction = new String[] { "jmp", " label2" };
 Main.instruction = new String[] { "label1", "mov", "c", "4" };
 Main.instruction = new String[] { "label2", "mov", "d", "5" };
 Main.Variable.setVariableValue("a", 0);
 Main.Variable.setVariableValue("b", 1);
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(4, Main.Variable.getVariableValue("c"));
 }

}
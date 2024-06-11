package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_NAME = "validInstructions.asm";
 private static final String INVALID_FILE_NAME = "invalidInstructions.asm";
 private static final String EMPTY_FILE_NAME = "";

 @Before
 public void setup() {
 resetInstructions();
 }

 @Test
 public void testLoadInstructionsValidFileName() {
 final boolean result = Main.loadInstructions(VALID_FILE_NAME);
 assertTrue(result);
 }

 @Test
 public void testLoadInstructionsEmptyFileName() {
 final boolean result = Main.loadInstructions(EMPTY_FILE_NAME);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFileName() {
 Main.loadInstructions(INVALID_FILE_NAME);
 }

 @Test
 public void testProcessInstructionHalt() {
 Main.instruction = new String[]{"halt"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(0, System.exitCode());
 }

 @Test
 public void testProcessInstructionOut() {
 Main.instruction = new String[]{"out", "test"};
 Main.iPtr = 0;
 System.out.flush();
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals("test", System.console().readLine());
 }

 @Test
 public void testProcessInstructionIn() {
 Main.instruction = new String[]{"in", "variableName"};
 Main.iPtr = 0;
 Main.Variable.setVariableValue("variableName", 42);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(42, Main.Variable.getVariableValue("variableName"));
 }

 @Test
 public void testProcessInstructionMov() {
 Main.instruction = new String[]{"mov", "variableName1", "variableName2"};
 Main.iPtr = 0;
 Main.Variable.setVariableValue("variableName1", 10);
 Main.Variable.setVariableValue("variableName2", 5);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(10, Main.Variable.getVariableValue("variableName2"));
 }

 @Test
 public void testProcessInstructionSet() {
 Main.instruction = new String[]{"set", "variableName", "5"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(5, Main.Variable.getVariableValue("variableName"));
 }

 @Test
 public void testProcessInstructionDel() {
 Main.instruction = new String[]{"del", "variableName"};
 Main.iPtr = 0;
 Main.Variable.setVariableValue("variableName", 5);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertNull(Main.Variable.getVariableValue("variableName"));
 }

 @Test
 public void testProcessInstructionArr() {
 Main.instruction = new String[]{"arr", "arrayName", "3"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(3, Main.Variable.getVariables().size());
 assertEquals("arrayName", Main.Variable.getVariableName(0));
 assertEquals("arrayName0", Main.Variable.getVariableName(1));
 assertEquals("arrayName1", Main.Variable.getVariableName(2));
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.instruction = new String[]{"jmp", "1"};
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJnz() {
 Main.instruction = new String[]{"jnz", "0", "1"};
 Main.iPtr = 0;
 Main.Variable.setVariableValue("0", 1);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(1, Main.iPtr);
 Main.Variable.setVariableValue("0", 0);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(0, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJz() {
 Main.instruction = new String[]{"jz", "0", "1"};
 Main.iPtr = 0;
 Main.Variable.setVariableValue("0", 0);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(1, Main.iPtr);
 Main.Variable.setVariableValue("0", 1);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 assertEquals(0, Main.iPtr);
 }
}
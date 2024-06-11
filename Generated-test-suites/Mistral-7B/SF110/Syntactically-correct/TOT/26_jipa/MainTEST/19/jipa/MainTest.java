package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_PATH = "validInstructions.txt";
 private static final String INVALID_FILE_PATH = "invalidInstructions.txt";
 private static final String QUIT_COMMAND = "q";

 @Before
 public void setUp() {
 resetInstructions();
 }

 @After
 public void tearDown() {
 System.exit(0);
 }

 @Test
 public void testLoadInstructionsWithValidFilePath() {
 String fileName = VALID_FILE_PATH;
 assertTrue(loadInstructions(fileName));
 assertEquals(TOTAL_INSTRUCTIONS, Main.instruction.length);
 }

 @Test
 public void testLoadInstructionsWithInvalidFilePath() {
 String fileName = INVALID_FILE_PATH;
 assertFalse(loadInstructions(fileName));
 assertEquals(0, Main.instruction.length);
 }

 @Test
 public void testLoadInstructionsWithQuitCommand() {
 String fileName = QUIT_COMMAND;
 assertFalse(loadInstructions(fileName));
 }

 @Test
 public void testProcessInstructionWithHaltOpcode() {
 processInstruction("halt");
 assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testProcessInstructionWithOutOpcode() {
 processInstruction("out 1");
 assertEquals("1", getStringValue(Main.instruction[Main.iPtr - 1]));
 }

 @Test
 public void testProcessInstructionWithInOpcode() {
 processInstruction("in 0");
 assertEquals(42, Variable.getVariableValue("0")); // arbitrary value for testing
 }

 @Test
 public void testProcessInstructionWithMovOpcode() {
 processInstruction("mov 1 2");
 assertEquals(Variable.getVariableValue("1"), Variable.getVariableValue("2"));
 }

 @Test
 public void testProcessInstructionWithSetOpcode() {
 processInstruction("set 1 10");
 assertEquals(10, Variable.getVariableValue("1"));
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstructionWithUnknownOpcode() {
 processInstruction("unknown");
 fail("Unexpectedly continued execution");
 }

 @Test
 public void testProcessInstructionWithDelOpcode() {
 processInstruction("del 1");
 assertFalse(Variable.containsKey("1"));
 }

 @Test
 public void testProcessInstructionWithArrOpcode() {
 processInstruction("arr 1 3");
 assertTrue(Variable.containsKey("1"));
 assertTrue(Variable.containsKey("1[0]"));
 assertTrue(Variable.containsKey("1[1]"));
 assertTrue(Variable.containsKey("1[2]"));
 }

 @Test
 public void testProcessInstructionWithVarOpcode() {
 processInstruction("var x");
 assertTrue(Variable.containsKey("x"));
 }

 @Test
 public void testProcessInstructionWithIncOpcode() {
 processInstruction("inc 1");
 assertEquals(Variable.getVariableValue("1") + 1, Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithDecOpcode() {
 processInstruction("dec 1");
 assertEquals(Variable.getVariableValue("1") - 1, Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithAddOpcode() {
 processInstruction("add 1 2");
 assertEquals(Variable.getVariableValue("1") + Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithSubOpcode() {
 processInstruction("sub 1 2");
 assertEquals(Variable.getVariableValue("1") - Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithMulOpcode() {
 processInstruction("mul 1 2");
 assertEquals(Variable.getVariableValue("1") * Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithDivOpcode() {
 processInstruction("div 1 2");
 assertEquals(Variable.getVariableValue("1") / Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithModOpcode() {
 processInstruction("mod 1 2");
 assertEquals(Variable.getVariableValue("1") % Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithXorOpcode() {
 processInstruction("xor 1 2");
 assertEquals(Variable.getVariableValue("1") ^ Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithOrOpcode() {
 processInstruction("or 1 2");
 assertEquals(Variable.getVariableValue("1") | Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithAndOpcode() {
 processInstruction("and 1 2");
 assertEquals(Variable.getVariableValue("1") & Variable.getVariableValue("2"), Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithJmpOpcode() {
 processInstruction("jmp 10");
 assertEquals(10, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJnzOpcode() {
 processInstruction("jnz 1 10");
 assertEquals(10, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJzOpcode() {
 processInstruction("jz 1 10");
 assertEquals(10, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJeOpcode() {
 processInstruction("je 1 1 10");
 assertEquals(10, Main.iPtr);
 }

 @Test
 public void testProcessInstructionWithJneOpcode() {
 processInstruction("jne 1 1 10");
 assertEquals(Main.iPtr, 1);
 }

 @Test
 public void testProcessInstructionWithCmpOpcode() {
 processInstruction("cmp 1 2 3");
 assertEquals(1, Variable.getVariableValue("3"));
 }

 @Test
 public void testProcessInstructionWithInvalidInstruction() {
 processInstruction("invalid");
 fail("Unexpectedly continued execution");
 }
}
package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_NAME = "testFile.txt";
 private static final String INVALID_FILE_NAME = "";
 private static final String INSTRUCTION_SET = "mov a 0\nmov b a\ninc a\ninc a\njmp label1\nlabel1: inc a";

 @Before
 public void setUp() {
 resetInstructions();
 }

 @After
 public void tearDown() {
 System.exit(0);
 }

 @Test
 public void testLoadInstructionsValidFileName() {
 loadInstructions(VALID_FILE_NAME);
 assertEquals(6, TOTAL_INSTRUCTIONS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsEmptyFileName() {
 loadInstructions(INVALID_FILE_NAME);
 }

 @Test
 public void testProcessInstructionMov() {
 processInstruction("mov a 0");
 assertEquals(0, Variable.getVariableValue("a"));
 processInstruction("mov b a");
 assertEquals(0, Variable.getVariableValue("a"));
 assertEquals(0, Variable.getVariableValue("b"));
 processInstruction("inc a");
 assertEquals(1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionJmp() {
 processInstruction("mov a 0");
 processInstruction("jmp label1");
 processInstruction("inc a");
 processInstruction("label1: inc a");
 processInstruction("inc a");
 assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionOut() {
 processInstruction("out a");
 assertEquals("" + Variable.getVariableValue("a"), System.out.getHistory(0).getLine().substring(6));
 }

 @Test
 public void testProcessInstructionAdd() {
 processInstruction("mov a 3");
 processInstruction("mov b 2");
 processInstruction("add a b");
 assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionSet() {
 processInstruction("mov a 3");
 processInstruction("set c a");
 assertEquals(3, Variable.getVariableValue("c"));
 }

 @Test
 public void testProcessInstructionVar() {
 processInstruction("mov a 3");
 processInstruction("var d");
 assertEquals(3, Variable.getVariableValue("d"));
 }

 @Test
 public void testProcessInstructionDel() {
 processInstruction("mov a 3");
 processInstruction("del a");
 assertTrue(Variable.isVariableRemoved("a"));
 }

 @Test
 public void testProcessInstructionArr() {
 processInstruction("mov a 3");
 processInstruction("arr a 4");
 assertEquals(5, Variable.getVariableValue("a").length());
 assertEquals("", Variable.getVariableValue("a0"));
 assertEquals("", Variable.getVariableValue("a1"));
 assertEquals("", Variable.getVariableValue("a2"));
 assertEquals("", Variable.getVariableValue("a3"));
 assertEquals("", Variable.getVariableValue("a4"));
 }

 @Test
 public void testProcessInstructionIn() {
 processInstruction("mov a 3");
 processInstruction("in a");
 assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstructionInvalidOperation() {
 processInstruction("mov a 3");
 processInstruction("xor a 5"); // Unknown opcode
 assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testLoadInstructionsInvalidFile() {
 loadInstructions("invalidFile.txt");
 assertEquals(0, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructionsEmptyFile() {
 loadInstructions("");
 assertEquals(0, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructionsFileWithInvalidInstruction() {
 loadInstructions("mov a 3\ninvalidInstruction mov b 2");
 assertEquals(1, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructionsFileWithComment() {
 loadInstructions("mov a 3\n// This is a comment");
 assertEquals(1, TOTAL_INSTRUCTIONS);
 }
}
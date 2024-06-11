package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE = "testData/validInstructions.txt";
 private static final String INVALID_FILE = "testData/invalidInstructions.txt";
 private static final String EMPTY_FILE = "";

 @Before
 public void setUp() {
 resetInstructions();
 }

 @After
 public void tearDown() {
 System.exit(0);
 }

 @Test
 public void testLoadInstructionsValidFile() {
 Main.loadInstructions(VALID_FILE);
 assertEquals(6, Main.TOTAL_INSTRUCTIONS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFile() {
 Main.loadInstructions(INVALID_FILE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsEmptyFile() {
 Main.loadInstructions(EMPTY_FILE);
 }

 @Test
 public void testLoadInstructionsGetFileName() {
 String fileName = Main.getFileName();
 assertFalse(fileName.isEmpty());
 }

 @Test
 public void testProcessInstructionOut() {
 Variable.setVariableValue("output", "Test Output");
 Main.processInstruction("out output");
 assertEquals("Test Output", System.out.getHistogram().get(0).toString());
 }

 @Test
 public void testProcessInstructionIn() {
 Variable.setVariableValue("input", 5);
 Main.processInstruction("in input");
 assertEquals(5, Variable.getVariableValue("input"));
 }

 @Test
 public void testProcessInstructionMov() {
 Variable.setVariableValue("var1", 5);
 Main.processInstruction("mov var2 var1");
 assertEquals(5, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstructionSet() {
 Main.processInstruction("set var1 10");
 assertEquals(10, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionDel() {
 Main.processInstruction("del var1");
 assertNull(Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionArr() {
 Main.processInstruction("arr array 3");
 assertEquals(4, Variable.getVariableValue("array").length);
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.processInstruction("jmp label1");
 assertEquals(0, Main.iPtr);
 Main.processInstruction("label1: jmp label2");
 assertEquals(1, Main.iPtr);
 Main.processInstruction("label2:");
 assertEquals(2, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJnz() {
 Main.processInstruction("set var1 5");
 Main.processInstruction("jnz var1 label1");
 assertEquals(0, Main.iPtr);
 Main.processInstruction("label1: set var1 0");
 Main.processInstruction("jnz var1 label1");
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJz() {
 Main.processInstruction("set var1 5");
 Main.processInstruction("jz var1 label1");
 assertEquals(0, Main.iPtr);
 Main.processInstruction("label1: set var1 0");
 Main.processInstruction("jz var1 label1");
 assertEquals(1, Main.iPtr);
 Main.processInstruction("jz var1 label1");
 assertEquals(2, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJe() {
 Main.processInstruction("set var1 5");
 Main.processInstruction("set var2 5");
 Main.processInstruction("je var1 var2 label1");
 assertEquals(2, Main.iPtr);
 Main.processInstruction("set var1 6");
 Main.processInstruction("set var2 5");
 Main.processInstruction("je var1 var2 label1");
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJne() {
 Main.processInstruction("set var1 5");
 Main.processInstruction("set var2 5");
 Main.processInstruction("jne var1 var2 label1");
 assertEquals(4, Main.iPtr);
 Main.processInstruction("set var1 6");
 Main.processInstruction("set var2 5");
 Main.processInstruction("jne var1 var2 label1");
 assertEquals(4, Main.iPtr);
 }

 @Test
 public void testProcessInstructionCmp() {
 Main.processInstruction("set var1 5");
 Main.processInstruction("set var2 5");
 Main.processInstruction("cmp var1 var2 label1");
 assertEquals(5, Main.iPtr);
 Main.processInstruction("set var1 6");
 Main.processInstruction("set var2 5");
 Main.processInstruction("cmp var1 var2 label1");
 assertEquals(6, Main.iPtr);
 Main.processInstruction("set var1 5");
 Main.processInstruction("set var2 6");
 Main.processInstruction("cmp var1 var2 label1");
 assertEquals(5, Main.iPtr);
 }
}
package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String FILENAME = "testFile.txt";
 private static final int EXPECTED_TOTAL_INSTRUCTIONS = 20;
 private static final String[] EXPECTED_INSTRUCTIONS = new String[]{"mov", "set", "inc", "out", "mov", "mov", "add", "jmp", "mov", "jz", "mov", "mov", "mov", "sub", "jmp", "mov", "mov", "jmp", "mov", "mov"};

 @BeforeClass
 public static void setup() {
 resetInstructions();
 }

 @Test
 public void testLoadInstructions() {
 boolean result = Main.loadInstructions(FILENAME);
 assertTrue(result);
 assertEquals(EXPECTED_TOTAL_INSTRUCTIONS, Main.TOTAL_INSTRUCTIONS);
 assertArrayEquals(EXPECTED_INSTRUCTIONS, Main.instruction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructionsInvalidFileName() {
 Main.loadInstructions("");
 fail("Expected IllegalArgumentException not thrown.");
 }

 @Test
 public void testProcessInstructionMov() {
 String[] operands = new String[]{"var1", "var2"};
 Main.processInstruction("mov var1 var2");
 assertEquals(0, Variable.getVariableValue("var1"));
 assertEquals(Variable.getVariableValue("var2"), Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionSet() {
 Main.processInstruction("set var1 5");
 assertEquals(5, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionInc() {
 Main.processInstruction("inc var1");
 assertEquals(6, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionOut() {
 Main.processInstruction("out var1");
 assertEquals("6\n", System.out.getHistogram().get(System.out.size()));
 }

 @Test
 public void testProcessInstructionJmp() {
 Main.processInstruction("jmp 3");
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJz() {
 Variable.setVariableValue("var1", 0);
 Main.processInstruction("jz 3");
 assertEquals(3, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJne() {
 Variable.setVariableValue("var1", 1);
 Main.processInstruction("jne 3");
 assertEquals(6, Main.iPtr);
 }

 @Test
 public void testProcessInstructionJmpNegative() {
 Main.processInstruction("jmp -3");
 fail("Expected IndexOutOfBoundsException not thrown.");
 }

 @Test
 public void testProcessInstructionJmpBeyondInstructions() {
 Main.TOTAL_INSTRUCTIONS = 10;
 Main.instruction = new String[Main.TOTAL_INSTRUCTIONS];
 Main.processInstruction("jmp 20");
 fail("Expected IndexOutOfBoundsException not thrown.");
 }
}
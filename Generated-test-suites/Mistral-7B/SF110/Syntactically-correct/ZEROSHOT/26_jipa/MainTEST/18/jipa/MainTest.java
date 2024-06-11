package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

 private static final String TEST_FILE = "testFile.txt";
 private static final String[] TEST_INSTRUCTIONS = { "mov a 0", "inc a", "mov b a", "inc b", "mov a b", "dec a", "dec b", "jmp 0" }; // Add more test cases as necessary

 @BeforeClass
 public static void setup() {
 Main.loadInstructions(TEST_FILE);
 }

 @Test
 public void testProcessInstruction_mov() {
 String[] instructionArgs = { "a", "0" };
 Main.processInstruction("mov a 0");
 assertEquals(0, Main.Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_inc() {
 String[] instructionArgs = { "a" };
 Main.processInstruction("inc a");
 assertEquals(1, Main.Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_dec() {
 String[] instructionArgs = { "a" };
 Main.processInstruction("dec a");
 assertEquals(0, Main.Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_add() {
 String[] instructionArgs = { "a", "1" };
 Main.processInstruction("add a 1");
 assertEquals(1, Main.Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_sub() {
 String[] instructionArgs = { "a", "1" };
 Main.processInstruction("sub a 1");
 assertEquals(0, Main.Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jmp() {
 String[] instructionArgs = { "0" };
 Main.processInstruction("jmp 0");
 fail("Should not reach this point");
 }

 @Test
 public void testProcessInstruction_jmpWithCondition() {
 String[] instructionArgs = { "1" };
 Main.processInstruction("jnz 1 10");
 assertEquals(1, Main.Variable.getVariableValue("a")); // Before the jump
 Main.processInstruction("mov a 2"); // At the jump destination
 assertEquals(2, Main.Variable.getVariableValue("a")); // After the jump
 }

 @Test
 public void testProcessInstruction_halt() {
 Main.processInstruction("halt");
 // The program should have exited after this instruction
 assertTrue(false);
 }

 @AfterClass
 public static void teardown() {
 Main.resetInstructions();
 }
}
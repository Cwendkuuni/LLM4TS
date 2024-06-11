package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

 private static final String TEST_FILE = "testFile.jip";
 private static final String[] TEST_INSTRUCTIONS = {
 "mov a 0",
 "mov b a",
 "inc a",
 "dec b",
 "set c 5",
 "mov d a",
 "mov a c",
 "inc a",
 "cmp a d",
 "jne label1",
 "mov e 3",
 "mov a e",
 "inc a",
 "mov f a",
 "mov a f",
 "jmp end"
 };

 @Test
 public void testLoadInstructions() {
 Main.loadInstructions(TEST_FILE);
 assertEquals(TEST_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testProcessInstruction_mov() {
 String[] args = { "a", "0" };
 Main.processInstruction(Main.instruction[0]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction(TEST_INSTRUCTIONS[0]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction(TEST_INSTRUCTIONS[1]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction(TEST_INSTRUCTIONS[2]);
 assertEquals(0, Variable.getVariableValue("b"));
 Main.processInstruction(TEST_INSTRUCTIONS[3]);
 assertEquals(0, Variable.getVariableValue("b"));
 Main.processInstruction(TEST_INSTRUCTIONS[4]);
 assertEquals(5, Variable.getVariableValue("c"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.processInstruction(TEST_INSTRUCTIONS[6]);
 assertEquals(1, Variable.getVariableValue("a"));
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(2, Variable.getVariableValue("a"));
 Main.processInstruction(TEST_INSTRUCTIONS[10]);
 assertEquals(1, Variable.getVariableValue("d"));
 Main.processInstruction(TEST_INSTRUCTIONS[12]);
 assertEquals(2, Variable.getVariableValue("d"));
 }

 @Test
 public void testProcessInstruction_dec() {
 Main.processInstruction(TEST_INSTRUCTIONS[5]);
 assertEquals(5, Variable.getVariableValue("b"));
 Main.processInstruction(TEST_INSTRUCTIONS[7]);
 assertEquals(4, Variable.getVariableValue("b"));
 }

 @Test
 public void testProcessInstruction_set() {
 Main.processInstruction(TEST_INSTRUCTIONS[4]);
 assertEquals(5, Variable.getVariableValue("c"));
 }

 @Test
 public void testProcessInstruction_add() {
 String[] args = { "a", "1" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(2, Variable.getVariableValue("a"));
 Main.processInstruction("add a b");
 assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_sub() {
 String[] args = { "a", "1" };
 Main.processInstruction(TEST_INSTRUCTIONS[6]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction("sub a d");
 assertEquals(-1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_mul() {
 String[] args = { "a", "2" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(2, Variable.getVariableValue("a"));
 Main.processInstruction("mul a b");
 assertEquals(10, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_div() {
 String[] args = { "a", "2" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(2, Variable.getVariableValue("a"));
 Main.processInstruction("div a b");
 assertEquals(1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_mod() {
 String[] args = { "a", "2" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(2, Variable.getVariableValue("a"));
 Main.processInstruction("mod a b");
 assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_xor() {
 String[] args = { "a", "1" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction("xor a b");
 assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_or() {
 String[] args = { "a", "1" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction("or a b");
 assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_and() {
 String[] args = { "a", "1" };
 Main.processInstruction(TEST_INSTRUCTIONS[8]);
 assertEquals(0, Variable.getVariableValue("a"));
 Main.processInstruction("and a b");
 assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.processInstruction(TEST_INSTRUCTIONS[15]);
 assertEquals(15, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jnz() {
 Main.processInstruction("jnz label1 10");
 assertEquals(10, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jz() {
 Main.processInstruction("jz label1 10");
 assertEquals(Main.TOTAL_INSTRUCTIONS, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_je() {
 Main.processInstruction("je label1 10");
 assertEquals(Main.TOTAL_INSTRUCTIONS, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jne() {
 Main.processInstruction("jne label1 10");
 assertEquals(15, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_cmp() {
 String[] args = { "d", "a" };
 Main.processInstruction(TEST_INSTRUCTIONS[13]);
 assertEquals(0, Variable.getVariableValue("a"));
 assertEquals(1, Variable.getVariableValue("d"));
 Main.processInstruction("cmp a d");
 assertEquals(1, Variable.getVariableValue(2));
 }

 @Test
 public void testMain() {
 Main.main(new String[0]);
 }

 private static class Variable {
 private static final int NUM_VARIABLES = 26;
 private static final String[] VAR_NAMES = { "a", "b", "c", "d", "e", "f" };
 private static int[] variableValues = new int[NUM_VARIABLES];

 public static int getVariableValue(final String variableName) {
 return variableValues[VAR_NAMES.indexOf(variableName)];
 }

 public static void setVariableValue(final String variableName, final int value) {
 variableValues[VAR_NAMES.indexOf(variableName)] = value;
 }

 public static void removeVariable(final String variableName) {
 System.arraycopy(variableValues, VAR_NAMES.indexOf(variableName) + 1, variableValues, VAR_NAMES.indexOf(variableName), NUM_VARIABLES - VAR_NAMES.indexOf(variableName) - 1);
 }

 public static void addVariable(final String variableName) {
 System.arraycopy(variableValues, NUM_VARIABLES - 1, variableValues, NUM_VARIABLES, 1);
 }
 }

 static {
 Main.TOTAL_INSTRUCTIONS = 0;
 Main.instruction = new String[0];
 Main.iPtr = 0;
 }
}
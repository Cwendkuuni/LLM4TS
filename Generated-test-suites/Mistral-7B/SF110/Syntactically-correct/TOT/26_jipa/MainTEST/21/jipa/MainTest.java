package jipa.test;

import org.junit.Assert;
import org.junit.Test;
import jipa.Main;
import jipa.Variable;

public class MainTest {

 private final static String TEST_FILE = "testFile.txt";
 private final static int INITIAL_VALUE = 5;

 @Test
 public void testLoadInstructions() {
 String fileName = TEST_FILE;
 Main.loadInstructions(fileName);
 Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, 10); // Replace with the actual total number of instructions in the test file
 }

 @Test
 public void testProcessInstruction_mov() {
 String[] instructions = { "mov a b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("b"), Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_out() {
 String[] instructions = { "out a" };
 Main.main(instructions);
 Assert.assertEquals(System.out.size(), 1);
 Assert.assertEquals(System.out.getHistory().get(0).getLine(), "a");
 }

 @Test
 public void testProcessInstruction_jmp() {
 String[] instructions = { "mov a 1", "jmp 1" };
 Main.main(instructions);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testProcessInstruction_outOfBounds() {
 String[] instructions = { "mov a 1", "jmp 1000" };
 Main.main(instructions);
 }

 @Test(expected = NumberFormatException.class)
 public void testProcessInstruction_invalidInstruction() {
 String[] instructions = { "mov a b", "xoo a b" };
 Main.main(instructions);
 }

 @Test
 public void testProcessInstruction_jnz() {
 String[] instructions = { "mov a 1", "mov b 0", "jnz b 1" };
 Main.main(instructions);
 Assert.assertEquals(Main.iPtr, 2); // The instruction at index 1 is not executed
 }

 @Test
 public void testProcessInstruction_jz() {
 String[] instructions = { "mov a 1", "mov b 1", "jz b 1" };
 Main.main(instructions);
 Assert.assertEquals(Main.iPtr, 3); // The instruction at index 2 is executed
 }

 @Test
 public void testProcessInstruction_cmp() {
 String[] instructions = { "mov a 5", "mov b 5", "cmp a b c" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("c"), 1);
 }

 @Test
 public void testLoadInstructions_emptyFile() {
 Main.loadInstructions("");
 Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadInstructions_missingFile() {
 Main.loadInstructions("nonExistentFile.txt");
 }

 @Test
 public void testLoadInstructions_invalidFileFormat() {
 String fileName = TEST_FILE;
 // Modify the file content to be invalid (e.g., missing semicolons, improper indentation)
 try {
 Main.loadInstructions(fileName);
 } catch (Exception e) {
 Assert.fail("Expected an exception, but none was thrown.");
 }
 }

 @Test
 public void testProcessInstruction_invalidLabel() {
 String[] instructions = { "mov a 1", "jmp invalidLabel" };
 Main.main(instructions);
 }

 @Test
 public void testVariableManagement() {
 String[] instructions = { "mov a 1", "mov b a", "inc b", "dec a" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), 0);
 Assert.assertEquals(Variable.getVariableValue("b"), 2);
 }

 @Test
 public void testVariableManagement_multipleVariables() {
 String[] instructions = { "arr a 5", "mov a0 1", "mov a1 2", "mov a2 3", "mov a3 4", "mov a4 5" };
 Main.main(instructions);
 for (int i = 0; i < 5; ++i) {
 Assert.assertEquals(Variable.getVariableValue("a" + i), i + 1);
 }
 }

 @Test
 public void testVariableManagement_negativeValues() {
 String[] instructions = { "mov a -1", "mov b 1", "add a b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), 0);
 }

 @Test
 public void testVariableManagement_largeValues() {
 String[] instructions = { "mov a 2147483647", "mov b 1", "add a b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), 2147483648);
 }

 @Test
 public void testVariableManagement_invalidValue() {
 String[] instructions = { "mov a x", "mov b 1", "add a b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), 0);
 }

 @Test
 public void testVariableManagement_integerOverflow() {
 String[] instructions = { "mov a 2147483647", "mov b 1", "add a b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), Integer.MIN_VALUE);
 }

 @Test
 public void testVariableManagement_floatValues() {
 String[] instructions = { "mov a 3.14159265", "mov b 2", "add a b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), 5.14159265, 0.000001);
 }

 @Test
 public void testVariableManagement_stringValues() {
 String[] instructions = { "mov a \"Hello\", b \"World\"", "mov c a", "mov d b", "mov e a", "mov f b" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("a"), "Hello");
 Assert.assertEquals(Variable.getVariableValue("b"), "World");
 Assert.assertEquals(Variable.getVariableValue("c"), "Hello");
 Assert.assertEquals(Variable.getVariableValue("d"), "World");
 Assert.assertEquals(Variable.getVariableValue("e"), "Hello");
 Assert.assertEquals(Variable.getVariableValue("f"), "World");
 }

 @Test
 public void testVariableManagement_stringConcatenation() {
 String[] instructions = { "mov a \"Hello\"" , "mov b \"World\"", "mov c a", "mov d b", "mov e c", "mov f d", "mov g e", "mov h f", "mov i g", "mov j h", "mov k i", "mov l j", "mov m k" };
 Main.main(instructions);
 Assert.assertEquals(Variable.getVariableValue("m"), "HelloWorld");
 }
}
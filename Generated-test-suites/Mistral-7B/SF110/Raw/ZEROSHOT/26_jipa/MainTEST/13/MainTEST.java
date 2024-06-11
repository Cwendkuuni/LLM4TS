package jipa;

import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MainTest {

 private static final String TEST_FILE = "test.txt";
 private static final String[] TEST_INSTRUCTIONS = {
 "mov a 0",
 "inc a",
 "mov b a",
 "inc b",
 "inc a",
 "mov c a",
 "inc c",
 "mov a b",
 "dec a",
 "mov d a",
 "dec d",
 "dec a",
 "jz end",
 "out a"
 };

 @Test
 public void testProcessingInstructions() {
 Variable.resetVariables();
 Main.loadInstructions(TEST_FILE);
 for (String instruction : TEST_INSTRUCTIONS) {
 Main.processInstruction(instruction);
 }
 assertEquals("6", Variable.getVariableValue("a"));
 assertEquals("7", Variable.getVariableValue("b"));
 assertEquals("5", Variable.getVariableValue("c"));
 assertEquals("5", Variable.getVariableValue("d"));
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidInstruction() {
 Variable.resetVariables();
 Main.loadInstructions(TEST_FILE);
 Main.processInstruction("invalid");
 }

 @Test
 public void testJumpInstruction() {
 Variable.resetVariables();
 Main.loadInstructions(TEST_FILE);
 Main.iPtr = 0;
 for (int i = 0; i < 13; ++i) {
 Main.processInstruction(Main.instruction[i]);
 }
 Main.processInstruction(Main.instruction[13]);
 assertEquals("0", Variable.getVariableValue("a"));
 }

 @Test
 public void testLoadInstructions() {
 assertNull(Main.instruction);
 Main.loadInstructions("");
 assertEquals(0, Main.instruction.length);
 Main.loadInstructions(TEST_FILE);
 assertEquals(TEST_INSTRUCTIONS.length, Main.instruction.length);
 }

 @Test
 public void testGetFileName() {
 String fileName = Main.getFileName();
 assertNotNull(fileName);
 }

 @Test
 public void testRemoveComment() {
 String line = "mov a 0 // test comment";
 assertEquals("mov a 0", Main.removeComment(line));
 line = " mov a 0 // test comment";
 assertEquals("mov a 0", Main.removeComment(line));
 line = "mov a 0";
 assertEquals("mov a 0", Main.removeComment(line));
 }

 @Test
 public void testIsNumeric() {
 assertEquals(true, Main.isNumeric("1"));
 assertEquals(true, Main.isNumeric("123"));
 assertEquals(true, Main.isNumeric("1234567890"));
 assertEquals(false, Main.isNumeric("abc"));
 assertEquals(false, Main.isNumeric("abcd"));
 assertEquals(false, Main.isNumeric("abcd1"));
 }
}
package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String TEST_FILE = "testFile.txt";
 private static final String TEST_INSTRUCTIONS = "mov a 0\nout a\n";

 @Before
 public void setUp() {
 resetInstructions();
 loadInstructions(TEST_FILE);
 }

 @After
 public void tearDown() {
 System.out.println("Test finished. Exiting...");
 System.exit(0);
 }

 @Test
 public void testLoadInstructions() {
 assertEquals(2, TOTAL_INSTRUCTIONS);
 assertNotNull(instruction);
 assertArrayEquals(new String[]{"mov a 0", "out a"}, instruction);
 }

 @Test
 public void testProcessInstruction_mov() {
 processInstruction("mov a 0");
 assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_out() {
 processInstruction("out a");
 assertEquals("0", getStringValue("a"));
 System.out.println("Output: " + getStringValue("a"));
 }

 @Test
 public void testProcessInstruction_halt() {
 processInstruction("halt");
 fail("Test should not reach this point.");
 }

 @Test
 public void testProcessInstruction_invalidOpcode() {
 processInstruction("invalidOpcode");
 }

 @Test
 public void testGetFileName() {
 String fileName = getFileName();
 assertFalse(fileName.isEmpty());
 }

 @Test
 public void testLoadInstructions_emptyFile() {
 loadInstructions("");
 assertEquals(0, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructions_nonExistentFile() {
 loadInstructions("nonExistentFile.txt");
 assertEquals(0, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructions_invalidInstruction() {
 String invalidInstruction = "mov a a"; // This instruction should be considered invalid
 loadInstructions(TEST_FILE + "\n" + invalidInstruction);
 assertEquals(2, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructions_commentedInstruction() {
 String commentedInstruction = "mov a 0; This is a comment"; // This instruction should be ignored
 loadInstructions(TEST_FILE + "\n" + commentedInstruction);
 assertEquals(1, TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testLoadInstructions_multipleFiles() {
 String testFile1 = "testFile1.txt";
 String testFile2 = "testFile2.txt";
 String instruction1 = "mov a 1";
 String instruction2 = "mov b 2";
 String instructions = testFile1 + "\n" + instruction1 + "\n" + testFile2 + "\n" + instruction2;
 loadInstructions(instructions);
 assertEquals(3, TOTAL_INSTRUCTIONS);
 assertEquals("1", Variable.getVariableValue("a"));
 assertEquals("2", Variable.getVariableValue("b"));
 }
}
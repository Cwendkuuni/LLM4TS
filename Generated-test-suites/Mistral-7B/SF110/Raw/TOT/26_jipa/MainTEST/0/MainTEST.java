import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_PATH = "testFiles/validInstructions.txt";
 private static final String INVALID_FILE_PATH = "testFiles/invalidInstructions.txt";
 private static final String EMPTY_FILE_PATH = "";
 private static final String QUIT_COMMAND = "q";

 @Before
 public void setUp() {
 resetInstructions();
 }

 @Test
 public void testLoadInstructions_ValidFilePath() {
 final int expectedTotalInstructions = 10;
 final String[] expectedInstructions = new String[] {
 "mov A 0", "inc A", "mov B A", "dec B", "mov A B", "dec A", "add A B", "dec B", "mov C A", "jmp label1"
 };
 loadInstructions(VALID_FILE_PATH);
 assertEquals(expectedTotalInstructions, Main.TOTAL_INSTRUCTIONS);
 assertArrayEquals(expectedInstructions, Main.instruction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructions_InvalidFilePath() {
 loadInstructions(INVALID_FILE_PATH);
 }

 @Test
 public void testLoadInstructions_EmptyFilePath() {
 loadInstructions(EMPTY_FILE_PATH);
 assertEquals(0, Main.TOTAL_INSTICATIONS);
 assertEquals(new String[0], Main.instruction);
 }

 @Test
 public void testLoadInstructions_QuitCommand() {
 loadInstructions(QUIT_COMMAND);
 }

 @Test
 public void testProcessInstruction_Halt() {
 processInstruction("halt");
 assertEquals(0, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_Out() {
 processInstruction("out A");
 assertEquals("", System.out.getErrorStream().toString());
 assertEquals("", System.out.getOutputStream().toString());
 assertNotNull(System.out.getErrorStream());
 assertNotNull(System.out.getOutputStream());
 }

 @Test
 public void testProcessInstruction_In() {
 processInstruction("in A");
 assertNull(Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_Set() {
 processInstruction("set A 5");
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_Mov() {
 processInstruction("mov A B");
 assertEquals(Variable.getVariableValue("B"), Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_Add() {
 processInstruction("add A B");
 assertEquals(Variable.getVariableValue("A") + Variable.getVariableValue("B"), Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_Jmp() {
 processInstruction("jmp label1");
 assertEquals(0, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_Jz() {
 processInstruction("jz label1");
 processInstruction("mov A 1");
 processInstruction("jz label2");
 processInstruction("mov A 0");
 processInstruction("jz label1");
 assertEquals(2, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_Jne() {
 processInstruction("jne label1");
 processInstruction("mov A 1");
 processInstruction("mov B 1");
 processInstruction("jne label2");
 processInstruction("mov A 0");
 processInstruction("mov B 0");
 processInstruction("jne label1");
 assertEquals(6, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_JmpIndexOutOfBounds() {
 processInstruction("jmp 1000");
 }

 @Test
 public void testProcessInstruction_InvalidOpcode() {
 processInstruction("invalidOp");
 }
}
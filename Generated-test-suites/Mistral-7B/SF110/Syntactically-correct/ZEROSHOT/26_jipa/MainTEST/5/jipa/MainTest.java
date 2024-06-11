package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String VALID_FILE_NAME = "validInstructions.txt";
 private static final String INVALID_FILE_NAME = "nonExistentFile.txt";
 private static final String INPUT_DATA = "5, 10, var1, var2, arr1, 3, arr1, 5, jmp 5";
 private static final int EXpectedTotalInstructions = 10;
 private static final String[] EXpectedInstructionArray = {"halt", "set", "var1", "5", "mov", "var2", "0", "mov", "arr1", "3", "set", "arr1", "5", "jmp", "5"};
 private static final String[] instructionArray;
 private static int iPtr;

 @Before
 public void setup() {
 resetInstructions();
 }

 @Test
 public void testLoadInstructionsWithValidFileName() {
 loadInstructions(VALID_FILE_NAME);
 assertNotNull(instructionArray);
 assertEquals(EXpectedTotalInstructions, instruction.length);
 assertArrayEquals(EXpectedInstructionArray, instruction);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadInstructionsWithInvalidFileName() {
 loadInstructions(INVALID_FILE_NAME);
 fail("FileNotFoundException should be thrown.");
 }

 @Test
 public void testLoadInstructionsWithEmptyInput() {
 resetInstructions();
 loadInstructions("");
 assertEquals(0, instruction.length);
 }

 @Test
 public void testProcessInstructionWithHalt() {
 processInstruction("halt");
 assertEquals(0, iPtr);
 assertEquals(0, TOTAL_INSTRUCTIONS);
 System.exit(0);
 }

 @Test
 public void testProcessInstructionWithOut() {
 processInstruction("out var1");
 assertEquals("" + Variable.getVariableValue("var1"), System.out.getHistory(0).trim());
 }

 @Test
 public void testProcessInstructionWithIn() {
 Variable.setVariableValue("var3", 42);
 processInstruction("in var3");
 assertEquals(42, Variable.getVariableValue("var3"));
 }

 @Test
 public void testProcessInstructionWithMov() {
 Variable.setVariableValue("var1", 10);
 processInstruction("mov var2 var1");
 assertEquals(10, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstructionWithCpy() {
 Variable.setVariableValue("var1", 10);
 Variable.setVariableValue("var3", 20);
 processInstruction("cpy var3 var1");
 assertEquals(10, Variable.getVariableValue("var1"));
 assertEquals(20, Variable.getVariableValue("var3"));
 }

 @Test
 public void testProcessInstructionWithSet() {
 processInstruction("set arr1 5");
 assertEquals(1, Variable.getVariableCount());
 assertEquals("arr1", Variable.getVariableName(0));
 assertEquals(5, Variable.getVariableValue("arr1"));
 }

 @Test
 public void testProcessInstructionWithArr() {
 processInstruction("arr var1 3");
 assertEquals(3, Variable.getVariableCount());
 assertEquals("arr1", Variable.getVariableName(0));
 assertEquals("arr11", Variable.getVariableName(2));
 assertNotNull(Variable.getVariableValue("arr11"));
 }

 @Test
 public void testProcessInstructionWithAdd() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("add var1 var2");
 assertEquals(15, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithSub() {
 Variable.setVariableValue("var1", 15);
 Variable.setVariableValue("var2", 10);
 processInstruction("sub var1 var2");
 assertEquals(5, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithMul() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("mul var1 var2");
 assertEquals(50, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithDiv() {
 Variable.setVariableValue("var1", 20);
 Variable.setVariableValue("var2", 4);
 processInstruction("div var1 var2");
 assertEquals(5, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithMod() {
 Variable.setVariableValue("var1", 15);
 Variable.setVariableValue("var2", 3);
 processInstruction("mod var1 var2");
 assertEquals(0, Variable.getVariableValue("var1"));
 assertEquals(3, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstructionWithXor() {
 Variable.setVariableValue("var1", 10);
 Variable.setVariableValue("var2", 5);
 processInstruction("xor var1 var2");
 assertEquals(5, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithOr() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("or var1 var2");
 assertEquals(15, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithAnd() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("and var1 var2");
 assertEquals(0, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstructionWithJmp() {
 processInstruction("jmp 5");
 assertEquals(5, iPtr);
 }

 @Test
 public void testProcessInstructionWithJnz() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("jnz var1 10");
 assertEquals(10, iPtr);
 }

 @Test
 public void testProcessInstructionWithJz() {
 Variable.setVariableValue("var1", 0);
 Variable.setVariableValue("var2", 10);
 processInstruction("jz var1 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @Test
 public void testProcessInstructionWithJe() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 5);
 processInstruction("je var1 var2 10");
 assertEquals(10, iPtr);
 }

 @Test
 public void testProcessInstructionWithJne() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("jne var1 var2 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @Test
 public void testProcessInstructionWithCmp() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 5);
 processInstruction("cmp var1 var2 1");
 assertEquals(1, Variable.getVariableValue("1"));
 Variable.reset();
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 6);
 processInstruction("cmp var1 var2 1");
 assertEquals(0, Variable.getVariableValue("1"));
 }

 @Test
 public void testProcessInstructionWithJa() {
 Variable.setVariableValue("var1", 10);
 Variable.setVariableValue("var2", 5);
 processInstruction("ja var1 var2 10");
 assertEquals(10, iPtr);
 }

 @Test
 public void testProcessInstructionWithJae() {
 Variable.setVariableValue("var1", 10);
 Variable.setVariableValue("var2", 5);
 processInstruction("jae var1 var2 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @Test
 public void testProcessInstructionWithJb() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("jb var1 var2 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @Test
 public void testProcessInstructionWithJbe() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 5);
 processInstruction("jbe var1 var2 10");
 assertEquals(10, iPtr);
 }

 @Test
 public void testProcessInstructionWithJz() {
 Variable.setVariableValue("var1", 0);
 processInstruction("jz var1 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @Test
 public void testProcessInstructionWithJe() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 5);
 processInstruction("je var1 var2 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @Test
 public void testProcessInstructionWithJne() {
 Variable.setVariableValue("var1", 5);
 Variable.setVariableValue("var2", 10);
 processInstruction("jne var1 var2 10");
 assertEquals(EXpectedTotalInstructions, iPtr);
 }

 @After
 public void teardown() {
 resetInstructions();
 }

 private void loadInstructions(String fileName) {
 Main.loadInstructions(fileName);
 instructionArray = Main.instruction;
 iPtr = Main.iPtr;
 }
}
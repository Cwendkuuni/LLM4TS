package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

 static String[] instructions;
 static int iPtr;
 static int totalInstructions;

 @SuppressWarnings("static-access")
 @BeforeClass
 public static void setup() throws Exception {
 Main.loadInstructions("testFile.asm");
 totalInstructions = Main.TOTAL_INSTRUCTIONS;
 instructions = Main.instruction;
 iPtr = 0;
 }

 @AfterClass
 public static void teardown() {
 System.out.println("Test finished. Exiting program.");
 System.exit(0);
 }

 @Test
 public void testSet() {
 String opcode = "set";
 String variable = "x";
 int value = 5;

 Main.Variable.setVariableValue(variable, 0);
 Main.processInstruction(instructions[iPtr++]); // set x 5

 assertEquals(value, Main.Variable.getVariableValue(variable));
 }

 @Test
 public void testMov() {
 String opcode = "mov";
 String sourceVariable = "x";
 String targetVariable = "y";

 Main.Variable.setVariableValue(sourceVariable, 5);
 Main.Variable.setVariableValue(targetVariable, 0);

 Main.processInstruction(instructions[iPtr++]); // mov y x

 assertEquals(5, Main.Variable.getVariableValue(targetVariable));
 }

 @Test
 public void testAdd() {
 String opcode = "add";
 String variable1 = "x";
 String variable2 = "y";
 int value1 = 5;
 int value2 = 7;
 int expectedResult = value1 + value2;

 Main.Variable.setVariableValue(variable1, value1);
 Main.Variable.setVariableValue(variable2, value2);

 Main.processInstruction(instructions[iPtr++]); // add x y

 assertEquals(expectedResult, Main.Variable.getVariableValue(variable1));
 }

 @Test
 public void testJmp() {
 String opcode = "jmp";
 int label = 10;

 Main.Label.buildLabelList();

 Main.processInstruction(instructions[iPtr++]); // jmp 10

 assertEquals(10, iPtr);
 }

 @Test
 public void testJz() {
 String opcode = "jz";
 String label = "end";
 int value = 0;

 Main.Variable.setVariableValue("x", value);

 Main.processInstruction(instructions[iPtr++]); // jz end

 assertEquals(iPtr, totalInstructions);
 }

 @Test
 public void testJne() {
 String opcode = "jne";
 String label = "else";
 int value1 = 5;
 int value2 = 5;

 Main.Variable.setVariableValue("x", value1);
 Main.Variable.setVariableValue("y", value2);

 Main.processInstruction(instructions[iPtr++]); // jne else

 assertEquals(iPtr + 1, totalInstructions);
 }

 @Test
 public void testJmpWithInvalidLabel() {
 String opcode = "jmp";
 String label = "invalidLabel";

 Main.Label.buildLabelList();

 // This test should cause an exception
 Main.processInstruction(instructions[iPtr++]); // jmp invalidLabel
 }
}
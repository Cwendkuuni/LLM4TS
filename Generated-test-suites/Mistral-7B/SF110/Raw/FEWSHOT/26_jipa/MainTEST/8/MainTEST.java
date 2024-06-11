package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {
 // Add test cases for each method in the Main class

 @Test
 public void testLoadInstructions() {
 // Add test case to check if loadInstructions method loads instructions correctly
 String fileName = "test.txt";
 Main.loadInstructions(fileName);
 // Add assertions to check the loaded instructions
 }

 @Test
 public void testProcessInstruction_mov() {
 String instruction = "mov x 0";
 Main.processInstruction(instruction);
 assertEquals(0, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_inc() {
 String instruction = "inc x";
 Main.processInstruction(instruction);
 assertEquals(1, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_add() {
 String instruction = "add x 1";
 Variable.setVariableValue("x", 2);
 Main.processInstruction(instruction);
 assertEquals(3, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_jmp() {
 String instruction1 = "jmp 10";
 String instruction2 = "mov x 5";
 Main.instruction = new String[] { instruction1, instruction2 };
 Main.iPtr = 0;
 Main.processInstruction(instruction1);
 assertEquals(10, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jnz() {
 String instruction1 = "jnz x 5";
 String instruction2 = "mov x 0";
 Variable.setVariableValue("x", 1);
 Main.instruction = new String[] { instruction1, instruction2 };
 Main.iPtr = 0;
 Main.processInstruction(instruction1);
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_arr() {
 String instruction = "arr y 3";
 Variable.addVariable("y");
 Main.processInstruction(instruction);
 assertEquals(3, Variable.size("y"));
 }

 @Test
 public void testProcessInstruction_var() {
 String instruction = "var z";
 Variable.addVariable("z");
 }

 @Test
 public void testProcessInstruction_set() {
 String instruction = "set x 5";
 Main.processInstruction(instruction);
 assertEquals(5, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_del() {
 String instruction = "del x";
 Variable.removeVariable("x");
 assertNull(Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_out() {
 String instruction = "out x";
 assertEquals("" + Variable.getVariableValue("x"), Main.out.toString());
 }

 @Test
 public void testProcessInstruction_in() {
 String instruction = "in x";
 String input = "10";
 Main.in = new ByteArrayInputStream(input.getBytes());
 Main.processInstruction(instruction);
 assertEquals(10, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_halts() {
 String instruction = "halt";
 Main.processInstruction(instruction);
 // Add assertion to check if the program has been terminated
 }
}
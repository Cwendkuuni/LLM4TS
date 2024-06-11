package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {

 private static String[] instructions;

 @BeforeClass
 public static void setUpClass() {
 Main.loadInstructions("testFile.asm");
 }

 @Test
 public void testProcessInstruction_halt() {
 Main.processInstruction("halt");
 assertEquals(0, Main.iPtr);
 System.exit(0); // Test case should end the program
 }

 @Test
 public void testProcessInstruction_out() {
 Main.processInstruction("out var0");
 assertEquals("", Variable.getVariableValue("var0"));
 Main.Variable.setVariableValue("var0", "Hello");
 Main.processInstruction("out var0");
 assertEquals("Hello", System.console().readLine());
 }

 @Test
 public void testProcessInstruction_in() {
 Main.processInstruction("in var0");
 assertEquals(0, Variable.getVariableValue("var0"));
 Main.Variable.setVariableValue("var0", 42);
 Main.processInstruction("in var1");
 assertEquals(42, Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstruction_mov() {
 Main.Variable.setVariableValue("var2", 10);
 Main.processInstruction("mov var3 var2");
 assertEquals(10, Variable.getVariableValue("var3"));
 }

 @Test
 public void testProcessInstruction_cpy() {
 Main.Variable.setVariableValue("var2", 10);
 Main.Variable.setVariableValue("var4", 5);
 Main.processInstruction("cpy var5 var2");
 assertEquals(10, Variable.getVariableValue("var5"));
 }

 @Test
 public void testProcessInstruction_set() {
 Main.processInstruction("set var0 5");
 assertEquals(5, Variable.getVariableValue("var0"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstruction_set_invalid_number() {
 Main.processInstruction("set var0 x");
 }

 @Test
 public void testProcessInstruction_del() {
 Main.Variable.setVariableValue("var0", 10);
 Main.processInstruction("del var0");
 assertNull(Variable.getVariableValue("var0"));
 }

 @Test
 public void testProcessInstruction_arr() {
 Main.processInstruction("arr var1 3");
 assertEquals(3, Variable.getVariableValue("var1").length);
 assertEquals("", Variable.getVariableValue("var1[0]"));
 assertEquals("", Variable.getVariableValue("var1[1]"));
 assertEquals("", Variable.getVariableValue("var1[2]"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.Variable.setVariableValue("var0", 5);
 Main.processInstruction("inc var0");
 assertEquals(6, Variable.getVariableValue("var0"));
 }

 @Test
 public void testProcessInstruction_dec() {
 Main.Variable.setVariableValue("var0", 6);
 Main.processInstruction("dec var0");
 assertEquals(5, Variable.getVariableValue("var0"));
 }

 @Test
 public void testProcessInstruction_add() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("add var2 var0 var1");
 assertEquals(8, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_sub() {
 Main.Variable.setVariableValue("var0", 10);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("sub var2 var0 var1");
 assertEquals(7, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_mul() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("mul var2 var0 var1");
 assertEquals(15, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_div() {
 Main.Variable.setVariableValue("var0", 10);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("div var2 var0 var1");
 assertEquals(3, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_mod() {
 Main.Variable.setVariableValue("var0", 10);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("mod var2 var0 var1");
 assertEquals(1, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_xor() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("xor var2 var0 var1");
 assertEquals(2, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_or() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("or var2 var0 var1");
 assertEquals(6, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_and() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("and var2 var0 var1");
 assertEquals(1, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.processInstruction("jmp label1");
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jnz() {
 Main.Variable.setVariableValue("var0", 5);
 Main.processInstruction("jnz label1 label2");
 assertEquals(6, Main.iPtr);
 Main.Variable.setVariableValue("var0", 0);
 Main.processInstruction("jnz label1 label2");
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jz() {
 Main.Variable.setVariableValue("var0", 0);
 Main.processInstruction("jz label1 label2");
 assertEquals(1, Main.iPtr);
 Main.Variable.setVariableValue("var0", 5);
 Main.processInstruction("jz label1 label2");
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_je() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 5);
 Main.processInstruction("je label1 label2");
 assertEquals(5, Main.iPtr);
 Main.Variable.setVariableValue("var0", 3);
 Main.processInstruction("je label1 label2");
 assertEquals(6, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jne() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.processInstruction("jne label1 label2");
 assertEquals(6, Main.iPtr);
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 5);
 Main.processInstruction("jne label1 label2");
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_cmp() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.Variable.setVariableValue("var2", 1);
 Main.processInstruction("cmp var2 var0 var1");
 assertEquals(0, Variable.getVariableValue("var2"));
 Main.processInstruction("cmp var2 var0 var1");
 assertEquals(1, Variable.getVariableValue("var2"));
 }

 @Test
 public void testProcessInstruction_jeq() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 5);
 Main.Variable.setVariableValue("var2", 0);
 Main.processInstruction("jeq label1 label2");
 assertEquals(5, Main.iPtr);
 Main.Variable.setVariableValue("var0", 3);
 Main.processInstruction("jeq label1 label2");
 assertEquals(6, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jneq() {
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 3);
 Main.Variable.setVariableValue("var2", 1);
 Main.processInstruction("jneq label1 label2");
 assertEquals(6, Main.iPtr);
 Main.Variable.setVariableValue("var0", 5);
 Main.Variable.setVariableValue("var1", 5);
 Main.processInstruction("jneq label1 label2");
 assertEquals(5, Main.iPtr);
 }

 @Test
 public void testGetFileName() {
 Main.processInstruction("getFileName");
 String fileName = System.console().readLine();
 assertEquals("testFile.asm", fileName);
 }
}
package jipa.test;

import static org.junit.Assert.*;
import jipa.Main;
import static jipa.Main.opcodes.*;

import org.junit.Test;

public class MainTest {

 private static final String TEST_FILE = "test.txt";

 @Test
 public void testMain() {
 final String[] args = { TEST_FILE };
 Main.main(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithEmptyArgs() {
 Main.main(new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithInvalidFile() {
 Main.main(new String[] { "invalid_file.txt" });
 }

 @Test
 public void testLoadInstructions() {
 Main.loadInstructions(TEST_FILE);
 assertEquals(Main.TOTAL_INSTRUCTIONS, 10);
 }

 @Test(expected = IllegalStateException.class)
 public void testLoadInstructionsWithEmptyFile() {
 Main.loadInstructions("");
 }

 @Test
 public void testProcessInstruction_halt() {
 Main.processInstruction("halt");
 assertEquals(System.exit.callCount, 1);
 }

 @Test
 public void testProcessInstruction_out() {
 Main.processInstruction("out var1");
 assertEquals(System.out.println.callCount, 1);
 }

 @Test
 public void testProcessInstruction_in() {
 Main.processInstruction("in var2");
 // Cannot test this directly, as it requires user input
 }

 @Test
 public void testProcessInstruction_mov() {
 Main.processInstruction("mov var1 var2");
 assertEquals(Variable.getVariableValue("var2"), Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstruction_cpy() {
 Main.processInstruction("cpy var1 var3");
 assertEquals(Variable.getVariableValue("var3"), Variable.getVariableValue("var1"));
 }

 @Test
 public void testProcessInstruction_set() {
 Main.processInstruction("set var1 5");
 assertEquals(Variable.getVariableValue("var1"), 5);
 }

 @Test
 public void testProcessInstruction_del() {
 Main.processInstruction("del var1");
 assertFalse(Variable.containsKey("var1"));
 }

 @Test
 public void testProcessInstruction_arr() {
 Main.processInstruction("arr var1 3");
 assertTrue(Variable.containsKey("var1"));
 assertEquals(Variable.getVariableValue("var1").size(), 4);
 }

 @Test
 public void testProcessInstruction_var() {
 Main.processInstruction("var var4");
 assertTrue(Variable.containsKey("var4"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.processInstruction("inc var1");
 assertEquals(Variable.getVariableValue("var1"), 6);
 }

 @Test
 public void testProcessInstruction_dec() {
 Main.processInstruction("dec var1");
 assertEquals(Variable.getVariableValue("var1"), 5);
 }

 @Test
 public void testProcessInstruction_add() {
 Main.processInstruction("add var1 var2");
 Main.processInstruction("add var1 var3");
 Main.processInstruction("add var1 var4");
 Main.processInstruction("add var1 var5");
 assertEquals(Variable.getVariableValue("var1"), 15);
 }

 @Test
 public void testProcessInstruction_sub() {
 Main.processInstruction("sub var1 var2");
 Main.processInstruction("sub var1 var3");
 Main.processInstruction("sub var1 var4");
 Main.processInstruction("sub var1 var5");
 assertEquals(Variable.getVariableValue("var1"), -12);
 }

 @Test
 public void testProcessInstruction_mul() {
 Main.processInstruction("mul var1 var2");
 Main.processInstruction("mul var1 var3");
 Main.processInstruction("mul var1 var4");
 Main.processInstruction("mul var1 var5");
 assertEquals(Variable.getVariableValue("var1"), -622122547203200000L);
 }

 @Test
 public void testProcessInstruction_div() {
 Main.processInstruction("div var1 var2");
 Main.processInstruction("div var1 var3");
 Main.processInstruction("div var1 var4");
 Main.processInstruction("div var1 var5");
 assertEquals(Variable.getVariableValue("var1"), -2);
 }

 @Test
 public void testProcessInstruction_mod() {
 Main.processInstruction("mod var1 var2");
 Main.processInstruction("mod var1 var3");
 Main.processInstruction("mod var1 var4");
 Main.processInstruction("mod var1 var5");
 assertEquals(Variable.getVariableValue("var1"), 0);
 }

 @Test
 public void testProcessInstruction_xor() {
 Main.processInstruction("xor var1 var2");
 Main.processInstruction("xor var1 var3");
 Main.processInstruction("xor var1 var4");
 Main.processInstruction("xor var1 var5");
 assertEquals(Variable.getVariableValue("var1"), -125);
 }

 @Test
 public void testProcessInstruction_or() {
 Main.processInstruction("or var1 var2");
 Main.processInstruction("or var1 var3");
 Main.processInstruction("or var1 var4");
 Main.processInstruction("or var1 var5");
 assertEquals(Variable.getVariableValue("var1"), -1);
 }

 @Test
 public void testProcessInstruction_and() {
 Main.processInstruction("and var1 var2");
 Main.processInstruction("and var1 var3");
 Main.processInstruction("and var1 var4");
 Main.processInstruction("and var1 var5");
 assertEquals(Variable.getVariableValue("var1"), 0);
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.processInstruction("jmp 5");
 assertEquals(Main.iPtr, 5);
 }

 @Test
 public void testProcessInstruction_jnz() {
 Main.processInstruction("jnz var1 10");
 Main.setVariableValue("var1", 5);
 assertEquals(Main.iPtr, 10);
 Main.setVariableValue("var1", 0);
 assertEquals(Main.iPtr, 7);
 }

 @Test
 public void testProcessInstruction_jz() {
 Main.processInstruction("jz var1 10");
 Main.setVariableValue("var1", 5);
 assertEquals(Main.iPtr, 7);
 Main.setVariableValue("var1", 0);
 assertEquals(Main.iPtr, 10);
 }

 @Test
 public void testProcessInstruction_je() {
 Main.processInstruction("je var1 var2 10");
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 5);
 assertEquals(Main.iPtr, 10);
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 7);
 }

 @Test
 public void testProcessInstruction_ja() {
 Main.processInstruction("ja var1 var2 10");
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 4);
 assertEquals(Main.iPtr, 7);
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 10);
 }

 @Test
 public void testProcessInstruction_jae() {
 Main.processInstruction("jae var1 var2 10");
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 4);
 assertEquals(Main.iPtr, 7);
 Main.setVariableValue("var1", 6);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 10);
 }

 @Test
 public void testProcessInstruction_jb() {
 Main.processInstruction("jb var1 var2 10");
 Main.setVariableValue("var1", 6);
 Main.setVariableValue("var2", 4);
 assertEquals(Main.iPtr, 10);
 Main.setVariableValue("var1", 6);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 7);
 }

 @Test
 public void testProcessInstruction_jbe() {
 Main.processInstruction("jbe var1 var2 10");
 Main.setVariableValue("var1", 6);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 10);
 Main.setVariableValue("var1", 7);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 7);
 }

 @Test
 public void testProcessInstruction_jne() {
 Main.processInstruction("jne var1 var2 10");
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 5);
 assertEquals(Main.iPtr, 7);
 Main.setVariableValue("var1", 5);
 Main.setVariableValue("var2", 6);
 assertEquals(Main.iPtr, 10);
 }
}
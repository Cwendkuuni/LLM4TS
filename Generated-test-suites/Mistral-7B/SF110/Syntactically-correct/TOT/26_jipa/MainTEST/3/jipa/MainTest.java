package jipa.test;

import org.junit.Assert;
import org.junit.Test;
import jipa.Main;
import jipa.Variable;

public class MainTest {

 private static final String TEST_FILE_NAME = "testInstructions.txt";
 private static final String[] TEST_CASES = {
 "mov a 0",
 "mov b a",
 "mov a b",
 "inc a",
 "inc a",
 "dec a",
 "dec a",
 "set a 5",
 "set b a",
 "set c 10",
 "add a b",
 "add a c",
 "sub a b",
 "sub a c",
 "mul a b",
 "mul a c",
 "div a b",
 "div a c",
 "mod a b",
 "mod a c",
 "xor a b",
 "xor a c",
 "or a b",
 "or a c",
 "and a b",
 "and a c",
 "cmp a b",
 "cmp a c",
 "jmp label1",
 "jnz label2 0",
 "jz label3 0",
 "je label4 a",
 "ja label5 5",
 "jae label6 5",
 "jb label7 -5",
 "jbe label8 -5",
 "jne label9 b",
 "jmp label10"
 };

 @Test
 public void testMain() {
 Main.main(new String[] { TEST_FILE_NAME });
 Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, Variable.getVariableCount());
 }

 @Test
 public void testProcessInstruction_mov() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("mov a 0");
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_mov_with_operand() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("mov b a");
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 Assert.assertEquals(0, Variable.getVariableValue("b"));
 }

 @Test
 public void testProcessInstruction_mov_with_both_operands_and_destination_same() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("mov a a");
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("inc a");
 Assert.assertEquals(1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_dec() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("dec a");
 Assert.assertEquals(-1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_set() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 5");
 Assert.assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_set_with_invalid_operand() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set x 5");
 Assert.assertFalse(Variable.containsVariable("x"));
 }

 @Test
 public void testProcessInstruction_add() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("set b 2");
 Main.processInstruction("add a b");
 Assert.assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_add_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("add a b"); // b is not defined
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_sub() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 5");
 Main.processInstruction("set b 3");
 Main.processInstruction("sub a b");
 Assert.assertEquals(2, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_sub_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 5");
 Main.processInstruction("sub a b"); // b is not defined
 Assert.assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_mul() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("set b 2");
 Main.processInstruction("mul a b");
 Assert.assertEquals(6, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_mul_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("mul a b"); // b is not defined
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_div() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 6");
 Main.processInstruction("set b 2");
 Main.processInstruction("div a b");
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_div_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 6");
 Main.processInstruction("div a b"); // b is not defined
 Assert.assertEquals(6, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_mod() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 6");
 Main.processInstruction("set b 2");
 Main.processInstruction("mod a b");
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 Assert.assertEquals(2, Variable.getVariableValue("a")); // a is modified to 0, but the previous value is still accessible
 }

 @Test
 public void testProcessInstruction_mod_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 6");
 Main.processInstruction("mod a b"); // b is not defined
 Assert.assertEquals(6, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_xor() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("set b 2");
 Main.processInstruction("xor a b");
 Assert.assertEquals(1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_xor_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("xor a b"); // b is not defined
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_or() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("set b 2");
 Main.processInstruction("or a b");
 Assert.assertEquals(4, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_or_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("or a b"); // b is not defined
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_and() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("set b 2");
 Main.processInstruction("and a b");
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_and_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("and a b"); // b is not defined
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_cmp() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("set b 2");
 Main.processInstruction("cmp a b");
 Assert.assertEquals(1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_cmp_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 3");
 Main.processInstruction("cmp a b"); // b is not defined
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 0");
 Main.processInstruction("jmp label1");
 Main.processInstruction("set b 2");
 Main.processInstruction("label1: mov a b");
 Assert.assertEquals(2, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jmp_with_invalid_label() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 0");
 Main.processInstruction("jmp invalidLabel");
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jnz() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 0");
 Main.processInstruction("jnz label2 1");
 Main.processInstruction("mov a 2");
 Main.processInstruction("label2: mov a 3");
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jz() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 0");
 Main.processInstruction("jz label3 1");
 Main.processInstruction("mov a 2");
 Main.processInstruction("label3:"); // jmp is not executed since a is 0
 Assert.assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_je() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 2");
 Main.processInstruction("set b 2");
 Main.processInstruction("je label4 a");
 Main.processInstruction("mov a 3");
 Main.processInstruction("label4: mov a 4");
 Assert.assertEquals(4, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_je_with_invalid_operands() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 2");
 Main.processInstruction("je label5 a"); // a is not defined in the label
 Main.processInstruction("mov a 3");
 Main.processInstruction("label5: mov a 4");
 Assert.assertEquals(3, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jne() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 2");
 Main.processInstruction("set b 3");
 Main.processInstruction("jne label6 a");
 Main.processInstruction("mov a 4");
 Main.processInstruction("label6: mov a 5");
 Assert.assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jmp_with_invalid_labels() {
 Main.resetInstructions();
 Main.loadInstructions(TEST_FILE_NAME);
 Main.processInstruction("set a 0");
 Main.processInstruction("jmp label7 label8"); // only one label is allowed
 Main.processInstruction("mov a 2");
 Main.processInstruction("label8: mov a 3");
 Main.processInstruction("label7: mov a 4");
 Assert.fail(); // this test should fail due to the invalid jmp instruction
 }
}
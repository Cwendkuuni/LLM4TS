import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import jipa.Main;
import jipa.Variable;

public class MainTest {

 private static String[] testInstructions = {
 "mov a 0",
 "inc a",
 "mov b a",
 "inc b",
 "mov a b",
 "dec a",
 "dec b",
 "mov a 10",
 "mov c a",
 "mov b 5",
 "add a b",
 "dec c",
 "jnz label1",
 "mov c 0",
 "label1: out a"
 };

 @BeforeClass
 public static void setup() {
 Main.loadInstructions("TestInstructions.txt");
 Main.iPtr = 0;
 Variable.resetVariables();
 }

 @Test
 public void testProcessInstruction_mov() {
 String opcode = "mov";
 String operand1 = "a";
 String operand2 = "0";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(0, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_inc() {
 String opcode = "inc";
 String operand = "a";
 Main.processInstruction(opcode + " " + operand);
 assertEquals(1, Variable.getVariableValue(operand));
 }

 @Test
 public void testProcessInstruction_movWithRegisters() {
 String opcode = "mov";
 String operand1 = "a";
 String operand2 = "b";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(0, Variable.getVariableValue(operand1));
 assertEquals(0, Variable.getVariableValue(operand2));
 Main.processInstruction(opcode + " " + operand2 + " " + operand1);
 assertEquals(0, Variable.getVariableValue(operand1));
 assertEquals(0, Variable.getVariableValue(operand2));
 Main.processInstruction(opcode + " " + operand1 + " " + operand1);
 assertEquals(0, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_jmp() {
 String opcode = "jmp";
 String label = "label1";
 Main.processInstruction(opcode + " " + label);
 Main.processInstruction("mov a 5");
 Main.processInstruction("jmp start");
 Main.processInstruction("label1: out a");
 Main.processInstruction("mov a 10");
 assertEquals(5, Variable.getVariableValue("a"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInstruction_invalidOperation() {
 String opcode = "invalid";
 Main.processInstruction(opcode);
 }

 @Test
 public void testProcessInstruction_add() {
 String opcode = "add";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(5, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_sub() {
 String opcode = "sub";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(-5, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_mul() {
 String opcode = "mul";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(25, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_div() {
 String opcode = "div";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(5, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_mod() {
 String opcode = "mod";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(0, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_xor() {
 String opcode = "xor";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(-6, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_or() {
 String opcode = "or";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(-1, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_and() {
 String opcode = "and";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(0, Variable.getVariableValue(operand1));
 }

 @Test
 public void testProcessInstruction_cmp() {
 String opcode = "cmp";
 String operand1 = "a";
 String operand2 = "5";
 Main.processInstruction(opcode + " 0 cmpResult");
 Main.processInstruction(opcode + " " + operand1 + " " + operand2);
 assertEquals(1, Variable.getVariableValue("cmpResult"));
 }

 @Test
 public void testProcessInstruction_jz() {
 String opcode = "jz";
 String label = "label1";
 Main.processInstruction(opcode + " 0 " + label);
 Main.processInstruction("mov a 0");
 Main.processInstruction("jmp start");
 Main.processInstruction(opcode + " 5 " + label);
 Main.processInstruction("out a");
 assertEquals(0, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jne() {
 String opcode = "jne";
 String label1 = "label1";
 String label2 = "label2";
 Main.processInstruction(opcode + " 0 " + label1);
 Main.processInstruction("mov a 1");
 Main.processInstruction("jmp start");
 Main.processInstruction(opcode + " 1 " + label2);
 Main.processInstruction("out a");
 assertEquals(1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_out() {
 String opcode = "out";
 Main.processInstruction(opcode);
 Main.processInstruction("mov a 10");
 assertEquals("10", System.console().readLine());
 }

 @Test
 public void testProcessInstruction_in() {
 String opcode = "in";
 String variable = "a";
 Main.processInstruction(opcode);
 Main.processInstruction("mov a 0");
 Main.processInstruction("in");
 Main.processInstruction("mov a 10");
 assertEquals(10, Variable.getVariableValue(variable));
 }
}
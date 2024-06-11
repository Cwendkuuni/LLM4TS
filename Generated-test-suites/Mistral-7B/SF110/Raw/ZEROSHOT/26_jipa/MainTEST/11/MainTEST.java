package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jipa.Main;
import jipa.Variable;

public class MainTest {

 private static final String TEST_FILE = "testfile.txt";
 private static final String[] TEST_INSTRUCTIONS = {
 "mov x 0",
 "set y 5",
 "inc x",
 "mov z x",
 "dec z",
 "add x y",
 "out x",
 "jmp 3"
 };

 @BeforeClass
 public static void setup() {
 Main.loadInstructions(TEST_FILE);
 }

 @Before
 public void reset() {
 Main.resetInstructions();
 Variable.reset();
 }

 @Test
 public void testLoadInstructions() {
 assertEquals(TEST_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
 for (int i = 0; i < TEST_INSTRUCTIONS.length; i++) {
 assertEquals(TEST_INSTRUCTIONS[i], Main.instruction[i]);
 }
 }

 @Test
 public void testProcessInstruction_mov() {
 Variable.setVariableValue("x", 0);
 Main.processInstruction("mov x y");
 assertEquals(0, Variable.getVariableValue("y"));
 }

 @Test
 public void testProcessInstruction_set() {
 Main.processInstruction("set x 5");
 assertEquals(5, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.processInstruction("inc x");
 assertEquals(6, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_dec() {
 Main.processInstruction("dec x");
 assertEquals(5, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_add() {
 Variable.setVariableValue("x", 5);
 Variable.setVariableValue("y", 2);
 Main.processInstruction("add x y");
 assertEquals(7, Variable.getVariableValue("x"));
 }

 @Test
 public void testProcessInstruction_out() {
 Variable.setVariableValue("x", 5);
 Main.processInstruction("out x");
 assertEquals("5", System.out.getHistory().get(System.out.getHistory().size() - 1));
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.processInstruction("jmp 2");
 assertEquals("mov x y", Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_jnz() {
 Variable.setVariableValue("x", 0);
 Main.processInstruction("jnz 2 5");
 assertEquals("set y 5", Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_ja() {
 Variable.setVariableValue("x", 2);
 Variable.setVariableValue("y", 1);
 Main.processInstruction("ja x y");
 assertEquals("mov z x", Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_jz() {
 Variable.setVariableValue("x", 0);
 Main.processInstruction("jz 2");
 assertEquals("mov z x", Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_je() {
 Variable.setVariableValue("x", 5);
 Variable.setVariableValue("y", 5);
 Main.processInstruction("je 2");
 assertEquals("out x", Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_jne() {
 Variable.setVariableValue("x", 5);
 Variable.setVariableValue("y", 6);
 Main.processInstruction("jne 2");
 assertEquals("mov z x", Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_cmp() {
 Variable.setVariableValue("x", 5);
 Variable.setVariableValue("y", 5);
 Main.processInstruction("cmp x y z");
 assertEquals(1, Variable.getVariableValue("z"));
 }

 @Test
 public void testProcessInstruction_arr() {
 Main.processInstruction("arr x 3");
 assertEquals(4, Main.instruction.length);
 assertTrue(Main.instruction[3].startsWith("var x0"));
 assertTrue(Main.instruction[4].startsWith("var x1"));
 assertTrue(Main.instruction[5].startsWith("var x2"));
 }

 @Test
 public void testProcessInstruction_var() {
 Main.processInstruction("var x3");
 assertEquals(5, Main.instruction.length);
 assertEquals("var x3", Main.instruction[4]);
 }

 @Test
 public void testProcessInstruction_In() {
 Main.processInstruction("in x");
 assertEquals(5, System.in.read());
 Variable.setVariableValue("x", 5);
 }
}
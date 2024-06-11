package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

 private static final String[] INPUT_FILE_NAMES = new String[] { "testInput1.jpa", "testInput2.jpa" };
 private static final String[] EXPECTED_OUTPUTS = new String[] { "Expected output 1", "Expected output 2" };

 @BeforeClass
 public static void setup() {
 Main.resetInstructions();
 }

 @Test
 public void testLoadInstructions() {
 for (String fileName : INPUT_FILE_Names) {
 Main.loadInstructions(fileName);
 assertEquals(INPUT_FILE_NAMES.length, Main.TOTAL_INSTRUCTIONS);
 }
 }

 @Test
 public void testProcessInstruction_halt() {
 Main.instruction = new String[] { "halt" };
 Main.processInstruction(Main.instruction[0]);
 assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testProcessInstruction_out() {
 Main.instruction = new String[] { "out", "'Hello, World!'" };
 Variable.clearVariableValues();
 Variable.setVariableValue("out", "OutPort");
 Main.processInstruction(Main.instruction[0]);
 assertEquals("Hello, World!", System.out.getHistogram().get(0).getData());
 }

 @Test
 public void testProcessInstruction_in() {
 Main.instruction = new String[] { "in", "inPort" };
 Variable.clearVariableValues();
 Variable.setVariableValue("inPort", "5");
 Main.processInstruction(Main.instruction[0]);
 assertEquals(5, Variable.getVariableValue("in"));
 }

 @Test
 public void testProcessInstruction_mov() {
 Main.instruction = new String[] { "mov", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 0);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(5, Variable.getVariableValue("B"));
 assertEquals(0, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_cpy() {
 Main.instruction = new String[] { "cpy", "A", "C" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("C", 0);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(5, Variable.getVariableValue("C"));
 }

 @Test
 public void testProcessInstruction_set() {
 Main.instruction = new String[] { "set", "A", "5" };
 Variable.clearVariableValues();
 Main.processInstruction(Main.instruction[0]);
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_del() {
 Main.instruction = new String[] { "del", "A" };
 Variable.setVariableValue("A", 5);
 Main.processInstruction(Main.instruction[0]);
 assertNull(Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_arr() {
 Main.instruction = new String[] { "arr", "arr1", "3" };
 Variable.clearVariableValues();
 Variable.setVariableValue("arr1", "arr1");
 Main.processInstruction(Main.instruction[0]);
 assertTrue(Variable.getVariables().containsKey("arr1[0]"));
 assertTrue(Variable.getVariables().containsKey("arr1[1]"));
 assertTrue(Variable.getVariables().containsKey("arr1[2]"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.instruction = new String[] { "inc", "A" };
 Variable.setVariableValue("A", 5);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(6, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_dec() {
 Main.instruction = new String[] { "dec", "A" };
 Variable.setVariableValue("A", 5);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(4, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_add() {
 Main.instruction = new String[] { "add", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(8, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_sub() {
 Main.instruction = new String[] { "sub", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(2, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_mul() {
 Main.instruction = new String[] { "mul", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(15, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_div() {
 Main.instruction = new String[] { "div", "A", "B" };
 Variable.setVariableValue("A", 10);
 Variable.setVariableValue("B", 2);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(5, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_mod() {
 Main.instruction = new String[] { "mod", "A", "B" };
 Variable.setVariableValue("A", 10);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(1, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_xor() {
 Main.instruction = new String[] { "xor", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(2, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_or() {
 Main.instruction = new String[] { "or", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(6, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_and() {
 Main.instruction = new String[] { "and", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 3);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(1, Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.instruction = new String[] { "jmp", "label1" };
 Label.setLabel("label1", 2);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(2, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jnz() {
 Main.instruction = new String[] { "jnz", "0", "label1" };
 Variable.setVariableValue("0", 5);
 Label.setLabel("label1", 2);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jz() {
 Main.instruction = new String[] { "jz", "0", "label1" };
 Variable.setVariableValue("0", 0);
 Label.setLabel("label1", 2);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(2, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_je() {
 Main.instruction = new String[] { "je", "A", "label1" };
 Variable.setVariableValue("A", 5);
 Label.setLabel("label1", 2);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_jne() {
 Main.instruction = new String[] { "jne", "A", "label1" };
 Variable.setVariableValue("A", 5);
 Label.setLabel("label1", 2);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(1, Main.iPtr);
 }

 @Test
 public void testProcessInstruction_cmp() {
 Main.instruction = new String[] { "cmp", "A", "B" };
 Variable.setVariableValue("A", 5);
 Variable.setVariableValue("B", 5);
 Main.processInstruction(Main.instruction[0]);
 assertEquals(0, Variable.getVariableValue("0"));
 Main.processInstruction(Main.instruction[0]);
 assertEquals(1, Variable.getVariableValue("0"));
 }

 @After
 public void tearDown() {
 Variable.clearVariableValues();
 }

 @AfterClass
 public static void teardown() {
 Main.resetInstructions();
 System.exit(0);
 }

 @Test
 public void testProcessInstruction_invalid() {
 Main.instruction = new String[] { "invalid", "" };
 assertThrows(RuntimeException.class, () -> Main.processInstruction(Main.instruction[0]));
 }
}
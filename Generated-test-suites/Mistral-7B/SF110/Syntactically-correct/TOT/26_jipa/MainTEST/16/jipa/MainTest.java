import org.junit.Assert;
import org.junit.Test;
import jipa.Main;
import static jipa.Main.opcodes.*;

public class MainTest {

 private static final String TEST_FILE = "testInstructions.txt";
 private static final String VALID_INSTRUCTION_FILE = "validInstructions.txt";
 private static final String INVALID_INSTRUCTION_FILE = "invalidInstructions.txt";
 private static final String VAR_1 = "var1";
 private static final String VAR_2 = "var2";
 private static final String LABEL_1 = "label1";
 private static final String LABEL_2 = "label2";

 @Test
 public void testLoadInstructions() {
 String fileName = TEST_FILE;
 Main.loadInstructions(fileName);
 Assert.assertEquals(Main.TOTAL_INSTRUCTIONS, 10); // Replace with the actual number of instructions in the test file
 }

 @Test
 public void testProcessInstruction_out() {
 String[] instructions = { "out 5" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals(System.out.size(), 1);
 Assert.assertEquals(System.out.getHistory().get(0).getDiagnosticPortText(), "5");
 }

 @Test
 public void testProcessInstruction_in() {
 String[] instructions = { "in var1" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction("mov var1 0"); // Reset var1 for next tests
 Main.processInstruction("inc var1");
 Main.processInstruction("inc var1");
 Main.processInstruction("out " + VAR_1);
 Assert.assertEquals(System.out.getHistory().get(0).getDiagnosticPortText(), "3");
 }

 @Test
 public void testProcessInstruction_mov() {
 String[] instructions = { "mov var1 var2" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals(Variable.getVariableValue(VAR_2), Variable.getVariableValue(VAR_1));
 }

 @Test
 public void testProcessInstruction_var() {
 String[] instructions = { "var var1" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertEquals(Variable.getVariableValue(VAR_1), 0);
 }

 @Test
 public void testProcessInstruction_del() {
 String[] instructions = { "del var1" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Assert.assertFalse(Variable.containsKey(VAR_1));
 }

 @Test
 public void testProcessInstruction_arr() {
 String[] instructions = { "arr var1 3" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction("var var2");
 Main.processInstruction("mov var2 var1");
 Main.processInstruction("mov var1 1");
 Main.processInstruction("mov var1 1");
 Main.processInstruction("mov var1 1");
 Main.processInstruction("mov var1 1");
 Main.processInstruction("mov var2 0");
 Main.processInstruction("mov var2 7"); // Index out of bounds
 Main.processInstruction("mov var2 var1");
 Main.processInstruction("out " + VAR_2);
 Assert.assertEquals(System.out.getHistory().get(0).getDiagnosticPortText(), "10");
 }

 @Test
 public void testProcessInstruction_inc() {
 String[] instructions = { "inc var1" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction("mov var1 5");
 Main.processInstruction("inc var1");
 Main.processInstruction("inc var1");
 Main.processInstruction("out " + VAR_1);
 Assert.assertEquals(System.out.getHistory().get(0).getDiagnosticPortText(), "7");
 }

 @Test
 public void testProcessInstruction_add() {
 String[] instructions = { "mov var1 2", "mov var2 3", "add var3 var1 var2" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction("out " + VAR_3);
 Assert.assertEquals(System.out.getHistory().get(0).getDiagnosticPortText(), "5");
 }

 @Test
 public void testLoadInstructions_validFile() {
 Main.loadInstructions(VALID_INSTRUCTION_FILE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadInstructions_invalidFile() {
 Main.loadInstructions(INVALID_INSTRUCTION_FILE);
 }

 @Test
 public void testProcessInstruction_invalidOpcode() {
 String[] instructions = { "invalidopcode var1" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_invalidRegister() {
 String[] instructions = { "mov var1 invalidregister" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_invalidNumber() {
 String[] instructions = { "mov var1 invalidnumber" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_invalidLabel() {
 String[] instructions = { "jmp invalidlabel" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_labelNotFound() {
 String[] instructions = { "jmp label3" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 }

 @Test
 public void testProcessInstruction_jumpToLabel() {
 String[] instructions = { "mov var1 0", "label1:", "jmp label1", "label2:" };
 Main.instruction = instructions;
 Main.iPtr = 0;
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction(Main.instruction[Main.iPtr]);
 Main.processInstruction("out " + VAR_1); // Should loop indefinitely
 }
}
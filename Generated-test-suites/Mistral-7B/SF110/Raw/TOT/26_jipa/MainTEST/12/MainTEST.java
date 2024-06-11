import org.junit.Assert;
import org.junit.Test;
import jipa.Main;

public class MainTest {

 private static final String FILENAME = "testInstructions.txt";
 private static final String[] VALID_INSTRUCTIONS = {
 "mov A 0",
 "inc A",
 "dec A",
 "mov B A",
 "inc B",
 "mov A B",
 "jmp label1",
 "label1: out A"
 };

 @Test
 public void testLoadInstructions() {
 Main.loadInstructions(FILENAME);
 Assert.assertEquals(VALID_INSTRUCTIONS.length, Main.TOTAL_INSTRUCTIONS);
 }

 @Test
 public void testProcessInstruction_mov() {
 Main.iPtr = 0;
 Main.processInstruction("mov A 0");
 Main.processInstruction("inc A");
 Assert.assertEquals(1, Main.Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_inc() {
 Main.iPtr = 0;
 Main.processInstruction("mov A 0");
 Main.processInstruction("inc A");
 Main.processInstruction("dec A");
 Assert.assertEquals(0, Main.Variable.getVariableValue("A"));
 }

 @Test
 public void testProcessInstruction_out() {
 Main.iPtr = 0;
 Main.loadInstructions(FILENAME);
 Main.processInstruction("out A");
 Assert.assertEquals("1\n", System.out.getHistogram().toString()); // Assuming System.out has a getHistogram() method to capture the output
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInstruction_invalidInstruction() {
 Main.iPtr = 0;
 Main.loadInstructions(FILENAME);
 Main.processInstruction("invalidInstruction");
 }

 @Test
 public void testProcessInstruction_jmp() {
 Main.iPtr = 0;
 Main.loadInstructions(FILENAME);
 Main.processInstruction("jmp label1");
 Main.processInstruction("mov A 1");
 Main.processInstruction("label1: out A");
 Main.processInstruction("mov A 2");
 Main.processInstruction("out A");
 Assert.assertEquals("1\n2\n", System.out.getHistogram().toString());
 }
}
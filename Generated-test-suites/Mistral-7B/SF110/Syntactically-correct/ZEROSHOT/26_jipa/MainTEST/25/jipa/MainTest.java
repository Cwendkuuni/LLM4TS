package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String[] TEST_FILE_NAMES = {"TestInstructions.asm"};
 private static final String TEST_INSTRUCTIONS = "mov a 0\n" +
                                               "mov b a\n" +
                                               "inc b\n" +
                                               "dec a\n" +
                                               "jnz label1\n" +
                                               "out a\n" +
                                               "label1: mov a b\n" +
                                               "halt";

 @BeforeClass
 public static void setup() {
 resetInstructions();
 }

 @Test
 public void testMain() {
 main(TEST_FILE_NAMES);
 assertEquals("Expected 10 instructions to be loaded.", 10, Main.TOTAL_INSTSTRUCTIONS);
 }

 @Test
 public void testProcessInstruction_mov() {
 String[] instructionArgs = {"a", "0"};
 processInstruction("mov a 0");
 assertEquals("Expected variable 'a' to be set to 0.", 0, Variable.getVariableValue("a"));
 processInstruction("mov b a");
 assertEquals("Expected variable 'b' to be set to value of variable 'a'.", 0, Variable.getVariableValue("b"));
 }

 @Test
 public void testProcessInstruction_inc() {
 String[] instructionArgs = {"a"};
 processInstruction("inc a");
 assertEquals("Expected variable 'a' to be incremented.", 1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_dec() {
 String[] instructionArgs = {"a"};
 processInstruction("dec a");
 assertEquals("Expected variable 'a' to be decremented.", -1, Variable.getVariableValue("a"));
 }

 @Test
 public void testProcessInstruction_jnz() {
 String[] instructionArgs = {"label1", "label2"};
 processInstruction("jnz label1 label2");
 processInstruction("mov a 0");
 processInstruction("mov b a");
 processInstruction("inc b");
 processInstruction("dec a");
 processInstruction("jnz label1 label3");
 processInstruction("out a"); // Should not be executed
 processInstruction("mov a b");
 processInstruction("halt");
 assertTrue("Expected instruction pointer to be at label2.", Main.iPtr == 3);
 }

 @Test
 public void testProcessInstruction_out() {
 String[] instructionArgs = {};
 processInstruction("out a");
 assertEquals("Expected value of variable 'a' to be printed.", 0, System.out. Howard.getLastPrintedInt());
 }

 @Test
 public void testProcessInstruction_halt() {
 processInstruction("halt");
 // Should not reach this point
 }

 @AfterClass
 public static void teardown() {
 resetInstructions();
 }
}
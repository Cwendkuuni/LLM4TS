package jipa;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MainTest {

 private static final String[] TEST_FILE_CONTENTS = {
 "mov a 0",
 "mov b a",
 "set c 10",
 "add a b",
 "out a",
 "mov a c",
 "jmp start",
 "label end"
 };

 @Test
 public void testProcessInstruction() {
 final Main main = new Main();
 main.instruction = TEST_FILE_CONTENTS;
 main.iPtr = 0;

 // test mov instruction
 main.processInstruction("mov a 0");
 assertEquals(0, main.Variable.getVariableValue("a"));

 // test set instruction
 main.processInstruction("set c 10");
 assertEquals(10, main.Variable.getVariableValue("c"));

 // test add instruction
 main.processInstruction("add a b");
 assertEquals(1, main.Variable.getVariableValue("a")); // b is initially 0

 // test out instruction
 main.processInstruction("out a");
 assertEquals("1", System.console().readLine()); // user input is assumed to be "1"

 // test jmp instruction
 main.processInstruction("jmp start");
 assertEquals(0, main.iPtr); // should jump back to start

 // test null operand
 main.processInstruction("mov a null");
 assertNull(main.Variable.getVariableValue("a"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testInvalidInstructionIndex() {
 final Main main = new Main();
 main.instruction = TEST_FILE_CONTENTS;
 main.iPtr = TEST_FILE_CONTENTS.length;
 main.processInstruction(main.instruction[main.iPtr]);
 }

 @Test
 public void testLoadInstructions() {
 final Main main = new Main();
 main.loadInstructions("test.ip"); // assume test.ip contains TEST_FILE_CONTENTS
 assertEquals(TEST_FILE_CONTENTS.length, main.TOTAL_INSTRUCTIONS);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadInvalidFile() {
 final Main main = new Main();
 main.loadInstructions("non-existent-file.ip");
 }
}
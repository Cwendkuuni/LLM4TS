package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

 private static final String TEST_FILE = "testFile.txt";
 private static final String[] TEST_INSTRUCTIONS = new String[] {
 "mov a 0",
 "mov b a",
 "inc b",
 "mov c a",
 "inc c",
 "inc c",
 "dec c",
 "jmp loop",
 "out b",
 "halt"
 };

 @Before
 public void setUp() {
 Main.loadInstructions(TEST_FILE);
 }

 @After
 public void tearDown() {
 Main.resetInstructions();
 }

 @Test
 public void testProcessInstruction() {
 Main.iPtr = 0;
 Variable.initVariables();
 for (String instruction : TEST_INSTRUCTIONS) {
 Main.processInstruction(instruction);
 assertEquals(expectNextValue(instruction), Variable.getVariableValue("a"));
 assertEquals(expectNextValue(instruction), Variable.getVariableValue("b"));
 assertEquals(expectNextValue(instruction), Variable.getVariableValue("c"));
 }
 }

 private int expectNextValue(String instruction) {
 switch (opcodes.toOpcode(instruction.substring(0, instruction.indexOf(32)))) {
 case mov:
 return Integer.parseInt(instruction.substring(instruction.indexOf(32) + 1).split(" ")[0]);
 case inc:
 case dec:
 return Variable.getVariableValue(instruction.substring(0, instruction.indexOf(32)).split(" ")[0]) + 1;
 case out:
 case halt:
 return -1;
 default:
 throw new IllegalStateException("Unexpected opcode: " + instruction);
 }
 }
}
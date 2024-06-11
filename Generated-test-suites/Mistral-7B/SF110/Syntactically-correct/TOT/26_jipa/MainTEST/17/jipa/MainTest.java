package jipa;

import org.junit.Assert;
import org.junit.Test;

import static jipa.opcodes.opcodes.*;

public class MainTest {

 private static final String[] instructions = {
 "mov a 0",
 "mov b a",
 "inc a",
 "dec b",
 "add a b",
 "sub a b",
 "mul a b",
 "div a b",
 "mod a b",
 "xor a b",
 "or a b",
 "and a b",
 "mov c a",
 "jmp 10",
 "set a 10",
 "mov d a",
 "jnz 15",
 "mov e 5",
 "jz 20",
 "je a b",
 "jne a b",
 "mov f 1",
 "mov g a",
 "mov h 2",
 "mov i a",
 "mov j a",
 "mov k a",
 "mov l a",
 "mov m a",
 "mov n a",
 "mov o a",
 "mov p a",
 "mov q a",
 "mov r a",
 "mov s a",
 "mov t a",
 "mov u a",
 "mov v a",
 "mov w a",
 "mov x a",
 "mov y a",
 "mov z a",
 "mov a 0",
 "jmp 0" // this should cause an error
 };

 @Test
 public void testMain() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester(instructions);
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidInstruction() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester(new String[]{"mov a 0", "mov a z"}); // invalid register name
 tester.test();
 } catch (Throwable e) {
 Assert.assertEquals("Invalid instruction: mov a z", e.getMessage());
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testLoadInstructionsFile() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester("test.txt");
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadInstructionsFileNotFound() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester("nonexistentfile.txt");
 tester.test();
 } catch (Throwable e) {
 Assert.assertEquals("File not found", e.getMessage());
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testEmptyInstructions() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester(new String[]{});
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testInvalidInstructionFormat() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester(new String[]{"mov a 0", "mov a"}); // missing operation or operand
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testInvalidOperand() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester(new String[]{"mov a z", "mov a 123"}); // invalid register name or non-numeric value
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testInvalidLabel() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 Tester tester = new Tester(new String[]{"mov a 0", "mov a 10:, 0"}); // invalid label format
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testJumpToInvalidLabel() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 String[] instructions = {"mov a 0", "mov b 0", "jmp invalidLabel"};
 Tester tester = new Tester(instructions);
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testJumpToSelf() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 String[] instructions = {"mov a 0", "mov b 0", "jmp a"};
 Tester tester = new Tester(instructions);
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testJumpToLabelAfterInstruction() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 String[] instructions = {"mov a 0", "mov b 0", "mov c a", "jmp label"};
 Tester tester = new Tester(instructions);
 tester.test();
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testVariableInitialization() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 String[] instructions = {"set a 1", "mov b a", "out b", "mov c a", "inc a", "out c", "mov d a", "inc a", "out d"};
 Tester tester = new Tester(instructions);
 tester.test();
 String expectedOutput = "1\n2\n3\n4\n5\n";
 Assert.assertEquals(expectedOutput, System.out.getHistogram().getContents());
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }

 @Test
 public void testVariableInitializationWithLabels() {
 FinalizableThreadGroup group = new FinalizableThreadGroup("TestGroup");
 Thread thread = new Thread(group, "TestThread");
 try {
 thread.start();
 String[] instructions = {"set a 1", "mov b a", "jmp start", "out b", "mov c a", "inc a", "out c", "mov d a", "inc a", "out d", "jmp end", "halt"};
 String[] labels = {"start:", "end:"};
 Tester tester = new Tester(instructions, labels);
 tester.test();
 String expectedOutput = "1\n2\n3\n4\n5\n";
 Assert.assertEquals(expectedOutput, System.out.getHistogram().getContents());
 } catch (Throwable e) {
 e.printStackTrace();
 } finally {
 group.tearDown();
 }
 }
}
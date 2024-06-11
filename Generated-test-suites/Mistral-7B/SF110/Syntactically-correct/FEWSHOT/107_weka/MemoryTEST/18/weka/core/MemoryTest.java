package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory mem;
 final long initMemory;
 final long maxMemory;

 @Before
 public void setup() {
 mem = new Memory();
 initMemory = mem.getInitial();
 maxMemory = mem.getMax();
 }

 @Test
 public void testMemoryInitialization() {
 assertEquals("Initial memory is not correctly initialized", initMemory, mem.getInitial());
 assertEquals("Max memory is not correctly initialized", maxMemory, mem.getMax());
 }

 @Test
 public void testMemoryEnabled() {
 Memory.setEnabled(false);
 assertFalse("Memory should be disabled", Memory.isEnabled());
 Memory.setEnabled(true);
 assertTrue("Memory should be enabled", Memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 assertTrue("Memory should use GUI when initialized with true", memoryWithGUI.getUseGUI());
 Memory memoryWithoutGUI = new Memory();
 assertFalse("Memory should not use GUI when initialized with false", memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetCurrent() {
 long currentMemory = mem.getCurrent();
 long expectedMemory = Runtime.getRuntime().totalMemory();
 assertEquals("Current memory is not correct", expectedMemory, currentMemory);
 }

 @Test
 public void testIsOutOfMemory() {
 long memoryToConsume = 5000000;
 mem.setEnabled(true);
 for (int i = 0; i < 10; i++) {
 Object largeObject = new byte[memoryToConsume];
 // This statement will cause JVM to run the garbage collector
 System.gc();
 }
 assertTrue("Memory is out of memory", mem.isOutOfMemory());
 mem.setEnabled(false);
 assertFalse("Memory is not out of memory anymore", mem.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memWithGUI = new Memory(true);
 memWithGUI.setEnabled(true);
 // This statement will cause JVM to run the garbage collector
 System.gc();
 memWithGUI.showOutOfMemory();
 // Verify that the message dialog was shown
 JOptionPane.showMessageDialog = new StubMessageDialog(); // Replace this with a proper stub or mock library
 verify(StubMessageDialog.instance, times(1)).showMessageDialog(eq(null), eq("OutOfMemory"), eq(JOptionPane.PLAIN_MESSAGE), eq(JOptionPane.ERROR_MESSAGE));
 }

 @Test
 public void testShowOutOfMemoryConsole() {
 Memory memWithoutGUI = new Memory();
 memWithoutGUI.setEnabled(true);
 // This statement will cause JVM to run the garbage collector
 System.gc();
 String expectedConsoleMessage = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:    ";
 String consoleOutput = System.err.getErrorStream().getText();
 assertTrue("Console output is not as expected", consoleOutput.contains(expectedConsoleMessage));
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 Thread[] threads = new Thread[5];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread() {
 public void run() {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
 };
 threads[i].start();
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertTrue("Thread did not stop", !thread.isAlive());
 }
 }

 @Test
 public void testGetRevision() {
 String expectedRevision = "8034";
 assertEquals("Incorrect revision number", expectedRevision, mem.getRevision());
 }

 @After
 public void teardown() {
 mem = null;
 }
}

 class StubMessageDialog {
 private static StubMessageDialog instance;

 public static StubMessageDialog instance() {
 if (instance == null) {
 instance = new StubMessageDialog();
 }
 return instance;
 }

 public void showMessageDialog(Frame owner, Object message, int messageType, int optionType) {
 // Do nothing
 }
}
package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

 // Test constructor with default arguments
 @Test
 public void testMemoryDefaultConstructor() {
 Memory memory = new Memory();
 assertEquals(false, memory.isEnabled());
 assertEquals(false, memory.getUseGUI());
 assertEquals(0, memory.getInitial());
 assertEquals(0, memory.getCurrent());
 assertEquals(0, memory.getMax());
 assertFalse(memory.isOutOfMemory());
 }

 // Test constructor with useGUI argument
 @Test
 public void testMemoryConstructorWithUseGUI() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 // Test getters and setters for useGUI
 @Test
 public void testGettersAndSettersForUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory.setUseGUI(true);
 assertTrue(memory.getUseGUI());
 }

 // Test getInitial() with initial memory set in static block
 @Test
 public void testGetInitialWithStaticInitialization() {
 Memory memory = new Memory();
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 }

 // Test getInitial() with manual initialization
 @Test
 public void testGetInitialWithManualInitialization() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setInitial(Runtime.getRuntime().totalMemory());
 assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
 }

 // Test getCurrent()
 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long initial = Runtime.getRuntime().totalMemory();
 memory.setEnabled(true);
 memory.getCurrent(); // Trigger memory update
 assertEquals(initial, memory.getCurrent());
 }

 // Test getMax()
 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = Runtime.getRuntime().maxMemory();
 assertEquals(max, memory.getMax());
 }

 // Test isOutOfMemory() with initial memory set in static block
 @Test
 public void testIsOutOfMemoryWithStaticInitialization() {
 Memory memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 }

 // Test isOutOfMemory() with manual initialization and not enough memory
 @Test(expected = RuntimeException.class)
 public void testIsOutOfMemoryWithManualInitializationAndNotEnoughMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setInitial(Runtime.getRuntime().totalMemory() - 1000000); // Set initial memory below current memory
 memory.setMax(Runtime.getRuntime().maxMemory());
 assertTrue(memory.isOutOfMemory());
 }

 // Test showOutOfMemory() with useGUI = false
 @Test
 public void testShowOutOfMemoryWithUseGUIFalse() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 String expectedMessage = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   0.0MB (0)\n- total memory used:   " + Memory.toMegaByte(Runtime.getRuntime().totalMemory()) + "MB\n- max. memory avail.: " + Memory.toMegaByte(Runtime.getRuntime().maxMemory()) + "MB\n\nNote:\nThe Java heap size can be specified with the -Xmx option.\nE.g., to use 128MB as heap size, the command line looks like this:\n   java -Xmx128m -classpath ...\nThis does NOT work in the SimpleCLI, the java command refers\nto the one with which Weka is started.";
 String actualMessage = "";
 try {
 System.setErr(new PrintStream(new ByteArrayOutputStream()));
 memory.showOutOfMemory();
 } finally {
 System.setErr(System.out);
 try (ByteArrayInputStream input = new ByteArrayInputStream(new byte[0])) {
 String line = null;
 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 while ((line = reader.readLine()) != null) {
 actualMessage += line + "\n";
 }
 }
 }
 assertEquals(expectedMessage, actualMessage);
 }

 // Test showOutOfMemory() with useGUI = true
 @Test
 public void testShowOutOfMemoryWithUseGUITrue() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setUseGUI(true);
 memory.showOutOfMemory();
 }
}
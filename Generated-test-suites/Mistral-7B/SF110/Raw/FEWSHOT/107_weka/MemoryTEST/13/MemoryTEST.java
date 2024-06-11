package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory testMemory;
 long initialMemory;
 long maxMemory;

 @Test
 public void testMemory() {
 testMemory = new Memory();
 initialMemory = testMemory.getInitial();
 maxMemory = testMemory.getMax();

 assertEquals("Initial memory is not set correctly.", initialMemory, testMemory.getInitial());
 assertEquals("Max memory is not set correctly.", maxMemory, testMemory.getMax());
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue("Memory is not enabled by default.", Memory.m_Enabled);

 Memory.m_Enabled = false;
 assertFalse("Memory is still enabled.", Memory.m_Enabled);
 }

 @Test
 public void testSetEnabled() {
 Memory.m_Enabled = false;
 testMemory.setEnabled(true);
 assertTrue("Memory is not enabled after setEnabled() call.", Memory.m_Enabled);
 }

 @Test
 public void testGetUseGUI() {
 testMemory = new Memory(true);
 assertTrue("UseGUI is not set correctly.", testMemory.getUseGUI());

 testMemory = new Memory(false);
 assertFalse("UseGUI is still set.", testMemory.getUseGUI());
 }

 @Test
 public void testGetCurrent() {
 testMemory = new Memory();
 long currentMemory = testMemory.getCurrent();

 assertEquals("Current memory is not updated correctly.", testMemory.getInitial(), currentMemory);

 testMemory.getCurrent(); // Force update
 long currentMemory2 = testMemory.getCurrent();

 assertEquals("Current memory is not updated correctly on second call.", testMemory.getTotal(), currentMemory2);
 }

 @Test
 public void testIsOutOfMemory() {
 testMemory = new Memory();
 testMemory.setEnabled(true);
 long initialMemory = testMemory.getInitial();
 long maxMemory = testMemory.getMax();
 long currentMemory = testMemory.getCurrent();
 long initialFreeMemory = maxMemory - currentMemory;

 assertFalse("Memory is out of memory initially.", testMemory.isOutOfMemory());

 testMemory.setTotal(initialMemory + 200000);
 assertTrue("Memory is not out of memory after setting total memory.", testMemory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 testMemory = new Memory();
 testMemory.setEnabled(true);
 testMemory.setUseGUI(true);

 long initialMemory = testMemory.getInitial();
 long maxMemory = testMemory.getMax();
 long currentMemory = testMemory.getCurrent();
 long initialFreeMemory = maxMemory - currentMemory;

 testMemory.setTotal(initialMemory + 200000);
 testMemory.showOutOfMemory();

 String expectedMessage = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:    " + Memory.toMegaByte(initialMemory) + "MB\n" + "- total memory used:  " + Memory.toMegaByte(currentMemory) + "MB\n" + "- max. memory avail.: " + Memory.toMegaByte(maxMemory) + "MB\n" + "\n" + "Note:\n" + "The Java heap size can be specified with the -Xmx option.\n" + "E.g., to use 128MB as heap size, the command line looks like this:\n" + "   java -Xmx128m -classpath ...\n" + "This does NOT work in the SimpleCLI, the java command refers\n" + "to the one with which Weka is started.";

 String actualMessage = "";
 try {
 System.out.flush();
 String line = System.in.readLine();
 while (line != null) {
 actualMessage += line;
 line = System.in.readLine();
 }
 } catch (Exception e) {
 // Swallow exception
 }

 assertEquals("Unexpected message displayed in showOutOfMemory() method.", expectedMessage, actualMessage);
 }

 @Test
 public void testStopThreads() {
 testMemory = new Memory();
 Thread[] threads = new Thread[10];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }

 testMemory.stopThreads();
 for (Thread thread : threads) {
 assertTrue("Thread " + thread.getName() + " is still running.", !thread.isAlive());
 }
 }

 @Test
 public void testGetRevision() {
 String revision = testMemory.getRevision();
 assertNotNull("getRevision() method should not return null.", revision);
 }

 @Test
 public void testMain() {
 Memory mem = new Memory();
 String output = "";
 try {
 OutputStream os = new ByteArrayOutputStream();
 PrintStream ps = new PrintStream(os);
 Memory.main(new String[]{});
 output = os.toString();
 } catch (Exception e) {
 // Swallow exception
 }

 String expectedOutput = "Initial memory: " + Memory.toMegaByte(mem.getInitial()) + "MB (" + mem.getInitial() + ")\n" + "Max memory: " + Memory.toMegaByte(mem.getMax()) + "MB (" + mem.getMax() + ")";

 assertEquals("Unexpected output in main() method.", expectedOutput, output);
 }
}
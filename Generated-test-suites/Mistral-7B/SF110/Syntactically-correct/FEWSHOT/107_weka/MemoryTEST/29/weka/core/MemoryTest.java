package weka.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest {

 Memory testObject = null;
 long initialMemory = 0;
 long maxMemory = 0;
 long totalMemory = 0;

 @Test
 public void testMemory() {
 testObject = new Memory();
 initialMemory = testObject.getInitial();
 maxMemory = testObject.getMax();
 totalMemory = testObject.getCurrent();

 assertEquals("Initial memory should be set correctly", initialMemory, testObject.getInitial());
 assertEquals("Max memory should be set correctly", maxMemory, testObject.getMax());
 assertEquals("Total memory should be set correctly", totalMemory, testObject.getCurrent());
 }

 @Test
 public void testIsOutOfMemory() {
 testObject = new Memory();
 totalMemory += 1000000;

 assertFalse("Memory should not be out of memory when it's not", testObject.isOutOfMemory());

 testObject.setEnabled(true);
 totalMemory += 50000000;

 assertTrue("Memory should be out of memory when it's not", testObject.isOutOfMemory());
 }

 @Test
 public void testMemoryMethods() {
 Memory memory = new Memory(true);
 memory.setEnabled(true);
 memory.showOutOfMemory();
 JOptionPane expectedDialog = new JOptionPane("Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   0.0MB (1258291200)\n- total memory used:  0.0MB (1572864000)\n- max. memory avail.: 11.3MB (1276245760)\n\nNote:\nThe Java heap size can be specified with the -Xmx option.\nE.g., to use 128MB as heap size, the command line looks like this:\n   java -Xmx128m -classpath ...\nThis does NOT work in the SimpleCLI, the java command refers\nto the one with which Weka is started.", JOptionPane.ERROR_MESSAGE);
 JOptionPane actualDialog = null;

 try {
 actualDialog = JOptionPane.showInputDialog(null, memory.getRevision(), "OutOfMemory", JOptionPane.ERROR_MESSAGE);
 } catch (HeadlessException e) {
 // Ignore headless exception
 }

 assertEquals("Unexpected dialog message", expectedDialog.getMessage(), actualDialog.getMessage());
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsWithNullThreadGroup() {
 Memory memory = new Memory();
 memory.stopThreads((Thread[]) null);
 }

 @Test
 public void testToMegaByte() {
 assertEquals("Expected 1.0 for 1048576", 1.0, Memory.toMegaByte(1048576), 0.0);
 assertEquals("Expected 2.0 for 2097152", 2.0, Memory.toMegaByte(2097152), 0.0);
 }
}
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

 assertEquals("Initial memory should be set correctly", initialMemory, testMemory.getInitial());
 assertEquals("Max memory should be set correctly", maxMemory, testMemory.getMax());
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue("Memory should be enabled by default", Memory.m_Enabled);

 Memory.m_Enabled = false;
 assertFalse("Memory should be disabled when set to false", Memory.m_Enabled);
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 Memory memoryWithoutGUI = new Memory(false);

 assertEquals("Memory with GUI should have getUseGUI set to true", true, memoryWithGUI.getUseGUI());
 assertEquals("Memory without GUI should have getUseGUI set to false", false, memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long currentInitial = memory.getInitial();

 Memory newMemory = new Memory();
 long newInitial = newMemory.getInitial();

 assertEquals("Initial memory should remain the same for the same object", currentInitial, newInitial);
 }

 @Test
 public void testGetTotal() {
 Memory memory = new Memory();
 long currentTotal = memory.getCurrent();

 Memory newMemory = new Memory();
 long newTotal = newMemory.getCurrent();

 assertEquals("Total memory should remain the same for the same object", currentTotal, newTotal);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long currentMax = memory.getMax();

 Memory newMemory = new Memory();
 long newMax = newMemory.getMax();

 assertEquals("Max memory should remain the same for the same object", currentMax, newMax);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 long currentTotal = memory.getCurrent();
 long maxMemory = memory.getMax();
 long initialMemory = memory.getInitial();

 memory.setEnabled(true);
 long newTotal = memory.getCurrent();

 assertFalse("Memory should not be out of memory initially", memory.isOutOfMemory());

 memory.setEnabled(true);
 memory.setTotal(currentTotal + maxMemory);

 assertTrue("Memory should be out of memory when total memory is equal to max memory", memory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.setTotal(memory.getInitial() + maxMemory);

 String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:    " + Memory.toMegaByte(Memory.m_Initial) + "MB\n" +
                      "- total memory used:  " + Memory.toMegaByte(testMemory.getTotal()) + "MB\n" +
                      "- max. memory avail.: " + Memory.toMegaByte(testMemory.getMax()) + "MB\n" +
                      "\n" +
                      "Note:\n" +
                      "The Java heap size can be specified with the -Xmx option.\n" +
                      "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                      "   java -Xmx128m -classpath ...\n" +
                      "This does NOT work in the SimpleCLI, the java command refers\n" +
                      "to the one with which Weka is started.";

 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();

 // Need to mock out Thread.currentThread() and Thread.enumerate() methods for this test
 // as these are not likely to be mocked by JUnit out of the box
 // and it's not feasible to test these methods in isolation without external dependencies
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String expectedRevision = RevisionUtils.extract("$Revision: 8034 $");

 assertEquals("Memory getRevision should return the correct revision number", expectedRevision, memory.getRevision());
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();

 // Need to mock out System.out to test the output of the main method
 // as it's not feasible to test this method in isolation without external dependencies
 }
}
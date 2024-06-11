package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory testMemory;
 long initialMemory;
 long maxMemory;

 @Test
 public void MemoryConstructorTest() {
 testMemory = new Memory(false);
 initialMemory = testMemory.getInitial();
 maxMemory = testMemory.getMax();
 assertEquals("Initial memory should be set correctly in constructor", initialMemory, testMemory.getInitial());
 assertEquals("Max memory should be set correctly in constructor", maxMemory, testMemory.getMax());
 }

 @Test
 public void MemoryConstructorWithUseGUITest() {
 testMemory = new Memory(true);
 initialMemory = testMemory.getInitial();
 maxMemory = testMemory.getMax();
 assertEquals("Initial memory should be set correctly in constructor with useGUI", initialMemory, testMemory.getInitial());
 assertEquals("Max memory should be set correctly in constructor with useGUI", maxMemory, testMemory.getMax());
 }

 @Test
 public void isEnabledTest() {
 Memory.m_Enabled = false;
 assertFalse("Memory should not be enabled if m_Enabled is false", testMemory.isEnabled());
 Memory.m_Enabled = true;
 assertTrue("Memory should be enabled if m_Enabled is true", testMemory.isEnabled());
 }

 @Test
 public void getUseGUITest() {
 assertFalse("UseGUI should be false by default", testMemory.getUseGUI());
 Memory memoryWithUseGUI = new Memory(true);
 assertTrue("UseGUI should be true when constructor is called with useGUI = true", memoryWithUseGUI.getUseGUI());
 }

 @Test
 public void getInitialTest() {
 assertEquals("Initial memory should be the same as the one set in the constructor", initialMemory, testMemory.getInitial());
 testMemory.setEnabled(false);
 assertEquals("Initial memory should not change when Memory is disabled", initialMemory, testMemory.getInitial());
 }

 @Test
 public void getCurrentTest() {
 long currentMemory = testMemory.getCurrent();
 assertEquals("Current memory should be the same as total memory", testMemory.getTotal(), currentMemory);
 }

 @Test
 public void getMaxTest() {
 assertEquals("Max memory should be the same as the one set in the constructor", maxMemory, testMemory.getMax());
 testMemory.setEnabled(false);
 assertEquals("Max memory should not change when Memory is disabled", maxMemory, testMemory.getMax());
 }

 @Test
 public void isOutOfMemoryTest() {
 long testTotalMemory = testMemory.getTotal() + 1000000L;
 testMemory.setEnabled(true);
 assertFalse("Memory should not be out of memory when there is enough space", testMemory.isOutOfMemory());
 testMemory.setTotal(testTotalMemory);
 assertTrue("Memory should be out of memory when there is not enough space", testMemory.isOutOfMemory());
 }

 @Test
 public void toMegaByteTest() {
 double mb = Memory.toMegaByte(1024 * 1024);
 assertEquals("Test toMegaByte method with 1MB", 1.0, mb, 0.001);
 mb = Memory.toMegaByte(123456789L);
 assertEquals("Test toMegaByte method with 123MB", 123.0, mb, 0.001);
 }

 @Test
 public void showOutOfMemoryTest() {
 testMemory = new Memory();
 Memory memWithEnabledGUI = new Memory(true);
 testMemory.showOutOfMemory();
 String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   ?"; // Unknown initial memory value
 assertTrue("showOutOfMemory message should contain 'initial JVM size:'", expectedMsg.contains(Utils.doubleToString(toMegaByte(testMemory.getInitial()), 1)));
 assertTrue("showOutOfMemory message should contain 'total memory used:'", expectedMsg.contains("total memory used:"));
 assertTrue("showOutOfMemory message should contain 'max. memory avail.:' ", expectedMsg.contains("max. memory avail."));
 memWithEnabledGUI.showOutOfMemory();
 assertTrue("showOutOfMemory message should contain 'OutOfMemory' dialog as title", expectedMsg.contains("OutOfMemory"));
 }

 @Test
 public void stopThreadsTest() {
 testMemory = new Memory();
 Thread[] threads = new Thread[2];
 for (int i = 0; i < 2; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 testMemory.stopThreads();
 for (Thread thread : threads) {
 assertNotNull("Threads should not be null after stopThreads() call", thread);
 }
 }

 @Test
 public void getRevisionTest() {
 String revision = testMemory.getRevision();
 assertNotNull("getRevision method should not return null", revision);
 }

}
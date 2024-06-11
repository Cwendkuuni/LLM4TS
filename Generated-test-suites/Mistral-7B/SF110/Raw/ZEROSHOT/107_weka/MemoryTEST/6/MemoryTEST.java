package weka.core. MemoryTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory(true);
 }

 @Test
 public void testMemoryInitialization() {
 Memory.m_Enabled = false; // Disable Memory.m_Enabled for testing
 memory = new Memory(true);

 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = memory.getCurrent();

 assertEquals("Initial memory should be set correctly.", initial, Runtime.getRuntime().totalMemory());
 assertEquals("Max memory should be set correctly.", max, Runtime.getRuntime().maxMemory());
 assertEquals("Total memory should be set correctly.", total, Runtime.getRuntime().totalMemory());

 Memory.m_Enabled = true; // Enable Memory.m_Enabled
 }

 @Test
 public void testIsEnabled() {
 memory = new Memory(true);
 assertTrue("Memory should be enabled by default.", memory.isEnabled());

 memory.setEnabled(false);
 assertFalse("Memory should be disabled when setEnabled(false) is called.", memory.isEnabled());

 memory.setEnabled(true);
 assertTrue("Memory should be enabled when setEnabled(true) is called.", memory.isEnabled());
 }

 @Test
 public void testIsOutOfMemory() {
 memory = new Memory(true);
 long max = memory.getMax();
 long total = memory.getCurrent();
 long initial = memory.getInitial();

 // Test when not out of memory
 assertEquals("Memory should not be out of memory when there is enough memory.", false, memory.isOutOfMemory());

 // Test when out of memory
 total += 10000000L; // Consume 10MB more memory
 assertEquals("Memory should be out of memory when there is not enough memory.", true, memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testThreadJoinNullThread() {
 Memory.Memory mem = new Memory();
 Thread[] threads = new Thread[0];
 mem.stopThreads();
 }

 @Test
 public void testToString() {
 Memory mem = new Memory();
 assertEquals("ToString method should return correct string representation.",
 "Memory[useGUI=true, enabled=true, initial=" + Runtime.getRuntime().totalMemory() + ", total=" + Runtime.getRuntime().totalMemory() + ", max=" + Runtime.getRuntime().maxMemory() + "]", mem.toString());
 }
}
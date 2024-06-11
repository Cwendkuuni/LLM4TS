package weka.core.test;

import static org.junit.Assert.*;

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
 Memory memory = new Memory();
 assertNotEquals(0, Memory.m_Initial);
 assertNotEquals(0, memory.getInitial());
 assertNotEquals(0, Memory.m_Total);
 assertNotEquals(0, memory.getCurrent());
 assertNotEquals(0, memory.getMax());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testUseGUI() {
 Memory memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long newInitial = Runtime.getRuntime().totalMemory();
 assertTrue(initial < newInitial);
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long total = memory.getCurrent();
 long newTotal = Runtime.getRuntime().totalMemory();
 assertTrue(total < newTotal);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 Runtime runtime = Runtime.getRuntime();
 long newMax = runtime.maxMemory();
 assertTrue(max < newMax);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = memory.getCurrent();
 long newTotal = Runtime.getRuntime().totalMemory();
 long newMax = Runtime.getRuntime().maxMemory();
 long threshold = initial + 200000L;

 memory.setEnabled(true);
 if (newTotal > threshold && newMax > threshold) {
 fail("Test should failed due to enough memory");
 }

 memory.setEnabled(true);
 if (newTotal < threshold && newMax > threshold) {
 assertTrue(memory.isOutOfMemory());
 }
 }

 @Test
 public void testToMegaByte() {
 long bytes = 1234567890L;
 double megabytes = Memory.toMegaByte(bytes);
 assertEquals(115.26, megabytes, 0.01);
 }
}
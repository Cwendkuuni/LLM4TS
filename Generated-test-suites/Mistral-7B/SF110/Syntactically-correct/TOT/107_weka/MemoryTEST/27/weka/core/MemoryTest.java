package weka.core.test;

import org.junit.Assert;
import org.junit.Test;
import static weka.core.Memory.toMegaByte;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryConstructor() {
 Memory memory = new Memory(true);
 Assert.assertEquals(true, memory.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 Assert.assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 Assert.assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 memory = new Memory();
 long newInitial = memory.getInitial();
 Assert.assertEquals(initial, newInitial);
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long current = memory.getCurrent();
 memory.getCurrent();
 long newCurrent = memory.getCurrent();
 Assert.assertEquals(current, newCurrent);
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 memory = new Memory();
 long newMax = memory.getMax();
 Assert.assertEquals(max, newMax);
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 long maxMemory = memory.getMax();
 long currentMemory = memory.getCurrent();
 long initialMemory = memory.getInitial();
 long outOfMemoryThreshold = initialMemory + 200000L;
 memory.setInitial(outOfMemoryThreshold);
 Assert.assertFalse(memory.isOutOfMemory());
 memory.setTotal(maxMemory - 1000000L);
 Assert.assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testStopThreadsNullGroup() {
 Memory memory = new Memory();
 Thread[] threads = null;
 memory.stopThreads();
 }

 @Test
 public void testGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 Assert.assertNotNull(revision);
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }
}
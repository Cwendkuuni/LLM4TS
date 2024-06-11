package weka.core.test;

import org.junit.Assert;
import org.junit.Test;
import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryInitialization() {
 Memory memory = new Memory();
 Assert.assertFalse(memory.isEnabled());
 Assert.assertFalse(memory.getUseGUI());
 Assert.assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
 Assert.assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
 Assert.assertEquals(0, memory.getCurrent());
 Assert.assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testMemoryEnabled() {
 Memory memory = new Memory(true);
 memory.setEnabled(true);
 memory.setEnabled(false);
 Assert.assertTrue(memory.isEnabled());
 Assert.assertFalse(memory.getUseGUI());
 }

 @Test
 public void testMemoryUseGUI() {
 Memory memory = new Memory(true);
 memory.setUseGUI(false);
 memory.setUseGUI(true);
 Assert.assertFalse(memory.getUseGUI());
 Assert.assertFalse(memory.isOutOfMemory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoryNegativeUseGUI() {
 Memory memory = new Memory(-1);
 }

 @Test
 public void testMemoryOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 Runtime runtime = Runtime.getRuntime();
 runtime.gc();
 memory.setMax(runtime.totalMemory() - 1000000L);
 Assert.assertTrue(memory.isOutOfMemory());
 memory.showOutOfMemory();
 }

 @Test
 public void testMemoryToMegaByte() {
 long bytes = 123456789;
 Assert.assertEquals(11.731523809523807, Memory.toMegaByte(bytes), 0.001);
 }

 @Test
 public void testMemoryGetRevision() {
 Memory memory = new Memory();
 Assert.assertEquals("-8034 -", memory.getRevision());
 }

 @Test
 public void testMemoryMain() {
 Memory memory = new Memory();
 memory.main(new String[0]);
 }
}
package weka.core.memory;

import org.junit.Assert;
import org.junit.Test;
import weka.core.Memory;
import weka.core.Utils;

public class MemoryTest {

  @Test
  public void testMemoryConstructor() {
    Memory memory = new Memory();
    Assert.assertFalse(memory.isEnabled());
    Assert.assertFalse(memory.getUseGUI());
    Assert.assertNull(memory.getRevision());
    Memory memoryWithGUI = new Memory(true);
    Assert.assertTrue(memoryWithGUI.getUseGUI());
    Assert.assertNull(memoryWithGUI.getRevision());
  }

  @Test
  public void testIsEnabled() {
    Memory memory = new Memory();
    Assert.assertFalse(memory.isEnabled());
    Memory memoryEnabled = new Memory(true);
    memoryEnabled.setEnabled(true);
    Assert.assertTrue(memoryEnabled.isEnabled());
  }

  @Test
  public void testSetEnabled() {
    Memory memory = new Memory();
    memory.setEnabled(true);
    Assert.assertTrue(memory.isEnabled());
    memory.setEnabled(false);
    Assert.assertFalse(memory.isEnabled());
  }

  @Test
  public void testGetUseGUI() {
    Memory memory = new Memory();
    Assert.assertFalse(memory.getUseGUI());
    Memory memoryWithGUI = new Memory(true);
    Assert.assertTrue(memoryWithGUI.getUseGUI());
  }

  @Test
  public void testGetInitial() {
    Memory memory = new Memory();
    Assert.assertEquals(0, memory.getInitial());
    Memory memoryWithInitializedInitial = new Memory();
    memoryWithInitializedInitial.setInitial(100000000);
    Assert.assertEquals(100000000, memoryWithInitializedInitial.getInitial());
  }

  @Test
  public void testGetCurrent() {
    Memory memory = new Memory();
    Assert.assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
    Runtime runtime = Runtime.getRuntime();
    runtime.gc();
    memory.getCurrent();
    Assert.assertEquals(runtime.totalMemory() - runtime.freeMemory(), memory.getCurrent());
  }

  @Test
  public void testGetMax() {
    Memory memory = new Memory();
    Assert.assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
  }

  @Test
  public void testIsOutOfMemory() {
    Memory memory = new Memory();
    Assert.assertFalse(memory.isOutOfMemory());
    memory.setEnabled(true);
    long maxMemory = Runtime.getRuntime().maxMemory();
    long currentMemory = maxMemory - 10000000;
    memory.setInitial(maxMemory);
    Assert.assertTrue(memory.isOutOfMemory());
    memory.setCurrent(currentMemory);
    Assert.assertTrue(memory.isOutOfMemory());
  }

  @Test(expected = NullPointerException.class)
  public void testShowOutOfMemoryWithNullUseGUI() {
    Memory memory = new Memory();
    memory.setEnabled(true);
    memory.showOutOfMemory();
    memory.setUseGUI(null);
    memory.showOutOfMemory();
  }

  @Test
  public void testToMegaByte() {
    long bytes = 1024 * 1024;
    double expected = 1.0;
    Assert.assertEquals(expected, Memory.toMegaByte(bytes), 0.0);
  }

  @Test
  public void testStopThreads() {
    Memory memory = new Memory();
    Thread thread = new Thread(() -> {});
    thread.start();
    memory.stopThreads();
    Assert.assertTrue(thread.isInterrupted());
  }

  @Test
  public void testGetRevision() {
    Memory memory = new Memory();
    String expected = "$Revision: 8034 $";
    Assert.assertEquals(expected, memory.getRevision());
  }

  @Test
  public void testMain() {
    Memory memory = new Memory();
    memory.main(new String[0]);
  }
}
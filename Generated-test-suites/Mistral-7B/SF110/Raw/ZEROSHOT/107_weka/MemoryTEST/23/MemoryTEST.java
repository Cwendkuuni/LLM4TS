package weka.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

  private Memory memory;

  @Before
  public void setUp() {
    memory = new Memory();
  }

  @Test
  public void testMemoryConstructor() {
    Memory memoryWithGUI = new Memory(true);
    Memory memoryWithoutGUI = new Memory();

    assertEquals(false, memoryWithoutGUI.getUseGUI());
    assertEquals(true, memoryWithGUI.getUseGUI());
  }

  @Test
  public void testMemoryIsEnabled() {
    Memory memory = new Memory();

    assertFalse(memory.isEnabled());

    memory.setEnabled(true);

    assertTrue(memory.isEnabled());
  }

  @Test
  public void testMemoryGetUseGUI() {
    Memory memory = new Memory(true);

    assertTrue(memory.getUseGUI());

    memory = new Memory();

    assertFalse(memory.getUseGUI());
  }

  @Test
  public void testMemoryGetInitial() {
    Memory memory = new Memory();

    long initial = memory.getInitial();

    assertNotNull(initial);
  }

  @Test
  public void testMemoryGetCurrent() {
    Memory memory = new Memory();

    long current = memory.getCurrent();

    assertNotNull(current);
  }

  @Test
  public void testMemoryGetMax() {
    Memory memory = new Memory();

    long max = memory.getMax();

    assertNotNull(max);
  }

  @Test
  public void testMemoryIsOutOfMemory() {
    Memory memory = new Memory();

    assertFalse(memory.isOutOfMemory());

    long current = 99 * (long) (Memory.toMegaByte(memory.getMax()) / 2);
    memory.setTotal(current);

    assertTrue(memory.isOutOfMemory());
  }

  @Test(expected = NullPointerException.class)
  public void testMemoryShowOutOfMemoryWithNullGui() {
    Memory memory = new Memory();
    memory.showOutOfMemory();
  }

  @Test
  public void testMemoryShowOutOfMemory() {
    Memory memory = new Memory();
    memory.setEnabled(true);

    long current = 99 * (long) (Memory.toMegaByte(memory.getMax()) / 2);
    memory.setTotal(current);

    memory.showOutOfMemory();
  }

  @Test
  public void testMemoryToMegaByte() {
    assertEquals(1.2345, Memory.toMegaByte(1234567), 0.0001);
  }

  @Test
  public void testMemoryMain() {
    Memory memory = new Memory();

    memory.main(new String[0]);

    String initialMessage = "Initial memory: ";
    String maxMessage = "Max memory: ";

    assertTrue(memory.getOutput().contains(initialMessage));
    assertTrue(memory.getOutput().contains(maxMessage));
  }

  @Test
  public void testMemoryStopThreads() {
    Memory memory = new Memory();

    memory.stopThreads();
  }

  @Test
  public void testMemoryGetRevision() {
    Memory memory = new Memory();

    String revision = memory.getRevision();

    assertNotNull(revision);
  }

  @Test
  public void testMemoryStaticInitialization() {
    Memory memory = new Memory();

    assertEquals(true, Memory.m_Enabled);

    long initial = Memory.m_Initial;
    long max = Memory.getMax();

    assertNotEquals(0, initial);
    assertNotEquals(0, max);
  }
}
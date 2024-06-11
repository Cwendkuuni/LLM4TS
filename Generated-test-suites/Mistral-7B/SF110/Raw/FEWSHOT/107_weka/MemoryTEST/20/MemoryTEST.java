package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

 Memory m;
 final long BYTES_IN_MB = 1048576L;

 @Test
 public void testMemory() {
 m = new Memory();
 assertEquals(Memory.m_Enabled, true);
 assertEquals(m.getUseGUI(), false);
 assertNotNull(m.getRuntime());
 assertEquals(m.getMax(), m.getRuntime().maxMemory());
 assertEquals(m.getCurrent(), m.getRuntime().totalMemory());
 }

 @Test
 public void testMemoryWithGUI() {
 m = new Memory(true);
 assertEquals(Memory.m_Enabled, true);
 assertEquals(m.getUseGUI(), true);
 assertNotNull(m.getRuntime());
 assertEquals(m.getMax(), m.getRuntime().maxMemory());
 assertEquals(m.getCurrent(), m.getRuntime().totalMemory());
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = false;
 assertFalse(m.isEnabled());
 Memory.m_Enabled = true;
 assertTrue(m.isEnabled());
 }

 @Test
 public void testSetEnabled() {
 Memory.m_Enabled = false;
 m.setEnabled(true);
 assertTrue(m.isEnabled());
 Memory.m_Enabled = false;
 m.setEnabled(false);
 assertFalse(m.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 m = new Memory();
 assertFalse(m.getUseGUI());
 m = new Memory(true);
 assertTrue(m.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 assertEquals(0, m.getInitial());
 m = new Memory();
 assertEquals(m.getInitial(), m.getCurrent());
 m.setEnabled(true);
 m.getCurrent();
 Memory.m_Initial = m.getCurrent();
 assertEquals(Memory.m_Initial, m.getInitial());
 }

 @Test
 public void testGetCurrent() {
 assertEquals(0, m.getCurrent());
 m = new Memory();
 assertEquals(m.getInitial(), m.getCurrent());
 m.setEnabled(true);
 m.getCurrent();
 assertEquals(m.getCurrent(), m.getInitial());
 }

 @Test
 public void testIsOutOfMemory() {
 final long initial = m.getInitial();
 m.setEnabled(true);
 m.setTotal(initial + 10L * BYTES_IN_MB);
 assertFalse(m.isOutOfMemory());
 m.setTotal(initial + 11L * BYTES_IN_MB);
 assertTrue(m.isOutOfMemory());
 }

 @Test
 public void testToMegaByte() {
 assertEquals(Memory.toMegaByte(1), 1.0, 0.0);
 assertEquals(Memory.toMegaByte(1024 * 1024), 1.0, 0.0);
 assertEquals(Memory.toMegaByte(1024 * 1024 * 1024), 1024.0, 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryNull() {
 Memory.m_Enabled = true;
 Memory mem = null;
 mem.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemory() {
 m = new Memory();
 m.setEnabled(true);
 m.setTotal(m.getInitial() + 10L * BYTES_IN_MB);
 m.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 m = new Memory();
 Thread[] threads = new Thread[3];
 for (int i = 0; i < 3; ++i) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 m.stopThreads();
 for (Thread thread : threads) {
 assertFalse(thread.isAlive());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetRevisionInvalidArgument() {
 Memory m = new Memory();
 m.getRevision("$Revision: 8034 $notValid");
 }

 @Test
 public void testGetRevision() {
 Memory m = new Memory();
 assertEquals("$Revision: 8034 $", m.getRevision());
 }

 @Test
 public void testMain() {
 Memory mem = new Memory();
 assertEquals(Utils.doubleToString(Memory.toMegaByte(mem.getInitial()), 1) + "MB", mem.getInitial() + "MB");
 assertEquals(Utils.doubleToString(Memory.toMegaByte(mem.getMax()), 1) + "MB", mem.getMax() + "MB");
 }
}
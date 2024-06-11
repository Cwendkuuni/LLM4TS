package weka.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Before
 public void setUp() {
 memory = new Memory();
 }

 @Test
 public void testIsEnabled() {
 Memory.m_Enabled = true;
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testSetEnabled() {
 Memory.m_Enabled = false;
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 Memory.m_Enabled = false;
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 Memory memoryWithoutGUI = new Memory();
 assertFalse(memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 memory = new Memory();
 long initial = memory.getInitial();
 Memory staticMemory = new Memory();
 long staticInitial = staticMemory.getInitial();
 assertEquals(initial, staticInitial);
 }

 @Test
 public void testGetCurrent() {
 memory = new Memory();
 long initial = memory.getCurrent();
 Runtime runtime = Runtime.getRuntime();
 long totalMemory = runtime.totalMemory();
 long currentMemory = runtime.totalMemory() - runtime.freeMemory();
 assertEquals(initial, currentMemory);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetCurrentAfterDisablingMemory() {
 Memory.m_Enabled = false;
 memory = new Memory();
 memory.getCurrent();
 }

 @Test
 public void testGetMax() {
 memory = new Memory();
 long max = memory.getMax();
 long runtimeMax = Runtime.getRuntime().maxMemory();
 assertEquals(max, runtimeMax);
 }

 @Test
 public void testIsOutOfMemoryWithEnoughMemory() {
 memory = new Memory();
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testIsOutOfMemoryWithNotEnoughMemory() {
 Runtime runtime = Runtime.getRuntime();
 long maxMemory = runtime.maxMemory() / 2;
 long totalMemory = runtime.totalMemory() - runtime.freeMemory();
 Memory memory = new Memory(true);
 Memory.m_Enabled = true;
 memory.setInitial(totalMemory);
 memory.setMax(maxMemory);
 assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memory = new Memory(true);
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithDisabledMemory() {
 Memory memory = new Memory();
 memory.setEnabled(false);
 memory.showOutOfMemory();
 }

 @Test
 public void testStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 @Test(expected = SecurityException.class)
 public void testStopThreadsWithoutSecurityManager() {
 Memory memory = new Memory();
 System.setSecurityManager(null);
 memory.stopThreads();
 }

 @Test
 public void testStopThreadsWithSecurityManager() {
 Memory memory = new Memory();
 SecurityManager securityManager = new SecurityManager() {
 @Override
 public void checkPermission(Permission perm) {
 super.checkPermission(perm);
 }

 @Override
 public void checkAccess(int access) {
 super.checkAccess(access);
 }
 };
 System.setSecurityManager(securityManager);
 memory.stopThreads();
 }
}
package weka.core.MemoryTest;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

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
 Memory memoryInstance = new Memory();
 assertNotNull(memoryInstance);
 assertEquals(true, memoryInstance.isEnabled());
 assertEquals(false, memoryInstance.getUseGUI());
 assertFalse(memoryInstance.isOutOfMemory());
 long initial = memoryInstance.getInitial();
 long total = memoryInstance.getCurrent();
 long max = memoryInstance.getMax();
 assertEquals(initial, Memory.m_Initial);
 assertEquals(total, memoryInstance.m_Total);
 assertEquals(max, memoryInstance.m_Max);
 }

 @Test
 public void testMemoryWithGUI() {
 memory = new Memory(true);
 assertEquals(true, memory.getUseGUI());
 }

 @Test(expected = HeadlessException.class)
 public void testMemoryWithoutGUI() {
 memory = new Memory(false);
 memory.getUseGUI();
 }

 @Test
 public void testIsOutOfMemory() {
 Memory memoryInstance = new Memory();
 long initial = memoryInstance.getInitial();
 memoryInstance.setEnabled(true);
 long max = memoryInstance.getMax();
 long total = memoryInstance.getCurrent();
 memoryInstance.setInitial(initial + 500000);
 assertFalse(memoryInstance.isOutOfMemory());
 total += 1000000;
 assertTrue(memoryInstance.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemory() {
 Memory memoryInstance = new Memory();
 memoryInstance.setEnabled(true);
 memoryInstance.setInitial(1);
 memoryInstance.setTotal(1);
 memoryInstance.setMax(1024 * 1024); // 1MB
 memoryInstance.showOutOfMemory();
 }

 @Test
 public void testToMegaByte() {
 long bytes = 1024 * 1024 * 1024; // 1GB
 double megabytes = Memory.toMegaByte(bytes);
 assertEquals(1024.0, megabytes, 0.0);
 }

 @Test
 public void testStopThreads() throws InterruptedException, InvocationTargetException {
 memory.setEnabled(true);
 memory.setTotal(Runtime.getRuntime().totalMemory());
 memory.setMax(Runtime.getRuntime().maxMemory());
 memory.showOutOfMemory();
 memory.stopThreads();
 memory.setTotal(Runtime.getRuntime().totalMemory());
 assertFalse(memory.isOutOfMemory());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetRevision() {
 Memory memoryInstance = new Memory();
 memoryInstance.getRevision();
 }

 @Test
 public void testMain() {
 Memory memoryInstance = new Memory();
 memoryInstance.main(new String[0]);
 }
}
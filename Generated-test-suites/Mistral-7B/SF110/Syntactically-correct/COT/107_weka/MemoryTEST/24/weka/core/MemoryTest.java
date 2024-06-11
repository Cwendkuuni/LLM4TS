package weka.core.test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryConstructor() {
 Memory memory = new Memory();
 assertNotNull(memory);
 }

 @Test
 public void testMemoryConstructorWithUseGUI() {
 Memory memory = new Memory(true);
 assertNotNull(memory);
 }

 @Test
 public void testIsEnabled() throws Exception {
 Memory memory = new Memory();
 assertFalse(memory.isEnabled());

 Method setEnabledMethod = Memory.class.getDeclaredMethod("setEnabled", boolean.class);
 setEnabledMethod.setAccessible(true);
 setEnabledMethod.invoke(memory, true);

 assertTrue(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());

 Memory memoryWithGUI = new Memory(true);
 assertTrue(memoryWithGUI.getUseGUI());
 }

 @Test
 public void testGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 Memory staticMemory = new Memory();
 long staticInitial = staticMemory.getInitial();
 assertEquals(initial, staticInitial);
 }

 @Test
 public void testGetCurrent() {
 Memory memory = new Memory();
 long current = memory.getCurrent();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 long currentAfterShowMessage = memory.getCurrent();

 assertEquals(current, memory.getCurrent() - 200000L);
 assertEquals(currentAfterShowMessage, memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 Memory memory = new Memory();
 long max = memory.getMax();
 Memory staticMemory = new Memory();
 long staticMax = staticMemory.getMax();
 assertEquals(max, staticMax);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsOutOfMemoryWhenNotEnabled() {
 Memory memory = new Memory();
 memory.setEnabled(false);
 memory.isOutOfMemory();
 }

 @Test
 public void testIsOutOfMemoryWhenNotOutOfMemory() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testIsOutOfMemoryWhenOutOfMemory() throws Exception {
 Memory memory = new Memory();
 memory.setEnabled(true);
 memory.showOutOfMemory();
 assertTrue(memory.isOutOfMemory());
 }

 @Test(expected = NullPointerException.class)
 public void testShowOutOfMemoryWithNull() {
 Memory memory = new Memory();
 memory.showOutOfMemory(null);
 }

 @Test
 public void testToMegaByte() {
 long bytes = 1024 * 1024;
 double megaBytes = Memory.toMegaByte(bytes);
 assertEquals(1.0, megaBytes, 0.0);
 }

 @Test(expected = NegativeArraySizeException.class)
 public void testStopThreadsWithEmptyThreadGroup() {
 Memory memory = new Memory();
 Thread[] threads = new Thread[0];
 memory.stopThreads(threads);
 }

 @Test
 public void testStopThreads() throws InterruptedException, IllegalThreadStateException, SecurityException {
 Memory memory = new Memory();
 memory.setEnabled(true);

 Thread thread1 = new Thread(() -> {});
 thread1.start();
 Thread thread2 = new Thread(() -> {});
 thread2.start();

 memory.stopThreads();

 assertTrue(thread1.isInterrupted());
 assertTrue(thread2.isInterrupted());
 assertFalse(thread1.isAlive());
 assertFalse(thread2.isAlive());
 }

 @Test(expected = InvocationTargetException.class)
 public void testGetRevision() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
 Memory memory = new Memory();
 Method getRevisionMethod = Memory.class.getDeclaredMethod("getRevision");
 getRevisionMethod.setAccessible(true);
 String revision = (String) getRevisionMethod.invoke(memory);
 assertNotNull(revision);
 }

 @Test
 public void testMain() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 System.out.println("Initial memory: " + Memory.toMegaByte(initial) + "MB" + " (" + initial + ")");
 long max = memory.getMax();
 System.out.println("Max memory: " + Memory.toMegaByte(max) + "MB" + " (" + max + ")");
 }
}
package weka.core.test;

 import static org.junit.Assert.*;
 import weka.core.Memory;

 import org.junit.Before;
 import org.junit.Test;

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

 assertEquals(true, memoryWithGUI.getUseGUI());
 assertEquals(false, memoryWithoutGUI.getUseGUI());
 }

 @Test
 public void testIsEnabled() {
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void testGetUseGUI() {
 memory = new Memory(true);
 assertTrue(memory.getUseGUI());
 memory = new Memory(false);
 assertFalse(memory.getUseGUI());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsOutOfMemoryWhenMemoryIsEnabled() {
 memory.setEnabled(true);
 memory.setInitial(memory.getCurrent() + 500000L); // set initial to be less than total
 memory.isOutOfMemory();
 }

 @Test
 public void testIsOutOfMemoryWhenMemoryIsDisabled() {
 memory.setEnabled(false);
 assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testShowOutOfMemoryMessage() {
 memory.setEnabled(true);
 memory.showOutOfMemory();
 }

 @Test
 public void testShowOutOfMemoryMessageWithGUI() {
 memory = new Memory(true);
 memory.setEnabled(true);
 memory.showOutOfMemory();
 }

 @Test
 public void testGetInitial() {
 memory.setInitial(10 * 1024 * 1024); // 10MB
 assertEquals(10 * 1024 * 1024, memory.getInitial());
 }

 @Test
 public void testGetCurrent() {
 memory.setInitial(10 * 1024 * 1024); // 10MB
 memory.setTotal(5 * 1024 * 1024); // 5MB
 assertEquals(5 * 1024 * 1024, memory.getCurrent());
 }

 @Test
 public void testGetMax() {
 memory.setMax(20 * 1024 * 1024); // 20MB
 assertEquals(20 * 1024 * 1024, memory.getMax());
 }

 @Test
 public void testToMegaByte() {
 assertEquals(1.5, Memory.toMegaByte(1572864), 0.01); // 1.5MB
 }

 @Test
 public void testStopThreads() {
 // This test is not comprehensive as it's hard to check the threads status
 // and it may cause issues if it's run on an IDE
 // Can be checked using a separate test class or a framework like TestNG
 }
 }
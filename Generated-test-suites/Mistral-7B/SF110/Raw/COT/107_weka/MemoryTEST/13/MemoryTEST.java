import static org.junit.Assert.*;
import static weka.core.Utils.doubleToString;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.lang.reflect.UndeclaredException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

 // Test constructor with default useGUI parameter
 @Test
 public void testMemoryDefaultConstructor() {
 Memory memory = new Memory();
 assertEquals(false, memory.m_UseGUI);
 }

 // Test constructor with useGUI parameter
 @Test
 public void testMemoryConstructorWithUseGUI() {
 Memory memory = new Memory(true);
 assertEquals(true, memory.m_UseGUI);
 }

 // Test getters and setters for enabled flag
 @Test
 public void testGettersAndSettersForEnabledFlag() {
 Memory memory = new Memory();
 assertFalse(memory.isEnabled());
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 // Test isOutOfMemory method
 @Test
 public void testIsOutOfMemoryMethod() {
 Memory memory = new Memory(true);
 long maxMemory = memory.getMax();
 long initialMemory = memory.getInitial() + 200000L;
 memory.setEnabled(true);
 memory.setTotal(maxMemory - 1);
 assertFalse(memory.isOutOfMemory());
 memory.setTotal(maxMemory - (initialMemory + 100000L));
 assertTrue(memory.isOutOfMemory());
 }

 // Test toMegaByte static method
 @Test
 public void testToMegaByteStaticMethod() {
 long bytes = 1024 * 1024 * 5;
 double megaBytes = Memory.toMegaByte(bytes);
 assertEquals(5, megaBytes, 0.0);
 }

 // Test showOutOfMemory method when not enabled
 @Test(expected = IllegalStateException.class)
 public void testShowOutOfMemoryWhenNotEnabled() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 // Test showOutOfMemory method with useGUI
 @Test
 public void testShowOutOfMemoryWithUseGUI() {
 Memory memory = new Memory(true);
 memory.setTotal(memory.getMax() - 1);
 memory.showOutOfMemory();
 }

 // Test showOutOfMemory method without useGUI
 @Test
 public void testShowOutOfMemoryWithoutUseGUI() {
 Memory memory = new Memory();
 memory.setTotal(memory.getMax() - 1);
 System.setErr(new PrintStream(new ByteArrayOutputStream()) {
 @Override
 public void println(String s) {
 fail("OutOfMemory message should not be printed when not using GUI.");
 }
 });
 memory.showOutOfMemory();
 System.setErr(System.err);
 }

 // Test stopThreads method
 @Before
 public void setup() {
 System.setProperty("sun.awt.noerrormessage", "true");
 }

 @After
 public void tearDown() throws InterruptedException, UndeclaredException {
 Runtime.getRuntime().runFinalizersOnExit(false);
 }

 @Test
 public void testStopThreadsMethod() throws InterruptedException {
 Memory memory = new Memory();
 Thread[] threads = new Thread[10];
 for (int i = 0; i < threads.length; i++) {
 threads[i] = new Thread(() -> {});
 threads[i].start();
 }
 memory.stopThreads();
 for (Thread thread : threads) {
 assertTrue(thread.isAlive());
 }
 TimeUnit.SECONDS.sleep(1);
 for (Thread thread : threads) {
 try {
 assertFalse(thread.isAlive());
 } catch (Exception e) {
 fail("Expected thread to be terminated.");
 }
 }
 }

 // Test getRevision method
 @Test
 public void testGetRevisionMethod() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertFalse(revision.isEmpty());
 }

 // Test main method
 @Test
 public void testMainMethod() {
 Memory memory = new Memory();
 memory.m_UseGUI = true;
 memory.m_Runtime = null;
 memory.main(new String[0]);
 String initialMemory = doubleToString(memory.getInitial(), 1) + "MB (" + memory.getInitial() + ")";
 String maxMemory = doubleToString(memory.getMax(), 1) + "MB (" + memory.getMax() + ")";
 String output = "Initial memory: " + initialMemory + "\nMax memory: " + maxMemory;
 assertEquals(output, System.getProperty("out"));
 }

}
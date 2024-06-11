package weka.core.test;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
 public void testMemory() {
 Memory m = new Memory();
 assertEquals(false, m.isEnabled());
 m.setEnabled(true);
 assertEquals(true, m.isEnabled());
 m = new Memory(true);
 assertEquals(true, m.getUseGUI());
 Memory.setEnabled(false);
 m = new Memory(true);
 assertEquals(false, m.isEnabled());
 m.setEnabled(true);
 assertEquals(true, m.isEnabled());
 }

 @Test
 public void testIsOutOfMemory() {
 Memory m = new Memory();
 long initial = m.getInitial();
 long max = m.getMax();
 long total = m.getCurrent();
 assertFalse(m.isOutOfMemory());
 m.setEnabled(false);
 m = new Memory(true);
 total = m.getCurrent();
 assertTrue(m.isOutOfMemory());
 // set memory to max value
 long newTotal = max - (max - total) + initial;
 System.setProperty("java.awt.headless", "true"); // disable GUI
 try {
 Method setTotal = Memory.class.getDeclaredMethod("setTotal", long.class);
 setTotal.setAccessible(true);
 setTotal.invoke(m, newTotal);
 } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 fail();
 }
 assertFalse(m.isOutOfMemory());
 }

 @Test(expected = HeadlessException.class)
 public void testShowOutOfMemory() {
 Memory m = new Memory();
 m.showOutOfMemory();
 }

 @Test
 public void testToMegaByte() {
 long bytes = 123456789L;
 double megabytes = Memory.toMegaByte(bytes);
 assertEquals(11.63523529411765, megabytes, 0.001);
 }

 @Test
 public void testGetInitial() {
 Memory m = new Memory();
 long initial = m.getInitial();
 Memory.setEnabled(false);
 m = new Memory();
 assertEquals(initial, m.getInitial());
 }

 @Test
 public void testGetCurrent() {
 Memory m = new Memory();
 long total = m.getCurrent();
 Memory.setEnabled(false);
 m = new Memory();
 assertEquals(total, m.getCurrent());
 }

 @Test
 public void testGetMax() {
 Memory m = new Memory();
 long max = m.getMax();
 Memory.setEnabled(false);
 m = new Memory();
 assertEquals(max, m.getMax());
 }

 @Test
 public void testMain() throws Exception {
 Memory m = new Memory();
 Runtime runtime = Runtime.getRuntime();
 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = memory.getCurrent();
 Memory.setEnabled(false);
 m = new Memory();
 total = m.getCurrent();
 assertEquals(initial, m.getInitial());
 assertEquals(total, m.getCurrent());
 assertEquals(max, m.getMax());
 }
}
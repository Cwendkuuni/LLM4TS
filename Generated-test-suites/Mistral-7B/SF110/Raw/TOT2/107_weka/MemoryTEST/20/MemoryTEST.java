package weka.core.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

   @Test
   public void testIsEnabled() {
      Memory memory = new Memory();
      boolean expected = true; // default value of m_Enabled in Memory class
      boolean actual = memory.isEnabled();
      assertTrue("isEnabled() should return: " + expected, actual == expected);
   }

   @Test
   public void testIsEnabled_false() {
      Memory memory = new Memory(false);
      boolean expected = false;
      boolean actual = memory.isEnabled();
      assertFalse("isEnabled() should return: " + expected, actual == expected);
   }

   @Test
   public void testSetEnabled() {
      Memory memory = new Memory();
      memory.setEnabled(false);
      boolean expected = false;
      boolean actual = memory.isEnabled();
      assertFalse("setEnabled() should set isEnabled() to: " + expected, actual == expected);

      memory.setEnabled(true);
      expected = true;
      actual = memory.isEnabled();
      assertTrue("setEnabled() should set isEnabled() to: " + expected, actual == expected);
   }

   @Test
   public void testGetUseGUI() {
      Memory memory1 = new Memory();
      boolean expected1 = false;
      boolean actual1 = memory1.getUseGUI();
      assertFalse("getUseGUI() should return: " + expected1, actual1 == expected1);

      Memory memory2 = new Memory(true);
      boolean expected2 = true;
      boolean actual2 = memory2.getUseGUI();
      assertTrue("getUseGUI() should return: " + expected2, actual2 == expected2);
   }

   @Test
   public void testGetInitial() {
      Memory memory = new Memory();
      long expected = Runtime.getRuntime().totalMemory() / 1024 / 1024; // initial memory in MB
      long actual = (long) Memory.getInitial(); // since it's a static variable, need to use getInstance().getInitial()
      assertEquals("getInitial() should return: " + expected, expected, actual);
   }

   @Test
   public void testGetCurrent() {
      Memory memory = new Memory();
      long expected = Runtime.getRuntime().totalMemory() / 1024 / 1024;
      long actual = (long) Memory.getCurrent();
      assertEquals("getCurrent() should return: " + expected, expected, actual);

      Memory memory2 = new Memory();
      long expected2 = Runtime.getRuntime().totalMemory() / 1024 / 1024;
      long actual2 = (long) Memory.getCurrent();
      assertEquals("getCurrent() should return the same value when called multiple times:", expected2, actual2);
   }

   @Test
   public void testGetMax() {
      Memory memory = new Memory();
      long expected = Runtime.getRuntime().maxMemory() / 1024 / 1024;
      long actual = (long) Memory.getMax();
      assertEquals("getMax() should return: " + expected, expected, actual);
   }

   @Test
   public void testIsOutOfMemory() {
      Memory memory = new Memory();
      long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
      long initialMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
      long currentMemory = initialMemory;
      boolean expected = false;
      boolean actual = memory.isOutOfMemory();
      assertFalse("isOutOfMemory() should return: " + expected, actual == expected);

      // consume memory
      for (int i = 0; i < 100; i++) {
         byte[] bytes = new byte[1024 * 1024]; // 1 MB
      }

      currentMemory += 10 * 1024 * 1024; // 10 MB
      expected = true;
      actual = memory.isOutOfMemory();
      assertTrue("isOutOfMemory()
 continued in next message
vbnet
should return: " + expected, actual == expected);
   }

   @Test
   public void testShowOutOfMemory() {
      Memory memory = new Memory();
      long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
      long initialMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
      long currentMemory = initialMemory;
      boolean isEnabled = true;
      memory.setEnabled(isEnabled);

      // consume memory
      for (int i = 0; i < 100; i++) {
         byte[] bytes = new byte[1024 * 1024]; // 1 MB
      }

      currentMemory += 10 * 1024 * 1024; // 10 MB
      memory.showOutOfMemory();

      String expectedMsg = new String("Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   ");
      assertTrue(memory.isEnabled(), "Memory should be enabled before showing OutOfMemory message");
      assertTrue(memory.isOutOfMemory(), "Memory should be out of memory before showing OutOfMemory message");
      assertTrue(memory.getUseGUI(), "Memory should have GUI enabled before showing OutOfMemory message");
      assertTrue(expectedMsg.contains(Utils.doubleToString(toMegaByte(Memory.m_Initial), 1) + "MB"), "Initial memory should be displayed correctly");
      assertTrue(expectedMsg.contains(Utils.doubleToString(toMegaByte(memory.m_Total), 1) + "MB"), "Total memory used should be displayed correctly");
      assertTrue(expectedMsg.contains(Utils.doubleToString(toMegaByte(Memory.m_Max), 1) + "MB"), "Max memory available should be displayed correctly");
      assertTrue(expectedMsg.contains("Note:"), "Note should be displayed in the message");
      assertTrue(expectedMsg.contains("The Java heap size can be specified with the -Xmx option."), "Java heap size should be mentioned in the message");
      assertFalse(memory.getUseGUI(), "showOutOfMemory() should have closed the GUI after displaying the message");

      memory.setEnabled(false);
      memory.showOutOfMemory();
      assertFalse(memory.isEnabled(), "Memory should be disabled after showing OutOfMemory message");
      assertFalse(memory.getUseGUI(), "showOutOfMemory() should not have closed or opened the GUI after displaying the message");
   }

   @Test
   public void testStopThreads() {
      Thread thread1 = new Thread(() -> {});
      Thread thread2 = new Thread(() -> {});
      Thread thread3 = new Memory();
      Memory memory = new Memory();

      thread1.start();
      thread2.start();
      thread3.start();

      memory.stopThreads();

      // check if thread1 is stopped
      assertFalse(thread1.isAlive(), "thread1 should not be alive after stopThreads()");
      assertFalse(thread1.isInterrupted(), "thread1 should not be interrupted after stopThreads()");

      // check if thread2 is stopped
      assertFalse(thread2.isAlive(), "thread2 should not be alive after stopThreads()");
      assertFalse(thread2.isInterrupted(), "thread2 should not be interrupted after stopThreads()");

      // check if thread3 is still running
      assertTrue(thread3.isAlive(), "thread3 should still be alive after stopThreads()");
      assertFalse(thread3.isInterrupted(), "thread3 should not be interrupted after stopThreads()");

      memory.stopThreads();

      // check if thread3 is stopped
      assertFalse(thread3.isAlive(), "thread3 should not be alive after stopThreads() called twice");
      assertTrue(thread3.isInterrupted(), "thread3 should be interrupted after stopThreads() called twice");
   }

   @Test
   public void testGetRevision() {
      Memory memory = new Memory();
      String expected = "8034";
      String actual = memory.getRevision();
      assertEquals("getRevision() should return: " + expected, expected, actual);
   }

   @Test
   public void testMain() {
      Memory memory = new Memory();
      String expectedInitial = "Initial memory: " + Utils.doubleToString(toMegaByte(memory.getInitial()), 1) + "MB (" + memory.getInitial() + ")";
      String expectedMax = "Max memory: " + Utils.doubleToString(toMegaByte(memory.getMax()), 1) + "MB (" + memory.getMax() + ")";
      String actualOutput = "";
      try {
         Memory.main(new String[0]);
         String output = System.out.getHistory(0);
         actualOutput = output;
      } catch (Exception e) {}

      assertTrue(actualOutput.contains(expectedInitial), "Main() should print the Initial memory value correctly");
      assertTrue(actualOutput.contains(expectedMax), "Main() should print the Max memory value correctly");
   }
}
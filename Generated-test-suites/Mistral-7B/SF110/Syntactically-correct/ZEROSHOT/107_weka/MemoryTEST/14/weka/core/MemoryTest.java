package weka.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

	@Test
	public void testMemoryInitialization() {
		Memory memory = new Memory();
		Memory memoryWithGUI = new Memory(true);

		assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal(), 0);
		assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax(), 0);
		assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent(), 0);
		assertEquals(false, memory.isUseGUI());
		assertEquals(false, memory.isEnabled());
		assertEquals(0, memory.getInitial());

		assertEquals(Runtime.getRuntime().totalMemory(), memoryWithGUI.getTotal(), 0);
		assertEquals(Runtime.getRuntime().maxMemory(), memoryWithGUI.getMax(), 0);
		assertEquals(Runtime.getRuntime().totalMemory(), memoryWithGUI.getCurrent(), 0);
		assertEquals(true, memoryWithGUI.isUseGUI());
		assertEquals(true, memoryWithGUI.isEnabled());
		assertEquals(0, memoryWithGUI.getInitial());
	}

	@Test
	public void testMemorySetEnabled() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		assertTrue(memory.isEnabled());
		memory.setEnabled(false);
		assertFalse(memory.isEnabled());
	}

	@Test
	public void testMemoryGetUseGUI() {
		Memory memory = new Memory();
		memory.setUseGUI(true);
		assertTrue(memory.getUseGUI());
		memory.setUseGUI(false);
		assertFalse(memory.getUseGUI());
	}

	@Test
	public void testMemoryGetInitial() {
		Memory memory = new Memory();
		Runtime previousRuntime = Runtime.getRuntime();
		memory.setEnabled(true);
		long initial = memory.getInitial();
		memory.setEnabled(false);
		assertEquals(previousRuntime.totalMemory(), initial, 0);
	}

	@Test
	public void testMemoryGetCurrent() {
		Memory memory = new Memory();
		Runtime previousRuntime = Runtime.getRuntime();
		memory.setEnabled(true);
		memory.getCurrent();
		assertEquals(previousRuntime.totalMemory(), memory.getCurrent(), 0);
	}

	@Test
	public void testMemoryGetMax() {
		Memory memory = new Memory();
		long max = memory.getMax();
		assertEquals(Runtime.getRuntime().maxMemory(), max, 0);
	}

	@Test
	public void testMemoryIsOutOfMemory() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		memory.setInitial(Runtime.getRuntime().totalMemory() * 2);
		assertFalse(memory.isOutOfMemory());

		Runtime previousRuntime = Runtime.getRuntime();
		Runtime newRuntime = Runtime.getRuntime();
		newRuntime.gc();
		newRuntime.runFinalization();
		newRuntime.runFinalizersOnExit();

		memory.setEnabled(true);
		memory.setInitial(previousRuntime.totalMemory());
		assertTrue(memory.isOutOfMemory());
	}

	@Test(expected = IllegalThreadStateException.class)
	public void testMemoryStopThreads() {
		Memory memory = new Memory();
		memory.stopThreads();
	}

	@Test
	public void testMemoryToMegaByte() {
		long bytes = 1024 * 1024 * 1024; // 1GB
		assertEquals(1, Memory.toMegaByte(bytes), 0);
	}

	@Test
	public void testMemoryShowOutOfMemory() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		memory.showOutOfMemory();
	}

	@Test
	public void testMemoryMain() {
		Memory memory = new Memory();
		String[] args = {};
		memory.main(args);
		Memory memory2 = new Memory();
		assertEquals(Utils.doubleToString(Memory.toMegaByte(memory.getInitial()), 1), "0.0MB (0)", "");
		assertEquals(Utils.doubleToString(Memory.toMegaByte(memory.getMax()), 1), "X.XGB (X)", "");
	}

	@BeforeClass
	public static void setUpOnce() {
		Memory.m_Enabled = true;
		Memory.m_Initial = Runtime.getRuntime().totalMemory();
	}

	@AfterClass
	public static void tearDownOnce() {
		Memory.m_Enabled = false;
	}

}
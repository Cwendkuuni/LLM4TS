package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

	@Test
	public void testMemoryConstructor() {
		Memory memory = new Memory();
		assertEquals(false, memory.m_UseGUI);
		Memory memoryWithGUI = new Memory(true);
		assertEquals(true, memoryWithGUI.m_UseGUI);
		Runtime runtime = Runtime.getRuntime();
		long initial = memory.getInitial();
		long total = memory.getCurrent();
		long max = memory.getMax();
		assertEquals(initial, runtime.totalMemory() / 1024 / 1024);
		assertEquals(total, runtime.totalMemory() / 1024 / 1024);
		assertEquals(max, runtime.maxMemory() / 1024 / 1024);
	}

	@Test
	public void testIsEnabled() {
		Memory memory = new Memory();
		assertFalse(memory.isEnabled());
		Memory.setEnabled(true);
		memory = new Memory();
		assertTrue(memory.isEnabled());
	}

	@Test
	public void testIsOutOfMemory() {
		Memory memory = new Memory();
		assertFalse(memory.isOutOfMemory());
		Runtime runtime = Runtime.getRuntime();
		memory.m_Max = runtime.maxMemory() - 100_000_000;
		assertTrue(memory.isOutOfMemory());
		memory.m_Total += 200_000_000;
		assertTrue(memory.isOutOfMemory());
	}

	@Test
	public void testShowOutOfMemory() {
		Memory memory = new Memory();
		memory.isEnabled = true;
		memory.showOutOfMemory();
	}

	@Test(expected = NullPointerException.class)
	public void testStopThreadsNullArray() {
		Memory memory = new Memory();
		memory.stopThreads();
		Thread[] threads = null;
		memory.stopThreads();
	}
}
package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void testIsEnabled() {
		Memory memory = new Memory();
		assertFalse(memory.isEnabled());
		memory.setEnabled(true);
		assertTrue(memory.isEnabled());
	}

	@Test
	public void testGetUseGUI() {
		Memory memory = new Memory();
		assertFalse(memory.getUseGUI());
		memory = new Memory(true);
		assertTrue(memory.getUseGUI());
	}

	@Test
	public void testGetInitial() {
		Memory memory = new Memory();
		long initial = Runtime.getRuntime().totalMemory();
		assertEquals(initial, memory.getInitial());
	}

	@Test
	public void testGetCurrent() {
		Memory memory = new Memory();
		long current = Runtime.getRuntime().totalMemory();
		assertEquals(current, memory.getCurrent());
	}

	@Test
	public void testGetMax() {
		Memory memory = new Memory();
		Runtime runtime = Runtime.getRuntime();
		long max = runtime.maxMemory();
		assertEquals(max, memory.getMax());
	}

	@Test
	public void testIsOutOfMemory() {
		Memory memory = new Memory();
		assertFalse(memory.isOutOfMemory());
		memory.setEnabled(true);
		Runtime runtime = Runtime.getRuntime();
		long max = runtime.maxMemory();
		long current = max * 0.8f;
		memory.m_Total = current;
		assertTrue(memory.isOutOfMemory());
	}

	@Test
	public void testShowOutOfMemory() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		Runtime runtime = Runtime.getRuntime();
		long max = runtime.maxMemory();
		long current = max * 0.8f;
		memory.m_Total = current;
		memory.showOutOfMemory();
	}

	@Test(expected = NullPointerException.class)
	public void testStopThreadsNullPointerException() {
		Memory memory = new Memory();
		memory.stopThreads();
	}

	@Test
	public void testRevision() {
		Memory memory = new Memory();
		assertEquals("$Revision: 8034 $", memory.getRevision());
	}

	@Test
	public void testMemoryInitialAndMax() {
		Memory memory = new Memory();
		long initial = Runtime.getRuntime().totalMemory();
		long max = Runtime.getRuntime().maxMemory();
		assertEquals(initial, memory.getInitial());
		assertEquals(max, memory.getMax());
	}

	@Test
	public void testOutOfMemoryWithInitialGreaterThanMax() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		Runtime runtime = Runtime.getRuntime();
		long max = runtime.maxMemory() * 0.5f;
		memory.m_Initial = max;
		assertFalse(memory.isOutOfMemory());
		memory.m_Total = max * 0.9f;
		assertFalse(memory.isOutOfMemory());
		memory.m_Total = max * 0.99f;
		assertTrue(memory.isOutOfMemory());
	}

	@Test
	public void testToMegaByte() {
		Memory memory = new Memory();
		long bytes = 1024 * 1024 * 2;
		assertEquals(2.0, memory.toMegaByte(bytes), 0.0);
	}

	@Test
	public void testMain() {
		Memory memory = new Memory();
		Memory.main(new String[0]);
	}
}
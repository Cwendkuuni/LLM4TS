package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

	Memory memory;
	Memory memoryWithGUI;
	long initialMemory;
	long maxMemory;
	long totalMemory;

	@Test
	public void testMemoryConstructor() {
		memory = new Memory();
		memoryWithGUI = new Memory(true);
		assertNotNull(memory);
		assertNotNull(memoryWithGUI);
	}

	@Test
	public void testIsEnabled() {
		Memory.m_Enabled = true;
		memory = new Memory();
		assertTrue(memory.isEnabled());
		Memory.m_Enabled = false;
		memory = new Memory();
		assertFalse(memory.isEnabled());
	}

	@Test
	public void testGetUseGUI() {
		memory = new Memory();
		assertFalse(memory.getUseGUI());
		memoryWithGUI = new Memory(true);
		assertTrue(memoryWithGUI.getUseGUI());
	}

	@Test
	public void testGetInitial() {
		memory = new Memory();
		initialMemory = memory.getInitial();
		assertNotNull(initialMemory);
		maxMemory = memory.getMax();
		totalMemory = memory.getCurrent();
		assertEquals(initialMemory, Memory.m_Initial);
		assertEquals(maxMemory, Runtime.getRuntime().maxMemory());
		assertEquals(totalMemory, Runtime.getRuntime().totalMemory());
	}

	@Test
	public void testGetCurrent() {
		memory = new Memory();
		memory.getCurrent();
		totalMemory = memory.getCurrent();
		assertEquals(totalMemory, Runtime.getRuntime().totalMemory());
	}

	@Test
	public void testGetMax() {
		memory = new Memory();
		maxMemory = memory.getMax();
		assertEquals(maxMemory, Runtime.getRuntime().maxMemory());
	}

	@Test
	public void testIsOutOfMemory() {
		memory = new Memory();
		assertFalse(memory.isOutOfMemory());
		totalMemory += 5000000;
		assertTrue(memory.isOutOfMemory());
	}

	@Test(expected = NullPointerException.class)
	public void testToMegaByteNullBytes() {
		assertNull(Memory.toMegaByte(null));
	}

	@Test
	public void testToMegaByteValidBytes() {
		long bytes = 1234567890L;
		double expected = 115.223426;
		assertEquals(expected, Memory.toMegaByte(bytes), 0.0);
	}

	@Test
	public void testShowOutOfMemory() {
		memory = new Memory();
		memory.setEnabled(true);
		memory.showOutOfMemory();
	}

	@Test(expected = SecurityException.class)
	public void testStopThreads() {
		memory = new Memory();
		Thread thread = new Thread(() -> {});
		thread.start();
		memory.stopThreads();
	}

	@Test
	public void testGetRevision() {
		memory = new Memory();
		String expected = "8034";
		String result = memory.getRevision();
		assertEquals(expected, result);
	}

	@Test
	public void testMain() {
		Memory.main(new String[0]);
	}

	@Test(expected = IllegalThreadStateException.class)
	public void testStopThreadsWithIllegalThreadStateException() {
		memory = new Memory();
		Thread thread = new Thread(() -> {
			throw new IllegalThreadStateException();
		});
		thread.start();
		memory.stopThreads();
	}

	@Test(expected = NullPointerException.class)
	public void testStopThreadsWithNullThreads() {
		memory = new Memory();
		Thread[] threadGroup = null;
		memory.stopThreads();
	}
}
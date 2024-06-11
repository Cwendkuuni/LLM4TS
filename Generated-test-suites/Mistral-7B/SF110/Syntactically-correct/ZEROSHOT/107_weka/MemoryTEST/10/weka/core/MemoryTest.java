package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

	Memory memory;
	Memory memoryWithGUI;

	@Test
	public void testMemory() {
		memory = new Memory();
		memoryWithGUI = new Memory(true);

		// Test constructor without GUI
		assertFalse(memory.isUseGUI());
		assertEquals(memory.getInitial(), Memory.m_Initial);
		assertEquals(memory.getCurrent(), memory.getTotal());
		assertEquals(memory.getMax(), Memory.m_Max);
		assertFalse(memory.isOutOfMemory());

		// Test constructor with GUI
		assertTrue(memoryWithGUI.isUseGUI());
		assertEquals(memoryWithGUI.getInitial(), Memory.m_Initial);
		assertEquals(memoryWithGUI.getCurrent(), memoryWithGUI.getTotal());
		assertEquals(memoryWithGUI.getMax(), Memory.m_Max);
		assertFalse(memoryWithGUI.isOutOfMemory());

		// Test getters
		assertTrue(memory.isEnabled());
		memory.setEnabled(false);
		assertFalse(memory.isEnabled());
		assertEquals(memory.getUseGUI(), false);
		memory.setUseGUI(true);
		assertEquals(memory.getUseGUI(), true);
		assertEquals(memory.getInitial(), Memory.m_Initial);
		assertEquals(memory.getCurrent(), memory.getTotal());
		assertEquals(memory.getMax(), Memory.m_Max);
		assertFalse(memory.isOutOfMemory());

		// Test isOutOfMemory
		memory.setEnabled(true);
		Memory.m_Initial += 500000000;
		Memory.m_Max -= 100000000;
		assertTrue(memory.isOutOfMemory());
		memory.setEnabled(false);
		assertFalse(memory.isOutOfMemory());
	}

	@Test(expected = NullPointerException.class)
	public void testStopThreadsNullPointerException() {
		memory = new Memory();
		memory.stopThreads();
	}

	@Test(expected = SecurityException.class)
	public void testStopThreadsSecurityException() {
		memory = new Memory();
		Thread thread = new Thread(() -> {});
		memory.stopThreads();
		thread.start();
		memory.stopThreads();
	}

	@Test(expected = IllegalThreadStateException.class)
	public void testStopThreadsIllegalThreadStateException() {
		memory = new Memory();
		Thread thread = new Thread(() -> {
			synchronized (this) {
				while (true) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		memory.stopThreads();
	}

	@Test
	public void testShowOutOfMemory() {
		memory = new Memory();
		memory.setEnabled(true);
		Memory.m_Initial += 500000000;
		Memory.m_Max -= 100000000;
		memory.showOutOfMemory();
	}
}
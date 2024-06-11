package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void testMemoryInitialization() {
		Memory memory = new Memory();
		long initial = memory.getInitial();
		long max = memory.getMax();

		// Check if initial and max memory are correctly retrieved
		assertEquals(initial, Runtime.getRuntime().totalMemory());
		assertEquals(max, Runtime.getRuntime().maxMemory());
	}

	@Test
	public void testMemoryIsOutOfMemory() {
		Memory memory = new Memory();
		long usedMemory = memory.getTotal() + 200000000L; // Adding a huge amount of memory usage

		// Check if memory is out of memory
		assertTrue(memory.isOutOfMemory());
	}

	@Test
	public void testMemoryShowOutOfMemoryMessage() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		long initial = memory.getInitial();
		long total = memory.getTotal();
		long max = memory.getMax();

		// Simulate memory exhaustion
		memory.setTotal(initial + max);

		// Check if showOutOfMemory message is displayed
		memory.showOutOfMemory();
	}

	@Test
	public void testMemoryEnableAndDisable() {
		Memory memory = new Memory();
		boolean isEnabled = memory.isEnabled();

		// Enable memory
		memory.setEnabled(true);
		assertTrue(memory.isEnabled());

		// Disable memory
		memory.setEnabled(false);
		assertFalse(memory.isEnabled());
	}

	@Test(expected = NullPointerException.class)
	public void testMemoryThreadInterruption() {
		Memory memory = new Memory();
		Thread[] threads = new Thread[100];

		// Simulate a large number of threads
		for (int i = 0; i < threads.length; i++) {
			Thread t = new Thread(() -> {
				memory.stopThreads();
			});
			threads[i] = t;
			t.start();
		}

		// Interrupt all threads
		for (Thread thread : threads) {
			thread.interrupt();
		}

		// This Should throw a NullPointerException due to the deprecated method stop()
		memory.stopThreads();
	}
}
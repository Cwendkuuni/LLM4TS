import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import weka.core.Memory;
import weka.core.RevisionUtils;
import weka.core.Utils;

public class MemoryTest {

	Memory memory;
	Random random;

	@Before
	public void setUp() {
		memory = new Memory();
		random = new Random();
	}

	@Test
	public void testIsEnabled() {
		// Default value
		assertTrue(Memory.m_Enabled);

		// Set to false and check
		Memory.m_Enabled = false;
		assertFalse(Memory.m_Enabled);

		// Set back to true and check
		Memory.m_Enabled = true;
		assertTrue(Memory.m_Enabled);
	}

	@Test
	public void testIsOutOfMemory() {
		// Initial total memory is less than max memory
		Memory memory1 = new Memory(true);
		assertFalse(memory1.isOutOfMemory());

		// Total memory exceeds max memory
		long maxMemory = memory1.getMax() * 2;
		memory1.setEnabled(true);
		for (int i = 0; i < 100; i++) {
			byte[] data = new byte[1024 * 1024]; // 1MB
			System.arraycopy(data, 0, new byte[maxMemory], 0, maxMemory);
		}
		assertTrue(memory1.isOutOfMemory());
	}

	@Test(expected = HeadlessException.class)
	public void testShowOutOfMemory() {
		Memory memory1 = new Memory();
		memory1.showOutOfMemory();
	}

	@Test
	public void testToMegaByte() {
		long bytes = 1234567890L;
		double megabytes = Memory.toMegaByte(bytes);
		assertEquals(11.3, megabytes, 0.01); // Approximate test due to floating point issues
	}

	@Test
	public void testSetEnabled() {
		Memory memory1 = new Memory();
		memory1.setEnabled(true);
		assertTrue(Memory.m_Enabled);

		Memory memory2 = new Memory();
		memory2.setEnabled(false);
		assertFalse(Memory.m_Enabled);
	}

	@Test
	public void testGetTotal() throws InterruptedException, InvocationTargetException, IllegalAccessException {
		Memory memory1 = new Memory();
		long totalMemory = memory1.getTotal();
		long initialTotalMemory = memory1.getInitial();

		Method getCurrentMethod = Memory.class.getMethod("getCurrent");
		getCurrentMethod.setAccessible(true);

		// Simulate memory usage and check total memory
		long maxMemory = memory1.getMax() * 2;
		memory1.setEnabled(true);
		for (int i = 0; i < 100; i++) {
			byte[] data = new byte[1024 * 1024]; // 1MB
			System.arraycopy(data, 0, new byte[maxMemory], 0, maxMemory);
			Thread.sleep(random.nextInt(1000));
			long newTotalMemory = ((Long) getCurrentMethod.invoke(memory1)).longValue();
			assertEquals(totalMemory + (i + 1) * 1024 * 1024, newTotalMemory);
		}
	}

	@Test
	public void testMain() {
		Memory.main(new String[0]);
	}
}
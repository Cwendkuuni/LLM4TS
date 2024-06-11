import static org.junit.Assert.*;

public class MemoryTest {
    private Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testIsEnabled() {
        assertEquals(true, memory.isEnabled());
    }

    @Test
    public void testIsEnabledFalse() {
        memory.setEnabled(false);
        assertEquals(false, memory.isEnabled());
    }

    @Test
    public void testGetInitialAtMaxLimit() {
        long initial = Long.MAX_VALUE;
        Memory memory = new Memory(false);
        assertEquals(initial, memory.getInitial());
    }

    @Test
    public void testCurrentMemoryAtMaxLimit() {
        Memory memory = new Memory() {
            @Override
            public long totalMemory() {
                return Long.MAX_VALUE;
            }
        };
        assertEquals(Long.MAX_VALUE, memory.getCurrent());
    }

    @Test
    public void testIsOutOfMemoryDifferentConditions() {
        Memory memory = new Memory();
        long max = memory.getMax() - 100000L;
        memory.setEnabled(true);
        memory.setInitial(max + 100000L);

        // Test with current memory equal to max
        memory.setTotal(max);
        assertEquals(true, memory.isOutOfMemory());

        // Test with current memory less than max
        memory.setTotal(max - 1);
        assertEquals(false, memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByteNegativeNumber() {
        long bytes = -1;
        assertEquals(-1, Memory.toMegaByte(bytes));
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.setEnabled(true);
        memory.setInitial(10);
        memory.setMax(20);
        memory.setTotal(15);
        memory.showOutOfMemory();
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }
        memory.stopThreads();
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        assertEquals("$Revision: 8034 $", memory.getRevision());
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        Memory.main(new String[0]);
    }
}
package weka.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class MemoryTest {

    private Memory memory;

    @Test
    public void testMemory() {
        memory = new Memory();
        assertEquals(0, memory.getUseGUI());
        memory = new Memory(true);
        assertEquals(true, memory.getUseGUI());
    }

    @Test
    public void testIsEnabled() {
        Memory.m_Enabled = true;
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        Memory.m_Enabled = false;
        memory = new Memory();
        assertFalse(memory.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        memory = new Memory();
        assertFalse(memory.getUseGUI());

        memory = new Memory(true);
        assertTrue(memory.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        memory = new Memory();
        long initial = memory.getInitial();
        long total = memory.getCurrent();
        assertEquals(initial, total);

        memory = new Memory();
        initial = Memory.m_Initial;
        total = Runtime.getRuntime().totalMemory();
        assertEquals(initial, total);
    }

    @Test
    public void testGetCurrent() {
        memory = new Memory();
        long initial = memory.getInitial();
        long total = memory.getCurrent();
        assertEquals(initial, total);

        Runtime runtime = Runtime.getRuntime();
        total += 1000000;
        memory = new Memory();
        assertEquals(total, memory.getCurrent());
    }

    @Test
    public void testGetMax() {
        memory = new Memory();
        long max = memory.getMax();
        long total = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        assertEquals(max, total - freeMemory);

        memory = new Memory();
        max = Memory.m_Max;
        total = Runtime.getRuntime().totalMemory();
        freeMemory = Runtime.getRuntime().freeMemory();
        assertEquals(max, total - freeMemory);
    }

    @Test
    public void testIsOutOfMemory() {
        memory = new Memory();
        long max = Memory.m_Max;
        long total = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        Memory.m_Enabled = true;
        boolean outOfMemory = memory.isOutOfMemory();
        assertFalse(outOfMemory);

        total += 5000000;
        memory = new Memory();
        outOfMemory = memory.isOutOfMemory();
        assertTrue(outOfMemory);

        Memory.m_Enabled = false;
        total -= 5000000;
        memory = new Memory();
        assertFalse(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        assertEquals(4.5, Memory.toMegaByte(4611686016.0), 0.0001);
        assertEquals(5.5, Memory.toMegaByte(580983078400.0), 0.0001);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();
        memory.showOutOfMemory();
        String expected = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   0.0MB (0)\n" +
                "- total memory used: 0.0MB (0)\n- max. memory avail.: 0.0MB (0)\n\n" +
                "Note:\n" +
                "The Java heap size can be specified with the -Xmx option.\n" +
                "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                "   java -Xmx128m -classpath ...\n" +
                "This does NOT work in the SimpleCLI, the java command refers\n" +
                "to the one with which Weka is started.";
        String actual = "";
        try {
            StringWriter writer = new StringWriter(actual);
            PrintWriter printWriter = new PrintWriter(writer);
            memory.showOutOfMemory();
            printWriter.flush();
            actual = writer.toString();
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        assertEquals(expected, actual);

        memory = new Memory();
        memory.setEnabled(false);
        memory.showOutOfMemory();
        expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testStopThreads() {
        memory = new Memory();
        memory.stopThreads();
        Thread[] threads = Thread.getAllThreads();
        for (Thread thread : threads) {
            if (thread != null && thread != Thread.currentThread()) {
                assertFalse(thread.isAlive());
            }
        }
    }

    @Test
    public void testGetRevision() {
        memory = new Memory();
        String revision = memory.getRevision();
        String expected = "$Revision: 8034 $";
        assertEquals(expected, revision);
    }

    @Test
    public void testMain() {
        Memory.main(new String[0]);
        String output = "";
        Runtime runtime = Runtime.getRuntime();
        InputStream inputStream = System.in;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setIn(new ByteArrayInputStream(new byte[0]));
        System.setOut(printStream);
        try {
            Memory.main(new String[0]);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        } finally {
            System.setIn(inputStream);
            System.setOut(System.out);
        }
        output += byteArrayOutputStream.toString();
        String expected = "Initial memory: 0.0MB (0)\nMax memory: 0.0MB (0)\n";
        assertTrue(output.contains(expected));
    }
}
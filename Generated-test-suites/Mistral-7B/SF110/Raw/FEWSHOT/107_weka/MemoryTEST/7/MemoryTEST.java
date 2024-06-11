package weka.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertGreaterEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Dialog;
import java.awt.event.WindowEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MemoryTest {

    @Test
    public void testIsEnabled() {
        Memory memory = new Memory();
        assertTrue(memory.isEnabled());

        Memory memoryDisabled = new Memory();
        memoryDisabled.setEnabled(false);
        assertFalse(memoryDisabled.isEnabled());
    }

    @Test
    public void testGetUseGUI() {
        Memory memory = new Memory();
        assertFalse(memory.getUseGUI());

        Memory memoryWithGUI = new Memory(true);
        assertTrue(memoryWithGUI.getUseGUI());
    }

    @Test
    public void testGetInitial() {
        Memory memory = new Memory();
        long initial = memory.getInitial();
        Memory newMemory = new Memory();
        long newInitial = newMemory.getInitial();

        assertNotEquals(initial, newInitial);
    }

    @Test
    public void testGetCurrent() {
        Memory memory = new Memory();
        long current = memory.getCurrent();
        Memory newMemory = new Memory();
        long newCurrent = newMemory.getCurrent();

        assertNotEquals(current, newCurrent);
    }

    @Test
    public void testGetMax() {
        Memory memory = new Memory();
        long max = memory.getMax();
        Memory newMemory = new Memory();
        long newMax = newMemory.getMax();

        assertNotEquals(max, newMax);
    }

    @Test
    public void testIsOutOfMemory() {
        Memory memory = new Memory();

        assertFalse(memory.isOutOfMemory());

        memory.setEnabled(true);
        memory.setMax(memory.getInitial() - 100000L);
        assertTrue(memory.isOutOfMemory());
    }

    @Test
    public void testToMegaByte() {
        long bytes = 123456789;
        double expected = 11.75;

        assertEquals(expected, Memory.toMegaByte(bytes), 0.01);
    }

    @Test
    public void testShowOutOfMemory() {
        Memory memory = new Memory();

        memory.setEnabled(true);
        memory.setUseGUI(true);

        String message = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   11.0MB (11796480)\n" +
                         "- total memory used: 12.0MB (12582912)\n" +
                         "- max. memory avail.: 15.0MB (15728640)\n" +
                         "\n" +
                         "Note:\n" +
                         "The Java heap size can be specified with the -Xmx option.\n" +
                         "E.g., to use 128MB as heap size, the command line looks like this:\n" +
                         "   java -Xmx128m -classpath ...\n" +
                         "This does NOT work in the SimpleCLI, the java command refers\n" +
                         "to the one with which Weka is started.";

        Memory spyMemory = Mockito.spy(memory);
        spyMemory.showOutOfMemory();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(new Dialog(Mockito.any(Frame.class), Mockito.eq(false)), Mockito.times(1)).setMessage(messageCaptor.capture());
        assertEquals(message, messageCaptor.getValue());
    }

    @Test
    public void testStopThreads() {
        Memory memory = new Memory();
        memory.stopThreads();
    }

    @Test(expected = NullPointerException.class)
    public void testStopThreadsWithNullThreadGroup() {
        Memory memory = new Memory();
        memory.stopThreads(null);
    }

    @Test
    public void testGetRevision() {
        Memory memory = new Memory();
        String revision = memory.getRevision();
        assertNotNull(revision);
    }

    @Test
    public void testMain() {
        Memory memory = new Memory();
        memory.main(new String[0]);

        String expectedInitial = "Initial memory: 11.0MB (11796480)";
        String expectedMax = "Max memory: 15.0MB (15728640)";

        assertTrue(memory.isOutOfMemory());
        memory.showOutOfMemory();

        Memory newMemory = new Memory();
        newMemory.main(new String[0]);

        String actualInitial = newMemory.getInitial() > 0 ? "Initial memory: " + Utils.doubleToString(Memory.toMegaByte(newMemory.getInitial()), 1) + "MB (" + newMemory.getInitial() + ")" : "Initial memory: N/A";
        String actualMax = "Max memory: " + Utils.doubleToString(Memory.toMegaByte(newMemory.getMax()), 1) + "MB (" + newMemory.getMax() + ")";

        assertTrue(newMemory.isEnabled());

        assertEquals(expectedInitial, actualInitial);
        assertEquals(expectedMax, actualMax);
    }
}
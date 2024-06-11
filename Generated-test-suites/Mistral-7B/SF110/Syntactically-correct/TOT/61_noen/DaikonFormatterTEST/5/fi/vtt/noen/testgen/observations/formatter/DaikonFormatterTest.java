package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.parser.*;

public class DaikonFormatterTest {
    private DaikonFormatter formatter;

    @Before
    public void setUp() {
        List<String> inputNames = Arrays.asList("method1", "method2");
        try {
            formatter = new DaikonFormatter("test.dtrace", inputNames);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to initialize DaikonFormatter");
        }
    }

    @Test
    public void testDaikonFormatterConstruction() {
        assertNotNull(formatter);
        assertEquals(false, formatter.isSimple());
        assertEquals(new HashSet<>(inputNames), formatter.getInputs());
    }

    @Test
    public void testDaikonFormatterConstructionWithInputs() {
        assertNotNull(formatter);
        assertEquals(false, formatter.isSimple());
        assertEquals(new HashSet<>(inputNames), formatter.getInputs());
    }

    @Test
    public void testFormatterMethods() throws Exception {
        ProgramRun programRun = new ProgramRun();
        Event event1 = new Event("method1", "Enter");
        Event event2 = new Event("method2", "Exit");
        Event event3 = new Event("method1", "Exit");
        programRun.addEvent(event1);
        programRun.addEvent(event2);
        programRun.addEvent(event3);

        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("method1", "method2"));

        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertEquals("", formatter.footer());
        assertEquals("dtrace", formatter.fileNameExtension());

        String observations = formatter.observations(programRun);

        assertTrue(observations.contains("ppt method1"));
        assertTrue(observations.contains("ppt method2"));
        assertTrue(observations.contains("ppt method1:::EXIT1"));
        assertTrue(observations.contains("ppt method1:::ENTER"));
    }
}
package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.OutputBuffer;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterInitialization() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
            assertNotNull(formatter);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize formatter", e);
        }
    }

    @Test
    public void testDaikonFormatterInitializationWithInputs() {
        try {
            List<String> inputList = Arrays.asList("Method1", "Method2");
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", inputList);
            assertNotNull(formatter);
            assertEquals(inputList, formatter.inputs);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize formatter", e);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterInitializationWithInvalidInputs() {
        try {
            List<String> inputList = Collections.emptyList();
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", inputList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize formatter", e);
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
        OutputBuffer out = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", out.toString());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() {
        Properties configuration = new Properties();
        configuration.put("InputInterface", "fi.vtt.noen.testgen.testinterfaces.SimpleInputInterface");

        try (InputStream inputStream = new ByteArrayInputStream(new byte[]{})) {
            ProgramRun run = new ProgramRun("TestClass", inputStream);
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
            String result = formatter.observations(run);
            assertEquals("", result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load input and output interface definitions for daikon formatter", e);
        }

        Event event1 = new Event("Method1", "INPUT_1", new ArrayList<EventAttribute>(), null);
        Event event2 = new Event("Method2", "OUTPUT_1", new ArrayList<EventAttribute>(), null);
        Event event3 = new Event("Method1", "INPUT_2", new ArrayList<EventAttribute>(), null);
        ProgramRun run = new ProgramRun("TestClass", null);
        run.addEvent(event1);
        run.addEvent(event2);
        run.addEvent(event3);

        try {
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
            String result = formatter.observations(run);
            String expected = "ppt Method1\n"
                    + "ppt-type enter\n"
                    + "ppt Method1:::ENTER\n"
                    + "ppt Method1\n"
                    + "ppt-type enter\n"
                    + "ppt Method1:::ENTER\n";
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate daikon format", e);
        }
    }

    @Test
    public void testObservationsWithInputs() {
        Properties configuration = new Properties();
        configuration.put("InputInterface", "fi.vtt.noen.testgen.testinterfaces.InputInterface1");
        configuration.put("OutputInterface", "fi.vtt.noen.testgen.testinterfaces.OutputInterface1");

        try (InputStream inputStream = new ByteArrayInputStream(new byte[]{})) {
            ProgramRun run = new ProgramRun("TestClass", inputStream);
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
            String result = formatter.observations(run);
            assertEquals("", result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load input and output interface definitions for daikon formatter", e);
        }

        Event event1 = new Event("Method1", "INPUT_1", new ArrayList<EventAttribute>(), null);
        Event event2 = new Event("Method2", "OUTPUT_1", new ArrayList<EventAttribute>(), null);
        Event event3 = new Event("Method1", "INPUT_2", new ArrayList<EventAttribute>(), null);
        Event event4 = new Event("Method3", "OUTPUT_2", new ArrayList<EventAttribute>(), null);
        ProgramRun run = new ProgramRun("TestClass", null);
        run.addEvent(event1);
        run.addEvent(event2);
        run.addEvent(event3);
        run.addEvent(event4);

        try {
            DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
            String result = formatter.observations(run);
            String expected = "ppt Method1\n"
                    + "ppt-type enter\n"
                    + "ppt Method1:::ENTER\n"
                    + "ppt Method1\n"
                    + "ppt-type enter\n"
                    + "ppt Method1:::ENTER\n"
                    + "ppt Method2\n"
                    + "ppt-type enter\n"
                    + "ppt Method2:::ENTER\n"
                    + "ppt Method3\n"
                    + "ppt-type enter\n"
                    + "ppt Method3:::ENTER\n"
                    + "ppt Method1:::EXIT1\n"
                    + "ppt Method1\n"
                    + "ppt-type exit\n"
                    + "ppt Method1:::EXIT\n"
                    + "ppt Method3:::EXIT1\n"
                    + "ppt Method3\n"
                    + "ppt-type exit\n"
                    + "ppt Method3:::EXIT\n"
                    + "ppt Method2:::EXIT1\n"
                    + "ppt Method2\n"
                    + "ppt-type exit\n"
                    + "ppt Method2:::EXIT\n";
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate daikon format", e);
        }
    }

    @Test
    public void testDeclarations() {
        Event event = new Event("Method1", "INPUT_1", new ArrayList<EventAttribute>(), null);
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
        formatter.declarations(event, "Method1", out, "implicit");
        assertEquals("ppt Method1\n"
                + "ppt-type variable\n"
                + "ppt Method1\n"
                + "ppt-type var-kind variable\n"
                + "comparability implicit\n", out.toString());

        event.addAttribute(new EventAttribute("name", "java.lang.String", null, null));
        out = new OutputBuffer();
        formatter.declarations(event, "Method1", out, "explicit");
        assertEquals("ppt Method1\n"
                + "ppt-type variable\n"
                + "ppt Method1\n"
                + "ppt-type var-kind variable\n"
                + "comparability explicit\n"
                + "variable name\n"
                + "dec-type java.lang.String\n"
                + "rep-type java.lang.String\n"
                + "comparability explicit\n", out.toString());
    }

    @Test
    public void testAttributes() {
        Event event = new Event("Method1", "INPUT_1", new ArrayList<EventAttribute>(), null);
        event.addAttribute(new EventAttribute("name", "java.lang.String", null, "John Doe"));
        OutputBuffer out = new OutputBuffer();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
        formatter.attributes(event, out);
        assertEquals("name\"John Doe\"1\n", out.toString());
    }
}
package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    private static final String TESTGEN_PROPERTIES_FILE = "testgen.properties";
    private static final String TEST_INPUTS_FILE = "TestInputs.java";
    private static final String TEST_OUTPUTS_FILE = "TestOutputs.java";

    private Set<String> declarations;
    private DaikonFormatter simpleFormatter;
    private DaikonFormatter complexFormatter;
    private InterfaceParser parser;

    @Before
    public void setup() throws Exception {
        parser = new InterfaceParser();

        // Initialize testgen.properties file
        Properties configuration = new Properties();
        configuration.setProperty("InputInterface", "fi.vtt.noen.testgen.testinput.TestInputs");
        configuration.setProperty("OutputInterface", "fi.vtt.noen.testgen.testoutput.TestOutputs");
        new FileOutputStream(TESTGEN_PROPERTIES_FILE).write(configuration.toString().getBytes());

        // Initialize simple formatter
        simpleFormatter = new DaikonFormatter(TEST_INPUTS_FILE, false);

        // Initialize complex formatter
        complexFormatter = new DaikonFormatter(TEST_INPUTS_FILE, parser.methodNames(new FileInputStream(TEST_INPUTS_FILE)));
    }

    @Test
    public void testHeader() {
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", simpleFormatter.header());
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", complexFormatter.header());
    }

    @Test
    public void testFooter() {
        assertEquals("", simpleFormatter.footer());
        assertEquals("", complexFormatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", simpleFormatter.fileNameExtension());
        assertEquals("dtrace", complexFormatter.fileNameExtension());
    }

    @Test
    public void testInputClassLoading() throws Exception {
        assertEquals(TestInputs.class, complexFormatter.inputs().iterator().next());
        assertEquals(TestInputs.class, complexFormatter.inputs().iterator().next());
    }

    @Test
    public void testOutputClassLoading() throws Exception {
        assertEquals(TestOutputs.class, complexFormatter.outputs().iterator().next());
        assertEquals(TestOutputs.class, complexFormatter.outputs().iterator().next());
    }

    @Test
    public void testSimpleObservations() {
        Event event1 = new Event("testMethod1", "testClass1", new ArrayList<>());
        Event event2 = new Event("testMethod2", "testClass1", new ArrayList<>());
        Iterator<Event> runIterator = Collections.singletonList(event1).iterator();

        String observations = simpleFormatter.observations(new ProgramRun(runIterator));
        String[] lines = observations.split("\n");
        assertEquals(11, lines.length);
        assertEquals("ppt testClass1:::ENTER", lines[0]);
        assertEquals("ppt testMethod1:::ENTER", lines[1]);
        assertEquals("ppt testMethod1", lines[2]);
        assertEquals("ppt testMethod1:::EXIT1", lines[3]);
        assertEquals("ppt testClass1:::EXIT", lines[4]);
        assertEquals("ppt testClass1:::ENTER", lines[5]);
        assertEquals("ppt testMethod2:::ENTER", lines[6]);
        assertEquals("ppt testMethod2", lines[7]);
        assertEquals("ppt testMethod2:::EXIT1", lines[8]);
        assertEquals("ppt testClass1:::EXIT", lines[9]);
        assertEquals("ppt testMethod2", lines[10]);
    }

    @Test
    public void testComplexObservations() throws Exception {
        Event event1 = new Event("testMethod1", "testClass1", Arrays.asList(new EventAttribute("attr1", "type1"), new EventAttribute("attr2", "type2")));
        Event event2 = new Event("testMethod2", "testClass1", Arrays.asList(new EventAttribute("attr1", "type1"), new EventAttribute("attr3", "type3")));
        Iterator<Event> runIterator = Collections.singletonList(event1).iterator();

        String observations = complexFormatter.observations(new ProgramRun(runIterator));
        String[] lines = observations.split("\n");
        assertEquals(22, lines.length);
        assertEquals("ppt testClass1:::ENTER", lines[0]);
        assertEquals("ppt testMethod1:::ENTER", lines[1]);
        assertEquals("ppt testMethod1", lines[2]);
        assertEquals("ppt testMethod1:::EXIT1", lines[3]);
        assertEquals("ppt testClass1:::EXIT", lines[4]);
        assertEquals("ppt testClass1:::ENTER", lines[5]);
        assertEquals("ppt testMethod2:::ENTER", lines[6]);
        assertEquals("ppt testMethod2", lines[7]);
        assertEquals("ppt testMethod2:::EXIT1", lines[8]);
        assertEquals("ppt testClass1:::EXIT", lines[9]);
        assertEquals("ppt testMethod1", lines[10]);
        assertEquals("ppt testMethod2", lines[11]);
        assertEquals("variable attr1", lines[12]);
        assertEquals("var-kind variable", lines[13]);
        assertEquals("dec-type type1", lines[14]);
        assertEquals("rep-type type1", lines[15]);
        assertEquals("comparability implicit", lines[16]);
        assertEquals("variable attr2", lines[17]);
        assertEquals("var-kind variable", lines[18]);
        assertEquals("dec-type type2", lines[19]);
        assertEquals("rep-type type2", lines[20]);
        assertEquals("comparability implicit", lines[21]);
    }

    @Test
    public void testSimplePoint() {
        Event event = new Event("testMethod", "testClass", new ArrayList<>());
        String expected = "ppt testClass:::ENTER\n" +
                          "ppt testMethod\n" +
                          "ppt testMethod:::EXIT1\n" +
                          "ppt testClass:::EXIT\n";

        OutputBuffer out = new OutputBuffer();
        simpleFormatter.simplePoint(event, "10", out);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPoint() {
        Event previousEvent = new Event("testMethod1", "testClass1", new ArrayList<>());
        Event event = new Event("testMethod2", "testClass1", new ArrayList<>());

        String expected = "ppt testClass1:::ENTER\n" +
                          "ppt testMethod1:::ENTER\n" +
                          "ppt testMethod1\n" +
                          "ppt testMethod1:::EXIT1\n" +
                          "ppt testClass1:::EXIT\n" +
                          "ppt testClass1:::ENTER\n" +
                          "ppt testMethod2:::ENTER\n" +
                          "ppt testMethod2\n" +
                          "ppt testMethod2:::EXIT1\n" +
                          "ppt testClass1:::EXIT\n" +
                          "ppt testMethod2\n";

        OutputBuffer out = new OutputBuffer();
        complexFormatter.point(previousEvent.getName(), previousEvent, "22", out);
        complexFormatter.point(event.getName(), event, "22", out);
        assertEquals(expected, out.toString());
    }

    @After
    public void teardown() throws Exception {
        new File("testgen.properties").delete();
    }
}
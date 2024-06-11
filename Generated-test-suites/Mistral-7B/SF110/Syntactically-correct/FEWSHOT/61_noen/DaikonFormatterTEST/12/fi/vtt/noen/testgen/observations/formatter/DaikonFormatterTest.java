package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;

public class DaikonFormatterTest {
    private DaikonFormatter formatter;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        this.outContent = new ByteArrayOutputStream();
        this.formatter = new DaikonFormatter("test.dtrace", Collections.emptyList());
        this.formatter.setOutput(this.outContent);
    }

    @Test
    public void testDaikonFormatter() {
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", this.formatter.header());
        assertNull(this.formatter.footer());
        assertEquals("dtrace\n", this.formatter.fileNameExtension());
    }

    @Test
    public void testInitWithSimpleFlag() throws IOException {
        this.formatter = new DaikonFormatter("test.dtrace", true);
        assertNotNull(this.formatter.inputs);
        assertNull(this.formatter.inputs());
    }

    @Test
    public void testInitWithInputs() throws IOException {
        List<String> inputs = new ArrayList<>();
        inputs.add("method1");
        inputs.add("method2");
        this.formatter = new DaikonFormatter("test.dtrace", inputs);
        assertEquals(inputs, this.formatter.inputs());
    }

    @Test(expected = RuntimeException.class)
    public void testInitWithInvalidInputs() throws IOException, Exception {
        this.formatter = new DaikonFormatter("test.dtrace", null);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classes.size());
        assertEquals(Class.forName("fi.vtt.noen.testgen.parser.Class1"), classes.get(0));
        assertEquals(Class.forName("fi.vtt.noen.testgen.parser.Class2"), classes.get(1));
    }

    @Test(expected = Exception.class)
    public void testClassesForMultiplePropertiesWithInvalidProperty() throws Exception {
        DaikonFormatter.classesForMultipleProperties("InvalidProperty");
    }

    @Test
    public void testPointWithSimpleFlag() throws IOException {
        Event event = new Event("method1", null, null, null, null, null, new ArrayList<>());
        this.formatter.simplePoint(event, "10", this.outContent);
        assertEquals("ppt method1\n", this.outContent.toString());
    }

    @Test
    public void testPointWithoutSimpleFlag() throws IOException {
        Event previousEvent = new Event("method1", null, null, null, null, null, null);
        Event event = new Event("method2", null, null, null, null, null, null);
        this.formatter.point("method1,method2", previousEvent, "11", this.outContent);
        assertEquals("ppt method1:::ENTER\nppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", this.outContent.toString());

        this.outContent.reset();
        this.formatter.point("method1,method2", event, "11", this.outContent);
        assertEquals("ppt method2:::ENTER\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", this.outContent.toString());
    }

    @Test
    public void testObservations() throws IOException {
        List<Event> events = new ArrayList<>();
        events.add(new Event("method1", null, null, null, null, null, null));
        events.add(new Event("method2", null, null, null, null, null, null));
        events.add(new Event("method3", null, null, null, null, null, null));
        Iterator<Event> eventIterator = events.iterator();

        this.formatter.observations(new ProgramRun(eventIterator));

        String expectedOutput = "ppt method1:::ENTER\nppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
                + "ppt method1\n"
                + "ppt method2:::ENTER\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
                + "ppt method2\n"
                + "ppt method3:::ENTER\nppt method3\n  variable method3\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
                + "ppt method3\n";

        assertEquals(expectedOutput, this.outContent.toString());
    }
}
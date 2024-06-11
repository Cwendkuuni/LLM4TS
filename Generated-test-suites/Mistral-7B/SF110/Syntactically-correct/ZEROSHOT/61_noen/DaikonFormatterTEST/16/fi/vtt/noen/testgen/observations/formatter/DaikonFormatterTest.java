package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.Constants;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private InterfaceParser interfaceParser;
    private ProgramRun programRun;

    @Before
    public void setUp() {
        interfaceParser = mock(InterfaceParser.class);
        when(interfaceParser.methodNames(Collections.emptyList())).thenReturn(Arrays.asList("method1", "method2"));
        formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"), interfaceParser);
        programRun = new ProgramRun();
    }

    @After
    public void tearDown() {
        formatter = null;
        interfaceParser = null;
    }

    @Test
    public void testDaikonFormatterHeader() {
        String actual = formatter.header();
        String expected = "decl-version 2.0\nvar-comparability implicit\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterFooter() {
        String actual = formatter.footer();
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterFileNameExtension() {
        String actual = formatter.fileNameExtension();
        String expected = "dtrace";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterSimplePoint() {
        Event event = new Event("method1", "ENTER", Arrays.asList(new EventAttribute("parameter1", "java.lang.String", "value1")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.simplePoint(event, "implicit", new OutputBuffer(outputStream));
        String actual = outputStream.toString();
        String expected = "ppt method1:::ENTER\nvariable parameter1\n  variable-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterPoint() {
        Event previousEvent = new Event("method1", "EXIT", Arrays.asList(new EventAttribute("result", "java.lang.String", "value2")));
        Event event = new Event("method2", "ENTER", Collections.emptyList());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.point("method1,method2", previousEvent, "implicit", new OutputBuffer(outputStream));
        String actual = outputStream.toString();
        String expected = "ppt method1:::EXIT1\nvariable result\n  variable-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterDeclarations() {
        Event event = new Event("method1", "ENTER", Arrays.asList(new EventAttribute("parameter1", "java.lang.String", "value1")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.declarations(event, "method1:::ENTER", new OutputBuffer(outputStream), "implicit");
        String actual = outputStream.toString();
        String expected = "ppt method1:::ENTER\nvariable parameter1\n  variable-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterAttributes() {
        Event event = new Event("method1", "ENTER", Arrays.asList(new EventAttribute("parameter1", "java.lang.String", "value1"), new EventAttribute("parameter2", "int", "1")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.attributes(event, new OutputBuffer(outputStream));
        String actual = outputStream.toString();
        String expected = "parameter1\"value1\"1\nparameter21\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testDaikonFormatterObservations() {
        Event previousEvent = new Event("method1", "EXIT", Collections.emptyList());
        Event event = new Event("method2", "ENTER", Collections.emptyList());
        programRun.addEvent(previousEvent);
        programRun.addEvent(event);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String actual = formatter.observations(programRun);
        String expected = "";
        assertEquals(expected, actual); // Test case for simple formatter

        // Test case for complex formatter
        Set<String> inputs = new HashSet<>(Arrays.asList("method1", "method2"));
        formatter = new DaikonFormatter("test.txt", inputs, interfaceParser);
        previousEvent.addAttribute(new EventAttribute("result1", "java.lang.String", "value1"));
        event.addAttribute(new EventAttribute("result2", "java.lang.String", "value2"));
        programRun.clear();
        programRun.addEvent(previousEvent);
        programRun.addEvent(event);
        outputStream = new ByteArrayOutputStream();
        actual = formatter.observations(programRun);
        expected = "ppt method1:::EXIT1\nvariable result1\n  variable-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n" +
                  "ppt method1:::ENTER\nvariable result1\n  variable-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n" +
                  "ppt method2:::ENTER\nvariable result2\n  variable-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expected, actual);
    }
}

class OutputBuffer extends ByteArrayOutputStream {

    OutputBuffer(OutputStream outputStream) {
        super(outputStream);
    }

    OutputBuffer(ByteArrayOutputStream outputStream) {
        super(outputStream);
    }

    String toString() {
        return new String(this.buf);
    }
}
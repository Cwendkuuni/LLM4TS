package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", true);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertEquals("", formatter.footer());
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertNull(formatter.inputs);
    }

    @Test
    public void testDaikonFormatterWithInputs() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", Arrays.asList("method1", "method2"));
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertEquals("inputs:[method1, method2]\n", formatter.toString());
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:[method1, method2]\n", formatter.header() + formatter.toString());
    }

    @Test
    public void testGetClassesForMultipleProperties() throws Exception {
        List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classes.size());
        assertEquals(ClassA.class, classes.get(0));
        assertEquals(ClassB.class, classes.get(1));
    }

    @Test
    public void testSimplePoint() throws IOException {
        Event event = mock(Event.class);
        when(event.isExit()).thenReturn(false);
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        formatter.simplePoint(event, "10", out);
        assertEquals("ppt method_name:::ENTER\n", out.toString());
    }

    @Test
    public void testPoint() throws IOException {
        Event previousEvent = mock(Event.class);
        Event event = mock(Event.class);
        when(previousEvent.isExit()).thenReturn(true);
        when(event.isExit()).thenReturn(false);
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", Collections.singletonList("method_name"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        formatter.point("method_name", previousEvent, "11", out);
        assertEquals("ppt method_name:::ENTER\n", out.toString());
    }

    @Test
    public void testObservations() throws IOException {
        Event previousEvent = mock(Event.class);
        Event event = mock(Event.class);
        when(previousEvent.isExit()).thenReturn(true);
        when(event.isExit()).thenReturn(false);
        when(previousEvent.getName()).thenReturn("previous_method_name");
        when(event.getName()).thenReturn("method_name");
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", Collections.singletonList("method_name"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Iterator<Event> iterator = Arrays.asList(previousEvent, event).iterator();
        when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn(previousEvent).thenReturn(event);
        String observations = formatter.observations(mock(ProgramRun.class));
        assertEquals("ppt previous_method_name:::ENTER\n" +
                "ppt method_name:::ENTER\n", observations);
    }

    @Test
    public void testDeclarations() throws IOException {
        Event event = mock(Event.class);
        when(event.isExit()).thenReturn(false);
        when(event.getName()).thenReturn("method_name");
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        formatter.simplePoint(event, "10", out);
        assertEquals("ppt method_name:::ENTER\n" +
                "ppt method_name\n" +
                "variable method_name\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.Object\n" +
                "    rep-type java.lang.Object\n" +
                "    comparability implicit\n", formatter.header() + out.toString());
    }

    @Test
    public void testAttributes() throws IOException {
        Event event = mock(Event.class);
        when(event.isExit()).thenReturn(false);
        when(event.getName()).thenReturn("method_name");
        when(event.attributes().iterator().next().getName()).thenReturn("attribute_name");
        when(event.attributes().iterator().next().getType()).thenReturn("java.lang.String");
        when(event.attributes().iterator().next().getValue()).thenReturn("Attribute Value");
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        formatter.simplePoint(event, "10", out);
        assertEquals("ppt method_name:::ENTER\n" +
                "ppt method_name\n" +
                "variable method_name\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.Object\n" +
                "    rep-type java.lang.Object\n" +
                "    comparability implicit\n" +
                "attribute_name\"Attribute Value\"1\n", formatter.header() + out.toString());
    }

    @Test
    public void testPointWithExitEvents() throws IOException {
        Event previousEvent = mock(Event.class);
        Event event = mock(Event.class);
        when(previousEvent.isExit()).thenReturn(true);
        when(event.isExit()).thenReturn(true);
        when(previousEvent.getName()).thenReturn("previous_method_name");
        when(event.getName()).thenReturn("method_name");
        DaikonFormatter formatter = new DaikonFormatter("test.dtr", Collections.singletonList("method_name"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Iterator<Event> iterator = Arrays.asList(previousEvent, event).iterator();
        when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn(previousEvent).thenReturn(event);
        String observations = formatter.observations(mock(ProgramRun.class));
        assertEquals("ppt previous_method_name:::EXIT1\n" +
                "ppt method_name:::ENTER\n" +
                "ppt method_name:::EXIT1\n", observations);
    }
}

class EventAttribute {
    String name;
    String type;
    String value;
}

class Event {
    boolean isExit;
    String name;
    Iterator<EventAttribute> attributes;
}

class ProgramRun {
    Iterator<Event> iterator;
}

class OutputBuffer {
    ByteArrayOutputStream out;

    OutputBuffer() {
        this.out = new ByteArrayOutputStream();
    }

    void append(String s) {
        try {
            this.out.append(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String toString() {
        return this.out.toString();
    }
}

class InterfaceParser {}

class ClassA {}

class ClassB {}
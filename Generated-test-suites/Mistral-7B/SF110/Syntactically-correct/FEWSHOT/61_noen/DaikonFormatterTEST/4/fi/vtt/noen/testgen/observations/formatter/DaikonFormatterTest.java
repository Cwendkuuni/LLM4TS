package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
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

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.Constants;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private ByteArrayOutputStream out;
    private Set<String> declarations;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        formatter = new DaikonFormatter("test.dtrace", false);
        declarations = new HashSet<>();
    }

    @Test
    public void testHeader() {
        String result = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", result);
    }

    @Test
    public void testFooter() {
        String result = formatter.footer();
        assertEquals("", result);
    }

    @Test
    public void testFileNameExtension() {
        String result = formatter.fileNameExtension();
        assertEquals("dtrace", result);
    }

    @Test
    public void testDeclarations() {
        Event event = mock(Event.class);
        when(event.getName()).thenReturn("eventName");
        when(event.isExit()).thenReturn(true);

        formatter.point(event, "comparability", out);

        assertEquals("ppt eventName\nppt-type exit\n", out.toString());

        declarations.clear();

        event = mock(Event.class);
        when(event.getName()).thenReturn("eventName");
        when(event.isExit()).thenReturn(false);

        formatter.simplePoint(event, "comparability", out);

        assertEquals("ppt eventName:::ENTER\n", out.toString());
    }

    @Test
    public void testAttributes() {
        Event event = mock(Event.class);
        when(event.getName()).thenReturn("eventName");
        when(event.isExit()).thenReturn(false);
        List<EventAttribute> attributes = Arrays.asList(
            mock(EventAttribute.class),
            mock(EventAttribute.class)
        );
        when(event.attributes()).thenReturn(attributes.iterator());

        formatter.attributes(event, out);

        String expected = "eventName variable_1 var-kind variable dec-type type_1 rep-type type_1 comparability implicit\neventName variable_2 var-kind variable dec-type type_2 rep-type type_2 comparability implicit\n";

        if (formatter.simple) {
            expected = expected.replaceAll("variable_", "").replaceAll("dec-type ", "").replaceAll("rep-type ", "").replaceAll("comparability ", "");
        }

        assertEquals(expected, out.toString());
    }

    @Test
    public void testObservationsEmptyRun() {
        formatter = new DaikonFormatter("test.dtrace", false);

        String result = formatter.observations(Collections.emptyList());

        assertEquals("", result);
    }

    @Test
    public void testObservationsSimpleRun() {
        Event event1 = mock(Event.class);
        when(event1.getName()).thenReturn("event1");
        when(event1.isExit()).thenReturn(false);

        Event event2 = mock(Event.class);
        when(event2.getName()).thenReturn("event2");
        when(event2.isExit()).thenReturn(false);

        List<Event> events = Arrays.asList(event1, event2);

        formatter = new DaikonFormatter("test.dtrace", false);

        Iterator<Event> ei = events.iterator();

        while (ei.hasNext()) {
            Event currentEvent = ei.next();
            if (formatter.simple) {
                formatter.simplePoint(currentEvent, "comparability", out);
            } else {
                formatter.point(currentEvent.getName(), currentEvent, "comparability", out);
            }
        }

        String expected = "ppt event1:::ENTER\nevent1\n1\nppt event2:::ENTER\nevent2\n1\n";

        assertEquals(expected, out.toString());
    }

    @Test
    public void testObservationsRunWithInputAndOutput() throws IOException {
        Event event1 = mock(Event.class);
        when(event1.getName()).thenReturn("event1");
        when(event1.isExit()).thenReturn(false);

        Event event2 = mock(Event.class);
        when(event2.getName()).thenReturn("event2");
        when(event2.isExit()).thenReturn(true);

        Event event3 = mock(Event.class);
        when(event3.getName()).thenReturn("event3");
        when(event3.isExit()).thenReturn(false);

        List<Event> events = Arrays.asList(event1, event2, event3);

        formatter = new DaikonFormatter("test.dtrace", null);

        Iterator<Event> ei = events.iterator();

        String previousEventName = null;

        while (ei.hasNext()) {
            Event currentEvent = ei.next();
            if (formatter.simple) {
                formatter.simplePoint(currentEvent, "comparability", out);
            } else {
                if (previousEventName == null || currentEvent.getName().equals(previousEventName)) {
                    continue;
                }
                formatter.point(previousEventName, eventFromName(previousEventName), "comparability", out);
            }
            previousEventName = currentEvent.getName();
        }

        formatter.point(event3.getName(), event3, "comparability", out);

        String expected = "ppt event1:::ENTER\nevent1\n1\nppt event2:::EXIT1\nevent2\n11\n";

        assertEquals(expected, out.toString());
    }

    private Event eventFromName(String name) {
        Event event = mock(Event.class);
        when(event.getName()).thenReturn(name);
        when(event.isExit()).thenReturn(false);
        return event;
    }
}
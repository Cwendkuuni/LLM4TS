package fi.vtt.noen.testgen.observations.formatter;

import static fi.vtt.noen.testgen.model.Event.EventType.EXIT;
import static fi.vtt.noen.testgen.model.Event.EventType.ENTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        formatter = new DaikonFormatter("test.dtrace", true, outputStream);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());

        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.dtrace", Collections.singletonList("input1"), outputStream);
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:[input1]\n", formatterWithInputs.header());
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertNotNull(classes);
        assertEquals(2, classes.size());
        Class firstClass = classes.get(0);
        assertEquals("fi.vtt.noen.testgen.parser.InputInterface1", firstClass.getName());
        Class secondClass = classes.get(1);
        assertEquals("fi.vtt.noen.testgen.parser.InputInterface2", secondClass.getName());
    }

    @Test
    public void testPointSimple() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        Event event = new Event("event1", ENTER, null, Arrays.asList(new EventAttribute("attribute1", "value1")));
        formatter.simplePoint(event, "implicit", outputStream);
        String expectedOutput = "ppt event1\n ppt-type enter\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testPointEnter() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        Event previousEvent = new Event("previousEvent", EXIT, null, Arrays.asList(new EventAttribute("previousAttribute1", "previousValue1")));
        Event event = new Event("event1", ENTER, null, Arrays.asList(new EventAttribute("attribute1", "value1")));
        formatter.point("previousEvent,event1", previousEvent, "implicit", outputStream);
        String expectedOutput = "ppt previousEvent,event1\n ppt-type enter\n variable previousAttribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n ppt event1\n ppt-type enter\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testPointExit() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        Event previousEvent = new Event("previousEvent", ENTER, null, Arrays.asList(new EventAttribute("previousAttribute1", "previousValue1")));
        Event event = new Event("event1", EXIT, null, Arrays.asList(new EventAttribute("attribute1", "value1")));
        formatter.point("previousEvent,event1", previousEvent, "implicit", outputStream);
        String expectedOutput = "ppt previousEvent\n ppt-type enter\n variable previousAttribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n ppt previousEvent,event1\n ppt-type exit\n ppt event1\n ppt-type exit\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testPointEnterAndExit() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        Event previousEvent = new Event("previousEvent", ENTER, null, Arrays.asList(new EventAttribute("previousAttribute1", "previousValue1")));
        Event event = new Event("event1", EXIT, null, Arrays.asList(new EventAttribute("attribute1", "value1")));
        formatter.point("previousEvent,event1", previousEvent, "implicit", outputStream);
        String expectedOutput = "ppt previousEvent\n ppt-type enter\n variable previousAttribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n ppt previousEvent,event1\n ppt-type exit\n ppt event1\n ppt-type exit\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testDeclarations() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        Event event = new Event("event1", EXIT, null, Arrays.asList(new EventAttribute("attribute1", "value1")));
        formatter.declarations(event, "event1", outputStream, "implicit");
        String expectedOutput = "ppt event1\n ppt-type exit\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testAttributes() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        Event event = new Event("event1", EXIT, null, Arrays.asList(new EventAttribute("attribute1", "value1"), new EventAttribute("attribute2", "value2")));
        formatter.attributes(event, outputStream);
        String expectedOutput = "attribute1\"value1\"1\n attribute2\"value2\"1\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testObservations() throws IOException, Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false, outputStream);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("event1", ENTER, null, Arrays.asList(new EventAttribute("attribute1", "value1"))));
        run.addEvent(new Event("event2", EXIT, null, Collections.emptyList()));
        run.addEvent(new Event("event3", ENTER, null, Arrays.asList(new EventAttribute("attribute1", "value1"), new EventAttribute("attribute2", "value2"))));
        run.addEvent(new Event("event4", EXIT, null, Arrays.asList(new EventAttribute("attribute1", "value1"))));
        String expectedOutput = "ppt event1:::ENTER\n ppt-type enter\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n ppt event1:::ENTER\n ppt-type enter\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n ppt event2:::EXIT\n ppt-type exit\n ppt event3:::ENTER\n ppt-type enter\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n variable attribute2\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n ppt event4:::EXIT\n ppt-type exit\n variable attribute1\n   var-kind variable\n   dec-type java.lang.String\n   rep-type java.lang.String\n   comparability implicit\n";
        assertEquals(expectedOutput, formatter.observations(run));
    }
}
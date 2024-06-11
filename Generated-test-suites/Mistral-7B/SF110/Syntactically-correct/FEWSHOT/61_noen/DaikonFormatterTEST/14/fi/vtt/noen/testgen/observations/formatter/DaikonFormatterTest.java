package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        String fileName = "test.dtrace";
        DaikonFormatter formatter = new DaikonFormatter(fileName, false);
        assertNotNull(formatter);

        DaikonFormatter formatterWithInputs = new DaikonFormatter(fileName, Arrays.asList("input1", "input2"));
        assertNotNull(formatterWithInputs);
    }

    @Test
    public void testHeader() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String fileNameExtension = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Class<?> clazz1 = Class.forName("fi.vtt.noen.testgen.parser.InputInterface1");
        Class<?> clazz2 = Class.forName("fi.vtt.noen.testgen.parser.InputInterface2");
        Collection<Class<?>> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classes.size());
        assertTrue(classes.contains(clazz1));
        assertTrue(classes.contains(clazz2));
    }

    @Test
    public void testInputInterface() throws Exception {
        Collection<Class<?>> inputInterfaces = DaikonFormatter.inputs();
        assertFalse(inputInterfaces.isEmpty());

        InputStream inputStream = getClass().getResourceAsStream("/fi/vtt/noen/testgen/parser/TestInputInterface.class");
        byte[] inputBytes = new byte[inputStream.available()];
        inputStream.read(inputBytes);
        inputStream.close();

        ByteArrayInputStream inputClassStream = new ByteArrayInputStream(inputBytes);
        Properties inputsConfig = new Properties();
        inputsConfig.load(inputClassStream);
        Collection<Class<?>> expectedInputInterfaces = new ArrayList<>();
        String inputInterfaceName = inputsConfig.getProperty("InputInterface0");
        Class<?> inputInterfaceClass = Class.forName(inputInterfaceName);
        expectedInputInterfaces.add(inputInterfaceClass);
        inputInterfaceName = inputsConfig.getProperty("InputInterface1");
        inputInterfaceClass = Class.forName(inputInterfaceName);
        expectedInputInterfaces.add(inputInterfaceClass);

        assertEquals(expectedInputInterfaces, inputInterfaces);
    }

    @Test
    public void testObservationsWithSimplePoint() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        String observations = formatter.observations(createProgramRunWithTwoEvents("Event1", "Event2"));
        assertEquals("Event1:::ENTER\nEvent1\nEvent2:::ENTER\nEvent2\n", observations);
    }

    @Test
    public void testObservationsWithPoint() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String observations = formatter.observations(createProgramRunWithTwoEvents("Event1", "Event2"));
        String expectedObservations = "ppt Event1:::ENTER\nppt-type enter\nvariable Event1\nvar-kind variable\ndec-type Object\nrep-type Object\ncomparability implicit\nEvent1\n\nppt Event1:::EXIT1\nppt-type exit\nvariable Event1\nvar-kind variable\ndec-type Object\nrep-type Object\ncomparability implicit\n\nppt Event2:::ENTER\nppt-type enter\nvariable Event2\nvar-kind variable\ndec-type Object\nrep-type Object\ncomparability implicit\nEvent2\n";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testDeclarations() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        Event event = createEvent("Event1", "Object", new HashMap<>());
        String observations = formatter.observations(createProgramRunWithOneEvent(event));
        String expectedObservations = "ppt Event1:::ENTER\nppt-type enter\nvariable Event1\nvar-kind variable\ndec-type Object\nrep-type Object\ncomparability implicit\nEvent1\n";
        assertEquals(expectedObservations, observations);

        event = createEvent("Event1", "String", new HashMap<>());
        observations = formatter.observations(createProgramRunWithOneEvent(event));
        expectedObservations = "ppt Event1:::ENTER\nppt-type enter\nvariable Event1\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\nEvent1\"\n";
        assertEquals(expectedObservations, observations);

        event = createEvent("Event1", "Object[]", new HashMap<>());
        observations = formatter.observations(createProgramRunWithOneEvent(event));
        expectedObservations = "ppt Event1:::ENTER\nppt-type enter\nvariable Event1\nvar-kind variable\ndec-type Object[]\nrep-type Object[]\ncomparability implicit\nEvent1[]\n";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testAttributes() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        Event event = createEvent("Event1", "Object", new HashMap<>());
        event.addAttribute(new StringAttribute("attr1", "value1"));
        event.addAttribute(new IntegerAttribute("attr2", 1));
        String observations = formatter.observations(createProgramRunWithOneEvent(event));
        String expectedObservations = "ppt Event1:::ENTER\nppt-type enter\nvariable Event1\nvar-kind variable\ndec-type Object\nrep-type Object\ncomparability implicit\nEvent1\nattr1=\"value1\"1\nattr2=1\n";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testObservationsWithComplexPoint() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String observations = formatter.observations(createComplexProgramRun());
        String expectedObservations = "ppt Event1:::ENTER\nppt-type enter\nvariable Event1\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability explicit\nEvent1\"\n\nppt Event1:::EXIT1\nppt-type exit\nvariable Event1\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability explicit\nEvent1\"\n\nppt Event2:::ENTER\nppt-type enter\nvariable Event2\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\nEvent2\"\n\nppt Event2:::EXIT1\nppt-type exit\nvariable Event2\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\nEvent2\"\n\nppt Event3:::ENTER\nppt-type enter\nvariable Event3\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability explicit\nEvent3\"\n\nppt Event3:::EXIT1\nppt-type exit\nvariable Event3\nvar-kind variable\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability explicit\nEvent3\"\n";
        assertEquals(expectedObservations, observations);
    }

    private ProgramRun createProgramRunWithOneEvent(Event event) {
        List<Event> events = new ArrayList<>();
        events.add(event);
        return new ProgramRun(events);
    }

    private ProgramRun createProgramRunWithTwoEvents(String event1Name, String event2Name) {
        List<Event> events = new ArrayList<>();
        Event event1 = createEvent(event1Name, "Object", new HashMap<>());
        Event event2 = createEvent(event2Name, "Object", new HashMap<>());
        events.add(event1);
        events.add(event2);
        return new ProgramRun(events);
    }

    private ProgramRun createComplexProgramRun() {
        List<Event> events = new ArrayList<>();
        Event event1 = createEvent("Event1", "String", new HashMap<>());
        Event event2 = createEvent("Event2", "String", new HashMap<>());
        Event event3 = createEvent("Event3", "String", new HashMap<>());
        events.add(event1);
        events.add(event1);
        events.add(event2);
        events.add(event2);
        events.add(event3);
        events.add(event3);
        return new ProgramRun(events);
    }

    private Event createEvent(String name, String type, Map<String, EventAttribute> attributes) {
        return new Event(name, type, attributes);
    }
}

class Event {
    private String name;
    private String type;
    private Map<String, EventAttribute> attributes;

    public Event(String name, String type, Map<String, EventAttribute> attributes) {
        this.name = name;
        this.type = type;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Iterator<EventAttribute> iterator() {
        return attributes.iterator();
    }

    public boolean isExit() {
        return false;
    }
}

class EventAttribute {
    private String name;
    private String type;
    private Object value;

    public EventAttribute(String name, String type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}

class StringAttribute extends EventAttribute {
    public StringAttribute(String name, String value) {
        super(name, "java.lang.String", value);
    }
}

class IntegerAttribute extends EventAttribute {
    public IntegerAttribute(String name, int value) {
        super(name, "int", value);
    }
}

class ProgramRun {
    private List<Event> events;

    public ProgramRun(List<Event> events) {
        this.events = events;
    }

    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
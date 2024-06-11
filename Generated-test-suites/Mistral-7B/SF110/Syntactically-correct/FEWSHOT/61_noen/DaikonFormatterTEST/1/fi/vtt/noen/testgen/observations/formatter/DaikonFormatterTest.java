package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException, Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        final DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        assertNotNull(formatter);
    }

    @Test
    public void testDaikonFormatterSimple() throws IOException, Exception {
        final String fileName = "test.dtrace";
        final boolean simple = true;
        final DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        assertNotNull(formatter);
    }

    @Test
    public void testDaikonFormatterWithInputs() throws IOException, Exception {
        final String fileName = "test.dtrace";
        final Collection<String> inputs = Arrays.asList("Input1", "Input2");
        final DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        assertNotNull(formatter);
        assertEquals(inputs, formatter.getInputs());
    }

    @Test
    public void testHeader() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        final String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        final String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        final String fileNameExtension = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testObservationsWithSimpleEvents() throws IOException, Exception {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        formatter.observations(createSimpleProgramRun());
        final String observations = outputStream.toString();

        assertEquals("", observations);
    }

    @Test
    public void testObservationsWithComplexEvents() throws IOException, Exception {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        formatter.observations(createComplexProgramRun());

        final String observations = outputStream.toString();
        final String expectedObservations = "ppt Input1\n" +
                "ppt-type enter\n" +
                "ppt Input2\n" +
                "ppt-type enter\n" +
                "ppt Input1:::EXIT\n" +
                "ppt-type exit\n" +
                "ppt Input2:::EXIT\n" +
                "ppt Input1:::ENTER\n" +
                "ppt-type enter\n" +
                "ppt Input2:::ENTER\n" +
                "ppt Input1:::EXIT\n" +
                "ppt-type exit\n" +
                "ppt Input1:::ENTER\n" +
                "ppt-type enter\n" +
                "ppt Input2:::EXIT\n" +
                "ppt-type exit\n" +
                "ppt Input2:::ENTER\n" +
                "ppt Input1:::EXIT\n" +
                "ppt-type exit\n";

        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testDeclarationsWithSimpleEvent() throws IOException, Exception {
        final Event event = createSimpleEvent("Input1");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        formatter.declarations(event, "declaration1", outputStream, "implicit");

        final String expectedDeclaration = "ppt declaration1\n" +
                "ppt-type enter\n" +
                "variable Input1\n" +
                "var-kind variable\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n";

        assertEquals(expectedDeclaration, outputStream.toString());
    }

    @Test
    public void testDeclarationsWithComplexEvent() throws IOException, Exception {
        final Event event = createComplexEvent("Input1", "Input2");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        formatter.declarations(event, "declaration1", outputStream, "implicit");

        final String expectedDeclaration = "ppt declaration1\n" +
                "ppt-type enter\n" +
                "variable Input1\n" +
                "var-kind variable\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n" +
                "variable Input2\n" +
                "var-kind variable\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n";

        assertEquals(expectedDeclaration, outputStream.toString());
    }

    @Test
    public void testAttributes() throws IOException, Exception {
        final Event event = createComplexEvent("Input1", "Input2");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        formatter.attributes(event, outputStream);

        final String expectedAttributes = "Input1\\\"1\n" +
                "Input2\\\"2\n";

        assertEquals(expectedAttributes, outputStream.toString());
    }

    private ProgramRun createSimpleProgramRun() {
        final List<Event> events = Arrays.asList(createSimpleEvent("Input1"), createSimpleEvent("Input1"));
        final ProgramRun programRun = new ProgramRun("TestProgram", events);
        return programRun;
    }

    private ProgramRun createComplexProgramRun() throws IOException {
        final List<Event> events = Arrays.asList(createSimpleEvent("Input1"), createComplexEvent("Input1", "Input2"), createSimpleEvent("Input1"), createComplexEvent("Input1", "Input2"), createSimpleEvent("Input1"));
        final ProgramRun programRun = new ProgramRun("TestProgram", events);
        return programRun;
    }

    private Event createSimpleEvent(final String name) {
        final Set<EventAttribute> attributes = new HashSet<>();
        return new Event(name, attributes, false);
    }

    private Event createComplexEvent(final String name1, final String name2) throws IOException {
        final Set<EventAttribute> attributes = new HashSet<>();
        attributes.add(new StringAttribute("attribute1", "value1"));
        attributes.add(new StringAttribute("attribute2", "value2"));

        final Event event = new Event(name1 + "," + name2, attributes, true);
        return event;
    }
}
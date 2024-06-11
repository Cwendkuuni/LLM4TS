package fi.vtt.noen.testgen.observations.formatter;

import static fi.vtt.noen.testgen.model.Event.EventType.EXIT;
import static fi.vtt.noen.testgen.model.Event.EventType.ENTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.Constants;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private Event event;
    private List<EventAttribute> attributes;

    @Before
    public void setUp() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("testgen.properties"));
        InterfaceParser parser = new InterfaceParser();
        List<String> inputNames = Arrays.asList("Input1", "Input2");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        List<String> outputNames = Collections.emptyList();

        formatter = new DaikonFormatter("test.dtrace", true);

        event = new Event("Input1", ENTER, null);
        attributes = Arrays.asList(new EventAttribute("name", "value1", "java.lang.String"));
        event.setAttributes(attributes);
    }

    @Test
    public void testHeader() {
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() {
        String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        String fileNameExtension = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testObservationsSimple() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String observations = formatter.observations(new ProgramRun(inputStream, Collections.emptyList(), Arrays.asList(event), null));
        assertEquals("", observations);
    }

    @Test
    public void testObservationsComplex() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("testgen.properties"));
        InterfaceParser parser = new InterfaceParser();
        List<String> inputNames = Arrays.asList("Input1", "Input2");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        List<String> outputNames = Arrays.asList("Output1", "Output2");

        formatter = new DaikonFormatter("test.dtrace", inputNames);

        Event input = new Event("Input1", ENTER, null);
        Event output = new Event("Output1", EXIT, null);

        attributes = Arrays.asList(new EventAttribute("name1", "value1", "java.lang.String"), new EventAttribute("name2", "value2", "java.lang.Integer"));
        input.setAttributes(attributes);

        attributes = Arrays.asList(new EventAttribute("name1", "value1_exit", "java.lang.String"), new EventAttribute("name3", "value3", "java.lang.Double"));
        output.setAttributes(attributes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String observations = formatter.observations(new ProgramRun(inputStream, inputNames, Arrays.asList(input, output), null));

        String expected = "decl-version 2.0\n"
                + "var-comparability implicit\n"
                + "ppt Input1\n"
                + "ppt-type enter\n"
                + "ppt Input1:::ENTER\n"
                + "  variable name1\n"
                + "    var-kind variable\n"
                + "    dec-type java.lang.String\n"
                + "    rep-type java.lang.String\n"
                + "    comparability implicit\n"
                + "  variable name2\n"
                + "    var-kind variable\n"
                + "    dec-type java.lang.Integer\n"
                + "    rep-type java.lang.Integer\n"
                + "    comparability implicit\n"
                + "ppt Output1\n"
                + "ppt-type exit\n"
                + "ppt Output1:::EXIT1\n"
                + "  variable name1\n"
                + "    var-kind variable\n"
                + "    dec-type java.lang.String\n"
                + "    rep-type java.lang.String\n"
                + "    comparability implicit\n"
                + "  variable name3\n"
                + "    var-kind variable\n"
                + "    dec-type java.lang.Double\n"
                + "    rep-type java.lang.Double\n"
                + "    comparability implicit\n"
                + "";

        assertEquals(expected, observations);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Set<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertNotNull(classes);
        assertEquals(2, classes.size());

        Set<Class> classes2 = DaikonFormatter.classesForMultipleProperties("OutputInterface");
        assertNotNull(classes2);
        assertEquals(0, classes2.size());
    }

    @Test
    public void testClassForProperty() throws Exception {
        Class clazz = DaikonFormatter.classForProperty("InputInterface0");
        assertNull(clazz);

        clazz = DaikonFormatter.classForProperty("OutputInterface0");
        assertNull(clazz);

        clazz = DaikonFormatter.classForProperty("InputInterface1");
        assertNotNull(clazz);

        clazz = DaikonFormatter.classForProperty("OutputInterface1");
        assertNotNull(clazz);
    }
}
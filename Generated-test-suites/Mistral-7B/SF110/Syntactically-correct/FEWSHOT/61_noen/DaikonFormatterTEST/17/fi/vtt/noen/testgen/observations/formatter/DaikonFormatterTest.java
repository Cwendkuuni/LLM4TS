package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        final String fileName = "test.dtrace";
        final Collection<String> inputs = Arrays.asList("Method1", "Method2");

        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        assertNotNull(formatter);
        assertEquals(fileName, formatter.getFileName());
        assertEquals(inputs, formatter.getInputs());
    }

    @Test
    public void testHeader() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testSimplePoint() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);

        Event event = new Event("Method1", new ArrayList<EventAttribute>());
        String result = formatter.observations(event);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String expected = "ppt Method1:::ENTER\n";
        assertEquals(expected, result);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        String header = formatter.header();
        String observations = formatter.parse(in);
        assertEquals("Method1", observations);
    }

    @Test
    public void testPoint() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);

        Event previousEvent = new Event("Method1", new ArrayList<EventAttribute>());
        Event event = new Event("Method2", new ArrayList<EventAttribute>());

        String result = formatter.observations(event);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String expected = "ppt Method1:::ENTER\nppt Method2:::ENTER\n";
        assertEquals(expected, result);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        String header = formatter.header();
        String observations = formatter.parse(in);
        assertEquals("Method2", observations);

        assertEquals(2, formatter.getDeclarations().size());
        assertEquals("Method1", formatter.getDeclarations().iterator().next());
        assertEquals("Method2", formatter.getDeclarations().iterator().next());
    }

    @Test
    public void testDeclarations() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);

        Event event = new Event("Method1", Arrays.asList(new EventAttribute("attr1", "int"), new EventAttribute("attr2", "java.lang.String")));

        String result = formatter.observations(event);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String expected = "ppt Method1:::ENTER\n" +
                          "ppt-type enter\n" +
                          "ppt Method1:::ENTER\n" +
                          "variable attr1\n" +
                          "  var-kind variable\n" +
                          "  dec-type int\n" +
                          "  rep-type int\n" +
                          "  comparability implicit\n" +
                          "variable attr2\n" +
                          "  var-kind variable\n" +
                          "  dec-type java.lang.String\n" +
                          "  rep-type java.lang.String\n" +
                          "  comparability implicit\n";
        assertEquals(expected, result);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        String header = formatter.header();
        String observations = formatter.parse(in);
        assertEquals("Method1", observations);

        assertEquals(2, formatter.getDeclarations().size());
        assertEquals("Method1", formatter.getDeclarations().iterator().next());
        assertEquals("attr1", formatter.getDeclarations().iterator().next());
    }

    @Test
    public void testAttributes() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);

        Event event = new Event("Method1", Arrays.asList(new EventAttribute("attr1", "int"), new EventAttribute("attr2", "java.lang.String")));

        String result = formatter.observations(event);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String expected = "ppt Method1:::ENTER\n" +
                          "ppt-type enter\n" +
                          "ppt Method1:::ENTER\n" +
                          "variable attr1\n" +
                          "  var-kind variable\n" +
                          "  dec-type int\n" +
                          "  rep-type int\n" +
                          "  comparability implicit\n" +
                          "variable attr2\n" +
                          "  var-kind variable\n" +
                          "  dec-type java.lang.String\n" +
                          "  rep-type java.lang.String\n" +
                          "  comparability implicit\n" +
                          "attr1 \"1\"1\n" +
                          "attr2 \"test\"1\n";
        assertEquals(expected, result);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        String header = formatter.header();
        String observations = formatter.parse(in);
        assertEquals("Method1", observations);

        assertEquals(2, formatter.getDeclarations().size());
        assertEquals("Method1", formatter.getDeclarations().iterator().next());
        assertEquals("attr1", formatter.getDeclarations().iterator().next());

        List<EventAttribute> attributes = event.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("attr1", attributes.get(0).getName());
        assertEquals("int", attributes.get(0).getType());
        assertEquals("1", attributes.get(0).getValue());
        assertEquals("attr2", attributes.get(1).getName());
        assertEquals("java.lang.String", attributes.get(1).getType());
        assertEquals("test", attributes.get(1).getValue());
    }

    @Test
    public void testDeclarationsAndAttributes() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);

        Event event1 = new Event("Method1", Arrays.asList(new EventAttribute("attr1", "int"), new EventAttribute("attr2", "java.lang.String")));
        Event event2 = new Event("Method2", Arrays.asList(new EventAttribute("attr3", "int"), new EventAttribute("attr4", "java.lang.String")));

        String result = formatter.observations(event2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String expected = "ppt Method1:::ENTER\n" +
                          "ppt-type enter\n" +
                          "ppt Method1:::ENTER\n" +
                          "ppt Method2:::ENTER\n" +
                          "ppt-type enter\n" +
                          "ppt Method2:::ENTER\n" +
                          "variable attr1\n" +
                          "  var-kind variable\n" +
                          "  dec-type int\n" +
                          "  rep-type int\n" +
                          "  comparability implicit\n" +
                          "variable attr2\n" +
                          "  var-kind variable\n" +
                          "  dec-type java.lang.String\n" +
                          "  rep-type java.lang.String\n" +
                          "  comparability implicit\n" +
                          "variable attr3\n" +
                          "  var-kind variable\n" +
                          "  dec-type int\n" +
                          "  rep-type int\n" +
                          "  comparability implicit\n" +
                          "variable attr4\n" +
                          "  var-kind variable\n" +
                          "  dec-type java.lang.String\n" +
                          "  rep-type java.lang.String\n" +
                          "  comparability implicit\n" +
                          "attr1 \"1\"1\n" +
                          "attr2 \"test\"1\n" +
                          "attr3 \"2\"1\n" +
                          "attr4 \"hello\"1\n";
        assertEquals(expected, result);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        String header = formatter.header();
        String observations = formatter.parse(in);
        assertEquals("Method2", observations);

        assertEquals(4, formatter.getDeclarations().size());
        assertEquals("Method1", formatter.getDeclarations().iterator().next());
        assertEquals("Method2", formatter.getDeclarations().iterator().next());
        assertEquals("attr1", formatter.getDeclarations().iterator().next());
        assertEquals("attr2", formatter.getDeclarations().iterator().next());

        List<EventAttribute> attributes1 = event1.getAttributes();
        assertEquals(2, attributes1.size());
        assertEquals("attr1", attributes1.get(0).getName());
        assertEquals("int", attributes1.get(0).getType());
        assertEquals("1", attributes1.get(0).getValue());
        assertEquals("attr2", attributes1.get(1).getName());
        assertEquals("java.lang.String", attributes1.get(1).getType());
        assertEquals("test", attributes1.get(1).getValue());

        List<EventAttribute> attributes2 = event2.getAttributes();
        assertEquals(2, attributes2.size());
        assertEquals("attr3", attributes2.get(0).getName());
        assertEquals("int", attributes2.get(0).getType());
        assertEquals("2", attributes2.get(0).getValue());
        assertEquals("attr4", attributes2.get(1).getName());
        assertEquals("java.lang.String", attributes2.get(1).getType());
        assertEquals("hello", attributes2.get(1).getValue());
    }
}
package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        assertNotNull(formatter);
        assertEquals(fileName, formatter.getFileName());
        assertEquals(simple, formatter.isSimple());
        assertEquals(Collections.emptySet(), formatter.getDeclarations());
        assertNull(formatter.getInputs());
    }

    @Test
    public void testDaikonFormatterInputs() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
            final Collection<String> inputs = InterfaceParser.methodNames(new FileInputStream("inputs.txt"));
            formatter = new DaikonFormatter(fileName, inputs);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        assertNotNull(formatter);
        assertEquals(fileName, formatter.getFileName());
        assertEquals(simple, formatter.isSimple());
        assertNotNull(formatter.getInputs());
        assertEquals(Arrays.asList("input1", "input2"), formatter.getInputs());
    }

    @Test
    public void testDaikonFormatterDeclarations() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
            final List<Event> events = Arrays.asList(
                    new Event("input1"),
                    new Event("output1"),
                    new Event("output2"),
                    new Event("output3"));
            formatter = new DaikonFormatter(fileName, Collections.emptyList(), events);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        assertNotNull(formatter);
        assertEquals(fileName, formatter.getFileName());
        assertEquals(simple, formatter.isSimple());
        assertNotNull(formatter.getDeclarations());
        assertEquals(new HashSet<>(Arrays.asList("input1", "output1", "output2", "output3")), formatter.getDeclarations());
    }

    @Test
    public void testHeader() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final String header = formatter.header();
        assertNotNull(header);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final String footer = formatter.footer();
        assertNull(footer);
    }

    @Test
    public void testFileNameExtension() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = true;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event1 = new Event("input1");
        final Event event2 = new Event("input2");
        final Event event3 = new Event("output");
        final Iterator<Event> iterator = Arrays.asList(event1, event2, event3).iterator();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.observations(new ProgramRun(iterator), outputStream);
        final String observations = outputStream.toString();
        assertNotNull(observations);
        assertEquals("ppt input1\nppt input1:::ENTER\n", observations);
    }

    @Test
    public void testObservationsComplex() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event1 = new Event("input1");
        final Event event2 = new Event("input2");
        final Event event3 = new Event("output1");
        final Event event4 = new Event("output2");
        final Event event5 = new Event("output3");
        final Iterator<Event> iterator = Arrays.asList(event1, event2, event3, event4, event5).iterator();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.observations(new ProgramRun(iterator), outputStream);
        final String observations = outputStream.toString();
        assertNotNull(observations);
        final String expected = ""
                + "decl-version 2.0\n"
                + "var-comparability implicit\n"
                + "ppt input1\n"
                + "ppt-type enter\n"
                + "ppt input1:::ENTER\n"
                + "variable input1\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "ppt output1\n"
                + "ppt-type enter\n"
                + "ppt output1:::ENTER\n"
                + "variable output1\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "ppt input2\n"
                + "ppt-type enter\n"
                + "ppt input2:::ENTER\n"
                + "variable input2\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "ppt output2\n"
                + "ppt-type enter\n"
                + "ppt output2:::ENTER\n"
                + "variable output2\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "ppt output3\n"
                + "ppt-type enter\n"
                + "ppt output3:::ENTER\n"
                + "variable output3\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "";
        assertEquals(expected, observations);
    }

    @Test
    public void testPointSimple() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = true;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event = new Event("input");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.point("input", event, "11", outputStream);
        final String result = outputStream.toString();
        assertNotNull(result);
        assertEquals("ppt input\n", result);
    }

    @Test
    public void testPointComplex() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event1 = new Event("input1");
        final Event event2 = new Event("output1");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.point("input1,output1", event1, "22", outputStream);
        final String result = outputStream.toString();
        assertNotNull(result);
        final String expected = ""
                + "ppt input1,output1\n"
                + "ppt-type enter\n"
                + "ppt input1,output1:::ENTER\n"
                + "";
        assertEquals(expected, result);
    }

    @Test
    public void testAttributesSimple() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = true;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event = new Event("input");
        final EventAttribute attribute = new EventAttribute("name", "value");
        event.addAttribute(attribute);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.attributes(event, outputStream);
        final String result = outputStream.toString();
        assertNotNull(result);
        assertEquals("name=\"value\"1\n", result);
    }

    @Test
    public void testAttributesComplex() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event = new Event("input");
        final EventAttribute attribute1 = new EventAttribute("name1", "value1");
        final EventAttribute attribute2 = new EventAttribute("name2", "value2");
        event.addAttribute(attribute1);
        event.addAttribute(attribute2);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.attributes(event, outputStream);
        final String result = outputStream.toString();
        assertNotNull(result);
        final String expected = ""
                + "name1=\"value1\"1\n"
                + "name2=\"value2\"1\n"
                + "";
        assertEquals(expected, result);
    }

    @Test
    public void testDeclarationsSimple() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = true;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event = new Event("input");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.declarations(event, "input", outputStream, "11");
        final String result = outputStream.toString();
        assertNotNull(result);
        assertEquals("ppt input\n", result);
    }

    @Test
    public void testDeclarationsComplex() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;
        DaikonFormatter formatter = null;
        try {
            formatter = new DaikonFormatter(fileName, simple);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        final Event event = new Event("input");
        final EventAttribute attribute1 = new EventAttribute("name1", "value1");
        final EventAttribute attribute2 = new EventAttribute("name2", "value2");
        event.addAttribute(attribute1);
        event.addAttribute(attribute2);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.declarations(event, "input", outputStream, "22");
        final String result = outputStream.toString();
        assertNotNull(result);
        final String expected = ""
                + "ppt input\n"
                + "ppt-type enter\n"
                + "ppt input:::ENTER\n"
                + "variable input\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "variable name1\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "variable name2\n"
                + "  var-kind variable\n"
                + "  dec-type java.lang.String\n"
                + "  rep-type java.lang.String\n"
                + "  comparability implicit\n"
                + "";
        assertEquals(expected, result);
    }

}
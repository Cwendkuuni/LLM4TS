package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Collections.emptySet());
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertNull(formatter.footer());
        assertEquals("dtrace", formatter.fileNameExtension());

        Properties props = new Properties();
        props.load(new FileInputStream("test.properties"));
        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.dtrace", InterfaceParser.methodNames(props.getString("InputInterface").split(",")));
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:\n[InputInterface1, InputInterface2]\n", formatterWithInputs.header());
    }

    @Test
    public void testDeclarations() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Collections.emptySet());

        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("attr1", "StringValue1"));
        event.addAttribute(new EventAttribute("attr2", "IntegerValue1"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.point(event, "11", outputStream);

        String actualOutput = outputStream.toString();
        String expectedOutput = "ppt Event1\n  variable attr1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable attr2\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability implicit\n";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDeclarationsWithArrays() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Collections.emptySet());

        Event event = new Event("Event1");
        event.addAttribute(new EventAttribute("attr1", new EventAttribute[] { new EventAttribute("ArrayAttribute1", new String[] { "Value1", "Value2" }) }));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.point(event, "11", outputStream);

        String actualOutput = outputStream.toString();
        String expectedOutput = "ppt Event1\n  variable attr1[]\n    var-kind array\n    dec-type EventAttribute[]\n    rep-type EventAttribute[]\n    comparability implicit\n    variable ArrayAttribute1[]\n      var-kind array\n      dec-type String\n      rep-type String\n      comparability implicit\n      variable ArrayAttribute1[]\n        var-kind array\n        dec-type String\n        rep-type String\n        comparability implicit\n";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSimplePoint() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);

        Event event = new Event("Event1");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        formatter.simplePoint(event, "10", outputStream);

        String actualOutput = outputStream.toString();
        String expectedOutput = "Event1:::ENTER\n";

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testObservations() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", InterfaceParser.methodNames(new String[] { "Method1", "Method2" }));

        List<Event> events = Arrays.asList(new Event("Method1"), new Event("Method2"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter.observations(new ProgramRun(events));

        String actualOutput = outputStream.toString();
        String expectedOutput = "ppt Method1\n  variable Method1\n    var-kind variable\n    dec-type Method1\n    rep-type Method1\n    comparability implicit\n  variable Method1:::EXIT1\n    var-kind variable\n    dec-type Method1\n    rep-type Method1\n    comparability implicit\nppt Method2\n  variable Method2\n    var-kind variable\n    dec-type Method2\n    rep-type Method2\n    comparability implicit\n  variable Method2:::ENTER\n    var-kind variable\n    dec-type Method2\n    rep-type Method2\n    comparability implicit\n";

        assertEquals(expectedOutput, actualOutput);
    }
}
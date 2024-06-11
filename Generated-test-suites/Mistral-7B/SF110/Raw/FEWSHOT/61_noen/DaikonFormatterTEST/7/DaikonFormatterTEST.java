package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException, Exception {
        // Test empty constructor
        DaikonFormatter formatter = new DaikonFormatter(null, Collections.emptySet());
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertNull(formatter.inputs);

        // Test constructor with simple = true
        formatter = new DaikonFormatter("test.dtrace", true);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertNull(formatter.inputs);

        // Test constructor with simple = false and valid inputs
        List<String> methodNames = Arrays.asList("method1", "method2", "method3");
        formatter = new DaikonFormatter("test.dtrace", methodNames);
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:[method1, method2, method3]\n", formatter.header());
        assertEquals(methodNames, formatter.inputs);

        // Test constructor with exception
        try {
            formatter = new DaikonFormatter("test.dtrace", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test classesForMultipleProperties method
        Properties configuration = new Properties();
        configuration.setProperty("InputInterface0", "fi.vtt.noen.testgen.parser.InputInterface0");
        configuration.setProperty("InputInterface1", "fi.vtt.noen.testgen.parser.InputInterface1");
        InputStream inputStream = new ByteArrayInputStream(configuration.toBytes());
        formatter = new DaikonFormatter("test.dtrace", null);
        Collection<Class> classes = formatter.inputs();
        assertEquals(2, classes.size());
        assertEquals(Class.forName("fi.vtt.noen.testgen.parser.InputInterface0"), classes.iterator().next());
        assertEquals(Class.forName("fi.vtt.noen.testgen.parser.InputInterface1"), classes.iterator().next());

        // Test fileNameExtension method
        assertEquals("dtrace", formatter.fileNameExtension());

        // Test header method
        String input = "decl-version 2.0\nvar-comparability implicit\n";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        formatter = new DaikonFormatter("test.dtrace", null);
        String result = formatter.header();
        assertEquals(input, result);

        // Test footer method
        input = "";
        outputStream.reset();
        result = formatter.footer();
        assertEquals(input, result);

        // Test observations method
        Properties configuration2 = new Properties();
        configuration2.setProperty("InputInterface", "fi.vtt.noen.testgen.parser.InputInterface");
        configuration2.setProperty("OutputInterface", "fi.vtt.noen.testgen.parser.OutputInterface");
        inputStream = new ByteArrayInputStream(configuration2.toBytes());
        formatter = new DaikonFormatter("test.dtrace", null);
        formatter.setInputStream(inputStream);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        formatter.setOutputStream(outputStream2);
        Event event1 = new Event("method1", Arrays.asList(new EventAttribute("id", "1"), new EventAttribute("name", "value1")));
        Event event2 = new Event("method2", Arrays.asList(new EventAttribute("id", "2"), new EventAttribute("name", "value2")));
        formatter.addEvent(event1);
        formatter.addEvent(event2);
        formatter.format();
        String expectedOutput = "decl-version 2.0\nvar-comparability implicit\ninputs:[method1, method2]\n\nppt method1\n  variable id\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability implicit\n  variable name\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable value1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\nppt method2\n  variable id\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability implicit\n  variable name\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable value2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n";
        assertEquals(expectedOutput, outputStream2.toString());
    }
}
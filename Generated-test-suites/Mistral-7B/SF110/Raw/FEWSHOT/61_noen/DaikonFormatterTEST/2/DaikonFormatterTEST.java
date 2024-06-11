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
import java.util.Properties;
import java.util.Set;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.Event;
import fi.vtt.noen.testgen.parser.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        final String fileName = "test.dtrace";
        final boolean simple = false;

        // Test empty constructor
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        assertEquals("", formatter.header());
        assertEquals("", formatter.footer());
        assertEquals("decl-version 2.0", formatter.header().substring(0, 21));

        // Test constructor with inputs
        Set<String> inputNames = new HashSet<>(Arrays.asList("method1", "method2"));
        DaikonFormatter formatterWithInputs = new DaikonFormatter(fileName, inputNames);
        assertEquals("inputs:" + inputNames, formatterWithInputs.header());

        // Test constructor with exception
        try {
            new DaikonFormatter(fileName, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test getInputs method
        assertEquals(inputs(), formatterWithInputs.inputs());
        assertEquals(outputs(), formatterWithInputs.outputs());

        // Test observations method with simple mode
        final List<Event> events = Arrays.asList(
                new Event("method1", "ENTER", new Properties()),
                new Event("method2", "EXIT", new Properties()));
        when(parser.parse(new File("input.txt"))).thenReturn(events);

        ByteArrayOutputStream outSimple = new ByteArrayOutputStream();
        formatterWithInputs.observations(mock(ProgramRun.class), outSimple);

        String expectedSimple = "decl-version 2.0\n" +
                "var-comparability implicit\n" +
                "ppt method1:::ENTER ppt-type enter\n" +
                "ppt method2:::EXIT ppt-type exit\n";
        assertEquals(expectedSimple, outSimple.toString());

        // Test observations method with complex mode
        final Event event1 = new Event("method1", "ENTER", new Properties());
        event1.addAttribute(new EventAttribute("attribute1", "value1", "java.lang.String"));
        final Event event2 = new Event("method2", "EXIT", new Properties());
        event2.addAttribute(new EventAttribute("attribute2", "value2", "java.lang.Integer"));
        event2.addAttribute(new EventAttribute("attribute3", "value3", "java.lang.Double"));

        when(parser.parse(new File("input.txt"))).thenReturn(Arrays.asList(event1, event2));

        ByteArrayOutputStream outComplex = new ByteArrayOutputStream();
        formatterWithInputs.observations(mock(ProgramRun.class), outComplex);

        String expectedComplex = "decl-version 2.0\n" +
                "var-comparability implicit\n" +
                "ppt method1:::ENTER ppt-type enter\n" +
                "  variable attribute1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability implicit\n" +
                "ppt method2:::EXIT ppt-type exit\n" +
                "  variable attribute2\n" +
                "    var-kind variable\n" +
                "    dec-type int\n" +
                "    rep-type int\n" +
                "    comparability implicit\n" +
                "  variable attribute3\n" +
                "    var-kind variable\n" +
                "    dec-type double\n" +
                "    rep-type double\n" +
                "    comparability implicit\n";
        assertEquals(expectedComplex, outComplex.toString());
    }

    private static Collection<Class> inputs() throws Exception {
        return Collections.emptyList();
    }

    private static Collection<Class> outputs() throws Exception {
        return Collections.emptyList();
    }

    private static Parser parser = mock(Parser.class);
}
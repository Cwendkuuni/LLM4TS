package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        // Test constructor with simple flag and no inputs
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertEquals("", formatter.footer());
        assertEquals("dtrace", formatter.fileNameExtension());

        // Test constructor with inputs
        Collection<String> inputs = Arrays.asList("method1", "method2");
        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.daikon", inputs);
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:[method1, method2]\n", formatterWithInputs.header());

        // Test classesForMultipleProperties method
        Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classes.size());
        Class<?> clazz1 = classes.iterator().next();
        assertEquals("fi.vtt.noen.testgen.testclasses.InputInterface1", clazz1.getName());
        Class<?> clazz2 = classes.iterator().next();
        assertEquals("fi.vtt.noen.testgen.testclasses.InputInterface2", clazz2.getName());

        // Test observations method with simple flag
        ProgramRun mockRun = mock(ProgramRun.class);
        when(mockRun.iterator()).thenReturn(mockIterator());
        Event event1 = new Event("method1", "Enter");
        Event event2 = new Event("method2", "Exit");
        when(mockRun.iterator().next()).thenReturn(event1);
        when(mockRun.iterator().hasNext()).thenReturn(true);
        when(mockRun.iterator().next()).thenReturn(event2);

        OutputBuffer output = new OutputBuffer();
        String result = formatterWithInputs.observations(mockRun);
        assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::ENTER\n\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method2:::EXIT1\n\n", result);

        // Test observations method without simple flag and simple points
        DaikonFormatter formatterWithoutSimple = new DaikonFormatter("test.daikon", inputs);
        result = formatterWithoutSimple.observations(mockRun);
        assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::ENTER\n\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method2:::EXIT1\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::EXIT1\n", result);

        // Test observations method without simple flag and points between inputs
        Event previousEvent = new Event("method1", "Enter");
        Event event = new Event("method2", "Enter");
        when(mockRun.iterator().next()).thenReturn(previousEvent);
        when(mockRun.iterator().hasNext()).thenReturn(true);
        when(mockRun.iterator().next()).thenReturn(event);

        result = formatterWithoutSimple.observations(mockRun);
        assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::ENTER\n\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n    variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n    variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n", result);

        // Test observations method without simple flag and points between exit events
        previousEvent = new Event("method1", "Exit");
        event = new Event("method2", "Exit");
        when(mockRun.iterator().next()).thenReturn(previousEvent);
        when(mockRun.iterator().hasNext()).thenReturn(true);
        when(mockRun.iterator().next()).thenReturn(event);

        result = formatterWithoutSimple.observations(mockRun);
        assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::EXIT1\n\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n    variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n    variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n", result);

        // Test observations method without simple flag and points between input and exit events
        previousEvent = new Event("method1", "Enter");
        event = new Event("method2", "Exit");
        when(mockRun.iterator().next()).thenReturn(previousEvent);
        when(mockRun.iterator().hasNext()).thenReturn(false);

        result = formatterWithoutSimple.observations(mockRun);
        assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::ENTER\n\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1,method2\n    variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", result);
    }

    private Iterator<Event> mockIterator() {
        return Arrays.asList(new Event("method1", "Enter"), new Event("method2", "Exit")).iterator();
    }
}
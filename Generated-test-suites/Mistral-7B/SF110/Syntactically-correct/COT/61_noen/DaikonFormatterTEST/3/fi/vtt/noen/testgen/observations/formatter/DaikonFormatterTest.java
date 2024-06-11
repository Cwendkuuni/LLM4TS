package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.Comparability;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterConstructorWithFileNameAndSimple() throws IOException, Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        assertNotNull(formatter);
        assertTrue(formatter.isSimple());
        assertNull(formatter.getInputs());
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterConstructorWithFileNameAndInputsThrowsExceptionWhenFailingToLoadInputs()
            throws IOException, Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
    }

    @Test
    public void testDaikonFormatterConstructorWithFileNameAndInputs() throws IOException, Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        assertNotNull(formatter);
        assertFalse(formatter.isSimple());
        assertEquals(Arrays.asList("method1", "method2"), formatter.getInputs());
    }

    @Test
    public void testClassForPropertyReturnsExpectedClass() throws Exception {
        Class expectedClass = Class.forName("fi.vtt.noen.testgen.parser.TestInputInterface");
        assertEquals(expectedClass, DaikonFormatter.classForProperty("InputInterface1"));
    }

    @Test
    public void testClassesForMultiplePropertiesReturnsExpectedClasses() throws Exception {
        List<Class> expectedClasses = Arrays.asList(Class.forName("fi.vtt.noen.testgen.parser.TestInputInterface1"),
                Class.forName("fi.vtt.noen.testgen.parser.TestInputInterface2"));
        assertEquals(expectedClasses, DaikonFormatter.classesForMultipleProperties("InputInterface"));
    }

    @Test
    public void testHeaderMethodReturnsExpectedHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        String header = formatter.header();
        assertTrue(header.startsWith("decl-version 2.0"));
        assertTrue(header.contains("var-comparability implicit"));
    }

    @Test
    public void testFooterMethodReturnsEmptyString() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        String footer = formatter.footer();
        assertTrue(footer.isEmpty());
    }

    @Test
    public void testFileNameExtensionMethodReturnsExpectedString() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        String fileNameExtension = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testObservationsMethodWithSimplePointing() throws IOException, Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        ProgramRun run = new ProgramRun();
        Event event = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value", new EventAttribute("java.lang.String", "testValue1"));
        }});
        run.add(event);
        String observations = formatter.observations(run);
        String expectedObservations = "ppt method1:::ENTER\n";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testObservationsMethodWithPointing() throws IOException, Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        ProgramRun run = new ProgramRun();
        Event previousEvent = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value1", new EventAttribute("java.lang.String", "testValue1"));
        }});
        Event event = new Event("method2", new HashMap<String, EventAttribute>() {{
            put("value2", new EventAttribute("java.lang.String", "testValue2"));
        }});
        run.add(previousEvent);
        run.add(event);
        String observations = formatter.observations(run);
        String expectedObservations = "ppt method1:::ENTER\nppt method2:::ENTER\nppt method1:::EXIT1\nppt method2:::EXIT1\n";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testPointMethodWithSimplePointing() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        Event event = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value1", new EventAttribute("java.lang.String", "testValue1"));
        }});
        String name = "method1";
        String comparability = Comparability.IMPLICIT.toString();
        OutputBuffer out = new OutputBuffer();
        formatter.point(event, name, comparability, out);
        String expectedOutput = "ppt " + name + "\nppt-type enter\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testPointMethodWithComplexPointing() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        Event previousEvent = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value11", new EventAttribute("java.lang.String", "testValue11"));
        }});
        Event event = new Event("method2", new HashMap<String, EventAttribute>() {{
            put("value21", new EventAttribute("java.lang.String", "testValue21"));
            put("value22", new EventAttribute("java.lang.String", "testValue22"));
        }});
        String name = "method1,method2";
        String comparability = Comparability.IMPLICIT.toString();
        OutputBuffer out = new OutputBuffer();
        formatter.point(previousEvent, name, comparability, out);
        String expectedOutput = "ppt " + name + "\nppt-type enter\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDeclarationsMethodWithSimplePointing() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        Event event = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value1", new EventAttribute("java.lang.String", "testValue1"));
        }});
        String name = "method1";
        String comparability = Comparability.IMPLICIT.toString();
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(event, name, out, comparability);
        String expectedOutput = "ppt method1\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDeclarationsMethodWithComplexPointing() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method2"));
        Event previousEvent = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value11", new EventAttribute("java.lang.String", "testValue11"));
        }});
        Event event = new Event("method2", new HashMap<String, EventAttribute>() {{
            put("value21", new EventAttribute("java.lang.String", "testValue21"));
            put("value22", new EventAttribute("java.lang.String", "testValue22"));
        }});
        String name = "method1,method2";
        Comparability comparability = Comparability.IMPLICIT;
        OutputBuffer out = new OutputBuffer();
        formatter.declarations(previousEvent, name, out, comparability.toString());
        String expectedOutput = "ppt " + name + "\nppt-type enter\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testAttributesMethodWithSimpleAttribute() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        Event event = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("value", new EventAttribute("java.lang.String", "testValue"));
        }});
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        String expectedOutput = "method1\"testValue\"1\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testAttributesMethodWithArrayAttribute() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        Event event = new Event("method1", new HashMap<String, EventAttribute>() {{
            put("array", new EventAttribute("java.lang.String[]", new Object[]{"testValue1", "testValue2"}));
        }});
        OutputBuffer out = new OutputBuffer();
        formatter.attributes(event, out);
        String expectedOutput = "method1[testValue1, testValue2]1\n";
        assertEquals(expectedOutput, out.toString());
    }
}
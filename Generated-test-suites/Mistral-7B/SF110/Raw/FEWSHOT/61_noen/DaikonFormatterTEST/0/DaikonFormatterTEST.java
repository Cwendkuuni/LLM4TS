package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("InputInterface", "fi.vtt.noen.testgen.parser.TestInterfaceInput.class");
        properties.setProperty("OutputInterface", "fi.vtt.noen.testgen.parser.TestInterfaceOutput.class");

        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();

        formatter = new DaikonFormatter("test.dtrace", Collections.emptyList(), inputStream, outputStream, new HashSet<>());
    }

    @Test
    public void testHeader() throws IOException {
        final String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() throws IOException {
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() throws IOException {
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testEmptyObservations() throws IOException {
        formatter.observations(new ProgramRun(Collections.emptyList()));
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testSimpleObservations() throws Exception {
        final Set<String> declarations = new HashSet<>();
        final List<Event> events = Arrays.asList(new Event("method1", "ENTER"), new Event("method2", "EXIT1"), new Event("method3", "ENTER"));
        formatter = new DaikonFormatter("test.dtrace", true, inputStream, outputStream, declarations);
        formatter.observations(new ProgramRun(events));

        assertEquals("ppt method1:::ENTER\nppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt method2:::EXIT1\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt method3:::ENTER\nppt method3\n  variable method3\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", outputStream.toString());

        declarations.forEach(name -> assertEquals(1, formatter.getDeclarations().size()));
    }

    @Test
    public void testComplexObservationsWithSingleTransition() throws Exception {
        final Set<String> declarations = new HashSet<>();
        final List<Event> events = Arrays.asList(new Event("input1", "ENTER"), new Event("input2", "ENTER"), new Event("output1", "EXIT1"));
        formatter = new DaikonFormatter("test.dtrace", false, inputStream, outputStream, declarations);
        formatter.observations(new ProgramRun(events));

        assertEquals("ppt input1:::ENTER\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input2:::ENTER\nppt input2\n  variable input2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt output1:::EXIT1\nppt output1\n  variable output1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input1,output1\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", outputStream.toString());

        declarations.forEach(name -> assertEquals(1, formatter.getDeclarations().size()));
    }

    @Test
    public void testComplexObservationsWithMultipleTransitions() throws Exception {
        final Set<String> declarations = new HashSet<>();
        final List<Event> events = Arrays.asList(new Event("input1", "ENTER"), new Event("input2", "ENTER"), new Event("output1", "EXIT1"), new Event("output2", "EXIT1"));
        formatter = new DaikonFormatter("test.dtrace", false, inputStream, outputStream, declarations);
        formatter.observations(new ProgramRun(events));

        assertEquals("ppt input1:::ENTER\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input2:::ENTER\nppt input2\n  variable input2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt output1:::EXIT1\nppt output1\n  variable output1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt output2:::EXIT1\nppt output2\n  variable output2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input1,output1\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input1,output2\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", outputStream.toString());

        declarations.forEach(name -> assertEquals(1, formatter.getDeclarations().size()));
    }

    @Test
    public void testComplexObservationsWithMultipleTransitionsAndDuplicateDeclaration() throws Exception {
        final Set<String> declarations = new HashSet<>();
        final List<Event> events = Arrays.asList(new Event("input1", "ENTER"), new Event("input2", "ENTER"), new Event("output1", "EXIT1"), new Event("output1", "EXIT1"));
        formatter = new DaikonFormatter("test.dtrace", false, inputStream, outputStream, declarations);
        formatter.observations(new ProgramRun(events));

        assertEquals("ppt input1:::ENTER\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input2:::ENTER\nppt input2\n  variable input2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt output1:::EXIT1\nppt output1\n  variable output1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", outputStream.toString());

        assertEquals(1, declarations.size());
        assertEquals("input1", declarations.iterator().next());
    }

    @Test
    public void testComplexObservationsWithNestedTransitions() throws Exception {
        final Set<String> declarations = new HashSet<>();
        final List<Event> events = Arrays.asList(new Event("input1", "ENTER"), new Event("input2", "ENTER"), new Event("output1", "EXIT1"), new Event("input3", "ENTER"), new Event("output2", "EXIT1"));
        formatter = new DaikonFormatter("test.dtrace", false, inputStream, outputStream, declarations);
        formatter.observations(new ProgramRun(events));

        assertEquals("ppt input1:::ENTER\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input2:::ENTER\nppt input2\n  variable input2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt output1:::EXIT1\nppt output1\n  variable output1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input3:::ENTER\nppt input3\n  variable input3\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input1,output1\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input1,output2\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", outputStream.toString());

        declarations.forEach(name -> assertEquals(1, formatter.getDeclarations().size()));
    }

    @Test
    public void testComplexObservationsWithNestedTransitionsAndDuplicateDeclaration() throws Exception {
        final Set<String> declarations = new HashSet<>();
        final List<Event> events = Arrays.asList(new Event("input1", "ENTER"), new Event("input2", "ENTER"), new Event("output1", "EXIT1"), new Event("input3", "ENTER"), new Event("output1", "EXIT1"));
        formatter = new DaikonFormatter("test.dtrace", false, inputStream, outputStream, declarations);
        formatter.observations(new ProgramRun(events));

        assertEquals("ppt input1:::ENTER\nppt input1\n  variable input1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt input2:::ENTER\nppt input2\n  variable input2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt output1:::EXIT1\nppt output1\n  variable output1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", outputStream.toString());

        assertEquals(1, declarations.size());
        assertEquals("input1", declarations.iterator().next());
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        assertEquals(2, DaikonFormatter.classesForMultipleProperties("TestInterface").size());
    }

    @Test(expected = RuntimeException.class)
    public void testFailedToLoadInputsAndOutputs() throws Exception {
        final Set<String> declarations = new HashSet<>();
        formatter = new DaikonFormatter("test.dtrace", false, inputStream, outputStream, declarations);
        throw new RuntimeException("expected exception");
    }
}
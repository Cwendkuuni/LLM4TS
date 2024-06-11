package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        assertNotNull(formatter.declarations);
        assertEquals(2, formatter.declarations.size());
        assertNull(formatter.inputs);

        final DaikonFormatter formatterWithInputs = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"), Arrays.asList("input1", "input2"));
        assertNotNull(formatterWithInputs.declarations);
        assertEquals(2, formatterWithInputs.declarations.size());
        assertNotNull(formatterWithInputs.inputs);
        assertEquals(2, formatterWithInputs.inputs.size());
        assertEquals("input1", formatterWithInputs.inputs.iterator().next());
        assertEquals("input2", formatterWithInputs.inputs.iterator().next());
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        final Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(3, classes.size());
        for (Class clazz : classes) {
            assertNotNull(clazz);
        }

        classes = DaikonFormatter.classesForMultipleProperties("OutputInterface");
        assertEquals(2, classes.size());
        for (Class clazz : classes) {
            assertNotNull(clazz);
        }
    }

    @Test
    public void testHeader() {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        final String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        final String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservationsEmpty() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final String observations = formatter.observations(createEmptyProgramRun());
        assertEquals("", observations);
        assertEquals("", formatter.format(output, observations));
    }

    @Test
    public void testObservationsSimple() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final Event event1 = new Event("Class1", "enter", null);
        final Event event2 = new Event("Class2", "exit", null);
        final ProgramRun programRun = createProgramRun(event1, event2);

        final String observations = formatter.observations(programRun);
        assertEquals("ppt Class1\n  variable Class1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class2\n  variable Class2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", observations);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n\n" +
                     "ppt Class1\n  variable Class1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class2\n  variable Class2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n",
                     formatter.format(output, observations));
    }

    @Test
    public void testObservationsComplex() throws IOException {
        final DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("Class1", "Class2"));
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final Event event1 = new Event("Class1", "enter", null);
        final Event event2 = new Event("Class1", "exit", null);
        final Event event3 = new Event("Class2", "enter", null);
        final Event event4 = new Event("Class2", "exit", null);
        final ProgramRun programRun = createProgramRun(event1, event2, event3, event4);

        final String observations = formatter.observations(programRun);
        assertEquals("ppt Class1\n  variable Class1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class1:::EXIT1\n  variable Class1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class2\n  variable Class2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class2:::ENTER\n  variable Class2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", observations);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n\n" +
                     "ppt Class1\n  variable Class1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class1:::EXIT1\n  variable Class1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class2\n  variable Class2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
                     "ppt Class2:::ENTER\n  variable Class2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n",
                     formatter.format(output, observations));
    }

    private ProgramRun createEmptyProgramRun() {
        final Iterator<Event> emptyIterator = Collections.emptyIterator();
        return new ProgramRun(emptyIterator);
    }

    private ProgramRun createProgramRun(Event... events) {
        final List<Event> eventsList = Arrays.asList(events);
        return new ProgramRun(eventsList.iterator());
    }
}
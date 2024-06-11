package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
        assertNotNull(formatter);

        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.daikon", InterfaceParser.methodNames(new String[]{"testMethod1", "testMethod2"}));
        assertNotNull(formatterWithInputs);
    }

    @Test
    public void testClassForProperty() throws Exception {
        Class clazz = DaikonFormatter.classForProperty("InputInterface_0");
        assertNotNull(clazz);
    }

    @Test(expected = NullPointerException.class)
    public void testClassForPropertyNull() throws Exception {
        DaikonFormatter.classForProperty(null);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classes.size());
        assertNotNull(classes.get(0));
        assertNotNull(classes.get(1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testClassesForMultiplePropertiesEmpty() throws Exception {
        DaikonFormatter.classesForMultipleProperties("");
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
        String header = formatter.header();
        assertNotNull(header);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
        String footer = formatter.footer();
        assertNull(footer);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() throws IOException {
        ProgramRun run = new ProgramRun();
        Event event = new Event("testMethod1", "ENTER", new HashMap<String, Object>(){{
            put("attribute1", "value1");
        }});
        run.addEvent(event);

        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        String observations = formatter.observations(run);
        assertNotNull(observations);
        assertEquals("", observations);

        String expected = "ppt testMethod1\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n";
        observations = formatter.observations(new ProgramRun(){{
            addEvent(new Event("testMethod1", "ENTER", new HashMap<String, Object>(){{
                put("attribute1", "value1");
            }}));
            addEvent(new Event("testMethod1", "EXIT", new HashMap<String, Object>(){{
                put("attribute1", "value2");
            }}));
        }});
        assertEquals(expected, observations);
    }

    @Test
    public void testObservationsComplex() throws IOException {
        ProgramRun run = new ProgramRun();
        Event previousEvent = new Event("testMethod1", "ENTER", new HashMap<String, Object>(){{
            put("attribute1", "value1");
        }});
        Event event = new Event("testMethod2", "EXIT", new HashMap<String, Object>(){{
            put("attribute1", "value2");
            put("attribute2", new String[]{"value3", "value4"});
        }});
        run.addEvent(previousEvent);
        run.addEvent(event);

        DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
        String observations = formatter.observations(run);
        assertNotNull(observations);

        String expected = "ppt testMethod1\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
                + "ppt testMethod2\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
                + "ppt testMethod2\n  variable attribute2\n    var-kind array\n    dec-type java.lang.String[]\n    rep-type java.lang.String[]\n    comparability implicit\n"
                + "ppt testMethod1:::ENTER testMethod2:::EXIT\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
                + "ppt testMethod1:::EXIT testMethod2:::EXIT\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n";
        assertEquals(expected, observations);
    }
}
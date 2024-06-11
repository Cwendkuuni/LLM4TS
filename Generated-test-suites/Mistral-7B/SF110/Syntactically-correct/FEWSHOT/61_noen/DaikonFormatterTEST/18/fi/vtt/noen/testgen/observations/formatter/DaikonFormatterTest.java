package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException, Exception {
        final String fileName = "test.dtrace";

        // Test simple case
        DaikonFormatter formatter = new DaikonFormatter(fileName, true);
        assertEquals("", formatter.header());
        assertEquals("", formatter.footer());
        assertEquals("decl-version 2.0\n", formatter.observations(new ProgramRun(Arrays.asList(new Event("init", new HashMap<>(), null, null, EventType.INITIAL_STATE, Collections.emptyList(), false)))));
        assertEquals("decl-version 2.0\n", formatter.observations(new ProgramRun(Arrays.asList(new Event("method1", new HashMap<>(), null, null, EventType.ENTER, Collections.emptyList(), false), new Event("method1", new HashMap<>(), null, null, EventType.EXIT, Collections.emptyList(), false)))));

        // Test complex case with inputs
        HashSet<String> inputNames = new HashSet<>(Arrays.asList("method1", "method2"));
        DaikonFormatter complexFormatter = new DaikonFormatter(fileName, inputNames);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", complexFormatter.header());
        assertEquals("", complexFormatter.footer());
        List<Event> programRun = Arrays.asList(new Event("method1", new HashMap<>(), null, null, EventType.ENTER, Collections.emptyList(), false),
                new Event("method2", new HashMap<>(), null, null, EventType.ENTER, Collections.emptyList(), false),
                new Event("method1", new HashMap<>(), null, null, EventType.EXIT, Collections.emptyList(), false),
                new Event("method2", new HashMap<>(), null, null, EventType.EXIT, Collections.emptyList(), false));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        complexFormatter.observations(new ProgramRun(programRun), out);
        String expectedOutput = "decl-version 2.0\nvar-comparability implicit\n" +
                "ppt method1\n" +
                "ppt-type enter\n" +
                "ppt method2\n" +
                "ppt-type enter\n" +
                "ppt method1\n" +
                "ppt-type exit\n" +
                "ppt method2\n" +
                "ppt-type exit\n" +
                " ";
        assertEquals(expectedOutput, out.toString());

        // Test loading classes from properties file
        Properties properties = new Properties();
        properties.load(new FileInputStream("testgen.properties"));
        assertEquals("fi.vtt.noen.testgen.parser.InputInterface1", classForProperty("InputInterface1").getName());
        assertEquals("fi.vtt.noen.testgen.parser.OutputInterface1", classForProperty("OutputInterface1").getName());
        List<Class> inputClasses = classesForMultipleProperties("InputInterface");
        List<Class> outputClasses = classesForMultipleProperties("OutputInterface");
        assertEquals(2, inputClasses.size());
        assertEquals("fi.vtt.noen.testgen.parser.InputInterface1", inputClasses.get(0).getName());
        assertEquals("fi.vtt.noen.testgen.parser.InputInterface2", inputClasses.get(1).getName());
        assertEquals(2, outputClasses.size());
        assertEquals("fi.vtt.noen.testgen.parser.OutputInterface1", outputClasses.get(0).getName());
        assertEquals("fi.vtt.noen.testgen.parser.OutputInterface2", outputClasses.get(1).getName());
    }

    private static Class classForProperty(String property) throws Exception {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        String className = configuration.getProperty(property);
        if (className == null) {
            return null;
        }
        return Class.forName(className);
    }

    private static Collection<Class> classesForMultipleProperties(String prefix) throws Exception {
        Collection<Class> classes = new ArrayList<Class>();
        int index = 1;
        while (true) {
            Class clazz = classForProperty(prefix + index);
            ++index;
            if (clazz == null) {
                break;
            }
            classes.add(clazz);
        }
        return classes;
    }
}
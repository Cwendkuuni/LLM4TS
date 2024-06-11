package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        String fileName = "test.daikon";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
        assertNull(formatter.footer());
        assertEquals("dtrace", formatter.fileNameExtension());

        String[] methodNames = new String[] {"method1", "method2"};
        DaikonFormatter formatterWithInputs = new DaikonFormatter(fileName, Arrays.asList(methodNames));
        assertEquals(new HashSet<>(Arrays.asList("method1", "method2")), formatterWithInputs.inputs);

        Properties properties = new Properties();
        properties.load(new FileInputStream("test.properties"));
        List<Class> classesForMultipleProperties = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classesForMultipleProperties.size());
        assertEquals(Class.forName(properties.getProperty("InputInterface1")), classesForMultipleProperties.get(0));
        assertEquals(Class.forName(properties.getProperty("InputInterface2")), classesForMultipleProperties.get(1));

        // Test observations method
        List<Event> events = Arrays.asList(new Event("method1", "Event1", new EventAttribute("attribute1", "value1")), new Event("method1", "Event2", new EventAttribute("attribute1", "value2")), new Event("method2", "Event3", new EventAttribute("attribute2", "value3")));
        ProgramRun programRun = new ProgramRun(events);
        String observations = formatter.observations(programRun);

        String expectedObservations = "ppt method1\n" +
                "ppt-type enter\n" +
                "ppt method1:::ENTER\n" +
                "variable attribute1\n" +
                "var-kind variable\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n" +
                "\"value1\"1\n" +
                "\n" +
                "ppt method1\n" +
                "ppt-type enter\n" +
                "ppt method1:::ENTER\n" +
                "variable attribute1\n" +
                "var-kind variable\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n" +
                "\"value2\"1\n" +
                "\n" +
                "ppt method2\n" +
                "ppt-type enter\n" +
                "ppt method2:::ENTER\n" +
                "variable attribute2\n" +
                "var-kind variable\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n" +
                "\"value3\"1\n";

        assertEquals(expectedObservations, observations);

        // Test simple point method
        Event simpleEvent = new Event("simpleMethod", "SimpleEvent", null);
        String simpleObservations = formatter.simplePoint(simpleEvent, "implicit", new OutputBuffer()).toString();
        String expectedSimpleObservations = "ppt simpleMethod\n" +
                "ppt-type enter\n" +
                "ppt simpleMethod:::ENTER\n";

        assertEquals(expectedSimpleObservations, simpleObservations);

    }

}
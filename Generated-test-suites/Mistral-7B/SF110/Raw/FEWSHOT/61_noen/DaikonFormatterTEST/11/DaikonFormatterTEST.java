package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterSimple() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        formatter.setOutputStream(output);

        Event event1 = mock(Event.class);
        when(event1.getName()).thenReturn("event1");
        when(event1.isExit()).thenReturn(false);

        Event event2 = mock(Event.class);
        when(event2.getName()).thenReturn("event2");
        when(event2.isExit()).thenReturn(false);

        Event event3 = mock(Event.class);
        when(event3.getName()).thenReturn("event3");
        when(event3.isExit()).thenReturn(true);

        List<Event> events = Arrays.asList(event1, event2, event3);

        formatter.format(events);

        String expectedOutput = "decl-version 2.0\n" +
                "event1:::ENTER\n" +
                "event1:::ENTER\n" +
                "event2:::ENTER\n" +
                "event3:::EXIT1\n";

        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testDaikonFormatterComplex() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        formatter.setOutputStream(output);

        Properties testgenProperties = new Properties();
        testgenProperties.load(new FileInputStream("testgen.properties"));

        String inputInterface1 = "fi.vtt.noen.testgen.testinterface1.TestInterface1";
        String inputInterface2 = "fi.vtt.noen.testgen.testinterface2.TestInterface2";
        String outputInterface1 = "fi.vtt.noen.testgen.outputinterface1.OutputInterface1";
        String outputInterface2 = "fi.vtt.noen.testgen.outputinterface2.OutputInterface2";

        when(testgenProperties.getProperty("InputInterface1")).thenReturn(inputInterface1);
        when(testgenProperties.getProperty("InputInterface2")).thenReturn(inputInterface2);
        when(testgenProperties.getProperty("OutputInterface1")).thenReturn(outputInterface1);
        when(testgenProperties.getProperty("OutputInterface2")).thenReturn(outputInterface2);

        Set<String> inputs = new HashSet<>(Arrays.asList(inputInterface1, inputInterface2));

        Event event1 = mock(Event.class);
        when(event1.getName()).thenReturn("event1");
        when(event1.isExit()).thenReturn(false);

        Event event2 = mock(Event.class);
        when(event2.getName()).thenReturn("event2");
        when(event2.isExit()).thenReturn(true);

        Event event3 = mock(Event.class);
        when(event3.getName()).thenReturn("event3");
        when(event3.isExit()).thenReturn(false);

        Event event4 = mock(Event.class);
        when(event4.getName()).thenReturn("event4");
        when(event4.isExit()).thenReturn(false);

        Event event5 = mock(Event.class);
        when(event5.getName()).thenReturn("event5");
        when(event5.isExit()).thenReturn(true);

        List<Event> events = Arrays.asList(event1, event2, event3, event4, event5);

        formatter.format(events);

        String expectedOutput = "decl-version 2.0\n" +
                "var-comparability implicit\n" +
                "ppt testinterface1.TestInterface1\n" +
                "ppt-type enter\n" +
                "ppt testinterface2.TestInterface2\n" +
                "ppt-type enter\n" +
                "event1:::ENTER\n" +
                "event1:::ENTER\n" +
                "event2:::ENTER\n" +
                "event3:::EXIT1\n" +
                "event3:::ENTER\n" +
                "event4:::ENTER\n" +
                "event5:::EXIT1\n" +
                "outputinterface1.OutputInterface1\n" +
                "outputinterface1.OutputInterface1\n" +
                "outputinterface2.OutputInterface2\n" +
                "outputinterface2.OutputInterface2\n";

        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testClassForProperty() throws Exception {
        Class clazz = DaikonFormatter.classForProperty("TestInterface1");

        assertEquals(TestInterface1.class, clazz);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");

        List<Class> expectedClasses = Arrays.asList(TestInterface1.class, TestInterface2.class);

        assertEquals(expectedClasses, classes);
    }

    private static class TestInterface1 {
        public void method1() {}
    }

    private static class TestInterface2 {
        public void method2() {}
    }
}
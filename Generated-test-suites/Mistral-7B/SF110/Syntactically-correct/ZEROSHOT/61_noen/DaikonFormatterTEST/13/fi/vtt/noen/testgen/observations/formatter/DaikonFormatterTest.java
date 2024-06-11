package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.observations.data.EventType;
import fi.vtt.noen.testgen.testgen.Constants;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private ProgramRun programRun;

    @Before
    public void setup() throws Exception {
        final Properties properties = new Properties();
        properties.load(new FileInputStream("testgen.properties"));

        final String inputInterfaceFileName = "InputInterface1.java";
        final String outputInterfaceFileName = "OutputInterface1.java";

        final Set<String> expectedInputs = new HashSet<>(Arrays.asList("method1", "method2"));
        final String inputFileContent = "package test;\n" + "public interface InputInterface {\n"
                + "    void method1();\n" + "    void method2();\n" + "}";
        final InputStream inputStream = new ByteArrayInputStream(inputFileContent.getBytes());

        final Set<String> expectedOutputs = new HashSet<>(Arrays.asList("method3"));
        final String outputFileContent = "package test;\n" + "public interface OutputInterface {\n"
                + "    void method3(String arg1);\n" + "}";
        final InputStream outputStream = new ByteArrayInputStream(outputFileContent.getBytes());

        InterfaceParser.saveInterface(inputStream, inputInterfaceFileName, true);
        InterfaceParser.saveInterface(outputStream, outputInterfaceFileName, true);

        formatter = new DaikonFormatter(inputInterfaceFileName, outputInterfaceFileName);

        programRun = new ProgramRun();
        programRun.add(new Event(EventType.ENTER, "method1"));
        programRun.add(new Event(EventType.EXIT, "method1"));
        programRun.add(new Event(EventType.ENTER, "method2"));
        programRun.add(new Event(EventType.EXIT, "method2"));
        programRun.add(new Event(EventType.ENTER, "method3"));
        programRun.add(new Event(EventType.EXIT, "method3"));
    }

    @Test
    public void testDaikonFormatterHeader() {
        final String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, formatter.header());
    }

    @Test
    public void testDaikonFormatterFooter() {
        assertEquals("", formatter.footer());
    }

    @Test
    public void testDaikonFormatterFileNameExtension() {
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testDaikonFormatterSimpleObservations() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String observations = formatter.observations(programRun, true);
        formatter.writeToFile(outputStream, observations);
        final String expected = "ppt method1:::ENTER\n"
                + "ppt method1:::EXIT1\n"
                + "ppt method2:::ENTER\n"
                + "ppt method2:::EXIT1\n"
                + "ppt method3:::ENTER\n"
                + "ppt method3:::EXIT1\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testDaikonFormatterComplexObservations() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String observations = formatter.observations(programRun, false);
        formatter.writeToFile(outputStream, observations);
        final String expected = "ppt method1:::ENTER\n"
                + "ppt method1:::EXIT1\n"
                + "ppt method1:::ENTER method2:::ENTER:22\n"
                + "ppt method1:::EXIT1 method2:::ENTER:22\n"
                + "ppt method1:::EXIT1\n"
                + "ppt method2:::ENTER\n"
                + "ppt method2:::EXIT1\n"
                + "ppt method1:::ENTER method2:::ENTER:22\n"
                + "ppt method1:::EXIT method2:::ENTER:22\n"
                + "ppt method2:::EXIT1\n"
                + "ppt method3:::ENTER\n"
                + "ppt method3:::EXIT1\n"
                + "ppt method3:::ENTER\n"
                + "ppt method3:::EXIT1\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testDaikonFormatterDeclarations() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String observations = formatter.observations(programRun, false);
        formatter.writeToFile(outputStream, observations);

        final Map<String, List<Event>> eventMap = new HashMap<>();
        for (final Event event : programRun) {
            if (eventMap.containsKey(event.getName())) {
                eventMap.get(event.getName()).add(event);
            } else {
                eventMap.put(event.getName(), Arrays.asList(event));
            }
        }

        for (final Map.Entry<String, List<Event>> entry : eventMap.entrySet()) {
            final String name = entry.getKey();
            final List<Event> events = entry.getValue();
            final Event firstEvent = events.get(0);
            final Event lastEvent = events.get(events.size() - 1);

            final String expected = name.replace(' ', '_') + (firstEvent.isExit() ? ":::EXIT1" : ":::ENTER");
            final int index = outputStream.toString().lastIndexOf(expected);
            final String actual = outputStream.toString().substring(index - 100, index + expected.length() + 100);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterException() throws IOException, Exception {
        final String invalidInputFileName = "InvalidInputInterface.java";
        final DaikonFormatter daikonFormatter = new DaikonFormatter(invalidInputFileName, Collections.emptyList());
    }
}
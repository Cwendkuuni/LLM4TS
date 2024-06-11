package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventType;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun run;

 @Before
 public void setup() throws Exception {
 Properties props = new Properties();
 props.setProperty("InputInterface", "fi.vtt.noen.testgen.testinterface.InputInterface");
 props.setProperty("OutputInterface", "fi.vtt.noen.testgen.testinterface.OutputInterface");
 List<String> inputNames = new ArrayList<>();
 inputNames.add("method1");
 inputNames.add("method2");
 InputParser inputParser = new InterfaceParser();
 List<Class<?>> inputs = inputParser.parse(Fixture.getInputStreamForClass(InputInterface.class));
 run = new ProgramRun();
 formatter = new DaikonFormatter("test.dtrace", inputs);
 }

 @Test
 public void testHeader() {
 String expected = "decl-version 2.0\nvar-comparability implicit";
 String actual = formatter.header();
 assertEquals(expected, actual);
 }

 @Test
 public void testFooter() {
 String actual = formatter.footer();
 assertEquals("", actual);
 }

 @Test
 public void testFileNameExtension() {
 String expected = "dtrace";
 String actual = formatter.fileNameExtension();
 assertEquals(expected, actual);
 }

 @Test
 public void testSimplePoint() throws IOException {
 Event event = new Event("method1", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 formatter.simplePoint(event, "10", new OutputBuffer(outputStream));
 String expectedOutput = "ppt method1:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n";
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testPoint() throws IOException {
 Event prevEvent = new Event("method1", EventType.EXIT, Arrays.asList(new EventAttribute("result1", "java.lang.String", "value1")));
 Event event = new Event("method2", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.Integer", "value1"), new EventAttribute("param2", "java.lang.String", "value2")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 formatter.point("method1", prevEvent, "11", new OutputBuffer(outputStream));
 formatter.point("method1", prevEvent, "22", new OutputBuffer(outputStream));
 formatter.point("method2", event, "22", new OutputBuffer(outputStream));
 String expectedOutput = "ppt method1:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n\nppt method1:::EXIT1\nvariable result1\nvariable result1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n\nppt method2:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.Integer\nrep-type java.lang.Integer\ncomparability implicit\nvariable param2\nvariable param2\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n";
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDeclarations() throws IOException {
 Event event = new Event("method1", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 formatter.declarations(event, "method1", new OutputBuffer(outputStream), "implicit");
 String expectedOutput = "ppt method1\nppt-type enter\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n";
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testAttributes() throws IOException {
 Event event = new Event("method1", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 formatter.attributes(event, new OutputBuffer(outputStream));
 String expectedOutput = "param1\"value1\"1\n";
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testObservationsSimple() throws IOException {
 Event prevEvent = new Event("method1", EventType.EXIT, Arrays.asList(new EventAttribute("result1", "java.lang.String", "value1")));
 Event event = new Event("method1", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 formatter.observations(run);
 formatter = new DaikonFormatter("test.dtrace", Collections.singletonList("method1"));
 formatter.observations(run);
 String expectedOutput = "ppt method1:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n";
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testObservations() throws IOException {
 Event prevEvent1 = new Event("method1", EventType.EXIT, Arrays.asList(new EventAttribute("result1", "java.lang.String", "value1")));
 Event event1 = new Event("method2", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1"), new EventAttribute("param2", "java.lang.Integer", "value1")));
 Event prevEvent2 = new Event("method2", EventType.EXIT, Arrays.asList(new EventAttribute("result2", "java.lang.String", "value2")));
 Event event2 = new Event("method3", EventType.ENTER, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1"), new EventAttribute("param2", "java.lang.Integer", "value2")));
 Event prevEvent3 = new Event("method3", EventType.EXIT, Arrays.asList(new EventAttribute("result3", "java.lang.String", "value3")));
 run.addEvent(prevEvent1);
 run.addEvent(event1);
 run.addEvent(prevEvent2);
 run.addEvent(event2);
 run.addEvent(prevEvent3);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 formatter.observations(run);
 String expectedOutput = "ppt method1:::EXIT1\nvariable result1\nvariable result1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n\nppt method2:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\nvariable param2\nvariable param2\ndec-type java.lang.Integer\nrep-type java.lang.Integer\ncomparability implicit\n\nppt method1:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n\nppt method2:::EXIT1\nvariable result2\nvariable result2\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\n\nppt method3:::ENTER\nvariable param1\nvariable param1\ndec-type java.lang.String\nrep-type java.lang.String\ncomparability implicit\nvariable param2\nvariable param2\ndec-type java.lang.Integer\nrep-type java.lang.Integer\ncomparability implicit\n";
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }
}
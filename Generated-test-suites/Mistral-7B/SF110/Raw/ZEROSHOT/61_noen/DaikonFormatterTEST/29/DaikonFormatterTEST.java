package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import fi.vtt.noen.testgen.parser.ParserException;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private Event event;
 private ProgramRun programRun;

 @Before
 public void setUp() throws IOException, ParserException {
 Properties properties = new Properties();
 properties.load(new FileInputStream("src/test/resources/testgen.properties"));
 List<String> inputList = Arrays.asList("InputInterface1", "InputInterface2");
 Set<String> inputSet = new HashSet<String>(inputList);
 formatter = new DaikonFormatter("test.daikon", inputSet);

 programRun = new ProgramRun();
 event = new Event("Event1", "InputInterface1");
 event.setExit(false);
 event.addAttribute(new EventAttribute("attribute1", "value1"));
 programRun.addEvent(event);
 }

 @Test
 public void testHeader() {
 String expected = "decl-version 2.0\nvar-comparability implicit\n";
 String actual = formatter.header();
 assertEquals(expected, actual);
 }

 @Test
 public void testFooter() {
 String expected = "";
 String actual = formatter.footer();
 assertEquals(expected, actual);
 }

 @Test
 public void testFileNameExtension() {
 String expected = "dtrace";
 String actual = formatter.fileNameExtension();
 assertEquals(expected, actual);
 }

 @Test
 public void testObservationsSimple() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expected = "ppt Event1:::ENTER\nppt Event1:::ENTER\n";
 String actual = formatter.observations(programRun, outputStream, true);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String result = formatter.parseObservations(inputStream);
 assertEquals(expected, result);
 }

 @Test
 public void testObservationsComplex() throws IOException, ParserException {
 Event event2 = new Event("Event2", "InputInterface2");
 event2.setExit(true);
 event2.addAttribute(new EventAttribute("attribute1", "value2"));
 programRun.addEvent(event2);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expected = "ppt Event1:::ENTER\n" +
 "ppt Event1:::ENTER\n" +
 "ppt Event1:::EXIT1\n" +
 "ppt Event1:::EXIT1\n" +
 "ppt Event1:::ENTER\n" +
 "ppt Event2:::ENTER\n" +
 "ppt Event2:::EXIT1\n";
 String actual = formatter.observations(programRun, outputStream, false);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String result = formatter.parseObservations(inputStream);
 assertEquals(expected, result);
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 List<Class<?>> classes = formatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 assertTrue(classes.contains(Class.forName("fi.vtt.noen.testgen.testdata.InputInterface1")));
 assertTrue(classes.contains(Class.forName("fi.vtt.noen.testgen.testdata.InputInterface2")));
 }

 @Test
 public void testPointSimple() {
 String expected = "ppt Event1:::ENTER\n" +
 "ppt Event1:::ENTER\n";
 OutputBuffer out = new OutputBuffer();
 formatter.point("Event1", event, "11", out);
 String actual = out.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testPointComplex() {
 String expected = "ppt Event1:::ENTER\n" +
 "ppt Event1:::ENTER\n" +
 "ppt Event1:::EXIT1\n" +
 "ppt Event1:::EXIT1\n" +
 "ppt Event1:::ENTER\n";
 OutputBuffer out = new OutputBuffer();
 formatter.point("Event1", event, "22", out);
 String actual = out.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testDeclarations() {
 Set<String> declarations = new HashSet<String>();
 Event event3 = new Event("Event3", "InputInterface1");
 event3.setExit(false);
 event3.addAttribute(new EventAttribute("attribute1", "value3"));
 formatter.declarations(event3, "Event3", new OutputBuffer(), "implicit");
 declarations.add("Event3");
 assertEquals(declarations, formatter.declarations);
 }

 @Test
 public void testAttributes() {
 OutputBuffer out = new OutputBuffer();
 EventAttribute attribute = new EventAttribute("attribute1", "value1");
 formatter.attributes(event, out);
 assertEquals("attribute1value11", out.toString());
 }

}
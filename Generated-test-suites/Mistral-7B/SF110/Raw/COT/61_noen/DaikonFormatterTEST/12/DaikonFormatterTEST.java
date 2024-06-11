package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.parser.exceptions.ParserException;
import fi.vtt.noen.testgen.parser.interfaces.MethodNameExtractor;
import fi.vtt.noen.testgen.parser.interfaces.ProgramRun;
import fi.vtt.noen.testgen.formatter.BasicFormatter;
import fi.vtt.noen.testgen.formatter.OutputBuffer;
import fi.vtt.noen.testgen.formatter.DaikonFormatter;

public class DaikonFormatterTest {

 private static ProgramRun createTestRun() {
 return new TestProgramRun();
 }

 @Test
 public void testConstructorWithFileNameAndSimple() throws IOException, ParserException {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
 assertEquals(true, daikonFormatter.isSimple());
 assertNull(daikonFormatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithFileNameAndInvalidInputs() throws IOException, ParserException {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList("InvalidInput1", "InvalidInput2"));
 fail("Should have thrown RuntimeException");
 }

 @Test
 public void testConstructorWithFileNameAndValidInputs() throws IOException, ParserException {
 String[] methodNames = new String[]{"method1", "method2"};
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(methodNames));
 assertEquals(Arrays.asList(methodNames), daikonFormatter.getInputs());
 }

 @Test(expected = FileNotFoundException.class)
 public void testClassForPropertyWithInvalidProperty() throws Exception {
 DaikonFormatter.classForProperty("InvalidProperty");
 fail("Should have thrown FileNotFoundException");
 }

 @Test
 public void testClassesForMultiplePropertiesWithValidPrefix() throws Exception {
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 assertTrue(classes.contains(Class.forName("fi.vtt.noen.testgen.parser.InputInterface1")));
 assertTrue(classes.contains(Class.forName("fi.vtt.noen.testgen.parser.InputInterface2")));
 }

 @Test
 public void testHeader() {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 String header = daikonFormatter.header();
 out.append(header);
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", out.toString());
 }

 @Test
 public void testFooter() {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 assertEquals("", daikonFormatter.footer());
 }

 @Test
 public void testFileNameExtension() {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 assertEquals("dtrace", daikonFormatter.fileNameExtension());
 }

 @Test
 public void testObservationsWithSimplePointing() throws IOException, ParserException {
 ProgramRun run = createTestRun();
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
 String observations = daikonFormatter.observations(run);
 assertTrue(observations.isEmpty());
 }

 @Test
 public void testObservationsWithValidPointing() throws IOException, ParserException {
 ProgramRun run = createTestRun();
 MethodNameExtractor extractor = new MethodNameExtractor();
 extractor.setFileName("test.txt");
 extractor.extract();
 List<String> methodNames = extractor.getMethodNames();
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(methodNames.subList(0, 2)));
 String observations = daikonFormatter.observations(run);
 String expected = "ppt method1:::ENTER\n" +
 "ppt method1:::EXIT1\n" +
 "ppt method2:::ENTER\n" +
 "ppt method2:::EXIT1\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testObservationsWithInvalidPointing() throws IOException, ParserException {
 ProgramRun run = createTestRun();
 MethodNameExtractor extractor = new MethodNameExtractor();
 extractor.setFileName("test.txt");
 extractor.extract();
 List<String> methodNames = extractor.getMethodNames();
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(methodNames.subList(0, 1).add("InvalidMethodName")));
 String observations = daikonFormatter.observations(run);
 assertTrue(observations.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclarationsWithInvalidEvent() {
 Event event = new TestEvent("TestEvent");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 daikonFormatter.declarations(event, "TestEvent", new OutputBuffer(), "implicit");
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testDeclarationsWithValidEvent() {
 Event event = new TestEvent("method1");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.declarations(event, "method1", out, "implicit");
 String expected = "ppt method1\n" +
 "ppt-type enter\n";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributesWithValidEvent() {
 Event event = new TestEvent("method1");
 EventAttribute attribute = new TestAttribute("TestAttribute", "TestValue");
 event.addAttribute(attribute);
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.attributes(event, out);
 String expected = "method1\"TestAttribute\"TestValue1\n";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributesWithInvalidEventType() {
 Event event = new TestEvent("method1");
 EventAttribute attribute = new TestAttribute("TestAttribute", "TestValue");
 event.addAttribute(attribute);
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 try {
 daikonFormatter.attributes(new TestEvent("TestEvent"), out);
 fail("Should have thrown IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSimplePointingWithValidEvent() {
 Event event = new TestEvent("method1");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.simplePoint(event, "implicit", out);
 String expected = "method1:::ENTER\n" +
 "method1:::EXIT1\n" +
 "\"method1\"\n";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testPointingWithValidPreviousAndCurrentEvent() {
 Event previousEvent = new TestEvent("method1");
 Event event = new TestEvent("method2");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.point("method1:::ENTER", previousEvent, "implicit", out);
 String expected = "method1:::ENTER\n" +
 "ppt method1\n" +
 "ppt-type enter\n" +
 "\"method1\"\n";
 assertEquals(expected, out.toString());
 }

 @Test
 public void testPointingWithInvalidPreviousEvent() {
 Event previousEvent = new TestEvent("TestEvent");
 Event event = new TestEvent("method2");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.point("method1:::ENTER", previousEvent, "implicit", out);
 assertTrue(out.toString().isEmpty());
 }

 @Test
 public void testPointingWithInvalidCurrentEvent() {
 Event previousEvent = new TestEvent("method1");
 Event event = new TestEvent("TestEvent");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.point("method1:::ENTER", previousEvent, "implicit", out);
 assertTrue(out.toString().isEmpty());
 }

 @Test
 public void testPointingWithInvalidPreviousAndCurrentEvents() {
 Event previousEvent = new TestEvent("TestEvent1");
 Event event = new TestEvent("TestEvent2");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 daikonFormatter.point("method1:::ENTER", previousEvent, "implicit", out);
 assertTrue(out.toString().isEmpty());
 }

 @Test
 public void testDeclarationsWithMultipleEvents() {
 Event event1 = new TestEvent("method1");
 Event event2 = new TestEvent("method2");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList(new String[]{"method1", "method2"}));
 OutputBuffer out = new OutputBuffer();
 Set<String> declarations = new HashSet<>();
 for (Event e : Arrays.asList(event1, event2)) {
 daikonFormatter.declarations(e, "method1:::ENTER", out, "implicit");
 declarations.add(out.toString());
 out.reset();
 }
 String expected = "ppt method1\n" +
 "ppt-type enter\n" +
 "\"method1\"\n" +
 "ppt method2\n" +
 "ppt-type enter\n" +
 "\"method2\"\n";
 List<String> declarationsList = new ArrayList<>(declarations);
 assertEquals(expected, String.join("\n", declarationsList));
 }
}

class TestEvent implements Event {
 private String name;

 public TestEvent(String name) {
 this.name = name;
 }

 @Override
 public String getName() {
 return name;
 }

 @Override
 public boolean isExit() {
 return false;
 }

 @Override
 public Iterator<EventAttribute> iterator() {
 return new Iterator<EventAttribute>() {
 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public EventAttribute next() {
 return null;
 }
 };
 }
}

class TestAttribute implements EventAttribute {
 private String name;
 private String value;

 public TestAttribute(String name, String value) {
 this.name = name;
 this.value = value;
 }

 @Override
 public String getName() {
 return name;
 }

 @Override
 public String getType() {
 return "java.lang.String";
 }

 @Override
 public String getValue() {
 return value;
 }
}

class OutputBuffer {
 private StringBuilder stringBuilder;

 public OutputBuffer() {
 stringBuilder = new StringBuilder();
 }

 public void append(String string) {
 stringBuilder.append(string);
 }

 public void reset() {
 stringBuilder.setLength(0);
 }

 public String toString() {
 return stringBuilder.toString();
 }
}

class TestProgramRun implements ProgramRun {
 // Empty implementation for testing purposes
}
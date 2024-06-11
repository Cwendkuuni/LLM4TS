package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.parser.exceptions.ParseException;
import fi.vtt.noen.testgen.parser.util.FileUtil;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private static final String TEST_FILE = "test.dtrace";
 private static final boolean SIMPLE_MODE = false;
 private static final List<String> INPUTS = Arrays.asList(new String[]{"Input1", "Input2"});
 private static final List<String> OUTPUTS = Arrays.asList(new String[]{"Output1", "Output2"});

 @Before
 public void setUp() throws IOException, ParseException {
 FileUtil.writeFile(new File(TEST_FILE), "test_content".getBytes());
 InputStream inputStream = new FileInputStream(TEST_FILE);
 Properties configuration = new Properties();
 configuration.load(inputStream);
 DaikonFormatter.inputs = INPUTS;
 DaikonFormatter.outputs = OUTPUTS;
 formatter = new DaikonFormatter(TEST_FILE, INPUTS);
 }

 @Test
 public void testConstructorWithFileNameAndInputs() throws IOException {
 DaikonFormatter daikonFormatter = new DaikonFormatter(TEST_FILE, INPUTS);
 assertNotNull(daikonFormatter);
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithFileNameAndSimpleModeThrowsException() throws IOException {
 DaikonFormatter daikonFormatter = new DaikonFormatter(TEST_FILE, null);
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithFileNameThrowsExceptionIfFailedToLoadInputs() throws IOException {
 DaikonFormatter daikonFormatter = new DaikonFormatter(TEST_FILE, null);
 }

 @Test
 public void testConstructorWithFileNameAndSimpleModeIsNullInputs() throws IOException {
 DaikonFormatter daikonFormatter = new DaikonFormatter(TEST_FILE, null);
 assertEquals(null, daikonFormatter.inputs);
 }

 @Test
 public void testDeclarationsMethod() {
 Event event = new ProgramRunEvent("TestEvent");
 String eventName = event.getName();
 formatter.declarations(event, eventName, new OutputBuffer(), "implicit");
 Set<String> declarations = formatter.getDeclarations();
 assertEquals(1, declarations.size());
 assertEquals(eventName, declarations.iterator().next());
 }

 @Test
 public void testSimplePointMethod() throws IOException {
 Event event = new ProgramRunEvent("TestEvent");
 OutputBuffer out = new OutputBuffer();
 formatter.simplePoint(event, "10", out);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nTestEvent:::ENTER\nTestEvent\n", result);
 }

 @Test
 public void testPointMethod() throws IOException {
 Event previousEvent = new ProgramRunEvent("PreviousEvent");
 Event event = new ProgramRunEvent("TestEvent");
 OutputBuffer out = new OutputBuffer();
 formatter.point(previousEvent.getName(), previousEvent, "11", out);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nPreviousEvent:::ENTER\nPreviousEvent\nTestEvent:::ENTER\n", result);
 }

 @Test
 public void testPointMethodWithTwoEventsWithSameName() throws IOException {
 Event event1 = new ProgramRunEvent("TestEvent1");
 Event event2 = new ProgramRunEvent("TestEvent1");
 OutputBuffer out = new OutputBuffer();
 formatter.point(event1.getName(), event1, "11", out);
 formatter.point(event2.getName(), event2, "11", out);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nTestEvent1:::ENTER\nTestEvent1\nTestEvent1:::ENTER\n", result);
 }

 @Test
 public void testPointMethodWithExitEvent() throws IOException {
 Event previousEvent = new ProgramRunEvent("PreviousEvent");
 Event event = new ProgramRunEvent("TestEvent");
 OutputBuffer out = new OutputBuffer();
 formatter.point(previousEvent.getName(), previousEvent, "22", out);
 formatter.point(previousEvent.getName(), previousEvent, "22", out);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nPreviousEvent:::EXIT1\nPreviousEvent\n", result);
 }

 @Test
 public void testPointMethodWithTwoEventsWithSameNameAndOneExit() throws IOException {
 Event previousEvent = new ProgramRunEvent("PreviousEvent");
 Event event = new ProgramRunEvent("TestEvent");
 OutputBuffer out = new OutputBuffer();
 formatter.point(previousEvent.getName(), previousEvent, "22", out);
 Event event2 = new ProgramRunEvent("TestEvent");
 formatter.point(event2.getName(), event2, "11", out);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nPreviousEvent:::EXIT1\nPreviousEvent\nTestEvent:::ENTER\n", result);
 }

 @Test
 public void testAttributesMethod() throws IOException {
 Event event = new ProgramRunEvent("TestEvent");
 event.addAttribute(new EventAttribute("TestAttribute", "TestValue"));
 OutputBuffer out = new OutputBuffer();
 formatter.attributes(event, out);
 String result = out.toString();
 assertEquals("TestAttribute=\"TestValue\"1\n", result);
 }

 @Test
 public void testAttributesMethodWithArrayAttribute() throws IOException {
 Event event = new ProgramRunEvent("TestEvent");
 event.addAttribute(new ArrayAttribute("TestArrayAttribute", new String[]{"ArrayElement1", "ArrayElement2"}));
 OutputBuffer out = new OutputBuffer();
 formatter.attributes(event, out);
 String result = out.toString();
 assertEquals("TestArrayAttribute[]\n  variable TestArrayAttribute[]\n    var-kind array\n    dec-type java.lang.String[]\n    rep-type java.lang.String[]\n    comparability implicit\nTestArrayAttribute[0]=\"ArrayElement1\"1\nTestArrayAttribute[1]=\"ArrayElement2\"1\n", result);
 }

 @Test
 public void testObservationsMethod() throws IOException {
 ProgramRun run = new ProgramRun();
 Event event1 = new ProgramRunEvent("TestEvent1");
 Event event2 = new ProgramRunEvent("TestEvent2");
 run.addEvent(event1);
 run.addEvent(event2);
 OutputBuffer out = new OutputBuffer();
 formatter.observations(run);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nTestEvent1:::ENTER\nTestEvent1\nTestEvent2:::ENTER\nTestEvent2\n", result);
 }

 @Test
 public void testObservationsMethodWithSimpleMode() throws IOException {
 ProgramRun run = new ProgramRun();
 Event event1 = new ProgramRunEvent("TestEvent1");
 Event event2 = new ProgramRunEvent("TestEvent2");
 run.addEvent(event1);
 run.addEvent(event2);
 OutputBuffer out = new OutputBuffer();
 formatter.observations(run, SIMPLE_MODE);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nTestEvent1:::ENTER\nTestEvent1\nTestEvent2:::ENTER\nTestEvent2\n", result);
 }

 @Test
 public void testObservationsMethodWithTwoEventsWithSameNameAndOneExit() throws IOException {
 ProgramRun run = new ProgramRun();
 Event previousEvent = new ProgramRunEvent("PreviousEvent");
 Event event1 = new ProgramRunEvent("TestEvent");
 Event event2 = new ProgramRunEvent("TestEvent");
 run.addEvent(previousEvent);
 run.addEvent(event1);
 run.addEvent(event2);
 OutputBuffer out = new OutputBuffer();
 formatter.observations(run);
 String result = out.toString();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nPreviousEvent:::EXIT1\nPreviousEvent\nTestEvent:::ENTER\nTestEvent\n", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObservationsMethodWithNullRun() throws IOException {
 OutputBuffer out = new OutputBuffer();
 formatter.observations(null);
 }

 @Test
 public void testClassesForMultiplePropertiesMethod() throws Exception {
 List<String> expectedClasses = Arrays.asList(new String[]{"Class1", "Class2"});
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(expectedClasses.size(), classes.size());
 for (int i = 0; i < expectedClasses.size(); i++) {
 assertEquals(expectedClasses.get(i), classes.get(i).getName());
 }
 }

 @Test
 public void testClassesForMultiplePropertiesMethodWithOneClass() throws Exception {
 List<String> expectedClasses = Arrays.asList(new String[]{"Class1"});
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(expectedClasses.size(), classes.size());
 assertEquals(expectedClasses.get(0), classes.get(0).getName());
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesMethodWithEmptyPrefix() throws Exception {
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("");
 }
}
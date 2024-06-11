package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
import fi.vtt.noen.testgen.testdata.TestDataGenerator;
import fi.vtt.noen.testgen.testdata.TestData;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private Event event;
 private Set<String> inputs;

 @Before
 public void setUp() throws IOException, ParserException {
 inputs = InterfaceParser.methodNames(DaikonFormatter.inputs().orElse(Collections.emptyList()));
 }

 @Test
 public void testHeader() {
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
 String result = formatter.header();
 String expected = "decl-version 2.0\n" +
 "var-comparability implicit\n";
 assertEquals(expected, result);
 }

 @Test
 public void testFooter() {
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
 String result = formatter.footer();
 assertEquals("", result);
 }

 @Test
 public void testFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
 String result = formatter.fileNameExtension();
 String expected = "dtrace";
 assertEquals(expected, result);
 }

 @Test
 public void testObservationsSimpleCase() throws IOException, ParserException {
 TestData testData = TestDataGenerator.createTestData();
 ProgramRun run = TestDataGenerator.createProgramRun(testData);
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
 String expected = "";
 String result = formatter.observations(run);
 assertEquals(expected, result);
 }

 @Test
 public void testObservationsComplexCase() throws IOException, ParserException {
 TestData testData = TestDataGenerator.createTestData();
 ProgramRun run = TestDataGenerator.createProgramRun(testData);
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", inputs);
 String expected = "";
 String result = formatter.observations(run);
 assertEquals(expected, result);
 }

 @Test
 public void testSimplePoint() {
 event = TestDataGenerator.createEvent("testEvent", "testMethod", true, Arrays.asList(new EventAttribute("testAttribute", "testValue")));
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", false);
 String inputFileName = "test.daikon";
 OutputStream out = new ByteArrayOutputStream();
 String expected = "ppt testEvent\n" +
 "ppt-type enter\n" +
 "variable testAttribute\n" +
 "var-kind variable\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "1testValue";
 formatter.simplePoint(event, "implicit", out);
 String result = out.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testPoint() {
 event = TestDataGenerator.createEvent("testEvent1", "testMethod1", false, Collections.emptyList());
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", inputs);
 String inputFileName = "test.daikon";
 OutputStream out = new ByteArrayOutputStream();
 String expected = "ppt testEvent1\n" +
 "ppt-type enter\n" +
 "variable testEvent1\n" +
 "var-kind variable\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "1";
 formatter.point("testEvent1", event, "implicit", out);
 String result = out.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testDeclarations() {
 event = TestDataGenerator.createEvent("testEvent2", "testMethod2", false, Arrays.asList(new EventAttribute("testAttribute1", "testValue1"), new EventAttribute("testAttribute2", "testValue2")));
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", inputs);
 String inputFileName = "test.daikon";
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 String expected = "ppt testEvent2\n" +
 "ppt-type enter\n" +
 "variable testEvent2\n" +
 "var-kind variable\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "1testValue1\n" +
 "variable testAttribute1\n" +
 "var-kind variable\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "1testValue1\n" +
 "variable testAttribute2\n" +
 "var-kind variable\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "1testValue2";
 formatter.declarations(event, "testEvent2", out, "implicit");
 String result = out.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testAttributes() {
 event = TestDataGenerator.createEvent("testEvent3", "testMethod3", false, Arrays.asList(new EventAttribute("testAttribute1", "testValue1"), new EventAttribute("testAttribute2", "testValue2"), new EventAttribute("testAttribute3", "testValue3")));
 DaikonFormatter formatter = new DaikonFormatter("test.daikon", inputs);
 String inputFileName = "test.daikon";
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 String expected = "testAttribute1\"testValue1\"1\n" +
 "testAttribute2\"testValue2\"1\n" +
 "testAttribute3\"testValue3\"1";
 formatter.attributes(event, out);
 String result = out.toString();
 assertEquals(expected, result);
 }
}
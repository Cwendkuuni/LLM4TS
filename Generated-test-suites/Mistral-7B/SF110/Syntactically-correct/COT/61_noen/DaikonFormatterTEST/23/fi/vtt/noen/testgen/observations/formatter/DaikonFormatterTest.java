package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;
import fi.vtt.noen.testgen.parser.ParseException;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.parser.TestgenProperties;

public class DaikonFormatterTest {

 private static final String TEST_FILE = "testFile.dtrace";

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndSimple() throws IOException, ParseException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 assertNull(formatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructorWithFileNameAndInvalidInputs() throws IOException, ParseException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndInputs() throws IOException, ParseException {
 Set<String> inputs = new HashSet<>(Arrays.asList("Method1", "Method2"));
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, inputs);
 assertEquals(inputs, formatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructorWithFileNameAndInvalidInputsFile() throws IOException, ParseException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Collections.singleton("InvalidInterface"));
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithInvalidPrefix() throws Exception {
 DaikonFormatter.classesForMultipleProperties("InvalidPrefix");
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterHeader() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("Method1", "Method2"));
 assertNotNull(formatter.header());
 }

 @Test
 public void testDaikonFormatterFooter() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("Method1", "Method2"));
 assertEquals("", formatter.footer());
 }

 @Test
 public void testDaikonFormatterFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("Method1", "Method2"));
 assertEquals("dtrace", formatter.fileNameExtension());
 }

 @Test
 public void testObservationsSimple() throws IOException, ParseException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 ProgramRun programRun = new ProgramRun(new File("testProgram.out"));
 String observations = formatter.observations(programRun);
 assertEquals("", observations);
 }

 @Test
 public void testObservationsWithInputs() throws IOException, ParseException {
 Set<String> inputs = new HashSet<>(Arrays.asList("Method1", "Method2"));
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, inputs);
 ProgramRun programRun = new ProgramRun(new File("testProgram.out"));
 String observations = formatter.observations(programRun);
 // Add assertions for the generated Daikon code based on the expected output
 }

 @Test
 public void testSimplePointMethod() {
 Event event = new Event("Method1", Arrays.asList(new EventAttribute("Attribute1", "StringType", "Value1")), true);
 OutputBuffer out = new OutputBuffer();
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 formatter.simplePoint(event, "implicit", out);
 String expectedOutput = "ppt Method1\n  variable Attribute1\n    var-kind variable\n    dec-type StringType\n    rep-type StringType\n    comparability implicit\n";
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testPointMethod() {
 Event previousEvent = new Event("Method1", null, false);
 Event event = new Event("Method2", Arrays.asList(new EventAttribute("Attribute1", "StringType", "Value1")), true);
 OutputBuffer out = new OutputBuffer();
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("Method1", "Method2"));
 formatter.point("Method1:::ENTER", previousEvent, "implicit", out);
 formatter.point("Method2:::ENTER", event, "implicit", out);
 String expectedOutput = "ppt Method1\n  variable Attribute1\n    var-kind variable\n    dec-type StringType\n    rep-type StringType\n    comparability implicit\n\nppt Method2\n  variable Attribute1\n    var-kind variable\n    dec-type StringType\n    rep-type StringType\n    comparability implicit\n";
 assertEquals(expectedOutput, out.toString());
 }

}
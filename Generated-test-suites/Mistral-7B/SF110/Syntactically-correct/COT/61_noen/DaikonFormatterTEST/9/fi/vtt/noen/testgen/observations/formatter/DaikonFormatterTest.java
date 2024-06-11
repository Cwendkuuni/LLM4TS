package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import fi.vtt.noen.testgen.observations.data.Const;
import fi.vtt.noen.testgen.parser.ProgramRun;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.parser.TestDataProvider;
import fi.vtt.noen.testgen.observations.formatter.BasicFormatter;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun programRun;

 @Before
 public void setUp() throws Exception {
 Properties properties = new Properties();
 properties.load(new FileInputStream("testgen.properties"));
 List<String> inputInterfaces = Arrays.asList(properties.getProperty("InputInterface1"), properties.getProperty("InputInterface2"));
 List<String> outputInterfaces = Arrays.asList(properties.getProperty("OutputInterface1"));
 formatter = new DaikonFormatter("test.dtrace", inputInterfaces);
 programRun = TestDataProvider.getProgramRun();
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndInputs() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", formatter.inputs());
 assertEquals(formatter.inputs(), daikonFormatter.inputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructorWithFileNameAndNullInputs() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", null);
 fail("Should throw RuntimeException");
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndSimpleMode() throws Exception {
 DaikonFormatter simpleDaikonFormatter = new DaikonFormatter("test.dtrace", true);
 assertNull(simpleDaikonFormatter.inputs);
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface1");
 assertEquals("fi.vtt.noen.testgen.parser.InputInterface1", clazz.getName());
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyWithNullProperty() throws Exception {
 DaikonFormatter.classForProperty(null);
 fail("Should throw Exception");
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 Set<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithNullPrefix() throws Exception {
 DaikonFormatter.classesForMultipleProperties(null);
 fail("Should throw Exception");
 }

 @Test
 public void testHeader() {
 String header = formatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testFooter() {
 String footer = formatter.footer();
 assertTrue(footer.isEmpty());
 }

 @Test
 public void testFileNameExtension() {
 String fileNameExtension = formatter.fileNameExtension();
 assertEquals("dtrace", fileNameExtension);
 }

 @Test
 public void testObservationsWithSimpleMode() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 String observations = formatter.observations(programRun);
 formatter.format(new ByteArrayInputStream(observations.getBytes()), outputStream);
 String result = outStream.toString();
 assertEquals("ppt INITIAL_STATE:::ENTER\nppt INITIAL_STATE:::EXIT1\n", result);
 }

 @Test
 public void testObservationsWithComplexMode() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 String observations = formatter.observations(programRun);
 formatter.format(new ByteArrayInputStream(observations.getBytes()), outputStream);
 String result = outStream.toString();
 Set<String> expectedDeclarations = new HashSet<>(Arrays.asList("event1", "event2", "event3"));
 Set<String> actualDeclarations = new HashSet<>();
 String[] lines = result.split("\n");
 for (int i = 1; i < lines.length; i++) {
 String line = lines[i];
 if (line.startsWith("ppt")) {
 String declaration = line.split(" ")[1];
 actualDeclarations.add(declaration);
 }
 }
 assertEquals(expectedDeclarations, actualDeclarations);
 }

 @Test
 public void testPointWithSimpleMode() {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 Event event = new Event("event1", Collections.emptyList(), Collections.emptyList(), "event1_VALUE");
 String observations = formatter.simplePoint(event, "10", new OutputBuffer());
 formatter.format(new ByteArrayInputStream(observations.getBytes()), outputStream);
 String result = outStream.toString();
 assertEquals("ppt event1:::ENTER\nppt event1\nevent1_VALUE1\n", result);
 }

 @Test
 public void testPointWithComplexMode() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 Event event1 = new Event("event1", Arrays.asList("event1_input1", "event1_input2"), Collections.emptyList(), "event1_VALUE");
 Event event2 = new Event("event2", Collections.emptyList(), Arrays.asList("event2_output1", "event2_output2"), "event2_VALUE");
 String observations = formatter.point("event1", event1, "11", new OutputBuffer());
 formatter.format(new ByteArrayInputStream(observations.getBytes()), outputStream);
 String result = outStream.toString();
 assertEquals("ppt event1:::ENTER\nppt event1\nevent1_input1\nevent1_input2\nevent1_VALUE1\n", result);
 observations = formatter.point("event1", event1, "22", new OutputBuffer());
 result = outStream.toString();
 assertEquals("ppt event1:::EXIT1\nppt event1\nevent1_VALUE1\n", result);
 observations = formatter.point("event2", event2, "11", new OutputBuffer());
 result = outStream.toString();
 assertEquals("ppt event2:::ENTER\nppt event2\nevent2_output1\nevent2_output2\nevent2_VALUE1\n", result);
 }

 @Test
 public void testDeclarations() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 Event event = new Event("event1", Arrays.asList("event1_input1", "event1_input2"), Arrays.asList("event1_output1", "event1_output2"), "event1_VALUE");
 String observations = formatter.observations(programRun);
 formatter.format(new ByteArrayInputStream(observations.getBytes()), outputStream);
 String result = outStream.toString();
 Set<String> expectedDeclarations = new HashSet<>(Arrays.asList("event1", "event1_input1", "event1_input2", "event1_output1", "event1_output2"));
 Set<String> actualDeclarations = new HashSet<>();
 String[] lines = result.split("\n");
 for (int i = 1; i < lines.length; i++) {
 String line = lines[i];
 if (line.startsWith("ppt")) {
 String declaration = line.split(" ")[1];
 actualDeclarations.add(declaration);
 }
 }
 assertEquals(expectedDeclarations, actualDeclarations);
 }

 @Test
 public void testAttributes() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 Event event = new Event("event1", Collections.emptyList(), Arrays.asList(new EventAttribute("event1_attribute", "event1_VALUE")), "event1_VALUE");
 String observations = formatter.observations(programRun);
 formatter.format(new ByteArrayInputStream(observations.getBytes()), outputStream);
 String result = outStream.toString();
 assertEquals(event.getAttributes().toString(), result);
 }

}
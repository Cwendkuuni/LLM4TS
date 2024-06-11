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

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.Comparability;

public class DaikonFormatterTest {

 private static final String TEST_GEN_PROPERTIES = "testgen.properties";

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndSimple() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", true);
 assertTrue(daikonFormatter.isSimple());
 assertNull(daikonFormatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithFileNameAndInvalidInputs() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", Collections.singletonList("InvalidInput"));
 }

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndValidInputs() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 assertFalse(daikonFormatter.isSimple());
 assertNotNull(daikonFormatter.getInputs());
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface_0");
 assertNotNull(clazz);
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyWithInvalidPropertyName() throws Exception {
 DaikonFormatter.classForProperty("InvalidPropertyName");
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

 @Test
 public void testHeader() {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 String header = daikonFormatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testFooter() {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 String footer = daikonFormatter.footer();
 assertTrue(footer.isEmpty());
 }

 @Test
 public void testFileNameExtension() {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 assertEquals("dtrace", daikonFormatter.fileNameExtension());
 }

 @Test
 public void testObservationsSimple() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", true);
 Event event = new Event("EventName");
 InputStream inputStream = new ByteArrayInputStream(new byte[]{});
 ProgramRun programRun = new ProgramRun(inputStream);
 programRun.add(event);
 String observations = daikonFormatter.observations(programRun);
 assertEquals("", observations);
 }

 @Test
 public void testObservationsWithValidInputs() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 Event previousEvent = new Event("PreviousEventName");
 Event event = new Event("EventName");
 InputStream inputStream = new ByteArrayInputStream(new byte[]{"PreviousEventName".getBytes(), "EventName".getBytes()});
 ProgramRun programRun = new ProgramRun(inputStream);
 programRun.add(previousEvent);
 programRun.add(event);
 String observations = daikonFormatter.observations(programRun);
 // The generated Daikon format string should be equal to this hardcoded
 // string for the given test case.
 String expected = "decl-version 2.0\nvar-comparability implicit\nppt PreviousEventName\n  variable PreviousEventName\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt EventName\n  variable EventName\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n";
 assertEquals(expected, observations);
 }

 @Test(expected = RuntimeException.class)
 public void testObservationsWithNullProgramRun() throws Exception {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 String observations = daikonFormatter.observations(null);
 }

 @Test
 public void testDeclarations() throws Exception {
 Event event = new Event("EventName");
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 HashSet<String> declarations = new HashSet<>(Arrays.asList("EventName", "EventName_EXIT"));
 daikonFormatter.declarations(event, "EventName", new OutputBuffer(), Comparability.IMPLICIT.toString());
 assertEquals(declarations, daikonFormatter.getDeclarations());
 }

 @Test
 public void testAttributes() throws Exception {
 Event event = new Event("EventName");
 EventAttribute attribute = new EventAttribute("AttributeName", "java.lang.String", "AttributeValue");
 event.setAttributes(Collections.singletonList(attribute));
 DaikonFormatter daikonFormatter = new DaikonFormatter("test-file.txt", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[]{})));
 StringBuilder output = new StringBuilder();
 daikonFormatter.attributes(event, output);
 assertEquals("AttributeName\"AttributeValue1\"\n", output.toString());
 }
}
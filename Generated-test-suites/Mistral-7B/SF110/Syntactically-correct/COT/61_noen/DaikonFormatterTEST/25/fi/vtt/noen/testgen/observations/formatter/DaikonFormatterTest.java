package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.observations.data.Event;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.parser.PropertyLoader;
import fi.vtt.noen.testgen.model.ProgramRun;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ByteArrayOutputStream outputStream;
 private Collection<String> inputs;
 private Collection<String> expectedDeclarations;

 @Before
 public void setUp() throws Exception {
 inputs = Arrays.asList("Method1", "Method2");
 expectedDeclarations = new HashSet<String>(Arrays.asList("Method1:::ENTER", "Method1:::EXIT1", "Method2:::ENTER", "Method2:::EXIT1"));
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testDaikonFormatterConstructorSimple() throws IOException {
 formatter = new DaikonFormatter("test.log", true);
 assertNull(formatter.getInputs());
 }

 @Test
 public void testDaikonFormatterConstructorWithInputs() throws IOException {
 formatter = new DaikonFormatter("test.log", inputs);
 assertNotNull(formatter.getInputs());
 assertEquals(inputs, formatter.getInputs());
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface_0");
 assertNotNull(clazz);
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyNullProperty() throws Exception {
 DaikonFormatter.classForProperty(null);
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesNullPrefix() throws Exception {
 DaikonFormatter.classesForMultipleProperties(null);
 }

 @Test
 public void testInputs() throws Exception {
 Collection<Class> inputs = DaikonFormatter.inputs();
 assertEquals(2, inputs.size());
 }

 @Test
 public void testHeader() {
 String header = formatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testFooter() {
 String footer = formatter.footer();
 assertEquals("", footer);
 }

 @Test
 public void testFileNameExtension() {
 String extension = formatter.fileNameExtension();
 assertEquals("dtrace", extension);
 }

 @Test
 public void testObservationsSimple() throws Exception {
 Event event = new Event("Method1", false);
 formatter = new DaikonFormatter("test.log", true);
 formatter.observations(new ProgramRun(Arrays.asList(event), null));
 String result = formatter.getOutput();
 assertEquals("ppt Method1:::ENTER\n", result);
 }

 @Test
 public void testObservationsWithInputs() throws Exception {
 Event previousEvent = new Event("Method1", false);
 Event event = new Event("Method2", false);
 formatter = new DaikonFormatter("test.log", inputs);
 formatter.observations(new ProgramRun(Arrays.asList(previousEvent, event), null));
 String result = formatter.getOutput();
 List<String> lines = Arrays.asList(result.split("\n"));
 assertEquals(13, lines.size());
 for (int i = 0; i < lines.size(); i++) {
 String line = lines.get(i);
 if (i == 0) {
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", line);
 continue;
 }
 if (i < 7) {
 String expectedDeclaration = expectedDeclarations.iterator().next();
 assertEquals(expectedDeclaration.substring(0, expectedDeclaration.lastIndexOf(":")), line.substring(0, line.indexOf(":")));
 expectedDeclarations.remove(expectedDeclaration);
 continue;
 }
 if (i == 7) {
 assertEquals("ppt Method1:::ENTER\n", line);
 continue;
 }
 if (i == 8) {
 assertEquals("ppt Method2:::ENTER\n", line);
 continue;
 }
 if (i == 9) {
 assertEquals("ppt Method1:::EXIT1\n", line);
 continue;
 }
 if (i == 10) {
 assertEquals("ppt Method2:::EXIT1\n", line);
 continue;
 }
 throw new AssertionError("Unexpected declaration: " + line);
 }
 }

 @Test
 public void testDeclarations() {
 Event event = new Event("Method1", false);
 formatter = new DaikonFormatter("test.log", false);
 formatter.setInputs(inputs);
 formatter.declarations(event, "Method1:::ENTER", outputStream, "implicit");
 String result = outputStream.toString();
 assertEquals("ppt Method1:::ENTER\n  variable Method1 \n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", result);
 }

 @Test
 public void testAttributes() {
 Event event = new Event("Method1", false);
 event.addAttribute(new EventAttribute("attribute1", "value1"));
 formatter = new DaikonFormatter("test.log", false);
 formatter.setInputs(inputs);
 formatter.attributes(event, outputStream);
 String result = outputStream.toString();
 assertEquals("attribute1\"value1\"1\n", result);
 }
}
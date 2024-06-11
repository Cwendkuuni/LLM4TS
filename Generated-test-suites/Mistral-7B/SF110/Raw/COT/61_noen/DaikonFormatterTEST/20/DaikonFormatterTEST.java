package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

 private static final String TEST_GEN_PROPERTIES = "testgen.properties";

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndSimpleFlag() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
 assertNotNull(formatter);
 assertTrue(formatter.isSimple());
 assertNull(formatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithFileNameAndNullInputs() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", null);
 fail("Expected RuntimeException to be thrown");
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithFileNameAndInvalidInputs() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("invalidInput1", "invalidInput2"));
 fail("Expected RuntimeException to be thrown");
 }

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndValidInputs() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 assertNotNull(formatter);
 assertFalse(formatter.isSimple());
 assertFalse(formatter.getInputs().isEmpty());
 assertNotNull(formatter.getInputs());
 assertEquals(Arrays.asList("input1", "input2"), formatter.getInputs());
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface");
 assertNotNull(clazz);
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyWithNullProperty() {
 DaikonFormatter.classForProperty(null);
 fail("Expected Exception to be thrown");
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertFalse(classes.isEmpty());
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithEmptyPrefix() {
 DaikonFormatter.classesForMultipleProperties("");
 fail("Expected Exception to be thrown");
 }

 @Test
 public void testDaikonFormatterHeader() {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 String header = formatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testDaikonFormatterFooter() {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 String footer = formatter.footer();
 assertTrue(footer.isEmpty());
 }

 @Test
 public void testDaikonFormatterFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 String extension = formatter.fileNameExtension();
 assertEquals("dtrace", extension);
 }

 @Test
 public void testObservationsWithSimpleFlag() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
 ProgramRun run = new ProgramRun();
 Event event = new Event();
 event.setName("input1");
 run.addEvent(event);
 String observations = formatter.observations(run);
 String expected = "ppt input1\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testObservationsWithValidInputAndOutput() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("input1");
 Event event = new Event();
 event.setName("output1");
 run.addEvent(previousEvent);
 run.addEvent(event);
 String observations = formatter.observations(run);
 String expected = "ppt input1\n" +
         "ppt input1:::ENTER\n" +
         "ppt input1:::EXIT\n" +
         "ppt output1\n" +
         "ppt output1:::ENTER\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testObservationsWithInvalidPreviousInput() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("invalidInput");
 Event event = new Event();
 event.setName("input1");
 run.addEvent(previousEvent);
 run.addEvent(event);
 String observations = formatter.observations(run);
 assertTrue(observations.isEmpty());
 }

 @Test
 public void testObservationsWithInvalidNextInput() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("input1");
 Event event = new Event();
 event.setName("output1");
 run.addEvent(previousEvent);
 Event nextEvent = new Event();
 nextEvent.setName("invalidInput");
 run.addEvent(nextEvent);
 String observations = formatter.observations(run);
 assertTrue(observations.isEmpty());
 }

 @Test
 public void testObservationsWithInvalidPreviousAndNextInput() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("invalidInput1");
 Event event = new Event();
 event.setName("input1");
 run.addEvent(previousEvent);
 run.addEvent(event);
 Event nextEvent = new Event();
 nextEvent.setName("invalidInput2");
 run.addEvent(nextEvent);
 String observations = formatter.observations(run);
 assertTrue(observations.isEmpty());
 }

 @Test
 public void testObservationsWithExitEvent() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("input1");
 Event event = new Event();
 event.setName("output1");
 event.setExit(true);
 run.addEvent(previousEvent);
 run.addEvent(event);
 String observations = formatter.observations(run);
 String expected = "ppt input1\n" +
         "ppt input1:::EXIT\n" +
         "ppt output1\n" +
         "ppt output1:::ENTER\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testObservationsWithExitPreviousEvent() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("input1");
 previousEvent.setExit(true);
 Event event = new Event();
 event.setName("output1");
 run.addEvent(previousEvent);
 run.addEvent(event);
 String observations = formatter.observations(run);
 String expected = "ppt input1:::EXIT\n" +
         "ppt output1\n" +
         "ppt output1:::ENTER\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testObservationsWithExitNextEvent() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("input1");
 Event event = new Event();
 event.setName("output1");
 Event nextEvent = new Event();
 nextEvent.setName("output2");
 nextEvent.setExit(true);
 run.addEvent(previousEvent);
 run.addEvent(event);
 run.addEvent(nextEvent);
 String observations = formatter.observations(run);
 String expected = "ppt input1\n" +
         "ppt input1:::ENTER\n" +
         "ppt input1:::EXIT\n" +
         "ppt output1\n" +
         "ppt output1:::ENTER\n" +
         "ppt output2\n" +
         "ppt output2:::ENTER\n" +
         "ppt output2:::EXIT\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testObservationsWithExitBothEvents() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event previousEvent = new Event();
 previousEvent.setName("input1");
 previousEvent.setExit(true);
 Event event = new Event();
 event.setName("output1");
 event.setExit(true);
 run.addEvent(previousEvent);
 run.addEvent(event);
 String observations = formatter.observations(run);
 String expected = "ppt input1:::EXIT\n" +
         "ppt output1:::EXIT\n";
 assertEquals(expected, observations);
 }

 @Test
 public void testDeclarationsAndAttributes() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("input1", "input2"));
 Event event = new Event();
 event.setName("input1");
 event.setAttributes(Arrays.asList(new StringAttribute("attribute1", "value1"), new ArrayAttribute("arrayAttribute", new String[]{"value0", "value1"})));
 ProgramRun run = new ProgramRun();
 run.addEvent(event);
 String observations = formatter.observations(run);
 String expected = "ppt input1\n" +
         "ppt input1:::ENTER\n" +
         "  variable attribute1\n" +
         "    var-kind variable\n" +
         "    dec-type java.lang.String\n" +
         "    rep-type java.lang.String\n" +
         "    comparability implicit\n" +
         "    value value1\n" +
         "  variable arrayAttribute[]\n" +
         "    var-kind array\n" +
         "    dec-type java.lang.String[]\n" +
         "    rep-type java.lang.String[]\n" +
         "    comparability implicit\n" +
         "  ppt-type enter\n";
 assertEquals(expected, observations);
 }
}
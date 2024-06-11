package fi.vtt.noen.testgen.observations.formatter.test;

import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.model.*;
import java.io.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

 private static final String TEST_FILE = "test.dtrace";

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndSimpleBoolean() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithFileNameAndIOException() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 }

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndInputs() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithFileNameAndNullInputs() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 }

 @Test
 public void testDaikonFormatterClassForPropertySuccess() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface0");
 }

 @Test(expected = Exception.class)
 public void testDaikonFormatterClassForPropertyFailure() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("NonExistingProperty");
 }

 @Test
 public void testDaikonFormatterClassesForMultiplePropertiesSuccess() throws Exception {
 Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 }

 @Test(expected = Exception.class)
 public void testDaikonFormatterClassesForMultiplePropertiesFailure() throws Exception {
 Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("NonExistingPrefix");
 }

 @Test
 public void testDaikonFormatterHeader() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 String header = formatter.header();
 Assert.assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testDaikonFormatterFooter() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 String footer = formatter.footer();
 Assert.assertEquals("", footer);
 }

 @Test
 public void testDaikonFormatterFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 String fileNameExtension = formatter.fileNameExtension();
 Assert.assertEquals("dtrace", fileNameExtension);
 }

 @Test
 public void testDaikonFormatterObservations() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 ProgramRun run = new ProgramRun();
 Event event1 = new Event("event1", false);
 event1.addAttribute(new SimpleAttribute("name1", "value1"));
 run.addEvent(event1);
 Event event2 = new Event("event2", true);
 event2.addAttribute(new ArrayAttribute("name2[]", Arrays.asList("value2_1", "value2_2")));
 run.addEvent(event2);
 String observations = formatter.observations(run);
 Assert.assertTrue(observations.contains("decl-version 2.0\nvar-comparability implicit\n"));
 Assert.assertTrue(observations.contains("ppt event1"));
 Assert.assertTrue(observations.contains("ppt-type enter"));
 Assert.assertTrue(observations.contains("ppt event2"));
 Assert.assertTrue(observations.contains("ppt-type exit"));
}

 @Test
 public void testDaikonFormatterSimplePoint() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 Event event = new Event("event", false);
 String result = formatter.simplePoint(event, "comparability", new OutputBuffer());
 Assert.assertEquals("", result);
 Assert.assertEquals("event:::ENTER", formatter.getLastDeclaredName());
 Assert.assertEquals("event", formatter.getLastDeclaredNameWithoutComparability());
}

 @Test
 public void testDaikonFormatterPoint() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 Event previousEvent = new Event("previousEvent", false);
 Event event = new Event("event", true);
 String result = formatter.point(previousEvent.getName(), previousEvent, "comparability", new OutputBuffer());
 Assert.assertEquals("", result);
 Assert.assertEquals("previousEvent:::ENTER", formatter.getLastDeclaredName());
 Assert.assertEquals("previousEvent", formatter.getLastDeclaredNameWithoutComparability());
}

 @Test
 public void testDaikonFormatterDeclarations() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 Event event = new Event("event", false);
 formatter.declarations(event, "eventName", new OutputBuffer(), "comparability");
}

 @Test
 public void testDaikonFormatterAttributes() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("input1", "input2"));
 Event event = new Event("event", false);
 event.addAttribute(new SimpleAttribute("name1", "value1"));
 OutputBuffer out = new OutputBuffer();
 formatter.attributes(event, out);
 Assert.assertEquals("name1\"value11", out.toString());
}

}
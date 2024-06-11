package fi.vtt.noen.testgen.observations.formatter.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.TestgenProperties;
import fi.vtt.noen.testgen.testgen.ProgramRun;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun programRun;

 @Before
 public void setUp() throws IOException {
 Properties properties = new Properties();
 properties.load(new ByteArrayInputStream(TestgenProperties.TESTGEN_PROPERTIES.getBytes()));
 InputStream inputsStream = new ByteArrayInputStream(TestgenProperties.INPUT_INTERFACES.getBytes());
 InputStream outputsStream = new ByteArrayInputStream(TestgenProperties.OUTPUT_INTERFACES.getBytes());
 formatter = new DaikonFormatter("test.dtrace", false);
 programRun = new ProgramRun();
 Event event1 = new Event("event1", Arrays.asList(new EventAttribute("attr1", "type1")), false);
 Event event2 = new Event("event2", Arrays.asList(new EventAttribute("attr2", "type2")), true);
 programRun.addEvent(event1);
 programRun.addEvent(event2);
 }

 @Test
 public void testDaikonFormatterHeader() {
 String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
 String actualHeader = formatter.header();
 assertEquals(expectedHeader, actualHeader);
 }

 @Test
 public void testDaikonFormatterFooter() {
 String expectedFooter = "";
 String actualFooter = formatter.footer();
 assertEquals(expectedFooter, actualFooter);
 }

 @Test
 public void testDaikonFormatterFileNameExtension() {
 String expectedFileNameExtension = "dtrace";
 String actualFileNameExtension = formatter.fileNameExtension();
 assertEquals(expectedFileNameExtension, actualFileNameExtension);
 }

 @Test
 public void testDaikonFormatterSimpleObservations() {
 OutputBuffer expectedOutput = new OutputBuffer();
 expectedOutput.append("decl-version 2.0\nvar-comparability implicit\n");
 expectedOutput.append("ppt event1:::ENTER ppt-type enter var-kind variable dec-type java.lang.String comparability implicit\n");
 expectedOutput.append("ppt event1:::ENTER ppt-type enter var-kind variable dec-type java.lang.String comparability implicit\n");
 expectedOutput.append("ppt event2:::EXIT1 ppt-type exit var-kind variable dec-type java.lang.String comparability implicit\n");

 String actualOutput = formatter.observations(programRun);
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testDaikonFormatterComplexObservations() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TestgenProperties.INPUT_INTERFACES.getBytes());
 formatter = new DaikonFormatter("test.dtrace", Collections.singletonList("event1"));
 String expectedOutput = "decl-version 2.0\nvar-comparability implicit\n\nppt event1:::ENTER ppt-type enter var-kind variable dec-type java.lang.String comparability implicit\n";
 expectedOutput += "ppt event1:::ENTER ppt-type enter var-kind variable dec-type java.lang.String comparability implicit\n";
 expectedOutput += "ppt event1:::EXIT1 ppt-type exit var-kind variable dec-type java.lang.String comparability implicit\n";
 expectedOutput += "ppt event2:::ENTER ppt-type enter var-kind variable dec-type java.lang.String comparability implicit\n";
 expectedOutput += "ppt event2:::EXIT1 ppt-type exit var-kind variable dec-type java.lang.String comparability implicit\n";

 List<Event> events = ProgramRun.fromInputStream(inputsStream);
 for (Event event : events) {
 programRun.addEvent(event);
 }

 String actualOutput = formatter.observations(programRun);
 assertEquals(expectedOutput, actualOutput);
 }

}
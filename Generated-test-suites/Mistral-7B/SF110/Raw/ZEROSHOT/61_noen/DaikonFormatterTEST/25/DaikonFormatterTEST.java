package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import fi.vtt.noen.testgen.observations.data.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun run;

 @Before
 public void setUp() throws Exception {
 // Initialize testgen.properties file for ClassForProperty and classesForMultipleProperties methods
 Properties props = new Properties();
 props.setProperty("InputInterface_1", "fi.vtt.noen.testgen.testinterface.InputInterface1");
 props.setProperty("InputInterface_2", "fi.vtt.noen.testgen.testinterface.InputInterface2");
 props.setProperty("OutputInterface", "fi.vtt.noen.testgen.testinterface.OutputInterface");
 File file = new File("testgen.properties");
 file.createNewFile();
 try (FileWriter writer = new FileWriter(file)) {
 writer.write(props.toString());
 }

 // Initialize test data
 run = new ProgramRun();
 Event event1 = new Event("event1", "Class1", Arrays.asList(new EventAttribute("attribute1", "value1")), false);
 Event event2 = new Event("event2", "Class2", Arrays.asList(new EventAttribute("attribute2", "value2")), true);
 run.addEvent(event1);
 run.addEvent(event2);

 formatter = new DaikonFormatter("test.dtrace", Arrays.asList("event1", "event2"));
 }

 @Test
 public void testHeader() {
 String expected = "decl-version 2.0\nvar-comparability implicit\n";
 assertEquals(expected, formatter.header());
 }

 @Test
 public void testFooter() {
 assertEquals("", formatter.footer());
 }

 @Test
 public void testFileNameExtension() {
 assertEquals("dtrace", formatter.fileNameExtension());
 }

 @Test
 public void testObservationsSimple() throws IOException {
 // Test observations method with simple flag set to true
 formatter = new DaikonFormatter("test.dtrace", true);
 String actual = formatter.observations(run);
 String expected = "";
 assertEquals(expected, actual);

 // Test observations method with simple flag set to false and simple point method called
 formatter = new DaikonFormatter("test.dtrace", true);
 String header = formatter.header();
 String observations = formatter.observations(run);
 assertEquals(header, formatter.header());
 assertEquals(expected, observations);

 formatter = new DaikonFormatter("test.dtrace", false);
 formatter.simplePoint(event1, "10", new OutputBuffer());
 formatter.simplePoint(event2, "10", new OutputBuffer());
 observations = formatter.observations(run);
 String expectedObservations = "ppt event1\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
         "ppt event2\n  variable attribute2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
 assertEquals(expectedObservations, observations);
 }

 @Test
 public void testObservationsComplex() throws IOException {
 // Test observations method with simple flag set to false and point method called
 formatter = new DaikonFormatter("test.dtrace", false);
 String header = formatter.header();
 String observations = formatter.observations(run);
 assertEquals(header, formatter.header());
 assertEquals("", observations);

 Event previousEvent = null;
 for (Event event : run) {
 formatter.point(previousEvent == null ? "" : previousEvent.getName() + "," + event.getName(), event, "22", new OutputBuffer());
 previousEvent = event;
 }
 observations = formatter.observations(run);
 String expectedObservations = "ppt event1:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
         "ppt event1:::EXIT1\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
         "ppt event2:::ENTER\n  variable attribute2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n" +
         "ppt event2:::EXIT\n  variable attribute2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
 assertEquals(expectedObservations, observations);
 }

 @Test
 public void testDeclarations() throws IOException {
 // Test declarations method
 Event event = new Event("event", "Class", Arrays.asList(new EventAttribute("attribute1", "value1")), false);
 formatter = new DaikonFormatter("test.dtrace", false);
 String expected = "ppt event\n  variable attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n";
 OutputBuffer out = new OutputBuffer();
 formatter.declarations(event, "event", out, "implicit");
 String actual = out.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testAttributes() throws IOException {
 // Test attributes method
 EventAttribute attribute = new EventAttribute("attribute", "value");
 Event event = new Event("event", "Class", Arrays.asList(attribute), false);
 formatter = new DaikonFormatter("test.dtrace", false);
 String expected = "attribute\"value\"1\n";
 OutputBuffer out = new OutputBuffer();
 formatter.attributes(event, out);
 String actual = out.toString();
 assertEquals(expected, actual);
 }
}

class OutputBuffer {
 private StringBuilder sb = new StringBuilder();

 public void append(String s) {
 sb.append(s);
 }

 public String toString() {
 return sb.toString();
 }
}

class TestInterface1 {
}

class TestInterface2 {
}

class TestInterface3 {
}

class TestInterface4 {
}
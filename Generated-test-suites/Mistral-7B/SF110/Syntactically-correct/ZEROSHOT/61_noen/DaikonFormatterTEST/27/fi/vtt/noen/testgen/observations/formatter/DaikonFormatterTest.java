import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun programRun;

 @Before
 public void setUp() throws IOException {
 final File configFile = new File("testgen.properties");
 configFile.createNewFile();
 try (FileWriter writer = new FileWriter(configFile)) {
 writer.write("InputInterface=fi.vtt.noen.testgen.observations.data.InputInterface");
 writer.write("\nOutputInterface=fi.vtt.noen.testgen.observations.data.OutputInterface");
 }

 final String fileName = "test.dtrace";
 formatter = new DaikonFormatter(fileName, true);
 programRun = new ProgramRun();
 }

 @Test
 public void testHeader() {
 final String expected = "decl-version 2.0\nvar-comparability implicit";
 final String actual = formatter.header();
 assertEquals(expected, actual);
 }

 @Test
 public void testFooter() {
 final String actual = formatter.footer();
 assertEquals("", actual);
 }

 @Test
 public void testFileNameExtension() {
 final String expected = "dtrace";
 final String actual = formatter.fileNameExtension();
 assertEquals(expected, actual);
 }

 @Test
 public void testObservationsSimplePoint() throws Exception {
 final Event event = new Event("init");
 formatter.simplePoint(event, "10", new OutputBuffer());
 final String expected = "ppt init\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.simplePoint(event, "10", out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservationsPoint() throws Exception {
 final Event prevEvent = new Event("read");
 prevEvent.setExit(true);
 final Event event = new Event("write");
 formatter.point(prevEvent.getName(), prevEvent, "11", new OutputBuffer());
 final String expected = "ppt read:::ENTER\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.point(prevEvent.getName(), prevEvent, "11", out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservationsPointWithExit() throws Exception {
 final Event prevEvent = new Event("read");
 final Event event = new Event("write");
 formatter.point(prevEvent.getName(), prevEvent, "22", new OutputBuffer());
 final String expected = "ppt read:::EXIT1\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.point(prevEvent.getName(), prevEvent, "22", out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservationsPointWithSameName() throws Exception {
 final Event prevEvent = new Event("read");
 final Event event = new Event("read");
 formatter.point(prevEvent.getName(), prevEvent, "22", new OutputBuffer());
 final String expected = "";
 final OutputBuffer out = new OutputBuffer();
 formatter.point(prevEvent.getName(), prevEvent, "22", out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservationsPointWithDifferentNames() throws Exception {
 final Event prevEvent = new Event("read");
 final Event event = new Event("write");
 formatter.point("read,write", prevEvent, "22", new OutputBuffer());
 final String expected = "ppt read,write\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.point("read,write", prevEvent, "22", out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservationsPointWithExitEvent() throws Exception {
 final Event prevEvent = new Event("read");
 prevEvent.setExit(true);
 final Event event = new Event("write");
 formatter.point("read,write", prevEvent, "22", new OutputBuffer());
 final String expected = "ppt read:::EXIT1\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.point("read,write", prevEvent, "22", out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testDeclarations() throws Exception {
 final Event event = new Event("event");
 final String eventName = "event:::ENTER";
 formatter.declarations(event, eventName, new OutputBuffer(), "implicit");
 final String expected = "ppt " + eventName + "\n";
 final OutputBuffer out = new OutputBuffer();formatter.declarations(event, eventName, out, "implicit");
 assertEquals(expected, out.toString());
 }

 @Test
 public void testAttributes() throws Exception {
 final Event event = new Event("event");
 final EventAttribute attribute = new EventAttribute("attribute", "java.lang.String", "value");
 event.addAttribute(attribute);
 final String expected = "variable attribute\n\"value\"1\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.attributes(event, out);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservations() throws Exception {
 final Event event1 = new Event("event1");
 final Event event2 = new Event("event2");
 final Event event3 = new Event("event3");
 programRun.addEvent(event1);
 programRun.addEvent(event2);
 programRun.addEvent(event3);
 final String expected = "ppt event1:::ENTER\nppt event1\nppt event2:::ENTER\nppt event1,event2\nppt event2:::EXIT1\nppt event3:::ENTER\nppt event3:::EXIT1\n";
 final OutputBuffer out = new OutputBuffer();
 formatter.observations(programRun);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testObservationsWithSimpleMode() throws Exception {
 final Event event1 = new Event("event1");
 final Event event2 = new Event("event2");
 final Event event3 = new Event("event3");
 programRun.addEvent(event1);
 programRun.addEvent(event2);
 programRun.addEvent(event3);
 final String expected = "ppt event1:::ENTER\nppt event1:::EXIT\nppt event2:::ENTER\nppt event2:::EXIT\nppt event3:::ENTER\nppt event3:::EXIT\n";
 formatter = new DaikonFormatter(fileName, true);
 final OutputBuffer out = new OutputBuffer();
 formatter.observations(programRun);
 assertEquals(expected, out.toString());
 }

 @Test
 public void testInputs() throws Exception {
 final Collection<String> inputs = DaikonFormatter.inputs();
 final List<String> expected = new ArrayList<>();
 expected.add("InputInterface");
 expected.add("InputInterface_1");
 expected.add("InputInterface_2");
 assertEquals(expected, inputs);
 }
}
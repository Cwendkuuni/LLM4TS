package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private List<Class> inputInterfaces;
 private List<Class> outputInterfaces;

 @Before
 public void setup() throws Exception {
 System.setProperty("testgen.properties", getClass().getResource("/testgen.properties").getFile());
 inputInterfaces = DaikonFormatter.inputs();
 outputInterfaces = DaikonFormatter.outputs();
 }

 @Test
 public void testConstructorWithFileNameAndSimple() throws IOException {
 formatter = new DaikonFormatter("test.daikon", true);
 assertNotNull(formatter.declarations);
 assertNull(formatter.inputs);
 }

 @Test
 public void testConstructorWithFileNameAndInputs() throws IOException {
 formatter = new DaikonFormatter("test.daikon", inputInterfaces);
 assertNotNull(formatter.declarations);
 assertNotNull(formatter.inputs);
 }

 @Test(expected = RuntimeException.class)
 public void testFailedToLoadInterfaces() throws IOException {
 formatter = new DaikonFormatter("test.daikon", null);
 }

 @Test(expected = RuntimeException.class)
 public void testFailedToLoadInterfaceDefinition() throws IOException {
 InputInterface incorrectInterface = new InputInterface() {
 @Override
 public String getName() {
 return "IncorrectInterface";
 }
 };
 formatter = new DaikonFormatter("test.daikon", Arrays.asList(incorrectInterface, outputInterfaces.get(0)));
 }

 @Test
 public void testHeader() {
 OutputBuffer expectedOutput = new OutputBuffer();
 expectedOutput.append("decl-version 2.0");
 expectedOutput.append("var-comparability implicit");
 String actualOutput = formatter.header();
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testFooter() {
 String actualOutput = formatter.footer();
 assertEquals("", actualOutput);
 }

 @Test
 public void testFileNameExtension() {
 String actualOutput = formatter.fileNameExtension();
 assertEquals("dtrace", actualOutput);
 }

 @Test
 public void testObservationsEmptyRun() {
 ProgramRun emptyRun = new ProgramRun();
 String actualOutput = formatter.observations(emptyRun);
 assertEquals("", actualOutput);
 }

 @Test
 public void testObservationsSingleEvent() {
 Event singleEvent = new Event("Event1") {};
 ProgramRun singleRun = new ProgramRun();
 singleRun.addEvent(singleEvent);
 String actualOutput = formatter.observations(singleRun);
 String expectedOutput = new OutputBuffer() {
 @Override
 public String toString() {
 return "";
 }
 }.append("ppt Event1").append("ppt-type enter").toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testObservationsMultipleEvents() {
 Event event1 = new Event("Event1") {};
 Event event2 = new Event("Event2") {};
 ProgramRun run = new ProgramRun();
 run.addEvent(event1);
 run.addEvent(event2);
 String actualOutput = formatter.observations(run);
 String expectedOutput = new OutputBuffer() {
 @Override
 public String toString() {
 return "";
 }
 }.append("ppt Event1").append("ppt-type enter").append("ppt Event2").append("ppt-type enter");
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testObservationsInputOutputOrder() {
 Event event1 = new Event("Event1") {};
 Event event2 = new Event("Event2") {};
 ProgramRun run = new ProgramRun();
 run.addEvent(event2);
 run.addEvent(event1);
 String actualOutput = formatter.observations(run);
 String expectedOutput = new OutputBuffer() {
 @Override
 public String toString() {
 return "";
 }
 }.append("ppt Event1").append("ppt-type enter").append("ppt Event2").append("ppt-type enter");
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testObservationsInputOutputSameName() {
 Event event1 = new Event("Event1") {};
 Event event2 = new Event("Event1") {};
 ProgramRun run = new ProgramRun();
 run.addEvent(event1);
 run.addEvent(event2);
 String actualOutput = formatter.observations(run);
 assertNull(actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObservationsNullEvent() {
 Event nullEvent = null;
 ProgramRun run = new ProgramRun();
 run.addEvent(nullEvent);
 String actualOutput = formatter.observations(run);
 }

 @Test
 public void testDeclarations() {
 Event event = new Event("Event1") {};
 String eventName = "Event1";
 String comparability = "implicit";
 OutputBuffer expectedOutput = new OutputBuffer();
 expectedOutput.append("ppt " + eventName);
 expectedOutput.append("ppt-type enter");
 expectedOutput.append("variable Event1 variable var-kind variable dec-type java.lang.Object rep-type java.lang.Object comparability " + comparability);
 String actualOutput = formatter.declarations(event, eventName, new OutputBuffer(), comparability).toString();
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testAttributes() {
 Event event = new Event("Event1") {};
 event.addAttribute(new StringAttribute("Attribute1", "Value1"));
 event.addAttribute(new IntAttribute("Attribute2", 1));
 OutputBuffer expectedOutput = new OutputBuffer();
 expectedOutput.append("Attribute1\"Value1\"1");
 expectedOutput.append("Attribute21");
 String actualOutput = formatter.attributes(event, new OutputBuffer()).toString();
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testAttributesEmptyEvent() {
 Event emptyEvent = new Event("Event1") {};
 OutputBuffer expectedOutput = new OutputBuffer();
 String actualOutput = formatter.attributes(emptyEvent, new OutputBuffer()).toString();
 assertEquals("", actualOutput);
 }

 @Test
 public void testAttributesNullEvent() {
 Event nullEvent = null;
 OutputBuffer expectedOutput = new OutputBuffer();
 String actualOutput = formatter.attributes(nullEvent, new OutputBuffer()).toString();
 assertEquals("", actualOutput);
 }
}
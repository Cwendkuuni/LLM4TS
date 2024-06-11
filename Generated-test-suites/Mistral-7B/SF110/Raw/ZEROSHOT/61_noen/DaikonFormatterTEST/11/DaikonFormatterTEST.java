package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.ArrayAttribute;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.observations.data.EventType;
import fi.vtt.noen.testgen.parsers.InterfaceParser;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun programRun;

 @Before
 public void setUp() throws Exception {
 Properties configuration = new Properties();
 configuration.load(new FileInputStream("testgen.properties"));
 formatter = new DaikonFormatter("test.daikon", false);
 programRun = createTestProgramRun();
 }

 @Test
 public void testDaikonFormatterHeader() {
 String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
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
 public void testDaikonFormatterSimplePoint() throws IOException {
 Event event = new Event("init", EventType.ENTER, null, null);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedOutput = "ppt init:::ENTER\nvariable init\ncomparability implicit\n";
 formatter.simplePoint(event, "implicit", new OutputBuffer(outputStream));
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDaikonFormatterPoint() throws IOException {
 Event previousEvent = new Event("init", EventType.ENTER, null, null);
 Event event = new Event("main", EventType.ENTER, null, null);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedOutput = "ppt init:::ENTER\nvariable init\ncomparability implicit\nppt main:::ENTER\nvariable main\ncomparability implicit\n";
 formatter.point(previousEvent.getName(), previousEvent, "implicit", new OutputBuffer(outputStream));
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDaikonFormatterDeclarations() throws Exception {
 Event event = new Event("init", EventType.ENTER, null, null);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedOutput = "ppt init:::ENTER\nvariable init\ncomparability implicit\n";
 formatter.declarations(event, "init", new OutputBuffer(outputStream), "implicit");
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDaikonFormatterAttributes() throws IOException {
 Event event = new Event("init", EventType.ENTER, null, null);
 event.addAttribute(new ArrayAttribute("arrayAttribute", new int[0], null));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedOutput = "ppt init:::ENTER\nvariable init\ncomparability implicit\nvariable arrayAttribute[]\ncomparability implicit\n";
 formatter.attributes(event, new OutputBuffer(outputStream));
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDaikonFormatterObservationsSimple() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedOutput = "ppt init:::ENTER\nvariable init\ncomparability implicit\nppt main:::ENTER\nvariable main\ncomparability implicit\nppt main:::EXIT1\nvariable main\ncomparability implicit\n";
 formatter.observations(programRun);
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDaikonFormatterObservationsComplex() throws IOException, Exception {
 programRun = createTestProgramRunWithInputsAndExits();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedOutput = "ppt init:::ENTER\nvariable init\ncomparability implicit\nppt main:::ENTER\nvariable main\ncomparability implicit\nppt main:::EXIT1\nvariable main\ncomparability implicit\nppt main:::EXIT2\nvariable main\ncomparability implicit\nppt print_int:::ENTER\nvariable print_int\ncomparability implicit\nppt print_int:::EXIT1\nvariable print_int\ncomparability implicit\nppt print_int:::EXIT2\nvariable print_int\ncomparability implicit\nppt print_array:::ENTER\nvariable print_array\ncomparability implicit\nppt print_array:::EXIT1\nvariable print_array\ncomparability implicit\nppt print_array:::EXIT2\nvariable print_array\ncomparability implicit\n";
 formatter.observations(programRun);
 String actualOutput = outputStream.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 private ProgramRun createTestProgramRun() {
 ProgramRun programRun = new ProgramRun();
 Event event1 = new Event("init", EventType.ENTER, null, null);
 Event event2 = new Event("main", EventType.ENTER, null, null);
 Event event3 = new Event("main", EventType.EXIT, null, null);
 Event event4 = new Event("main", EventType.EXIT, null, null);
 Event event5 = new Event("print_int", EventType.ENTER, null, null);
 Event event6 = new Event("print_int", EventType.EXIT, null, null);
 Event event7 = new Event("print_int", EventType.EXIT, null, null);
 Event event8 = new Event("print_array", EventType.ENTER, null, null);
 Event event9 = new Event("print_array", EventType.EXIT, null, null);
 Event event10 = new Event("print_array", EventType.EXIT, null, null);
 programRun.addEvent(event1);
 programRun.addEvent(event2);
 programRun.addEvent(event3);
 programRun.addEvent(event4);
 programRun.addEvent(event5);
 programRun.addEvent(event6);
 programRun.addEvent(event7);
 programRun.addEvent(event8);
 programRun.addEvent(event9);
 programRun.addEvent(event10);
 return programRun;
 }

 private ProgramRun createTestProgramRunWithInputsAndExits() throws Exception {
 ProgramRun programRun = new ProgramRun();
 Event event1 = new Event("init", EventType.ENTER, null, null);
 Event event2 = new Event("main", EventType.ENTER, null, null);
 Event event3 = new Event("main", EventType.EXIT, null, null);
 Event event4 = new Event("main", EventType.EXIT, null, null);
 Event event5 = new Event("print_int", EventType.ENTER, null, null);
 Event event6 = new Event("print_int", EventType.EXIT, null, null);
 Event event7 = new Event("print_int", EventType.EXIT, null, null);
 Event event8 = new Event("print_array", EventType.ENTER, null, null);
 Event event9 = new Event("print_array", EventType.EXIT, null, null);
 Event event10 = new Event("print_array", EventType.EXIT, null, null);
 Event event11 = new Event("print_int_input", EventType.ENTER, null, null);
 Event event12 = new Event("print_int_input", EventType.EXIT, null, null);
 Event event13 = new Event("print_array_input", EventType.ENTER, null, null);
 Event event14 = new Event("print_array_input", EventType.EXIT, null, null);
 programRun.addEvent(event1);
 programRun.addEvent(event2);
 programRun.addEvent(event3);
 programRun.addEvent(event4);
 programRun.addEvent(event5);
 programRun.addEvent(event6);
 programRun.addEvent(event7);
 programRun.addEvent(event8);
 programRun.addEvent(event9);
 programRun.addEvent(event10);
 programRun.addEvent(event11);
 programRun.addEvent(event12);
 programRun.addEvent(event13);
 programRun.addEvent(event14);
 return programRun;
 }

}
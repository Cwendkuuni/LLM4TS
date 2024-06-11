package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.parser.ParserException;
import fi.vtt.noen.testgen.observations.data.ObservationData;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun programRun;

 @Before
 public void setUp() throws IOException, ParserException {
 InputStream inputStream = new ByteArrayInputStream(
 "InputInterface testMethod1, testMethod2
 OutputInterface Output1, Output2".getBytes());
 List<String> inputs = InterfaceParser.methodNames(inputStream);
 formatter = new DaikonFormatter("test.daikon", inputs);
 programRun = new ProgramRun();
 programRun.add(new Event("testMethod1", Arrays.asList(new EventAttribute("input", "value1")), false));
 programRun.add(new Event("testMethod1", Collections.emptyList(), true));
 programRun.add(new Event("testMethod2", Arrays.asList(new EventAttribute("input", "value2")), false));
 programRun.add(new Event("testMethod2", Collections.emptyList(), true));
 }

 @Test
 public void testDaikonFormatterHeader() {
 String expected = "decl-version 2.0\nvar-comparability implicit";
 String actual = formatter.header();
 assertEquals(expected, actual);
 }

 @Test
 public void testDaikonFormatterFooter() {
 String expected = "";
 String actual = formatter.footer();
 assertEquals(expected, actual);
 }

 @Test
 public void testDaikonFormatterFileNameExtension() {
 String expected = "dtrace";
 String actual = formatter.fileNameExtension();
 assertEquals(expected, actual);
 }

 @Test
 public void testDaikonFormatterObservationsSimple() {
 String expected = "ppt testMethod1:::ENTER\nppt testMethod1:::EXIT1\nppt testMethod2:::ENTER\nppt testMethod2:::EXIT1";
 String actual = formatter.observations(programRun, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testDaikonFormatterObservationsComplex() {
 String expected = "ppt testMethod1:::ENTER\nppt testMethod1:::EXIT1\nppt testMethod1:::ENTER ppt testMethod2:::ENTER\nppt testMethod1:::EXIT1 ppt testMethod2:::EXIT1\nppt testMethod2:::ENTER\nppt testMethod2:::EXIT1";
 String actual = formatter.observations(programRun, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testDaikonFormatterDeclarations() {
 String expected = "ppt testMethod1\nppt-type enter\nvariable input\n variable input\n dec-type java.lang.String\n rep-type java.lang.String\n comparability implicit\nvariable input\n variable input\n dec-type java.lang.String\n rep-type java.lang.String\n comparability implicit\n";
 String actual = formatter.header() + "\n" + formatter.declarations(programRun.get(0), "testMethod1", new ByteArrayOutputStream(), "implicit")
 .toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testDaikonFormatterAttributes() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 EventAttribute attribute = new EventAttribute("input", "value1");
 String expected = "input\"value1\"1";
 formatter.attributes(programRun.get(0), outputStream);
 assertEquals(expected, outputStream.toString());
 }

}
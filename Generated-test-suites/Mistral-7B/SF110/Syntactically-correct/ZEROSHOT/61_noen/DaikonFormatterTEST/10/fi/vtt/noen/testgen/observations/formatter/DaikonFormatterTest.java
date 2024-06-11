package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun programRun;

 @Before
 public void setUp() throws Exception {
 final Properties configuration = new Properties();
 configuration.setProperty("InputInterface", "fi.vtt.noen.testgen.observations.formatter.test.TestInputInterface");
 configuration.setProperty("OutputInterface", "fi.vtt.noen.testgen.observations.formatter.test.TestOutputInterface");

 formatter = new DaikonFormatter("test.daikon", Collections.emptyList());
 programRun = new ProgramRun();
 final List<Event> events = Arrays.asList(
 new Event("init", null, null, null, Collections.emptyList(), null, null, null),
 new Event("method1", "input1", null, null, Collections.emptyList(), null, null, null),
 new Event("method2", "input2", null, null, Collections.emptyList(), null, null, null),
 new Event("method3", "input2", "output1", null, null, null, null, null),
 new Event("method4", "input1", "output2", null, null, null, null, null),
 new Event("method5", null, "output3", null, null, null, null, null),
 new Event("method6", null, null, "exit", null, null, null, null)
 );
 programRun.setEvents(events);
 }

 @Test
 public void testFileNameExtension() {
 assertEquals("dtrace", formatter.fileNameExtension());
 }

 @Test
 public void testHeader() throws IOException {
 final ByteArrayOutputStream out = new ByteArrayOutputStream();
 assertEquals(formatter.header(), out.toString());
 }

 @Test
 public void testFooter() {
 assertEquals("", formatter.footer());
 }

 @Test
 public void testObservations() throws IOException {
 final ByteArrayOutputStream out = new ByteArrayOutputStream();
 formatter.observations(programRun, out);
 final String result = out.toString();

 assertEquals(
 "decl-version 2.0\n" +
 "var-comparability implicit\n" +
 "ppt init\n" +
 "ppt-type enter\n" +
 "ppt method1\n" +
 "ppt-type enter\n" +
 "ppt-comparability implicit\n" +
 "variable input1\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt method2\n" +
 "ppt-type enter\n" +
 "ppt-comparability implicit\n" +
 "variable input2\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt method3\n" +
 "ppt-type enter\n" +
 "ppt-comparability implicit\n" +
 "variable input2\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "variable output1\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt method4\n" +
 "ppt-type enter\n" +
 "ppt-comparability implicit\n" +
 "variable input1\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "variable output2\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt method5\n" +
 "ppt-type enter\n" +
 "ppt-comparability implicit\n" +
 "ppt method6\n" +
 "ppt-type exit\n" +
 "", result
 );
 }

 @Test
 public void testDeclarationsSimple() throws IOException {
 final ByteArrayOutputStream out = new ByteArrayOutputStream();
 formatter = new DaikonFormatter("test.daikon", true);
 programRun = new ProgramRun();
 final List<Event> events = Arrays.asList(
 new Event("init", null, null, null, Collections.emptyList(), null, null, null),
 new Event("method1", "input1", null, null, Collections.emptyList(), null, null, null),
 new Event("method1", null, "output1", null, null, null, null, null),
 new Event("method2", "input2", null, null, Collections.emptyList(), null, null, null),
 new Event("method3", "input2", "output2", null, null, null, null, null),
 new Event("method4", "input1", "output3", null, null, null, null, null),
 new Event("method5", null, null, "exit", null, null, null, null)
 );
 programRun.setEvents(events);
 formatter.observations(programRun, out);
 final String result = out.toString();

 assertEquals(
 "decl-version 2.0\n" +
 "var-comparability implicit\n" +
 "ppt init\n" +
 "ppt-type enter\n" +
 "ppt init:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method1\n" +
 "ppt-type enter\n" +
 "ppt input1:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method1:::EXIT1\n" +
 "ppt-comparability implicit\n" +
 "ppt method1:::EXIT1\n" +
 "ppt-comparability implicit\n" +
 "ppt method2\n" +
 "ppt-type enter\n" +
 "ppt method2:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method3\n" +
 "ppt-type enter\n" +
 "ppt method3:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method4\n" +
 "ppt-type enter\n" +
 "ppt method4:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method5\n" +
 "ppt-type enter\n" +
 "ppt method5:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method6\n" +
 "ppt-type exit\n" +
 "ppt method6:::EXIT\n" +
 "ppt-comparability implicit\n" +
 "", result
 );
 }

 @Test
 public void testDeclarationsComplex() throws Exception {
 final ByteArrayOutputStream out = new ByteArrayOutputStream();
 final List<Event> events = Arrays.asList(
 new Event("init", null, null, null, Collections.emptyList(), null, null, null),
 new Event("method1", "input1", null, null, Collections.emptyList(), null, null, null),
 new Event("method1", null, "output1", null, null, null, null, null),
 new Event("method2", "input2", null, null, Collections.emptyList(), null, null, null),
 new Event("method3", "input2", "output2", null, null, null, null, null),
 new Event("method4", "input1", "output3", null, null, null, null, null),
 new Event("method5", null, null, "exit", null, null, null, null)
 );
 programRun.setEvents(events);
 formatter = new DaikonFormatter("test.daikon", Arrays.asList("method1", "method3"));
 formatter.observations(programRun, out);
 final String result = out.toString();

 assertEquals(
 "decl-version 2.0\n" +
 "var-comparability implicit\n" +
 "ppt init\n" +
 "ppt-type enter\n" +
 "ppt init:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method1\n" +
 "ppt-type enter\n" +
 "ppt input1:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method1:::EXIT1\n" +
 "ppt method1:::EXIT1\n" +
 "ppt method2\n" +
 "ppt-type enter\n" +
 "ppt method2:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method3\n" +
 "ppt-type enter\n" +
 "ppt input2:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method3:::EXIT1\n" +
 "ppt method3:::ENTER\n" +
 "ppt output2\n" +
 "ppt-type variable\n" +
 "ppt output2\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt method4\n" +
 "ppt-type enter\n" +
 "ppt input1:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method4:::EXIT1\n" +
 "ppt method4:::ENTER\n" +
 "ppt output3\n" +
 "ppt-type variable\n" +
 "ppt output3\n" +
 "decl-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt method5\n" +
 "ppt-type enter\n" +
 "ppt method5:::ENTER\n" +
 "ppt-comparability implicit\n" +
 "ppt method6\n" +
 "ppt-type exit\n" +
 "ppt method6:::EXIT\n" +
 "ppt-comparability implicit\n" +
 "", result
 );
 }
}
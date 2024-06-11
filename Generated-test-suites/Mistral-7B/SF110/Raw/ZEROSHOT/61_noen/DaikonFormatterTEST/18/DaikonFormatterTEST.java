package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.Const;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.observations.formatter.BasicFormatter;

public class DaikonFormatterTest {

 private static final String TEST_FILE = "test.dtrace";

 @Test
 public void testDaikonFormatter() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, false); // simple formatter
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());

 ProgramRun run = new ProgramRun();
 run.add(new Event("main", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "hello"))));
 run.add(new Event("print", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "world"))));
 run.add(new Event("main", true, Collections.emptyList()));

 String observations = formatter.observations(run);
 assertEquals("", observations);

 formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("main", "print")); // non-simple formatter
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());

 run = new ProgramRun();
 run.add(new Event("main", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "hello"))));
 run.add(new Event("print", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "world"))));
 run.add(new Event("main", true, Collections.emptyList()));

 observations = formatter.observations(run);
 Set<String> expectedDeclarations = new HashSet<String>(Arrays.asList("main:::ENTER", "print:::ENTER", "main:::EXIT1", "main:::EXIT1", "main:::ENTER"));
 Set<String> declarations = new HashSet<String>();
 String[] lines = observations.split("\n");
 for (String line : lines) {
 if (line.startsWith("ppt ")) {
 declarations.add(line.substring(4).split(" ")[0]);
 }
 }
 assertEquals(expectedDeclarations, declarations);
 }

 @Test
 public void testDaikonFormatter_SimplePoint() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, false); // simple formatter
 Event event = new Event("test", false, Collections.emptyList());
 String observations = formatter.observations(new ProgramRun(Collections.singletonList(event)));
 String expectedObservations = "ppt test:::ENTER\n";
 assertEquals(expectedObservations, observations);
 }

 @Test
 public void testDaikonFormatter_Point() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("test1", "test2")); // non-simple formatter
 Event previousEvent = new Event("test1", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "arg1")));
 Event event = new Event("test2", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "arg2")));
 ProgramRun run = new ProgramRun();
 run.add(previousEvent);
 run.add(event);
 String observations = formatter.observations(run);
 Set<String> expectedDeclarations = new HashSet<String>(Arrays.asList("test1:::ENTER", "test2:::ENTER", "test1:::EXIT1", "test2:::EXIT1", "test1:::ENTER test2:::ENTER"));
 Set<String> declarations = new HashSet<String>();
 String[] lines = observations.split("\n");
 for (String line : lines) {
 if (line.startsWith("ppt ")) {
 declarations.add(line.substring(4).split(" ")[0]);
 }
 }
 assertEquals(expectedDeclarations, declarations);
 }

 @Test
 public void testDaikonFormatter_Point2() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("test1", "test2")); // non-simple formatter
 Event previousEvent = new Event("test1", false, Arrays.asList(new EventAttribute("arg0", "java.lang.String", "arg1")));
 Event event = new Event("test2", true, Collections.emptyList());
 ProgramRun run = new ProgramRun();
 run.add(previousEvent);
 run.add(event);
 String observations = formatter.observations(run);
 Set<String> expectedDeclarations = new HashSet<String>(Arrays.asList("test1:::ENTER", "test1:::EXIT1", "test2:::EXIT1"));
 Set<String> declarations = new HashSet<String>();
 String[] lines = observations.split("\n");
 for (String line : lines) {
 if (line.startsWith("ppt ")) {
 declarations.add(line.substring(4).split(" ")[0]);
 }
 }
 assertEquals(expectedDeclarations, declarations);
 }

 @Test
 public void testDaikonFormatter_Classes() throws IOException, Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, false); // simple formatter
 assertEquals(Collections.emptyList(), formatter.inputs());

 formatter = new DaikonFormatter(TEST_FILE, false); // simple formatter
 assertEquals(Collections.singletonList(getClassForProperty("InputInterface0")), formatter.inputs());

 formatter = new DaikonFormatter(TEST_FILE, false); // simple formatter
 assertEquals(Arrays.asList(getClassForProperty("InputInterface0"), getClassForProperty("InputInterface1"), getClassForProperty("InputInterface2")), formatter.inputs());
 }

 private static Class getClassForProperty(String property) throws Exception {
 return Class.forName(InterfaceParser.classNameForProperty(property));
 }
}
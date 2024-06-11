package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventAttributeType;
import fi.vtt.noen.testgen.observations.data.EventType;
import fi.vtt.noen.testgen.parser.data.InterfaceDefinition;

public class DaikonFormatterTest {

 private static final String TESTGEN_PROPERTIES = "testgen.properties";
 private static final String INPUT_INTERFACES = "InputInterface";
 private static final String OUTPUT_INTERFACES = "OutputInterface";
 private static final String DAKON_FORMATTER_FILE = "DaikonFormatter.java";
 private static final String DAKON_FORMATTER_SIMPLE_FILE = "DaikonFormatterSimple.java";

 @Test
 public void testDaikonFormatterConstruction() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, false);
 assertTrue(formatter instanceof DaikonFormatter);
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithInputsFailure() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, null);
 fail("Expected RuntimeException");
 }

 @Test
 public void testDaikonFormatterConstructionWithInputs() throws Exception {
 Collection<String> inputs = new ArrayList<>();
 inputs.add("Input1");
 inputs.add("Input2");
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, inputs);
 assertTrue(formatter instanceof DaikonFormatter);
 assertEquals(inputs, formatter.inputs);
 }

 @Test
 public void testDaikonFormatterClassesForMultipleProperties() throws Exception {
 Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 }

 @Test
 public void testDaikonFormatterClassesForMultiplePropertiesFailure() throws Exception {
 try {
 DaikonFormatter.classesForMultipleProperties("NonExistentPrefix");
 fail("Expected Exception");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testDaikonFormatterHeader() {
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, new HashSet<String>());
 String header = formatter.header();
 assertTrue(header.startsWith("decl-version 2.0"));
 }

 @Test
 public void testDaikonFormatterFooter() {
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, new HashSet<String>());
 String footer = formatter.footer();
 assertTrue(footer.isEmpty());
 }

 @Test
 public void testDaikonFormatterFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, new HashSet<String>());
 String fileNameExtension = formatter.fileNameExtension();
 assertEquals("dtrace", fileNameExtension);
 }

 @Test
 public void testObservationsSimple() throws Exception {
 // create test data
 ProgramRun programRun = createTestProgramRun();
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, false);
 // test
 String observations = formatter.observations(programRun);
 // check result
 assertTrue(observations.startsWith("decl-version 2.0"));
 // additional checks can be added here
 }

 @Test
 public void testObservationsComplex() throws Exception {
 // create test data
 ProgramRun programRun = createTestProgramRun();
 DaikonFormatter formatter = new DaikonFormatter(DAKON_FORMATTER_FILE, new HashSet<String>(Arrays.asList("Input1", "Input2")));
 // test
 String observations = formatter.observations(programRun);
 // check result
 assertTrue(observations.startsWith("decl-version 2.0"));
 // additional checks can be added here
 }

 private ProgramRun createTestProgramRun() throws Exception {
 Properties properties = new Properties();
 properties.load(new FileInputStream(TESTGEN_PROPERTIES));
 InterfaceDefinition inputDefinition = InterfaceParser.parse(new ByteArrayInputStream(properties.getProperty(INPUT_INTERFACES).getBytes()), true);
 InterfaceDefinition outputDefinition = InterfaceParser.parse(new ByteArrayInputStream(properties.getProperty(OUTPUT_INTERFACES).getBytes()), true);
 List<Event> events = new ArrayList<>();
 Event event1 = new Event("Event1", EventType.ENTER, new HashMap<>());
 Event event2 = new Event("Event2", EventType.EXIT, new HashMap<>());
 Event event3 = new Event("Event3", EventType.ENTER, new HashMap<>());
 events.add(event1);
 events.add(event2);
 events.add(event3);
 ProgramRun programRun = new ProgramRun(new ByteArrayInputStream(properties.getProperty("test.main.TestClass").getBytes()), events);
 return programRun;
 }

}
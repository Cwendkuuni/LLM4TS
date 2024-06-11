package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private List<Event> events;
 private ProgramRun programRun;

 @Before
 public void setup() throws Exception {
 mockInterfaceFiles();
 formatter = new DaikonFormatter("test.dtrace", false);
 programRun = new ProgramRun();
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndSimpleFlag() throws IOException {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", false);
 assertNull(daikonFormatter.inputs);
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndInputs() throws IOException {
 DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("event1", "event2"));
 assertNotNull(daikonFormatter.inputs);
 assertEquals(2, daikonFormatter.inputs.size());
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface");
 assertNotNull(clazz);
 }

 @Test(expected = NullPointerException.class)
 public void testClassForPropertyWithNullProperty() {
 DaikonFormatter.classForProperty(null);
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithEmptyPrefix() throws Exception {
 DaikonFormatter.classesForMultipleProperties("");
 }

 @Test
 public void testClassesForMultiplePropertiesWithPrefix() throws Exception {
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithInvalidPrefixFormat() throws Exception {
 DaikonFormatter.classesForMultipleProperties("InputInterface_");
 }

 @Test
 public void testHeader() {
 String header = formatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testFooter() {
 String footer = formatter.footer();
 assertEquals("", footer);
 }

 @Test
 public void testFileNameExtension() {
 String fileNameExtension = formatter.fileNameExtension();
 assertEquals("dtrace", fileNameExtension);
 }

 @Test
 public void testObservationsWithSimpleFlag() throws IOException {
 String observations = formatter.observations(programRun);
 assertEquals("decl-version 2.0\nvar-comparability implicit\nppt event1:::ENTER\nppt event1:::EXIT1\n", observations);
 }

 @Test
 public void testObservations() throws IOException {
 Event event1 = new Event("event1", "EnterMethod1", new ArrayList<>(), Arrays.asList("param1", "param2"), new HashMap<>());
 Event event2 = new Event("event2", "ExitMethod2", new ArrayList<>(), Arrays.asList("result1", "result2"), new HashMap<>());
 event1.addAttribute(new EventAttribute("param1", "java.lang.Integer", "10"));
 event1.addAttribute(new EventAttribute("param2", "java.lang.String", "some string value"));
 event2.addAttribute(new EventAttribute("result1", "java.lang.String", "result value 1"));
 event2.addAttribute(new EventAttribute("result2", "java.lang.Integer", "20"));
 programRun.addEvent(event1);
 programRun.addEvent(event2);
 String observations = formatter.observations(programRun);
 String expectedObservations = "decl-version 2.0\nvar-comparability implicit\nppt event1:::ENTER\n  variable param1\n    var-kind variable\n    dec-type java.lang.Integer\n    rep-type java.lang.Integer\n    comparability implicit\nppt event1:::EXIT1\n  variable param1\n    var-kind variable\n    dec-type java.lang.Integer\n    rep-type java.lang.Integer\n  variable param2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt event2:::ENTER\n  variable result1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability explicit\nppt event2:::EXIT1\n  variable result1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n  variable result2\n    var-kind variable\n    dec-type java.lang.Integer\n    rep-type java.lang.Integer\n    comparability explicit\n";
 assertEquals(expectedObservations, observations);
 }

 private void mockInterfaceFiles() throws IOException {
 Properties properties = new Properties();
 properties.load(new FileInputStream("testgen.properties"));
 properties.setProperty("InputInterface", "fi.vtt.testgen.test.input.InputInterface");
 properties.setProperty("OutputInterface", "fi.vtt.testgen.test.output.OutputInterface");
 FileInputStream inputStream = new FileInputStream("testgen.properties");
 InterfaceParser.loadInterfaceFiles(inputStream, new HashSet<>());
 }

}
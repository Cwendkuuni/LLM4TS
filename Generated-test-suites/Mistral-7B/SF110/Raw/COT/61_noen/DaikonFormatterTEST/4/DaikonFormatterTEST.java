package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.Event;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun run;

 @Before
 public void setup() throws Exception {
 // Initialize testgen.properties with input and output interfaces
 Properties props = new Properties();
 props.setProperty("InputInterface", "fi.test.input.InputInterface");
 props.setProperty("OutputInterface", "fi.test.output.OutputInterface");
 File file = new File("testgen.properties");
 try (FileOutputStream fos = new FileOutputStream(file)) {
 props.store(fos, null);
 }

 // Initialize formatter with a test input file
 formatter = new DaikonFormatter("test.txt", Collections.emptyList());
 run = new ProgramRun();
 }

 @Test
 public void testDaikonFormatterConstructorWithEmptyInputs() throws Exception {
 // Test constructor with empty inputs
 DaikonFormatter formatterWithEmptyInputs = new DaikonFormatter("test.txt", false);
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatterWithEmptyInputs.header());
 assertEquals("", formatterWithEmptyInputs.footer());
 assertEquals("dtrace", formatterWithEmptyInputs.fileNameExtension());
 }

 @Test
 public void testDaikonFormatterConstructorWithInputs() throws Exception {
 // Test constructor with inputs
 DaikonFormatter formatterWithInputs = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatterWithInputs.header());
 assertEquals("", formatterWithInputs.footer());
 assertEquals("dtrace", formatterWithInputs.fileNameExtension());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterThrowsExceptionWhenLoadingInputInterfacesFails() throws Exception {
 // Test exception handling when loading input interfaces fails
 DaikonFormatter formatterWithFailedInputs = new DaikonFormatter("test.txt", Collections.emptyList());
 throw new IOException("Failed to load input interface definitions for daikon formatter");
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterThrowsExceptionWhenLoadingOutputInterfacesFails() throws Exception {
 // Test exception handling when loading output interfaces fails
 DaikonFormatter formatterWithFailedOutputs = new DaikonFormatter("test.txt", Collections.emptyList());
 throw new IOException("Failed to load output interface definitions for daikon formatter");
 }

 @Test
 public void testSimplePointMethod() throws Exception {
 // Test simplePoint method
 Event simpleEvent = new Event("method1", "ENTER", new HashMap<>());
 FormatterOutput expectedOutput = new FormatterOutput("decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit");
 formatter.simplePoint(simpleEvent, "implicit", new OutputBuffer());
 assertEquals(expectedOutput.toString(), formatter.getOutput().toString());
 }

 @Test
 public void testPointMethodWithTwoEventsEnter() throws Exception {
 // Test point method with two events, both ENTER
 Event event1 = new Event("method1", "ENTER", new HashMap<>());
 Event event2 = new Event("method2", "ENTER", new HashMap<>());
 FormatterOutput expectedOutput = new FormatterOutput("decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method2:::ENTER\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit");
 formatter.point("method1,method2", event1, "implicit", new OutputBuffer());
 assertEquals(expectedOutput.toString(), formatter.getOutput().toString());
 }

 @Test
 public void testPointMethodWithTwoEventsOneEnterOneExit() throws Exception {
 // Test point method with one ENTER and one EXIT
 Event event1 = new Event("method1", "ENTER", new HashMap<>());
 Event event2 = new Event("method2", "EXIT", new HashMap<>());
 FormatterOutput expectedOutput = new FormatterOutput("decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method1:::EXIT1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit");
 formatter.point("method1,method2", event1, "implicit", new OutputBuffer());
 assertEquals(expectedOutput.toString(), formatter.getOutput().toString());
 }

 @Test
 public void testPointMethodWithTwoEventsBothExit() throws Exception {
 // Test point method with two EXIT events
 Event event1 = new Event("method1", "EXIT", new HashMap<>());
 Event event2 = new Event("method2", "EXIT", new HashMap<>());
 FormatterOutput expectedOutput = new FormatterOutput("decl-version 2.0\nvar-comparability implicit\n");
 formatter.point("method1,method2", event1, "implicit", new OutputBuffer());
 assertEquals(expectedOutput.toString(), formatter.getOutput().toString());
 }

 @Test
 public void testPointMethodWithInputFile() throws Exception {
 // Test point method with input file
 File inputFile = new File("testInputFile.txt");
 InputStream inputStream = new FileInputStream(inputFile);
 InterfaceParser parser = new InterfaceParser(inputStream);
 Collection<String> inputs = parser.methodNames(parser.inputs());
 DaikonFormatter formatterWithFileInputs = new DaikonFormatter("test.txt", inputs);
 Event event1 = new Event("method1", "ENTER", new HashMap<>());
 Event event2 = new Event("method2", "EXIT", new HashMap<>());
 formatterWithFileInputs.point("method1,method2", event1, "implicit", new OutputBuffer());
 assertEquals("decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method2:::EXIT1\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit", formatterWithFileInputs.getOutput().toString());
 }

 @Test
 public void testObservationsMethod() throws Exception {
 // Test observations method
 ProgramRun runWithTwoEvents = new ProgramRun();
 runWithTwoEvents.addEvent(new Event("method1", "ENTER", new HashMap<>()));
 runWithTwoEvents.addEvent(new Event("method2", "EXIT", new HashMap<>()));
 FormatterOutput expectedOutput = new FormatterOutput("decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method2:::EXIT1\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit");
 formatter.observations(runWithTwoEvents);
 assertEquals(expectedOutput.toString(), formatter.getOutput().toString());
 }

 @Test
 public void testObservationsMethodWithSimplePointing() throws Exception {
 // Test observations method with simple pointing
 ProgramRun runWithSimplePointing = new ProgramRun();
 runWithSimplePointing.addEvent(new Event("method1", "ENTER", new HashMap<>()));
 runWithSimplePointing.addEvent(new Event("method1", "EXIT", new HashMap<>()));
 FormatterOutput expectedOutput = new FormatterOutput("decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method1:::EXIT1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit");
 formatter.observations(runWithSimplePointing);
 assertEquals(expectedOutput.toString(), formatter.getOutput().toString());
 }
}
package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

 private DaikonFormatter daikonFormatter;
 private Event event;
 private ProgramRun programRun;
 private OutputBuffer outputBuffer;

 @Before
 public void setup() throws IOException {
 event = new EventImpl("eventName");
 programRun = new ProgramRunImpl();
 outputBuffer = new OutputBuffer();
 }

 @Test
 public void testConstructorWithFileNameAndSimpleTrue() throws Exception {
 daikonFormatter = new DaikonFormatter("fileName.txt", true);
 assertNull(daikonFormatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithFileNameAndInputsNull() throws Exception {
 daikonFormatter = new DaikonFormatter("fileName.txt", null);
 }

 @Test
 public void testConstructorWithFileNameAndInputs() throws Exception {
 InputInterface inputInterface = mock(InputInterface.class);
 OutputInterface outputInterface = mock(OutputInterface.class);
 when(inputInterface.getName()).thenReturn("inputName");
 when(outputInterface.getName()).thenReturn("outputName");
 when(InterfaceParser.methodNames(inputInterface)).thenReturn(Collections.singletonList("inputName"));
 when(InterfaceParser.methodNames(outputInterface)).thenReturn(Collections.singletonList("outputName"));
 daikonFormatter = new DaikonFormatter("fileName.txt", Arrays.asList(inputInterface, outputInterface));
 assertEquals(Arrays.asList(inputInterface, outputInterface), daikonFormatter.getInputs());
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface");
 assertEquals(InputInterface.class, clazz);
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(1, classes.size());
 assertEquals(InputInterface.class, classes.get(0));
 }

 @Test
 public void testHeader() {
 String expected = "decl-version 2.0\nvar-comparability implicit";
 assertEquals(expected, daikonFormatter.header());
 }

 @Test
 public void testFooter() {
 assertEquals("", daikonFormatter.footer());
 }

 @Test
 public void testFileNameExtension() {
 assertEquals("dtrace", daikonFormatter.fileNameExtension());
 }

 @Test
 public void testObservations() throws IOException {
 when(programRun.iterator()).thenReturn(Arrays.asList(event, event).iterator());
 when(event.isExit()).thenReturn(false).thenReturn(true);
 when(event.getName()).thenReturn("eventName");
 daikonFormatter = new DaikonFormatter("fileName.txt", Arrays.asList(new InputInterfaceImpl(), new OutputInterfaceImpl()));
 String observations = daikonFormatter.observations(programRun);
 String expected = "";
 if (!daikonFormatter.isSimple()) {
 expected += "ppt eventName:::ENTER\n";
 expected += "ppt eventName:::EXIT1\n";
 expected += "ppt eventName:::ENTER\n";
 expected += "ppt eventName:::EXIT1\n";
 }
 else {
 expected += "ppt eventName:::ENTER\n";
 expected += "ppt eventName:::EXIT1\n";
 }
 assertEquals(expected, observations);
 }

 @Test
 public void testSimplePoint() {
 String expected = "";
 if (!event.isExit()) {
 expected += "ppt eventName:::ENTER\n";
 }
 else {
 expected += "ppt eventName:::EXIT1\n";
 }
 expected += "ppt eventName\n";
 daikonFormatter = new DaikonFormatter("fileName.txt", true);
 assertEquals(expected, daikonFormatter.simplePoint(event, "10", outputBuffer).toString());
 }

 @Test
 public void testPoint() {
 String expected = "";
 if (!event.isExit()) {
 expected += "ppt previousEventName:::ENTER\n";
 }
 else {
 expected += "ppt previousEventName:::EXIT1\n";
 }
 expected += "ppt previousEventName\n";
 expected += "ppt eventName:::ENTER\n";
 expected += "ppt eventName\n";
 daikonFormatter = new DaikonFormatter("fileName.txt", Arrays.asList(new InputInterfaceImpl(), new InputInterfaceImpl(), new OutputInterfaceImpl()));
 assertEquals(expected, daikonFormatter.point("previousEventName", event, "11", outputBuffer).toString());
 }

 @Test
 public void testDeclarations() throws IOException {
 Properties properties = new Properties();
 FileInputStream fileInputStream = new FileInputStream("testgen.properties");
 properties.load(fileInputStream);
 when(event.getName()).thenReturn("eventName");
 when(event.isExit()).thenReturn(false);
 when(event.attributes()).thenReturn(Collections.singletonList(new EventAttributeImpl("attributeName", "attributeValue", "java.lang.String")));
 when(InterfaceParser.methodNames(inputInterface)).thenReturn(Arrays.asList("inputName1", "inputName2"));
 when(InterfaceParser.methodNames(outputInterface)).thenReturn(Arrays.asList("outputName1", "outputName2"));
 when(InterfaceParser.methodNames(programRun.getInputs())).thenReturn(Arrays.asList("inputName3"));
 when(InterfaceParser.methodNames(programRun.getOutputs())).thenReturn(Arrays.asList("outputName3"));
 daikonFormatter = new DaikonFormatter("fileName.txt", Arrays.asList(new InputInterfaceImpl(), new OutputInterfaceImpl(), new ProgramRunImpl()));
 String observations = daikonFormatter.observations(programRun);
 String expected = "ppt eventName:::ENTER\n" +
 "ppt eventName\n" +
 "var-comparability implicit\n" +
 "ppt inputName1\n" +
 "ppt-type enter\n" +
 "variable attributeName\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt-type variable\n" +
 "ppt inputName2\n" +
 "ppt-type enter\n" +
 "variable attributeName\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt-type variable\n" +
 "ppt outputName1\n" +
 "ppt-type exit\n" +
 "ppt outputName2\n" +
 "ppt-type exit\n" +
 "ppt inputName3\n" +
 "ppt-type enter\n" +
 "variable attributeName\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt-type variable\n" +
 "ppt outputName3\n" +
 "ppt-type enter\n" +
 "variable attributeName\n" +
 "dec-type java.lang.String\n" +
 "rep-type java.lang.String\n" +
 "comparability implicit\n" +
 "ppt-type variable\n";
 assertEquals(expected, observations);
 }
}
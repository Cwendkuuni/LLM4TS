package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventAttributeType;
import fi.vtt.noen.testgen.observations.data.EventType;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;
 private ProgramRun run;
 private Event event;
 private Event previousEvent;

 @Before
 public void setUp() throws IOException {
 InputStream inputStream = new FileInputStream("testgen.properties");
 Properties properties = new Properties();
 properties.load(inputStream);
 when(InterfaceParser.methodNames(mock(InputStream.class))).thenReturn(Collections.singletonList("testMethod"));
 formatter = new DaikonFormatter("testFile.dtrace", null);
 run = mock(ProgramRun.class);
 event = mock(Event.class);
 previousEvent = mock(Event.class);
 }

 @Test
 public void testConstructorWithFileNameAndSimpleFlag() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("testFile.dtrace", true);
 assertNotNull(formatter.declarations);
 assertNull(formatter.inputs);
 }

 @Test
 public void testConstructorWithFileNameAndInputs() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("testFile.dtrace", InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 assertNotNull(formatter.declarations);
 assertNotNull(formatter.inputs);
 }

 @Test
 public void testClassForProperty() throws Exception {
 Class clazz = DaikonFormatter.classForProperty("InputInterface0");
 assertNotNull(clazz);
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyWithInvalidPropertyName() throws Exception {
 DaikonFormatter.classForProperty("InvalidPropertyName");
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(1, classes.size());
 }

 @Test
 public void testHeaderMethod() {
 String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
 String actualHeader = formatter.header();
 assertEquals(expectedHeader, actualHeader);
 }

 @Test
 public void testFooterMethod() {
 String actualFooter = formatter.footer();
 assertEquals("", actualFooter);
 }

 @Test
 public void testFileNameExtensionMethod() {
 String expectedFileNameExtension = "dtrace";
 String actualFileNameExtension = formatter.fileNameExtension();
 assertEquals(expectedFileNameExtension, actualFileNameExtension);
 }

 @Test
 public void testObservationsMethodWithSimplePoint() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.getName()).thenReturn("testMethod");
 when(previousEvent.isExit()).thenReturn(false);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod\nvariable testMethod\ncomparability implicit\n";
 assertEquals(expectedObservations, outputStream.toString());
 }

 @Test
 public void testObservationsMethodWithSimplePointAndPreviousEventExit() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.getName()).thenReturn("testMethod");
 when(previousEvent.isExit()).thenReturn(true);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod:::EXIT1\nvariable testMethod\ncomparability implicit\n";
 assertEquals(expectedObservations, outputStream.toString());
 }

 @Test
 public void testObservationsMethodWithPoint() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod1");
 when(previousEvent.getName()).thenReturn("testMethod2");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.isExit()).thenReturn(false);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod1:::ENTER\nvariable testMethod1\ncomparability implicit\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 outputStream.reset();
 formatter.observations(run);
 expectedObservations = "ppt testMethod2:::EXIT1\nvariable testMethod2\ncomparability implicit\n";
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithPointAndPreviousEventExit() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod1");
 when(previousEvent.getName()).thenReturn("testMethod2");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.isExit()).thenReturn(true);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod1:::ENTER\nvariable testMethod1\ncomparability implicit\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 outputStream.reset();
 formatter.observations(run);
 expectedObservations = "ppt testMethod2:::EXIT1\nvariable testMethod2\ncomparability implicit\n";
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithPointAndPreviousEventSameNameEnter() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod");
 when(previousEvent.getName()).thenReturn("testMethod");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.isExit()).thenReturn(false);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod:::ENTER\nvariable testMethod\ncomparability implicit\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithPointAndPreviousEventSameNameExit() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod");
 when(previousEvent.getName()).thenReturn("testMethod");
 when(event.isExit()).thenReturn(true);
 when(previousEvent.isExit()).thenReturn(false);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod:::EXIT1\nvariable testMethod\ncomparability implicit\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithPointAndPreviousEventSameNameBothExit() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod");
 when(previousEvent.getName()).thenReturn("testMethod");
 when(event.isExit()).thenReturn(true);
 when(previousEvent.isExit()).thenReturn(true);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithPointAndDifferentNames() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod1");
 when(previousEvent.getName()).thenReturn("testMethod2");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.isExit()).thenReturn(false);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod1:::ENTER\nvariable testMethod1\ncomparability implicit\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 outputStream.reset();
 formatter.observations(run);
 expectedObservations = "ppt testMethod2:::EXIT1\nvariable testMethod2\ncomparability implicit\n";
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithPointAndDifferentNamesAndPreviousEventExit() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(event.getName()).thenReturn("testMethod1");
 when(previousEvent.getName()).thenReturn("testMethod2");
 when(event.isExit()).thenReturn(false);
 when(previousEvent.isExit()).thenReturn(true);
 when(run.iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod1:::ENTER\nvariable testMethod1\ncomparability implicit\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 outputStream.reset();
 formatter.observations(run);
 expectedObservations = "ppt testMethod2:::EXIT1\nvariable testMethod2\ncomparability implicit\n";
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testPointMethodWithArrayAttribute() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Event eventWithArrayAttribute = mock(Event.class);
 when(eventWithArrayAttribute.getName()).thenReturn("testMethod");
 when(eventWithArrayAttribute.isExit()).thenReturn(false);
 EventAttribute arrayAttribute = mock(EventAttribute.class);
 when(arrayAttribute.getName()).thenReturn("arrayAttribute");
 when(arrayAttribute.getType()).thenReturn(EventAttributeType.ARRAY.toString());
 when(arrayAttribute.getValue()).thenReturn(new int[10]);
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().next()).thenReturn(arrayAttribute);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod\nvariable testMethod\ncomparability implicit\nvariable arrayAttribute[]\nvar-kind array\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testPointMethodWithNonArrayAttribute() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Event eventWithNonArrayAttribute = mock(Event.class);
 when(eventWithNonArrayAttribute.getName()).thenReturn("testMethod");
 when(eventWithNonArrayAttribute.isExit()).thenReturn(false);
 EventAttribute nonArrayAttribute = mock(EventAttribute.class);
 when(nonArrayAttribute.getName()).thenReturn("nonArrayAttribute");
 when(nonArrayAttribute.getType()).thenReturn(EventAttributeType.STRING.toString());
 when(nonArrayAttribute.getValue()).thenReturn("testValue");
 when(event.attributes().iterator().hasNext()).thenReturn(true).thenReturn(false);
 when(event.attributes().iterator().next()).thenReturn(nonArrayAttribute);
 formatter.observations(run);
 String expectedObservations = "ppt testMethod\nvariable testMethod\ncomparability implicit\nvariable nonArrayAttribute\n";
 String actualObservations = outputStream.toString();
 assertEquals(expectedObservations, actualObservations);
 }

}
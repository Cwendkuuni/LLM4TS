package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventAttributeType;
import fi.vtt.noen.testgen.observations.data.EventType;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.Comparability;

public class DaikonFormatterTest {

 private static final String TEST_FILE = "test.dtrace";

 @Test
 public void testDaikonFormatterConstructionWithSimpleBoolean() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 assertNull(formatter.getInputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithFileNameThrowsIOException() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructionWithInputsThrowsIOException() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Collections.singletonList("NonExistentInterface.class"));
 }

 @Test
 public void testDaikonFormatterConstructionWithFileNameAndInputs() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 assertNotNull(formatter.getInputs());
 }

 @Test
 public void testDaikonFormatterGetsCorrectInputNames() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 Set<String> expectedInputNames = new HashSet<>(Arrays.asList("method1", "method2", "method3"));
 Set<String> actualInputNames = formatter.getInputs();
 assertEquals(expectedInputNames, actualInputNames);
 }

 @Test
 public void testHeaderMethodReturnsValidHeaderString() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
 String actualHeader = formatter.header();
 assertEquals(expectedHeader, actualHeader);
 }

 @Test
 public void testFooterMethodReturnsEmptyString() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 String expectedFooter = "";
 String actualFooter = formatter.footer();
 assertEquals(expectedFooter, actualFooter);
 }

 @Test
 public void testFileNameExtensionMethodReturnsValidFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, null);
 String expectedFileNameExtension = "dtrace";
 String actualFileNameExtension = formatter.fileNameExtension();
 assertEquals(expectedFileNameExtension, actualFileNameExtension);
 }

 @Test
 public void testObservationsMethodWithSimpleBooleanReturnsValidOutputString() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 ProgramRun run = new ProgramRun(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(), Collections.emptyList(), Arrays.asList(new Event(EventType.ENTRY, "method1", new ArrayList<>()), new Event(EventType.EXIT, "method1")));
 String expectedObservations = "";
 String actualObservations = formatter.observations(run);
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithInputsWithCorrectEventProcessing() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 List<Event> events = Arrays.asList(new Event(EventType.ENTRY, "method1", new ArrayList<>()), new Event(EventType.EXIT, "method1"), new Event(EventType.ENTRY, "method2", new ArrayList<>()), new Event(EventType.EXIT, "method2"), new Event(EventType.ENTRY, "method3", new ArrayList<>()), new Event(EventType.EXIT, "method3"));
 ProgramRun run = new ProgramRun(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(), Collections.emptyList(), events);
 String expectedObservations = "ppt method1\nppt-type enter\nppt method1\nppt-type exit1\nppt method2\nppt-type enter\nppt method2\nppt-type exit1\nppt method3\nppt-type enter\nppt method3\nppt-type exit1\n";
 String actualObservations = formatter.observations(run);
 assertEquals(expectedObservations, actualObservations);
 }

 @Test
 public void testObservationsMethodWithInputsWithComplexEventProcessing() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 List<Event> events = Arrays.asList(new Event(EventType.ENTRY, "method1", new ArrayList<>()), new Event(EventType.EXIT, "method1"), new Event(EventType.ENTRY, "method2", new ArrayList<>()), new Event(EventType.EXIT, "method2"), new Event(EventType.ENTRY, "method3", new ArrayList<>()), new Event(EventType.EXIT, "method3"), new Event(EventType.ENTRY, "method1", new ArrayList<>()), new Event(EventType.EXIT, "method1"), new Event(EventType.ENTRY, "method2", new ArrayList<>()), new Event(EventType.EXIT, "method2"), new Event(EventType.ENTRY, "method3", new ArrayList<>()), new Event(EventType.EXIT, "method3"));
 ProgramRun run = new ProgramRun(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(), Collections.emptyList(), events);
 String expectedObservations = "ppt method1\nppt-type enter\nppt method1\nppt-type exit1\nppt method1\nppt-type enter\nppt method1\nppt-type exit1\nppt method2\nppt-type enter\nppt method2\nppt-type exit1\nppt method2\nppt-type enter\nppt method2\nppt-type exit1\nppt method3\nppt-type enter\nppt method3\nppt-type exit1\nppt method3\nppt-type enter\nppt method3\nppt-type exit1\n";
 String actualObservations = formatter.observations(run);
 assertEquals(expectedObservations, actualObservations);
 }

 @Test(expected = RuntimeException.class)
 public void testObservationsMethodWithInputsWithInvalidProgramRunThrowsRuntimeException() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 ProgramRun run = new ProgramRun(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(), Collections.emptyList(), Arrays.asList(new Event(EventType.ENTRY, "method1"), new Event(EventType.EXIT, "method1")));
 run.iterator().next().setName("NonExistentMethod");
 String actualObservations = formatter.observations(run);
 fail("Expected RuntimeException to be thrown");
 }

 @Test(expected = RuntimeException.class)
 public void testDeclarationsMethodWithInvalidEventThrowsRuntimeException() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 Event event = new Event(EventType.ENTRY, "NonExistentMethod", new ArrayList<>());
 String actualDeclarations = formatter.declarations(event, "NonExistentDeclaration", new OutputBuffer(), Comparability.IMPLICIT);
 fail("Expected RuntimeException to be thrown");
 }

 @Test
 public void testDeclarationsMethodWithValidEventAndDeclarationReturnsCorrectString() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 Event event = new Event(EventType.ENTRY, "method1", new ArrayList<>());
 OutputBuffer out = new OutputBuffer();
 String expectedDeclarations = "ppt method1\nppt-type enter\nvariable method1\nvar-kind variable\ndec-type java.lang.Object\nrep-type java.lang.Object\ncomparability implicit\n";
 String actualDeclarations = formatter.declarations(event, "method1", out, Comparability.IMPLICIT);
 assertEquals(expectedDeclarations, actualDeclarations);
 }

 @Test
 public void testAttributesMethodWithValidEventAndOutputBufferReturnsCorrectString() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, InterfaceParser.methodNames(new ByteArrayInputStream(new byte[0])));
 Event event = new Event(EventType.ENTRY, "method1", Arrays.asList(new EventAttribute("attribute1", EventAttributeType.STRING, "value1"), new EventAttribute("attribute2", EventAttributeType.INT, "value2")));
 OutputBuffer out = new OutputBuffer();
 String expectedAttributes = "attribute1\"value1\"1\nattribute21\n";
 String actualAttributes = formatter.attributes(event, out);
 assertEquals(expectedAttributes, actualAttributes);
 }
}
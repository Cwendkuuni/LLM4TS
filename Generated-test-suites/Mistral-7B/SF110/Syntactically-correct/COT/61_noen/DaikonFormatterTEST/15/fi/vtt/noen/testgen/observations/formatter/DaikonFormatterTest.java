package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.parser.exceptions.ParseException;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.Event;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.EventAttribute;

public class DaikonFormatterTest {

 private static final String TEST_GEN_PROPERTIES = "testgen.properties";

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndSimple() throws IOException, ParseException {
 DaikonFormatter formatter = new DaikonFormatter("input.txt", true);
 assertNull(formatter.getInputs());
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndInputs() throws IOException, ParseException {
 List<String> inputs = Arrays.asList("Method1", "Method2");
 DaikonFormatter formatter = new DaikonFormatter("input.txt", inputs);
 assertEquals(inputs, formatter.getInputs());
 }

 @Test
 public void testClassForPropertyReturnsNull() throws Exception {
 assertNull(DaikonFormatter.classForProperty("NonExistingProperty"));
 }

 @Test
 public void testClassesForMultiplePropertiesReturnsEmptyList() throws Exception {
 assertEmpty(DaikonFormatter.classesForMultipleProperties("NonExistingPrefix"));
 }

 @Test
 public void testClassesForMultiplePropertiesReturnsListWithClasses() throws Exception {
 List<Class> expectedClasses = Arrays.asList(ClassA.class, ClassB.class, ClassC.class);
 List<Class> classes = DaikonFormatter.classesForMultipleProperties("Prefix");
 assertEquals(expectedClasses, classes);
 }

 @Test
 public void testHeaderMethodReturnsExpectedDaikonDeclaration() throws IOException {
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"));
 assertEquals(formatter.header(), "decl-version 2.0\nvar-comparability implicit\n");
 }

 @Test
 public void testFooterMethodReturnsEmptyString() {
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"));
 assertEquals(formatter.footer(), "");
 }

 @Test
 public void testFileNameExtensionMethodReturnsExpectedString() {
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"));
 assertEquals(formatter.fileNameExtension(), "dtrace");
 }

 @Test
 public void testObservationsMethodWhenSimpleModeReturnsExpectedString() throws IOException, ParseException {
 ProgramRun run = new ProgramRun();
 Event event1 = new Event("Method1:::ENTER", "Method1", null, new ProgramCounter(1), null);
 run.addEvent(event1);
 Event event2 = new Event("Method2:::EXIT", "Method2", null, new ProgramCounter(3), null);
 run.addEvent(event2);
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"), run);
 String expectedOutput = "ppt Method1\n" +
                        "ppt-type enter\n" +
                        "ppt Method2\n" +
                        "ppt-type exit\n";
 assertEquals(formatter.observations(run), expectedOutput);
 }

 @Test
 public void testObservationsMethodWhenComplexModeReturnsExpectedString() throws IOException, ParseException {
 ProgramRun run = new ProgramRun();
 Event event1 = new Event("Method1:::ENTER", "Method1", null, new ProgramCounter(1), null);
 run.addEvent(event1);
 Event event2 = new Event("Method2:::ENTER", "Method2", null, new ProgramCounter(2), null);
 run.addEvent(event2);
 Event event3 = new Event("Method1:::EXIT", "Method1", null, new ProgramCounter(3), null);
 run.addEvent(event3);
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"), run);
 String expectedOutput = "ppt Method1\n" +
                        "ppt-type enter\n" +
                        "ppt Method1:::ENTER\n" +
                        "ppt-type enter\n" +
                        "ppt Method2:::ENTER\n" +
                        "ppt Method1:::EXIT\n" +
                        "ppt-type exit\n" +
                        "ppt Method2:::EXIT\n";
 assertEquals(formatter.observations(run), expectedOutput);
 }

 @Test
 public void testObservationsMethodWhenEmptyRunReturnsEmptyString() throws IOException, ParseException {
 ProgramRun run = new ProgramRun();
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"), run);
 assertEquals(formatter.observations(run), "");
 }

 @Test(expected = RuntimeException.class)
 public void testObservationsMethodThrowsRuntimeExceptionWhenInputsAreNotLoaded() throws IOException, ParseException {
 ProgramRun run = new ProgramRun();
 DaikonFormatter formatter = new DaikonFormatter("input.txt", Arrays.asList("Method1", "Method2"), run);
 }

 private void assertEquals(String expected, String actual) {
 assertEquals(expected, actual);
 }

 private void assertEquals(List<?> expected, List<?> actual) {
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 private void assertEmpty(List<?> list) {
 assertEquals(0, list.size());
 }
}
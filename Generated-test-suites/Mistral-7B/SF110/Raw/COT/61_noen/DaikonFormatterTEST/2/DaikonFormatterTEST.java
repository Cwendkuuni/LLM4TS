import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.model.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DaikonFormatterTest {

 private static final String TEST_FILE = "test.dtrace";

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndSimple() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 assertEquals(true, formatter.isSimple());
 assertNull(formatter.getInputs());
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndInputs() throws Exception {
 Set<String> inputs = new HashSet<>(Arrays.asList(new String[]{"method1", "method2"}));
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, inputs);
 assertFalse(formatter.isSimple());
 assertEquals(inputs, formatter.getInputs());
 }

 @Test
 public void testClassForProperty() throws Exception {
 assertEquals(String.class, DaikonFormatter.classForProperty("InputInterface_0"));
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyWithNullProperty() throws Exception {
 DaikonFormatter.classForProperty(null);
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithEmptyPrefix() throws Exception {
 assertNull(DaikonFormatter.classesForMultipleProperties(""));
 }

 @Test
 public void testClassesForMultiplePropertiesWithValidPrefix() throws Exception {
 Class<?> clazz1 = DaikonFormatter.classForProperty("InputInterface_0");
 Class<?> clazz2 = DaikonFormatter.classForProperty("InputInterface_1");
 Collection<Class<?>> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertTrue(classes.contains(clazz1));
 assertTrue(classes.contains(clazz2));
 }

 @Test(expected = Exception.class)
 public void testClassesForMultiplePropertiesWithInvalidPrefix() throws Exception {
 DaikonFormatter.classesForMultipleProperties("OutputInterface_INVALID");
 }

 @Test
 public void testHeader() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList(new String[]{"method1", "method2"}));
 String header = formatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testFooter() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList(new String[]{"method1", "method2"}));
 String footer = formatter.footer();
 assertEquals("", footer);
 }

 @Test
 public void testFileNameExtension() {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList(new String[]{"method1", "method2"}));
 String fileNameExtension = formatter.fileNameExtension();
 assertEquals("dtrace", fileNameExtension);
 }

 @Test
 public void testObservationsWithSimplePoint() throws Exception {
 Event event = new Event("method1", null, null, null, null, null, null, null, false, null);
 OutputBuffer out = new OutputBuffer();
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 formatter.simplePoint(event, "10", out);
 String observations = formatter.observations(null);
 assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", observations.substring(0, observations.lastIndexOf("\n") + 1));
 assertEquals("method1", out.toString());
 }

 @Test
 public void testObservationsWithPoint() throws Exception {
 Event previousEvent = new Event("method1", null, null, null, null, null, null, null, false, null);
 Event event = new Event("method2", null, null, null, null, null, null, null, false, null);
 OutputBuffer out = new OutputBuffer();
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList(new String[]{"method1", "method2"}));
 formatter.point(previousEvent.getName(), previousEvent, "22", out);
 formatter.point(event.getName(), event, "22", out);
 String observations = formatter.observations(null);
 assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", observations.substring(0, observations.lastIndexOf("\n") + 1));
 assertEquals("method1:::ENTERmethod2:::ENTER", out.toString());
 }

 @Test
 public void testDeclarations() throws Exception {
 Event event = new Event("method1", null, null, null, null, null, null, null, false, null);
 OutputBuffer out = new OutputBuffer();
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList(new String[]{"method1", "method2"}));
 formatter.declarations(event, "declaration1", out, "implicit");
 formatter.declarations(event, "declaration2", out, "implicit");
 String observations = formatter.observations(null);
 assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable declaration1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable declaration2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", observations.substring(0, observations.lastIndexOf("\n") + 1));
 assertEquals("method1:::ENTERdeclaration1declaration1declaration2method2:::ENTER", out.toString());
 }

 @Test
 public void testAttributes() throws Exception {
 Event event = new Event("method1", null, null, null, null, null, null, null, false, null);
 event.addAttribute(new ArrayAttribute("attr1", new Object[]{"value1", "value2"}));
 OutputBuffer out = new OutputBuffer();
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, Arrays.asList(new String[]{"method1", "method2"}));
 formatter.attributes(event, out);
 String observations = formatter.observations(null);
 assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nattr1\n  value1\n1\nattr1\n  value2\n1\n", observations.substring(0, observations.lastIndexOf("\n") + 1));
 assertEquals("method1:::ENTERattr1[value1, value2]attr1[value1, value2]", out.toString());
 }

}
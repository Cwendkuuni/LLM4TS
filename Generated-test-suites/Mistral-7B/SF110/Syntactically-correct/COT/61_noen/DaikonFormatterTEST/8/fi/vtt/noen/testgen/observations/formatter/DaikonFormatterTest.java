import org.junit.Test;
import static org.junit.Assert.assertEquals;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class DaikonFormatterTest {

 private DaikonFormatter formatter;

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndSimple() {
 try {
 formatter = new DaikonFormatter("test.txt", true);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to create DaikonFormatter with simple parameter");
 }
 assertEquals(null, formatter.getInputs());
 }

 @Test
 public void testDaikonFormatterConstructorWithFileNameAndInputs() {
 try {
 formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to create DaikonFormatter with input parameter");
 }
 assertEquals(Arrays.asList("method1", "method2"), formatter.getInputs());
 }

 @Test
 public void testClassForPropertySuccess() {
 try {
 final Class clazz = DaikonFormatter.classForProperty("InputInterface_0");
 assertEquals(String.class, clazz.getSuperclass());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to load class for property InputInterface_0");
 }
 }

 @Test(expected = NullPointerException.class)
 public void testClassForPropertyNullProperty() {
 DaikonFormatter.classForProperty(null);
 }

 @Test
 public void testClassesForMultiplePropertiesSuccess() {
 try {
 final List<Class> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
 assertEquals(2, classes.size());
 assertEquals(String.class, classes.get(0).getSuperclass());
 assertEquals(List.class, classes.get(1).getSuperclass());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to load classes for multiple properties InputInterface");
 }
 }

 @Test
 public void testHeader() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final String header = formatter.header();
 assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
 }

 @Test
 public void testFooter() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final String footer = formatter.footer();
 assertEquals("", footer);
 }

 @Test
 public void testFileNameExtension() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final String extension = formatter.fileNameExtension();
 assertEquals("dtrace", extension);
 }

 @Test
 public void testObservationsNormalCase() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final List<Event> events = new ArrayList<>();
 events.add(new Event("method1", "ENTER", new HashMap<>()));
 events.add(new Event("method2", "EXIT", new HashMap<>()));
 final Iterator<Event> iterator = events.iterator();
 when(input.iterator()).thenReturn(iterator);
 final ProgramRun run = new ProgramRun("test.program", events);
 final String observations = formatter.observations(run);
 assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::ENTER\nppt method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method2:::EXIT1\n", observations);
 }

 @Test
 public void testObservationsSimpleCase() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
 final List<Event> events = new ArrayList<>();
 final Event event = new Event("method1", "EXIT", new HashMap<>());
 when(input.iterator()).thenReturn(Collections.singletonList(event).iterator());
 final ProgramRun run = new ProgramRun("test.program", events);
 final String observations = formatter.observations(run);
 assertEquals("ppt method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n  variable method1:::EXIT1\n", observations);
 }

 @Test(expected = RuntimeException.class)
 public void testObservationsException() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final List<Event> events = new ArrayList<>();
 final Event event = new Event("method1", "ENTER", new HashMap<>());
 when(input.iterator()).thenReturn(Collections.singletonList(event).iterator());
 doThrow(new RuntimeException("test exception")).when(formatter).point(anyString(), any(Event.class), anyString(), any(OutputBuffer.class));
 final ProgramRun run = new ProgramRun("test.program", events);
 formatter.observations(run);
 }

 @Test
 public void testDeclarations() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final Event event1 = new Event("method1", "ENTER", new HashMap<>());
 final Event event2 = new Event("method2", "EXIT", new HashMap<>());
 final String name1 = "method1:::ENTER";
 final String name2 = "method2:::EXIT1";
 final OutputBuffer out = new OutputBuffer();
 formatter.declarations(event1, name1, out, "implicit");
 formatter.declarations(event2, name2, out, "implicit");
 final String declarations = out.toString();
 assertEquals("ppt method1\nppt-type enter\nvariable method1\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nvariable method1:::ENTER\nppt method2\nppt-type exit\nvariable method2\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", declarations);
 }

 @Test
 public void testAttributes() {
 final DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
 final Event event = new Event("method1", "ENTER", new HashMap<>());
 final OutputBuffer out = new OutputBuffer();
 final EventAttribute attribute1 = new EventAttribute("attr1", "StringType");
 final EventAttribute attribute2 = new ArrayAttribute("attr2", "int[]");
 event.addAttribute(attribute1);
 event.addAttribute(attribute2);
 formatter.attributes(event, out);
 final String attributes = out.toString();
 assertEquals("attr1\"StringType\"1\nattr2int[]1\n", attributes);
 }

}
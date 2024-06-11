package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.parser.exceptions.ParserException;
import fi.vtt.noen.testgen.parser.mocks.InterfaceParserMock;
import fi.vtt.noen.testgen.parser.mocks.PropertiesLoaderMock;
import fi.vtt.noen.testgen.parser.mocks.TestgenPropertiesMock;
import fi.vtt.noen.testgen.parser.mocks.TestgenPropertiesMock.TestgenProperty;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.Event;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter.EventAttribute;

@RunWith(MockitoJUnitRunner.class)
public class DaikonFormatterTest {

 private DaikonFormatter daikonFormatter;
 private InterfaceParserMock interfaceParserMock;
 private PropertiesLoaderMock propertiesLoaderMock;
 private TestgenPropertiesMock testgenPropertiesMock;

 @Before
 public void setUp() throws IOException, ParserException {
 interfaceParserMock = new InterfaceParserMock();
 propertiesLoaderMock = new PropertiesLoaderMock();
 testgenPropertiesMock = new TestgenPropertiesMock();

 testgenPropertiesMock.setProperties(new Properties() {{
 put(TestgenProperty.INPUT_INTERFACES, "InputInterface1,InputInterface2");
 put(TestgenProperty.OUTPUT_INTERFACES, "OutputInterface1,OutputInterface2");
 put(TestgenProperty.SIMPLE_MODE, "true");
}});

 propertiesLoaderMock.setTestgenProperties(testgenPropertiesMock);
 interfaceParserMock.setPropertiesLoader(propertiesLoaderMock);
 }

 @Test
 public void testDaikonFormatterConstructorSimpleMode() throws IOException {
 final String fileName = "test.txt";
 final DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, true);
 assertEquals("decl-version 2.0", daikonFormatter.header());
 assertEquals("", daikonFormatter.footer());
 assertNull(daikonFormatter.inputs());
 assertNull(daikonFormatter.outputs());
 }

 @Test
 public void testDaikonFormatterConstructorWithInputs() throws IOException, ParserException {
 final String fileName = "test.txt";
 final Collection<String> inputs = Arrays.asList("InputInterface1", "InputInterface2");
 final DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, inputs);
 assertEquals("decl-version 2.0", daikonFormatter.header());
 assertEquals("inputs:[InputInterface1, InputInterface2]", daikonFormatter.toString());
 assertSame(inputs, daikonFormatter.inputs);
 assertNull(daikonFormatter.outputs());
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterConstructorThrowsExceptionWhenFailedToLoadInputs() throws IOException, ParserException {
 final String fileName = "test.txt";
 final DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, null);
 fail("Expected a RuntimeException to be thrown");
 }

 @Test
 public void testClassForProperty() throws Exception {
 final String property = "InputInterface1";
 final Class<?> expectedClass = Mockito.mock(Class.class);
 final Class<?> actualClass = DaikonFormatter.classForProperty(property);
 assertEquals(expectedClass, actualClass);
 }

 @Test(expected = Exception.class)
 public void testClassForPropertyThrowsExceptionWhenPropertyNotFound() throws Exception {
 final String property = "NonExistentProperty";
 DaikonFormatter.classForProperty(property);
 fail("Expected an Exception to be thrown");
 }

 @Test
 public void testClassesForMultipleProperties() throws Exception {
 final String prefix = "InputInterface";
 final Collection<Class<?>> expectedClasses = Arrays.asList(Mockito.mock(Class.class), Mockito.mock(Class.class));
 final Collection<Class<?>> actualClasses = DaikonFormatter.classesForMultipleProperties(prefix);
 assertEquals(expectedClasses, actualClasses);
 }

 @Test
 public void testObservationsMethodSimpleMode() throws IOException, ParserException {
 final String fileName = "test.txt";
 final DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, true);
 final ProgramRun programRun = new ProgramRun();
 programRun.addEvent(new Event(EventType.ENTER, "Event1"));
 programRun.addEvent(new Event(EventType.EXIT, "Event1"));
 final String expectedOutput = "decl-version 2.0\n\nppt Event1\n  variable Event1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
 final String actualOutput = daikonFormatter.observations(programRun);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testObservationsMethodWithInputs() throws IOException, ParserException {
 final String fileName = "test.txt";
 final DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, Arrays.asList("InputInterface1", "InputInterface2"));
 final ProgramRun programRun = new ProgramRun();
 programRun.addEvent(new Event(EventType.ENTER, new EventData("Event1", "InputInterface1", new HashMap<String, Object>() {{
 put("Attribute1", "Value1");
 put("Attribute2", "Value2");
}})));
 programRun.addEvent(new Event(EventType.EXIT, new EventData("Event1", "InputInterface2", new HashMap<String, Object>() {{
 put("Attribute1", "Value1_EXIT");
 put("Attribute2", "Value2_EXIT");
}})));
 final String expectedOutput = "decl-version 2.0\n\n"
 + "ppt Event1_INPUTInterface1\n  variable Event1_INPUTInterface1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
 + "  variable Attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
 + "  variable Attribute2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n"
 + "ppt Event1_INPUTInterface2\n  variable Event1_INPUTInterface2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
 + "  variable Attribute1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n"
 + "  variable Attribute2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
 final String actualOutput = daikonFormatter.observations(programRun);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDeclarationsMethod() throws IOException, ParserException {
 final String eventName = "Event1";
 final Event event = new Event(EventType.ENTER, new EventData("Event1", "InputInterface1", new HashMap<String, Object>() {{
 put("Attribute1", "Value1");
 put("Attribute2", "Value2");
}}));
 final DaikonFormatter daikonFormatter = Mockito.spy(new DaikonFormatter("test.txt", Arrays.asList("InputInterface1", "InputInterface2")));
 Mockito.doReturn(new HashSet<String>() {{
 add("Event1");
 add("Event2");
 }}).when(daikonFormatter).declarations;
 final OutputBuffer out = Mockito.mock(OutputBuffer.class);
 final String comparability = "implicit";
 daikonFormatter.declarations(event, eventName, out, comparability);
 Mockito.verify(daikonFormatter.declarations).add(eventName);
 Mockito.verify(out, Mockito.times(1)).append("ppt " + eventName);
 Mockito.verify(out, Mockito.times(1)).append("ppt-type enter");
 Mockito.verify(out, Mockito.times(1)).append("var-comparability implicit");
 Mockito.verify(out, Mockito.times(2)).append("");
 Mockito.verify(out, Mockito.times(2)).append(eventName);
 Mockito.verify(out, Mockito.times(1)).append("variable Attribute1");
 Mockito.verify(out, Mockito.times(1)).append("variable Attribute2");
 Mockito.verify(out, Mockito.times(2)).append("dec-type java.lang.String");
 Mockito.verify(out, Mockito.times(2)).append("rep-type java.lang.String");
 Mockito.verify(out, Mockito.times(2)).append("comparability " + comparability);
 }

 @Test
 public void testAttributesMethod() throws IOException, ParserException {
 final String eventName = "Event1";
 final Event event = new Event(EventType.ENTER, new EventData("Event1", "InputInterface1", new HashMap<String, Object>() {{
 put("Attribute1", "Value1");
 put("Attribute2", "Value2");
}}));
 final DaikonFormatter daikonFormatter = Mockito.spy(new DaikonFormatter("test.txt", Arrays.asList("InputInterface1", "InputInterface2")));
 final OutputBuffer out = Mockito.mock(OutputBuffer.class);
 daikonFormatter.attributes(event, out);
 final Iterator<EventAttribute> ai = event.attributes().iterator();
 final EventAttribute attribute1 = ai.next();
 final EventAttribute attribute2 = ai.next();
 Mockito.verify(out, Mockito.times(2)).append(attribute1.getName());
 Mockito.verify(out, Mockito.times(2)).append(attribute2.getName());
 Mockito.verify(out, Mockito.times(2)).append("1");
 if (attribute1 instanceof ArrayAttribute) {
 Mockito.verify(out, Mockito.times(1)).append(attribute1.getName() + "[]");
 }
 else {
 Mockito.verify(out, Mockito.times(1)).append(attribute1.getName());
 }
 if (attribute2 instanceof ArrayAttribute) {
 Mockito.verify(out, Mockito.times(1)).append(attribute2.getName() + "[]");
 }
 else {
 Mockito.verify(out, Mockito.times(1)).append(attribute2.getName());
 }
 }

}
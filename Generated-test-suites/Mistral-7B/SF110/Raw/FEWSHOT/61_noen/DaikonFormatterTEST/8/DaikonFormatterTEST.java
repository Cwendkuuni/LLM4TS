package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.Const;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.parser.MethodName;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private ByteArrayOutputStream outContent;
    private OutputBuffer out;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        out = new OutputBuffer(outContent);
    }

    @Test
    public void testDaikonFormatterConstructorWithFileNameAndSimple() throws IOException {
        List<MethodName> inputMethodNames = Arrays.asList(new MethodName("testMethod1"), new MethodName("testMethod2"));
        InterfaceParser mockInterfaceParser = mock(InterfaceParser.class);
        when(mockInterfaceParser.methodNames(Mockito.any())).thenReturn(inputMethodNames);

        String fileName = "test.dtrace";
        boolean simple = true;
        DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, simple);
        assertEquals(fileName, daikonFormatter.getFileName());
        assertEquals(null, daikonFormatter.getInputs());
        assertEquals(simple, daikonFormatter.isSimple());

        verifyInterfaceParserCalls(mockInterfaceParser, fileName);
    }

    @Test
    public void testDaikonFormatterConstructorWithFileNameAndInputCollection() throws IOException {
        List<MethodName> inputMethodNames = Arrays.asList(new MethodName("testMethod1"), new MethodName("testMethod2"));
        InterfaceParser mockInterfaceParser = mock(InterfaceParser.class);
        when(mockInterfaceParser.methodNames(Mockito.any())).thenReturn(inputMethodNames);

        String fileName = "test.dtrace";
        Collection<String> inputs = Arrays.asList("Method1", "Method2");
        DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, inputs);
        assertEquals(fileName, daikonFormatter.getFileName());
        assertEquals(inputs, daikonFormatter.getInputs());
        assertFalse(daikonFormatter.isSimple());

        verifyInterfaceParserCalls(mockInterfaceParser, fileName);
    }

    @Test
    public void testHeaderMethod() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        String header = daikonFormatter.header();
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
        assertEquals(expectedHeader, header);
    }

    @Test
    public void testFooterMethod() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        String footer = daikonFormatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtensionMethod() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        String fileNameExtension = daikonFormatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testObservationsMethodWithSimple() throws IOException {
        DaikonFormatter daikonFormatter = createDaikonFormatterWithSimpleInput();
        ProgramRun mockRun = mockProgramRun();
        String observations = daikonFormatter.observations(mockRun);

        String expectedObservations = "";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testObservationsMethodWithComplex() throws IOException {
        DaikonFormatter daikonFormatter = createDaikonFormatterWithComplexInput();
        ProgramRun mockRun = mockProgramRun();
        String observations = daikonFormatter.observations(mockRun);

        String expectedObservations = "ppt Method1:::ENTER\nppt Method1:::EXIT1\n";
        assertEquals(expectedObservations, observations);
    }

    @Test
    public void testSimplePointMethod() throws IOException {
        Event event = createEvent("testMethod");
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        daikonFormatter.simplePoint(event, "implicit", out);

        String expectedOutput = "ppt testMethod:::ENTER\n";
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPointMethod() throws IOException {
        Event previousEvent = createEvent("testMethod1");
        Event event = createEvent("testMethod2");
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        daikonFormatter.point("testMethod1", previousEvent, "implicit", out);

        String expectedOutput = "ppt testMethod1:::ENTER\n";
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDeclarationsMethod() throws IOException {
        Event event = createEvent("testMethod");
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        String eventName = "testMethod";
        daikonFormatter.declarations(event, eventName, out, "implicit");

        String expectedOutput = "ppt " + eventName + "\nppt-type enter\n";
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAttributesMethod() throws IOException {
        Event event = createEvent("testMethod");
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", Arrays.asList("Method1", "Method2"));
        daikonFormatter.attributes(event, out);

        String expectedOutput = "testMethod\"1\n";
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    private DaikonFormatter createDaikonFormatterWithSimpleInput() throws IOException {
        List<MethodName> inputMethodNames = Arrays.asList(new MethodName("testMethod"));
        InterfaceParser mockInterfaceParser = mock(InterfaceParser.class);
        when(mockInterfaceParser.methodNames(Mockito.any())).thenReturn(inputMethodNames);

        String fileName = "test.dtrace";
        boolean simple = true;
        DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, simple);
        return daikonFormatter;
    }

    private DaikonFormatter createDaikonFormatterWithComplexInput() throws IOException {
        List<MethodName> inputMethodNames = Arrays.asList(new MethodName("testMethod1"), new MethodName("testMethod2"));
        InterfaceParser mockInterfaceParser = mock(InterfaceParser.class);
        when(mockInterfaceParser.methodNames(Mockito.any())).thenReturn(inputMethodNames);

        String fileName = "test.dtrace";
        Collection<String> inputs = Arrays.asList("Method1", "Method2");
        DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, inputs);
        return daikonFormatter;
    }

    private ProgramRun mockProgramRun() {
        List<Event> events = new ArrayList<>();
        Event event1 = createEvent("testMethod1");
        Event event2 = createEvent("testMethod2");
        events.add(event1);
        events.add(event2);
        ProgramRun programRun = new ProgramRun("TestClass", events);
        return programRun;
    }

    private Event createEvent(String methodName) {
        EventAttribute eventAttribute = new EventAttribute("testAttribute", "testValue");
        Event event = new Event(methodName, Arrays.asList(eventAttribute));
        return event;
    }

    private void verifyInterfaceParserCalls(InterfaceParser mockInterfaceParser, String fileName) throws IOException {
        when(mockInterfaceParser.methodNames(fileName)).thenReturn(Arrays.asList(new MethodName("testMethod1"), new MethodName("testMethod2")));
        mockInterfaceParser.methodNames(fileName);
        verify(mockInterfaceParser).methodNames(fileName);
    }
}
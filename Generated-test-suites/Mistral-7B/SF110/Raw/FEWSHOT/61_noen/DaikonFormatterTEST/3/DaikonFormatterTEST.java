package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private ByteArrayOutputStream outContent;
    private ProgramRun programRun;

    @Before
    public void setUp() {
        this.outContent = new ByteArrayOutputStream();
        this.formatter = new DaikonFormatter("test.dtrace", Collections.emptyList());
        this.formatter.setOutputStream(this.outContent);
        this.programRun = mock(ProgramRun.class);
    }

    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedHeader, this.formatter.header());
    }

    @Test
    public void testFooter() {
        assertEquals("", this.formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", this.formatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() throws IOException {
        Event event1 = mock(Event.class);
        when(event1.getName()).thenReturn("event1");
        when(event1.isExit()).thenReturn(false);

        Event event2 = mock(Event.class);
        when(event2.getName()).thenReturn("event2");
        when(event2.isExit()).thenReturn(false);

        when(this.programRun.iterator()).thenReturn(Arrays.asList(event1, event2).listIterator());

        this.formatter = new DaikonFormatter("test.dtrace", false);
        this.formatter.format(this.programRun);

        String expectedObservations = "ppt event1:::ENTER\n" +
                "ppt event1\n" +
                "ppt-type enter\n" +
                "event1\n";

        String actualObservations = new String(this.outContent.toByteArray());

        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testObservationsWithInputs() throws IOException {
        Event event1 = mock(Event.class);
        when(event1.getName()).thenReturn("input1");
        when(event1.isExit()).thenReturn(false);

        Event event2 = mock(Event.class);
        when(event2.getName()).thenReturn("output1");
        when(event2.isExit()).thenReturn(true);

        Event event3 = mock(Event.class);
        when(event3.getName()).thenReturn("output2");
        when(event3.isExit()).thenReturn(true);

        when(this.programRun.iterator()).thenReturn(Arrays.asList(event1, event2, event3).listIterator());

        this.formatter = new DaikonFormatter("test.dtrace", Arrays.asList("input1", "output1", "output2"));
        this.formatter.format(this.programRun);

        String expectedObservations = "ppt input1:::ENTER\n" +
                "ppt input1\n" +
                "ppt-type enter\n" +
                "input1\n" +
                "ppt output1:::ENTER\n" +
                "ppt output1:::EXIT1\n" +
                "ppt output1\n" +
                "ppt-type exit\n" +
                "output1\n" +
                "ppt output2:::ENTER\n" +
                "ppt output2:::EXIT1\n" +
                "ppt output2\n" +
                "ppt-type exit\n" +
                "output2\n";

        String actualObservations = new String(this.outContent.toByteArray());

        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testDeclarations() throws IOException {
        Event event = mock(Event.class);
        when(event.getName()).thenReturn("testEvent");
        when(event.isExit()).thenReturn(true);

        EventAttribute attribute1 = mock(EventAttribute.class);
        when(attribute1.getName()).thenReturn("attr1");
        when(attribute1.getType()).thenReturn("java.lang.String");
        when(attribute1.getValue()).thenReturn("value1");

        EventAttribute attribute2 = mock(EventAttribute.class);
        when(attribute2.getName()).thenReturn("attr2");
        when(attribute2.getType()).thenReturn("int");
        when(attribute2.getValue()).thenReturn(123);

        when(event.iterator()).thenReturn(Arrays.asList(attribute1, attribute2).listIterator());

        this.formatter = new DaikonFormatter("test.dtrace", Collections.emptyList());
        this.formatter.format(this.programRun);

        String expectedDeclarations = "ppt testEvent:::ENTER\n" +
                "ppt testEvent\n" +
                "ppt-type exit\n" +
                "testEvent\n" +
                "  variable attr1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability implicit\n" +
                "    \"value1\"1\n" +
                "  variable attr2\n" +
                "    var-kind variable\n" +
                "    dec-type int\n" +
                "    rep-type int\n" +
                "    comparability implicit\n" +
                "    1231\n";

        String actualDeclarations = new String(this.outContent.toByteArray());

        assertEquals(expectedDeclarations, actualDeclarations);
    }

    @Test
    public void testAttributes() throws IOException {
        Event event = mock(Event.class);
        when(event.getName()).thenReturn("testEvent");
        when(event.isExit()).thenReturn(true);

        EventAttribute attribute1 = mock(EventAttribute.class);
        when(attribute1.getName()).thenReturn("attr1");
        when(attribute1.getType()).thenReturn("java.lang.String");
        when(attribute1.getValue()).thenReturn("value1");

        EventAttribute attribute2 = mock(EventAttribute.class);
        when(attribute2.getName()).thenReturn("attr2");
        when(attribute2.getType()).thenReturn("int");
        when(attribute2.getValue()).thenReturn(123);

        when(event.iterator()).thenReturn(Arrays.asList(attribute1, attribute2).listIterator());

        this.formatter = new DaikonFormatter("test.dtrace", Collections.emptyList());
        this.formatter.format(this.programRun);

        String expectedAttributes = "testEventattr1\"value1\"1\n" +
                "testEventattr21231\n";

        String actualAttributes = new String(this.outContent.toByteArray()).substring(this.outContent.toString().indexOf("ppt testEvent") + "ppt testEvent".length());

        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("test.properties"));

        Set<Class> expectedClasses = new HashSet<Class>();
        expectedClasses.add(Class.forName("fi.vtt.noen.testgen.testInterface1.Interface1"));
        expectedClasses.add(Class.forName("fi.vtt.noen.testgen.testInterface2.Interface2"));

        Set<Class> actualClasses = DaikonFormatter.classesForMultipleProperties("TestInterface");

        assertEquals(expectedClasses, actualClasses);
    }

}
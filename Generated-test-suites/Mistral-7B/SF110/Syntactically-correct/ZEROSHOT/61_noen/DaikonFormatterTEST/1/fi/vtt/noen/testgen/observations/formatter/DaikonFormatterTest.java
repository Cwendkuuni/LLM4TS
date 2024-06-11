package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.Const;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;
    private ProgramRun programRun;
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream("testData.txt".getBytes());
        outputStream = new ByteArrayOutputStream();
        InterfaceParser interfaceParser = mock(InterfaceParser.class);
        when(interfaceParser.methodNames(Arrays.asList("method1", "method2"))).thenReturn(Arrays.asList("Method1", "Method2"));
        when(interfaceParser.methodNames(new FileInputStream("testInterface.txt"))).thenReturn(Arrays.asList("Method1_INPUT", "Method1_OUTPUT", "Method2_INPUT", "Method2_OUTPUT"));

        daikonFormatter = new DaikonFormatter("test.dtrace", false, interfaceParser);
        programRun = new ProgramRun(new ByteArrayInputStream("testData.txt".getBytes()), new File("testInterface.txt"), new ArrayList<>());
    }

    @Test
    public void testDaikonFormatterHeader() throws IOException {
        assertEquals(daikonFormatter.header(), "decl-version 2.0\nvar-comparability implicit\n");
    }

    @Test
    public void testDaikonFormatterFooter() throws IOException {
        assertEquals(daikonFormatter.footer(), "");
    }

    @Test
    public void testDaikonFormatterFileNameExtension() throws IOException {
        assertEquals(daikonFormatter.fileNameExtension(), "dtrace");
    }

    @Test
    public void testDaikonFormatterObservationsWithSimpleMode() throws IOException {
        ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
        daikonFormatter = new DaikonFormatter("test.dtrace", true);
        daikonFormatter.observations(programRun, newOutputStream);

        String expectedOutput = "Method1:::ENTER\nMethod1:::EXIT1\n";
        assertEquals(expectedOutput, newOutputStream.toString());
    }

    @Test
    public void testDaikonFormatterObservationsWithComplexMode() throws IOException {
        ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
        daikonFormatter.observations(programRun, newOutputStream);

        String expectedOutput = "decl-version 2.0\nvar-comparability implicit\n\n" +
                "ppt Method1_INPUT\n  variable Method1_INPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n\n" +
                "ppt Method1\n  variable Method1\n  var-kind variable\n  dec-type void\n  rep-type void\n  comparability implicit\n\n" +
                "ppt Method1_OUTPUT\n  variable Method1_OUTPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n\n" +
                "ppt Method2_INPUT\n  variable Method2_INPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n\n" +
                "ppt Method2\n  variable Method2\n  var-kind variable\n  dec-type void\n  rep-type void\n  comparability implicit\n\n" +
                "ppt Method2_OUTPUT\n  variable Method2_OUTPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n\n" +
                "Method1_INPUT:::ENTER\nMethod1:::ENTER\nMethod1:::EXIT1\nMethod1_OUTPUT:::ENTER\nMethod1:::EXIT1\nMethod1_OUTPUT:::EXIT1\n\n" +
                "Method2_INPUT:::ENTER\nMethod2:::ENTER\nMethod2:::EXIT1\nMethod2_OUTPUT:::ENTER\nMethod2:::EXIT1\nMethod2_OUTPUT:::EXIT1\n";

        assertEquals(expectedOutput, newOutputStream.toString());
    }

    @Test
    public void testDaikonFormatterPointMethodWithSimplePoint() throws IOException {
        ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
        Event simpleEvent = new Event("Method1", "simpleEvent", "INPUT", Arrays.asList(new EventAttribute("name", "SimpleName")));
        Event simpleEvent2 = new Event("Method1", "simpleEvent2", "OUTPUT", Arrays.asList(new EventAttribute("name", "SimpleName2")));

        daikonFormatter = new DaikonFormatter("test.dtrace", true);
        daikonFormatter.simplePoint(simpleEvent, "10", newOutputStream);

        String expectedOutput = "Method1:::ENTER\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.simplePoint(simpleEvent2, "10", newOutputStream);

        expectedOutput = "Method1:::EXIT1\n";
        assertEquals(expectedOutput, newOutputStream.toString());
    }

    @Test
    public void testDaikonFormatterPointMethodWithPoint() throws IOException {
        ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
        Event complexEvent = new Event("Method1", "complexEvent", "INPUT", Arrays.asList(new EventAttribute("name", "ComplexName"), new EventAttribute("array", new ArrayAttribute(10, "Integer"))));
        Event complexEvent2 = new Event("Method1", "complexEvent2", "OUTPUT", Arrays.asList(new EventAttribute("name", "ComplexName2")));

        daikonFormatter = new DaikonFormatter("test.dtrace", false);
        daikonFormatter.point(complexEvent.getName(), complexEvent, "22", newOutputStream);

        String expectedOutput = "Method1:::ENTER\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point(complexEvent2.getName(), complexEvent2, "22", newOutputStream);

        expectedOutput = "Method1:::EXIT1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point(complexEvent.getName(), complexEvent, "11", newOutputStream);

        expectedOutput = "Method1:::ENTER\nMethod1:::EXIT1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point(complexEvent2.getName(), complexEvent2, "11", newOutputStream);

        expectedOutput = "Method1:::EXIT1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point("Method1_INPUT", null, "22", newOutputStream);

        expectedOutput = "Method1_INPUT:::ENTER\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point("Method1_OUTPUT", null, "22", newOutputStream);

        expectedOutput = "Method1_OUTPUT:::ENTER\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point("Method1_INPUT", null, "11", newOutputStream);

        expectedOutput = "Method1_INPUT:::ENTER\nMethod1_INPUT:::EXIT1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.point("Method1_OUTPUT", null, "11", newOutputStream);

        expectedOutput = "Method1_OUTPUT:::ENTER\n";
        assertEquals(expectedOutput, newOutputStream.toString());
    }

    @Test
    public void testDaikonFormatterDeclarations() throws IOException {
        ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
        Event complexEvent = new Event("Method1", "complexEvent", "INPUT", Arrays.asList(new EventAttribute("name", "ComplexName"), new EventAttribute("array", new ArrayAttribute(10, "Integer"))));

        daikonFormatter = new DaikonFormatter("test.dtrace", false);
        daikonFormatter.declarations(complexEvent, complexEvent.getName(), newOutputStream, "implicit");

        String expectedOutput = "ppt Method1_INPUT\n  variable Method1_INPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.declarations(complexEvent, "Method1_OUTPUT", newOutputStream, "implicit");

        expectedOutput = "ppt Method1_OUTPUT\n  variable Method1_OUTPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.declarations(complexEvent, "Method1", newOutputStream, "implicit");

        expectedOutput = "ppt Method1\n  variable Method1\n  var-kind variable\n  dec-type void\n  rep-type void\n  comparability implicit\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.declarations(complexEvent, "Method1_INPUT", newOutputStream, "implicit");

        expectedOutput = "ppt Method1_INPUT\n  variable Method1_INPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.declarations(complexEvent, "Method1_OUTPUT", newOutputStream, "implicit");

        expectedOutput = "ppt Method1_OUTPUT\n  variable Method1_OUTPUT\n  var-kind variable\n  dec-type java.lang.String\n  rep-type java.lang.String\n  comparability implicit\n";
        assertEquals(expectedOutput, newOutputStream.toString());
    }

    @Test
    public void testDaikonFormatterAttributes() throws IOException {
        ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
        Event complexEvent = new Event("Method1", "complexEvent", "INPUT", Arrays.asList(new EventAttribute("name", "ComplexName"), new EventAttribute("array", new ArrayAttribute(10, "Integer"))));

        daikonFormatter = new DaikonFormatter("test.dtrace", false);
        daikonFormatter.attributes(complexEvent, newOutputStream);

        String expectedOutput = "ComplexName\"ComplexName\"1\nComplexName\"ComplexName\"1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.attributes(complexEvent, newOutputStream);

        expectedOutput = "ComplexName\"ComplexName\"1\nComplexName\"ComplexName\"1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        EventAttribute simpleAttribute = new EventAttribute("name", "SimpleName");
        daikonFormatter.attributes(simpleAttribute, newOutputStream);

        expectedOutput = "SimpleName\"SimpleName\"1\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        EventAttribute arrayAttribute = new ArrayAttribute(10, "Integer");
        daikonFormatter.attributes(arrayAttribute, newOutputStream);

        expectedOutput = "[I]10\n";
        assertEquals(expectedOutput, newOutputStream.toString());

        newOutputStream.reset();
        daikonFormatter.attributes(complexEvent, newOutputStream);

        expectedOutput = "ComplexName\"ComplexName\"1\nComplexName\"ComplexName\"1\n[I]10\n";
        assertEquals(expectedOutput, newOutputStream.toString());
    }
}
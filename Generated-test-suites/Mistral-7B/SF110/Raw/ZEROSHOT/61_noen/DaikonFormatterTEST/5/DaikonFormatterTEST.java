package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.*;
import fi.vtt.noen.testgen.observations.data.*;
import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.parser.exceptions.ParserException;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;
    private Parser parser;
    private ProgramRun programRun;

    @Before
    public void setup() throws IOException, ParserException {
        parser = mock(Parser.class);
        programRun = mock(ProgramRun.class);
        when(parser.parse("test.txt")).thenReturn(Arrays.asList(new Event("Event1"), new Event("Event2"), new Event("Event3")));
        when(programRun.iterator()).thenReturn(new Iterator<Event>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Event next() {
                return (Event) Arrays.asList(new Event("Event1"), new Event("Event2"), new Event("Event3")).get(index++);
            }
        });

        daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList("Event1", "Event2"));
    }

    @After
    public void tearDown() {
        daikonFormatter = null;
        parser = null;
        programRun = null;
    }

    @Test
    public void testHeader() {
        String expected = "decl-version 2.0\nvar-comparability implicit\n";
        String actual = daikonFormatter.header();
        assertEquals(expected, actual);
    }

    @Test
    public void testFooter() {
        String expected = "";
        String actual = daikonFormatter.footer();
        assertEquals(expected, actual);
    }

    @Test
    public void testFileNameExtension() {
        String expected = "dtrace";
        String actual = daikonFormatter.fileNameExtension();
        assertEquals(expected, actual);
    }

    @Test
    public void testObservationsSimple() {
        String expected = "ppt Event1:::ENTER\nEvent1\n\n"
                + "ppt Event2:::ENTER\nEvent2\n\n";
        String actual = daikonFormatter.observations(programRun);
        assertEquals(expected, actual);
    }

    @Test
    public void testObservationsWithInputs() {
        String expected = "ppt Event1:::ENTER\nEvent1\n\n"
                + "ppt Event1:::EXIT1\nEvent1\n\n"
                + "ppt Event1,Event2:::ENTER\nEvent1,Event2\n\n"
                + "ppt Event2:::ENTER\nEvent2\n\n";
        String actual = daikonFormatter.observations(programRun);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeclarations() throws IOException {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList("Event1", "Event2"));
        Set<String> declarations = daikonFormatter.getDeclarations();
        assertEquals(new HashSet<>(Arrays.asList("Event1", "Event2")), declarations);
    }
}
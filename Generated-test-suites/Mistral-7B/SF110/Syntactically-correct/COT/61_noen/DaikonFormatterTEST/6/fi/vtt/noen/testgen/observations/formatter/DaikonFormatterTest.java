package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private ProgramRun programRun;

    @Before
    public void setUp() throws Exception {
        formatter = new DaikonFormatter("filename.dtrace", false); // Simple constructor
        programRun = new ProgramRun();
        programRun.add(new Event("event1", "EventClass1", new ArrayList<>()));
        programRun.add(new Event("event2", "EventClass2", new ArrayList<>()));
    }

    @Test
    public void testDaikonFormatterConstructorWithFile() throws Exception {
        DaikonFormatter daikonFormatter = new DaikonFormatter("filename.dtrace", false);
        assertNotNull(daikonFormatter);
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterConstructorWithFileFailure() throws Exception {
        DaikonFormatter daikonFormatter = new DaikonFormatter("filename.dtrace", false); // Expect exception
    }

    @Test
    public void testDaikonFormatterConstructorWithInputs() throws Exception {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter daikonFormatter = new DaikonFormatter("filename.dtrace", inputs);
        assertNotNull(daikonFormatter);
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterConstructorWithInputsFailure() throws Exception {
        Collection<String> inputs = new ArrayList<>();
        inputs.add("input1");
        DaikonFormatter daikonFormatter = new DaikonFormatter("filename.dtrace", inputs); // Expect exception
    }

    @Test
    public void testInputs() throws Exception {
        Collection<Class> classes = DaikonFormatter.inputs();
        assertNotNull(classes);
        assertEquals(2, classes.size());
    }

    @Test
    public void testHeader() throws Exception {
        String header = formatter.header();
        assertNotNull(header);
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() throws Exception {
        String footer = formatter.footer();
        assertNull(footer);
    }

    @Test
    public void testFileNameExtension() throws Exception {
        String fileNameExtension = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);
    }

    @Test
    public void testObservationsBySimplePoint() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputBuffer out = new OutputBuffer(outputStream);
        formatter.simplePoint(new Event("event1", "EventClass1", new ArrayList<>()), "10", out);
        String observations = out.toString();
        assertEquals("ppt event1\n  variable event1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", observations);
    }

    @Test
    public void testObservationsByPoint() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputBuffer out = new OutputBuffer(outputStream);
        formatter.point(new Event("event1", "EventClass1", new ArrayList<>()), new Event("event2", "EventClass2", new ArrayList<>()), "11", out);
        String observations = out.toString();
        assertEquals("ppt event1\n  variable event1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\nppt event2\n  variable event2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n", observations);
    }

    @Test
    public void testObservationsWithMultipleInputs() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputBuffer out = new OutputBuffer(outputStream);
        formatter = new DaikonFormatter("filename.dtrace", Arrays.asList("input1", "input2"));
        formatter.observations(programRun);
        String observations = outputStream.toString();
        // Complex observations with multiple input events
    }
}
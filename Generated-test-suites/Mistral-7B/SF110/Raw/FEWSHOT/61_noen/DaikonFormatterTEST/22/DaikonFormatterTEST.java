package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    private static final String TEST_FILE = "DaikonFormatterTest.dtrace";
    private DaikonFormatter formatter;
    private Collection<String> inputs;

    @Before
    public void setUp() throws IOException {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream(new File("testgen.properties").getAbsolutePath()));
        System.setProperty("testgen.properties", "testgen-test.properties");
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testgen-test.properties");
            configuration.load(inputStream);
            inputStream.close();
        } finally {
            System.clearProperty("testgen.properties");
        }

        String fileName = "TestFormatter.dtrace";
        this.inputs = Arrays.asList("testMethod1", "testMethod2");
        formatter = new DaikonFormatter(fileName, this.inputs);
    }

    @Test
    public void testInput() throws IOException {
        assertEquals("inputs:[testMethod1, testMethod2]", formatter.getClass().getName() + ": inputs()");
    }

    @Test
    public void testOutput() throws IOException {
        Collection<String> outputs = DaikonFormatter.outputs();
        assertEquals("outputs:[OutputInterface1, OutputInterface2]", outputs.toString());
    }

    @Test
    public void testHeader() throws IOException {
        String header = formatter.header();
        Writer writer = new OutputStreamWriter(new FileOutputStream(TEST_FILE));
        writer.write(header);
        writer.close();
        File file = new File(TEST_FILE);
        String content = new String(new FileInputStream(file).readAllBytes());
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", content);
    }

    @Test
    public void testFooter() throws IOException {
        String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() throws IOException {
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() throws IOException {
        Event event1 = new Event("testMethod1", Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")), false);
        Event event2 = new Event("testMethod1", Arrays.asList(new EventAttribute("param2", "java.lang.Integer", "1")), true);
        ProgramRun programRun = new ProgramRun(Arrays.asList(event1, event2));
        String observations = formatter.observations(programRun);

        Writer writer = new OutputStreamWriter(new FileOutputStream(TEST_FILE, true));
        writer.append(observations);
        writer.close();
        File file = new File(TEST_FILE);
        String content = new String(new FileInputStream(file).readAllBytes());
        assertEquals("ppt testMethod1\n  variable param1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n", content);
        File.delete(TEST_FILE);
    }

    @Test
    public void testObservationsComplex() throws IOException {
        Event event1 = new Event("testMethod1", Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")), false);
        Event event2 = new Event("testMethod2", Arrays.asList(new EventAttribute("param2", "java.lang.Integer", "1"), new EventAttribute("param3", "java.lang.String", "value3")), false);
        Event event3 = new Event("testMethod1", Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")), true);
        ProgramRun programRun = new ProgramRun(Arrays.asList(event1, event2, event3));
        String observations = formatter.observations(programRun);

        Writer writer = new OutputStreamWriter(new FileOutputStream(TEST_FILE, true));
        writer.append(observations);
        writer.close();
        File file = new File(TEST_FILE);
        String content = new String(new FileInputStream(file).readAllBytes());
        String expectedContent = "ppt testMethod1\n  variable param1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
        expectedContent += "ppt testMethod2\n  variable param2\n    var-kind variable\n    dec-type java.lang.Integer\n    rep-type java.lang.Integer\n    comparability implicit\n  variable param3\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
        assertEquals(expectedContent, content);
        File.delete(TEST_FILE);
    }

    @Test
    public void testDeclarations() throws IOException {
        Event event1 = new Event("testMethod1", Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1")), false);
        Event event2 = new Event("testMethod1", Arrays.asList(new EventAttribute("param2", "java.lang.Integer", "1")), true);
        ProgramRun programRun = new ProgramRun(Arrays.asList(event1, event2));
        String observations = formatter.observations(programRun);

        Writer writer = new OutputStreamWriter(new FileOutputStream(TEST_FILE, true));
        writer.append(observations);
        writer.close();
        File file = new File(TEST_FILE);
        String content = new String(new FileInputStream(file).readAllBytes());
        Set<String> declarations = new HashSet<>(Arrays.asList("testMethod1:::ENTER", "testMethod1:::EXIT1"));
        Iterator<String> declarationIterator = declarations.iterator();
        String line;
        while ((line = content.split("\n")[0].trim()).startsWith("ppt ")) {
            String declarationName = line.substring(4).split(" ")[0];
            assertTrue(declarationIterator.hasNext() && declarationName.equals(declarationIterator.next()));
        }
        File.delete(TEST_FILE);
    }
}
package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        final String testFileName = "test.dtrace";
        final String inputFileName = "TestInput.java";
        final String outputFileName = "TestOutput.java";

        // Test with simple flag
        DaikonFormatter formatter = new DaikonFormatter(testFileName, true);
        assertNull(formatter.inputs);
        formatter.close();

        // Test with input file
        final File inputFile = new File(inputFileName);
        formatter = new DaikonFormatter(testFileName, inputFile);
        assertNotNull(formatter.inputs);
        assertEquals(2, formatter.inputs.size());
        formatter.close();

        // Test with property file
        final String configFileName = "testgen.properties";
        Properties props = new Properties();
        props.setProperty("InputInterface", "fi.vtt.noen.TestInput");
        props.setProperty("OutputInterface", "fi.vtt.noen.TestOutput");
        File configFile = new File(configFileName);
        FileOutputStream fos = new FileOutputStream(configFile);
        props.store(fos, null);
        fos.close();

        formatter = new DaikonFormatter(testFileName, null);
        assertNotNull(formatter.inputs);
        assertEquals(2, formatter.inputs.size());
        assertEquals("fi.vtt.noen.TestInput", formatter.inputs.toArray()[0].getName());
        assertEquals("fi.vtt.noen.TestOutput", formatter.inputs.toArray()[1].getName());
        formatter.close();

        // Test header, footer and fileNameExtension
        formatter = new DaikonFormatter(testFileName, null);
        String header = formatter.header();
        assertEquals("decl-version 2.0var-comparability implicit", header);
        String footer = formatter.footer();
        assertEquals("", footer);
        String extension = formatter.fileNameExtension();
        assertEquals("dtrace", extension);
        formatter.close();

        // Test observations method
        formatter = new DaikonFormatter(testFileName, null);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("method1", "ENTER");
        Event event2 = new Event("method2", "EXIT");
        Event event3 = new Event("method1", "EXIT");
        run.addEvent(event1);
        run.addEvent(event2);
        run.addEvent(event3);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        formatter.observations(run, os);
        String observations = os.toString();
        String expectedObservations = "ppt method1\n" +
                                      "  variable method1\n" +
                                      "    var-kind variable\n" +
                                      "    dec-type java.lang.String\n" +
                                      "    rep-type java.lang.String\n" +
                                      "    comparability implicit\n" +
                                      "ppt method2\n" +
                                      "  variable method2\n" +
                                      "    var-kind variable\n" +
                                      "    dec-type java.lang.String\n" +
                                      "    rep-type java.lang.String\n" +
                                      "    comparability implicit\n" +
                                      "ppt method1:::EXIT1\n" +
                                      "ppt-type exit\n" +
                                      "ppt method1:::ENTER\n" +
                                      "ppt method2:::ENTER\n" +
                                      "ppt method1:::EXIT2\n" +
                                      "ppt method1:::ENTER\n";
        assertEquals(expectedObservations, observations);
        formatter.close();

        // Test classesForMultipleProperties method
        List<Class> classes = new ArrayList<Class>();
        formatter = new DaikonFormatter(testFileName, null);
        try {
            classes = formatter.inputs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2, classes.size());
        assertEquals(TestInput.class, classes.get(0));
        assertEquals(TestOutput.class, classes.get(1));
        formatter.close();

        // Test declarations method
        formatter = new DaikonFormatter(testFileName, null);
        Event testEvent = new Event("testEvent", "ENTER");
        String declaration = "";
        formatter.declarations(testEvent, declaration, new OutputBuffer(), "implicit");
        assertEquals("ppt testEvent\n" +
                     "  variable testEvent\n" +
                     "    var-kind variable\n" +
                     "    dec-type java.lang.String\n" +
                     "    rep-type java.lang.String\n" +
                     "    comparability implicit\n", declaration);
        formatter.close();

        // Test point method
        formatter = new DaikonFormatter(testFileName, null);
        String pointDeclaration = "";
        Event previousEvent = new Event("previousEvent", "ENTER");
        Event currentEvent = new Event("currentEvent", "ENTER");
        formatter.point("previousEvent", previousEvent, "implicit", pointDeclaration);
        assertEquals("ppt previousEvent:::ENTER\n" +
                     "ppt-type enter\n" +
                     "ppt previousEvent:::ENTER\n", pointDeclaration);
        formatter.close();

        // Test attributes method
        formatter = new DaikonFormatter(testFileName, null);
        String attributesDeclaration = "";
        Event eventWithArrayAttribute = new Event("eventWithArrayAttribute", "ENTER");
        eventWithArrayAttribute.addAttribute(new ArrayAttribute("arrayAttribute", "java.lang.String[]", "value1, value2"));
        formatter.attributes(eventWithArrayAttribute, attributesDeclaration);
        assertEquals("eventWithArrayAttribute[]\n" +
                     "  variable arrayAttribute[]\n" +
                     "    var-kind array\n" +
                     "    dec-type java.lang.String[]\n" +
                     "    rep-type java.lang.String[]\n" +
                     "    comparability implicit\n" +
                     "value1\n" +
                     "value2\n1\n", attributesDeclaration);
        formatter.close();

        // Cleanup
        if (configFile.exists()) {
            File configFileDel = new File(configFileName);
            configFileDel.delete();
        }
        if (inputFile.exists()) {
            inputFile.delete();
        }
        if (new File(testFileName).exists()) {
            File fileDel = new File(testFileName);
            fileDel.delete();
        }
    }
}
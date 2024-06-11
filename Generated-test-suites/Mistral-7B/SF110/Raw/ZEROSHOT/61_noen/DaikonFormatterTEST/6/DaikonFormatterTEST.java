package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.data.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        // Test constructor with fileName and simple flag
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        // Test constructor with fileName and inputs
        List<String> inputs = Arrays.asList("method1", "method2");
        DaikonFormatter formatter2 = new DaikonFormatter("test.daikon", inputs);

        // Test header method
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
        String actualHeader = formatter.header();
        assertEquals(expectedHeader, actualHeader);

        // Test footer method
        String expectedFooter = "";
        String actualFooter = formatter.footer();
        assertEquals(expectedFooter, actualFooter);

        // Test fileNameExtension method
        String expectedFileNameExtension = "dtrace";
        String actualFileNameExtension = formatter.fileNameExtension();
        assertEquals(expectedFileNameExtension, actualFileNameExtension);

        // Test observations method with simple flag
        ProgramRun run = new ProgramRun();
        run.add(new Event("method1", "ENTER"));
        run.add(new Event("method2", "EXIT"));
        String expectedObservationsSimple = "ppt method1:::ENTER ppt-type enter ppt method1 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n"
                + "ppt method2:::EXIT ppt-type exit ppt method2 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n";
        String actualObservationsSimple = formatter.observations(run);
        assertEquals(expectedObservationsSimple, actualObservationsSimple);

        // Test observations method with inputs
        run = new ProgramRun();
        run.add(new Event("method1", "ENTER"));
        run.add(new Event("method1", "EXIT"));
        run.add(new Event("method2", "ENTER"));
        run.add(new Event("method2", "EXIT"));
        String expectedObservationsWithInputs = "ppt method1:::ENTER ppt-type enter ppt method1 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n"
                + "ppt method1:::EXIT ppt-type exit ppt method1 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n"
                + "ppt method2:::ENTER ppt-type enter ppt method2 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n"
                + "ppt method2:::EXIT ppt-type exit ppt method2 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n"
                + "ppt method1:::ENTER ppt-type enter ppt method1 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n";
        String actualObservationsWithInputs = formatter2.observations(run);
        assertEquals(expectedObservationsWithInputs, actualObservationsWithInputs);

        // Test point method with simple flag
        Event event = new Event("method1", "ENTER");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        formatter.point(event, "11", out);
        String expectedPointSimple = "ppt method1:::ENTER ppt-type enter\n";
        assertEquals(expectedPointSimple, out.toString());

        // Test point method with inputs
        event = new Event("method1", "EXIT");
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        formatter2.point("method1:::ENTER", event, "11", out2);
        String expectedPointWithInputs = "ppt method1:::ENTER ppt-type enter ppt method1 variable name1 var-kind variable dec-type java.lang.String comparability implicit\n";
        assertEquals(expectedPointWithInputs, out2.toString());

        // Test declarations method with simple flag
        event = new Event("method1", "ENTER");
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        formatter.declarations(event, "method1", out3, "implicit");
        String expectedDeclarationsSimple = "ppt method1 ppt-type enter\n";
        assertEquals(expectedDeclarationsSimple, out3.toString());

        // Test declarations method with inputs
        event = new Event("method1", "EXIT");
        ByteArrayOutputStream out4 = new ByteArrayOutputStream();
        formatter2.declarations(event, "method1", out4, "implicit");
        String expectedDeclarationsWithInputs = "ppt method1 ppt-type enter\n"
                + "  variable name1 var-kind variable dec-type java.lang.String comparability implicit\n";
        assertEquals(expectedDeclarationsWithInputs, out4.toString());

        // Test attributes method
        event = new Event("method1", "ENTER");
        event.addAttribute(new EventAttribute("name1", "value1"));
        ByteArrayOutputStream out5 = new ByteArrayOutputStream();
        formatter.attributes(event, out5);
        String expectedAttributesSimple = "name1value11\n";
        assertEquals(expectedAttributesSimple, out5.toString());

        // Test attributes method with inputs
        event.addAttribute(new EventAttribute("name1", "value1"));
        event.addAttribute(new EventAttribute("name2", "value2"));
        ByteArrayOutputStream out6 = new ByteArrayOutputStream();
        formatter2.attributes(event, out6);
        String expectedAttributesWithInputs = "name1value1 name2value21\n";
        assertEquals(expectedAttributesWithInputs, out6.toString());

        // Test classesForMultipleProperties method
        InputStream inputStream = new ByteArrayInputStream(("InputInterface1=com.test.InputInterface1\n" +
                "InputInterface2=com.test.InputInterface2").getBytes());
        Properties properties = new Properties();
        properties.load(inputStream);
        List<Class> expectedClasses = Arrays.asList(Class.forName(properties.getProperty("InputInterface1")),
                Class.forName(properties.getProperty("InputInterface2")));
        List<Class> actualClasses = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(expectedClasses, actualClasses);

        // Test inputs method
        InputStream inputStream2 = new ByteArrayInputStream(("InputInterface1=com.test.InputInterface1\n" +
                "InputInterface2=com.test.InputInterface2").getBytes());
        Properties properties2 = new Properties();
        properties2.load(inputStream2);
        InputStream inputStream3 = new ByteArrayInputStream(("OutputInterface1=com.test.OutputInterface1\n" +
                "OutputInterface2=com.test.OutputInterface2").getBytes());
        Properties properties3 = new Properties();
        properties3.load(inputStream3);
        List<Class> inputsExpected = Arrays.asList(Class.forName(properties2.getProperty("InputInterface1")),
                Class.forName(properties2.getProperty("InputInterface2")));
        List<String> inputsList = Arrays.asList("InputInterface1", "InputInterface2");
        DaikonFormatter formatter3 = new DaikonFormatter("test.daikon", inputsList);
        List<Class> actualInputs = formatter3.inputs();
        assertEquals(inputsExpected, actualInputs);

        // Test footer method with empty list of inputs
        String expectedEmptyFooter = "";
        String actualEmptyFooter = formatter.footer();
        assertEquals(expectedEmptyFooter, actualEmptyFooter);

        // Test footer method with non-empty list of inputs
        HashSet<String> inputsHashSet = new HashSet<>(inputsList);
        String expectedFooterWithInputs = "ppt-file test.daikon\nvar-version 2.0\n";
        for (Class clazz : inputsExpected) {
            expectedFooterWithInputs += "ppt-input " + clazz.getName() + "\n";
        }
        String actualFooterWithInputs = formatter2.footer();
        assertEquals(expectedFooterWithInputs, actualFooterWithInputs);
    }
}
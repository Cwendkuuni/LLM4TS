package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import fi.vtt.noen.testgen.parser.*;
import fi.vtt.noen.testgen.model.*;
import java.io.*;
import java.util.*;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        // Test constructor with fileName and simple parameter
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        assertNull(formatter.inputs);

        // Test constructor with fileName and inputs parameter
        Collection<String> inputNames = new ArrayList<>();
        inputNames.add("input1");
        inputNames.add("input2");
        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.dtrace", inputNames);
        assertEquals(inputNames, formatterWithInputs.inputs);

        // Test classForProperty method
        assertEquals(String.class, DaikonFormatter.classForProperty("InputInterface1"));

        // Test classesForMultipleProperties method
        Collection<Class> inputInterfaces = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, inputInterfaces.size());
        assertEquals(String[].class, inputInterfaces.get(0).getComponentType());
        assertEquals(Integer[].class, inputInterfaces.get(1).getComponentType());

        // Test header method
        DaikonFormatter formatter1 = new DaikonFormatter("test.dtrace", inputNames);
        String header = formatter1.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);

        // Test footer method
        String footer = formatter1.footer();
        assertEquals("", footer);

        // Test fileNameExtension method
        assertEquals("dtrace", formatter1.fileNameExtension());

        // Test observations method (simple case)
        ProgramRun run = new ProgramRun();
        Event event = new Event("input1", "enter", new HashMap<>());
        Event event2 = new Event("output1", "exit", new HashMap<>());
        run.addEvent(event);
        run.addEvent(event2);
        String observations = formatter1.observations(run);
        assertEquals("ppt input1:::ENTER\nppt output1:::EXIT1\n", observations);

        // Test observations method (complex case)
        DaikonFormatter formatter2 = new DaikonFormatter("test.dtrace", inputNames);
        observations = formatter2.observations(run);
        String expectedObservations = "decl-version 2.0\n"
                + "var-comparability implicit\n"
                + "ppt input1:::ENTER\n"
                + "ppt input1:::ENTER\n"
                + "ppt input1:::EXIT\n"
                + "ppt input2:::ENTER\n"
                + "ppt input2:::EXIT\n"
                + "ppt output1:::EXIT1\n"
                + "ppt input1:::EXIT\n"
                + "ppt output1:::EXIT2\n";
        assertEquals(expectedObservations, observations);
    }
}
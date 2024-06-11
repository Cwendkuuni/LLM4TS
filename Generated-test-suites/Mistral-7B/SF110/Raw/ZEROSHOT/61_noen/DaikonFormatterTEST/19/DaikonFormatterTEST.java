package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.parser.ParserFactory;
import fi.vtt.noen.testgen.testdata.ProgramRun;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws IOException {
        String fileName = "test.txt";
        boolean simple = false;

        // Test DaikonFormatter with simple flag false and input file
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        Collection<String> inputs = InterfaceParser.methodNames(new File("inputs.txt").BufferedReader());
        DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, inputs);

        ProgramRun programRun = createTestProgramRun();

        String expectedOutput = generateExpectedOutput("simpleOutput.dtrace", programRun);
        String actualOutput = daikonFormatter.observations(programRun);

        assertEquals(expectedOutput, actualOutput);

        // Test DaikonFormatter with simple flag false and no input file
        formatter = new DaikonFormatter(fileName, simple);
        daikonFormatter = new DaikonFormatter(fileName, null);

        programRun = createTestProgramRun();

        expectedOutput = generateExpectedOutput("noInputOutput.dtrace", programRun);
        actualOutput = daikonFormatter.observations(programRun);

        assertEquals(expectedOutput, actualOutput);

        // Test DaikonFormatter with simple flag true
        formatter = new DaikonFormatter(fileName, simple);

        programRun = createTestProgramRun();

        expectedOutput = generateExpectedOutput("simpleTrueOutput.dtrace", programRun);
        actualOutput = formatter.observations(programRun);

        assertEquals(expectedOutput, actualOutput);
    }

    private ProgramRun createTestProgramRun() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("method1", null, "ENTER", null, null, null, Arrays.asList(new EventAttribute("param1", "java.lang.String", "value1"))));
        events.add(new Event("method1", null, "EXIT", null, null, null, null));
        events.add(new Event("method2", null, "ENTER", null, null, null, null));
        events.add(new Event("method2", null, "EXIT", null, null, null, null));
        events.add(new Event("method3", null, "EXIT", null, null, null, null));
        events.add(new Event("method3", null, "ENTER", null, null, null, null));
        events.add(new Event("method1", null, "EXIT", null, null, null, null));

        return new ProgramRun(null, null, events);
    }

    private String generateExpectedOutput(String fileName, ProgramRun programRun) throws IOException {
        File file = new File(fileName);
        String expectedOutput = new String(Files.readAllBytes(file.toPath()));

        return expectedOutput;
    }
}
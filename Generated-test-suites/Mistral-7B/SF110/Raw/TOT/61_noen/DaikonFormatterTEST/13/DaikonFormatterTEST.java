// Your complete JUnit test file should start here

package fi.vtt.noen.testgen.observations.formatter;

import org.junit.*;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.model.*;
import static fi.vtt.noen.testgen.parser.InterfaceParser.methodNames;

public class DaikonFormatterTest {

    private DaikonFormatter formatter;
    private ProgramRun programRun;
    private Event event;

    @Before
    public void setUp() throws Exception {
        formatter = new DaikonFormatter("input.txt", false);
        programRun = new ProgramRun();
    }

    @Test
    public void testDaikonFormatterSimpleCase() {
        String expectedObservations = formatter.observations(programRun);
        String actualObservations = "ppt eventName:::ENTER\nppt eventName:::EXIT1\n";
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testDaikonFormatterComplexCase() {
        List<String> inputMethodNames = methodNames(DaikonFormatter.inputs());
        Event inputEvent = new Event(inputMethodNames.get(0), EventType.ENTER);
        programRun.add(inputEvent);

        List<String> outputMethodNames = methodNames(DaikonFormatter.outputs());
        Event outputEvent = new Event(outputMethodNames.get(0), EventType.EXIT);
        programRun.add(outputEvent);

        String expectedObservations = formatter.observations(programRun);
        String actualObservations = "ppt " + inputMethodNames.get(0) + ":::ENTER\nppt " + outputMethodNames.get(0) + ":::EXIT1\n";
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testDaikonFormatterWithNullInputs() {
        formatter = new DaikonFormatter("input.txt", null);
        try {
            formatter.observations(new ProgramRun());
            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            // Ignore
        }
    }

    @Test
    public void testDaikonFormatterWithEmptyInputs() {
        formatter = new DaikonFormatter("input.txt", Collections.emptyList());
        try {
            formatter.observations(new ProgramRun());
            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            // Ignore
        }
    }

    @Test
    public void testDaikonFormatterWithInvalidInputs() {
        formatter = new DaikonFormatter("input.txt", Arrays.asList("invalidMethodName"));
        try {
            formatter.observations(new ProgramRun());
            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            // Ignore
        }
    }

    @Test
    public void testDaikonFormatterWithMultipleInputs() {
        List<String> inputMethodNames = methodNames(DaikonFormatter.inputs());
        Event inputEvent1 = new Event(inputMethodNames.get(0), EventType.ENTER);
        event = new Event(inputMethodNames.get(1), EventType.ENTER);
        programRun.add(inputEvent1);
        programRun.add(event);

        List<String> outputMethodNames = methodNames(DaikonFormatter.outputs());
        Event outputEvent = new Event(outputMethodNames.get(0), EventType.EXIT);
        programRun.add(outputEvent);

        String expectedObservations = formatter.observations(programRun);
        String actualObservations = "ppt " + inputMethodNames.get(0) + ":::ENTER\nppt " + inputMethodNames.get(1) + ":::ENTER\nppt " + outputMethodNames.get(0) + ":::EXIT1\n";
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testDaikonFormatterWithMultipleOutputs() {
        List<String> inputMethodNames = methodNames(DaikonFormatter.inputs());
        Event inputEvent = new Event(inputMethodNames.get(0), EventType.ENTER);
        programRun.add(inputEvent);

        List<String> outputMethodNames = methodNames(DaikonFormatter.outputs());
        Event outputEvent1 = new Event(outputMethodNames.get(0), EventType.EXIT);
        Event outputEvent2 = new Event(outputMethodNames.get(1), EventType.EXIT);
        programRun.add(outputEvent1);
        programRun.add(outputEvent2);

        String expectedObservations = formatter.observations(programRun);
        String actualObservations = "ppt " + inputMethodNames.get(0) + ":::ENTER\nppt " + outputMethodNames.get(0) + ":::EXIT1\nppt " + outputMethodNames.get(1) + ":::EXIT1\n";
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testDaikonFormatterWithNoInputs() {
        formatter = new DaikonFormatter("input.txt", Collections.emptyList());
        String expectedObservations = "";
        String actualObservations = formatter.observations(new ProgramRun());
        assertEquals(expectedObservations, actualObservations);
    }

    @Test
    public void testDaikonFormatterWithNoOutputs() {
        formatter = new DaikonFormatter("input.txt", Arrays.asList("inputMethodName"));
        String expectedObservations = "ppt inputMethodName:::ENTER\n";
        String actualObservations = formatter.observations(new ProgramRun());
        assertEquals(expectedObservations, actualObservations);
    }
}
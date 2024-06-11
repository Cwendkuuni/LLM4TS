import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterWithSimpleInput() throws Exception {
        String fileName = "test-simple.txt";
        DaikonFormatter formatter = new DaikonFormatter(fileName, true);
        ProgramRun programRun = new ProgramRun();
        Event event1 = new Event("method1", EventType.CALL, new HashMap<>());
        Event event2 = new Event("method2", EventType.RETURN, new HashMap<>());
        programRun.addEvent(event1);
        programRun.addEvent(event2);

        String output = formatter.observations(programRun);
        String expectedOutput = "decl-version 2.0\n" +
                "var-comparability implicit\n" +
                "ppt method1:::ENTER\n" +
                "ppt-type enter\n" +
                "ppt method2:::EXIT1\n" +
                "ppt-type exit\n";

        assertEquals(expectedOutput, output);
    }

    // Add more test cases for various scenarios here
}
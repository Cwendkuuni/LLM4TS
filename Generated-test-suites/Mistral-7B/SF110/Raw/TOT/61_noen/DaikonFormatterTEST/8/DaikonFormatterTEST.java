import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.Event;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterSimple() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("input.txt", true);
            ProgramRun run = new ProgramRun();
            Event event1 = new Event("method1", "ENTRY");
            run.addEvent(event1);
            Event event2 = new Event("method2", "EXIT");
            run.addEvent(event2);
            String result = formatter.observations(run);
            String expectedResult = "decl-version 2.0\nvar-comparability implicit\n\nppt method1:::ENTER\n  variable method1\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\nppt method2:::EXIT1\n  variable method2\n    var-kind variable\n    dec-type java.lang.String\n    rep-type java.lang.String\n    comparability implicit\n\n";
            assertEquals(expectedResult, result);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to initialize DaikonFormatter.");
        }
    }
}
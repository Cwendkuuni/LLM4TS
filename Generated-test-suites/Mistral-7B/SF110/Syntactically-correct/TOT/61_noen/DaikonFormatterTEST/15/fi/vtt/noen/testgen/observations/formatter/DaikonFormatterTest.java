package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Assert.*;
import fi.vtt.noen.testgen.model.*;
import java.util.*;

public class DaikonFormatterTest {
    private DaikonFormatter formatter;
    private List<Event> simulatedRun;

    @Before
    public void setup() throws Exception {
        formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
        simulatedRun = Arrays.asList(
                new Event("method1", new Date(), true),
                new Event("method2", new Date(), true),
                new Event("method1", new Date(), false),
                new Event("method2", new Date(), false),
                new Event("method1", new Date(), true)
        );
    }

    @Test
    public void testDaikonFormatterConstructorWithFileName() {
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", formatter.header());
    }

    @Test
    public void testDaikonFormatterConstructorWithInputs() {
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:[method1, method2]\n", formatter.header());
    }

    @Test
    public void testDaikonFormatterObservations() {
        assertEquals("decl-version 2.0\nvar-comparability implicit\ninputs:[method1, method2]\n", formatter.header());
        assertEquals("ppt method1\n  ppt-type enter\n  variable method1\n    dec-type void\n    rep-type void\n    comparability implicit\n\n" +
                "ppt method1:::ENTER\n  variable method1\n    dec-type void\n    rep-type void\n    comparability implicit\n\n" +
                "ppt method2\n  ppt-type enter\n  variable method2\n    dec-type void\n    rep-type void\n    comparability implicit\n\n" +
                "ppt method1:::EXIT1\n  variable method1\n    dec-type void\n    rep-type void\n    comparability implicit\n\n" +
                "ppt method2:::ENTER\n  variable method2\n    dec-type void\n    rep-type void\n    comparability implicit\n\n" +
                "ppt method1\n  ppt-type enter\n  variable method1\n    dec-type void\n    rep-type void\n    comparability implicit\n", formatter.observations(new ProgramRun(simulatedRun)));
    }
}
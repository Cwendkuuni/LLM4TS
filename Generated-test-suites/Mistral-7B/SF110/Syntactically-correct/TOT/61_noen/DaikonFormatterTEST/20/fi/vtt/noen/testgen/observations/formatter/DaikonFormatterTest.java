package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.Event;
import fi.vtt.noen.testgen.parser.*;

@RunWith(JUnit4.class)
public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterWithSimpleFlag() throws IOException {
        String fileName = "test.dtrace";
        boolean simple = true;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        Assert.assertNotNull(formatter);
        Assert.assertNull(formatter.getInputs());
    }

    @Test
    public void testObservationsMethod() throws Exception {
        String fileName = "test.dtrace";
        DaikonFormatter formatter = new DaikonFormatter(fileName, null);
        ProgramRun run = new ProgramRun();
        Event event1 = new Event("main", "main", "ENTER");
        Event event2 = new Event("main", "print", "EXIT");
        run.addEvent(event1);
        run.addEvent(event2);
        String observations = formatter.observations(run);
        String expectedObservations = "decl-version 2.0\n" +
                "var-comparability implicit\n" +
                "ppt main:::ENTER\n" +
                "ppt-type enter\n" +
                "variable main\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n" +
                "ppt main:::EXIT1\n" +
                "ppt-type exit\n" +
                "variable print\n" +
                "dec-type java.lang.String\n" +
                "rep-type java.lang.String\n" +
                "comparability implicit\n";
        Assert.assertEquals(expectedObservations, observations);
    }

    @Test
    public void testInputsAndOutputsMethods() throws Exception {
        Collection<Class> inputs = DaikonFormatter.inputs();
        Collection<Class> outputs = DaikonFormatter.outputs();
        Assert.assertFalse(inputs.isEmpty());
        Assert.assertFalse(outputs.isEmpty());
        Assert.assertEquals("fi.vtt.noen.testgen.parser.InputInterface", inputs.iterator().next().getName());
        Assert.assertEquals("fi.vtt.noen.testgen.parser.OutputInterface", outputs.iterator().next().getName());
    }
}
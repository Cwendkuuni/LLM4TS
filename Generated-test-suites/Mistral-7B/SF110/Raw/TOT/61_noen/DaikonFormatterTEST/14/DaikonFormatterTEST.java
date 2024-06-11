import org.junit.Test;
import static org.junit.Assert.assertEquals;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter_Simple() throws Exception {
        // Given
        String fileName = "test.dtrace";
        boolean simple = true;
        DaikonFormatter daikonFormatter = new DaikonFormatter(fileName, simple);

        // When
        String result = daikonFormatter.observations(createProgramRun());

        // Then
        String expectedResult = "decl-version 2.0\n" +
                "var-comparability implicit\n" +
                "ppt Init:::ENTER ppt-type enter\n" +
                "ppt Init:::EXIT ppt-type exit\n" +
                "";
        assertEquals(expectedResult, result);
    }

    private ProgramRun createProgramRun() {
        ProgramRun programRun = new ProgramRun();
        Event initEvent = new Event("Init");
        programRun.addEvent(initEvent);
        return programRun;
    }
}
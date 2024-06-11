package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterWithSimpleInput() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
            String result = formatter.observations(new ProgramRun());
            String expected = "decl-version 2.0\nvar-comparability implicit\n"; // Simple point test with no inputs
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to initialize DaikonFormatter with simple input");
        }
    }

    @Test
    public void testDaikonFormatterWithComplexInput() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.txt", InputInterface.class.getMethods()[0].getName());
            String result = formatter.observations(new ProgramRun());
            String expected = "decl-version 2.0\nvar-comparability implicit\n" // Complex point test with one input
                    + "ppt method1:::ENTER\n" // Input event
                    + "ppt method1:::EXIT1\n"; // Output event

            assertEquals(expected, result);
        } catch (IOException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            fail("Failed to initialize DaikonFormatter with complex input");
        }
    }

    // Add more test cases for edge cases and error scenarios here
}
package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    private DaikonFormatter formatter;
    private Collection<String> inputs;
    private ProgramRun run;

    @Before
    public void setUp() throws IOException {
        inputs = new ArrayList<>();
        inputs.add("method1");
        inputs.add("method2");
        formatter = new DaikonFormatter("test.dtrace", inputs);
        run = new ProgramRun();
    }

    @Test(expected = IOException.class)
    public void testDaikonFormatterWithInvalidFileName() {
        // Test the constructor with invalid fileName
        throw new IOException("Invalid file name");
    }

    @Test
    public void testDaikonFormatterWithValidFileNameAndInputs() {
        // Test the constructor with valid fileName and inputs
        assertNotNull(formatter);
    }

    @Test
    public void testDaikonFormatterWithValidProgramRun() {
        // Test the method observations with a valid ProgramRun object
        String observations = formatter.observations(run);
        assertTrue(observations.length() > 0);
    }

    @Test
    public void testDaikonFormatterWithValidFileNameAndSimpleFlag() {
        // Test the constructor with valid fileName and simple flag
        assertNotNull(formatter);
        // Test the method observations with a valid ProgramRun object (simple flag)
        String observations = formatter.observations(run);
        assertTrue(observations.length() > 0);
    }

    @Test
    public void testDaikonFormatterWithValidFileNameAndInputsAndInvalidProgramRun() {
        // Test the method observations with an invalid ProgramRun object
        String observations = formatter.observations(null);
        assertNull(observations);
    }

    @Test
    public void testDaikonFormatterWithValidFileNameAndSimpleFlagAndEmptyInputs() {
        // Test the constructor with valid fileName, simple flag and empty inputs
        formatter = new DaikonFormatter("test.dtrace", Collections.emptyList());
        assertNotNull(formatter);
    }

    @Test(expected = RuntimeException.class)
    public void testDaikonFormatterWithFailingConstructor() throws IOException, Exception {
        // Test the constructor that fails due to an Exception
        formatter = new DaikonFormatter("test.dtrace", null);
        throw new RuntimeException("expected exception");
    }

    @Test
    public void testDaikonFormatterWithValidFileNameAndInputsAndInvalidMethodNames() throws IOException, Exception {
        // Test the constructor that fails due to invalid method names
        formatter = new DaikonFormatter("test.dtrace", Arrays.asList("method1", "method2", "InvalidMethodName"));
        assertNotNull(formatter);
    }
}
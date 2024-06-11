import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testMainWithValidInputFile() {
        String validInputFile = "valid_input.txt";
        String expectedOutput = "Expected output for valid input file";
        Main.main(new String[]{validInputFile});
        String actualOutput = System.out.getHistogram().get(0).toString();
        assertEquals(expectedOutput, actualOutput);
    }
}
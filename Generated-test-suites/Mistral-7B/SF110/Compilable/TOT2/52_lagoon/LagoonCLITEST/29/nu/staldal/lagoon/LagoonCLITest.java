package nu.staldal.lagoon;
import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {
    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArguments() {
        String[] args = new String[]{"property_file.xml", "invalid"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"property_file.xml", "1a"};
        LagoonCLI.main(args);
    }
}
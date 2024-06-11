import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LagoonCLITest {

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        // Check if the correct message is printed for valid arguments
        assertEquals("Lagoon initialized successfully\n", System.out.getText().substring(0, System.out.length()));
    }

    @Test(expected = Exception.class)
    public void testMainWithInvalidArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        // Check if the correct error message is printed for invalid arguments
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.err.getText().substring(0, System.err.length()));
    }
}
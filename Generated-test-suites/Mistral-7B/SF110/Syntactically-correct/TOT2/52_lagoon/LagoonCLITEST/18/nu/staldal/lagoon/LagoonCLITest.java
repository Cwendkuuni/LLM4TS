import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LagoonCLITest {
    @Test
    public void testMain() {
        String[] args = new String[]{"property_file"};
        LagoonCLI.main(args);
        // Check if the correct message is printed when an argument is missing
        assertFalse(LagoonCLI.DEBUG);
        assertFalse(LagoonCLI.build);
        assertFalse(LagoonCLI.force);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWBuf().toString());
    }
}
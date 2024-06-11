import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LagoonCLITest {

    @Test
    void testMain_ValidArguments() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        // Additional assertions for correct behavior after the method execution
    }

    @Test
    void testMain_InvalidSyntax() {
        String[] args = new String[]{"property_file.xml", "invalid"};
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }

    @Test
    void testMain_InvalidInterval() {
        String[] args = new String[]{"property_file.xml", "-5"};
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }

    @Test
    void testMain_MissingArguments() {
        String[] args = new String[0];
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }
}
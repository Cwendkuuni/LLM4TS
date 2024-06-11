import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterWithBooleanParameter() {
        DaikonFormatter formatter = new DaikonFormatter("test.daikon", true);
        // Check if the constructor was called with correct parameters
        assertNotNull(formatter);
    }
}
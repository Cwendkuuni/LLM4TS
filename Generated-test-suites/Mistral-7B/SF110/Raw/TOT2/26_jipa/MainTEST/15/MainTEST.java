import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class MainTest {
    @Test
    public void testMain() {
        Main.main(new String[]{"testFile.asm"});
        assertTrue(true); // Add your assertions here
    }
}
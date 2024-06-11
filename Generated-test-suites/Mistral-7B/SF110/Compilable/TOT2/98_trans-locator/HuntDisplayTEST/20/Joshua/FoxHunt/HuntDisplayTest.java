package Joshua.FoxHunt;
import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;

public class HuntDisplayTest {
    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }
}
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }
}
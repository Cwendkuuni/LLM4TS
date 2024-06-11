import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    @Test
    public void testConstructor() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        assertNotNull(returnsSmartNulls);
    }
}
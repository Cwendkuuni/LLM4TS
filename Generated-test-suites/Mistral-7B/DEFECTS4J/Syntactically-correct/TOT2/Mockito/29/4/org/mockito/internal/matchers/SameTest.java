import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.assertEquals;

public class SameTest {

    @Test
    public void testConstructor() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        assertEquals(wanted, same.getWanted());
    }

    // ...
}
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {
    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }
}
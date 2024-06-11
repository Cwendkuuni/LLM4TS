import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionImplTest {
    @Test
    public void testToString() {
        Option option = new OptionImpl(1, false);
        String expected = "<option id=\"1\">";
        String actual = option.toString();
        assertEquals(expected, actual);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupInt() {
        // Test valid axis numbers
        for (int i = 0; i <= 13; i++) {
            String actual = Axis.lookup(i);
            String expected = null; // Default value

            switch (i) {
                case 1: expected = "child"; break;
                case 2: expected = "descendant"; break;
                case 3: expected = "parent"; break;
                case 4: expected = "ancestor"; break;
                case 5: expected = "following-sibling"; break;
                case 6: expected = "preceding-sibling"; break;
                case 7: expected = "following"; break;
                case 8: expected = "preceding"; break;
                case 9: expected = "attribute"; break;
                case 10: expected = "namespace"; break;
                case 11: expected = "self"; break;
                case 12: expected = "descendant-or-self"; break;
                case 13: expected = "ancestor-or-self"; break;
            }

            assertEquals(expected, actual);
        }
    }
}
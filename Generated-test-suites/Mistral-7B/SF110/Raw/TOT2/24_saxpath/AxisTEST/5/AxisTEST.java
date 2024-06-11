import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNumber() {
        String expectedAxisNames = "child, descendant, parent, ancestor, following-sibling, preceding-sibling, following, preceding, attribute, namespace, self, descendant-or-self, ancestor-or-self";
        String[] actualAxisNames = new String[13];

        for (int i = 0; i < 13; i++) {
            actualAxisNames[i] = Axis.lookup(i);
        }

        assertEquals(expectedAxisNames, String.join(",", actualAxisNames));
    }
}
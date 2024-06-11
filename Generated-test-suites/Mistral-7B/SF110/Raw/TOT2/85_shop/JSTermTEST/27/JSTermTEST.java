import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testEmptyConstructor() {
        JSTerm jsterm = new JSTerm();
        assertInstanceOf(JSTerm.class, jsterm);
    }
}
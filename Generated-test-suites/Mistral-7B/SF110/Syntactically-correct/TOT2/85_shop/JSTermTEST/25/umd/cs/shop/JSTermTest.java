import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testJSTermConstructorNoArgs() {
        JSTerm jsterm = new JSTerm();
        assertNotNull(jsterm);
    }
}
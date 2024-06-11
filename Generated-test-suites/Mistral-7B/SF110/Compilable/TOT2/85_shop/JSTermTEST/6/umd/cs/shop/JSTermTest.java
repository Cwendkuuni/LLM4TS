package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {
    @Test
    public void testJSTermDefaultConstructor() {
        JSTerm jsterm = new JSTerm();
        assertNotNull(jsterm);
    }
}
package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

    @Test
    public void testJSTermConstructorNoArgs() {
        JSTerm term = new JSTerm();
        assertTrue(term instanceof JSTerm);
    }
}
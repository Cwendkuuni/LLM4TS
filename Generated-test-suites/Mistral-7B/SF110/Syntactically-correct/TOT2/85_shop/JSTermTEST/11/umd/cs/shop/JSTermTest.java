import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

    @Test
    public void testEmptyJSTermConstruction() {
        JSTerm jsterm = new JSTerm();
        assertTrue(jsterm.isEmpty());
    }
}
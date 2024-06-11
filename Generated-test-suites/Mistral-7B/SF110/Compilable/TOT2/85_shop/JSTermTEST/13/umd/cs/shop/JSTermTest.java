package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

    @Test
    public void testEmptyConstructor() {
        JSTerm jsterm = new JSTerm();
        assertFalse(jsterm.isEval());
    }

    // Test cases for other constructors and methods will go here
}
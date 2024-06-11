import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JSTermTest {

    @Test
    public void testEmptyConstructor() {
        JSTerm term = new JSTerm();
        assertEquals(0, term.size());
        assertEquals(false, term.isVariable);
        assertEquals(false, term.isConstant);
        assertEquals(false, term.isFunction);
        assertEquals(false, term.isEval);
    }
}
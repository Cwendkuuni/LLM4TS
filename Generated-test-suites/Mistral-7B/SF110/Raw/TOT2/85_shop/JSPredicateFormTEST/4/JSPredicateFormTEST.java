import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormInitWithValidString() {
        JSPredicateForm pf = new JSPredicateForm("(or (not (and a b)) (not (and c d)))");
        assertEquals("(or (not (and a b)) (not (and c d)))", pf.toStr().toString());
    }
}
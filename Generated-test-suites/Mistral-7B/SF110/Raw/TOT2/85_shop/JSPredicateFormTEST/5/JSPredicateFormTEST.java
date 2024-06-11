import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormWithValidText() {
        JSPredicateForm jspf = new JSPredicateForm("!(a > b) and (c = d)");
        assertEquals("!(a > b) and (c = d)", jspf.toString());
    }
}
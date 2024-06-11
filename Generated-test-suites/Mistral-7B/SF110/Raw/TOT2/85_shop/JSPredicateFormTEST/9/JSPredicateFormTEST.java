import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructorWithText() {
        String text = "not ( and ( b = c ) ( a = b ) )";
        JSPredicateForm jsp = new JSPredicateForm(text);
        // Basic functionality check
        assertEquals("[-not-[-and-[-b-=--c-]-[-and-[-a-=--b-]]]", jsp.toStr().toString());
    }
}
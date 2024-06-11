import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

    @Test
    public void testConstructorWithText() {
        String text = "!(a and b)";
        JSPredicateForm predicateForm = new JSPredicateForm(text);

        // Basic functionality test
        assertEquals("!(a and b)", predicateForm.toStr().toString());

        // Edge cases and error scenarios
        String invalidText = "(!)";
        try {
            JSPredicateForm invalidPredicateForm = new JSPredicateForm(invalidText);
            fail("Expected JSParserError but no error was thrown.");
        } catch (JSParserError e) {
            // Expected error, no action needed
        }

        String missingLeftParenthesisText = "a and b";
        try {
            JSPredicateForm missingLeftParenthesisPredicateForm = new JSPredicateForm(missingLeftParenthesisText);
            fail("Expected JSParserError but no error was thrown.");
        } catch (JSParserError e) {
            // Expected error, no action needed
        }

        String missingRightParenthesisText = "!(a";
        try {
            JSPredicateForm missingRightParenthesisPredicateForm = new JSPredicateForm(missingRightParenthesisText);
            fail("Expected JSParserError but no error was thrown.");
        } catch (JSParserError e) {
            // Expected error, no action needed
        }
    }
}
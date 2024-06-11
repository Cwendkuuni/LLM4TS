package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSParserError;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm_String() {
        String input = "(! p1)";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals(2, jspf.size());
        String expected1 = "!";
        String expected2 = "p1";
        assertEquals(expected1, jspf.elementAt(0));
        assertEquals(expected2, jspf.elementAt(1).toString());
    }

    // Test cases for other methods and edge cases can be added here
}
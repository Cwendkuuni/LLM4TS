import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructorWithText() {
        String text = "testPredicate";
        JSPredicateForm jspf = new JSPredicateForm(text);
        Assert.assertNotNull(jspf);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormConstructorWithNullText() {
        String text = null;
        JSPredicateForm jspf = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormConstructorWithEmptyText() {
        String text = "";
        JSPredicateForm jspf = new JSPredicateForm(text);
    }
}
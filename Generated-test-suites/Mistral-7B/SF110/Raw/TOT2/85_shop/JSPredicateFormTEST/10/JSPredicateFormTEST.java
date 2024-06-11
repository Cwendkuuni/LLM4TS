import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInitWithText() {
        String text = "(!p1) (or (= x 3) (= y z))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        String expected = "(!p1) (or (= x 3) (= y z))";
        assertEquals(expected, jspf.toStr().toString());
    }
}
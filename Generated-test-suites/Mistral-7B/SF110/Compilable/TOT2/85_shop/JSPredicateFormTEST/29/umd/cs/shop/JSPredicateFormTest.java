package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
    
    @Test
    public void testDefaultConstructor() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertTrue(jspf.size() == 0);
    }
}
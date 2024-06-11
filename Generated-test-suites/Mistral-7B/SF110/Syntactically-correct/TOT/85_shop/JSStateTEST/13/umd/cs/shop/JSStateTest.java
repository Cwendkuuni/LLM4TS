import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSState;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        assertEquals(0, jsState.size());
        assertNull(jsState.getLabel());
        JSUtil.println("Test for empty constructor passed.");
    }

    @Test
    public void testJSStateWithLabelConstructor() {
        String label = "TestLabel";
        JSState jsState = new JSState(label);
        assertEquals(0, jsState.size());
        assertEquals(label, jsState.getLabel());
        JSUtil.println("Test for constructor with label passed.");
    }

    @Test
    public void testAddElementsToState() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(new JSPredicateForm("p1"));
        expected.addElement(new JSPredicateForm("p2"));
        s.addElement(new JSPredicateForm("p1"));
        s.addElement(new JSPredicateForm("p2"));

        jsState.addElementsToState(s);
        assertEquals(expected.size(), jsState.size());
        for (int i = 0; i < jsState.size(); ++i) {
            assertEquals(expected.elementAt(i), jsState.elementAt(i));
        }
        JSUtil.println("Test for addElementsToState passed.");
    }

    // Add tests for other methods here
}
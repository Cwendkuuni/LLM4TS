package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        s.addElement(new JSPredicateForm("p2"));

        JSState jsState = new JSState();
        jsState.addElementsToState(s);

        assertEquals(2, jsState.size());
        assertTrue(jsState.contains(new JSPredicateForm("p1")));
        assertTrue(jsState.contains(new JSPredicateForm("p2")));
    }
}
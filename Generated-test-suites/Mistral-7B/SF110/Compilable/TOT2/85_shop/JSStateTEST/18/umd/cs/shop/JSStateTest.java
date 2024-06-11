package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState jsState = new JSState();
        JSPredicateForm element1 = new JSPredicateForm("p1");
        JSPredicateForm element2 = new JSPredicateForm("p2");
        JSPredicateForm element3 = new JSPredicateForm("p3");

        s.addElement(element1);
        s.addElement(element2);

        jsState.addElementsToState(s);

        int expectedSize = 3;
        int actualSize = jsState.size();

        assertEquals(expectedSize, actualSize);

        assertTrue(jsState.contains(element1));
        assertTrue(jsState.contains(element2));
        assertTrue(jsState.contains(element3));
    }
}
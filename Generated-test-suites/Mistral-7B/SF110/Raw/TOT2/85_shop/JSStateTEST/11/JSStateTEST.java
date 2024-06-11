import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1"));
        list.addElement(new JSPredicateForm("p2"));
        state.addElementsToState(list);
        assertEquals(2, state.size());
        JSPredicateForm el1 = state.elementAt(0);
        JSPredicateForm el2 = state.elementAt(1);
        assertEquals("p1", el1.toString());
        assertEquals("p2", el2.toString());
    }
}
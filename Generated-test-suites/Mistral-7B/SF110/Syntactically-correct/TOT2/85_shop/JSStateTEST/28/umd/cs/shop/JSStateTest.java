import static org.junit.Assert.assertEquals;
import org.junit.Test;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSState;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1", null));
        list.addElement(new JSPredicateForm("p2", null));
        state.addElementsToState(list);
        assertEquals(2, state.size());
    }
}
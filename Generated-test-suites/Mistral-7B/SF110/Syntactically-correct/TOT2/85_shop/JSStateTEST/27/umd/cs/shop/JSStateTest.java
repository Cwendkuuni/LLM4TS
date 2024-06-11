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
    }
}
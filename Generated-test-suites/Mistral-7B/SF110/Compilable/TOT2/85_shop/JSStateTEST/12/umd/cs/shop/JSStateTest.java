package umd.cs.shop;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        s.add(new JSPredicateForm("p1"));
        s.add(new JSPredicateForm("p2"));
        state.addElementsToState(s);
        assertEquals(2, state.size());
        assertTrue(state.contains(new JSPredicateForm("p1")));
        assertTrue(state.contains(new JSPredicateForm("p2")));
    }
}
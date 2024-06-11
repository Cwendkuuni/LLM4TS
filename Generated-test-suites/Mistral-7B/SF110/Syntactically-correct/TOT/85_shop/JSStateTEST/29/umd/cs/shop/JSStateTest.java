import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state1 = new JSState();
        JSListLogicalAtoms list1 = new JSListLogicalAtoms();
        list1.addElement(new JSPredicateForm("p", new IntTerm(1)));
        list1.addElement(new JSPredicateForm("q", new IntTerm(2)));
        state1.addElementsToState(list1);

        JSState state2 = new JSState();
        JSListLogicalAtoms list2 = new JSListLogicalAtoms();
        list2.addElement(new JSPredicateForm("p", new IntTerm(1)));
        list2.addElement(new JSPredicateForm("q", new IntTerm(2)));
        state2.addElementsToState(list1);

        // Test elements already in the state
        assertEquals(state1, state2);

        JSListLogicalAtoms list3 = new JSListLogicalAtoms();
        list3.addElement(new JSPredicateForm("r", new IntTerm(3)));
        state1.addElementsToState(list3);

        JSState state3 = new JSState();
        state3.addElementsToState(list3);

        // Test new elements not already in the state
        assertNotSame(state1, state3);
        assertEquals(state1.size() + list3.size(), state3.size());
    }
}
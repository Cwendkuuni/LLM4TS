import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSState state1 = new JSState();
        JSListLogicalAtoms list1 = new JSListLogicalAtoms();
        JSListLogicalAtoms list2 = new JSListLogicalAtoms();
        list1.addElement(new JSPredicateForm("p1"));
        list1.addElement(new JSPredicateForm("p2"));
        list2.addElement(new JSPredicateForm("p3"));

        state1.addElementsToState(list1);

        assertTrue(state1.size() == 3);
        assertFalse(state1.contains(new JSPredicateForm("p3")));

        JSState state2 = new JSState();
        state2.addElementsToState(list2);

        assertTrue(state2.size() == 1);
        assertFalse(state2.contains(new JSPredicateForm("p1")));

        state1.addElementsToState(list2);

        assertTrue(state1.size() == 4);
        assertTrue(state1.contains(new JSPredicateForm("p3")));
    }
}
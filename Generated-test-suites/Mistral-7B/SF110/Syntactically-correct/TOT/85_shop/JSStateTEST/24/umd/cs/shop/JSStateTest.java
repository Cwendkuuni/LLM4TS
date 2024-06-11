import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state1 = new JSState();
        JSListLogicalAtoms listToAdd = new JSListLogicalAtoms();
        listToAdd.addElement(new JSPredicateForm("p1"));
        listToAdd.addElement(new JSPredicateForm("p2"));

        state1.addElementsToState(listToAdd);
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(new JSPredicateForm("p1"));
        expected.addElement(new JSPredicateForm("p2"));

        assertEquals(expected, state1);

        JSState state2 = new JSState();
        listToAdd = new JSListLogicalAtoms();
        listToAdd.addElement(new JSPredicateForm("p1"));
        JSListLogicalAtoms state2Elements = state2.getElements();

        state2.addElementsToState(listToAdd);
        expected = new JSListLogicalAtoms();
        expected.addElement(new JSPredicateForm("p1")); // p1 was already in state2, so no new addition

        assertEquals(expected, state2);
    }
}
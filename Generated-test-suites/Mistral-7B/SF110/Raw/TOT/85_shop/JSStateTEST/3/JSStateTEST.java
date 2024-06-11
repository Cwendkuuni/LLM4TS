import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state1 = new JSState();
        JSListLogicalAtoms listToAdd = new JSListLogicalAtoms();
        listToAdd.addElement(new JSPredicateForm("p1"));
        listToAdd.addElement(new JSPredicateForm("p2"));

        state1.addElementsToState(listToAdd);

        JSListLogicalAtoms expectedState = new JSListLogicalAtoms();
        expectedState.addElement(new JSPredicateForm("p1"));
        expectedState.addElement(new JSPredicateForm("p2"));

        assertEquals(expectedState.size(), state1.size());
        for (JSPredicateForm element : state1) {
            assertEquals(expectedState.contains(element), true);
        }

        JSListLogicalAtoms listToAddDuplicates = new JSListLogicalAtoms();
        listToAddDuplicates.addElement(new JSPredicateForm("p1"));
        listToAddDuplicates.addElement(new JSPredicateForm("p1"));

        state1.addElementsToState(listToAddDuplicates);

        assertEquals(expectedState.size(), state1.size());
    }
}
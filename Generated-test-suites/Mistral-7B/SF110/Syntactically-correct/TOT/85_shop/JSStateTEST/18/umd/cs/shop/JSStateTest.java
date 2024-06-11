import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSState;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {
    // Test starts here
    @Test
    public void testSatisfiesTAm() {
        String expectedLabel = "expected_state";
        JSState state = new JSState();
        JSPredicateForm templateAtom = new JSPredicateForm("p(a, b)", new String[]{"a", "b"});

        // Add known elements to the state
        state.addElement(new JSPredicateForm("p(1, 2)", new String[]{"a", "b"}));
        state.addElement(new JSPredicateForm("p(3, 4)", new String[]{"a", "b"}));

        // Test if template atom is present in the state
        JSListSubstitution results = state.satisfiesTAm(templateAtom, new JSSubstitution());
        assertTrue(results.size() > 0);

        // Test if template atom is not present in the state
        JSState emptyState = new JSState();
        results = emptyState.satisfiesTAm(templateAtom, new JSSubstitution());
        assertEquals(0, results.size());

        // Clean up
        state = null;
        emptyState = null;
    }
    // Test ends here
}
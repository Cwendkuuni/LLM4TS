import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new JSSymbol("x"));
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new JSSymbol("y"));

        JSState state = new JSState();
        state.addElementsToState(s);
        state.addElement(atom1);
        state.addElement(atom2);

        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(atom1);
        expected.addElement(atom2);

        assertTrue(state.equals(expected));
    }
}
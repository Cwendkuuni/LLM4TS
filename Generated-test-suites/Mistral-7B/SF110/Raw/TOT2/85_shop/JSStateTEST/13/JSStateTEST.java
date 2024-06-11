import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSState;
import umd.cs.shop.JSListLogicalAtoms;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1"));
        list.addElement(new JSPredicateForm("p2"));
        state.addElementsToState(list);
        Assert.assertEquals(2, state.size());
        Assert.assertTrue(state.contains(new JSPredicateForm("p1")));
        Assert.assertTrue(state.contains(new JSPredicateForm("p2")));
    }
}
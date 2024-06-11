package umd.cs.shop;
import org.junit.Assert;
import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.add(new JSPredicateForm("p1"));
        s.add(new JSPredicateForm("p2"));
        state.addElementsToState(s);

        JSPredicateForm expectedFirstElement = new JSPredicateForm("p1");
        Assert.assertEquals(expectedFirstElement, state.elementAt(0));

        JSPredicateForm expectedSecondElement = new JSPredicateForm("p2");
        Assert.assertEquals(expectedSecondElement, state.elementAt(1));
    }
}
import static org.junit.Assert.*;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSState;

public class JSStateTest {
   @Test
   public void testAddElementsToState() {
      JSListLogicalAtoms list = new JSListLogicalAtoms();
      JSState state = new JSState();
      JSPredicateForm atom1 = new JSPredicateForm("p1(x)");
      JSPredicateForm atom2 = new JSPredicateForm("p2(y)");

      list.addElement(atom1);
      list.addElement(atom2);

      state.addElementsToState(list);

      int expectedSize = 2;
      int actualSize = state.size();

      assertEquals(expectedSize, actualSize);
   }
}
import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSState(JSListLogicalAtoms elements) {
 JSState state = new JSState();
 state.addElementsToState(elements);
 return state;
 }

 @Test
 public void testJSStateConstructor() {
 JSState state = createJSState();
 assertNull(state.getLabel());
 assertEquals(0, state.size());
 }

 @Test
 public void testJSStateConstructorWithStreamTokenizer() {
 // Since the constructor requires a StreamTokenizer argument, it cannot be tested directly
 // in this test file. You may create a separate test file for the JSListLogicalAtoms class.
 }

 @Test
 public void testAddElementsToState() {
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p"));
 list.addElement(new JSPredicateForm("q"));
 JSState state = createJSState();
 state.addElementsToState(list);
 JSListLogicalAtoms expectedList = new JSListLogicalAtoms();
 expectedList.addElement(new JSPredicateForm("p"));
 expectedList.addElement(new JSPredicateForm("q"));
 assertEquals(expectedList, state);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullElements() {
 JSState state = createJSState();
 JSListLogicalAtoms nullList = null;
 state.addElementsToState(nullList);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testApplyOpWithNullOp() {
 JSState state = createJSState();
 JSOperator nullOp = null;
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSState expectedState = createJSState();
 assertSame(expectedState, state.applyOp(nullOp, null, addList, delList));
 }

 @Test(expected = NullPointerException.class)
 public void testApplyOpWithNullAddList() {
 JSState state = createJSState();
 JSOperator op = new JSOperator(new JSPredicateForm("op"), new JSListLogicalAtoms(), null);
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSState expectedState = createJSState();
 assertSame(expectedState, state.applyOp(op, null, null, delList));
 }

 @Test(expected = NullPointerException.class)
 public void testApplyOpWithNullDelList() {
 JSState state = createJSState();
 JSOperator op = new JSOperator(new JSPredicateForm("op"), null, null);
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSState expectedState = createJSState();
 assertSame(expectedState, state.applyOp(op, null, addList, null));
 }

 @Test
 public void testApplyOp() {
 JSState state1 = createJSState();
 JSOperator op = new JSOperator(new JSPredicateForm("op"), new JSListLogicalAtoms(), null);
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("r"));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 delList.addElement(new JSPredicateForm("p"));
 JSState expectedState = createJSState();
 expectedState.addElement(new JSPredicateForm("p"));
 expectedState.addElement(new JSPredicateForm("r"));
 JSState result = state1.applyOp(op, null, addList, delList);
 assertEquals(expectedState, result);
 }

 @Test
 public void testSatisfies() {
 JSState state = createJSState();
 JSListLogicalAtoms conditions = new JSListLogicalAtoms();
 conditions.addElement(new JSPredicateForm("p"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = state.satisfies(conditions, alpha, axioms);
 assertFalse(answer.isAssigned());

 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p"));
 answer = expectedState.satisfies(conditions, alpha, axioms);
 assertTrue(answer.isAssigned());
 assertEquals(expectedState, answer.getSubstitution());
 }

 @Test
 public void testSatisfiesAll() {
 JSState state = createJSState();
 JSListLogicalAtoms conditions = new JSListLogicalAtoms();
 conditions.addElement(new JSPredicateForm("p"));
 conditions.addElement(new JSPredicateForm("q"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = state.satisfiesAll(conditions, alpha, axioms);
 assertTrue(answers.isEmpty());

 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p"));
 expectedState.addElement(new JSPredicateForm("q"));
 answers = new JSListSubstitution();
 answers.addElement(expectedState);
 assertEquals(answers, state.satisfiesAll(conditions, alpha, axioms));
 }

 @Test
 public void testSatisfiesTAm() {
 JSState state = createJSState();
 JSPredicateForm t = new JSPredicateForm("p(x)");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesTAm(t, alpha);
 assertTrue(answers.isEmpty());

 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p(a)"));
 answers = new JSListSubstitution();
 answers.addElement(new JSSubstitution().assign("a", new JSTerminalSymbol("a")));
 assertEquals(answers, state.satisfiesTAm(t, alpha));
 }
}
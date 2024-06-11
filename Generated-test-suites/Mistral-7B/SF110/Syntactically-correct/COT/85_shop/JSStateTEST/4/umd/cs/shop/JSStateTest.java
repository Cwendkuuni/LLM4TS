import org.junit.Test;
import org.junit.Assert.*;
import umd.cs.shop.*; // for imports

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSStateWithLabel(String label) {
 JSState jsState = new JSState();
 jsState.setLabel(label);
 return jsState;
 }

 private JSListLogicalAtoms createJSListLogicalAtoms() {
 return new JSListLogicalAtoms();
 }

 private JSOperator createJSOperator() {
 return new JSOperator();
 }

 private JSSubstitution createJSSubstitution() {
 return new JSSubstitution();
 }

 private JSListAxioms createJSListAxioms() {
 return new JSListAxioms();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 assertTrue(jsState instanceof JSState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertTrue(jsState instanceof JSState);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = createJSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 s.addElement(new JSPredicateForm("p2"));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = createJSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p1"));
 expected.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms actual = jsState.getState();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyOp_whenEmptyState() {
 JSState jsState = createJSState();
 JSOperator op = createJSOperator();
 JSListLogicalAtoms addL = createJSListLogicalAtoms();
 JSListLogicalAtoms delL = createJSListLogicalAtoms();
 JSTState expected = new JSTState(new JSState(), addL, delL);
 JSTate actual = jsState.applyOp(op, createJSSubstitution(), addL, delL);
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyOp_whenStateContainsElements() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSOperator op = createJSOperator();
 JSListLogicalAtoms addL = createJSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = createJSListLogicalAtoms();
 JSListLogicalAtoms expectedAddL = createJSListLogicalAtoms();
 expectedAddL.addElement(new JSPredicateForm("p1"));
 expectedAddL.addElement(new JSPredicateForm("p3"));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 JSState expectedState = createJSState();
 expectedState.addElement(new JSPredicateForm("p1"));
 JSTate actual = jsState.applyOp(op, createJSSubstitution(), addL, delL);
 JSListLogicalAtoms actualAddL = actual.getAddList();
 JSListLogicalAtoms actualDelL = actual.getDelList();
 JSState actualState = actual.getState();
 assertEquals(expectedAddL, actualAddL);
 assertEquals(expectedDelL, actualDelL);
 assertEquals(expectedState, actualState);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyOp_whenApplyMethodNotImplemented() {
 JSState jsState = createJSState();
 JSPlan pl = new JSPlan();
 JSState result = jsState.apply(pl);
 fail("apply method should not be called since it's not implemented");
 }

 @Test
 public void testSatisfies_whenEmptyState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = createJSListLogicalAtoms();
 JSSubstitution alpha = createJSSubstitution();
 JSListAxioms axioms = createJSListAxioms();
 JSSubstitution expected = createJSSubstitution();
 JSSubstitution actual = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfies_whenStateContainsElements() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms conds = createJSListLogicalAtoms();
 JSSubstitution alpha = createJSSubstitution();
 JSListAxioms axioms = createJSListAxioms();
 JSListLogicalAtoms expected = createJSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p1"));
 JSSubstitution actual = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAll_whenEmptyState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = createJSListLogicalAtoms();
 JSSubstitution alpha = createJSSubstitution();
 JSListAxioms axioms = createJSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAll_whenStateContainsElements() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms conds = createJSListLogicalAtoms();
 JSSubstitution alpha = createJSSubstitution();
 JSListAxioms axioms = createJSListAxioms();
 JSListSubstitution expected = createJSListSubstitution();
 expected.addElement(createJSSubstitution());
 expected.addElement(createJSSubstitution());
 JSListSubstitution actual = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesTAm_whenEmptyState() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("t");
 JSSubstitution alpha = createJSSubstitution();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = jsState.satisfiesTAm(t, alpha);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesTAm_whenStateContainsElements() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSOperator op = new JSOperator();
 op.setName("op");
 JSSubstitution alpha = createJSSubstitution();
 JSListLogicalAtoms expected = createJSListLogicalAtoms();
 expected.addElement(new JSSubstitution());
 JSListSubstitution actual = jsState.satisfiesTAm(new JSPredicateForm("op(p1)"), alpha);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesTAm_whenInvalidTautologyAtomMatch() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p1(a)");
 JSSubstitution alpha = createJSSubstitution();
 JSListSubstitution actual = jsState.satisfiesTAm(t, alpha);
 fail("Should throw IllegalArgumentException since the tautology atom does not match with the state");
 }
}
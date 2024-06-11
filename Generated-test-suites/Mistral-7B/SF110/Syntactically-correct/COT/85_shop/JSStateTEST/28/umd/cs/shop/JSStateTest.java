import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*; // Importing the package to use the class

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
 assertNull(jsState.getLabel());
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 s.addElement(new JSPredicateForm("q", new JSTerm("c"), new JSTerm("d")));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 expected.addElement(new JSPredicateForm("q", new JSTerm("c"), new JSTerm("d")));
 assertEquals(expected, jsState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullList() {
 JSState jsState = createJSState();
 jsState.addElementsToState(null);
 }

 @Test
 public void testApply() {
 JSState jsState = createJSState();
 JSPlan pl = new JSPlan();
 JSState result = jsState.apply(pl);
 fail("JSState.apply method should not be called as it's not implemented.");
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testApplyOp() throws Exception {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator(new JSToken("+"), new JSToken(":" + JSOperator.OperatorType.LIST), null);
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSListLogicalAtoms opAddL = new JSListLogicalAtoms();
 opAddL.addElement(new JSPredicateForm("q", new JSTerm("c"), new JSTerm("d")));
 JSListLogicalAtoms opDelL = new JSListLogicalAtoms();
 opDelL.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 JSSubstitution alpha = new JSSubstitution();
 JSTState expectedResult = new JSTState(jsState, addL, delL);
 JSTState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult.getJSState(), result.getJSState());
 assertEquals(addL, result.getAddL());
 assertEquals(delL, result.getDelL());
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testSatisfiesWithSuccessfulMatch() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSSubstitutionEntry("x", new JSTerm("c")));
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(alpha, result);
 }

 @Test(expected = NullPointerException.class)
 public void testSatisfiesWithNullConditions() {
 JSState jsState = createJSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 jsState.satisfies(null, alpha, axioms);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesWithInvalidAlpha() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution alpha = new JSSubstitution();
 jsState.satisfies(conds, null, axioms);
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 conds.addElement(new JSPredicateForm("q", new JSTerm("c"), new JSTerm("d")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds,alpha, axiums);
 assertTrue(result.isEmpty());
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testSatisfiesAllWithSuccessfulMatch() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b")));
 conds.addElement(new JSPredicateForm("q", new JSTerm("c"), new JSTerm("d")));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSSubstitutionEntry("x", new JSTerm("c")));
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(1, result.size());
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testSatisfiesTAmWithEmptyList() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b"));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertTrue(result.isEmpty());
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testSatisfiesTAmWithSuccessfulMatch() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p", new JSTerm("a"), new JSTerm("b"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSSubstitutionEntry("x", new JSTerm("c")));
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(1, result.size());
 }

}
import org.junit.Test;
import org.junit.Assert.*;
import umd.cs.shop.*; // for imports

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSState(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 Assert.assertNotNull(jsState);
 }

 @Test
 public void testJSStateApplyPlan() {
 // This method is not implemented yet, so we can't test it
 JSState jsState = createJSState();
 JSPlan jsPlan = new JSPlan(); // or use an existing instance
 JSState expectedResult = new JSState(); // or use an existing instance
 Assert.assertNotSame(jsState.apply(jsPlan), expectedResult);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable()));
 s.addElement(new JSPredicateForm("p2(y)", new JSSymbolTable()));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expectedResult = new JSListLogicalAtoms();
 expectedResult.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable()));
 expectedResult.addElement(new JSPredicateForm("p2(y)", new JSSymbolTable()));
 assertTrue(jsState.equals(expectedResult));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateInvalidElement() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new Object()); // an invalid element
 jsState.addElementsToState(s);
 }

 @Test
 public void testApplyOp() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator(":=", new JSListLogicalAtoms(), new JSListLogicalAtoms()); // or use an existing instance
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable()));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResultState = new JSState();
 JSListLogicalAtoms expectedResultAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedResultDelL = new JSListLogicalAtoms();
 expectedResultState.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable())); // or use an existing element
 expectedResultAddL.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable())); // or use an existing element
 expectedResultDelL.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable())); // or use an existing element
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertTrue(result.equals(expectedResultState));
 assertTrue(result.addElementsToState(expectedResultAddL).equals(expectedResultState));
 assertTrue(result.addElementsToState(expectedResultDelL).equals(expectedResultState));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpInvalidOp() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator(":=", null, null); // an invalid operator
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 jsState.applyOp(op, alpha, addL, delL);
 }

 @Test
 public void testSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new JSSymbolTable())); // use an existing predicate form
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListAxioms axioms = new JSListAxioms(); // or use an existing instance
 JSSubstitution expectedResult = new JSSubstitution(); // or use an existing instance
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSatisfiesEmptyList() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListAxioms axioms = new JSListAxioms(); // or use an existing instance
 JSSubstitution expectedResult = new JSSubstitution(); // or use an existing instance
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesInvalidConditions() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = null; // an invalid argument
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListAxioms axioms = new JSListAxioms(); // or use an existing instance
 jsState.satisfies(conds, alpha, axioms);
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1(x)", new JSSymbolTable()));
 conds.addElement(new JSPredicateForm("p2(y)", new JSSymbolTable())); // add another condition
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListAxioms axioms = new JSListAxioms(); // or use an existing instance
 JSListSubstitution expectedResult = new JSListSubstitution(); // or use an existing instance
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSatisfiesTAmEmptyList() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new JSSymbolTable()); // use an existing predicate form
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListSubstitution expectedResult = new JSListSubstitution(); // or use an existing instance
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesTAmInvalidT() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", null); // an invalid argument
 JSSubstitution alpha = new JSSubstitution(); // or use an existing instance
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 }

}
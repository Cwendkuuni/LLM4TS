import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSState;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSStateWithLabel(String label) {
 JSState jsState = new JSState();
 jsState.setLabel(label);
 return jsState;
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNull(jsState.getLabel());
 assertEquals(0, jsState.size());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertNull(jsState.getLabel());
 assertEquals(0, jsState.size());
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 s.addElement(new JSPredicateForm("p2"));
 jsState.addElementsToState(s);
 assertEquals(2, jsState.size());
 }

 @Test
 public void testApplyOp_GivenNullOperators() {
 JSState jsState = createJSState();
 JSOperator op = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = createJSState();
 JSState actualResult = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testApplyOp_GivenEmptyOperators() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator(new StringTokenizer(":Operator :"));
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = createJSState();
 JSState actualResult = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfies_GivenEmptyConditions() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedResult = new JSSubstitution();
 JSSubstitution actualResult = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesAll_GivenEmptyConditions() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedResult = new JSListSubstitution();
 JSListSubstitution actualResult = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesTAm_GivenNullPredicateForm() {
 JSState jsState = createJSState();
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = null;
 JSListSubstitution expectedResult = new JSListSubstitution();
 JSListSubstitution actualResult = jsState.satisfiesTAm(t, alpha);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesTAm_GivenEmptyListOfAtoms() {
 JSState jsState = createJSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListSubstitution expectedResult = new JSListSubstitution();
 JSListSubstitution actualResult = jsState.satisfiesTAm(conds, alpha);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesTAm_GivenSingleMatchingAtom() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("p1");
 JSListSubstitution expectedResult = new JSListSubstitution();
 expectedResult.addElement(alpha);
 JSListSubstitution actualResult = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesTAm_GivenMultipleMatchingAtoms() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("p1");
 JSListSubstitution expectedResult = new JSListSubstitution();
 expectedResult.addElement(alpha);
 expectedResult.addElement(alpha);
 JSListSubstitution actualResult = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesTAm_GivenNoMatchingAtoms() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("p2");
 JSListSubstitution expectedResult = new JSListSubstitution();
 JSListSubstitution actualResult = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testApplyOp_GivenValidOperators() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSOperator op = new JSOperator(new StringTokenizer(":Operator :"));
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = createJSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 expectedResult.addElement(new JSPredicateForm("p3"));
 JSState actualResult = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, actualResult);
 }

}
package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSListSubstitution;

public class JSStateTest {

 @Test
 public void testJSStateEmptyConstruction() {
 JSState jsState = new JSState();
assertEquals(0, jsState.size());
 }

 @Test
 public void testJSStateStreamTokenizerConstruction() {
 JSState jsState = new JSState(new StreamTokenizer());
fail("This method is not implemented yet.");
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.add(new JSPredicateForm("p(x)", new String[]{"x"}));
 s.add(new JSPredicateForm("q(y)", new String[]{"y"}));
 jsState.addElementsToState(s);
 assertEquals(2, jsState.size());
 }

 @Test
 public void testApplyOpNullOperators() {
 JSState jsState = new JSState();
 JSOperator op = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = null;
 JSListLogicalAtoms delL = null;
 JSTState result = jsState.applyOp(op, alpha, addL, delL);
 assertNull(result);
 }

 @Test
 public void testApplyOpEmptyOperators() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSTState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(0, result.getState().size());
 assertEquals(0, result.getAddL().size());
 assertEquals(0, result.getDelL().size());
 }

 @Test
 public void testApplyOpSingleAtomAddAndRemove() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSOperator op = new JSOperator("op", new String[]{"p(x)"}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = null;
 JSListLogicalAtoms delL = null;
 JSTState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(0, result.getState().size());
 assertEquals(1, result.getAddL().size());
 assertEquals(1, result.getDelL().size());
 assertTrue(result.getAddL().get(0).toString().equals("p(x)"));
 assertTrue(result.getDelL().get(0).toString().equals("p(x)"));
 }

 @Test
 public void testApplyOpMultipleAtomsAdd() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("q(y)", new String[]{"y"}));
 JSOperator op = new JSOperator("op", new String[]{"p(x)", "q(y)"}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSTState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(2, result.getState().size());
 assertEquals(0, result.getAddL().size());
 assertEquals(0, result.getDelL().size());
 }

 @Test
 public void testApplyOpMultipleAtomsRemove() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("p(y)", new String[]{"y"}));
 jsState.addElement(new JSPredicateForm("q(z)", new String[]{"z"}));
 JSOperator op = new JSOperator("op", new String[]{}, new String[]{"p(x)", "p(y)"}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSTState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(1, result.getState().size());
 assertEquals(2, result.getAddL().size());
 assertEquals(2, result.getDelL().size());
 assertFalse(result.getState().contains(new JSPredicateForm("p(x)", new String[]{"x"})));
 assertFalse(result.getState().contains(new JSPredicateForm("p(y)", new String[]{"y"})));
 assertTrue(result.getAddL().get(0).toString().equals("p(x)"));
 assertTrue(result.getAddL().get(1).toString().equals("p(y)"));
 assertTrue(result.getDelL().get(0).toString().equals("p(x)"));
 assertTrue(result.getDelL().get(1).toString().equals("p(y)"));
 }

 @Test
 public void testSatisfiesNullConditions() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertNull(result);
 }

 @Test
 public void testSatisfiesEmptyConditions() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesSingleCondition() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertFalse(result.isEmpty());
 assertTrue(result.get(0).size() > 0);
 }

 @Test
 public void testSatisfiesAllEmptyConditions() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("q(y)", new String[]{"y"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesAllSingleCondition() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("q(y)", new String[]{"y"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertFalse(result.isEmpty());
 assertTrue(result.size() > 0);
 }

 @Test
 public void testSatisfiesTAmSingleAtom() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSOperator op = new JSOperator("op", new String[]{"p(x)"}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(op.head(), alpha);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesTAmMultipleAtoms() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("q(y)", new String[]{"y"}));
 JSOperator op = new JSOperator("op", new String[]{"p(x)", "q(y)"}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(op.head(), alpha);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesTAmSingleAtomMatches() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("p(y)", new String[]{"y"}));
 JSOperator op = new JSOperator("op", new String[]{"p(x)"}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(op.head(), alpha);
 assertFalse(result.isEmpty());
 assertTrue(result.get(0).size() > 0);
 }

 @Test
 public void testSatisfiesTAmMultipleAtomsMatches() {
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 jsState.addElement(new JSPredicateForm("q(y)", new String[]{"y"}));
 jsState.addElement(new JSPredicateForm("r(z)", new String[]{"z"}));
 JSOperator op = new JSOperator("op", new String[]{"p(x)", "q(y)"}, new String[]{}, null, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(op.head(), alpha);
 assertTrue(result.isEmpty());
 }
}
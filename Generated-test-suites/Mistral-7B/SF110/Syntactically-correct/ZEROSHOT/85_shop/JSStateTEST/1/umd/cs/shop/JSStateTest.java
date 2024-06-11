package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNull(jsState.getLabel());
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateTokenizerConstructor() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertNull(jsState.getLabel());
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 s.addElement(new JSPredicateForm("q(b)", new String[]{"b"}));

 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 expected.addElement(new JSPredicateForm("q(b)", new String[]{"b"}));
 assertTrue(jsState.equals(expected));
 }

 @Test
 public void testApply() {
 JSState jsState = new JSState();
 JSPlan pl = new JSPlan();
 JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
 JSState expected = new JSState();
 JSState result = jsState.apply(pl);
 assertSame(expected, result);
 }

 @Test
 public void testApplyOp() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator(":", new String[]{"Op1"}, new String[]{}, new String[]{"x", "y"}, "Op1", null);
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.addElement(new JSPredicateForm("q(b)", new String[]{"b"}));

 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 expectedDelL.addElement(new JSPredicateForm("q(b)", new String[]{"b"}));

 JSTState expectedJSTState = new JSTState(expectedState, expectedAddL, expectedDelL);
 JSState resultState = jsState.applyOp(op, alpha, addL, delL);
 assertSame(expectedJSTState, resultState);
 }

 @Test
 public void testSatisfies() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 JSSubstitution expected = new JSSubstitution();
 assertSame(expected, result);
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertTrue(expected.isEmpty());
 assertSame(expected, result);
 }

 @Test
 public void testSatisfiesTAm() {
 JSState jsState = new JSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new String[]{"x"});
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertTrue(result.isEmpty());
 }
}
package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
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
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
 JSState jsState = new JSState(tokenizer);
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 s.addElement(new JSPredicateForm("q(b)", "q", new String[]{"b"}));
 JSState jsState = new JSState();
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 expected.addElement(new JSPredicateForm("q(b)", "q", new String[]{"b"}));
 assertEquals(expected, jsState);
 }

 @Test
 public void testApplyOp() {
 JSOperator op = new JSOperator("op", new String[]{"x(y)", "z(x)", "r(x)"}, new String[]{"y", "z", "r"}, new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 addL.addElement(new JSPredicateForm("q(b)", "q", new String[]{"b"}));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.addElement(new JSPredicateForm("r(c)", "r", new String[]{"c"}));
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 jsState.addElement(new JSPredicateForm("q(b)", "q", new String[]{"b"}));
 jsState.addElement(new JSPredicateForm("r(c)", "r", new String[]{"c"}));
 JSTState expected = new JSTState(new JSState(), addL, delL);
 JSTState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", "p", new String[]{"x"}));
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("x", "a");
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 JSSubstitution expected = new JSSubstitution();
 expected.assign("x", "a");
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", "p", new String[]{"x"}));
 conds.addElement(new JSPredicateForm("q(y)", "q", new String[]{"y"}));
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 jsState.addElement(new JSPredicateForm("q(b)", "q", new String[]{"b"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution());
 expected.addElement(new JSSubstitution());
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p(x)", "p", new String[]{"x"});
 JSState jsState = new JSState();
 jsState.addElement(new JSPredicateForm("p(a)", "p", new String[]{"a"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 JSListSubstitution expected = new JSListSubstitution();
 assertEquals(expected, result);
 }
}
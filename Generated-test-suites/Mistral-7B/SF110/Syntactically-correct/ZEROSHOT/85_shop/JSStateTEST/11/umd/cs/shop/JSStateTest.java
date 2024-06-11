package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

 private JSState createEmptyJSState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createEmptyJSState();
 assertNull(jsState.getLabel());
 assertEquals(0, jsState.size());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() throws IOException {
 JSState jsState = new JSState(JSUtil.getStreamTokenizer("(A.true)"));
 assertNull(jsState.getLabel());
 assertEquals(1, jsState.size());
 assertEquals("A.true", jsState.elementAt(0).toString());
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState1 = createEmptyJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.add(new JSPredicateForm("p(1)", null, null, null));
 s.add(new JSPredicateForm("q(2)", null, null, null));
 jsState1.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p(1)", null, null, null));
 expected.add(new JSPredicateForm("q(2)", null, null, null));
 List<JSPredicateForm> jsState1Actual = new ArrayList<>(jsState1.size());
 for (Object obj : jsState1) {
 jsState1Actual.add((JSPredicateForm)obj);
 }
 assertEquals(expected, jsState1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullArgument() {
 JSState jsState = createEmptyJSState();
 JSListLogicalAtoms s = null;
 jsState.addElementsToState(s);
 fail("Should throw IllegalArgumentException with null argument");
 }

 @Test
 public void testApply() {
 JSState jsState = createEmptyJSState();
 JSPlan pl = new JSPlan();
 JSState ns = jsState.apply(pl);
 assertEquals(jsState, ns);
 }

 @Test
 public void testApplyOp() {
 JSOperator op = new JSOperator("+", new JSOperator("and", new JSPredicateForm("p(1)", null, null, null), new JSPredicateForm("q(2)", null, null, null)));
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("r(3)", null, null, null));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState jsState1 = new JSState();
 jsState1.add(new JSPredicateForm("p(1)", null, null, null));
 jsState1.add(new JSPredicateForm("q(2)", null, null, null));
 JSState jsState2 = new JSState();
 jsState2.add(new JSPredicateForm("q(2)", null, null, null));
 jsState2.add(new JSPredicateForm("r(3)", null, null, null));
 JSState expectedResult = new JSState();
 expectedResult.add(new JSPredicateForm("p(1)", null, null, null));
 JSState actualResult = jsState1.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p(1)", null, null, null));
 JSState jsState1 = new JSState();
 jsState1.add(new JSPredicateForm("p(1)", null, null, null));
 jsState1.add(new JSPredicateForm("q(2)", null, null, null));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedResult = new JSSubstitution();
 expectedResult.add(new JSPredicateForm("x = 1", null, null, null));
 JSSubstitution actualResult = jsState1.satisfies(conds, alpha, axioms);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p(1)", null, null, null));
 conds.add(new JSPredicateForm("q(2)", null, null, null));
 JSState jsState1 = new JSState();
 jsState1.add(new JSPredicateForm("p(1)", null, null, null));
 jsState1.add(new JSPredicateForm("q(2)", null, null, null));
 jsState1.add(new JSPredicateForm("r(3)", null, null, null));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedResult = new JSListSubstitution();
 expectedResult.add(new JSSubstitution().assign("x", "1").assign("y", "2"));
 JSListSubstitution actualResult = jsState1.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesTAmWithSingleMatch() {
 JSState jsState1 = new JSState();
 jsState1.add(new JSPredicateForm("p(1)", null, null, null));
 jsState1.add(new JSPredicateForm("q(1)", null, null, null));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("p(1)", null, null, null);
 JSListSubstitution expectedResult = new JSListSubstitution();
 expectedResult.add(alpha);
 JSListSubstitution actualResult = jsState1.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSatisfiesTAmWithNoMatch() {
 JSState jsState1 = new JSState();
 jsState1.add(new JSPredicateForm("p(1)", null, null, null));
 jsState1.add(new JSPredicateForm("q(1)", null, null, null));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("q(1)", null, null, null);
 JSListSubstitution expectedResult = new JSListSubstitution();
 assertEquals(expectedResult, jsState1.satisfiesTAm(t, alpha));
 }

 @Test
 public void testSatisfiesTAmWithMultipleMatches() {
 JSState jsState1 = new JSState();
 jsState1.add(new JSPredicateForm("p(1)", null, null, null));
 jsState1.add(new JSPredicateForm("q(1)", null, null, null));
 jsState1.add(new JSPredicateForm("r(1)", null, null, null));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("p(1)", null, null, null);
 JSListSubstitution expectedResult = new JSListSubstitution();
 expectedResult.add(new JSSubstitution().assign("x", "1").assign("y", "1"));
 expectedResult.add(new JSSubstitution().assign("x", "1").assign("y", "2"));
 JSListSubstitution actualResult = jsState1.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, actualResult);
 }
}
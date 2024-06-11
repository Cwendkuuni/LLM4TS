package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSSubstitutionList;
import umd.cs.shop.JSSubstitutionListLogicalAtoms;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 assertTrue(jsState instanceof JSListLogicalAtoms);
 }

 @Test
 public void testJSStateConstructorWithStreamTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
 assertNull(jsState.getLabel());
 assertTrue(jsState instanceof JSListLogicalAtoms);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null));
 s.addElement(new JSPredicateForm("q(y)", new ArrayList<String>() {{ add("y"); add("z"); }}, null));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null));
 assertEquals(expected, jsState);
 }

 @Test
 public void testApply() {
 JSState jsState = createJSState();
 JSPlan pl = new JSPlan();
 JSState result = jsState.apply(pl);
 fail("JSState.apply(JSPlan) method should not be called directly.");
 }

 @Test
 public void testApplyOp() throws Exception {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op", new ArrayList<String>() {{ add(":Operator"); add("+"); }}, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.addElement(new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 JSState expected = new JSState();
 expected.addElement(new JSPredicateForm("q(y)", new ArrayList<String>() {{ add("y"); add("z"); }}, null));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expected, result.getState());
 assertEquals(expectedAddL, result.getAddList());
 assertEquals(expectedDelL, result.getDelList());
 }

 @Test
 public void testSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = jsState.satisfiesAll(conds, alpha, axioms);
 assertTrue(answers.isEmpty());
 }

 @Test
 public void testSatisfiesTAm() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new ArrayList<String>() {{ add("x"); }}, null);
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);
 assertTrue(answers.isEmpty());
 }

}
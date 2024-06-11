package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

 JSState emptyState = new JSState();
 JSState state1 = new JSState();
 JSState state2 = new JSState();
 JSListLogicalAtoms list1 = new JSListLogicalAtoms();
 JSListLogicalAtoms list2 = new JSListLogicalAtoms();
 JSPredicateForm atom1 = new And(new PredicateForm("p", new Constant("1")), new PredicateForm("q", new Constant("2")));
 JSPredicateForm atom2 = new PredicateForm("p", new Constant("3"));
 JSPredicateForm atom3 = new PredicateForm("q", new Constant("3"));
 JSPredicateForm atom4 = new PredicateForm("r", new Constant("4"));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 JSSubstitution gamma = new JSSubstitution();
 JSSubstitution delta = new JSSubstitution();

 @Test
 public void testJSStateEmptyConstructor() {
     assertTrue(emptyState.isEmpty());
 }

 @Test
 public void testJSStateNonEmptyConstructor() {
     list1.addElement(atom1);
     state1.addElementsToState(list1);
     assertFalse(emptyState.isEmpty());
     assertFalse(state1.isEmpty());
 }

 @Test
 public void testJSStateSize() {
     list1.addElement(atom1);
     list1.addElement(atom2);
     state1.addElementsToState(list1);
     state2.addElementsToState(list2);
     assertEquals(2, state1.size());
     assertEquals(0, state2.size());
 }

 @Test
 public void testJSStateApplyOp() {
     list1.addElement(atom1);
     list2.addElement(atom2);
     list2.addElement(atom3);
     state1.addElementsToState(list1);
     state2.addElementsToState(list2);
     JSPredicateForm op = new And(new PredicateForm("r", new Variable("X")), new PredicateForm("q", new Variable("X")));
     JSOp andOp = new AndOp();
     JSSubstitution alpha = new JSSubstitution();
     JSSubstitution beta1 = new JSSubstitution();
     beta1.addElement(new Assignment(new Variable("X"), new Constant("1")));
     JSSubstitution beta2 = new JSSubstitution();
     beta2.addElement(new Assignment(new Variable("X"), new Constant("2")));
     JSSubstitution gamma1 = new JSSubstitution();
     gamma1.addElement(new Assignment(new Variable("X"), new Constant("3")));
     JSSubstitution gamma2 = new JSSubstitution();
     gamma2.addElement(new Assignment(new Variable("X"), new Constant("4")));
     JSSubstitution delta1 = new JSSubstitution();
     delta1.addElement(new Assignment(new Variable("X"), new Constant("1")));
     JSSubstitution delta2 = new JSSubstitution();
     delta2.addElement(new Assignment(new Variable("X"), new Constant("2")));
     JSListLogicalAtoms delL = new JSListLogicalAtoms();
     delL.addElement(atom4);
     JSListLogicalAtoms addL = new JSListLogicalAtoms();
     addL.addElement(atom1);
     addL.addElement(atom2);
     JSTState expected = new JSTate(new JSState(addL), new JSState(addL), new JSState(delL));
     JSTState actual = state1.applyOp(andOp, alpha, list1, delL);
     assertEquals(expected, actual);
     assertEquals(3, state1.size());
     assertEquals(1, list1.size());
     assertTrue(state1.contains(atom1));
     assertTrue(state1.contains(atom2));
     assertFalse(state1.contains(atom3));
     assertFalse(state1.contains(atom4));
     assertEquals(1, alpha.size());
     assertEquals(2, beta1.size());
     assertEquals(2, beta2.size());
     assertEquals(2, gamma1.size());
     assertEquals(2, gamma2.size());
     assertEquals(2, delta1.size());
     assertEquals(2, delta2.size());
 }

 @Test
 public void testJSStateSatisfies() {
     list1.addElement(atom1);
     list1.addElement(atom2);
     state1.addElementsToState(list1);
     alpha.addElement(new Assignment(new Variable("p"), new Constant("1")));
     alpha.addElement(new Assignment(new Variable("q"), new Constant("2")));
     assertEquals(alpha, state1.satisfies(new JSListLogicalAtoms(atom1, atom2), alpha, new JSListAxioms()));
 }

 @Test
 public void testJSStateSatisfiesAll() {
     list1.addElement(atom1);
     list1.addElement(atom2);
     list2.addElement(atom3);
     state1.addElementsToState(list1);
     state2.addElementsToState(list2);
     alpha.addElement(new Assignment(new Variable("p"), new Constant("1")));
     beta.addElement(new Assignment(new Variable("q"), new Constant("2")));
     gamma.addElement(new Assignment(new Variable("q"), new Variable("X")));
     delta.addElement(new Assignment(new Variable("p"), new Variable("X")));
     assertEquals(new JSListSubstitution(new ArrayList<JSSubstitution>() {{
         add(new JSSubstitution(new ArrayList<JSSubstitution>() {{
             add(alpha);
             add(gamma);
         }}));
         add(new JSSubstitution(new ArrayList<JSSubstitution>() {{
             add(beta);
             add(delta);
         }}));
     }}), state1.satisfiesAll(new JSListLogicalAtoms(atom1, atom2, atom3), alpha, new JSListAxioms()));
 }

 @Test
 public void testJSStateSatisfiesTAm() {
     list1.addElement(atom1);
     list1.addElement(atom2);
     state1.addElementsToState(list1);
     alpha.addElement(new Assignment(new Variable("p"), new Constant("1")));
     alpha.addElement(new Assignment(new Variable("q"), new Constant("2")));
     JSPredicateForm t = new And(atom1, atom2);
     JSSubstitution answer = state1.satisfiesTAm(t, alpha);
     assertEquals(new JSListSubstitution(new ArrayList<JSSubstitution>() {{
         add(alpha);
     }}), answer);
 }

}
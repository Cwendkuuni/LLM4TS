package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {

 JSState emptyState = new JSState();
 JSState state1 = new JSState();
 JSState state2 = new JSState();
 JSState state3 = new JSState();
 JSListLogicalAtoms list1 = new JSListLogicalAtoms();
 JSListLogicalAtoms list2 = new JSListLogicalAtoms();
 JSListLogicalAtoms list3 = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSPredicateForm p1, p2, p3;

 @Test
 public void testJSStateEmptyConstructor() {
 assertNotNull(emptyState);
 }

 @Test
 public void testJSStateWithConstructor() {
 assertNotNull(new JSState(new StreamTokenizer(new StringReader(""))));
 }

 @Test
 public void testAddElementsToState() {
 list1.addElement(p1 = new JSPredicateForm("p1(x)", new SymbolTable()));
 list1.addElement(p2 = new JSPredicateForm("p2(x)", new SymbolTable()));
 state1.addElementsToState(list1);
 state1.addElement(p3 = new JSPredicateForm("p3(x)", new SymbolTable()));
 state2.addElementsToState(list1);
 state3.addElementsToState(list2);

 assertEquals(3, state1.size());
 assertEquals(1, state2.size());
 assertEquals(0, state3.size());

 assertEquals(p1, state1.elementAt(0));
 assertEquals(p1, state2.elementAt(0));
 assertNotEquals(p1, state3.elementAt(0));
 assertEquals(p3, state1.elementAt(2));
 assertNull(state3.elementAt(0));
 }

 @Test
 public void testApply() {
 assertNull(state1.apply(null));
 }

 @Test
 public void testApplyOp() {
 p1.addElement(new JSPredicateSymbol("x", new SymbolTable()));
 p2.addElement(new JSPredicateSymbol("y", new SymbolTable()));
 p3.addElement(new JSPredicateSymbol("z", new SymbolTable()));

 list1.addElement(p1);
 list2.addElement(p2);
 list3.addElement(p3);

 JSOperator op = new JSOperator("op", new SymbolTable(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
 op.addElement(p1);
 op.addElement(p2);
 op.addElement(p3);

 JSState expectedState = new JSState();
 expectedState.addElement(p1);
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.addElement(p2);
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();

 JSState actualState = state1.applyOp(op, alpha, list1, list2);
 assertEquals(expectedState, actualState);
 assertEquals(expectedAddL.size(), actualState.size() - state1.size());
 assertEquals(expectedDelL.size(), list2.size());
 }

 @Test
 public void testSatisfies() {
 p1.addElement(new JSPredicateSymbol("x", new SymbolTable()));
 p2.addElement(new JSPredicateSymbol("y", new SymbolTable()));
 list1.addElement(p1);
 list1.addElement(p2);

 axioms.addAxiom(new JSListAxioms.JSListAxiom("(p1(x) -> p2(x))", new SymbolTable()));

 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSPredicateSymbol("x", new SymbolTable()), new JSPredicateSymbol("y", new SymbolTable()));

 JSSubstitution actualAnswer = state1.satisfies(list1, alpha, axioms);
 assertEquals(expectedAnswer, actualAnswer);
 }

 @Test
 public void testSatisfiesAll() {
 p1.addElement(new JSPredicateSymbol("x", new SymbolTable()));
 p2.addElement(new JSPredicateSymbol("y", new SymbolTable()));
 list1.addElement(p1);
 list1.addElement(p2);
 list2.addElement(p2);
 list2.addElement(p3 = new JSPredicateForm("p3(y)", new SymbolTable()));

 JSListSubstitution expectedAnswer = new JSListSubstitution();
 expectedAnswer.addElement(new JSSubstitution());
 expectedAnswer.addElement(new JSSubstitution());
 expectedAnswer.addElement(new JSPredicateSymbol("x", new SymbolTable()), new JSPredicateSymbol("y", new SymbolTable()));
 expectedAnswer.addElement(new JSPredicateSymbol("y", new SymbolTable()), new JSPredicateSymbol("z", new SymbolTable()));

 JSSubstitution alpha1 = new JSSubstitution();
 alpha1.addElement(new JSPredicateSymbol("x", new SymbolTable()), new JSPredicateSymbol("a", new SymbolTable()));
 JSSubstitution alpha2 = new JSSubstitution();
 alpha2.addElement(new JSPredicateSymbol("y", new SymbolTable()), new JSPredicateSymbol("b", new SymbolTable()));

 JSSubstitution actualAnswer = state1.satisfiesAll(list1, alpha1, axioms);
 assertEquals(expectedAnswer, actualAnswer);
 }

 @Test
 public void testSatisfiesTAm() {
 p1.addElement(new JSPredicateSymbol("x", new SymbolTable()));
 p2.addElement(new JSPredicateSymbol("y", new SymbolTable()));
 list1.addElement(p1);
 list1.addElement(p2);

 JSPredicateForm t = new JSPredicateForm("p1(x)", new SymbolTable());
 t.addElement(new JSPredicateSymbol("x", new SymbolTable()), new JSPredicateSymbol("y", new SymbolTable()));

 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSPredicateSymbol("x", new SymbolTable()), new JSPredicateSymbol("y", new SymbolTable()));

 JSSubstitution actualAnswer = state1.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, actualAnswer);
 }

}
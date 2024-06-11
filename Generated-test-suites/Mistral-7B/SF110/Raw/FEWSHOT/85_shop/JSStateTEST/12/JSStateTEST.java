package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {

 JSState newJSState() {
 return new JSState();
 }

 JSState newJSState(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 JSState newJSStateWithLabel(String label) {
 JSState jsState = newJSState();
 jsState.label = label;
 return jsState;
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = newJSState();
 assertNull(jsState.label);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
 JSState jsState = newJSState(tokenizer);
 assertNull(jsState.label);
 }

 @Test
 public void testJSStateAddElementsToState() {
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.add(new JSPredicateForm("p1"));
 s.add(new JSPredicateForm("p2"));
 JSState jsState = newJSState();
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 assertEquals(expected, jsState.getState());
 }

 @Test
 public void testJSStateApply() {
 JSPlan pl = new JSPlan();
 JSState jsState = newJSState();
 JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
 JSState expected = newJSState();
 assertEquals(expected, jsState.apply(pl));
 }

 @Test
 public void testJSStateApplyOp() {
 JSOperator op = new JSOperator("op", new JSArithmeticOperator("+"));
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p1"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState jsState = newJSState();
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.add(new JSPredicateForm("p1"));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 JSState expectedNS = newJSState();
 JSListLogicalAtoms expectednAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectednDelL = new JSListLogicalAtoms();
 assertNull(jsState.applyOp(op, null, addL, delL).getNS());
 assertNull(jsState.applyOp(op, null, addL, delL).getnAddL());
 assertNull(jsState.applyOp(op, null, addL, delL).getnDelL());
 jsState = newJSStateWithLabel("state1");
 jsState.addElementsToState(expectedAddL);
 JSListLogicalAtoms add = op.addList();
 add.add(new JSPredicateForm("p3"));
 JSListLogicalAtoms del = op.deleteList();
 JSListLogicalAtoms opAddL = add.applySubstitutionListLogicalAtoms(null);
 JSListLogicalAtoms opDelL = del.applySubstitutionListLogicalAtoms(null);
 JSState ns = newJSState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 assertEquals(expectedStateLabel(jsState.getLabel()), expectedStateLabel(ns.getLabel()));
 assertEquals(expectedAddL, opAddL);
 assertEquals(expectedDelL, opDelL);
 assertEquals(expected, ns);
 assertEquals(expectedAddL, jsState.applyOp(op, null, addL, delL).getnAddL());
 assertEquals(expectedDelL, jsState.applyOp(op, null, addL, delL).getnDelL());
 assertEquals(ns, jsState.applyOp(op, null, addL, delL).getNS());
 }

 @Test
 public void testJSStateSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected, answer);
 }

 @Test
 public void testJSStateSatisfiesAll() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 conds.add(new JSPredicateForm("p2"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution answers = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, answers);
 }

 @Test
 public void testJSStateSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p(x)", new JSSymbolTable());
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);
 assertEquals(answers, new JSListSubstitution());
 }

 @Test
 public void testJSStateSatisfiesTAmWithMatch() {
 JSPredicateForm t = new JSPredicateForm("p(x)", new JSSymbolTable());
 JSPredicateForm el = new JSPredicateForm("p(y)", new JSSymbolTable());
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = new JSListSubstitution();
 answers.add(new JSSubstitution());
 JSState jsState = newJSStateWithLabel("state1");
 jsState.addElement(el);
 assertEquals(answers, jsState.satisfiesTAm(t, alpha));
 }

 private String expectedStateLabel(String label) {
 return label == null ? "state" : label;
 }

}
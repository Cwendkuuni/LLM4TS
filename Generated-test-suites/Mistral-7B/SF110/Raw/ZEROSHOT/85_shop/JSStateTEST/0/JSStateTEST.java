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
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSState(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 @Test
 public void testEmptyConstructor() {
 JSState jsState = createJSState();
 assertEquals(0, jsState.size());
 }

 @Test
 public void testStreamTokenizerConstructor() throws IOException {
 // Assuming that the StreamTokenizer is properly initialized and provides valid tokens
 JSState jsState = createJSState(new StreamTokenizer(new ByteArrayInputStream("".getBytes())));
 assertEquals(0, jsState.size());
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p", new ArrayList<>()));
 s.addElement(new JSPredicateForm("q", new ArrayList<>()));
 jsState.addElementsToState(s);

 assertEquals(2, jsState.size());

 JSPredicateForm p = new JSPredicateForm("p", new ArrayList<>());
 JSPredicateForm q = new JSPredicateForm("q", new ArrayList<>());

 assertEquals(p, jsState.elementAt(0));
 assertEquals(q, jsState.elementAt(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullList() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = null;
 jsState.addElementsToState(s);
 }

 @Test
 public void testApplyOpWithNullOperator() {
 JSState jsState = createJSState();
 JSOperator op = null;
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();

 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertNull(result);
 }

 @Test
 public void testApplyOpWithEmptyAddLAndDelL() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op");
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();

 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(jsState, result);
 }

 @Test
 public void testApplyOpWithNullAddLAndDelL() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op");
 JSListLogicalAtoms addL = null;
 JSListLogicalAtoms delL = null;

 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(jsState, result);
 }

 @Test
 public void testApplyOpWithNullSubstitution() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op");
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = null;

 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertNull(result);
 }

 @Test
 public void testApplyOp() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op");
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();

 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertNull(result); // apply method is not implemented yet
 }

 @Test
 public void testSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new ArrayList<>()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesWithSatisfyingSubstitution() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new ArrayList<>()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution satisfyingSubstitution = new JSSubstitution();
 satisfyingSubstitution.addElement(new JSSubstitution.Binding("x", new JSSubstitution.Value("a")));
 axioms.addAxiom(new JSAxiom(new JSPredicateForm("p(x)", new ArrayList<>()), satisfyingSubstitution));

 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(satisfyingSubstitution, result);
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new ArrayList<>()));
 conds.addElement(new JSPredicateForm("q", new ArrayList<>()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesAllWithSatisfyingSubstitutions() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new ArrayList<>()));
 conds.addElement(new JSPredicateForm("q", new ArrayList<>()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution satisfyingSubstitution1 = new JSSubstitution();
 satisfyingSubstitution1.addElement(new JSSubstitution.Binding("x", new JSSubstitution.Value("a")));
 JSSubstitution satisfyingSubstitution2 = new JSSubstitution();
 satisfyingSubstitution2.addElement(new JSSubstitution.Binding("y", new JSSubstitution.Value("b")));
 axioms.addAxiom(new JSAxiom(new JSPredicateForm("p(x)", new ArrayList<>()), satisfyingSubstitution1));
 axioms.addAxiom(new JSAxiom(new JSPredicateForm("q(y)", new ArrayList<>()), satisfyingSubstitution2));

 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(satisfyingSubstitution1, result.elementAt(0));
 assertEquals(satisfyingSubstitution2, result.elementAt(1));
 }

 @Test
 public void testSatisfiesTAmWithSatisfyingSubstitution() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new ArrayList<>());
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution satisfyingSubstitution = new JSSubstitution();
 satisfyingSubstitution.addElement(new JSSubstitution.Binding("x", new JSSubstitution.Value("a")));

 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(satisfyingSubstitution, result.elementAt(0));
 }

}
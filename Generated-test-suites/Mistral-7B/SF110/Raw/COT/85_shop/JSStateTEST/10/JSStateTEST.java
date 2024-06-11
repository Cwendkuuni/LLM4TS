import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {

 private JSState createState() {
 return new JSState();
 }

 private JSState createStateWithLabel(String label) {
 JSState state = new JSState();
 state.label = label;
 return state;
 }

 private JSState createStateWithElements(JSListLogicalAtoms elements) {
 JSState state = new JSState();
 state.addElementsToState(elements);
 return state;
 }

 private JSOperator createOperator() {
 return new JSOperator();
 }

 private JSPlan createPlan() {
 return new JSPlan();
 }

 private JSListLogicalAtoms createListWithElements(JSPredicateForm... elements) {
 return new JSListLogicalAtoms(elements);
 }

 private JSListLogicalAtoms createListWithLabelAndElements(String label, JSPredicateForm... elements) {
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm(label));
 list.addElements(elements);
 return list;
 }

 private JSSubstitution createSubstitution() {
 return new JSSubstitution();
 }

 private JSSubstitution createSubstitutionWithAssignFailure() {
 JSSubstitution substitution = new JSSubstitution();
 substitution.assignFailure();
 return substitution;
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateTokenizerConstructor() {
 JSState jsState = new JSState(new FileReader("testFile.txt"));
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 JSListLogicalAtoms list = createListWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createState();
 state.addElementsToState(list);
 JSListLogicalAtoms stateList = state.getLogicalAtoms();
 assertEquals(2, stateList.size());
 assertTrue(stateList.contains(new JSPredicateForm("p1")));
 assertTrue(stateList.contains(new JSPredicateForm("p2")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateNullList() {
 JSState state = createState();
 try {
 state.addElementsToState(null);
 fail("This method should throw an IllegalArgumentException when passed a null list.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddElementsToStateDuplicateElement() {
 JSListLogicalAtoms list = createListWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p1"));
 JSState state = createState();
 state.addElementsToState(list);
 JSListLogicalAtoms stateList = state.getLogicalAtoms();
 assertEquals(1, stateList.size());
 }

 @Test
 public void testApply() {
 JSState state = createState();
 JSPlan plan = createPlan();
 JSState expected = createState();
 assertEquals(expected, state.apply(plan));
 }

 @Test
 public void testApplyOp() {
 JSOperator op = createOperator();
 JSState state1 = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSListLogicalAtoms addList = createListWithElements(new JSPredicateForm("p3"), new JSPredicateForm("p4"));
 JSListLogicalAtoms delList = createListWithElements(new JSPredicateForm("p2"));
 JSState expectedState = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p3"), new JSPredicateForm("p4"));
 JSState result = state1.applyOp(op, createSubstitution(), addList, delList);
 assertEquals(expectedState, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpNullOp() {
 JSListLogicalAtoms addList = createListWithElements(new JSPredicateForm("p1"));
 JSListLogicalAtoms delList = createListWithElements(new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p3"));
 try {
 state.applyOp(null, createSubstitution(), addList, delList);
 fail("This method should throw an IllegalArgumentException when passed a null operator.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpNullAddList() {
 JSOperator op = createOperator();
 JSListLogicalAtoms delList = createListWithElements(new JSPredicateForm("p1"));
 JSState state = createStateWithElements(new JSPredicateForm("p2"));
 try {
 state.applyOp(op, createSubstitution(), null, delList);
 fail("This method should throw an IllegalArgumentException when passed a null add list.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpNullDelList() {
 JSOperator op = createOperator();
 JSListLogicalAtoms addList = createListWithElements(new JSPredicateForm("p1"));
 JSState state = createStateWithElements(new JSPredicateForm("p2"));
 try {
 state.applyOp(op, createSubstitution(), addList, null);
 fail("This method should throw an IllegalArgumentException when passed a null del list.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
 JSSubstitution alpha = createSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesWithSatisfyingSubstitution() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
 JSSubstitution alpha = createSubstitution();
 JSSubstitution beta = createSubstitution();
 beta.addElement(new JSSubstitution.Assignment("x", new JSPredicateForm("p1")));
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertEquals(beta, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesNullConditionsList() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"));
 JSSubstitution alpha = createSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 try {
 state.satisfies(null, alpha, axioms);
 fail("This method should throw an IllegalArgumentException when passed a null conditions list.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesNullSubstitution() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
 JSListAxioms axioms = new JSListAxioms();
 try {
 state.satisfies(conds, null, axioms);
 fail("This method should throw an IllegalArgumentException when passed a null substitution.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesNullAxiomsList() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
 JSSubstitution alpha = createSubstitution();
 try {
 state.satisfies(conds, alpha, null);
 fail("This method should throw an IllegalArgumentException when passed a null axioms list.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSatisfiesTAmWithMatchedAtom() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"));
 JSState state = createStateWithElements(new JSPredicateForm("p1"));
 JSOperator operator = createOperator();
 JSSubstitution alpha = createSubstitution();
 JSSubstitution result = state.satisfiesTAm(operator.head(), alpha);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesTAmWithMatchedAtoms() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSOperator operator = createOperator();
 JSSubstitution alpha = createSubstitution();
 JSListSubstitution result = state.satisfiesTAm(operator.head(), alpha);
 assertEquals(1, result.size());
 }

 @Test
 public void testSatisfiesTAmWithNoMatchedAtoms() {
 JSListLogicalAtoms conds = createListWithLabelAndElements("cond1", new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSState state = createStateWithElements(new JSPredicateForm("p3"));
 JSOperator operator = createOperator();
 JSSubstitution alpha = createSubstitution();
 JSListSubstitution result = state.satisfiesTAm(operator.head(), alpha);
 assertTrue(result.isEmpty());
 }
}
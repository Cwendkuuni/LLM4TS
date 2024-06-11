package umd.cs.shop.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSState;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {

 private JSState jsState;
 private JSListLogicalAtoms jsListLogicalAtoms1;
 private JSListLogicalAtoms jsListLogicalAtoms2;
 private JSOperator jsOperator;
 private JSListAxioms jsListAxioms;
 private JSSubstitution jsSubstitution1;
 private JSSubstitution jsSubstitution2;
 private JSPredicateForm jspredicateForm1;
 private JSPredicateForm jspredicateForm2;
 private List<JSPredicateForm> listJSPredicateForm;
 private List<JSSubstitution> listJSSubstitution;
 private Map<String, JSPredicateForm> mapJSPredicateForm;

 @Before
 public void setUp() {
 jsState = new JSState();
 jsListLogicalAtoms1 = new JSListLogicalAtoms();
 jsListLogicalAtoms2 = new JSListLogicalAtoms();
 jsOperator = new JSOperator();
 jsListAxioms = new JSListAxioms();
 jsSubstitution1 = new JSSubstitution();
 jsSubstitution2 = new JSSubstitution();
 jspredicateForm1 = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"y"}, new String[]{"p"}, new String[]{"x", "y"});
 jspredicateForm2 = new JSPredicateForm("q(x)", new String[]{"x"}, new String[]{"z"}, new String[]{"q"}, new String[]{"x", "z"});
 listJSPredicateForm = new ArrayList<>();
 listJSSubstitution = new ArrayList<>();
 mapJSPredicateForm = new HashMap<>();
 mapJSPredicateForm.put("p(x)", jspredicateForm1);
 mapJSPredicateForm.put("q(x)", jspredicateForm2);
 }

 // Test constructor
 @Test
 public void testJSState() {
 JSState jsState1 = new JSState();
 assertNotNull(jsState1);
 }

 // Test empty constructor
 @Test
 public void testJSStateEmpty() {
 jsState = new JSState();
 assertEquals(0, jsState.size());
 }

 // Test constructor with StreamTokenizer
 @Test
 public void testJSStateWithTokenizer() {
 String input = "((p(x) | q(x)) & (r(y) | s(y))).applySubstitutionTA(<alpha>):-";
 JSState jsState1 = new JSState(JSUtil.tokenizer(input));
 assertEquals(0, jsState1.size());
 }

 // Test addElementsToState
 @Test
 public void testAddElementsToState() {
 jsListLogicalAtoms1.add(jspredicateForm1);
 jsListLogicalAtoms2.add(jspredicateForm2);
 jsState.addElementsToState(jsListLogicalAtoms1);
 jsState.addElementsToState(jsListLogicalAtoms2);
 assertEquals(2, jsState.size());
 }

 // Test contains
 @Test
 public void testContains() {
 jsListLogicalAtoms1.add(jspredicateForm1);
 jsState.addElementsToState(jsListLogicalAtoms1);
 assertTrue(jsState.contains(jspredicateForm1));
 }

 // Test equals
 @Test
 public void testEquals() {
 JSState jsState1 = new JSState();
 JSState jsState2 = new JSState();
 assertFalse(jsState1.equals(jsState2));
 jsState1.addElementsToState(jspredicateForm1);
 jsState2.addElementsToState(jspredicateForm1);
 assertTrue(jsState1.equals(jsState2));
 }

 // Test getClass
 @Test
 public void testGetClass() {
 assertEquals(JSState.class, jsState.getClass());
 }

 // Test hashCode
 @Test
 public void testHashCode() {
 JSState jsState1 = new JSState();
 JSState jsState2 = new JSState();
 assertEquals(jsState1.hashCode(), jsState2.hashCode());
 jsState1.addElementsToState(jspredicateForm1);
 jsState2.addElementsToState(jspredicateForm1);
 assertEquals(jsState1.hashCode(), jsState2.hashCode());
 }

 // Test insertElementAt
 @Test
 public void testInsertElementAt() {
 jsState.insertElementAt(jspredicateForm1, 0);
 assertEquals(jspredicateForm1, jsState.elementAt(0));
 }

 // Test size
 @Test
 public void testSize() {
 assertEquals(0, jsState.size());
 jsState.addElementsToState(jspredicateForm1);
 assertEquals(1, jsState.size());
 }

 // Test apply
 @Test
 public void testApply() {
 JSPlan jsPlan = new JSPlan();
 JSState jsState1 = jsState.apply(jsPlan);
 assertNull(jsState1);
 }

 // Test applyOp
 @Test
 public void testApplyOp() {
 JSOperator jsOperator = new JSOperator();
 jsOperator.addList(jsListLogicalAtoms1);
 jsOperator.deleteList(jsListLogicalAtoms2);
 JSSubstitution jsSubstitution1 = new JSSubstitution();
 JSSubstitution jsSubstitution2 = new JSSubstitution();
 listJSSubstitution.add(jsSubstitution1);
 listJSSubstitution.add(jsSubstitution2);
 JSListLogicalAtoms jsListLogicalAtoms3 = new JSListLogicalAtoms();
 JSListLogicalAtoms jsListLogicalAtoms4 = new JSListLogicalAtoms();
 jsListLogicalAtoms3.add(jspredicateForm1);
 jsListLogicalAtoms3.add(jspredicateForm2);
 jsListLogicalAtoms4.add(jspredicateForm2);
 jsListLogicalAtoms4.add(jspredicateForm1);
 JSState expectedJSState = new JSState();
 expectedJSState.addElementsToState(jsListLogicalAtoms3);
 JSTState jsTState = expectedJSState.applyOp(jsOperator, jsSubstitution1, jsListLogicalAtoms1, jsListLogicalAtoms2);
 JSState jsState1 = jsState.applyOp(jsOperator, jsSubstitution1, jsListLogicalAtoms1, jsListLogicalAtoms2);
 assertEquals(expectedJSState, jsState1);
 }

 // Test satisfies
 @Test
 public void testSatisfies() {
 JSListLogicalAtoms jsListLogicalAtoms1 = new JSListLogicalAtoms();
 jsListLogicalAtoms1.add(jspredicateForm1);
 JSSubstitution jsSubstitution1 = new JSSubstitution();
 jsSubstitution1.assign("x", "a");
 jsSubstitution1.assign("y", "b");
 JSSubstitution expectedJsSubstitution = new JSSubstitution();
 expectedJsSubstitution.assign("x", "a");
 expectedJsSubstitution.assign("y", "b");
 JSListSubstitution jsListSubstitution = jsState.satisfies(jsListLogicalAtoms1, jsSubstitution1, jsListAxioms);
 assertEquals(expectedJsSubstitution, jsListSubstitution.elementAt(0));
 }

 // Test satisfiesAll
 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms jsListLogicalAtoms1 = new JSListLogicalAtoms();
 jsListLogicalAtoms1.add(jspredicateForm1);
 jsListLogicalAtoms1.add(jspredicateForm2);
 JSSubstitution jsSubstitution1 = new JSSubstitution();
 jsSubstitution1.assign("x", "a");
 jsSubstitution1.assign("y", "b");
 jsSubstitution1.assign("z", "c");
 JSListSubstitution expectedJsListSubstitution = new JSListSubstitution();
 expectedJsListSubstitution.add(jsSubstitution1);
 JSListSubstitution jsListSubstitution = jsState.satisfiesAll(jsListLogicalAtoms1, jsSubstitution1, jsListAxioms);
 assertEquals(expectedJsListSubstitution, jsListSubstitution);
 }

 // Test satisfiesTAm
 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm jspredicateForm3 = new JSPredicateForm("r(z)", new String[]{"z"}, new String[]{}, new String[]{"r"}, new String[]{"z"});
 JSSubstitution jsSubstitution1 = new JSSubstitution();
 jsSubstitution1.assign("x", "a");
 jsSubstitution1.assign("y", "b");
 jsSubstitution1.assign("z", "c");
 List<JSSubstitution> jsListSubstitution = jsState.satisfiesTAm(jspredicateForm1, jsSubstitution1);
 assertTrue(jsListSubstitution.isEmpty());
 jsListSubstitution = jsState.satisfiesTAm(jspredicateForm2, jsSubstitution1);
 assertTrue(jsListSubstitution.isEmpty());
 jsListSubstitution = jsState.satisfiesTAm(jspredicateForm3, jsSubstitution1);
 assertTrue(jsListSubstitution.isEmpty());
 }
}
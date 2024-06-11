import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSState;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {

 private JSState state1;
 private JSState state2;
 private JSListLogicalAtoms list1;
 private JSListLogicalAtoms list2;
 private JSListLogicalAtoms list3;
 private JSPlan plan;
 private JSOperator op;
 private JSSubstitution alpha;
 private JSListAxioms axioms;

 @Before
 public void setUp() {
 state1 = new JSState();
 state2 = new JSState();
 list1 = new JSListLogicalAtoms();
 list2 = new JSListLogicalAtoms();
 list3 = new JSListLogicalAtoms();
 plan = new JSPlan();
 op = new JSOperator();
 alpha = new JSSubstitution();
 axioms = new JSListAxioms();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 list1.addElement(new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"}));
 list1.addElement(new JSPredicateForm("q", new String[]{"x"}, new String[]{"z"}));
 state1.addElementsToState(list1);
 list2.addElement(new JSPredicateForm("p", new String[]{"z"}, new String[]{"x"}));
 state1.addElementsToState(list2);

 assertEquals(3, state1.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateNullList() {
 JSListLogicalAtoms nullList = null;
 state1.addElementsToState(nullList);
 }

 @Test
 public void testApply() {
 JSState result = state1.apply(plan);
 assertEquals(state1, result);
 }

 @Test
 public void testApplyOpNullOp() {
 try {
 state1.applyOp(null, alpha, list1, list2);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testApplyOpNullAlpha() {
 try {
 state1.applyOp(op, null, list1, list2);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testApplyOpNullAddL() {
 JSState result = state1.applyOp(op, alpha, null, list2);
 assertEquals(state1, result);
 }

 @Test
 public void testApplyOpNullDelL() {
 JSState result = state1.applyOp(op, alpha, list1, null);
 assertEquals(state1, result);
 }

 @Test
 public void testSatisfies() {
 list3.addElement(new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"}));
 list3.addElement(new JSPredicateForm("q", new String[]{"x"}, new String[]{"z"}));
 JSSubstitution alpha1 = new JSSubstitution();
 alpha1.addElement(new JSSubstitution.SubstitutionEntry("y", "a"));
 alpha1.addElement(new JSSubstitution.SubstitutionEntry("z", "b"));

 JSPredicateForm t = new JSPredicateForm("p", new String[]{"x"}, new String[]{"c"});

 JSListSubstitution answers = state1.satisfies(list3, alpha1, axioms);
 assertEquals(0, answers.size());

 JSPredicateForm t1 = new JSPredicateForm("p", new String[]{"x"}, new String[]{"a"});
 JSListSubstitution answers1 = state1.satisfies(list3, alpha1, axioms);
 assertEquals(1, answers1.size());
 }

 @Test
 public void testSatisfiesAll() {
 list1.addElement(new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"}));
 list1.addElement(new JSPredicateForm("q", new String[]{"x"}, new String[]{"z"}));
 list2.addElement(new JSPredicateForm("r", new String[]{"x"}, new String[]{"w"}));
 list2.addElement(new JSPredicateForm("s", new String[]{"x"}, new String[]{"z"}));
 JSSubstitution alpha1 = new JSSubstitution();
 alpha1.addElement(new JSSubstitution.SubstitutionEntry("y", "a"));
 alpha1.addElement(new JSSubstitution.SubstitutionEntry("z", "b"));
 JSListSubstitution answers = state1.satisfiesAll(new JSListLogicalAtoms(new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"}), new JSPredicateForm("q", new String[]{"x"}, new String[]{"z"}), new JSPredicateForm("r", new String[]{"x"}, new String[]{"w"})), alpha1, axioms);
 assertEquals(0, answers.size());

 JSListSubstitution answers1 = state1.satisfiesAll(new JSListLogicalAtoms(new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"}), new JSPredicateForm("q", new String[]{"x"}, new String[]{"z"}), new JSPredicateForm("r", new String[]{"x"}, new String[]{"w"})), alpha1, axioms);
 assertEquals(1, answers1.size());
 }

 @Test
 public void testSatisfiesTAmNullT() {
 try {
 state1.satisfiesTAm(null, alpha);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testSatisfiesTAmNullAlpha() {
 try {
 state1.satisfiesTAm(t, null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testSatisfiesTAmEmptyState() {
 JSPredicateForm t1 = new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"});
 JSListSubstitution answers = state1.satisfiesTAm(t1, alpha);
 assertEquals(0, answers.size());
 }

 @Test
 public void testSatisfiesTAmMatches() {
 list1.addElement(new JSPredicateForm("p", new String[]{"x"}, new String[]{"y"}));
 list1.addElement(new JSPredicateForm("p", new String[]{"x"}, new String[]{"z"}));
 JSSubstitution alpha1 = new JSSubstitution();
 alpha1.addElement(new JSSubstitution.SubstitutionEntry("y", "a"));
 alpha1.addElement(new JSSubstitution.SubstitutionEntry("z", "b"));
 JSListSubstitution answers = state1.satisfiesTAm(t, alpha1);
 assertEquals(1, answers.size());
 }
}
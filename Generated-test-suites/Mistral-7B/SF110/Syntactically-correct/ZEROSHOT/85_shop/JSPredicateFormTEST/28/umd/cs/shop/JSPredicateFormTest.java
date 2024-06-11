import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private static String testData = "(! x < 5)";

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm pf = new JSPredicateForm(testData);
 assertEquals("!(x<5)", pf.elementAt(0).toString());
 assertEquals(1, pf.size());
 }

 @Test
 public void testJSPredicateFormInitNull() {
 assertThrows(NullPointerException.class, () -> new JSPredicateForm(null));
 }

 @Test
 public void testJSPredicateFormInitStreamTokenizer() {
 JSPredicateForm pf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 assertEquals("!(x<5)", pf.elementAt(0).toString());
 assertEquals(1, pf.size());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm pf = new JSPredicateForm(testData);
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals(pf.toString(), clonedPF.toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSPredicateForm pf = new JSPredicateForm(testData);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", 7);
 JSPredicateForm appliedPF = pf.applySubstitutionPF(alpha);
 assertEquals("!(7<5)", appliedPF.elementAt(0).toString());
 assertEquals(1, appliedPF.size());
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSPredicateForm pf = new JSPredicateForm(testData);
 JSPredicateForm t = new JSPredicateForm("!(x>5)");
 JSSubstitution beta = pf.matches(t);
 assertNull(beta);
 t = new JSPredicateForm(testData);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", 5);
 beta = pf.matches(t, alpha);
 assertNotNull(beta);
 assertEquals(0, beta.size());
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSPredicateForm pf1 = new JSPredicateForm(testData);
 JSPredicateForm pf2 = new JSPredicateForm(testData);
 assertTrue(pf1.equals(pf2));
 JSPredicateForm pf3 = new JSPredicateForm("! (x<5)");
 assertFalse(pf1.equals(pf3));
 }

 @Test
 public void testJSPredicateFormToString() {
 JSPredicateForm pf = new JSPredicateForm(testData);
 assertEquals("!(x<5)", pf.toString());
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm(testData);
 JSPredicateForm np = pf.standarizerPredicateForm();
 assertEquals("!(= x 5)", np.toString());
 }

 @Test
 public void testVectorAdd() {
 Vector<Object> vec = new Vector<>();
 vec.add("test");
 vec.add(1);
 assertEquals(2, vec.size());
 }
}
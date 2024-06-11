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
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

 private static JSPredicateForm createPredicateForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testJSPredicateFormEmpty() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals("", pf.toStr().toString());
 assertTrue(pf.size() == 0);
 }

 @Test
 public void testJSPredicateFormInit() {
 // Test parsing an empty string
 String text = "";
 JSPredicateForm pf = createPredicateForm(text);
 assertEquals("", pf.toStr().toString());
 assertTrue(pf.size() == 0);

 // Test parsing a simple predicate
 text = " (x + y > 5) ";
 pf = createPredicateForm(text);
 assertEquals("(x+y)>5", pf.elementAt(0).toString());
 assertEquals(1, pf.size());

 // Test parsing a complex predicate with not operator
 text = " (not (x > y) ) ";
 pf = createPredicateForm(text);
 assertEquals("not", pf.elementAt(0).toString());
 assertEquals(1, pf.size());
 assertEquals("x>y", pf.elementAt(1).toStr().toString());

 // Test parsing a complex predicate with call operator
 text = " (call f(x, y) ) ";
 pf = createPredicateForm(text);
 assertEquals("call", pf.elementAt(0).toString());
 assertEquals("f", pf.elementAt(1).toString());
 assertEquals(1, pf.size());

 // Test parsing a complex predicate with multiple terms
 text = " (x < y and z > 5) ";
 pf = createPredicateForm(text);
 assertEquals("x<y", pf.elementAt(0).toStr().toString());
 assertEquals("and", pf.elementAt(pf.size() - 1).toString());
 assertEquals("z>", pf.elementAt(pf.size() - 2).toStr().toString());
 assertEquals(3, pf.size());
 }

 @Test
 public void testJSPredicateFormMethods() {
 JSPredicateForm pf = testJSPredicateFormInit();

 // Test print method
 pf.print();
 String expectedPrint = " (x<y) ";
 String actualPrint = JSUtil.getPrintBuffer().toString();
 assertEquals(expectedPrint, actualPrint);
 JSUtil.getPrintBuffer().setLength(0);

 // Test toStr method
 assertEquals(expectedPrint, pf.toStr().toString());

 // Test clonePF method
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals(pf.toStr().toString(), clonedPF.toStr().toString());

 // Test applySubstitutionPF method
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("a"));
 alpha.addBinding("y", new JSTerm("b"));
 JSPredicateForm pfSubstituted = pf.applySubstitutionPF(alpha);
 assertEquals("(a<b)", pfSubstituted.toStr().toString());

 // Test matches method
 JSPredicateForm pfMatch = createPredicateForm("(x<y and z > 5)");
 JSSubstitution beta = pf.matches(pfMatch);
 assertNull(beta);

 // Test matches method with substitution
 JSSubstitution gamma = new JSSubstitution();
 gamma.addBinding("x", new JSTerm("a"));
 gamma.addBinding("y", new JSTerm("b"));
 gamma.addBinding("z", new JSTerm("c"));
 beta = pf.matches(pfMatch, gamma);
 assertEquals(gamma, beta);

 // Test equals method
 JSPredicateForm pfEqual = createPredicateForm("(x<y and z > 5)");
 assertTrue(pf.equals(pfEqual));
 assertFalse(pf.equals(createPredicateForm("(x<y and z <= 5)")));

 // Test standarizerPredicateForm method
 JSPredicateForm pfStandardized = pf.standarizerPredicateForm();
 assertEquals("(< (x) (y))", pfStandardized.toStr().toString());
 }

 private JSPredicateForm testJSPredicateFormInit() {
 String text = " (x < y and z > 5) ";
 JSPredicateForm pf = createPredicateForm(text);
 return pf;
 }
}
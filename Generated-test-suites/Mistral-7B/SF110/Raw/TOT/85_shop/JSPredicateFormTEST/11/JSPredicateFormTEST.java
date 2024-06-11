import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 emptyForm.print();
 assertFalse(emptyForm.equals(new JSPredicateForm("not", new JSPredicateForm())));
 }

 @Test
 public void testSingleTermForm() {
 JSPredicateForm form = new JSPredicateForm("A");
 form.print();
 assertArrayEquals(new String[]{"A"}, form.toStr().toString().split("\\) "));
 assertTrue(form.equals(new JSPredicateForm("A")));
 }

 @Test
 public void testNegatedForm() {
 JSPredicateForm form1 = new JSPredicateForm("not", new JSPredicateForm("A"));
 form1.print();
 assertArrayEquals(new String[]{"not", "A"}, form1.toStr().toString().split("\\) "));

 JSPredicateForm form2 = new JSPredicateForm("not", new JSPredicateForm("not", new JSPredicateForm("A")));
 form2.print();
 assertArrayEquals(new String[]{"not", "not", "A"}, form2.toStr().toString().split("\\) "));

 JSPredicateForm form3 = new JSPredicateForm("not", new JSPredicateForm("not", new JSPredicateForm("A"), new JSTerm("B")));
 form3.print();
 assertArrayEquals(new String[]{"not", "not", "A", "B"}, form3.toStr().toString().split("\\) "));

 JSPredicateForm form4 = new JSPredicateForm("not", new JSTerm("A"));
 form4.print();
 assertArrayEquals(new String[]{"not", "A"}, form4.toStr().toString().split("\\) "));
 }

 @Test(expected = JSParserError.class)
 public void testMalformedInput() {
 new JSPredicateForm("not ( A");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 new JSPredicateForm("not A");
 }

 @Test
 public void testApplySubstitution() {
 JSPredicateForm pf1 = new JSPredicateForm("A(X)", new JSTerm("X"));
 JSPredicateForm pf2 = new JSPredicateForm("B(Y)", new JSTerm("Y"));
 JSSubstitution subs = new JSSubstitution();
 subs.addMapping("X", "1");
 subs.addMapping("Y", "2");
 JSPredicateForm expected = new JSPredicateForm("A(1)", new JSTerm("2"));
 JSPredicateForm result = pf1.applySubstitutionPF(subs);
 assertTrue(result.equals(expected));
 }

 @Test
 public void testMatches() {
 JSPredicateForm pf1 = new JSPredicateForm("A(X)", new JSTerm("X"));
 JSPredicateForm pf2 = new JSPredicateForm("B(Y)", new JSTerm("Y"));
 JSSubstitution subs = new JSSubstitution();
 subs.addMapping("X", "1");
 subs.addMapping("Y", "2");
 JSSubstitution result = pf1.matches(pf2);
 assertFalse(result.isEmpty());
 assertEquals("X->1, Y->2", result.toString());
 }

 @Test
 public void testMatchesFailure() {
 JSPredicateForm pf1 = new JSPredicateForm("A(X)", new JSTerm("Y"));
 JSPredicateForm pf2 = new JSPredicateForm("B(X)", new JSTerm("Z"));
 JSSubstitution result = pf1.matches(pf2);
 assertTrue(result.isEmpty());
 }
}
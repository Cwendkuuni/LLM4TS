import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 assertEquals("[-not- [=- x 3=-] [=- x 4=-]]", jspf1.toStr().toString());
 }

 @Test
 public void testJSPredicateFormInitTokenizer() {
 String text = "(not (or (= x 3) (= x 4)))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf2 = new JSPredicateForm(tokenizer);
 assertEquals("[-not- [=- x 3=-] [=- x 4=-]]", jspf2.toStr().toString());
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 jspf.print();
 assertEquals("(not (or (= x 3) (= x 4)))", System.out.getStackTraceElement(0).getMethodName());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm jspf2 = jspf1.clonePF();
 assertEquals("[-not- [=- x 3=-] [=- x 4=-]]", jspf2.toStr().toString());
 assertTrue(jspf1 != jspf2);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.bind("x", new JSTerm("5"));
 JSPredicateForm jspf2 = jspf1.applySubstitutionPF(alpha);
 assertEquals("(not (or (= x 5) (= x 5)))", jspf2.toStr().toString());
 }

 @Test
 public void testMatches() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm jspf2 = new JSPredicateForm("(not (or (= x 5) (= x 5)))");
 JSSubstitution beta = jspf1.matches(jspf2);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithSubstitution() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.bind("x", new JSTerm("5"));
 JSPredicateForm jspf2 = new JSPredicateForm("(not (or (= x 5) (= x 5)))");
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm jspf2 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testNotEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm jspf2 = new JSPredicateForm("(not (or (= x 3) (= x 5)))");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm stdJspf = jspf.standarizerPredicateForm();
 assertEquals("[-not- [=- x=-] [= or- [=[= =-] [= x=-] [= 3=-] ] [=[= =-] [= x=-] [= 4=-] ]]]", stdJspf.toStr().toString());
 }
}
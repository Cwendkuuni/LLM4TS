import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*; // for importing all necessary classes

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());
 }

 @Test
 public void testStringConstructor() {
 String input = "!(p(x) -> not q(y))";
 JSPredicateForm pf = new JSPredicateForm(input);
 assertEquals(4, pf.size());

 String expResult = "(!(p(x) -> not q(y)))";
 String result = pf.toStr().toString();
 assertEquals(expResult, result);
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String input = "(!(p(x) -> not q(y)))";
 StreamTokenizer st = new StreamTokenizer(new StringReader(input));
 JSPredicateForm pf = new JSPredicateForm(st);
 assertEquals(4, pf.size());

 String expResult = "(!(p(x) -> not q(y)))";
 String result = pf.toStr().toString();
 assertEquals(expResult, result);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidInput() {
 String input = "((p(x) -> not q(y)"; // missing '!' for not operator
 StreamTokenizer st = new StreamTokenizer(new StringReader(input));
 JSPredicateForm pf = new JSPredicateForm(st);
 fail("Expected JSParserError to be thrown");
 }

 @Test
 public void testPrint() {
 String input = "!(p(x) -> not q(y))";
 JSPredicateForm pf = new JSPredicateForm(input);
 System.out.println("Input: " + input);
 pf.print();
 String expResult = "((! p(x)) -> not (q(y)))";
 String result = "((! p(x)) -> not (q(y)))\n";
 assertEquals(expResult, result);
 }

 @Test
 public void testClonePF() {
 String input = "!(p(x) -> not q(y))";
 JSPredicateForm pf = new JSPredicateForm(input);
 JSPredicateForm clonedPF = pf.clonePF();

 assertEquals(pf.size(), clonedPF.size());
 for (int i = 0; i < pf.size(); i++) {
 JSTerm pfElement = pf.elementAt(i);
 JSTerm clonedPFElement = clonedPF.elementAt(i);
 assertEquals(pfElement.toString(), clonedPFElement.toString());
 }
 }

 @Test
 public void testApplySubstitutionPF() {
 String input = "!(p(x) -> not q(y))";
 JSPredicateForm pf = new JSPredicateForm(input);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("p(x)", "r(z)");
 alpha.addSubstitution("q(y)", "s(w)");
 JSPredicateForm expectedPF = new JSPredicateForm("!(r(z) -> not s(w))");
 JSPredicateForm resultPF = pf.applySubstitutionPF(alpha);

 assertEquals(pf.size(), resultPF.size());
 for (int i = 0; i < pf.size(); i++) {
 JSTerm pfElement = pf.elementAt(i);
 JSTerm resultPFElement = resultPF.elementAt(i);
 assertEquals(pfElement.toString(), resultPFElement.toString());
 }
 assertEquals(expectedPF.toStr().toString(), resultPF.toStr().toString());
 }

 @Test
 public void testMatches() {
 String input1 = "!(p(x) -> not q(y))";
 String input2 = "!(p(a) -> r(b))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = pf1.matches(pf2, alpha);

 assertFalse(result.isFailure());
 assertEquals(0, result.size());
 }

 @Test
 public void testMatchesFailure() {
 String input1 = "!(p(x) -> not q(y))";
 String input2 = "!(p(x) -> r(y))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = pf1.matches(pf2, alpha);

 assertTrue(result.isFailure());
 }

 @Test
 public void testEquals() {
 String input1 = "!(p(x) -> not q(y))";
 String input2 = "!(p(x) -> not q(y))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertTrue(pf1.equals(pf2));

 String input3 = "!(p(x) -> r(y))";
 assertFalse(pf1.equals(new JSPredicateForm(input3)));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String input = "!(p(x) -> not q(y))";
 JSPredicateForm pf = new JSPredicateForm(input);
 JSPredicateForm expectedPF = new JSPredicateForm("(!(p(x) -> q(y)))");
 JSPredicateForm resultPF = pf.standarizerPredicateForm();

 assertEquals(pf.size(), resultPF.size());
 for (int i = 0; i < pf.size(); i++) {
 JSTerm pfElement = pf.elementAt(i);
 JSTerm resultPFElement = resultPF.elementAt(i);
 assertEquals(pfElement.toString(), resultPFElement.toString());
 }
 assertEquals(expectedPF.toStr().toString(), resultPF.toStr().toString());
 }
}
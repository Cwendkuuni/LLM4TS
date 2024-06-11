import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import umd.cs.shop.*;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());
 }

 @Test
 public void testJSPredicateFormInitWithText() {
 JSPredicateForm pf = new JSPredicateForm("(not (and a b))");
 assertEquals(3, pf.size());
 assertEquals("not", pf.get(0).toString().toLowerCase());
 assertNotNull(pf.get(1));
 assertEquals("and", pf.get(2).toString().toLowerCase());
 assertEquals("a", pf.get(3).toString());
 assertEquals("b", pf.get(4).toString());
 }

 @Test
 public void testJSPredicateFormInitWithStreamTokenizer() {
 String text = "(not (and a b))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertEquals(3, pf.size());
 assertEquals("not", pf.get(0).toString().toLowerCase());
 assertNotNull(pf.get(1));
 assertEquals("and", pf.get(2).toString().toLowerCase());
 assertEquals("a", pf.get(3).toString());
 assertEquals("b", pf.get(4).toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidText() {
 String text = "(not (and a b ))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertFalse(pf.isEmpty());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMissingOpenParenthesis() {
 String text = "not (and a b)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertFalse(pf.isEmpty());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMissingCloseParenthesis() {
 String text = "(not (and a b";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertFalse(pf.isEmpty());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMissingOperator() {
 String text = "(not a b)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertFalse(pf.isEmpty());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidOperator() {
 String text = "(not x %%%)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertFalse(pf.isEmpty());
 }

 @Test
 public void testJSPredicateFormPrint() {
 JSPredicateForm pf = new JSPredicateForm("(not (and a b))");
 pf.print();
 assertEquals("(not (and a b))", System.getProperty("line.separator") + pf.toStr());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm pf = new JSPredicateForm("(not (and a b))");
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals(3, clonedPF.size());
 assertEquals("not", clonedPF.get(0).toString().toLowerCase());
 assertNotNull(clonedPF.get(1));
 assertEquals("and", clonedPF.get(2).toString().toLowerCase());
 assertEquals("a", clonedPF.get(3).toString());
 assertEquals("b", clonedPF.get(4).toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSTerm termA = new JSTerm("a", new IntValue(42));
 JSTerm termB = new JSTerm("b", new IntValue(13));
 JSPredicateForm pf = new JSPredicateForm("(not (and a b))");
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm np = pf.applySubstitutionPF(alpha);
 assertEquals("(not (and b 42))", np.toStr());
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSTerm termA = new JSTerm("a", new IntValue(42));
 JSTerm termB = new JSTerm("b", new IntValue(42));
 JSTerm termC = new JSTerm("c", new IntValue(13));
 JSPredicateForm pf1 = new JSPredicateForm("(and a b)");
 JSPredicateForm pf2 = new JSPredicateForm("(and b a)");
 JSPredicateForm pf3 = new JSPredicateForm("(not (and a b))");
 JSPredicateForm pf4 = new JSPredicateForm("(not (and b a))");
 JSPredicateForm pf5 = new JSPredicateForm("(not (and a c))");
 JSPredicateForm pf6 = new JSPredicateForm("(not (and c a))");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 JSSubstitution gama = new JSSubstitution();
 alpha.addElement(new Substitution("a", termA));
 alpha.addElement(new Substitution("b", termB));
 gama.addElement(new Substitution("a", termC));
 gama.addElement(new Substitution("b", termA));

 assertEquals(alpha, pf1.matches(pf1, alpha));
 assertEquals(gama, pf2.matches(pf1, gama));
 assertEquals(alpha, pf1.matches(pf2, alpha));
 assertEquals(alpha, pf1.matches(pf3, alpha));
 assertEquals(alpha, pf1.matches(pf4, alpha));
 assertEquals(alpha, pf1.matches(pf5, alpha));
 assertEquals(alpha, pf1.matches(pf6, alpha));
 assertEquals(null, pf1.matches(new JSPredicateForm(), alpha));
 assertEquals(null, pf1.matches(new JSTerm("a"), alpha));
 assertEquals(null, pf1.matches(new JSTerm("b"), alpha));
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSTerm termA = new JSTerm("a", new IntValue(42));
 JSTerm termB = new JSTerm("b", new IntValue(42));
 JSPredicateForm pf1 = new JSPredicateForm("(and a b)");
 JSPredicateForm pf2 = new JSPredicateForm("(and a b)");
 JSPredicateForm pf3 = new JSPredicateForm("(and b a)");
 JSPredicateForm pf4 = new JSPredicateForm("(not (and a b))");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();

 assertEquals(pf1, pf1);
 assertEquals(pf1, pf2);
 assertFalse(pf1.equals(pf3));
 assertFalse(pf1.equals(pf4));
 assertFalse(pf1.equals(new JSTerm("a")));
 assertFalse(pf1.equals(new JSTerm("b")));
 assertFalse(pf1.equals(new JSSubstitution()));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 JSTerm termA = new JSTerm("a", new IntValue(42));
 JSTerm termB = new JSTerm("b", new IntValue(13));
 JSPredicateForm pf = new JSPredicateForm("(not (or (not a) (not b)))");
 JSPredicateForm np = pf.standarizerPredicateForm();
 assertEquals("(not (or (and (not a) a) (and (not b) b)))", np.toStr());
 }
}
package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private StreamTokenizer createTokenizer(String text) {
 return new StreamTokenizer(new StringReader(text));
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.size() == 0);
 }

 @Test
 public void testStringConstructor() {
 String text = "(!p)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals("!p", jspf.elementAt(0).toString());
 assertEquals(1, jspf.size());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String text = "(!p)";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals("!p", jspf.elementAt(0).toString());
 assertEquals(1, jspf.size());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidInitialization() {
 String text = "(!p)";
 StreamTokenizer tokenizer = createTokenizer(text);
 tokenizer.nextToken(); // Consume '('
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 fail("Should throw JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 String text = "(!p";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 fail("Should throw JSParserError");
 }

 @Test
 public void testJSPredicateFormInit() {
 String text = "(!p)";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals("!p", jspf.elementAt(0).toString());
 assertEquals(1, jspf.size());
 }

 @Test
 public void testNestedNot() {
 String text = "(!(!p))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals("!(!p)", jspf.toStr().toString());
 }

 @Test
 public void testNestedCall() {
 String text = "(!f(x))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 JSTerm callTerm = jspf.elementAt(1);
 assertEquals("f", callTerm.toString());
 assertEquals(1, callTerm.size());
 assertEquals("x", callTerm.elementAt(0).toString());
 }

 @Test
 public void testNormalFormWithNegation() {
 String text = "(!p & q)";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 JSPredicateForm nf = jspf.standarizerPredicateForm();
 assertEquals("!(p & q)", nf.toStr().toString());
 }

 @Test
 public void testNormalFormWithCall() {
 String text = "(!f(x)) & g(y)";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 JSPredicateForm nf = jspf.standarizerPredicateForm();
 assertEquals("!(f(x) & g(y))", nf.toStr().toString());
 }

 @Test
 public void testIdentityEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspf2 = new JSPredicateForm("(!p) & q");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testDifferentSize() {
 JSPredicateForm jspf1 = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspf2 = new JSPredicateForm("(!p) & q & r");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testDifferentFunctor() {
 JSPredicateForm jspf1 = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspf2 = new JSPredicateForm("!(p & r)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testDifferentTerms() {
 JSPredicateForm jspf1 = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspf2 = new JSPredicateForm("(!p) & r");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveElement() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 jspf.remove(0);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testMatchesNull() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 assertFalse(jspf.matches(null));
 }

 @Test
 public void testMatchesIdentity() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 assertTrue(jspf.matches(jspf).isEmpty());
 }

 @Test
 public void testMatchesDifferentSize() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspft = new JSPredicateForm("(!p) & q & r");
 assertFalse(jspf.matches(jspft).isEmpty());
 }

 @Test
 public void testMatchesDifferentFunctor() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspft = new JSPredicateForm("!(p & r)");
 assertFalse(jspf.matches(jspft).isEmpty());
 }

 @Test
 public void testMatchesDifferentTerms() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSPredicateForm jspft = new JSPredicateForm("!(p & r)");
 JSSubstitution substitution = jspf.matches(jspft);
 assertEquals("p->r", substitution.toStr().toString());
 }

 @Test
 public void testMatchesEmptySubstitution() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSSubstitution substitution = jspf.matches(new JSSubstitution());
 assertTrue(substitution.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSubstitution() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSSubstitution substitution = new JSSubstitution();
 substitution.add("p", "q");
 assertFalse(jspf.matches(substitution).isEmpty());
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSubstitutionWithOneTerm() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSSubstitution substitution = jspf.matches(new JSSubstitution());
 assertTrue(substitution.isEmpty());
 }

 @Test
 public void testSubstitutionWithOneTermReplace() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & p");
 JSSubstitution substitution = jspf.matches(new JSSubstitution()).cloneS();
 substitution.add("p", "q");
 JSPredicateForm jspft = new JSPredicateForm("(!p) & q");
 assertTrue(jspf.equals(jspft.applySubstitutionPF(substitution)));
 }

 @Test
 public void testSubstitutionWithMultipleTerms() {
 JSPredicateForm jspf = new JSPredicateForm("(!p & q) & r & s");
 JSSubstitution substitution = jspf.matches(new JSSubstitution());
 assertTrue(substitution.isEmpty());
 }

 @Test
 public void testSubstitutionWithMultipleTermsReplace() {
 JSPredicateForm jspf = new JSPredicateForm("(!p & q) & r & s");
 JSSubstitution substitution = jspf.matches(new JSSubstitution()).cloneS();
 substitution.add("p", "r");
 substitution.add("q", "s");
 JSPredicateForm jspft = new JSPredicateForm("(!r & s) & r & s");
 assertTrue(jspf.equals(jspft.applySubstitutionPF(substitution)));
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSPredicateForm clonedPF = jspf.clonePF();
 assertTrue(jspf.equals(clonedPF));
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testClonePFNotSupported() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSTerm jsterm = new JSTerm("p");
 jspf.addElement(jsterm);
 throw new AssertionError("Should not reach this point");
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm jspft = new JSPredicateForm("!(p & r) & s");
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 assertTrue(jspf.equals(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplySubstitutionPFNullSubstitution() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSPredicateForm result = jspf.applySubstitutionPF(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testApplySubstitutionPFClonedTerms() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSTerm jsterm = new JSTerm("p");
 jspf.addElement(jsterm.cloneT());
 JSSubstitution substitution = new JSSubstitution();
 substitution.add("p", "q");
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 fail("Should throw CloneNotSupportedException");
 }

 @Test
 public void testApplySubstitutionFTerm() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSTerm jsterm = new JSTerm("p");
 JSPredicateForm jspft = new JSPredicateForm("!(q)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.add("p", jspft);
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 assertEquals(jspft, result.elementAt(0));
 }

 @Test
 public void testApplySubstitutionFNotTerm() {
 JSPredicateForm jspf = new JSPredicateForm("(!(!p))");
 JSPredicateForm jspft = new JSPredicateForm("(!(!q))");
 JSSubstitution substitution = new JSSubstitution();
 substitution.add("p", "q");
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 assertEquals(jspft, result);
 }

 @Test
 public void testApplySubstitutionFCallTerm() {
 JSPredicateForm jspf = new JSPredicateForm("(!f(x))");
 JSTerm callTerm = new JSTerm("f(x)");
 JSPredicateForm jspft = new JSPredicateForm("!(g(y))");
 JSSubstitution substitution = new JSSubstitution();
 substitution.add("f(x)", "g(y)");
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 assertEquals(jspft, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplySubstitutionFInvalidSubstitution() {
 JSPredicateForm jspf = new JSPredicateForm("(!p) & q");
 JSTerm jsterm = new JSTerm("p");
 JSTerm jsterm2 = new JSTerm("q");
 JSPredicateForm jspft = new JSPredicateForm("!(r & s)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.add("p", jsterm2);
 substitution.add("q", jsterm);
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 fail("Should throw IllegalArgumentException");
 }

}
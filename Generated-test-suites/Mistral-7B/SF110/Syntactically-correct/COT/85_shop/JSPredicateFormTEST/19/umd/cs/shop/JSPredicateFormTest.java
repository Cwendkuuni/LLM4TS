package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private JSTerm createTerm(String termString) {
 return new JSTerm(new StreamTokenizer(new StringReader(termString)));
 }

 @Test
 public void testJSPredicateFormConstructorWithText() {
 String text = "not ( ( a = b ) and ( c = d ) )";
 JSPredicateForm pf = new JSPredicateForm(text);
 assertEquals("not", pf.elementAt(0));
 JSPredicateForm not = (JSPredicateForm) pf.elementAt(1);
 assertEquals("(", not.elementAt(0));
 assertEquals("a", not.elementAt(1).toString());
 assertEquals("b", not.elementAt(2).toString());
 assertEquals("and", not.elementAt(3).toString());
 JSPredicateForm and = (JSPredicateForm) not.elementAt(4);
 assertEquals("(", and.elementAt(0));
 assertEquals("a", and.elementAt(1).toString());
 assertEquals("=", and.elementAt(2).toString());
 assertEquals("b", and.elementAt(3).toString());
 assertEquals(")", and.size() - 1, and.size());
 assertEquals("c", and.elementAt(4).elementAt(0).toString());
 assertEquals("=", and.elementAt(4).elementAt(1).toString());
 assertEquals("d", and.elementAt(4).elementAt(2).toString());
 assertEquals(")", and.size() - 1, and.size());
 assertEquals(0, pf.size() - and.size());
 }

 @Test
 public void testJSPredicateFormConstructorWithTokenizer() {
 String text = "not ( ( a = b ) and ( c = d ) )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 // same assertions as in testJSPredicateFormConstructorWithText()
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(null);
 }

 @Test
 public void testJSPredicateFormInitWithInvalidTokenType() {
 String text = "not ( a = b )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm();
 try {
 pf.JSPredicateFormInit(tokenizer);
 fail();
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testJSPredicateFormInitWithUnexpectedCloseParenthesis() {
 String text = "not ( a = b ) )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm();
 try {
 pf.JSPredicateFormInit(tokenizer);
 fail();
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testJSPredicateFormPrint() {
 JSPredicateForm pf = new JSPredicateForm("not ( ( a = b ) and ( c = d ) )");
 pf.print();
 assertEquals("(not ( (a = b) and (c = d) ))", pf.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = pf1.clonePF();
 assertEquals(pf1.toStr().toString(), pf2.toStr().toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 JSPredicateForm pf2 = pf1.applySubstitutionPF(alpha);
 assertEquals("not ( " + termA.toString() + " = " + termB.toString() + " )", pf2.toStr().toString());
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSTerm termD = createTerm("d");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( c = d )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 alpha.add("c", termC);
 alpha.add("d", termD);
 JSSubstitution matches = pf1.matches(pf2, alpha);
 assertEquals(true, matches.isDefined());
 assertEquals(0, matches.size());
 }

 @Test
 public void testJSPredicateFormMatchesWithMismatchingSize() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSTerm termD = createTerm("d");
 JSTerm termE = createTerm("e");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( a = b ) ( c = d )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 alpha.add("c", termC);
 alpha.add("d", termD);
 JSSubstitution matches = pf1.matches(pf2, alpha);
 assertEquals(false, matches.isDefined());
 }

 @Test
 public void testJSPredicateFormMatchesWithMismatchingFunctor() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSTerm termD = createTerm("d");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( a = c )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 alpha.add("c", termC);
 JSSubstitution matches = pf1.matches(pf2, alpha);
 assertEquals(false, matches.isDefined());
 }

 @Test
 public void testJSPredicateFormMatchesWithMismatchingTerms() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSTerm termD = createTerm("d");
 JSTerm termE = createTerm("e");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( c = d )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 alpha.add("c", termC);
 alpha.add("d", termD);
 JSSubstitution matches = pf1.matches(pf2, alpha);
 assertEquals(false, matches.isDefined());
 }

 @Test
 public void testJSPredicateFormMatchesWithSuccess() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSTerm termD = createTerm("d");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( c = d )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 alpha.add("c", termC);
 alpha.add("d", termD);
 JSSubstitution matches = pf1.matches(pf2, alpha);
 assertEquals(true, matches.isDefined());
 assertEquals(0, matches.size());
 }

 @Test
 public void testJSPredicateFormEqualsWithNull() {
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 assertFalse(pf1.equals(null));
 }

 @Test
 public void testJSPredicateFormEqualsWithDifferentSize() {
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( a = b ) ( c = d )");
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testJSPredicateFormEqualsWithDifferentFunctor() {
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( a = c )");
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testJSPredicateFormEqualsWithDifferentTerms() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( c = d )");
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testJSPredicateFormEqualsWithSameValues() {
 JSTerm termA = createTerm("a");
 JSTerm termB = createTerm("b");
 JSTerm termC = createTerm("c");
 JSTerm termD = createTerm("d");
 JSPredicateForm pf1 = new JSPredicateForm("not ( a = b )");
 JSPredicateForm pf2 = new JSPredicateForm("not ( a = b )");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", termA);
 alpha.add("b", termB);
 JSPredicateForm pf3 = new JSPredicateForm("not ( a = b )");
 pf3.applySubstitutionPF(alpha);
 assertTrue(pf1.equals(pf3));
 }
}
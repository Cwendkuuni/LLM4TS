package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Vector;

public class JSPredicateFormTest {

 private JSPredicateForm pf1;
 private JSPredicateForm pf2;
 private JSPredicateForm pf3;
 private JSPredicateForm pf4;
 private StreamTokenizer tokenizer;

 @Before
 public void setUp() {
 tokenizer = new StreamTokenizer(new StringReader("(not (and x y) (or x (not y)))"));
 }

 @Test
 public void testJSPredicateFormInitWithText() {
 pf1 = new JSPredicateForm(tokenizer.sval);
 assertEquals("JSPredicateForm: size = 3", 3, pf1.size());
 assertEquals("JSPredicateForm: element at index 0 = not", "not", pf1.elementAt(0));
 assertEquals("JSPredicateForm: element at index 1 = and", "and", pf1.elementAt(1));
 assertEquals("JSPredicateForm: element at index 2 = x", "x", pf1.elementAt(2));
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 pf2 = new JSPredicateForm(tokenizer);
 assertEquals("JSPredicateForm: size = 3", 3, pf2.size());
 assertEquals("JSPredicateForm: element at index 0 = not", "not", pf2.elementAt(0));
 assertEquals("JSPredicateForm: element at index 1 = and", "and", pf2.elementAt(1));
 assertEquals("JSPredicateForm: element at index 2 = x", "x", pf2.elementAt(2));
 }

 @Test(expected = JSParserError.class)
 public void testInvalidJSPredicateFormInit() {
 tokenizer = new StreamTokenizer(new StringReader("(not (and x y) %%%)"));
 try {
 pf3 = new JSPredicateForm(tokenizer);
 fail("JSParserError expected");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testPrint() {
 pf1.print();
 assertEquals("Printed: (not (and x y) (or x (not y)))", "(not (and x y) (or x (not y)))");
 }

 @Test
 public void testToString() {
 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 String str1 = pf1.toString();
 String str2 = "(\\(not (x) (and z w)))";
 assertEquals("ToString: " + str1, str2, str1);
 }

 @Test
 public void testClonePF() {
 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 JSPredicateForm pfC1 = pf1.clonePF();
 JSPredicateForm pfC2 = pfC1.clonePF();

 assertEquals("ClonePF: size of pf1 and pfC1 is equal", pf1.size(), pfC1.size());
 assertEquals("ClonePF: size of pfC1 and pfC2 is equal", pfC1.size(), pfC2.size());
 assertEquals("ClonePF: pf1 equals pfC1", pf1, pfC1);
 assertEquals("ClonePF: pfC1 equals pfC2", pfC1, pfC2);
 }

 @Test
 public void testApplySubstitutionPF() {
 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("a"));
 alpha.addBinding("y", new JSTerm("b"));
 alpha.addBinding("z", new JSTerm("c"));
 alpha.addBinding("w", new JSTerm("d"));

 JSPredicateForm pfAS1 = pf1.applySubstitutionPF(alpha);

 String str1 = pf1.toString();
 String str2 = "(\\(not (a) (and c d)))";
 assertEquals("ApplySubstitutionPF: " + str1, str2, str1);
 assertEquals("ApplySubstitutionPF: size of pf1 = 6 and pfAS1 = 6", 6, pf1.size());
 assertEquals("ApplySubstitutionPF: size of pfAS1 = 6", 6, pfAS1.size());
 }

 @Test
 public void testMatches() {
 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 JSPredicateForm pf2 = new JSPredicateForm();
 pf2.addElement("not");
 pf2.addElement(new JSTerm("x"));
 pf2.addElement(new JSTerm("y"));
 pf2.addElement("and");
 pf2.addElement(new JSTerm("z"));
 pf2.addElement(new JSTerm("w"));

 JSSubstitution beta = pf1.matches(pf2);

 assertEquals("Matches: size of beta = 0", 0, beta.size());

 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 pf2 = new JSPredicateForm();
 pf2.addElement("not");
 pf2.addElement(new JSTerm("x"));
 pf2.addElement(new JSTerm("y"));
 pf2.addElement("or");
 pf2.addElement(new JSTerm("z"));
 pf2.addElement(new JSTerm("w"));

 JSSubstitution gamma = new JSSubstitution();
 gamma.addBinding("x", new JSTerm("1"));
 gamma.addBinding("y", new JSTerm("2"));
 gamma.addBinding("z", new JSTerm("3"));
 gamma.addBinding("w", new JSTerm("4"));

 JSSubstitution beta1 = pf1.matches(pf2, gamma);
 assertEquals("Matches: size of beta1 = 4", 4, beta1.size());
 assertEquals("Matches: beta1[0] = x -> 1", "1", beta1.getBinding("x").toString());
 assertEquals("Matches: beta1[1] = y -> 2", "2", beta1.getBinding("y").toString());
 assertEquals("Matches: beta1[2] = z -> 3", "3", beta1.getBinding("z").toString());
 assertEquals("Matches: beta1[3] = w -> 4", "4", beta1.getBinding("w").toString());
 }

 @Test
 public void testEquals() {
 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 JSPredicateForm pf3 = new JSPredicateForm();
 pf3.addElement("not");
 pf3.addElement(new JSTerm("x"));
 pf3.addElement(new JSTerm("y"));
 pf3.addElement("or");
 pf3.addElement(new JSTerm("z"));
 pf3.addElement(new JSTerm("w"));

 assertFalse("Equals: pf1 != pf3", pf1.equals(pf3));

 pf3 = new JSPredicateForm();
 pf3.addElement("not");
 pf3.addElement(new JSTerm("x"));
 pf3.addElement(new JSTerm("y"));
 pf3.addElement("and");
 pf3.addElement(new JSTerm("z"));
 pf3.addElement(new JSTerm("w"));

 assertTrue("Equals: pf1 == pf3", pf1.equals(pf3));

 pf4 = new JSPredicateForm();
 pf4.addElement("not");
 pf4.addElement(new JSTerm("x"));
 pf4.addElement(new JSTerm("y"));
 pf4.addElement("and");
 pf4.addElement(new JSTerm("z"));
 pf4.addElement(new JSTerm("w"));

 pf4.set(0, "not2");
 assertFalse("Equals: pf1 != pf4", pf1.equals(pf4));

 pf4.set(1, new JSTerm("a"));
 assertFalse("Equals: pf1 != pf4", pf1.equals(pf4));

 pf4.set(1, new JSTerm("y"));
 assertTrue("Equals: pf1 == pf4", pf1.equals(pf4));

 pf4.remove(1);
 assertFalse("Equals: pf1 != pf4", pf1.equals(pf4));
 }

 @Test
 public void testStandarizerPredicateForm() {
 pf1 = new JSPredicateForm();
 pf1.addElement("not");
 pf1.addElement(new JSTerm("x"));
 pf1.addElement(new JSTerm("y"));
 pf1.addElement("and");
 pf1.addElement(new JSTerm("z"));
 pf1.addElement(new JSTerm("w"));

 JSPredicateForm pfSPF1 = pf1.standarizerPredicateForm();

 String str1 = pf1.toString();
 String str2 = "(\\(not (not x) (and z w)))";
 assertEquals("StandarizerPredicateForm: " + str1, str2, str1);
 assertEquals("StandarizerPredicateForm: size of pf1 = 6 and pfSPF1 = 4", 6, pf1.size());
 assertEquals("StandarizerPredicateForm: size of pfSPF1 = 4", 4, pfSPF1.size());
 }
}
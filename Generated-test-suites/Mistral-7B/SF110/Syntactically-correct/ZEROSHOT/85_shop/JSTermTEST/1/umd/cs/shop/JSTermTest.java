import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

@Test
public void testJSTermConstructor() {
JSTerm term = new JSTerm();
assertNull(term.getIsVariable());
assertFalse(term.isConstant());
assertFalse(term.isFunction());
assertFalse(term.isEval());

term = new JSTerm(new StreamTokenizer(new StringReader("(term1)")));
assertTrue(term.isFunction());
assertFalse(term.isVariable());
assertFalse(term.isConstant());
assertFalse(term.isEval());
assertEquals("term1", term.elementAt(0).toString());

term = new JSTerm(new StreamTokenizer(new StringReader("(call term1)")));
assertTrue(term.isEval());
assertTrue(term.isFunction());
assertEquals("call", term.elementAt(0).toString());
assertEquals(1, term.size());
assertEquals("term1", term.elementAt(1).toString());
}

@Test
public void testJSTermAddElement() {
JSTerm term = new JSTerm();
term.addElement("variable1");
assertTrue(term.isVariable());
assertEquals("variable1", term.elementAt(0).toString());
assertEquals(1, term.size());

term.addElement(new Double(1.0));
assertFalse(term.isVariable());
assertFalse(term.isConstant());
assertEquals("1.0", term.elementAt(1).toString());
assertEquals(2, term.size());

term.addElement("constant1");
assertTrue(term.isConstant());
assertEquals("constant1", term.elementAt(2).toString());
assertEquals(3, term.size());
}

@Test
public void testJSTermParseList() {
final String input = "(list term1 term2 term3)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertEquals(3, term.size());
assertEquals("term1", term.elementAt(1).toString());
assertEquals("term2", term.elementAt(2).toString());
assertEquals("term3", term.elementAt(3).toString());
}

@Test
public void testJSTermPrint() {
final String input = "(term1 term2 term3)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
term.print();
assertEquals("term1 term2 term3)", term.toStr().toString());
}

@Test
public void testJSTermCloneT() {
final String input = "(term1 term2 term3)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
JSTerm clonedTerm = term.cloneT();
assertEquals(3, clonedTerm.size());
assertEquals("term1", clonedTerm.elementAt(1).toString());
assertEquals("term2", clonedTerm.elementAt(2).toString());
assertEquals("term3", clonedTerm.elementAt(3).toString());
}

@Test
public void testJSTermApplySubstitutionT() {
final String input = "(term1 variable1 term2)";
JSTerm term = new JSTerm();
JSSubstitution substitution = new JSSubstitution();
substitution.addElement(new JSPairVarTerm("term1", "term3"));
term.parseList(new StreamTokenizer(new StringReader(input)));
JSTerm result = term.applySubstitutionT(substitution);
assertEquals("term3 term2", result.toStr().toString());
}

@Test
public void testJSTermMatches() {
final String input = "(term1 variable1 term2)";
JSTerm term1 = new JSTerm();
JSTerm term2 = new JSTerm();
JSSubstitution substitution = new JSSubstitution();
substitution.addElement(new JSPairVarTerm("term1", "term3"));
term1.parseList(new StreamTokenizer(new StringReader(input)));
term2.parseList(new StreamTokenizer(new StringReader("(term3 term2 term4)")));
JSSubstitution result = term1.matches(term2);
assertTrue(result.isEmpty());

input = "(term1 variable1 term2)";
term1.parseList(new StreamTokenizer(new StringReader(input)));
term2.parseList(new StreamTokenizer(new StringReader("(term1 term3 term4)")));
result = term1.matches(term2);
assertEquals(1, result.size());
assertEquals(new JSPairVarTerm("variable1", "term3"), result.elementAt(0));
}

@Test
public void testJSTermEquals() {
final String input1 = "(term1 variable1 term2)";
final String input2 = "(term1 variable2 term2)";
JSTerm term1 = new JSTerm();
JSTerm term2 = new JSTerm();
term1.parseList(new StreamTokenizer(new StringReader(input1)));
term2.parseList(new StreamTokenizer(new StringReader(input2)));
assertFalse(term1.equals(term2));

input1 = "(term1 variable1 term2)";
input2 = "(term1 variable1 term2)";
term1.parseList(new StreamTokenizer(new StringReader(input1)));
term2.parseList(new StreamTokenizer(new StringReader(input2)));
assertTrue(term1.equals(term2));
}

@Test
public void testJSTermIsVariable() {
final String input = "(variable1)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertTrue(term.isVariable());
}

@Test
public void testJSTermIsConstant() {
final String input = "(1.0)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertTrue(term.isConstant());
}

@Test
public void testJSTermIsFunction() {
final String input = "(term1)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertFalse(term.isFunction());
}

@Test
public void testJSTermIsEval() {
final String input = "(call term1)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertFalse(term.isEval());
}

@Test
public void testJSTermMakeFunction() {
final String input = "(term1)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
term.makeFunction();
assertTrue(term.isFunction());
}

@Test
public void testJSTermMakeVariable() {
final String input = "(term1)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
term.makeVariable();
assertTrue(term.isVariable());
}

@Test
public void testJSTermMakeConstant() {
final String input = "(1.0)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
term.makeConstant();
assertTrue(term.isConstant());
}

@Test
public void testJSTermMakeEval() {
final String input = "(call term1)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
term.makeEval(true);
assertTrue(term.isEval());
}

@Test
public void testJSTermToString() {
final String input = "(term1 term2 term3)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertEquals("term1 term2 term3)", term.toStr().toString());
}

@Test
public void testJSTermIsGround() {
final String input = "(term1 variable1 term2)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
assertFalse(term.isGround());

input = "(term1 term2 term3)";
term.parseList(new StreamTokenizer(new StringReader(input)));
assertTrue(term.isGround());
}

@Test
public void testJSTermStandardizerTerm() {
final String input = "(term1 variable1 term2)";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
JSTerm result = term.standardizerTerm();
assertEquals("term1_1 term2", result.toStr().toString());
}

@Test
public void testJSTermCall() {
final String input = "(not (= term1 term2))";
JSTerm term = new JSTerm();
term.parseList(new StreamTokenizer(new StringReader(input)));
JSTerm result = term.call();
assertEquals("not (= term1 term2)", result.toStr().toString());
}
}
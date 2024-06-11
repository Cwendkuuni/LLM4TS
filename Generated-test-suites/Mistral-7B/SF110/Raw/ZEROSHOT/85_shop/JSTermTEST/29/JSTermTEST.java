import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

 private StreamTokenizer tokenizer;
 private JSTerm term;

 @Test
 public void testEmptyTerm() {
 term = new JSTerm();
 assertTrue(term.isEmpty());
 }

 @Test
 public void testVariableTerm() {
 term = new JSTerm(new StringReader("x"));
 tokenizer = new StreamTokenizer(new StringReader("x"));
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("x");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testConstantTerm() {
 term = new JSTerm(new StringReader("1"));
 tokenizer = new StreamTokenizer(new StringReader("1"));
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("1");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListTerm() {
 term = new JSTerm(new StringReader("(+ 1 2)"));
 tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("+");
 expected.add(1);
 expected.add(2);
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListVariableTerm() {
 term = new JSTerm(new StringReader("(list x y z)"));
 tokenizer = new StreamTokenizer(new StringReader("(list x y z)"));
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("list");
 expected.add("x");
 expected.add("y");
 expected.add("z");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionTerm() {
 term = new JSTerm(new StringReader("(call f x y)"));
 tokenizer = new StreamTokenizer(new StringReader("(call f x y)"));
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("f");
 expected.add(1);
 expected.add(2);
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionCallTermWithEmptyList() {
 term = new JSTerm(new StringReader("(call f)"));
 tokenizer = new StreamTokenizer(new StringReader("(call f)"));
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("f");
 expected.add("nil");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionCallTermWithOneElementList() {
 term = new JSTerm(new StringReader("(call f (x))"));
 tokenizer = new StringReader("(call f (x))");
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("f");
 expected.add(1);
 expected.add("x");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionCallTermWithTwoElementsList() {
 term = new JSTerm(new StringReader("(call f (x y))"));
 tokenizer = new StringReader("(call f (x y))");
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("f");
 expected.add(1);
 expected.add("x");
 expected.add("y");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionCallTermWithThreeOrMoreElementsList() {
 term = new JSTerm(new StringReader("(call f (x y z))"));
 tokenizer = new StringReader("(call f (x y z))");
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("f");
 expected.add(1);
 expected.add("x");
 expected.add("y");
 expected.add("z");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionCallTermWithFunctionInList() {
 term = new JSTerm(new StringReader("(call (list f g) x)"));
 tokenizer = new StringReader("(call (list f g) x)");
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("list");
 expected.add("f");
 expected.add("g");
 expected.add(1);
 expected.add("x");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testListFunctionCallTermWithNestedLists() {
 term = new JSTerm(new StringReader("(call (list f (list x y)))"));
 tokenizer = new StringReader("(call (list f (list x y)))");
 term.parse(tokenizer);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("list");
 expected.add("f");
 expected.add(1);
 expected.add("list");
 expected.add("x");
 expected.add("y");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplySubstitution() {
 term = new JSTerm(new StringReader("(x)"));
 tokenizer = new StringReader("(x)");
 term.parse(tokenizer);
 JSSubstitution substitution = new JSSubstitution();
 substitution.put("x", new JSTerm(new StringReader("y")));
 term.applySubstitution(substitution);
 List<Object> expected = new ArrayList<>();
 expected.add("y");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplySubstitutionWithComplexTerm() {
 term = new JSTerm(new StringReader("(call f (x y))"));
 tokenizer = new StringReader("(call f (x y))");
 term.parse(tokenizer);
 JSSubstitution substitution = new JSSubstitution();
 substitution.put("x", new JSTerm(new StringReader("y")));
 substitution.put("y", new JSTerm(new StringReader("z")));
 term.applySubstitution(substitution);
 List<Object> expected = new ArrayList<>();
 expected.add("call");
 expected.add("f");
 expected.add(1);
 expected.add("z");
 List<Object> actual = term.getElements();
 assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 term = new JSTerm(new StringReader("(x)"));
 tokenizer = new StringReader("(x)");
 term.parse(tokenizer);
 JSTerm otherTerm = new JSTerm(new StringReader("(y)"));
 tokenizer = new StringReader("(y)");
 otherTerm.parse(tokenizer);
 List<Object> beta = term.matches(otherTerm).getSubstitution();
 List<Object> expected = new ArrayList<>();
 expected.add(new JSPairTerm("x", "y"));
 assertEquals(expected, beta.getElements());
 }

 @Test
 public void testMatchesWithEmptyTerms() {
 term = new JSTerm(new StringReader("()"));
 tokenizer = new StringReader("()");
 term.parse(tokenizer);
 JSTerm otherTerm = new JSTerm(new StringReader("()"));
 tokenizer = new StringReader("()");
 otherTerm.parse(tokenizer);
 List<Object> beta = term.matches(otherTerm).getSubstitution();
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithDifferentTerms() {
 term = new JSTerm(new StringReader("(x)"));
 tokenizer = new StringReader("(x)");
 term.parse(tokenizer);
 JSTerm otherTerm = new JSTerm(new StringReader("(y)"));
 tokenizer = new StringReader("(y)");
 otherTerm.parse(tokenizer);
 List<Object> beta = term.matches(otherTerm);
 assertTrue(beta.isFail());
 }

 @Test
 public void testMatchesWithVariables() {
 term = new JSTerm(new StringReader("(x)"));
 tokenizer = new StringReader("(x)");
 term.parse(tokenizer);
 JSTerm otherTerm = new JSTerm(new StringReader("(y)"));
 tokenizer = new StringReader("(y)");
 otherTerm.parse(tokenizer);
 JSSubstitution substitution = new JSSubstitution();
 substitution.put("x", otherTerm);
 term.applySubstitution(substitution);
 assertEquals(otherTerm, term);
 }

 @Test
 public void testMatchesWithComplexTerms() {
 term = new JSTerm(new StringReader("(call f (x y))"));
 tokenizer = new StringReader("(call f (x y))");
 term.parse(tokenizer);
 JSTerm otherTerm1 = new JSTerm(new StringReader("(call g (x z))"));
 tokenizer = new StringReader("(call g (x z))");
 otherTerm1.parse(tokenizer);
 JSTerm otherTerm2 = new JSTerm(new StringReader("(call h (x y z))"));
 tokenizer = new StringReader("(call h (x y z))");
 otherTerm2.parse(tokenizer);
 JSSubstitution substitution = new JSSubstitution();
 substitution.put("f", otherTerm1);
 substitution.put("x", new JSTerm(new StringReader("a")));
 substitution.put("y", new JSTerm(new StringReader("b")));
 substitution.put("z", new JSTerm(new StringReader("c")));
 term.applySubstitution(substitution);
 assertEquals(otherTerm2, term);
 }

}
package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

 private StreamTokenizer tokenizer;
 private ByteArrayOutputStream outputStream;
 private InputStream inputStream;

 @Before
 public void setUp() {
 tokenizer = new StreamTokenizer(new ByteArrayInputStream(new byte[]{}));
 outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 }

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 JSUtil.println("Empty term: " + term.toStr());
 assertTrue(term.isGround());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.readWord(tokenizer, "x");
 JSUtil.println("Variable term: " + term.toStr());
 assertFalse(term.isGround());
 assertEquals("x", term.elementAt(0).toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.readWord(tokenizer, "pi");
 JSUtil.println("Constant term: " + term.toStr());
 assertTrue(term.isGround());
 assertEquals("pi", term.elementAt(0).toString());
 }

 @Test
 public void testTermWithFunction() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.readToken(tokenizer, "{");
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "}");
 JSUtil.println("Term with function: " + term.toStr());
 assertFalse(term.isGround());
 assertEquals("add", term.elementAt(0).toString());
 assertEquals(2, term.size());
 }

 @Test
 public void testTermWithList() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.readToken(tokenizer, "{");
 JSUtil.readToken(tokenizer, "call");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "list");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "1");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "}");
 JSUtil.println("Term with list: " + term.toStr());
 assertFalse(term.isGround());
 assertEquals("call", term.elementAt(0).toString());
 assertEquals(3, term.size());
 }

 @Test
 public void testErrorHandling() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.expectTokenType(-3, tokenizer, "Expecting Variable name in term");
 tokenizer.nextToken();
 JSUtil.println("Error handling: " + term.toStr());
 assertNull(term);
 }

 @Test
 public void testTermPrinting() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "}");
 JSUtil.println("Print term: " + term);
 assertEquals("(add 2 3)", outputStream.toString());
 }

 @Test
 public void testTermCloning() {
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.reset();
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUTerm clonedTerm = term.cloneT();
 JSUtil.println("Cloned term: " + clonedTerm.toStr());
 assertEquals("(add 2 3)", clonedTerm.toStr());
 }

 @Test
 public void testTermSubstitution() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "x");
 JSUtil.println("Term before substitution: " + term.toStr());
 term = term.applySubstitutionT(new JSSubstitution());
 JSUtil.println("Term after substitution: " + term.toStr());
 assertNull(term);
 }

 @Test
 public void testTermMatches() {
 JSTerm term1 = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x x}");
 JSSTerm term2 = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x x}");
 JSSubstitution beta = term1.matches(term2);
 JSUtil.println("Matches result: " + beta.toString());
 assertTrue(beta.size() > 0);
 }

 @Test
 public void testTermEquals() {
 JSTerm term1 = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x x}");
 JSTerm term2 = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x x}");
 assertTrue(term1.equals(term2));
 }

 @Test
 public void testTermNotEquals() {
 JSTerm term1 = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x x}");
 JSTerm term2 = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x y}");
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testTermSize() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "}");
 assertEquals(3, term.size());
 }

 @Test
 public void testTermIsGround() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "pi");
 assertTrue(term.isGround());
 term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "x");
 assertFalse(term.isGround());
 term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "{x x}");
 assertFalse(term.isGround());
 }

 @Test
 public void testTermIsVariable() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "pi");
 assertFalse(term.isVariable());
 term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "x");
 assertTrue(term.isVariable());
 }

 @Test
 public void testTermIsConstant() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "pi");
 assertFalse(term.isConstant());
 term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "pi");
 assertTrue(term.isConstant());
 }

 @Test
 public void testTermIsFunction() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "pi");
 assertFalse(term.isFunction());
 term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "add");
 assertTrue(term.isFunction());
 }

 @Test
 public void testTermIsEval() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "pi");
 assertFalse(term.isEval());
 term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "call");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, ")");
 assertTrue(term.isEval());
 }

 @Test
 public void testTermStandardizer() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "}");
 JSTerm standardizedTerm = term.standardizerTerm();
 JSUtil.println("Standardized term: " + standardizedTerm.toStr());
 assertEquals("(add 2 3)", standardizedTerm.toStr());
 }

 @Test
 public void testTermCall() {
 JSTerm term = new JSTerm(tokenizer);
 JSUtil.readToken(tokenizer, "add");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "2");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "(");
 JSUtil.readToken(tokenizer, "3");
 JSUtil.readToken(tokenizer, ")");
 JSUtil.readToken(tokenizer, "}");
 JSTerm result = term.call();
 JSUtil.println("Term call result: " + result.toStr());
 assertEquals("5", result.toStr());
 }

 @After
 public void tearDown() {
 System.setOut(System.out);
 }

}
package umd.cs.shop;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

public class JSTermTest {

 private static final String JS_CODE = "(% + x 2)"; // valid term
 private static final String JS_CODE_VAR = "(x + x)"; // valid term with variable
 private static final String JS_CODE_LIST = "(list . (list 1 2))"; // valid term with list
 private static final String JS_CODE_INVALID_SYNTAX = "(list . 3)"; // invalid syntax term
 private static final String JS_CODE_VAR_NOT_DEFINED = "(% x)"; // variable not defined term

 @Test
 public void testValidTerm() {
 JSTerm term = parseJSCode(JS_CODE);
 assertEquals(JS_CODE, term.toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = parseJSCode(JS_CODE_VAR);
 term.makeVariable();
 assertEquals("x", term.elementAt(0).toString());
 }

 @Test
 public void testListTerm() {
 JSTerm term = parseJSCode(JS_CODE_LIST);
 assertEquals("list", term.elementAt(0).toString());
 assertEquals(2, term.size());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidSyntax() {
 JSTerm term = parseJSCode(JS_CODE_INVALID_SYNTAX);
 fail("Invalid syntax term should not be parsed");
 }

 @Test
 public void testVariableNotDefined() {
 JSTerm term = parseJSCode(JS_CODE_VAR_NOT_DEFINED);
 assertFalse(term.isVariable());
 }

 private JSTerm parseJSCode(final String code) {
 StringReader reader = new StringReader(code);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 try {
 return new JSTerm(tokenizer);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to parse JS code: " + code);
 }
 }

}
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

  @Test
  public void testJSTermConstruction() throws Exception {
    // Test empty constructor
    JSTerm jsterm = new JSTerm();
    assertNull(jsterm.elementAt(0));
    assertEquals(0, jsterm.size());

    // Test constructor with StreamTokenizer
    StringReader reader = new StringReader(
        "(% x)\n" +
        "(% y)\n" +
        "(call (list x y) 2)"
    );
    StreamTokenizer tokenizer = new StreamTokenizer(reader);
    JSTerm constructedJSTerm = new JSTerm(tokenizer);

    List<String> expectedElements = new ArrayList<>();
    expectedElements.add("call");
    expectedElements.add("list");
    expectedElements.add("x");
    expectedElements.add("y");
    expectedElements.add("2");

    for (int i = 0; i < constructedJSTerm.size(); i++) {
      assertEquals(expectedElements.get(i), constructedJSTerm.elementAt(i).toString());
    }
  }

  @Test
  public void testJSTermIsVariable() {
    JSTerm jsterm = new JSTerm();
    jsterm.addElement("x");
    jsterm.makeVariable();
    assertTrue(jsterm.isVariable());
    assertFalse(jsterm.isConstant());
  }

  @Test
  public void testJSTermIsConstant() {
    JSTerm jsterm = new JSTerm();
    jsterm.addElement("10");
    jsterm.makeConstant();
    assertTrue(jsterm.isConstant());
    assertFalse(jsterm.isVariable());
  }

  @Test
  public void testJSTermIsFunction() {
    JSTerm jsterm = new JSTerm();
    jsterm.addElement("x");
    jsterm.makeFunction();
    assertTrue(jsterm.isFunction());
    assertFalse(jsterm.isVariable());
    assertFalse(jsterm.isConstant());
  }

  @Test
  public void testJSTermIsEval() {
    JSTerm jsterm = new JSTerm();
    jsterm.makeEval(true);
    assertTrue(jsterm.isEval());
  }

  @Test
  public void testJSTermPrint() {
    JSTerm jsterm = new JSTerm();
    jsterm.addElement("x");
    jsterm.addElement("y");
    jsterm.makeFunction();
    jsterm.print();

    String expectedOutput = "(\n" +
        "  call\n" +
        "  list\n" +
        "  x\n" +
        "  y\n" +
        ")";

    JSUtil jsu = new JSUtil();
    String actualOutput = jsu.readFromStdout();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testCloneT() {
    JSTerm jsterm = new JSTerm();
    jsterm.addElement("x");
    jsterm.makeVariable();

    JSTerm clonedJSTerm = jsterm.cloneT();

    assertEquals(jsterm.toString(), clonedJSTerm.toString());
  }

  @Test
  public void testApplySubstitutionT() {
    JSTerm jsterm = new JSTerm();
    jsterm.addElement("x");
    jsterm.makeVariable();

    JSSubstitution substitution = new JSSubstitution();
    JSTerm substitutedTerm = jsterm.applySubstitutionT(substitution);

    assertEquals(substitution.size(), substitutedTerm.size());

    JSTerm expectedFirstElement = new JSTerm();
    expectedFirstElement.addElement("x");
    expectedFirstElement.makeVariable();

    JSTerm actualFirstElement = substitutedTerm.elementAt(0);
    assertEquals(expectedFirstElement.toString(), actualFirstElement.toString());

    JSTerm expectedSecondElement = new JSTerm();
    expectedSecondElement.addElement("?x");
    expectedSecondElement.makeVariable();

    JSTerm actualSecondElement = substitutedTerm.elementAt(1);
    assertEquals(expectedSecondElement.toString(), actualSecondElement.toString());
  }

  @Test
  public void testMatches() {
    JSTerm term1 = new JSTerm();
    term1.addElement("x");
    term1.makeVariable();

    JSTerm term2 = new JSTerm();
    term2.addElement("x");
    term2.makeVariable();

    JSTerm term3 = new JSTerm();
    term3.addElement("y");
    term3.makeVariable();

    JSSubstitution substitution = new JSSubstitution();
    substitution.addElement(new JSPairVarTerm("x", "y"));

    JSTerm matchedTerm = term1.matches(term2);
    assertEquals(term1.toString(), matchedTerm.toString());

    JSTerm unmatchedTerm = term1.matches(term3);
    assertNull(unmatchedTerm);
  }

  @Test
  public void testMatchesWithSubstitution() {
    JSTerm term1 = new JSTerm();
    term1.addElement("x");
    term1.makeVariable();

    JSTerm term2 = new JSTerm();
    term2.addElement("y");
    term2.makeVariable();

    JSSubstitution substitution = new JSSubstitution();
    substitution.addElement(new JSPairVarTerm("x", "y"));

    JSTerm matchedTerm = term1.matches(term2, substitution);
    JSTerm expectedMatchedTerm = new JSTerm();
    expectedMatchedTerm.addElement("y");
    expectedMatchedTerm.makeVariable();

    assertEquals(expectedMatchedTerm.toString(), matchedTerm.toString());
  }

  @Test
  public void testEquals() {
    JSTerm term1 = new JSTerm();
    term1.addElement("x");
    term1.makeVariable();

    JSTerm term2 = new JSTerm();
    term2.addElement("x");
    term2.makeVariable();

    JSTerm term3 = new JSTerm();
    term3.addElement("y");
    term3.makeVariable();

    assertTrue(term1.equals(term2));
    assertFalse(term1.equals(term3));
  }

  @Test
  public void testStandardizerTerm() {
    JSTerm term = new JSTerm();
    term.addElement("x");
    term.makeVariable();
    term.addElement("y");
    term.makeVariable();
    term.addElement("z");
    term.makeVariable();
    term.makeFunction();

    JSTerm standardizedTerm = term.standardizerTerm();

    String expectedOutput = "(\n" +
        "  call\n" +
        "  list\n" +
        "  ?VarCounter\n" +
        "  ?VarCounter1\n" +
        "  ?VarCounter2\n" +
        ")";

    JSUtil jsu = new JSUtil();
    String actualOutput = jsu.readFromStdout();
    assertEquals(expectedOutput, actualOutput);
  }
}
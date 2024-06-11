import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test case for valid usage of goog.scope
    String sourceCode =
        "goog.scope(function() {" +
            "var a = goog.a; " +
            "goog.a = 1; " +
            "var b = goog.a; " +
            "assertEquals(1, b); " +
            "});";

    Compiler compiler = new Compiler();
    SourceFile input = new SourceFile("Test.js", sourceCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, input.getTree());

    Node tree = input.getTree();
    Node root = tree.getChildAtIndex(0);
    Node googScopeCall = root.getFirstChild();
    Node googScopeFunction = googScopeCall.getLastChild();

    // Check if the goog.scope function has only one parameter
    assertEquals(1, googScopeFunction.getChildCount());

    // Check if the body of the goog.scope function does not reference 'this'
    assertFalse(NodeUtil.contains(googScopeFunction, Token.THIS));

    // Check if the alias 'a' is defined inside the goog.scope block
    Node aAliasDefinition = null;
    for (Node child = googScopeFunction.getChildAtIndex(1).getFirstChild();
         child != null;
         child = child.getNext()) {
      if (child.isVar() && "a".equals(child.getFirstChild().getString())) {
        aAliasDefinition = child;
        break;
      }
    }
    assertNotNull(aAliasDefinition);

    // Check if the alias 'a' is used inside the goog.scope block
    Node aUsage = null;
    for (Node grandChild = googScopeFunction.getFirstChild();
         grandChild != null;
         grandChild = grandChild.getNext()) {
      if (grandChild.isGetProp() && "a".equals(grandChild.getQualifiedName())) {
        aUsage = grandChild;
        break;
      }
    }
    assertNotNull(aUsage);

    // Check if the alias 'a' is replaced by its definition inside the code
    assertEquals("1", aUsage.getString());

    // Test case for invalid usage of goog.scope
    sourceCode =
        "goog.scope(function() {" +
            "var a = goog.a; " +
            "goog.a = 1; " +
            "goog.scope(function() {" +
            "var a = goog.a; " +
            "assertEquals(1, a); " +
            "}); " +
            "});";

    input = new SourceFile("Test.js", sourceCode);
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, input.getTree());

    tree = input.getTree();
    root = tree.getChildAtIndex(0);
    Node firstGoogScopeCall = root.getFirstChild();
    Node firstGoogScopeFunction = firstGoogScopeCall.getLastChild();

    // Check if the first goog.scope function has only one parameter
    assertEquals(1, firstGoogScopeFunction.getChildCount());

    Node secondGoogScopeCall = firstGoogScopeFunction.getLastChild();
    Node secondGoogScopeFunction = secondGoogScopeCall.getLastChild();

    // Check if the second goog.scope function has only one parameter
    assertEquals(1, secondGoogScopeFunction.getChildCount());

    // Check if the body of the second goog.scope function does not reference 'this'
    assertFalse(NodeUtil.contains(secondGoogScopeFunction, Token.THIS));

    // Check if the alias 'a' is defined inside the first goog.scope block
    Node aAliasDefinitionInFirstScope = null;
    for (Node child = firstGoogScopeFunction.getChildAtIndex(1).getFirstChild();
         child != null;
         child = child.getNext()) {
      if (child.isVar() && "a".equals(child.getFirstChild().getString())) {
        aAliasDefinitionInFirstScope = child;
        break;
      }
    }
    assertNotNull(aAliasDefinitionInFirstScope);

    // Check if the alias 'a' is used inside the first goog.scope block
    Node aUsageInFirstScope = null;
    for (Node grandChild = firstGoogScopeFunction.getChildAtIndex(0).getFirstChild();
         grandChild != null;
         grandChild = grandChild.getNext()) {
      if (grandChild.isGetProp() && "a".equals(grandChild.getQualifiedName())) {
        aUsageInFirstScope = grandChild;
        break;
      }
    }
    assertNotNull(aUsageInFirstScope);

    // Check if the alias 'a' is defined inside the second goog.scope block
    Node aAliasDefinitionInSecondScope = null;
    for (Node child = secondGoogScopeFunction.getChildAtIndex(1).getFirstChild();
         child != null;
         child = child.getNext()) {
      if (child.isVar() && "a".equals(child.getFirstChild().getString())) {
        aAliasDefinitionInSecondScope = child;
        break;
      }
    }
    assertNotNull(aAliasDefinitionInSecondScope);

    // Check if the alias 'a' is used inside the second goog.scope block
    Node aUsageInSecondScope = null;
    for (Node grandChild = secondGoogScopeFunction.getChildAtIndex(0).getFirstChild();
         grandChild != null;
         grandChild = grandChild.getNext()) {
      if (grandChild.isGetProp() && "a".equals(grandChild.getQualifiedName())) {
        aUsageInSecondScope = grandChild;
        break;
      }
    }
    assertNotNull(aUsageInSecondScope);

    // Check if the alias 'a' is replaced by its definition inside the first goog.scope block
    assertEquals("1", aUsageInFirstScope.getString());

    // Check if the alias 'a' is replaced by its definition inside the second goog.scope block
    assertEquals("1", aUsageInSecondScope.getString());

    // Check if there is an error when using 'return' inside a goog.scope block
    sourceCode =
        "goog.scope(function() {" +
            "return 1; " +
            "});";

    input = new SourceFile("Test.js", sourceCode);
    scopedAliases = new ScopedAliases(compiler, null, null);
    try {
      scopedAliases.process(null, input.getTree());
      fail("Expected an error when using 'return' inside a goog.scope block.");
    } catch (AssertionError e) {
      // Expected
    }

    // Check if there is an error when using 'throw' inside a goog.scope block
    sourceCode =
        "goog.scope(function() {" +
            "throw new Error('Test'); " +
            "});";

    input = new SourceFile("Test.js", sourceCode);
    scopedAliases = new ScopedAliases(compiler, null, null);
    try {
      scopedAliases.process(null, input.getTree());
      fail("Expected an error when using 'throw' inside a goog.scope block.");
    } catch (AssertionError e) {
      // Expected
    }
  }
}
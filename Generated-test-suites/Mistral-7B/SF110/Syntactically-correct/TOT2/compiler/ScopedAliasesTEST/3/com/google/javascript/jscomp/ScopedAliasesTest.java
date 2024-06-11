import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

import java.util.List;

/**
 * Test class for ScopedAliases.java
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test case for basic functionality
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    // Prepare test code
    String testCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});";

    Node root = Node.parse(testCode);

    // Run the compiler pass
    scopedAliases.process(null, root);

    // Check the output
    Node output = NodeUtil.getRootNode(root);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        output.toString());

    // Test case for edge cases: multiple scopes in one function
    String testCode2 =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    Node root2 = Node.parse(testCode2);

    scopedAliases.process(null, root2);

    Node output2 = NodeUtil.getRootNode(root2);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        output2.getFirstChild().toString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        output2.getLastChild().toString());

    // Test case for exception handling: illegal goog.scope usage
    String testCode3 =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "goog.scope(function() {" +
            "goog.scope(function() {" +
            "});" + // missing parameter
            "});" +
            "});" +
            "});";

    Node root3 = Node.parse(testCode3);

    try {
      scopedAliases.process(null, root3);
      fail("Expected compilation error");
    } catch (AssertionError e) {
      // The compiler should have thrown an error
    }

    // Test case for error handling: alias redefinition
    String testCode4 =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "var DIV = 42;" +
            "});" +
            "dom.createElement(DIV);" +
            "});";

    Node root4 = Node.parse(testCode4);

    try {
      scopedAliases.process(null, root4);
      fail("Expected compilation error");
    } catch (AssertionError e) {
      String errorMessage = e.getCause().getMessage();
      assertTrue(errorMessage.startsWith("The alias DIV is assigned a value"));
    }

    // Test case for error handling: non-alias local variable
    String testCode5 =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "var div = document.createElement(DIV);" +
            "});";

    Node root5 = Node.parse(testCode5);

    try {
      scopedAliases.process(null, root5);
      fail("Expected compilation error");
    } catch (AssertionError e) {
      String errorMessage = e.getCause().getMessage();
      assertTrue(errorMessage.startsWith("The local variable div is in a goog.scope"));
    }
  }
}
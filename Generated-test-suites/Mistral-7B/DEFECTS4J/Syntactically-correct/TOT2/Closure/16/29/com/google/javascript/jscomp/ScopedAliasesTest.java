import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Test;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {

  @Test
  public void testValidScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    var x = goog.x;\n" +
        "    goog.scope(function() {\n" +
        "      var y = goog.y;\n" +
        "      var z = goog.z;\n" +
        "    });\n" +
        "    goog.scope(function() {\n" +
        "      var x = 1;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();
    assertEmpty(diagnostics.getErrors());
    assertEmpty(diagnostics.getWarnings());

    assertEquals("1", traversal.getRoot().getFirstChild().toString());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopedAliasesUsesThis() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(function() {\n" +
        "      this.x = 1;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.getErrors().get(0).getType());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopedAliasesUsesReturn() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static int test() {\n" +
        "    goog.scope(function() {\n" +
        "      return 1;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.getErrors().get(0).getType());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopedAliasesUsesThrow() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(function() {\n" +
        "      throw new Error('Test error');\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesMultipleAliasDefinitions() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(function() {\n" +
        "      var x = goog.x;\n" +
        "      var x = 1;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesNonAliasLocalVariable() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(function() {\n" +
        "      var x = 1;\n" +
        "    });\n" +
        "    var x = goog.x;\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesInvalidScopeCall() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(1);\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasesInvalidScopeCallWithParameters() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test(int x) {\n" +
        "    goog.scope(x);\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();

    assertEquals(1, diagnostics.getErrors().size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.getErrors().get(0).getType());
  }

  @Test
  public void testValidScopedAliasesWithAliasUsages() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(function() {\n" +
        "      var x = goog.x;\n" +
        "      var y = goog.y;\n" +
        "      var z = goog.z;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();
    assertEmpty(diagnostics.getErrors());
    assertEmpty(diagnostics.getWarnings());

    Node root = traversal.getRoot();
    Node test = root.getFirstChild();
    Node testMethod = test.getFirstChild();

    Node body = testMethod.getLastChild();
    Node scopeCall = body.getFirstChild();

    assertEquals("function() {", scopeCall.toString());
    assertEquals("}", scopeCall.getLastChild().toString());

    Node scopeBody = scopeCall.getLastChild().getLastChild();

    Node x = NodeOperations.getFirstDescendantByName(
        scopeBody, "x");
    assertEquals("x", x.getName());

    Node y = NodeOperations.getFirstDescendantByName(
        scopeBody, "y");
    assertEquals("y", y.getName());

    Node z = NodeOperations.getFirstDescendantByName(
        scopeBody, "z");
    assertEquals("z", z.getName());

    assertEquals("undefined", x.getFirstChild().toString());
    assertEquals("undefined", y.getFirstChild().toString());
    assertEquals("undefined", z.getFirstChild().toString());

    assertEquals(1, root.getChildCount());
    assertEquals(1, root.getChildAtIndex(0).getChildCount());
    assertEquals(1, root.getChildAtIndex(0).getChildAtIndex(0).getChildCount());
  }

  @Test
  public void testValidScopedAliasesWithAliasUsagesAndTypeNodes() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_RENAME_ALIASES);
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    AbstractCompiler compiler = new AbstractCompiler(options, null, null);
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void test() {\n" +
        "    goog.scope(function() {\n" +
        "      var x = goog.x;\n" +
        "      var y = goog.y;\n" +
        "      var z = goog.z;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, sourceFile.getRootNode(), traversal);

    Diagnostics diagnostics = traversal.getDiagnostics();
    assertEmpty(diagnostics.getErrors());
    assertEmpty(diagnostics.getWarnings());

    Node root = traversal.getRoot();
    Node test = root.getFirstChild();
    Node testMethod = test.getFirstChild();

    Node body = testMethod.getLastChild();
    Node scopeCall = body.getFirstChild();

    Node scopeBody = scopeCall.getLastChild().getLastChild();

    Node x = NodeOperations.getFirstDescendantByName(
        scopeBody, "x");
    assertEquals("x", x.getName());

    Node y = NodeOperations.getFirstDescendantByName(
        scopeBody, "y");
    assertEquals("y", y.getName());

    Node z = NodeOperations.getFirstDescendantByName(
        scopeBody, "z");
    assertEquals("z", z.getName());

    Node xTypeNode = NodeOperations.getFirstDescendantByName(
        scopeBody, "x", Node.TYPE);
    assertEquals("Number", xTypeNode.getString());

    Node yTypeNode = NodeOperations.getFirstDescendantByName(
        scopeBody, "y", Node.TYPE);
    assertEquals("String", yTypeNode.getString());

    Node zTypeNode = NodeOperations.getFirstDescendantByName(
        scopeBody, "z", Node.TYPE);
    assertEquals("Boolean", zTypeNode.getString());

    assertEquals("undefined", x.getFirstChild().toString());
    assertEquals("undefined", y.getFirstChild().toString());
    assertEquals("undefined", z.getFirstChild().toString());

    assertEquals(1, root.getChildCount());
    assertEquals(1, root.getChildAtIndex(0).getChildCount());
    assertEquals(1, root.getChildAtIndex(0).getChildAtIndex(0).getChildCount());
  }
}
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_REFERENCES_THIS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_RETURN;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_THROW;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Test;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    CompilerOptions.AliasTransformationHandler transformationHandler =
        new CompilerOptions.AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            System.out.println("Alias " + fileName + ":" + position + " transformed.");
            return new AliasTransformation() {};
          }
        };

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addPass(new HotSwapCompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new ScopedAliases(compiler, null, transformationHandler).process(externs, root);
      }

      @Override
      public void hotSwapScript(Node root, Node originalRoot) {
        // Not used in this test case
      }
    });

    String sourceCode =
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  function createElement() {\n" +
        "    return dom.createElement(DIV);\n" +
        "  }\n" +
        "  return createElement;\n" +
        "});";

    Node rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);

    // Check that the correct alias transformations were applied.
    Node functionNode = NodeUtil.findFunction(rootNode, "createElement");
    assertEquals(
        "goog.dom.createElement",
        functionNode.getQualifiedName());

    // Test bad goog.scope usage.
    sourceCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  function createElement() {\n" +
        "    return dom.createElement(dom.TagName.DIV);\n" +
        "  }\n" +
        "});";
    rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY,
        compiler.getDiagnostics()[0].getType());

    // Test goog.scope with incorrect parameters.
    sourceCode = "goog.scope(function() {\n" +
        "});";
    rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS,
        compiler.getDiagnostics()[0].getType());

    // Test goog.scope with "this" keyword.
    sourceCode = "goog.scope(function() {\n" +
        "  return this;\n" +
        "});";
    rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS,
        compiler.getDiagnostics()[0].getType());

    // Test goog.scope with "return" statement.
    sourceCode = "goog.scope(function() {\n" +
        "  return;\n" +
        "});";
    rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals(GOOG_SCOPE_USES_RETURN,
        compiler.getDiagnostics()[0].getType());

    // Test goog.scope with "throw" statement.
    sourceCode = "goog.scope(function() {\n" +
        "  throw new Error();\n" +
        "});";
    rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals(GOOG_SCOPE_USES_THROW,
        compiler.getDiagnostics()[0].getType());

    // Test alias redefinition.
    sourceCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var createElement = function() {\n" +
        "    return dom.createElement(DIV);\n" +
        "  };\n" +
        "  var createElement = function() {\n" +
        "    return 'Hello world';\n" +
        "  };\n" +
        "  return createElement;\n" +
        "});";
    rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED,
        compiler.getDiagnostics()[0].getType());
  }

  @Test
  public void testScopedAliasesWithJSDocs() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    CompilerOptions.AliasTransformationHandler transformationHandler =
        new CompilerOptions.AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            System.out.println("Alias " + fileName + ":" + position + " transformed.");
            return new AliasTransformation() {};
          }
        };

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addPass(new HotSwapCompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new ScopedAliases(compiler, null, transformationHandler).process(externs, root);
      }

      @Override
      public void hotSwapScript(Node root, Node originalRoot) {
        // Not used in this test case
      }
    });

    String sourceCode =
        "/**\n" +
        " * Some documentation.\n" +
        " */\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  function createElement(tagName) {\n" +
        "    return dom.createElement(tagName);\n" +
        "  }\n" +
        "  return createElement;\n" +
        "});";

    Node rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);

    // Check that the correct alias transformations were applied.
    Node functionNode = NodeUtil.findFunction(rootNode, "createElement");
    assertEquals(
        "goog.dom.createElement",
        functionNode.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithPrivateVariables() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    CompilerOptions.AliasTransformationHandler transformationHandler =
        new CompilerOptions.AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            System.out.println("Alias " + fileName + ":" + position + " transformed.");
            return new AliasTransformation() {};
          }
        };

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addPass(new HotSwapCompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new ScopedAliases(compiler, null, transformationHandler).process(externs, root);
      }

      @Override
      public void hotSwapScript(Node root, Node originalRoot) {
        // Not used in this test case
      }
    });

    String sourceCode =
        "goog.scope(function() {\n" +
        "  var privateVar = 1;\n" +
        "  function getPrivateVar() {\n" +
        "    return privateVar;\n" +
        "  }\n" +
        "  return getPrivateVar;\n" +
        "});";

    Node rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);

    // Check that the correct alias transformations were applied.
    Node functionNode = NodeUtil.findFunction(rootNode, "getPrivateVar");
    assertEquals(
        "goog.scope(function() {\n" +
        "  var privateVar = 1;\n" +
        "  return function() {\n" +
        "    return privateVar;\n" +
        "  };\n" +
        "})().getPrivateVar",
        functionNode.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithFunctionsAsArguments() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    CompilerOptions.AliasTransformationHandler transformationHandler =
        new CompilerOptions.AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            System.out.println("Alias " + fileName + ":" + position + " transformed.");
            return new AliasTransformation() {};
          }
        };

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addPass(new HotSwapCompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new ScopedAliases(compiler, null, transformationHandler).process(externs, root);
      }

      @Override
      public void hotSwapScript(Node root, Node originalRoot) {
        // Not used in this test case
      }
    });

    String sourceCode =
        "goog.scope(function() {\n" +
        "  function foo() {\n" +
        "    return 'Hello from foo';\n" +
        "  }\n" +
        "  function bar(func) {\n" +
        "    return func();\n" +
        "  }\n" +
        "  return bar;\n" +
        "});";

    Node rootNode = NodeUtil.parse(sourceCode);
    compiler.process(null, rootNode);

    // Check that the correct alias transformations were applied.
    Node functionNode = NodeUtil.findFunction(rootNode, "bar");
    assertEquals(
        "goog.scope(function() {\n" +
        "  function foo() {\n" +
        "    return 'Hello from foo';\n" +
        "  }\n" +
        "  return function(func) {\n" +
        "    return func();\n" +
        "  };\n" +
        "})().bar",
        functionNode.getQualifiedName());
  }
}
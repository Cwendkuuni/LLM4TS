import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    options.setOutputFile("out.js");
  }

  @Test
  public void testSimpleScopedAlias() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var x = goog.x;\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.getFirstChild().getString());
    assertEquals("goog.x", aliasUsage.getLastChild().getString());
  }

  @Test
  public void testScopedAliasWithArguments() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function(y) {\n" +
        "    goog.x = y;\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.getFirstChild().getString());
    assertEquals("goog.x", aliasUsage.getLastChild().getString());
  }

  @Test
  public void testScopedAliasWithNestedFunction() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var x = goog.x;\n" +
        "    function foo() {\n" +
        "      goog.scope(function() {\n" +
        "        goog.x = 'foo';\n" +
        "      });\n" +
        "      return x;\n" +
        "    }\n" +
        "    return foo();\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.getFirstChild().getString());
    assertEquals("goog.x", aliasUsage.getLastChild().getString());

    Node result = tree.getFirstChild();
    assertEquals("foo", result.getString());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithMultipleAssignments() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    goog.x = 1;\n" +
        "    goog.x = 2;\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_ALIAS_REDEFINED");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithNonAliasLocal() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithThis() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    this.x = 1;\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_REFERENCES_THIS");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithReturn() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    return 'hello';\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_USES_RETURN");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithThrow() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {\n" +
        "    throw new Error('error');\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_USES_THROW");
  }

  @Test
  public void testScopedAliasWithEmptyScope() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function() {});\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @Test
  public void testScopedAliasWithFunctionAsArgument() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  goog.scope(function(goog) {\n" +
        "    goog.scope(function() {\n" +
        "      goog.x = 1;\n" +
        "    });\n" +
        "  });\n" +
        "}");

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, null);
    Traversal traversal = new Traversal();
    Node tree = compiler.parse(input, options);

    scopedAliasesPass.process(null, tree);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.getFirstChild().getString());
    assertEquals("goog.x", aliasUsage.getLastChild().getString());
  }
}
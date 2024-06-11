import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_REFERENCES_THIS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_RETURN;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_THROW;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    String goog = \"google\";\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      dom.createElement(DIV);\n" +
        "    });\n" +
        "    assert(goog.dom != null);\n" +
        "  }\n" +
        "}\n");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(Lists.newArrayList(
        new AliasTransformation("goog.dom", "dom")));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = scopedAliases.process(null, sourceFile.getTree());

    // No errors reported during compilation
    assertTrue(compiler.hasErrors() == false);

    // Check if the alias is applied correctly
    Node googNode = root.lookupVariableInCurrentScope("goog");
    Node domNode = root.lookupVariableInCurrentScope("dom");
    assertNotNull(domNode);
    assertEquals(Node.OP_ASSIGN, domNode.getOp().getType());
    assertSame(googNode, domNode.getRightChild());

    // Test for improper use of goog.scope
    sourceFile = new SourceFile("Test2.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    goog.scope(function() {\n" +
        "      var a = 1;\n" +
        "      a = 2;\n" +
        "      goog.scope(function() {\n" +
        "        a = 3;\n" +
        "      });\n" +
        "    });\n" +
        "    assert(a == 3);\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // Check for GOOG_SCOPE_ALIAS_REDEFINED error
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error);

    // Test for non-alias local variable usage in goog.scope
    sourceFile = new SourceFile("Test3.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    goog.scope(function() {\n" +
        "      var x = 1;\n" +
        "    });\n" +
        "    assert(x == 1);\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // Check for GOOG_SCOPE_NON_ALIAS_LOCAL error
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().size());
    error = compiler.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, error);

    // Test for proper use of goog.scope with a single parameter
    sourceFile = new SourceFile("Test4.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    goog.scope(function(dom) {\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      dom.createElement(DIV);\n" +
        "    });\n" +
        "    assert(document.createElement instanceof Function);\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // No errors reported during compilation
    assertTrue(compiler.hasErrors() == false);

    // Test for improper use of 'this' inside goog.scope
    sourceFile = new SourceFile("Test5.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    goog.scope(function() {\n" +
        "      this.a = 1;\n" +
        "    });\n" +
        "    assert(this.a == null);\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // Check for GOOG_SCOPE_REFERENCES_THIS error
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().size());
    error = compiler.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, error);

    // Test for improper use of return inside goog.scope
    sourceFile = new SourceFile("Test6.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    goog.scope(function() {\n" +
        "      return true;\n" +
        "    });\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // Check for GOOG_SCOPE_USES_RETURN error
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().size());
    error = compiler.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_RETURN, error);

    // Test for improper use of throw inside goog.scope
    sourceFile = new SourceFile("Test7.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    try {\n" +
        "      goog.scope(function() {\n" +
        "        throw new Error(\"Test error\");\n" +
        "      });\n" +
        "    } catch (Error e) {\n" +
        "      assert(false);\n" +
        "    }\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // Check for GOOG_SCOPE_USES_THROW error
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().size());
    error = compiler.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_THROW, error);

    // Test for proper use of goog.scope with a function that references a
    // global variable
    sourceFile = new SourceFile("Test8.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    var x = 1;\n" +
        "    goog.scope(function(dom) {\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      dom.createElement(DIV).setAttribute(\"class\", x.toString());\n" +
        "    });\n" +
        "    assert(document.createElement.hasAttribute(\"class\", \"1\"));\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // No errors reported during compilation
    assertTrue(compiler.hasErrors() == false);

    // Test for proper use of goog.scope with a function that declares a
    // local variable and uses an alias
    sourceFile = new SourceFile("Test9.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  @SuppressWarnings(\"unused\")\n" +
        "  static void main() {\n" +
        "    goog.scope(function() {\n" +
        "      var x = 1;\n" +
        "      var y = goog.dom;\n" +
        "      y.createElement(goog.dom.TagName.DIV);\n" +
        "    });\n" +
        "    assert(document.createElement instanceof Function);\n" +
        "  }\n" +
        "}\n");

    root = scopedAliases.process(null, sourceFile.getTree());
    compiler.resetErrors();

    // No errors reported during compilation
    assertTrue(compiler.hasErrors() == false);
  }
}
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    options = new CompilerOptions();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testSimpleAlias() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  var goog = {\n" +
        "    dom: function() {\n" +
        "      return this.dom = this.dom || {};\n" +
        "    }.bind(goog),\n" +
        "    DIV: goog.dom.TagName.DIV\n" +
        "  };\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var div = dom.createElement(dom.DIV);\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    Node goog = root.getFirstChild();
    Node domain = goog.getChildAtIndex(1);
    Node tagName = domain.getChildAtIndex(1);
    Node tagNameDiv = tagName.getChildAtIndex(1);
    Node scopeCall = root.getLastChild();

    assertEquals(tagNameDiv, scopeCall.getChildAtIndex(1).getLastChild());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopeCall() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // This will fail as the goog.scope function does not take any arguments.
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testInvalidAliasDefinition() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    var goog = {\n" +
        "      dom: function() {\n" +
        "        return this.dom = this.dom || {};\n" +
        "      }.bind(goog),\n" +
        "      DIV: goog.dom.TagName.DIV,\n" +
        "      a: 5\n" +
        "    };\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // This will fail as the 'a' is not an alias.
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testAliasRedefined() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    var a = 1;\n" +
        "    goog.scope(function() {\n" +
        "      var a = 2;\n" +
        "    });\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // This will fail as 'a' is redefined.
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testThisInScope() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    this.test = 1;\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // This will fail as the 'this' keyword is used inside the scope.
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testReturnInScope() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    return 1;\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // This will fail as the return statement is used inside the scope.
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testThrowInScope() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    throw new Error();\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // This will fail as the throw statement is used inside the scope.
    scopedAliases.hotSwapScript(root, null);
  }

  @Test
  public void testHotSwap() {
    SourceFile source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    var a = 1;\n" +
        "  });\n" +
        "}");

    Node root = compiler.parse(source, options);
    scopedAliases.process(null, root);

    // The alias 'a' is not defined in the original code.
    assertNull(preprocessorSymbolTable.getVariable("a"));

    // Change the code to define 'a'.
    source = new SourceFile("Test.js", "function test() {\n" +
        "  goog.scope(function() {\n" +
        "    var a = 1;\n" +
        "  });\n" +
        "}");
    Node modifiedRoot = compiler.parse(source, options);
    scopedAliases.process(null, modifiedRoot);

    // The alias 'a' is now defined.
    assertEquals("a", preprocessorSymbolTable.getVariable("a").getName());
  }
}
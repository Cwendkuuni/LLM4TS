import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RenameVarsTest {
  private Compiler compiler;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("missing_var_decl", "QUIET");
    compiler = new Compiler(options);

    input = new CompilerInput("test.js", "test.js");

    String code = "function test() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = a + b;" +
        "  return c;" +
        "}";
    root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);
  }

  @Test
  public void testRenameVars() {
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "function test() {" +
        "  var aa = 1;" +
        "  var bb = 2;" +
        "  var cc = aa + bb;" +
        "  return cc;" +
        "}", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithExternals() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    // Add 'console' as an external.
    compiler.getGlobalObject().putProperty("console", new Node(Token.NAME, "console"));

    String code = "console.log(1 + 1);";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "console.log(1 + 1);", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithPreserveAnonymousFunctions() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, true, false, null, null, null);

    String code = "function test() {" +
        "  (function() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = a + b;" +
        "  }.bind(this))();" +
        "  return c;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "function test() {" +
        "  (function() {" +
        "  var aa = 1;" +
        "  var bb = 2;" +
        "  var cc = aa + bb;" +
        "  }.bind(this))();" +
        "  return cc;" +
        "}", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithGeneratePseudoNames() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, true, null, null, null);

    String code = "function test() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = a + b;" +
        "  return c;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "function test() {" +
        "  var $a$ = 1;" +
        "  var $b$ = 2;" +
        "  var $c$ = $a$.$ + $b$.$;" +
        "  return $c$.$;" +
        "}", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithLocalRenamingOnly() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", true, false, false, null, null, null);

    String code = "function test() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = a + b;" +
        "  return c;" +
        "}" +
        "function foo() {" +
        "  var a = 3;" +
        "  var b = 4;" +
        "  var c = a + b;" +
        "  return c;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "function test() {" +
        "  var aa = 1;" +
        "  var ba = 2;" +
        "  var ca = aa + ba;" +
        "  return ca;" +
        "}" +
        "function foo() {" +
        "  var aa = 3;" +
        "  var ba = 4;" +
        "  var ca = aa + ba;" +
        "  return ca;" +
        "}", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithTheSameNameInDifferentScopes() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    String code = "function test() {" +
        "  var a = 1;" +
        "  function foo() {" +
        "    var a = 2;" +
        "  }" +
        "  foo();" +
        "}" +
        "function foo() {" +
        "  var a = 3;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "function test() {" +
        "  var aa = 1;" +
        "  function foo() {" +
        "    var ba = 2;" +
        "  }" +
        "  foo();" +
        "}" +
        "function foo() {" +
        "  var ca = 3;" +
        "}", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithAnonymousFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    String code = "function test() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = function() {" +
        "    return a + b;" +
        "  }();" +
        "  return c;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    renameVars.process(null, root);

    Node actual = compiler.getAST();
    Node expected = NodeTraversal.parse(NodeFactory.getFactory(),
        "function test() {" +
        "  var aa = 1;" +
        "  var bb = 2;" +
        "  var cc = function() {" +
        "    return aa + bb;" +
        "  }();" +
        "  return cc;" +
        "}", null, null);

    NodeTraversal.traverse(compiler, actual, new NothingCallback());
    NodeTraversal.traverse(compiler, expected, new NothingCallback());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testRenameVarsWithDuplicateNames() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    String code = "function test() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "  var c = a + a;" +
        "  return c;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    try {
      renameVars.process(null, root);
      fail("Expected an exception");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Duplicate declaration of variable a"));
    }
  }

  @Test
  public void testRenameVarsWithInvalidVariableNameCharacter() {
    CompilerOptions options = new CompilerOptions();
    options.setSourceMap(false);
    compiler = new Compiler(options);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, new char[]{'?'}, null);

    String code = "function test() {" +
        "  var a = 1;" +
        "  var ?b = 2;" +
        "  var c = a + ?b;" +
        "  return c;" +
        "}";
    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node root = NodeTraversal.parse(NodeFactory.getFactory(), code, null, null);

    try {
      renameVars.process(null, root);
      fail("Expected an exception");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Invalid character '?' in variable name ?b"));
    }
  }
}

class NothingCallback implements NodeTraversal.Callback {
  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    // Do nothing
  }
}
import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.NodeTree.newNode;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }," +
        "    d: function() { return 4; }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  z = obj.c();" +
        "  w = obj.d();" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = 1, b = 2, c, d;" +
        "  c = function() { return 3; }" +
        "  d = function() { return 4; }" +
        "  var x = a;" +
        "  var y = b;" +
        "  z = c();" +
        "  w = d();" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: function() { return function() { return this.b; }.bind(this) }," +
        "    b: 2" +
        "  };" +
        "  x = obj.a();" +
        "  y = obj.b;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = function temp_var_a() { return function temp_var_a_inner() { return this.b; }; }.bind(this), b = 2;" +
        "  x = a();" +
        "  y = b;" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithSingleAssignment() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1" +
        "  };" +
        "  x = obj.a;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = 1, x = a;" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithSinglePropertyReadOnly() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1" +
        "  }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = 1;" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithMultipleProperties() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {d: 3}" +
        "  };" +
        "  x = obj.a;" +
        "  y = obj.b;" +
        "  z = obj.c.d;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = 1, b = 2, c, d;" +
        "  c = {" +
        "    d: 3" +
        "  };" +
        "  x = a;" +
        "  y = b;" +
        "  z = c.d;" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    String testCode = "function foo() {" +
        "  var obj = {};" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObjectAndSingleProperty() {
    String testCode = "function foo() {" +
        "  var obj = {}," +
        "  x = obj.a;" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: function() { return 1; }," +
        "    b: function() { return 2; }" +
        "  };" +
        "  x = obj.a();" +
        "  y = obj.b();" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = function() { return 1; }, b = function() { return 2; }," +
        "  x = a();" +
        "  y = b();" +
        "}",
        printer.printToString(tree));
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionPropertiesAndFunction() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: function() { return 1; }," +
        "    b: function() { return function() { return 2; }() }" +
        "  };" +
        "  x = obj.a();" +
        "  y = obj.b();" +
        "}" ;

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarning = false;

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp_var_") {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };

    Node tree = parser.parse(testCode, null, null);
    pass.process(externs(tree), tree);
    assertEquals(
        "function foo() {" +
        "  var a = function() { return 1; }, b = (function() { var temp_var_b = function() { return 2; }; return temp_var_b; })();" +
        "  x = a();" +
        "  y = b();" +
        "}",
        printer.printToString(tree));
  }
}
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.Config.CompilationLevel.ADVANCED_OPTIMIZATIONS;
import static com.google.javascript.jscomp.CompilerOptions.Config.CompilationLevel.SIMPLE_OPTIMIZATIONS;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RenamePrototypesTest {

  @org.junit.Test
  public void testSimpleRename() {
    String input = "function Foo() {} Foo.prototype.bar = function() { return 1; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();
    assertEquals("function Foo() {}\nFoo.prototype._c = function() { return 1; }", output);
  }

  @org.junit.Test
  public void testAggressiveRename() {
    String input = "function Foo() {} Foo.prototype.fooBar = function() { return 1; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();
    assertEquals("function Foo() {}\nFoo.prototype._c_f_o_B_a_r = function() { return 1; }", output);
  }

  @org.junit.Test
  public void testReservedRename() {
    String input = "function Foo() {} Foo.prototype.indexOf = function() { return 1; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, false, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();
    assertEquals("function Foo() {}\nFoo.prototype.indexOf = function() { return 1; }", output);
  }

  @org.junit.Test
  public void testNoRename() {
    String input = "function Foo() {} Foo.prototype.__foo = function() { return 1; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, false, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();
    assertEquals("function Foo() {}\nFoo.prototype.__foo = function() { return 1; }", output);
  }

  @org.junit.Test
  public void testNestedPrototype() {
    String input = "function Foo() {} Foo.prototype.bar = function() { return 1; }; Foo.prototype.baz = function() { return 2; }; Foo.prototype.qux.quux = function() { return 3; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();

    assertEquals("function Foo() {}\nFoo.prototype._c = function() { return 1; }\nFoo.prototype._b = function() { return 2; }\nFoo.prototype._b_q_u_x._q_u_u_x = function() { return 3; }", output);
  }

  @org.junit.Test
  public void testNoRenameWithAggressive() {
    String input = "function Foo() { this.qux = 1; } Foo.prototype.bar = function() { return 1; }; Foo.prototype.qux = function() { return 2; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();

    assertEquals("function Foo() { this._q_u_x = 1; } Foo.prototype._c = function() { return 1; }; Foo.prototype._q_u_x_q_u_x = function() { return 2; }", output);
  }

  @org.junit.Test
  public void testComplexInput() {
    String input = "function Foo() {} Foo.prototype.foo = function() { return 1; };\n" +
        "function Bar(a, b) { this.qux = a; this.quux = b; }\n" +
        "Bar.prototype.quuxQuux = function() { return 1; };\n" +
        "Bar.prototype.baz = function() { return 2; };\n" +
        "var qux = new Bar(1, 2);\n" +
        "qux.quuxQuux = function() { return 3; };\n" +
        "qux.qux = function() { return 4; }\n" +
        "qux.baz = function() { return 5; };\n" +
        "delete qux.baz;\n" +
        "function Baz() { this.quux = 1; } Baz.prototype.quux = function() { return 6; }";

    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();

    assertEquals("function Foo() {} Foo.prototype._c = function() { return 1; };\n" +
        "function Bar(a, b) { this._q_u_x = a; this._q_u_u_x = b; }\n" +
        "Bar.prototype._b_a_z = function() { return 2; };\n" +
        "Bar.prototype._q_u_u_x_q_u_u_x = function() { return 3; };\n" +
        "Bar.prototype._q_u_u_x = function() { return 4; };\n" +
        "var qux = new Bar(_c_a, _c_b);\n" +
        "qux._q_u_u_x_q_u_u_x = function() { return 5; };\n" +
        "delete qux._b_a_z;", output);
  }

  @org.junit.Test
  public void testError() {
    String input = "function Foo() {} Foo.prototype.bar = function() { return 1; } 1;";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    try {
      CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);
      fail("Expected compilation error");
    } catch (CompilerException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testSimpleOptimizationLevel() {
    String input = "function Foo() {} Foo.prototype.bar = function() { return 1; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, false, null, null)));
    options.setOutputFile("output.js");
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();
    assertEquals("function Foo() {}\nFoo.prototype.bar = function() { return 1; }", output);
  }

  @org.junit.Test
  public void testAdvancedOptimizationLevel() {
    String input = "function Foo() {} Foo.prototype.bar = function() { return 1; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, false, null, null)));
    options.setOutputFile("output.js");
    options.setCompilationLevel(ADVANCED_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();
    assertEquals("function Foo() {} Foo.prototype._c = function() { return 1; }", output);
  }

  @org.junit.Test
  public void testRenameExistingVariable() {
    String input = "var a = 1; function Foo() { this.a = 2; } Foo.prototype.renamedA = function() { return 3; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();

    assertEquals("var a = 1;\nfunction Foo() { this._a = 2; } Foo.prototype._r_e_n_a_m_e_d_A = function() { return 3; }", output);
  }

  @org.junit.Test
  public void testRenameExistingFunction() {
    String input = "function a() { return 1; } function Foo() { this.a = function() { return 2; } } Foo.prototype.renamedA = function() { return 3; }";
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Arrays.asList(new RenamePrototypes(null, true, null, null)));
    options.setOutputFile("output.js");

    Compiler compiler = new Compiler();
    CompilerResults results = compiler.compile(new CompilerInput(TokenStream.create(new StringReader(input)), null), options);

    String output = results.toJsSource();

    assertEquals("function a() { return 1; } function Foo() { this._a = function() { return 2; } } Foo.prototype._r_e_n_a_m_e_d_A = function() { return 3; }", output);
  }
}
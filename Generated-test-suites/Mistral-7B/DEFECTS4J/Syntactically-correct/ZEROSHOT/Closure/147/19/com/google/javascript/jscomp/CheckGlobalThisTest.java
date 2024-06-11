import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.Diagnostics.diagnosticType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions.CheckGlobalThis;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(Parameterized.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel checkLevel;
  private boolean expectedResult;

  public CheckGlobalThisTest(boolean expectedResult, String sourceCode) {
    this.expectedResult = expectedResult;
    CompilerOptions options = new CompilerOptions(true, ImmutableList.of());
    this.compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return options;
      }
    };
    this.checkLevel = new CheckGlobalThis(compiler, WARNING);
  }

  @Parameterized.Parameters(name = "Test case {index}: {0}")
  public static Iterable<Object[]> data() {
    return newArrayList(new Object[][] {
        {false, "function evil() { this.useful = undefined; }"},
        {true, "function evil() { var a = this; a.useful = undefined; }"},
        {true, "function evil() { this.prototype.useful = undefined; }"},
        {true, "function evil() { this.prototype.a.useful = undefined; }"},
        {false, "var evil = function() { this.useful = undefined; }"},
        {false, "var evil = function evil() { this.useful = undefined; }"},
        {false, "function Foo() { this.useful = undefined; }"},
        {false, "function Foo() { this.prototype.useful = undefined; }"},
        {true, "function Foo() { this.prototype.a.useful = undefined; }"},
        {false, "function Foo() { this.a = function() { this.useful = undefined; }}"},
        {false, "function Foo() { this.a = {}; this.a.useful = undefined; }"},
        {false, "var Foo = function() {}; Foo.prototype.useful = undefined;"},
        {false, "function Foo() { function Bar() {} this.Bar = Bar; }"},
        {false, "function Foo() { this.Bar = function Bar() {}; }"},
        {false, "function Foo() { this.Bar = { Bar: function() {} }; }"},
        {true, "function Foo() { function Bar() {} this.Bar = Bar; this.useful = undefined; }"},
        {true, "function Foo() { this.Bar = function Bar() {}; this.useful = undefined; }"},
        {true, "function Foo() { this.Bar = { Bar: function() {} }; this.useful = undefined; }"},
        {false, "function Foo() { var Bar = function() {}; this.Bar = Bar; }"},
        {false, "function Foo() { var Bar = {}; this.Bar = Bar; }"},
        {false, "function Foo() { this.Bar = function() {}; var Bar = {}; }"},
    });
  }

  @Test
  public void testCheckGlobalThis() throws Exception {
    TestSourceFile sourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node root = compiler.parseTree(sourceFile.getCode());
    NodeTraversal traverser = new NodeTraversal(root, checkLevel);
    traverser.traverse();

    ImmutableList<Diagnostics> diagnostics = compiler.getDiagnostics();
    if (expectedResult) {
      assertThat(diagnostics, newDiagnostic(diagnosticType(GLOBAL_THIS), root));
    } else {
      assertThat(diagnostics, emptyDiagnosticList());
    }
  }
}
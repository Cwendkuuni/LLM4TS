import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.Option.*;
import static com.google.javascript.jscomp.JSError.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  @org.junit.Test
  public void testVarCheck() {
    CompilerOptions options = new CompilerOptions(new DiagnosticGroup());
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptStrict, false);

    Compiler compiler = new Compiler();
    compiler.compile("class Test {\n" +
        "  var x;\n" +
        "  function foo() {\n" +
        "    var y;\n" +
        "  }\n" +
        "}", options);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumberOfErrors() == 0);

    options.setOptBoolean(OptStrict, true);
    compiler.compile("class Test {\n" +
        "  var x;\n" +
        "  function foo() {\n" +
        "    var y;\n" +
        "  }\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostics.getError(0).getCode().name());
    assertEquals("y", diagnostics.getError(0).getMessage(0));

    options.setOptBoolean(OptStrict, false);
    options.setOptBoolean(OptCompileWithChecks, false);
    compiler.compile("class Test {\n" +
        "  var x;\n" +
        "  function foo() {\n" +
        "    var y;\n" +
        "  }\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());

    options.setOptBoolean(OptStrict, false);
    options.setOptBoolean(OptCompileWithChecks, true);
    compiler.compile("class Test {\n" +
        "  var x;\n" +
        "  function foo() {\n" +
        "    var y;\n" +
        "  }\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostics.getError(0).getCode().name());
    assertEquals("y", diagnostics.getError(0).getMessage(0));

    options.setOptBoolean(OptStrict, true);
    options.setOptBoolean(OptCompileWithChecks, true);
    compiler.compile("class Test {\n" +
        "  var x;\n" +
        "  function foo() {\n" +
        "    var y;\n" +
        "  }\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostics.getError(0).getCode().name());
    assertEquals("y", diagnostics.getError(0).getMessage(0));

    options.setOptBoolean(OptStrict, false);
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptDepCheck, true);
    compiler.compile("module A {\n" +
        "  export var x = 1;\n" +
        "}", options);

    options.setOptBoolean(OptStrict, false);
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptDepCheck, true);
    compiler.compile("module B {\n" +
        "  import x from './A';\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());

    options.setOptBoolean(OptStrict, true);
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptDepCheck, true);
    compiler.compile("module A {\n" +
        "  export var x = 1;\n" +
        "}", options);

    options.setOptBoolean(OptStrict, true);
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptDepCheck, true);
    compiler.compile("module B {\n" +
        "  import x from './A';\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());

    options.setOptBoolean(OptStrict, false);
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptDepCheck, true);
    compiler.compile("module A {\n" +
        "  export var x = 1;\n" +
        "}", options);

    options.setOptBoolean(OptStrict, false);
    options.setOptBoolean(OptCompileWithChecks, true);
    options.setOptBoolean(OptDepCheck, true);
    compiler.compile("module B {\n" +
        "  import x from './A';\n" +
        "}", options);

    diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.getNumberOfErrors());
    assertEquals(0, diagnostics.getNumberOfWarnings());
  }
}
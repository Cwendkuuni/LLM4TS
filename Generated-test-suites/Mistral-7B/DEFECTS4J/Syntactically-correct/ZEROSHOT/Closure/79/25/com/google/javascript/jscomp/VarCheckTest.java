import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VarCheckTest {

  private static Compiler createCompiler() {
    Parser parser = new Parser(new CheckerFactory());
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(parser, options);
    return compiler;
  }

  @org.junit.Test
  public void testVarCheck() throws IOException {
    Compiler compiler = createCompiler();

    // Test that all variables are declared
    String code = "var x; function foo() { var y; }";
    DiagnosticGroup diagnostics = compiler.compile(null, code, null);
    assertDiagnostics(diagnostics, "x", "y");

    // Test file-private variables are accessed only in the file that declares them
    code = "var x = 1; function foo() { var x = 2; }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnostics(diagnostics, "x");

    // Test that any var references that cross module boundaries respect declared module dependencies
    code = "module1.js:\n" +
           "function foo() {} module2.js:\n" +
           "foo.bar = 1; // should error since module1 doesn't depend on module2\n" +
           "module1.js:\n" +
           "import * as m2 from 'module2'; // should not error";
    compiler.addInputFile("module1.js", code);
    code = "module2.js:\n" +
           "export let bar = 1;";
    compiler.addInputFile("module2.js", code);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnostics(diagnostics, "foo.bar");

    code = "module1.js:\n" +
           "import * as m2 from 'module2'; // should not error\n" +
           "function foo() {} module2.js:\n" +
           "export let bar = 1;";
    compiler.addInputFile("module1.js", code);
    compiler.addInputFile("module2.js", code);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnosticsEmpty(diagnostics);

    // Test undeclared variables as warnings
    code = "var x; function foo() { var y; } var z;";
    diagnostics = compiler.compile(null, code, null, true, null, null);
    assertDiagnostics(diagnostics, "z");

    // Test error messages
    code = "var x; function foo() { var y; } var z;";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnostics(diagnostics, "z", "y");

    code = "function foo() { var x; } var x;";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnostics(diagnostics, "x");

    code = "function foo() { var x; } function bar() { var x; }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnostics(diagnostics, "x");

    code = "function foo() { var x; } function bar() { var x = 1; }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { let x = 1; }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { const x = 1; }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { class X {} }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { function x() {} }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { class X extends X {} }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { var x = 1; }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "function foo() { var x; } function bar() { function x() {} }";
    diagnostics = compiler.compile(null, code, null);
    assertDiagnosticsEmpty(diagnostics);

    // Test strict module dependency check
    code = "module1.js:\n" +
           "export let x = 1;\n" +
           "import * as m2 from 'module2';\n" +
           "function foo() { var y; }\n";
    compiler.addInputFile("module1.js", code);
    code = "module2.js:\n" +
           "export let y = 1;\n" +
           "import 'module1';\n";
    compiler.addInputFile("module2.js", code);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "module1.js:\n" +
           "export let x = 1;\n" +
           "import * as m2 from 'module2';\n" +
           "function foo() { var y; }\n";
    compiler.addInputFile("module1.js", code);
    compiler.addInputFile("module2.js", code);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "module1.js:\n" +
           "export let x = 1;\n" +
           "import * as m2 from 'module2';\n" +
           "function foo() { var y; }\n";
    compiler.addInputFile("module1.js", code);
    compiler.addInputFile("module2.js", code);
    compiler.setErrorLevel(JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR), CheckLevel.ERROR);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "module1.js:\n" +
           "export let x = 1;\n" +
           "import * as m2 from 'module2';\n" +
           "function foo() { var y; }\n";
    compiler.addInputFile("module1.js", code);
    compiler.setErrorLevel(JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR), CheckLevel.WARNING);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnosticsEmpty(diagnostics);

    code = "module1.js:\n" +
           "export let x = 1;\n" +
           "import * as m2 from 'module2';\n" +
           "function foo() { var y; }\n";
    compiler.addInputFile("module1.js", code);
    compiler.setErrorLevel(JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR), CheckLevel.ERROR);
    diagnostics = compiler.compile(null, null, null);
    assertDiagnostics(diagnostics, "foo.y");

    // Test name references in externs
    code = "externs.js:\n" +
           "function foo() {}";
    code += "\n" +
           "// This should not cause an error since it is in the externs file\n" +
           "foo.bar = 1;";
    NodeFactory factory = new NodeFactory();
    Node externsRoot = factory.parseTree(code);
    NodeTraversal.traverse(compiler, externsRoot, new VarCheck(compiler, true));
    Set<String> variablesInExterns = Sets.newHashSet();
    NodeTraversal.traverse(compiler, externsRoot, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME) {
          variablesInExterns.add(n.getString());
        }
      }
    });
    Set<String> variablesInCode = new HashSet<>();
    NodeTraversal.traverseRoots(compiler, Arrays.asList(new Node(Token.PROGRAM, null)), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR || NodeUtil.isFunctionDeclaration(parent)) {
          Scope scope = t.getScope();
          for (Scope.Var var : scope.getVarList()) {
            variablesInCode.add(var.name);
          }
        }
      }
    });
    // Check that all variables in the externs file are also declared in the code
    assertVariablesInExternsDeclaredInCode(variablesInExterns, variablesInCode);
  }

  private void assertDiagnostics(DiagnosticGroup diagnostics, String... messages) {
    for (DiagnosticType diagnosticType : new DiagnosticType[]{UNDEFINED_VAR_ERROR, VIOLATED_MODULE_DEP_ERROR, MISSING_MODULE_DEP_ERROR, NAME_REFERENCE_IN_EXTERNS_ERROR}) {
      assertTrue(diagnostics.hasError(diagnosticType));
      for (String message : messages) {
        assertTrue(diagnostics.getMessages(diagnosticType).contains(message));
      }
    }
  }

  private void assertDiagnosticsEmpty(DiagnosticGroup diagnostics) {
    assertFalse(diagnostics.hasErrors());
  }

  private void assertVariablesInExternsDeclaredInCode(Set<String> variablesInExterns, Set<String> variablesInCode) {
    assertTrue(variablesInExterns.containsAll(variablesInCode));
    assertTrue(variablesInCode.containsAll(variablesInExterns));
  }
}
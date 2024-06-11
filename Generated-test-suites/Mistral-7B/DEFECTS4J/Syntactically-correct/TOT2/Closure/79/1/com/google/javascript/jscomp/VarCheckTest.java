import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;
import static com.google.javascript.jscomp.JSError.make;

public class VarCheckTest {

  @Test
  public void testVarCheck() {
    Compiler compiler = new Compiler();
    CompilerInput input = new CompilerInput("Test.java", null, null, null, null);
    compiler.addSourceUnit(input);

    // Test normal case
    String code1 = "class Test {\n" +
                   "  int a;\n" +
                   "  void foo() {\n" +
                   "    int b;\n" +
                   "  }\n" +
                   "}";
    input.setCode(code1);
    Node root = compiler.parseTree(input, null);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    VarCheck checker = new VarCheck(compiler);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    assertEquals(0, compiler.getNumDiagnostics());

    // Test undefined variable
    String code2 = "class Test {\n" +
                   "  void foo() {\n" +
                   "    int x;\n" +
                   "  }\n" +
                   "}";
    input.setCode(code2);
    root = compiler.parseTree(input, null);
    traversal = new NodeTraversal(compiler, root);
    checker = new VarCheck(compiler);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    Node errorNode = traversal.getLastError();
    assertEquals(UNDEFINED_VAR_ERROR, errorNode.getError().getType());
    assertEquals("Test.java:1:1: variable x is undefined", errorNode.getMessage());

    // Test module dependency violation
    String code3 = "module A {\n" +
                   "  export function foo() {\n" +
                   "    var b = 1;\n" +
                   "  }\n" +
                   "}\n" +
                   "module B {\n" +
                   "  import foo from 'A';\n" +
                   "  function bar() {\n" +
                   "    var b = 2;\n" +
                   "  }\n" +
                   "}";
    input.setCode(code3);
    root = compiler.parseTree(input, null);
    traversal = new NodeTraversal(compiler, root);
    checker = new VarCheck(compiler, true);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    errorNode = traversal.getLastError();
    assertEquals(VIOLATED_MODULE_DEP_ERROR, errorNode.getError().getType());
    assertEquals("B.java:3:1: module A cannot reference b, defined in A.java, since A.java loads after B.java",
                errorNode.getMessage());

    // Test missing module dependency
    String code4 = "module A {\n" +
                   "  export function foo() {\n" +
                   "  }\n" +
                   "}\n" +
                   "module B {\n" +
                   "  import foo from 'A';\n" +
                   "  function bar() {\n" +
                   "  }\n" +
                   "}";
    input.setCode(code4);
    root = compiler.parseTree(input, null);
    traversal = new NodeTraversal(compiler, root);
    checker = new VarCheck(compiler, true);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    errorNode = traversal.getLastError();
    assertEquals(MISSING_MODULE_DEP_ERROR, errorNode.getError().getType());
    assertEquals("B.java:2:1: missing module dependency; module B should depend on module A because it references foo",
                errorNode.getMessage());

    // Test strict module dependency
    String code5 = "module A {\n" +
                   "  export function foo() {\n" +
                   "  }\n" +
                   "}\n" +
                   "module B {\n" +
                   "  import foo from 'A';\n" +
                   "  function bar() {\n" +
                   "  }\n" +
                   "}\n" +
                   "module C {\n" +
                   "  import foo from 'A';\n" +
                   "  function bar() {\n" +
                   "  }\n" +
                   "}";
    input.setCode(code5);
    root = compiler.parseTree(input, null);
    traversal = new NodeTraversal(compiler, root);
    checker = new VarCheck(compiler, true);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    errorNode = traversal.getLastError();
    assertEquals(STRICT_MODULE_DEP_ERROR, errorNode.getError().getType());
    assertEquals("C.java:2:1: module B cannot reference foo, defined in A.java",
                errorNode.getMessage());

    // Test name reference in externs
    String code6 = "extern function foo() {}";
    input.setCode(code6);
    root = compiler.parseTree(input, null);
    traversal = new NodeTraversal(compiler, root);
    checker = new VarCheck(compiler);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    errorNode = traversal.getLastError();
    assertEquals(NAME_REFERENCE_IN_EXTERNS_ERROR, errorNode.getError().getType());
    assertEquals("foo has no effect", errorNode.getMessage());

    // Test undefined extern variable
    String code7 = "extern function foo() {}";
    input.setCode(code7);
    root = compiler.parseTree(input, null);
    traversal = new NodeTraversal(compiler, root);
    checker = new VarCheck(compiler);
    checker.process(compiler.getExternsRoot(), root);
    checker.traverse(traversal, root, null);
    errorNode = traversal.getLastError();
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, errorNode.getError().getType());
    assertEquals("foo is not undefined in the externs.", errorNode.getMessage());
  }

  private static DiagnosticType UNDEFINED_VAR_ERROR = make("", 0, 0, VarCheck.UNDEFINED_VAR_ERROR);
  private static DiagnosticType VIOLATED_MODULE_DEP_ERROR = make("", 0, 0, VarCheck.VIOLATED_MODULE_DEP_ERROR);
  private static DiagnosticType MISSING_MODULE_DEP_ERROR = make("", 0, 0, VarCheck.MISSING_MODULE_DEP_ERROR);
  private static DiagnosticType STRICT_MODULE_DEP_ERROR = make("", 0, 0, VarCheck.STRICT_MODULE_DEP_ERROR);
  private static DiagnosticType NAME_REFERENCE_IN_EXTERNS_ERROR = make("", 0, 0, VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR);
  private static DiagnosticType UNDEFINED_EXTERN_VAR_ERROR = make("", 0, 0, VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
}
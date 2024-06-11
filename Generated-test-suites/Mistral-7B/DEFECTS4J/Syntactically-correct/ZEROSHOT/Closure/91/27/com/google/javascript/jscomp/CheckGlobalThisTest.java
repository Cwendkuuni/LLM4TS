import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CheckGlobalThis;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "function evil() {" +
            "this.useful = undefined;" +
            "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());

    ast = Node.externalProgram(
        "function evil() {" +
            "var a = this;" +
            "a.useful = undefined;" +
            "}"
    );

    traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    DiagnosticType diagnosticType = DIAGNOSTIC_TYPES.get(DiagnosticType.WARNING.name());
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testCheckGlobalThisWithFunctionAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "@constructor\n" +
        "function EvilConstructor() {" +
            "this.useful = undefined;" +
            "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "function EvilFunction() {" +
            "@this\n" +
            "this.useful = undefined;" +
            "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: function() {" +
                    "this.foo = undefined;" +
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var a = this;"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: function() {" +
                    "this = this.someProperty;" + // this is a prototype property
                    "this.foo = undefined;" +
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "function evil() {" +
            "prop = this.prop;" + // property access
            "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypePropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: function() {" +
                    "prop = this.prop;" + // prototype property access
                    "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeMethodCall() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: function() {" +
                    "this.someMethod();" + // method call
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeMethodCallWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: function(arg1, arg2) {" +
                    "this.someMethod(arg1, arg2);" + // method call with arguments
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypePropertyAccessWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: function() {" +
                    "this.someProperty(arg1, arg2);" + // property access with arguments
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypePropertyAccessWithObjectLiteral() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: {" +
                    "foo: function() {" +
                        "this.x = undefined;" + // property access on object literal
                    "}" +
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypePropertyAccessWithObjectLiteralAndArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node ast = Node.externalProgram(
        "var MyObject = {" +
            "prototype: {" +
                "useful: {" +
                    "foo: function(arg1, arg2) {" +
                        "this.x = arg1;" + // property access on object literal with arguments
                    "}" +
                "}" +
            "}" +
        "}"
    );

    NodeTraversal traverser = new NodeTraversal(ast, null, checker);
    traverser.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }
}
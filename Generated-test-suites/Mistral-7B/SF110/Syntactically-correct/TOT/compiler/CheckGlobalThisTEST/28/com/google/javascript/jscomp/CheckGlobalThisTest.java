import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckGlobalThis.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsage() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newFunction("safeFunction")
        .addBody(Node.newExpression("return 42;"))
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertTrue("No warning should have been issued for safe usage",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testConstructorUsage() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newFunction("MyConstructor")
        .addAnnotation("constructor")
        .addBody(Node.newExpression("this.x = 42;"))
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertTrue("No warning should have been issued for constructor usage",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testPrototypeMethodUsage() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newFunction("MyPrototype")
        .addAnnotation("prototype")
        .addBody(Node.newExpression("this.x = 42;"))
        .finalize();

    Node prototypeNode = Node.newObjectLiteral()
        .addProperty("prototype", node)
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(prototypeNode, checker);

    assertTrue("No warning should have been issued for prototype method usage",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithNoAnnotationUsage() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newFunction("myFunction")
        .addBody(Node.newExpression("this.x = 42;"))
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertTrue("No warning should have been issued for function with no annotation usage",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testUnsafeAssignment() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newExpression("this = 42;")
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertFalse("Warning should have been issued for unsafe assignment",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testUnsafePropertyAccess() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newExpression("console.log(this.x);")
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertFalse("Warning should have been issued for unsafe property access",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithinPrototypeProperty() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newObjectLiteral()
        .addProperty("prototype", Node.newFunction("prototypeFunction")
            .addBody(Node.newExpression("this.x = 42;"))
            .finalize())
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertTrue("No warning should have been issued for function within prototype property",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithinPrototypeMethod() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newObjectLiteral()
        .addProperty("prototype", Node.newFunction("prototypeFunction", "arg")
            .addBody(Node.newExpression("this.x = arg;"))
            .finalize())
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertTrue("No warning should have been issued for function within prototype method",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithinPrototypeSubProperty() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newObjectLiteral()
        .addProperty("prototype", Node.newObjectLiteral()
            .addProperty("myFunction", Node.newFunction("myFunction")
                .addBody(Node.newExpression("this.x = 42;"))
                .finalize())
            .finalize())
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(node, checker);

    assertTrue("No warning should have been issued for function within prototype subproperty",
        checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithinPrototypeSubPropertyUsage() {
    AbstractCompiler compiler = new Compiler();
    NodeTraversal traverser = new NodeTraversal(compiler);
    Node node = Node.newExpression("myFunction();")
        .finalize();

    Node prototypeNode = Node.newObjectLiteral()
        .addProperty("prototype", Node.newObjectLiteral()
            .addProperty("myFunction", Node.newFunction("myFunction")
                .addBody(Node.newExpression("this.x = 42;"))
                .finalize())
            .finalize())
        .finalize();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    traverser.traverse(prototypeNode, checker);

    assertTrue("No warning should have been issued for function within prototype subproperty usage",
        checker.compiler.getDiagnostics().isEmpty());
  }
}
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createGetProp;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createUnequal;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.NodeFactory.createParameter;
import static com.google.javascript.rhino.NodeFactory.createBooleanLiteral;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.DiagnosticType.JSC_USED_GLOBAL_THIS;

import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.*;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node root = createScript("function evil() { this.useful = undefined; }");

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());

    root = createScript("function evil() { var a = this; a.useful = undefined; }");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().get(0).getType() == JSC_USED_GLOBAL_THIS);

    root = createScript("function Foo() { this.bar = 42; }");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().get(0).getType() == JSC_USED_GLOBAL_THIS);

    root = createScript("function Foo() { this.bar = 42; } function Foo.prototype.oop = function() {}");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());

    root = createScript("function Foo() { this.bar = 42; } function Foo.prototype.constructor() {}");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());

    root = createScript("function Foo() { this.bar = 42; } function Foo.prototype.constructor() { this.x = function() {} }");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());

    root = createScript("function Foo() { this.bar = 42; } function Foo.prototype.constructor() { this.x = this.y; }");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().get(0).getType() == JSC_USED_GLOBAL_THIS);

    root = createScript("function Foo() { } Foo.prototype.bar = function() {}; function Foo.prototype.constructor() { }");
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}
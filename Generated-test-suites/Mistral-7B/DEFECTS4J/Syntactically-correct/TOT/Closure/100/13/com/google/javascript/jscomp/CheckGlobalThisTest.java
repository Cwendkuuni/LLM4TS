import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    // Safe usage of 'this'
    String safeCode = "function Foo() { this.x = 5; }";
    Node safeNode = CompilerOptions.standard().parseTree(safeCode);
    NodeTraversal traversal = new NodeTraversal(compiler, checker, safeNode);
    traversal.traverseAllChildren();
    assertEquals(0, compiler.getDiagnostics().size());

    // Unsafe usage of 'this'
    String unsafeCode = "var a = function() { this.x = 5; }";
    Node unsafeNode = CompilerOptions.standard().parseTree(unsafeCode);
    traversal = new NodeTraversal(compiler, checker, unsafeNode);
    traversal.traverseAllChildren();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThisWithConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    // Safe usage of 'this' inside constructor
    String constructorCode = "function Foo() { this.x = 5; }";
    Node constructorNode = CompilerOptions.standard().parseTree(constructorCode);
    constructorNode.setJSDocInfo(new JSDocInfo(new JSDocInfo.ConstructorTag()));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, constructorNode);
    traversal.traverseAllChildren();
    assertEquals(0, compiler.getDiagnostics().size());

    // Unsafe usage of 'this' outside constructor
    String notConstructorCode = "var Foo = function() { this.x = 5; }";
    Node notConstructorNode = CompilerOptions.standard().parseTree(notConstructorCode);
    traversal = new NodeTraversal(compiler, checker, notConstructorNode);
    traversal.traverseAllChildren();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThisWithPrototype() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    // Safe usage of 'this' inside prototype method
    String prototypeCode = "function Foo() {} Foo.prototype.bar = function() { this.x = 5; }";
    Node prototypeNode = CompilerOptions.standard().parseTree(prototypeCode);
    NodeTraversal traversal = new NodeTraversal(compiler, checker, prototypeNode);
    traversal.traverseAllChildren();
    assertEquals(0, compiler.getDiagnostics().size());

    // Unsafe usage of 'this' outside prototype method
    String notPrototypeCode = "function Foo() {} Foo.x = function() { this.x = 5; }";
    Node notPrototypeNode = CompilerOptions.standard().parseTree(notPrototypeCode);
    traversal = new NodeTraversal(compiler, checker, notPrototypeNode);
    traversal.traverseAllChildren();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }
}
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;

public class CrossModuleMethodMotionTest {

  @org.junit.Before
  public void setup() {
    Compiler compiler = new Compiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    compiler.addPass(pass);
  }

  @org.junit.Test
  public void testProcess() {
    // Given a valid Node tree
    Node root = createNodeTree();
    Node externRoot = createNodeTree();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(null, new CrossModuleMethodMotion.IdGenerator(), true);

    // When the process method is called
    pass.process(externRoot, root);

    // Then no errors are reported
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testProcessWithNullExternRoot() {
    // Given a null externRoot
    Node root = createNodeTree();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(null, new CrossModuleMethodMotion.IdGenerator(), true);

    // When the process method is called
    pass.process(null, root);

    // Then an error is reported
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND: null deepest common module", compiler.getDiagnostics().get(0).getMessage());
  }

  @org.junit.Test
  public void testProcessWithEmptyModuleGraph() {
    // Given an empty module graph
    Compiler compiler = new Compiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    compiler.setModuleGraph(new JSModuleGraph());

    // When the process method is called
    Node root = createNodeTree();
    Node externRoot = createNodeTree();
    pass.process(externRoot, root);

    // Then no methods are moved
    Iterator<Node> iterator = root.iterateChildren();
    int count = 0;
    while (iterator.hasNext()) {
      Node child = iterator.next();
      if (child.isFunction()) {
        count++;
      }
    }
    assertEquals(0, count);
  }

  @org.junit.Test
  public void testProcessWithReferencedUnmovedMethod() {
    // Given a method that is referenced but not moved
    Compiler compiler = new Compiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    compiler.setModuleGraph(new JSModuleGraph());

    // When the process method is called
    Node root = createNodeTreeWithReferencedUnmovedMethod();
    Node externRoot = createNodeTree();
    pass.process(externRoot, root);

    // Then the method is not stubbed
    Node method = root.getFirstChild();
    assertFalse(method.isFunction());
  }

  @org.junit.Test
  public void testProcessWithMovedMethod() {
    // Given a method that is moved
    Compiler compiler = new Compiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    compiler.setModuleGraph(new JSModuleGraph());

    // When the process method is called
    Node root = createNodeTreeWithMovedMethod();
    Node externRoot = createNodeTree();
    pass.process(externRoot, root);

    // Then the method is stubbed in the original module and unstubbed in the deeper module
    Node originalMethod = root.getFirstChild();
    assertTrue(originalMethod.isFunction());
    Node originalMethodStub = originalMethod.getLastChild();
    assertEquals("JSCompiler_stubMethod", originalMethodStub.getFirstChild().getName().getName());

    Node deeperModule = root.getChild(1);
    Node deeperModuleMethod = deeperModule.getFirstChild();
    assertTrue(deeperModuleMethod.isFunction());
    Node deeperModuleMethodUnstub = deeperModuleMethod.getLastChild();
    assertEquals("JSCompiler_unstubMethod", deeperModuleMethodUnstub.getFirstChild().getName().getName());
  }

  private Node createNodeTree() {
    // Create a simple Node tree
    // ...
  }

  private Node createNodeTreeWithReferencedUnmovedMethod() {
    // Create a Node tree with a referenced but not moved method
    // ...
  }

  private Node createNodeTreeWithMovedMethod() {
    // Create a Node tree with a moved method
    // ...
  }
}
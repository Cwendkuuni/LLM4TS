import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = new CompilerOptions();
    checker = new CheckGlobalThis(compiler, WarningLevel.VERBOSE);
  }

  @Test
  public void testGlobalThis() {
    Node node = createNode("function evil() { this.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, traversal.getDiagnostics().get(0).getType());
  }

  @Test
  public void testGlobalThisInConstructor() {
    Node node = createNode("function Foo() { this.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInPrototypeMethod() {
    Node node = createNode("function Foo() {} Foo.prototype.method = function() { this.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithThisAnnotation() {
    Node node = createNode("function Foo() { @this this; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithoutParent() {
    Node node = createNode("function evil() { this.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(new Context(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithParentVariable() {
    Node node = createNode("var a = function evil() { this.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithParentFunction() {
    Node node = createNode("function a() { function evil() { this.property = undefined; } }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithPrototypeProperty() {
    Node node = createNode("function Foo() {} Foo.prototype.bar = function() { this.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithPrototypeSubproperty() {
    Node node = createNode("function Foo() {} Foo.prototype.bar = function() { this.baz.property = undefined; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithPrototypePropertyGet() {
    Node node = createNode("function Foo() {} Foo.prototype.getBar = function() { return this.property; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithPrototypePropertySet() {
    Node node = createNode("function Foo() {} Foo.prototype.setBar = function(value) { this.property = value; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testGlobalThisInFunctionWithPrototypePropertyGetAndSet() {
    Node node = createNode("function Foo() {} Foo.prototype.setBar = function(value) { this.property = value; }; Foo.prototype.getBar = function() { return this.property; }");
    NodeTraversal traversal = new NodeTraversal(createTopLevelContext(), node);
    checker.traverse(traversal, node, null);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  private Node createNode(String code) {
    return RhinoCompiler.compileToString(compiler, code, null, null);
  }

  private Context createTopLevelContext() {
    return new Context();
  }
}
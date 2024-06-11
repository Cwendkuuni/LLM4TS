import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.rhino.NodeTree.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.analyzer.AnalyzePrototypeProperties;
import com.google.javascript.rhino.ErrorReporter;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    idGenerator = mock(IdGenerator.class);
    moduleGraph = mock(JSModuleGraph.class);
    errorReporter = mock(ErrorReporter.class);
  }

  @Test
  public void testMoveMethodToDeeperModule() {
    // Given
    Node root = parse("class A { a() {} } class B extends A { b() {} }");
    Node externRoot = parse("{}");
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    // When
    pass.process(externRoot, root);

    // Then
    Node a = root.getFirstChild();
    Node b = getFirstChildOfType(a, NodeType.CLASS_DEF);
    Node aPrototype = b.getLastChild();
    Node bPrototype = getFirstChildOfType(b, NodeType.CLASS_DEF);
    Node bMethod = getFirstChildOfType(bPrototype, NodeType.METHOD_DEF);

    assertNull(bMethod);

    // When moving method 'b' to a deeper module
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);
    when(moduleGraph.dependsOn(any(JSModule.class), eq(bPrototype.getParent()))).thenReturn(true);
    when(idGenerator.newId()).thenReturn(1);

    pass.process(externRoot, root);

    // Then
    Node stubFunction = getChildOfType(aPrototype, NodeType.EXPR_STMT);
    Node stubCall = getFirstChildOfType(stubFunction, NodeType.CALL_EXPR);
    Node unstubFunction = getChildOfType(bPrototype, NodeType.EXPR_STMT);
    Node unstubCall = getFirstChildOfType(unstubFunction, NodeType.CALL_EXPR);

    assertEquals("JSCompiler_stubMethod(1)", stubCall.getExpression().toString());
    assertEquals("JSCompiler_unstubMethod(1, a.prototype.b)", unstubCall.getExpression().toString());
  }

  @Test
  public void testMoveMethodToSameModule() {
    // Given
    Node root = parse("class A { a() {} } class B extends A { b() {} }");
    Node externRoot = parse("{}");
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    // When
    pass.process(externRoot, root);

    // Then
    Node a = root.getFirstChild();
    Node b = getFirstChildOfType(a, NodeType.CLASS_DEF);
    Node bPrototype = b.getLastChild();
    Node bMethod = getFirstChildOfType(bPrototype, NodeType.METHOD_DEF);

    assertNotNull(bMethod);

    // When moving method 'b' to the same module
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);
    when(moduleGraph.dependsOn(any(JSModule.class), eq(bPrototype.getParent()))).thenReturn(false);

    pass.process(externRoot, root);

    // Then
    Node stubFunction = getChildOfType(aPrototype, NodeType.EXPR_STMT);
    Node stubCall = getFirstChildOfType(stubFunction, NodeType.CALL_EXPR);

    assertNull(unstubFunction);
  }

  @Test
  public void testMoveMethodToNewModule() {
    // Given
    Node root = parse("class A { a() {} } class B extends A { b() {} }");
    Node externRoot = parse("{}");
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    // When
    pass.process(externRoot, root);

    // Then
    Node a = root.getFirstChild();
    Node b = getFirstChildOfType(a, NodeType.CLASS_DEF);
    Node bPrototype = b.getLastChild();
    Node bMethod = getFirstChildOfType(bPrototype, NodeType.METHOD_DEF);

    assertNotNull(bMethod);

    // Given new module C
    Node rootC = parse("class C {}");
    Node externRootC = parse("{}");
    CompilerPass passC = new CrossModuleMethodMotion(compiler, idGenerator, true);

    when(moduleGraph.getModuleCount()).thenReturn(3);
    when(moduleGraph.getModule(2)).thenReturn(getMockModule("C"));
    when(moduleGraph.dependsOn(any(JSModule.class), eq(bPrototype.getParent()))).thenReturn(false);

    // When moving method 'b' to a new module
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);

    pass.process(externRoot, root);
    pass.process(externRootC, rootC);

    // Then
    Node stubFunction = getChildOfType(aPrototype, NodeType.EXPR_STMT);
    Node stubCall = getFirstChildOfType(stubFunction, NodeType.CALL_EXPR);
    Node unstubFunction = getChildOfType(bPrototype, NodeType.EXPR_STMT);
    Node unstubCall = getFirstChildOfType(unstubFunction, NodeType.CALL_EXPR);
    Node unstubFunctionC = getChildOfType(getMockModule("C").getRootNode(), NodeType.EXPR_STMT);
    Node unstubCallC = getFirstChildOfType(unstubFunctionC, NodeType.CALL_EXPR);

    assertEquals("JSCompiler_stubMethod(1)", stubCall.getExpression().toString());
    assertEquals("JSCompiler_unstubMethod(1, a.prototype.b)", unstubCall.getExpression().toString());
    assertEquals("JSCompiler_unstubMethod(2, a.prototype.b)", unstubCallC.getExpression().toString());
  }

  @Test
  public void testNullDeepestCommonModuleRef() {
    // Given
    Node root = parse("class A {}");
    Node externRoot = parse("{}");
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    // When
    pass.process(externRoot, root);

    // Then
    NameInfo nameInfo = new NameInfo(
        new Symbol(Symbol.Type.VAR, null, "a", null),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        new Property(
            new Symbol(Symbol.Type.FUNCTION, null, "a", null),
            new Node(NodeType.FUNCTION_DEF, null, null, null),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new JSModule(null),
            null,
            null,
            null,
            null,
            null));

    when(nameInfo.getDeepestCommonModuleRef())
        .thenReturn(null);

    // When
    pass.process(externRoot, root);

    // Then
    compiler.verifyReportedErrors(NULL_COMMON_MODULE_ERROR);
  }

  private static Node getMockModule(String name) {
    return new Node(NodeType.MODULE_DEF, null, null, null);
  }

  private static Node parse(String code) {
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.compile(code, options, null, null);
    return root;
  }

  private static Node getFirstChildOfType(Node node, NodeType nodeType) {
    if (node == null) {
      return null;
    }

    Node child = node.getFirstChild();
    while (child != null && child.getNodeType() != nodeType) {
      child = child.getNextSibling();
    }

    return child;
  }

  private static Node getChildOfType(Node node, NodeType nodeType) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child.getNodeType() == nodeType) {
        return child;
      }
    }

    return null;
  }
}
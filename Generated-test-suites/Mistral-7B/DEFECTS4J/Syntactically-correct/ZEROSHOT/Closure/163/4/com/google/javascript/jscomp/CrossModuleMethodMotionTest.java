import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPass.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeFactory;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;
  private IdGenerator idGenerator;
  private AnalyzePrototypeProperties analyzer;
  private JSModuleGraph moduleGraph;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    nodeFactory = Mockito.mock(NodeFactory.class);
    options = Mockito.mock(CompilerOptions.class);
    idGenerator = Mockito.mock(IdGenerator.class);
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    moduleGraph = Mockito.mock(JSModuleGraph.class);
  }

  @Test
  public void testMoveMethods() {
    // Given
    setupCompiler();

    Node root = createTestRoot();
    setupAnalyzer(root);

    CrossModuleMethodMotion testee = new CrossModuleMethodMotion(compiler, idGenerator, false);

    // When
    testee.moveMethods(analyzer.getAllNameInfo());

    // Then
    assertMethodsMoved(root);
  }

  private void setupCompiler() {
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.createNodeFactory()).thenReturn(nodeFactory);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
  }

  private void setupAnalyzer(Node root) {
    when(analyzer.getAllNameInfo()).thenReturn(createNameInfos());

    Node externs = createExterns();
    when(moduleGraph.getExternsNode()).thenReturn(externs);

    when(analyzer.process(eq(externRoot), eq(root))).thenReturn(null);
  }

  private Node createTestRoot() {
    Node root = createTree(
        "function Foo() {}",
        "function Foo.bar() {}",
        "function Foo.baz() {}",
        "function ModuleA() {}",
        "function ModuleA.prototype.qux = Foo.bar;",
        "function ModuleB() {}",
        "function ModuleB.prototype.qux = Foo.baz;");
    return root;
  }

  private List<NameInfo> createNameInfos() {
    NameInfo nameInfoFooBar = createNameInfo("Foo.bar");
    NameInfo nameInfoFooBaz = createNameInfo("Foo.baz");
    NameInfo nameInfoModuleAQux = createNameInfo("ModuleA.qux");
    NameInfo nameInfoModuleBQux = createNameInfo("ModuleB.qux");

    return ImmutableList.of(nameInfoFooBar, nameInfoFooBaz, nameInfoModuleAQux, nameInfoModuleBQux);
  }

  private NameInfo createNameInfo(String name) {
    return Mockito.mock(NameInfo.class);
  }

  private void assertMethodsMoved(Node root) {
    // Foo.bar should be stubbed in ModuleA and unstubbed in ModuleB
    assertFooBarStubbedInModuleA(root);
    assertFooBarUnstubbedInModuleB(root);

    // Foo.baz should be stubbed in ModuleB and unstubbed in ModuleA
    assertFooBazStubbedInModuleB(root);
    assertFooBazUnstubbedInModuleA(root);
  }

  private void assertFooBarStubbedInModuleA(Node root) {
    Node fooBar = findMethodInModule(root, "Foo", "bar");
    assertEquals("JSCompiler_stubMethod(...)", fooBar.toString());
  }

  private void assertFooBarUnstubbedInModuleB(Node root) {
    Node fooBar = findMethodInModule(root, "ModuleB", "qux");
    assertNotNull(fooBar);
    assertNotEquals("JSCompiler_stubMethod(...)", fooBar.toString());
    assertFooBazFunctionInModuleB(fooBar);
  }

  private void assertFooBazStubbedInModuleB(Node root) {
    Node fooBaz = findMethodInModule(root, "ModuleB", "qux");
    assertEquals("JSCompiler_stubMethod(...)", fooBaz.toString());
  }

  private void assertFooBazUnstubbedInModuleA(Node root) {
    Node fooBaz = findMethodInModule(root, "Foo", "baz");
    assertNotNull(fooBaz);
    assertNotEquals("JSCompiler_stubMethod(...)", fooBaz.toString());
  }

  private Node findMethodInModule(Node root, String name, String methodName) {
    List<Node> methods = findAllMethodsInModule(root, name);
    for (Node method : methods) {
      if (method.getFirstChild().getText().equals(methodName)) {
        return method;
      }
    }
    return null;
  }

  private List<Node> findAllMethodsInModule(Node root, String name) {
    List<Node> methods = new ArrayList<>();
    traverseTree(root, new NodeTraversingCompilerPass<Void, Void>() {
      @Override
      public Void visit(Node currentNode, Void data) {
        if (currentNode.isFunction()) {
          String parentName = currentNode.getParent().getText();
          if (parentName.equals(name)) {
            methods.add(currentNode);
          }
        }
        return null;
      }
    });
    return methods;
  }

  private void assertFooBazFunctionInModuleB(Node fooBar) {
    Node fooBazBody = findChildWithName(fooBar, "body");
    assertTrue(fooBazBody.isFunctionCall());
    Node fooBazFunction = fooBazBody.getFirstChild().getFirstChild();
    assertTrue(fooBazFunction.isFunctionDeclaration());
  }

  private Node createExterns() {
    return createTree("", Collections.emptyList());
  }

  private Node createTree(String code, List<String> exports) {
    return nodeFactory.compile(code, null, null, null, null, options);
  }
}
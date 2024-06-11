import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IDEPerson;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.compiler.CompilerOptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CrossModuleMethodMotionTest {

  private CompilerPass crossModuleMethodMotion;
  private AnalyzePrototypeProperties analyzer;
  private AbstractCompiler compiler;
  private JSModuleGraph moduleGraph;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = mock(AbstractCompiler.class);
    moduleGraph = mock(JSModuleGraph.class);
    analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, analyzer.getIdGenerator(), false);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testMoveMethodToAnotherModule() {
    Node root = Node.root();
    Node moduleA = createModule("module A");
    Node moduleB = createModule("module B");
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);
    analyzer.process(Node.nullNode(), root);

    Node prototypeA = createPrototype("prototype A", moduleA);
    Node prototypeB = createPrototype("prototype B", moduleB);

    Node methodA = createFunction("method A", prototypeA);
    prototypeA.addChildToBack(methodA);

    Node methodB = createFunction("method B", prototypeB);
    prototypeB.addChildToBack(methodB);

    Node functionBody = Node.literal("return 'hello world';");
    methodB.setBody(functionBody);

    crossModuleMethodMotion.process(Node.nullNode(), root);

    assertEquals("JSCompiler_stubMethod(<id>)", methodA.toString());
    verify(compiler).getNodeForCodeInsertion(eq(moduleB)).addChildrenToFront(
        IR.exprResult(IR.assign(
            IR.getprop(prototypeB, "method A"),
            IR.call(IR.name("JSCompiler_unstubMethod"),
                anyInt(), functionBody))).copyInformationFromForTree(methodA));
  }

  private Node createPrototype(String name, Node module) {
    Node prototype = Node.newObject("Object", null);
    prototype.setName(name);
    Node prototypeDeclaration = createFunction("constructor", prototype);
    prototype.addChildToBack(prototypeDeclaration);
    module.addChildToBack(prototype);
    return prototype;
  }

  private Node createFunction(String name, Node prototype) {
    Node functionDeclaration = Node.newObject("FunctionDeclaration", null);
    functionDeclaration.setName(name);
    Node functionExpression = Node.newObject("FunctionExpression", null);
    Node body = Node.newBlock("{ }");
    functionExpression.setBody(body);
    functionDeclaration.setBody(functionExpression);
    prototype.addChildToBack(functionDeclaration);
    return functionExpression;
  }

  private Node createModule(String name) {
    Node module = Node.newObject("Module", null);
    module.setName(name);
    return module;
  }
}
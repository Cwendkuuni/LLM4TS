import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Resolution;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckGlobalThis.class})
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal.traverse(traversal, getFunctionNode());

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(notNullValue()));
    assertThat(diagnostic.getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckPrototypedFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node prototypeObject = NodeFactory.createObject();
    Node prototypeFunction = NodeFactory.createFunction(prototypeObject, "prototypeFunction");
    prototypeFunction.setJSDocInfo(JSDocInfo.createFunction("prototypeFunction", new String[] {"prototype"}));

    NodeTraversal.traverse(traversal, prototypeFunction);

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(nullValue()));
  }

  @Test
  public void testCheckConstructorFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node constructorObject = NodeFactory.createObject();
    Node constructorFunction = NodeFactory.createFunction(constructorObject, "constructorFunction", new String[] {"constructor"});
    constructorFunction.setJSDocInfo(JSDocInfo.createFunction("constructorFunction", new String[] {"constructor"}));

    NodeTraversal.traverse(traversal, constructorFunction);

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(nullValue()));
  }

  @Test
  public void testCheckFunctionWithNoAnnotation() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node functionNode = NodeFactory.createFunction(null, "functionWithNoAnnotation");

    NodeTraversal.traverse(traversal, functionNode);

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(nullValue()));
  }

  @Test
  public void testCheckAssignmentToVariable() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node assignNode = NodeFactory.createAssign(NodeFactory.createName("a"), NodeFactory.createName("this"));

    Node rootNode = NodeFactory.createProgram();
    NodeTraversal.traverse(traversal, rootNode);
    NodeTraversal.traverse(traversal, assignNode);

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(nullValue()));
  }

  @Test
  public void testCheckAssignmentToPrototype() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node prototypeObject = NodeFactory.createObject();
    Node prototypeProperty = NodeFactory.createGetProp(prototypeObject, "prototype");
    Node assignNode = NodeFactory.createAssign(prototypeProperty, NodeFactory.createName("this"));

    Node rootNode = NodeFactory.createProgram();
    NodeTraversal.traverse(traversal, rootNode);
    NodeTraversal.traverse(traversal, assignNode);

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(notNullValue()));
    assertThat(diagnostic.getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckAssignmentToDeeplyNestedPrototype() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, WARNING);

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traversal = new NodeTraversal(compiler, options, new DiagnosticCollector());
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    Node prototypeObject1 = NodeFactory.createObject();
    Node prototypeProperty1 = NodeFactory.createGetProp(prototypeObject1, "prototype");
    Node prototypeObject2 = NodeFactory.createObject();
    Node prototypeProperty2 = NodeFactory.createGetProp(prototypeObject2, "prototype");
    Node assignNode = NodeFactory.createAssign(prototypeProperty2, NodeFactory.createName("this"));

    Node rootNode = NodeFactory.createProgram();
    NodeTraversal.traverse(traversal, rootNode);
    NodeTraversal.traverse(traversal, assignNode);

    Node diagnostic = traversal.getLastDiagnostic();
    assertThat(diagnostic, is(notNullValue()));
    assertThat(diagnostic.getMessage(), is("dangerous use of the global 'this' object"));
  }

  private Node getFunctionNode() {
    Node functionNode = NodeFactory.createFunction(null, "functionNode");

    return functionNode;
  }
}
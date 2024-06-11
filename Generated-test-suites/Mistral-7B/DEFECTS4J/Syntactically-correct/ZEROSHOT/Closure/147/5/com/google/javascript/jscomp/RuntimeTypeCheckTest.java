import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeTraversal;

import java.io.IOException;
import java.util.TreeSet;

public class RuntimeTypeCheckTest {

  @SuppressWarnings("unchecked")
  private static <T extends CompilerPass> T newCompilerPass(
      AbstractCompiler compiler) {
    return (T) new RuntimeTypeCheck(compiler, null);
  }

  @org.junit.Test
  public void testProcess() throws IOException {
    CompilerInput input = mock(CompilerInput.class);
    when(input.isExtern()).thenReturn(false);

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(input);
    when(compiler.getCodingConvention()).thenReturn(mock(NodeUtil.CodingConvention.class));

    Node root = NodeUtil.parseJsSource("function Foo() { }", null);

    CompilerPass compilerPass = newCompilerPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();

    Node prototypeNode = constructorNode.getLastChild();
    Node instanceOfCheckerNode = prototypeNode.getChild(0);
    Node implementsCheckerNode = prototypeNode.getChild(1);

    assertEquals(NodeUtil.getFunctionName(functionNode),
        instanceOfCheckerNode.getLastChild().getStringValue());
    assertEquals("instance_of__Foo", instanceOfCheckerNode.getLastChild().getValue());

    TreeSet<String> interfaces = new TreeSet<>();
    interfaces.add("I1");
    interfaces.add("I2");

    ObjectType interfaceType1 = new ObjectType("I1");
    ObjectType interfaceType2 = new ObjectType("I2");

    when(input.getAllDeclaredTypes(anyString(), anyInt(), anyInt()))
        .thenReturn(Iterables.concat(
            ImmutableList.of(interfaceType1, interfaceType2),
            Collections.singletonList((JSType) NodeUtil.intType)));

    Node functionWithInterfacesNode = NodeUtil.parseJsSource(
        "function Bar() { }; Bar.prototype.foo = function() {};", null);

    compilerPass = newCompilerPass(compiler);
    compilerPass.process(null, functionWithInterfacesNode);

    functionNode = NodeUtil.getFunctionNode("Bar", functionWithInterfacesNode);
    prototypeNode = functionNode.getLastChild();

    assertEquals(NodeUtil.getFunctionName(functionNode),
        implementsCheckerNode.getLastChild().getStringValue());
    assertEquals("implements__I1", implementsCheckerNode.getLastChild().getValue());

    assertEquals("implements__I2",
        prototypeNode.getChild(1).getLastChild().getValue());
  }

  @org.junit.Test
  public void testProcessExternClass() throws IOException {
    CompilerInput input = mock(CompilerInput.class);
    when(input.isExtern()).thenReturn(true);

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(input);

    Node root = NodeUtil.parseJsSource(
        "class Foo {}",
        "//@ sourceURL=foo.js");

    CompilerPass compilerPass = newCompilerPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();

    Node prototypeNode = constructorNode.getLastChild();
    Node instanceOfCheckerNode = prototypeNode.getChild(0);

    assertEquals("Foo", instanceOfCheckerNode.getLastChild().getStringValue());
  }

  @org.junit.Test
  public void testProcessExternInterface() throws IOException {
    CompilerInput input = mock(CompilerInput.class);
    when(input.isExtern()).thenReturn(true);

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(input);

    Node root = NodeUtil.parseJsSource(
        "interface I1 {}",
        "//@ sourceURL=interface1.js");

    CompilerPass compilerPass = newCompilerPass(compiler);
    compilerPass.process(null, root);

    Node interfaceNode = root;
    while (!NodeUtil.isInterfaceDeclaration(interfaceNode)) {
      interfaceNode = interfaceNode.getPrevious();
    }

    Node prototypeNode = interfaceNode.getLastChild();
    Node implementsCheckerNode = prototypeNode.getChild(0);

    assertEquals("I1", implementsCheckerNode.getLastChild().getStringValue());
  }

  @org.junit.Test
  public void testProcessValueType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource("function Foo() { }", null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node returnNode = constructorNode.getLastChild().getLastChild();

    Node checkNode = returnNode.getPrevious();
    Node checkerNode = checkNode.getLastChild();

    assertEquals("valueChecker", checkerNode.getStringValue());
  }

  @org.junit.Test
  public void testProcessBooleanValueType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource("function Foo() { }", null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node returnNode = constructorNode.getLastChild().getLastChild();

    Node checkNode = returnNode.getPrevious();
    Node checkerNode = checkNode.getLastChild();

    assertEquals("booleanValueChecker", checkerNode.getStringValue());
  }

  @org.junit.Test
  public void testProcessNumberValueType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource("function Foo() { }", null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node returnNode = constructorNode.getLastChild().getLastChild();

    Node checkNode = returnNode.getPrevious();
    Node checkerNode = checkNode.getLastChild();

    assertEquals("numberValueChecker", checkerNode.getStringValue());
  }

  @org.junit.Test
  public void testProcessStringValueType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource("function Foo() { }", null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node returnNode = constructorNode.getLastChild().getLastChild();

    Node checkNode = returnNode.getPrevious();
    Node checkerNode = checkNode.getLastChild();

    assertEquals("stringValueChecker", checkerNode.getStringValue());
  }

  @org.junit.Test
  public void testProcessVoidType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource("function Foo() { }", null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node returnNode = constructorNode.getLastChild();

    Node checkNode = returnNode.getPrevious();
    Node checkerNode = checkNode.getLastChild();

    assertEquals("voidChecker", checkerNode.getStringValue());
  }

  @org.junit.Test
  public void testProcessInstanceType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource("class Foo {}", null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node prototypeNode = constructorNode.getLastChild();

    Node instanceOfCheckerNode = prototypeNode.getChild(0);

    assertEquals("classChecker", instanceOfCheckerNode.getLastChild().getStringValue());
    assertEquals("Foo", instanceOfCheckerNode.getLastChild().getLastChild().getStringValue());
  }

  @org.junit.Test
  public void testProcessInterfaceType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource(
        "class Foo implements I1, I2 {}",
        null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node prototypeNode = constructorNode.getLastChild();

    Node implementsCheckerNode = prototypeNode.getChild(1);

    assertEquals("interfaceChecker", implementsCheckerNode.getLastChild().getStringValue());
    assertEquals("I1", implementsCheckerNode.getLastChild().getLastChild().getStringValue());

    assertEquals("I2", prototypeNode.getChild(2).getLastChild().getStringValue());
  }

  @org.junit.Test
  public void testProcessUnionType() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput(anyString())).thenReturn(null);

    Node root = NodeUtil.parseJsSource(
        "class Foo { foo(x: number|string) {} }",
        null);

    CompilerPass compilerPass = newRuntimeTypeCheckPass(compiler);
    compilerPass.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode("Foo", root);
    Node constructorNode = functionNode.getFirstChild();
    Node prototypeNode = constructorNode.getLastChild();
    Node fooNode = prototypeNode.getChild(0);

    Node checkNode = fooNode.getLastChild().getLastChild().getLastChild();
    Node checkersNode = checkNode.getPrevious();

    assertEquals(2, checkersNode.getNumChildren());

    Node numberCheckerNode = checkersNode.getChild(0);
    assertEquals("valueChecker", numberCheckerNode.getStringValue());
    assertEquals("number", numberCheckerNode.getLastChild().getStringValue());

    Node stringCheckerNode = checkersNode.getChild(1);
    assertEquals("valueChecker", stringCheckerNode.getStringValue());
    assertEquals("string", stringCheckerNode.getLastChild().getStringValue());
  }

  private static CompilerPass newRuntimeTypeCheckPass(AbstractCompiler compiler) {
    return newRuntimeTypeCheckPass(compiler, null);
  }

  private static CompilerPass newRuntimeTypeCheckPass(
      AbstractCompiler compiler, @Nullable String logFunction) {
    return newRuntimeTypeCheck(compiler, logFunction);
  }

  private static RuntimeTypeCheck newRuntimeTypeCheck(
      AbstractCompiler compiler, @Nullable String logFunction) {
    return new RuntimeTypeCheck(compiler, logFunction);
  }
}
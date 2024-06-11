import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;
  private Node rootNode;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid instance
    methodCompilerPass = new MethodCompilerPass(compiler);

    rootNode = NodeOperations.parse("function Foo() {}");
  }

  @Test
  public void testEmptyExterns() {
    methodCompilerPass.process(null, rootNode);
    assertEquals(0, methodCompilerPass.externMethods.size());
  }

  @Test
  public void testMethodDeclarationInExterns() {
    Node externsNode = NodeOperations.parse("function Foo().bar() {}");
    methodCompilerPass.process(externsNode, rootNode);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
  }

  @Test
  public void testMethodDeclarationWithSignatureInExterns() {
    Node externsNode = NodeOperations.parse("function Foo().bar(arg1: number, arg2: string) {}");
    methodCompilerPass.process(externsNode, rootNode);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
  }

  @Test
  public void testMethodDeclarationWithoutSignatureInExterns() {
    Node externsNode = NodeOperations.parse("function Foo().bar() {}");
    methodCompilerPass.process(externsNode, rootNode);

    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals("bar", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());
  }

  @Test
  public void testMethodDeclarationInSourceCode() {
    Node sourceNode = NodeOperations.parse("function Foo() {" +
        "this.bar = function(arg1: number, arg2: string) {}" +
        "}");
    methodCompilerPass.process(null, sourceNode);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    MethodDefinition methodDefinition = methodCompilerPass.methodDefinitions.get("bar");
    assertNotNull(methodDefinition);
    assertEquals(2, methodDefinition.size());
  }

  @Test
  public void testMethodDeclarationWithInvalidSignatureInSourceCode() {
    Node sourceNode = NodeOperations.parse("function Foo() {" +
        "this.bar = function(arg1: number, arg2: number) {}" + // Should be arg1: string, arg2: number
        "}");
    methodCompilerPass.process(null, sourceNode);

    assertEquals(0, methodCompilerPass.methodDefinitions.get("bar").size());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    Node sourceNode = NodeOperations.parse("function Foo() {" +
        "this.undefinedMethod = 42;" +
        "}");
    methodCompilerPass.process(null, sourceNode);

    // This should not be reachable as undefinedMethod is not a method
    methodCompilerPass.getActingCallback().call(methodCompilerPass, null, null);
  }
}
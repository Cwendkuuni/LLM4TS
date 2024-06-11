import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setup() {
    compiler = new Compiler();
    methodCompilerPass = new MethodCompilerPass(compiler);
  }

  @Test
  public void testExtractMethodsFromExterns() {
    // Given a Node representing the externs
    Node externsNode = createNode("{\n" +
        "  function foo() {}\n" +
        "  function bar() {}\n" +
        "  function baz() {}\n" +
        "}");

    // When processing the externs
    methodCompilerPass.process(externsNode, null);

    // Then the methodCompilerPass should have extracted the methods
    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals("foo", methodCompilerPass.externMethods.iterator().next());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals("baz", methodCompilerPass.externMethods.iterator().next());
  }

  @Test
  public void testExtractMethodsFromSourceCode() {
    // Given a Node representing the source code
    Node sourceNode = createNode("function Foo() {\n" +
        "  this.foo = function() {}\n" +
        "  this.bar = function(arg) {}\n" +
        "}");

    // When processing the source code
    methodCompilerPass.process(null, sourceNode);

    // Then the methodCompilerPass should have extracted the methods
    assertEquals(2, methodCompilerPass.methodDefinitions.size());

    Node fooMethod = methodCompilerPass.methodDefinitions.get("foo");
    assertNotNull(fooMethod);

    Node barMethod = methodCompilerPass.methodDefinitions.get("bar");
    assertNotNull(barMethod);
  }

  @Test
  public void testHandleMethodsWithoutSignaturesInExterns() {
    // Given a Node representing the externs
    Node externsNode = createNode("{\n" +
        "  function foo() {}\n" +
        "  function bar() {}\n" +
        "}");

    // When processing the externs
    methodCompilerPass.process(externsNode, null);

    // Then the methodCompilerPass should have ignored methods without signatures
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("foo", methodCompilerPass.externMethods.iterator().next());

    // And the nonMethodProperties set should contain the ignored method
    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals("bar", methodCompilerPass.nonMethodProperties.iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void testFailureWhenUndefinedFunctionIsEncountered() {
    // Given a Node representing the source code
    Node sourceNode = createNode("function Foo() {\n" +
        "  this.undefinedFunction = undefined;\n" +
        "}");

    // When processing the source code
    methodCompilerPass.process(null, sourceNode);

    // Then an exception should be thrown
  }

  private Node createNode(String nodeContent) {
    return new RhinoCompileUnit().parse(nodeContent).getRootNode();
  }
}
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() {
    // Create a stub for AbstractCompiler
    Class<?> compilerClass = MethodCompilerPass.class;
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      abstract Callback getActingCallback() {
        return null;
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return null;
      }
    };

    // Test empty case
    Node root = null;
    Node externs = null;
    methodCompilerPass.process(externs, root);
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());

    // Test with externs
    externs = Node.newObjectLiteral();
    externs.addChild(Node.newName("foo"));
    externs.addChild(Node.newName("bar"));
    methodCompilerPass.process(externs, root);
    assertEquals(2, methodCompilerPass.externMethods.size());
    assertTrue(methodCompilerPass.externMethods.containsAll(Sets.newHashSet("foo", "bar")));
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());

    // Test with root containing methods
    root = Node.newObjectLiteral();
    root.addChild(Node.newName("foo"));
    root.addChild(Node.newGetProp(Node.newName("foo"), Node.newName("bar")));
    root.addChild(Node.newFunction(Node.newName("bar"), Lists.newArrayList(), Node.newBlock()));
    methodCompilerPass.process(externs, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertTrue(methodCompilerPass.externMethods.contains("foo"));
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertSame(methodCompilerPass.methodDefinitions.get("foo"), root.getChild(0));

    // Test with prototype
    root = Node.newObjectLiteral();
    root.addChild(Node.newName("foo"));
    root.addChild(Node.newGetProp(Node.newName("foo"), Node.newStringLiteral("prototype")));
    root.addChild(Node.newObjectLiteral());
    root.getLastChild().addChild(Node.newName("bar"));
    root.getLastChild().addChild(Node.newFunction(Node.newName("bar"), Lists.newArrayList(), Node.newBlock()));
    methodCompilerPass.process(externs, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertTrue(methodCompilerPass.externMethods.contains("foo"));
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertSame(methodCompilerPass.methodDefinitions.get("foo"), root.getChild(0));
    assertSame(methodCompilerPass.methodDefinitions.get("foo").getFirstChild(),
        root.getChild(0).getLastChild());
  }
}
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodCompilerPassTest {

 private MethodCompilerPass methodCompilerPass;
 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 methodCompilerPass = new MethodCompilerPass(compiler);
 }

 @Test
 public void testExternMethods() {
 // Assume we have a set of extern methods in the externs file
 Set<String> externMethods = Sets.newHashSet("method1", "method2");
 Node externs = Node.objectLiteral();
 for (String method : externMethods) {
 Node methodNode = Node.newObject("method", Node.string(method));
 externs.addChild(methodNode);
 }

 methodCompilerPass.process(externs, null);

 // Check that the expected methods have been added to methodDefinitions
 Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;
 assertEquals(2, methodDefinitions.size());
 assertEquals("method1", methodDefinitions.get("method1").getFirst().getString());
 assertEquals("method2", methodDefinitions.get("method2").get("method").getString());
 }

 @Test
 public void testMethodDefinitionsFromSource() {
 // Assume we have the following code in the source file
 String sourceCode = "function Foo() {\n" +
 "  this.method1 = function() {\n" +
 "  };\n" +
 "  this.method2 = function(arg1, arg2) {\n" +
 "  };\n" +
 "}";

 Node root = Node.parse(sourceCode);
 methodCompilerPass.process(null, root);

 // Check that the expected methods have been added to methodDefinitions
 Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;
 assertEquals(2, methodDefinitions.size());
 assertEquals("method1", methodDefinitions.get("method1").getFirst().getString());
 assertEquals("method2", methodDefinitions.get("method2").get("method").getString());
 }

 @Test(expected = IllegalStateException.class)
 public void testUndefinedMethod() {
 methodCompilerPass.process(null, null);
 methodCompilerPass.getSignatureStore().removeSignature("undefinedMethod");
 Node undefinedMethodNode = Node.function("undefinedMethod");
 methodCompilerPass.addPossibleSignature("undefinedMethod", undefinedMethodNode, null);
 methodCompilerPass.process(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateMethodSignatures() {
 methodCompilerPass.process(null, null);
 methodCompilerPass.getSignatureStore().addSignature("duplicateMethod", mock(Node.class), "");
 methodCompilerPass.process(null, null);
 }
}
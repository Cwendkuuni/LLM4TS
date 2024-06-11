import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerPass;

public class MethodCompilerPassTest {

 private AbstractCompiler compiler;
 private MethodCompilerPass methodCompilerPass;
 private SignatureStore signatureStore;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 methodCompilerPass = new MethodCompilerPass(compiler);
 signatureStore = methodCompilerPass.getSignatureStore();
 }

 @Test
 public void testExternMethodsWithSignatures() {
 Node externs = createTestExterns();
 Node root = createTestRoot();
 methodCompilerPass.process(externs, root);

 assertEquals(1, signatureStore.size());
 assertEquals("testMethod", signatureStore.keySet().iterator().next());
 assertNotNull(methodCompilerPass.methodDefinitions.get("testMethod"));
 }

 @Test
 public void testExternMethodsWithoutSignatures() {
 Node externs = createTestExternsWithoutSignatures();
 Node root = createTestRoot();
 methodCompilerPass.process(externs, root);

 assertEquals(1, signatureStore.size());
 assertEquals("anotherTestMethod", signatureStore.keySet().iterator().next());
 assertNull(methodCompilerPass.methodDefinitions.get("anotherTestMethod"));
 }

 @Test
 public void testNonMethodProperties() {
 Node externs = createTestExternsWithNonMethodProperties();
 Node root = createTestRoot();
 methodCompilerPass.process(externs, root);

 assertEquals(1, methodCompilerPass.externMethods.size());
 assertEquals("testProperty", methodCompilerPass.externMethods.iterator().next());
 assertFalse(methodCompilerPass.methodDefinitions.containsKey("testProperty"));
 }

 @Test(expected = IllegalStateException.class)
 public void testUndefinedFunction() {
 Node externs = createTestExternsWithUndefinedFunction();
 Node root = createTestRoot();
 methodCompilerPass.process(externs, root);
 fail("Should have thrown an IllegalStateException");
 }

 @Test
 public void testFunctionWithInvalidSignature() {
 Node externs = createTestExternsWithInvalidSignature();
 Node root = createTestRoot();
 methodCompilerPass.process(externs, root);

 assertEquals(0, signatureStore.size());
 }

 @Test
 public void testFunctionWithMultipleSignatures() {
 Node externs = createTestExternsWithMultipleSignatures();
 Node root = createTestRoot();
 methodCompilerPass.process(externs, root);

 assertEquals(2, signatureStore.size());
 }

 @Test
 public void testGettingSignaturesFromSourceCode() {
 Node externs = null;
 Node root = createTestSourceCode();
 methodCompilerPass.process(externs, root);

 assertEquals(2, signatureStore.size());
 assertEquals("testMethod", signatureStore.keySet().iterator().next());
 assertNotNull(methodCompilerPass.methodDefinitions.get("testMethod"));
 }

 private Node createTestExterns() {
 Node externs = createNode("{\n" +
 "  'testMethod': function() {}\n" +
 "}");
 return externs;
 }

 private Node createTestExternsWithoutSignatures() {
 Node externs = createNode("{\n" +
 "  'anotherTestMethod': ''\n" +
 "}");
 return externs;
 }

 private Node createTestExternsWithNonMethodProperties() {
 Node externs = createNode("{\n" +
 "  'testProperty': 'value'\n" +
 "}");
 return externs;
 }

 private Node createTestExternsWithUndefinedFunction() {
 Node externs = createNode("{\n" +
 "  'undefinedFunction': undefined\n" +
 "}");
 return externs;
 }

 private Node createTestExternsWithInvalidSignature() {
 Node externs = createNode("{\n" +
 "  'testMethod': 42\n" +
 "}");
 return externs;
 }

 private Node createTestExternsWithMultipleSignatures() {
 Node externs = createNode("{\n" +
 "  'testMethod': function() {},\n" +
 "  'testMethod': function(a) {}\n" +
 "}");
 return externs;
 }

 private Node createTestRoot() {
 Node root = createNode("function Foo() {}\n");
 return root;
 }

 private Node createTestSourceCode() {
 Node root = createNode("function Foo() {\n" +
 "  this.testMethod = function() {\n" +
 "  // some code\n" +
 "  };\n" +
 "}\n");
 return root;
 }

 private Node createNode(String code) {
 return new Node(Token.PROGRAM, null, null, code);
 }

}
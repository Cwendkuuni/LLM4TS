import static org.junit.Assert.*;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

 private AbstractCompiler compiler;
 private MethodCompilerPass methodCompilerPass;
 private SignatureStore signatureStore;

 @Before
 public void setUp() {
 compiler = new Compiler(); // Initialize the compiler
 methodCompilerPass = new MethodCompilerPass(compiler);
 signatureStore = methodCompilerPass.getSignatureStore();
 }

 @Test
 public void testMethodCompilerPass_ExternMethods() {
 // Testing the extraction of methods defined in externs
 String externsCode = "var foo = function() {};\n" +
 "var bar = function(a, b) {};\n";
 Node externs = ParserUtil.parseJs(externsCode);
 methodCompilerPass.process(externs, null);

 // Check that the expected methods were added
 assertEquals(2, signatureStore.getSignatures().size());
 assertEquals("foo", signatureStore.getSignatures().keySet().iterator().next());
 assertEquals("bar", signatureStore.getSignatures().keySet().iterator().next());
 }

 @Test
 public void testMethodCompilerPass_FunctionDeclaration() {
 // Testing the extraction of methods declared in the source code
 String sourceCode = "function foo() {}" +
 "\n" +
 "function bar(a, b) {}" +
 "\n" +
 "function baz() { return 42; }";
 Node source = ParserUtil.parseJs(sourceCode);
 methodCompilerPass.process(null, source);

 // Check that the expected methods were added
 assertEquals(3, signatureStore.getSignatures().size());
 assertEquals("foo", signatureStore.getSignatures().keySet().iterator().next());
 assertEquals("bar", signatureStore.getSignatures().keySet().iterator().next());
 assertEquals("baz", signatureStore.getSignatures().keySet().iterator().next());
 }

 @Test
 public void testMethodCompilerPass_FunctionExpression() {
 // Testing the extraction of methods defined as function expressions
 String sourceCode = "var foo = function() {}" +
 "\n" +
 "var bar = function(a, b) {}";
 Node source = ParserUtil.parseJs(sourceCode);
 methodCompilerPass.process(null, source);

 // Check that the expected methods were added
 assertEquals(2, signatureStore.getSignatures().size());
 assertEquals("foo", signatureStore.getSignatures().keySet().iterator().next());
 assertEquals("bar", signatureStore.getSignatures().keySet().iterator().next());
 }

 @Test
 public void testMethodCompilerPass_IgnoredExternMethods() {
 // Testing the ignoring of methods defined in externs without signatures
 String externsCode = "var foo = function() {};\n" +
 "var qux"; // No signature provided
 methodCompilerPass.process(ParserUtil.parseJs(externsCode), null);

 // Check that the ignored method was not added
 assertEquals(0, signatureStore.getSignatures().size());
 }

 @Test
 public void testMethodCompilerPass_NonMethodProperties() {
 // Testing the ignoring of non-method properties
 String sourceCode = "var foo = 42;\n" +
 "\n" +
 "function bar() {}";
 methodCompilerPass.process(null, ParserUtil.parseJs(sourceCode));

 // Check that the non-method property was not added
 assertEquals(1, signatureStore.getSignatures().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testMethodCompilerPass_UndefinedFunction() {
 // Testing the error handling when an undefined function is encountered
 String sourceCode = "var foo = undefined;\nfunction bar() {}";
 methodCompilerPass.process(null, ParserUtil.parseJs(sourceCode));
 fail("An IllegalStateException should have been thrown");
 }

 @Test
 public void testMethodCompilerPass_MethodWithVariadicArguments() {
 // Testing the handling of methods with variadic arguments
 String sourceCode = "function foo(a, b, ...args) {}";
 methodCompilerPass.process(null, ParserUtil.parseJs(sourceCode));

 // Check that the method was added with the correct signature
 Map<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions.asMap();
 Node fooMethodDefinition = methodDefinitions.get("foo");
 Node fooFunctionNode = fooMethodDefinition.getValue();
 List<Node> fooFunctionParameters = fooFunctionNode.getChildren();
 assertEquals(3, fooFunctionParameters.size());
 assertTrue(fooFunctionParameters.get(2).getType() == Token.ELLIPSIS);
 }

 @Test
 public void testMethodCompilerPass_MethodWithNoArguments() {
 // Testing the handling of methods with no arguments
 String sourceCode = "function foo() {}";
 methodCompilerPass.process(null, ParserUtil.parseJs(sourceCode));

 // Check that the method was added with the correct signature
 Map<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions.asMap();
 Node fooMethodDefinition = methodDefinitions.get("foo");
 Node fooFunctionNode = fooMethodDefinition.getValue();
 assertTrue(fooFunctionNode.getChildren().isEmpty());
 }

 @Test
 public void testMethodCompilerPass_MethodWithMultipleArguments() {
 // Testing the handling of methods with multiple arguments
 String sourceCode = "function foo(a, b, c) {}";
 methodCompilerPass.process(null, ParserUtil.parseJs(sourceCode));

 // Check that the method was added with the correct signature
 Map<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions.asMap();
 Node fooMethodDefinition = methodDefinitions.get("foo");
 Node fooFunctionNode = fooMethodDefinition.getValue();
 List<Node> fooFunctionParameters = fooFunctionNode.getChildren();
 assertEquals(3, fooFunctionParameters.size());
 }

 @Test
 public void testMethodCompilerPass_MethodWithEmptySourceFile() {
 // Testing the handling of an empty source file
 methodCompilerPass.process(null, null);

 // Check that no methods were added
 assertEquals(0, signatureStore.getSignatures().size());
 }
}
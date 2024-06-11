import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private ErrorReporter errorReporter;
 private Node rootNode;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 errorReporter = mock(ErrorReporter.class);
 Parser parser = new Parser();
 rootNode = parser.parse("function evil() { this.useful = undefined; }", null, null);
 }

 @Test
 public void testSafeFunction() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 new NodeTraversal(rootNode, checker).traverse();
 verifyNoErrorReported();
 }

 @Test
 public void testUnsafeFunction() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 Node unsafeFunctionNode = new Node(Token.FUNCTION);
 unsafeFunctionNode.setJSDocInfo(new JSDocInfo("@constructor"));
 Node bodyNode = new Node(Token.BLOCK);
 Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.THIS), new Node(Token.IDENT("useful")));
 bodyNode.addChild(assignmentNode);
 unsafeFunctionNode.addChild(bodyNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.addChild(unsafeFunctionNode);
 new NodeTraversal(parentNode, checker).traverse();
 verifyErrorReported("JSC_USED_GLOBAL_THIS", unsafeFunctionNode.getFirstChild());
 }

 @Test
 public void testUnsafeFunctionWithThisAnnotation() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 Node unsafeFunctionNode = new Node(Token.FUNCTION);
 unsafeFunctionNode.setJSDocInfo(new JSDocInfo("@this", null));
 Node bodyNode = new Node(Token.BLOCK);
 Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.THIS), new Node(Token.IDENT("useful")));
 bodyNode.addChild(assignmentNode);
 unsafeFunctionNode.addChild(bodyNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.addChild(unsafeFunctionNode);
 new NodeTraversal(parentNode, checker).traverse();
 verifyNoErrorReported();
 }

 @Test
 public void testUnsafeFunctionWithPrototypeProperty() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 Node objectNode = new Node(Token.NAME, "obj");
 Node prototypeNode = new Node(Token.GETPROP, objectNode, new Node(Token.IDENT("prototype")));
 Node unsafeFunctionNode = new Node(Token.FUNCTION, prototypeNode);
 Node bodyNode = new Node(Token.BLOCK);
 Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.THIS), new Node(Token.IDENT("useful")));
 bodyNode.addChild(assignmentNode);
 unsafeFunctionNode.addChild(bodyNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.addChild(unsafeFunctionNode);
 new NodeTraversal(parentNode, checker).traverse();
 verifyNoErrorReported();
 }

 @Test
 public void testUnsafeFunctionWithPrototypeSubProperty() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 Node objectNode = new Node(Token.NAME, "obj");
 Node prototypeNode = new Node(Token.GETPROP, objectNode, new Node(Token.IDENT("prototype")));
 Node subPropertyNode = new Node(Token.GETPROP, prototypeNode, new Node(Token.IDENT("subProperty")));
 Node unsafeFunctionNode = new Node(Token.FUNCTION, subPropertyNode);
 Node bodyNode = new Node(Token.BLOCK);
 Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.THIS), new Node(Token.IDENT("useful")));
 bodyNode.addChild(assignmentNode);
 unsafeFunctionNode.addChild(bodyNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.addChild(unsafeFunctionNode);
 new NodeTraversal(parentNode, checker).traverse();
 verifyNoErrorReported();
 }

 @Test
 public void testSafePrototypeMethod() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 Node objectNode = new Node(Token.NAME, "obj");
 Node constructorNode = new Node(Token.FUNCTION, new Node(Token.IDENT("obj")));
 constructorNode.setJSDocInfo(new JSDocInfo("@constructor"));
 Node prototypeNode = constructorNode.addChild(new Node(Token.PROTOTYPE));
 Node methodNode = new Node(Token.METHOD, prototypeNode, new Node(Token.IDENT("method")));
 Node bodyNode = new Node(Token.BLOCK);
 Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.THIS), new Node(Token.IDENT("useful")));
 bodyNode.addChild(assignmentNode);
 methodNode.addChild(bodyNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.addChild(constructorNode);
 new NodeTraversal(parentNode, checker).traverse();
 verifyNoErrorReported();
 }

 @Test
 public void testSafeFunctionWithThisVariable() {
 CheckLevel level = CheckLevel.WARNING;
 CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
 Node variableNode = new Node(Token.VAR, new Node(Token.IDENT("a")));
 Node functionNode = new Node(Token.FUNCTION, null);
 Node bodyNode = new Node(Token.BLOCK);
 Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.IDENT("a"), new Node(Token.THIS)));
 bodyNode.addChild(assignmentNode);
 functionNode.addChild(bodyNode);
 Node parentNode = new Node(Token.SCRIPT);
 parentNode.addChild(functionNode);
 new NodeTraversal(parentNode, checker).traverse();
 verifyNoErrorReported();
 }

 private void verifyErrorReported(String diagnosticType, Node node) {
 when(errorReporter.report).withArg(0).thenAnswer(invocation -> {
 Node errorNode = invocation.getArgument(0);
 DiagnosticType expectedDiagnosticType = DiagnosticType.parse(diagnosticType);
 assertSame(expectedDiagnosticType, errorNode.getDiagnosticType());
 });
 when(compiler.getOptions()).thenReturn(new CompilerOptions());
 when(compiler.getErrorReporter()).thenReturn(errorReporter);
 checker.compiler = compiler;
 new NodeTraversal(node.getParent(), checker).traverse();
 }

 private void verifyNoErrorReported() {
 when(compiler.getOptions()).thenReturn(new CompilerOptions());
 when(compiler.getErrorReporter()).thenReturn(mock(ErrorReporter.class));
 checker.compiler = compiler;
 }
}
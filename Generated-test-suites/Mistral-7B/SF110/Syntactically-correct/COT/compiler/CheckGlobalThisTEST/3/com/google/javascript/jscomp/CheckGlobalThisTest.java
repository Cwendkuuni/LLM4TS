import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.annotation.MockAnnotation;
import org.powermock.api.support.annotation.PowerMockAnnotation;
import org.powermock.api.support.annotation.RestoreAnnotation;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

@RunWith(PowerMockRunner.class)
@PowerMockAnnotation(moduleForClass = CheckGlobalThis.class)
public class CheckGlobalThisTest {

 @MockAnnotation(classToMock = AbstractCompiler.class)
 private AbstractCompiler compiler;

 @RestoreAnnotation(afterClass = MockAnnotation.class)
 @BeforeClass
 public static void setupClass() {
 AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
 CheckGlobalThis.class.setClassLoader(classLoader);
 }

 @Test
 public void testShouldTraverse() {
 NodeTraversal nodeTraversal = new NodeTraversal(compiler);
 CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
 nodeTraversal.addCallback(checkGlobalThis);

 // Traverse a function that is a constructor or has the @constructor annotation.
 Node constructorNode = Node.newFunction("function constructor() {}");
 assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, constructorNode, null));

 // Traverse a function that has the @this annotation.
 Node thisAnnotatedFunctionNode = Node.newFunction("function thisAnnotatedFunction() { @this }");
 assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, thisAnnotatedFunctionNode, null));

 // Traverse a function where there's no logical place to put a @this annotation.
 Node noThisAnnotatedFunctionNode = Node.newFunction("function noThisAnnotatedFunction() {}");
 assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, noThisAnnotatedFunctionNode, null));

 // Don't traverse a function that is not a function or not defined in the global context.
 Node nonFunctionNode = Node.newText("nonFunction");
 assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, nonFunctionNode, null));
 }

 @Test
 public void testShouldReportThis() {
 NodeTraversal nodeTraversal = new NodeTraversal(compiler);
 CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
 nodeTraversal.addCallback(checkGlobalThis);

 Node thisNode = Node.newThis();
 assertTrue(checkGlobalThis.shouldReportThis(thisNode));

 Node assignNode = Node.newAssign(Node.newThis(), Node.newNumber(42));
 assertTrue(checkGlobalThis.shouldReportThis(assignNode.getFirstChild()));
 }

 @Test
 public void testGetFunctionJsDocInfo() {
 Node functionNodeWithJsDoc = Node.newFunction("/** @constructor */ function Foo() {}");
 JSDocInfo jsDoc = CheckGlobalThis.getFunctionJsDocInfo(functionNodeWithJsDoc);
 assertNotNull(jsDoc);
 assertTrue(jsDoc.isConstructor());

 Node functionNodeWithoutJsDoc = Node.newFunction("function Bar() {}");
 jsDoc = CheckGlobalThis.getFunctionJsDocInfo(functionNodeWithoutJsDoc);
 assertNull(jsDoc);
 }
}
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionRewriterTest {

 private Compiler compiler;
 private FunctionRewriter functionRewriter;

 @Before
 public void setUp() {
 compiler = new Compiler();
 functionRewriter = new FunctionRewriter(compiler);
 }

 @Test
 public void testEmptyFunctionReducer() {
 Node node = new Node(Token.FUNCTION, 1, 1, null);
 node.setLineAndCharPositionInOriginal(1, 1);
 Node emptyFunction = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);

 Node originalRoot = compiler.parseSyntheticCode("root", "function test() {}");
 Node rewrittenRoot = new Node(Token.CALL, 1, 1, null);
 rewrittenRoot.putBooleanProp(Node.FREE_CALL, true);
 rewrittenRoot.addChildToBack(new Node(Token.NAME, 1, 1, "emptyFn"));
 originalRoot.replaceChildren(0, rewrittenRoot);

 functionRewriter.process(null, originalRoot);

 assertTrue(originalRoot.getFirstChild().isFunction());
 assertFalse(originalRoot.getFirstChild().isGetter());
 assertFalse(originalRoot.getFirstChild().isSetter());

 assertEquals(0, originalRoot.getFirstChild().getNumChildren());

 Node rewrittenFunction = originalRoot.getFirstChild().getFirstChild();
 assertTrue(rewrittenFunction.isFunction());
 assertFalse(rewrittenFunction.isGetter());
 assertFalse(rewrittenFunction.isSetter());

 assertEquals(0, rewrittenFunction.getNumChildren());
 }

 @Test
 public void testIdentityReducer() {
 Node node = new Node(Token.FUNCTION, 1, 1, null);
 node.setLineAndCharPositionInOriginal(1, 1);
 node.setName("IdentityFunction");
 node.addChildToBack(new Node(Token.PARAMETER, 1, 1, null, "a"));
 node.addChildToBack(new Node(Token.RETURN, 1, 1, null));
 node.getLastChild().addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "a"));

 Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
 Node originalRoot = compiler.parseSyntheticCode("root", "function test(a) { return a }");
 Node rewrittenRoot = new Node(Token.CALL, 1, 1, null);
 rewrittenRoot.putBooleanProp(Node.FREE_CALL, true);
 rewrittenRoot.addChildToBack(new Node(Token.NAME, 1, 1, "identityFn"));
 rewrittenRoot.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "a"));
 originalRoot.replaceChildren(0, rewrittenRoot);

 functionRewriter.process(null, originalRoot);

 assertTrue(originalRoot.getFirstChild().isFunction());
 assertFalse(originalRoot.getFirstChild().isGetter());
 assertFalse(originalRoot.getFirstChild().isSetter());

 assertEquals(2, originalRoot.getFirstChild().getNumChildren());

 Node originalFunction = originalRoot.getFirstChild();
 assertTrue(originalFunction.isFunction());
 assertFalse(originalFunction.isGetter());
 assertFalse(originalFunction.isSetter());

 assertEquals(2, originalFunction.getNumChildren());
 assertEquals("IdentityFunction", originalFunction.getName());
 assertEquals("a", originalFunction.getParameter(0).getString());

 Node rewrittenFunction = originalFunction.getChild(0);
 assertTrue(rewrittenFunction.isFunction());
 assertFalse(rewrittenFunction.isGetter());
 assertFalse(rewrittenFunction.isSetter());

 assertEquals(1, rewrittenFunction.getNumChildren());
 assertEquals("IdentityFunction", rewrittenFunction.getName());
 assertEquals("a", rewrittenFunction.getParameter(0).getString());
 }

 @Test
 public void testSingleReturnStatementReducer() {
 Node node = new Node(Token.FUNCTION, 1, 1, null);
 node.setLineAndCharPositionInOriginal(1, 1);
 node.setName("SingleReturnStatementFunction");
 node.addChildToBack(new Node(Token.PARAMETER, 1, 1, null, "a"));
 node.addChildToBack(new Node(Token.RETURN, 1, 1, null));
 node.getLastChild().addChildToBack(new Node(Token.INTEGER, 1, 1, 10));

 Node originalRoot = compiler.parseSyntheticCode("root", "function test(a) { return 10 }");
 Node rewrittenRoot = new Node(Token.CALL, 1, 1, null);
 rewrittenRoot.putBooleanProp(Node.FREE_CALL, true);
 rewrittenRoot.addChildToBack(new Node(Token.NAME, 1, 1, "returnconst"));
 rewrittenRoot.addChildToBack(new Node(Token.INTEGER, 1, 1, 10));
 originalRoot.replaceChildren(0, rewrittenRoot);

 functionRewriter.process(null, originalRoot);

 assertTrue(originalRoot.getFirstChild().isFunction());
 assertFalse(originalRoot.getFirstChild().isGetter());
 assertFalse(originalRoot.getFirstChild().isSetter());

 assertEquals(2, originalRoot.getFirstChild().getNumChildren());

 Node originalFunction = originalRoot.getFirstChild();
 assertTrue(originalFunction.isFunction());
 assertFalse(originalFunction.isGetter());
 assertFalse(originalFunction.isSetter());

 assertEquals(2, originalFunction.getNumChildren());
 assertEquals("SingleReturnStatementFunction", originalFunction.getName());
 assertEquals("a", originalFunction.getParameter(0).getString());

 Node rewrittenFunction = originalFunction.getChild(0);
 assertTrue(rewrittenFunction.isFunction());
 assertFalse(rewrittenFunction.isGetter());
 assertFalse(rewrittenFunction.isSetter());

 assertEquals(1, rewrittenFunction.getNumChildren());
 assertEquals("returnconst", rewrittenFunction.getName());
 assertEquals(10, rewrittenFunction.getFirstChild().getInteger());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonReducibleFunction() {
 Node node = new Node(Token.FUNCTION, 1, 1, null);
 node.setLineAndCharPositionInOriginal(1, 1);
 node.setName("NonReducibleFunction");
 node.addChildToBack(new Node(Token.PARAMETER, 1, 1, null, "a"));
 node.addChildToBack(new Node(Token.RETURN, 1, 1, null));
 node.addChildToBack(new Node(Token.IF, 1, 1, null));
 node.addChildToBack(new Node(Token.IDENTIFIER, 1, 1, "x"));
 node.addChildToBack(new Node(Token.RETURN, 1, 1, null));
 node.addChildToBack(new Node(Token.ELSE, 1, 1, null));
 node.addChildToBack(new Node(Token.RETURN, 1, 1, null));

 Node originalRoot = compiler.parseSyntheticCode("root", "function test(a) { if (x) { return 1; } else { return 2; } }");
 functionRewriter.process(null, originalRoot);
 }
}
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FunctionRewriterTest {

 private Compiler compiler;
 private FunctionRewriter functionRewriter;

 @Before
 public void setUp() {
 compiler = mock(Compiler.class);
 functionRewriter = new FunctionRewriter(compiler);
 }

 @Test
 public void testEmptyFunctionReduction() {
 Node root = new Node(Token.BLOCK, 0, 0);
 Node emptyFunction = new Node(Token.FUNCTION, 0, 0);
 root.addChildToBack(emptyFunction);

 Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
 int savings = functionRewriter.process(null, root).getCost() - helperCode.getCost();

 assertEquals("Empty function reduction should save bytes", savings, FunctionRewriter.SAVINGS_THRESHOLD);
 }

 @Test
 public void testIdentityFunctionReduction() {
 Node root = new Node(Token.BLOCK, 0, 0);
 Node identityFunction = new Node(Token.FUNCTION, 0, 0);
 Node argList = new Node(Token.LIST, 0, 0);
 Node paramNode = new Node(Token.IDENT, 0, 0, "arg");
 argList.addChildToBack(paramNode);
 identityFunction.addChildToBack(argList);
 identityFunction.addChildToBack(new Node(Token.RETURN, 0, 0, paramNode));
 root.addChildToBack(identityFunction);

 Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
 int savings = functionRewriter.process(null, root).getCost() - helperCode.getCost();

 assertEquals("Identity function reduction should save bytes", savings, FunctionRewriter.SAVINGS_THRESHOLD);
 }

 @Test
 public void testSingleReturnStatementFunctionReduction() {
 Node root = new Node(Token.BLOCK, 0, 0);
 Node singleReturnStatementFunction = new Node(Token.FUNCTION, 0, 0);
 Node argList = new Node(Token.LIST, 0, 0);
 Node paramNode = new Node(Token.IDENT, 0, 0, "arg");
 argList.addChildToBack(paramNode);
 singleReturnStatementFunction.addChildToBack(argList);
 singleReturnStatementFunction.addChildToBack(new Node(Token.RETURN, 0, 0, new Node(Token.NUM, 0, 0, 10)));
 root.addChildToBack(singleReturnStatementFunction);

 Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class);
 int savings = functionRewriter.process(null, root).getCost() - helperCode.getCost();

 assertEquals("Single return statement function reduction should save bytes", savings, FunctionRewriter.SAVINGS_THRESHOLD);
 }

 @Test(expected = IllegalStateException.class)
 public void testNonReducibleFunction() {
 Node root = new Node(Token.BLOCK, 0, 0);
 Node nonReducibleFunction = new Node(Token.FUNCTION, 0, 0);
 Node argList = new Node(Token.LIST, 0, 0);
 Node paramNode = new Node(Token.IDENT, 0, 0, "arg");
 argList.addChildToBack(paramNode);
 nonReducibleFunction.addChildToBack(argList);
 nonReducibleFunction.addChildToBack(new Node(Token.IF, 0, 0, new Node(Token.RETURN, 0, 0, new Node(Token.NUM, 0, 0, 10)),
                                                                       new Node(Token.RETURN, 0, 0, new Node(Token.NUM, 0, 0, 20))));
 root.addChildToBack(nonReducibleFunction);

 functionRewriter.process(null, root);
 }

 @Test
 public void testGetterReduction() {
 Node root = new Node(Token.BLOCK, 0, 0);
 Node getterFunction = new Node(Token.FUNCTION, 0, 0);
 Node getterPropName = new Node(Token.IDENT, 0, 0, "propName");
 getterFunction.addChildToBack(new Node(Token.VAR, 0, 0, getterPropName, null, null, new Node(Token.FUNCTION, 0, 0, null, null, null, new Node(Token.RETURN, 0, 0, getterPropName))));
 getterFunction.addChildToBack(new Node(Token.RETURN, 0, 0, getterFunction));
 root.addChildToBack(getterFunction);

 Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.GetterReducer.class);
 int savings = functionRewriter.process(null, root).getCost() - helperCode.getCost();

 assertEquals("Getter function reduction should save bytes", savings, FunctionRewriter.SAVINGS_THRESHOLD);
 }

 @Test
 public void testSetterReduction() {
 Node root = new Node(Token.BLOCK, 0, 0);
 Node setterFunction = new Node(Token.FUNCTION, 0, 0);
 Node setterPropName = new Node(Token.IDENT, 0, 0, "propName");
 Node setterArg = new Node(Token.IDENT, 0, 0, "value");
 setterFunction.addChildToBack(new Node(Token.VAR, 0, 0, setterPropName, null, null, new Node(Token.FUNCTION, 0, 0, null, null, null, new Node(Token.ASSIGN, 0, 0, setterPropName, new Node(Token.IDENT, 0, 0, "this"), setterArg))));
 setterFunction.addChildToBack(new Node(Token.RETURN, 0, 0, getterFunction));
 root.addChildToBack(setterFunction);

 Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.SetterReducer.class);
 int savings = functionRewriter.process(null, root).getCost() - helperCode.getCost();

 assertEquals("Setter function reduction should save bytes", savings, FunctionRewriter.SAVINGS_THRESHOLD);
 }

 @Test
 public void testInvalidHelperCode() {
 functionRewriter.process(null, new Node(Token.BLOCK, 0, 0));
 }
}
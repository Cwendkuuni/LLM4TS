import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = new Supplier<String>() {
      private int i = 0;

      @Override
      public String get() {
        return "test_" + Integer.toString(i++);
      }
    };
  }

  @org.junit.Test
  public void testMutate() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(Node.newText("return 1;"))).cloneTree();

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node callNode = Node.newCall(Node.newThis(), "test", Node.newList(),
        Node.newList(), Node.newList(), Node.newList(), null).cloneTree();

    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate("test", functionNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @org.junit.Test
  public void testAliasAndInlineArguments() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(Node.newText("return a + b;"))).cloneTree();

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("a", Node.newNumber(1));
    argMap.put("b", Node.newNumber(2));

    Set<String> namesToAlias = Set.of("a");

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node mutatedNode = functionToBlockMutator.aliasAndInlineArguments(
        functionNode, argMap, namesToAlias);

    assertNotNull(mutatedNode);
  }

  @org.junit.Test
  public void testReplaceReturns() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(Node.newText("return 1;"))).cloneTree();

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = functionToBlockMutator.replaceReturns(
        functionNode, resultName, labelName, false);

    assertNotNull(mutatedNode);
  }

  @org.junit.Test
  public void testReplaceReturnsWithResult() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(Node.newText("return a;"))).cloneTree();

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = functionToBlockMutator.replaceReturns(
        functionNode, resultName, labelName, true);

    assertNotNull(mutatedNode);
  }

  @org.junit.Test
  public void testReplaceReturnsInLoop() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(Node.newText("return a;"))).cloneTree();

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String resultName = "result";
    String labelName = "label";

    Node mutatedNode = functionToBlockMutator.replaceReturns(
        functionNode, resultName, labelName, true).cloneTree();

    LabelNameSupplier labelNameSupplier = new LabelNameSupplier(safeNameIdSupplier);
    String newLabelName = labelNameSupplier.get();

    Node loopNode = Node.newBlock(
        new Node(Token.FOR, Node.newName("i"), Node.newNumber(0),
            Node.newNumber(1), Node.newNumber(1), Node.newList(),
            NodeUtil.getFunctionBody(Node.newCall(Node.newName("test"),
                Node.newList(), Node.newList(), Node.newList(),
                Node.newList(), Node.newList(), Node.newList())))).cloneTree();

    mutatedNode = functionToBlockMutator.replaceReturns(
        mutatedNode, resultName, newLabelName, true);

    assertNotNull(mutatedNode);
  }

  @org.junit.Test
  public void testMakeLocalNamesUnique() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(
            Node.newList(Node.newVar("x"), Node.newVar("x"), Node.newText(";")))).cloneTree();

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    functionToBlockMutator.makeLocalNamesUnique(functionNode, false);

    Node mutatedNode = functionToBlockMutator.mutate("test", functionNode, null,
        null, false, false).cloneTree();

    assertNotNull(mutatedNode);
  }

  @org.junit.Test
  public void testReplaceUninitializedVariables() {
    Node functionNode = Node.newFunction("test", Node.newJsDocCommentList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList(),
        Node.newList(), NodeUtil.getFunctionBody(
            Node.newList(Node.newVar("x"), Node.newText(";"), Node.newText(";"),
                Node.newText("return x;")))).cloneTree();

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node mutatedNode = functionToBlockMutator.mutate("test", functionNode, null,
        null, false, false).cloneTree();

    assertNotNull(mutatedNode);
  }
}
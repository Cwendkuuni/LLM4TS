import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.google.common.truth.Truth.assertThat;

public class LiveVariablesAnalysisTest {

 private Scope mockScope;
 private ControlFlowGraph<Node> mockCFG;

 @Before
 public void setup() {
 mockScope = mock(Scope.class);
 mockCFG = mock(ControlFlowGraph.class);
 }

 @Test
 public void testLiveVariablesAnalysisEmpty() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Set<Var> expected = Sets.newHashSet();
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }

 @Test
 public void testLiveVariablesAnalysisWithSimpleLocalVariables() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var var1 = mock(Var.class);
 Var var2 = mock(Var.class);
 when(mockScope.getVar("var1")).thenReturn(var1);
 when(mockScope.getVar("var2")).thenReturn(var2);
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class));
 Set<Var> expected = Sets.newHashSet(var1, var2);
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }

 @Test(expected = AssertionError.class)
 public void testLiveVariablesAnalysisWithEscapedVariable() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var escapedVar = mock(Var.class);
 when(mockScope.getVar("escapedVar")).thenReturn(escapedVar);
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class));
 analysis.escaped.add(escapedVar);
 }

 @Test
 public void testLiveVariablesAnalysisWithAssignments() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var var1 = mock(Var.class);
 Var var2 = mock(Var.class);
 when(mockScope.getVar("var1")).thenReturn(var1);
 when(mockScope.getVar("var2")).thenReturn(var2);
 Node assignNode1 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var1,
 Node.newIntLiteral(1));
 Node assignNode2 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var2,
 Node.newIntLiteral(2));
 Node rootNode = Node.newBlock(NodeUtil.createList(assignNode1, assignNode2));
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class), rootNode);
 Set<Var> expected = Sets.newHashSet(var1, var2);
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }

 @Test
 public void testLiveVariablesAnalysisWithConditionals() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var var1 = mock(Var.class);
 Var var2 = mock(Var.class);
 when(mockScope.getVar("var1")).thenReturn(var1);
 when(mockScope.getVar("var2")).thenReturn(var2);
 Node ifNode = Node.newIf(NodeUtil.newBooleanLiteral(true));
 Node assignNode1 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var1,
 Node.newIntLiteral(1));
 Node assignNode2 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var2,
 Node.newIntLiteral(2));
 Node assignNode3 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var1,
 Node.newIntLiteral(3));
 ifNode.setFirstChild(assignNode1);
 ifNode.setLastChild(assignNode3);
 Node rootNode = Node.newBlock(NodeUtil.createList(ifNode));
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class), rootNode);
 Set<Var> expected = Sets.newHashSet(var1, var2);
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }

 @Test
 public void testLiveVariablesAnalysisWithLoops() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var var1 = mock(Var.class);
 Var var2 = mock(Var.class);
 when(mockScope.getVar("var1")).thenReturn(var1);
 when(mockScope.getVar("var2")).thenReturn(var2);
 Node forNode = Node.newFor(NodeUtil.newName("i"), NodeUtil.newIntLiteral(0),
 NodeUtil.newIntLiteral(5), null);
 Node assignNode = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var1,
 Node.newName("i"));
 forNode.setLastChild(assignNode);
 Node rootNode = Node.newBlock(NodeUtil.createList(forNode));
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class), rootNode);
 Set<Var> expected = Sets.newHashSet(var1);
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }

 @Test
 public void testLiveVariablesAnalysisWithFunctions() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var var1 = mock(Var.class);
 Var var2 = mock(Var.class);
 when(mockScope.getVar("var1")).thenReturn(var1);
 when(mockScope.getVar("var2")).thenReturn(var2);
 Node functionNode = Node.newFunctionDeclaration("func", NodeUtil.createList(), NodeUtil.createList());
 Node assignNode1 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var1,
 Node.newIntLiteral(1));
 Node assignNode2 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var2,
 Node.newIntLiteral(2));
 functionNode.setLastChild(Node.newBlock(NodeUtil.createList(assignNode1, assignNode2)));
 Node rootNode = Node.newBlock(NodeUtil.createList(functionNode));
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class), rootNode);
 Set<Var> expected = Sets.newHashSet(var1, var2);
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }

 @Test
 public void testLiveVariablesAnalysisWithExceptionHandling() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCFG, mockScope, mock(AbstractCompiler.class));
 Var var1 = mock(Var.class);
 Var var2 = mock(Var.class);
 when(mockScope.getVar("var1")).thenReturn(var1);
 when(mockScope.getVar("var2")).thenReturn(var2);
 Node tryNode = Node.newTry();
 Node tryBlock = Node.newBlock(NodeUtil.createList());
 Node catchBlock = Node.newBlock(NodeUtil.createList());
 Node assignNode1 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var1,
 Node.newIntLiteral(1));
 Node assignNode2 = Node.newNode(Token.ASSIGN, Token.OP_ASSIGN, var2,
 Node.newIntLiteral(2));
 Node throwNode = Node.newThrowStatement();
 tryBlock.setLastChild(Node.newBlock(NodeUtil.createList(assignNode1, assignNode2)));
 catchBlock.setFirstChild(throwNode);
 tryNode.setTry(tryBlock);
 tryNode.setHandler(catchBlock);
 Node rootNode = Node.newBlock(NodeUtil.createList(tryNode));
 analysis.computeEscaped(mockScope, analysis.escaped, mock(AbstractCompiler.class), rootNode);
 Set<Var> expected = Sets.newHashSet(var1, var2);
 assertThat(analysis.getEscapedLocals()).isEqualTo(expected);
 }
}
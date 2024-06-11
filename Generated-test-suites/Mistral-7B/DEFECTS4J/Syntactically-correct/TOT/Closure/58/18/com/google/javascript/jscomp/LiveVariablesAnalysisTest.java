import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.rhino.Node;
import static com.google.common.truth.Truth.assertThat;

public class LiveVariablesAnalysisTest {

 private Scope jsScope;
 private ControlFlowGraph<Node> cfg;
 private LiveVariablesAnalysis analysis;

 @Before
 public void setUp() {
 jsScope = new Scope();
 jsScope.declareLocal("a");
 jsScope.declareLocal("b");
 jsScope.declareLocal("c");
 jsScope.declareLocal("d");
 jsScope.declareVariable("e", Node.NULL);
 jsScope.declareVariable("f", Node.UNDEFINED);

 cfg = new ControlFlowGraph<Node>(jsScope.getRootNode(), null);
 analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 }

 @Test
 public void testEmptyFunction() {
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 assertThat(result.isLive("a")).isFalse();
 assertThat(result.isLive("b")).isFalse();
 assertThat(result.isLive("c")).isFalse();
 }

 @Test
 public void testAssignment() {
 jsScope.setCurrentPosition(1);
 Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 jsScope.push(assign);
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 assertThat(result.isLive("a")).isTrue();
 }

 @Test
 public void testConditionalAssignment() {
 jsScope.setCurrentPosition(1);
 Node ifNode = new Node(Token.IF, new Node(Token.BOOL, Node.FALSE));
 Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 jsScope.push(ifNode);
 jsScope.push(assign);
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 assertThat(result.isLive("a")).isFalse();
 }

 @Test
 public void testLoop() {
 jsScope.setCurrentPosition(1);
 Node whileNode = new Node(Token.WHILE, NodeUtil.createLiteral(true));
 Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 jsScope.push(whileNode);
 jsScope.push(assign);
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 // The loop variable 'a' should be live throughout the loop
 assertThat(result.isLive("a")).isTrue();
 }

 @Test
 public void testFunctionCall() {
 jsScope.setCurrentPosition(1);
 Node call = new Node(Token.NAME, "functionName");
 jsScope.push(call);
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 // The function's local variables should be live throughout the call
 assertThat(result.isLive("functionName_a")).isTrue();
 }

 @Test(expected = RuntimeException.class)
 public void testException() {
 jsScope.setCurrentPosition(1);
 Node throwExpr = new Node(Token.THROW, Node.UNDEFINED);
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 // An exception should cause all variables to be considered live
 // (since the flow might resume at a point where they are read)
 assertThat(result.isLive("a")).isTrue();
 jsScope.push(throwExpr);
 }

 @Test
 public void testVariableEscaped() {
 jsScope.setCurrentPosition(1);
 Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "functionName"), null, null);
 Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 jsScope.declareLocal("a");
 jsScope.declareLocal("b");
 jsScope.push(function);
 jsScope.push(assign);
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
 LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice(jsScope.getRootNode(), null);
 // 'a' should be considered escaped since it can be returned from the function
 // and accessed outside of it
 assertThat(analysis.getEscapedLocals().contains(jsScope.getVar("a"))).isTrue();
 }
}
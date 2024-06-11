import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

 private ControlFlowGraph<Node> cfg;
 private Scope jsScope;
 private MaybeReachingVariableUse analysis;

 @Before
 public void setUp() {
 jsScope = new Scope();
 cfg = new ControlFlowGraph<>();
 analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
 }

 @Test
 public void testSimpleVariableUse() {
 Node program = makeNode("function foo() { var x = 1; x = 2; alert(x); }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getLastChild(), uses.get(0));
 }

 @Test
 public void testVariableAssignmentBeforeUse() {
 Node program = makeNode("function foo() { x = 1; var x = 'hello'; alert(x); }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getLastChild(), uses.get(0));
 }

 @Test
 public void testVariableUseBeforeAssignment() {
 Node program = makeNode("function foo() { alert(x); x = 1; }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertTrue(uses.isEmpty());
 }

 @Test
 public void testVariableUseAfterAssignment() {
 Node program = makeNode("function foo() { x = 1; alert(x); x = 2; }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getLastChild(), uses.get(0));
 }

 @Test
 public void testVariableUseInCondition() {
 Node program = makeNode("function foo() { if (x) { alert(x); } x = 1; }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getChild(1), uses.get(0));
 }

 @Test
 public void testVariableUseInLoop() {
 Node program = makeNode("for (var i = 0; i < 10; i++) { alert(i); }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("i", program);
 assertFalse(uses.isEmpty());
 for (Node use : uses) {
 if (use.getParent() != program.getChild(0)) {
 fail("Unexpected use of 'i' found.");
 }
 }
 }

 @Test
 public void testVariableUseInFunctionCall() {
 Node program = makeNode("function foo() { alert(bar()); } function bar() { var x = 1; }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getChild(0), uses.get(0));
 }

 @Test
 public void testVariableEscapedFromFunction() {
 Node program = makeNode("function foo() { var x = 1; return function() { alert(x); } } var y = foo();");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertTrue(uses.isEmpty());
 }

 @Test
 public void testVariableEscapedFromVariable() {
 Node program = makeNode("var x = 1; var y = x; function foo() { alert(x); }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getLastChild(), uses.get(0));
 }

 @Test
 public void testVariableInConditionAndAssignment() {
 Node program = makeNode("function foo() { if (x) { x = 1; } alert(x); }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getLastChild(), uses.get(0));
 }

 @Test
 public void testVariableInFunctionCallAndAssignment() {
 Node program = makeNode("function foo() { x = 1; alert(bar()); x = 2; } function bar() { }");
 analysis.computeAll(program);
 List<Node> uses = analysis.getUses("x", program);
 assertFalse(uses.isEmpty());
 assertEquals(program.getLastChild(), uses.get(0));
 }

 private Node makeNode(String source) {
 return NodeUtil.makeNode(new Node(Token.DUMMY, 0, source));
 }
}
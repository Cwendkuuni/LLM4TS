import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleCase() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.BLOCK, null, null, null);
    Node def = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node use = new AstNode(Token.NAME, "x", null, null);

    root.appendChild(def);
    root.appendChild(use);

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", def);
    assertTrue(uses.contains(use));
  }

  @Test
  public void testConditional() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.BLOCK, null, null, null);
    Node def1 =
        new AstNode(
            Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node cond = new AstNode(Token.NAME, "condition", null, null);
    Node use = new AstNode(Token.NAME, "x", null, null);
    Node def2 = new AstNode(Token.ASSIGN, null, null, use);

    root.appendChild(def1);
    root.appendChild(cond);
    root.appendChild(def2);
    root.appendChild(use);

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", def1);
    assertTrue(uses.contains(use));
  }

  @Test
  public void testLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.BLOCK, null, null, null);
    Node var = new AstNode(Token.VAR, null, null, new AstNode(Token.NAME, "x", null, null));
    Node init = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node condition = new AstNode(Token.NAME, "condition", null, null);
    Node increment = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.ADD, null, init, new AstNode(Token.INT_LITERAL, 1, null, null)));
    Node body = new AstNode(Token.BLOCK, null, null, null);
    Node assign = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node use = new AstNode(Token.NAME, "x", null, null);

    root.appendChild(var);
    root.appendChild(init);
    root.appendChild(condition);
    root.appendChild(body);
    root.appendChild(increment);
    root.appendChild(assign);
    root.appendChild(use);

    body.appendChild(assign);
    body.appendChild(use);

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", init);
    assertTrue(uses.contains(use));
  }

  @Test
  public void testFunction() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.FUNCTION, null, null, null);
    Node param = new AstNode(Token.VAR, null, null, new AstNode(Token.NAME, "x", null, null));
    Node def = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node use = new AstNode(Token.NAME, "x", null, null);

    root.appendChild(param);
    root.appendChild(def);
    root.appendChild(use);

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", def);
    assertTrue(uses.contains(use));
  }

  @Test
  public void testEscape() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.BLOCK, null, null, null);
    Node var = new AstNode(Token.VAR, null, null, new AstNode(Token.NAME, "x", null, null));
    Node assign = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node use = new AstNode(Token.NAME, "x", null, null);
    Node esc = new AstNode(Token.NAME, "escape", null, null);

    root.appendChild(var);
    root.appendChild(assign);
    root.appendChild(use);
    root.appendChild(esc);
    root.appendChild(new AstNode(Token.ASSIGN, null, null, use));

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", assign);
    assertFalse(uses.contains(esc));
  }

  @Test
  public void testNestedFunction() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.FUNCTION, null, null, null);
    Node inner = new AstNode(Token.FUNCTION, null, null, null);
    Node innerParam = new AstNode(Token.VAR, null, null, new AstNode(Token.NAME, "x", null, null));
    Node innerDef = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node innerUse = new AstNode(Token.NAME, "x", null, null);
    Node outerParam = new AstNode(Token.VAR, null, null, new AstNode(Token.NAME, "y", null, null));
    Node outerDef = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "y", null, null));
    Node outerUse = new AstNode(Token.NAME, "y", null, null);

    root.appendChild(inner);
    inner.appendChild(innerParam);
    inner.appendChild(innerDef);
    inner.appendChild(innerUse);
    inner.appendChild(outerParam);
    inner.appendChild(outerDef);
    inner.appendChild(outerUse);
    root.appendChild(outerDef);
    root.appendChild(outerUse);

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", innerDef);
    assertTrue(uses.contains(innerUse));
  }

  @Test
  public void testInitFromUse() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, new CompilerOptions());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node root = new AstNode(Token.BLOCK, null, null, null);
    Node def = new AstNode(Token.ASSIGN, null, null, new AstNode(Token.NAME, "x", null, null));
    Node use = new AstNode(Token.NAME, "x", null, null);
    Node assign = new AstNode(Token.ASSIGN, null, null, use);

    root.appendChild(def);
    root.appendChild(assign);
    root.appendChild(use);

    analysis.process(root);
    Collection<Node> uses = analysis.getUses("x", def);
    assertTrue(uses.contains(use));
  }
}
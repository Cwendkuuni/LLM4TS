import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.ast.AbstractNode;
import com.google.javascript.jscomp.ast.CallExpr;
import com.google.javascript.jscomp.ast.ExprNode;
import com.google.javascript.jscomp.ast.Name;
import com.google.javascript.jscomp.ast.NodeFactory;
import com.google.javascript.jscomp.pass.AbstractPass;

import java.util.Arrays;
import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private AbstractPass checkSideEffectsPass;

  @org.junit.Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.QUIRKS, "JSC_USELESS_CODE");
    checkSideEffectsPass = new CheckSideEffects(compiler, CheckLevel.QUIRKS, true);
  }

  @org.junit.Test
  public void testCheckSideEffectsWithNoUsefulCode() {
    SourceFile sourceFile = createSourceFile("Test.java", "void foo() {}");
    CompilerInput input = new CompilerInput(sourceFile);

    NodeTraversal traversal = new NodeTraversal(compiler, input, options);
    checkSideEffectsPass.process(input, input.getRoot());

    Node result = input.getRoot();
    assertTrue("No warnings should have been produced", result.getChildren().isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsWithUsefulCode() {
    SourceFile sourceFile = createSourceFile("Test.java", "int x = 1;");
    CompilerInput input = new CompilerInput(sourceFile);

    NodeTraversal traversal = new NodeTraversal(compiler, input, options);
    checkSideEffectsPass.process(input, input.getRoot());

    Node result = input.getRoot();
    assertFalse("A warning should have been produced", result.getChildren().isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsWithForStatement() {
    SourceFile sourceFile = createSourceFile("Test.java", "for (int i = 0; i < 10; i++) {}");
    CompilerInput input = new CompilerInput(sourceFile);

    NodeTraversal traversal = new NodeTraversal(compiler, input, options);
    checkSideEffectsPass.process(input, input.getRoot());

    Node result = input.getRoot();
    assertTrue("No warnings should have been produced", result.getChildren().isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsWithCallStatement() {
    SourceFile sourceFile = createSourceFile("Test.java", "foo();");
    CompilerInput input = new CompilerInput(sourceFile);

    NodeTraversal traversal = new NodeTraversal(compiler, input, options);
    checkSideEffectsPass.process(input, input.getRoot());

    Node result = input.getRoot();
    assertFalse("A warning should have been produced", result.getChildren().isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsWithProtectedCode() {
    SourceFile sourceFile = createSourceFile("Test.java", "JSCOMPILER_PRESERVE JSCOMPILER_PRESERVE JSCOMPILER_PRESERVE JS.Compiler.processSourceFile(JS.Context.current(), this);");
    CompilerInput input = new CompilerInput(sourceFile);

    NodeTraversal traversal = new NodeTraversal(compiler, input, options);
    checkSideEffectsPass.process(input, input.getRoot());

    Node result = input.getRoot();
    assertTrue("No warnings should have been produced", result.getChildren().isEmpty());
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    SourceFile sourceFile = createSourceFile("Test.java", "void foo() {}");
    CompilerInput input = new CompilerInput(sourceFile);
    checkSideEffectsPass.process(input, input.getRoot());

    checkSideEffectsPass.protectSideEffects();

    AbstractNode root = input.getRoot();
    List<AbstractNode> children = Arrays.asList(root.getChildren());
    AbstractNode protectorFn = findNodeByName("JSCOMPILER_PRESERVE", children);
    CallExpr callExpr = (CallExpr) findNodeByType(Token.CALL, children);

    assertTrue("The protector function should exist", protectorFn != null);
    assertTrue("The call statement should be replaced by a call to the protector function", callExpr.getFirstChild() == protectorFn);
  }

  private AbstractNode findNodeByName(String name, List<AbstractNode> nodes) {
    for (AbstractNode node : nodes) {
      if (node.isName() && node.getString().equals(name)) {
        return node;
      }
    }
    return null;
  }

  private AbstractNode findNodeByType(int type, List<AbstractNode> nodes) {
    for (AbstractNode node : nodes) {
      if (node.getType() == type) {
        return node;
      }
    }
    return null;
  }

  private SourceFile createSourceFile(String name, String code) {
    NodeFactory factory = new NodeFactory();
    Node root = factory.parse(code);
    return new SourceFile(name, code, null, root);
  }
}
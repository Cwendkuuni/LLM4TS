import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.BREAK;
import static com.google.javascript.rhino.NodeType.CONTINUE;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.STRING_LITERAL;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {
  private static Node createLabelNode(String labelName) {
    Node label = NodeFactory.createLabel(labelName);
    Node block = NodeFactory.createBlock();
    label.setParent(block);
    block.addChild(label);
    return block;
  }

  private static Node createBreakOrContinueNode(String labelName, Node parent) {
    Node label = NodeFactory.createName(labelName);
    label.setType(labelName.equals("continue") ? CONTINUE : BREAK);
    label.setParent(parent);
    return label;
  }

  @Test
  public void testRenameLabels() {
    String sourceCode = "function foo() {"
        + "a: {"
        + "   b: break a;"
        + "}"
        + "a: {"
        + "   b: continue a;"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    // Create an empty abstract compiler.
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Compile the source code.
    Node externs = NodeFactory.createScript(new String[] { ""}, new String[] {}, null, null);
    Node root = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);
    renameLabels.process(externs, root);
    Node result = compiler.process(externs, root, null);

    // Traverse the Node tree and check the labels have been renamed.
    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(result);
    traverse(renameLabels, nodeTraversal, result, null);

    Node expectedResult = NodeFactory.createScript(new String[] {
        "function foo() {"
            + "c: {"
            + "   d: break c;"
            + "}"
            + "c: {"
            + "   d: continue c;"
            + "}"
            + "}"
    }, new String[] {}, null, null);

    NodeUtil.assertNodeEquals(expectedResult, result);
  }

  @Test
  public void testRenameLabelsWithDuplicateLabels() {
    String sourceCode = "function foo() {"
        + "a: {"
        + "   break a;"
        + "   a: {"
        + "       continue a;"
        + "   }"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    // Create an empty abstract compiler.
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Compile the source code.
    Node externs = NodeFactory.createScript(new String[] { ""}, new String[] {}, null, null);
    Node root = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);
    renameLabels.process(externs, root);
    Node result = compiler.process(externs, root, null);

    // Traverse the Node tree and check the labels have been renamed.
    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(result);
    traverse(renameLabels, nodeTraversal, result, null);

    Node expectedResult = NodeFactory.createScript(new String[] {
        "function foo() {"
            + "b: {"
            + "   break b;"
            + "   e: {"
            + "       continue e;"
            + "   }"
            + "}"
            + "}"
    }, new String[] {}, null, null);

    NodeUtil.assertNodeEquals(expectedResult, result);
  }

  @Test
  public void testRenameLabelsWithDeepNestedLabels() {
    String sourceCode = "function foo() {"
        + "a: {"
        + "   b: {"
        + "       c: {"
        + "           d: break c;"
        + "       }"
        + "   }"
        + "   b: {"
        + "       c: {"
        + "           d: continue c;"
        + "       }"
        + "   }"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    // Create an empty abstract compiler.
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Compile the source code.
    Node externs = NodeFactory.createScript(new String[] { ""}, new String[] {}, null, null);
    Node root = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);
    renameLabels.process(externs, root);
    Node result = compiler.process(externs, root, null);

    // Traverse the Node tree and check the labels have been renamed.
    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(result);
    traverse(renameLabels, nodeTraversal, result, null);

    Node expectedResult = NodeFactory.createScript(new String[] {
        "function foo() {"
            + "e: {"
            + "   f: {"
            + "       g: {"
            + "           h: break h;"
            + "       }"
            + "   }"
            + "   f: {"
            + "       g: {"
            + "           h: continue h;"
            + "       }"
            + "   }"
            + "}"
            + "}"
    }, new String[] {}, null, null);

    NodeUtil.assertNodeEquals(expectedResult, result);
  }

  @Test
  public void testRenameLabelsWithUnreferencedLabels() {
    String sourceCode = "function foo() {"
        + "a: {"
        + "   b: break a;"
        + "   c: {"
        + "       break a;"
        + "   }"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    // Create an empty abstract compiler.
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(abstractCompiler, new DontRenameUnusedLabelSupplier(), false);

    // Compile the source code.
    Node externs = NodeFactory.createScript(new String[] { ""}, new String[] {}, null, null);
    Node root = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);
    renameLabels.process(externs, root);
    Node result = compiler.process(externs, root, null);

    // Traverse the Node tree and check the labels have not been renamed.
    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(result);
    traverse(renameLabels, nodeTraversal, result, null);

    Node expectedResult = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);

    NodeUtil.assertNodeEquals(expectedResult, result);
  }

  @Test
  public void testRenameLabelsWithUnreachableLabels() {
    String sourceCode = "function foo() {"
        + "a: {"
        + "   b: break a;"
        + "}"
        + "c: {"
        + "   d: {"
        + "       break a;"
        + "   }"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    // Create an empty abstract compiler.
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(abstractCompiler);

    // Compile the source code.
    Node externs = NodeFactory.createScript(new String[] { ""}, new String[] {}, null, null);
    Node root = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);
    renameLabels.process(externs, root);
    Node result = compiler.process(externs, root, null);

    // Traverse the Node tree and check the labels have not been renamed.
    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(result);
    traverse(renameLabels, nodeTraversal, result, null);

    Node expectedResult = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);

    NodeUtil.assertNodeEquals(expectedResult, result);
  }

  @Test
  public void testRenameLabelsWithUnreachableLabelsAndUnreferencedLabels() {
    String sourceCode = "function foo() {"
        + "a: {"
        + "   b: break a;"
        + "   c: {"
        + "       break a;"
        + "   }"
        + "}"
        + "c: {"
        + "   d: {}"
        + "}"
        + "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    // Create an empty abstract compiler.
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(abstractCompiler, new DontRenameUnusedLabelSupplier(), false);

    // Compile the source code.
    Node externs = NodeFactory.createScript(new String[] { ""}, new String[] {}, null, null);
    Node root = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);
    renameLabels.process(externs, root);
    Node result = compiler.process(externs, root, null);

    // Traverse the Node tree and check the labels have not been renamed.
    NodeTraversal nodeTraversal = NodeTraversal.createTraverser(result);
    traverse(renameLabels, nodeTraversal, result, null);

    Node expectedResult = NodeFactory.createScript(new String[] { sourceCode }, new String[] {}, null, null);

    NodeUtil.assertNodeEquals(expectedResult, result);
  }

  private static class DontRenameUnusedLabelSupplier implements Supplier<String> {
    @Override
    public String get() {
      return "";
    }
  }
}
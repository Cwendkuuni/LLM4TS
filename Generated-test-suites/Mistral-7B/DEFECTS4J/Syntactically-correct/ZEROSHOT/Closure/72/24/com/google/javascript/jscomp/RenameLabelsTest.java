import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeFactory.createLabel;
import static com.google.javascript.jscomp.NodeFactory.createNode;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.BREAK;
import static com.google.javascript.rhino.NodeType.CONTINUE;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.TOKEN;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private static <T> Map<String, T> createMap() {
    return (Map<String, T>) Maps.newHashMap();
  }

  @Test
  public void testRenameLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = createNode(Token.START_FILE, "test.js");
    Node label = createLabel("a");
    Node body = createNode(TOKEN.BLOCK, null);
    label.setParent(body);
    body.addChild(label);
    root.addChild(body);

    renameLabels.process(null, root);
    assertEquals("a", label.getString());

    label.setParent(null);
    label.setString("newLabelName");

    Node newLabel = createLabel("a");
    body.addChild(newLabel);
    assertEquals("newLabelName", newLabel.getString());

    traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == LABEL && node.getString().equals("a")) {
          fail("Label 'a' should have been renamed.");
        }
      }
    });
  }

  @Test
  public void testRenameLabelWithChild() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = createNode(Token.START_FILE, "test.js");
    Node aLabel = createLabel("a");
    Node body = createNode(TOKEN.BLOCK, null);
    aLabel.setParent(body);
    body.addChild(aLabel);
    Node statement = createNode(Token.ASSIGN, null);
    statement.setParent(body);
    statement.setFirstChild(aLabel);
    statement.setLastChild(createNode(TOKEN.NUMBER, 42));
    root.addChild(body);

    renameLabels.process(null, root);
    assertEquals("a", aLabel.getString());

    traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == LABEL && node.getString().equals("a")) {
          fail("Label 'a' should have been renamed.");
        }
      }
    });

    Node renamedLabel = body.getFirstChild();
    assertEquals("newLabelName", renamedLabel.getString());
  }

  @Test
  public void testRenameLabelWithBreakOrContinue() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = createNode(Token.START_FILE, "test.js");
    Node aLabel = createLabel("a");
    Node body = createNode(TOKEN.BLOCK, null);
    aLabel.setParent(body);
    body.addChild(aLabel);
    Node statement1 = createNode(TOKEN.ASSIGN, null);
    statement1.setParent(body);
    statement1.setFirstChild(aLabel);
    statement1.setLastChild(createNode(Token.NUMBER, 42));
    Node statement2 = createNode(LABEL, null);
    statement2.setParent(body);
    statement2.setFirstChild(createLabel("a"));
    statement2.setLastChild(createNode(BREAK, null));
    Node statement3 = createNode(TOKEN.ASSIGN, null);
    statement3.setParent(body);
    statement3.setFirstChild(createLabel("a"));
    statement3.setLastChild(createNode(CONTINUE, null));
    root.addChild(body);

    renameLabels.process(null, root);
    assertEquals("a", aLabel.getString());

    traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == LABEL && node.getString().equals("a")) {
          fail("Label 'a' should have been renamed.");
        }
      }
    });

    Node renamedLabel1 = body.getFirstChild();
    assertEquals("newLabelName", renamedLabel1.getString());

    Node renamedBreak = body.getChildren().get(1);
    assertEquals("newLabelName", renamedBreak.getFirstChild().getString());

    Node renamedContinue = body.getChildren().get(2);
    assertEquals("newLabelName", renamedContinue.getFirstChild().getString());
  }

  @Test
  public void testRemoveUnusedLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renameLabels = new RenameLabels(compiler, new UnusedNameSupplier(), false);

    Node root = createNode(Token.START_FILE, "test.js");
    Node aLabel = createLabel("a");
    Node body = createNode(TOKEN.BLOCK, null);
    aLabel.setParent(body);
    body.addChild(aLabel);
    Node statement = createNode(Token.ASSIGN, null);
    statement.setParent(body);
    statement.setFirstChild(createLabel("b"));
    statement.setLastChild(createNode(Token.NUMBER, 42));
    root.addChild(body);

    renameLabels.process(null, root);
    assertEquals("b", aLabel.getString());

    traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == LABEL && node.getString().equals("a")) {
          fail("Label 'a' should have been removed.");
        }
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void testRenameLabelWithConflictingKeyword() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = createNode(Token.START_FILE, "test.js");
    Node label = createLabel("if");
    root.addChild(label);

    renameLabels.process(null, root);
    fail("Should throw an exception.");
  }

  private static class UnusedNameSupplier implements Supplier<String> {
    @Override
    public String get() {
      throw new UnsupportedOperationException();
    }
  }
}
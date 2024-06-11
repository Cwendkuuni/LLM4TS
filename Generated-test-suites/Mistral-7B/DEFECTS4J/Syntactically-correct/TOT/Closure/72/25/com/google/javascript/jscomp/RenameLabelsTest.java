import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RenameLabelsTest {

 private Compiler compiler;
 private CompilerPass renameLabels;

 @Before
 public void setUp() {
 compiler = new CompilerWithEnvironment();
 renameLabels = new RenameLabels(compiler);
 }

 @Test
 public void testSimpleLabelRenaming() {
 String jsCode = "function foo() {\n" +
 "  a: {\n" +
 "    b: break a;\n" +
 "  }\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 NodeTraversal traversal = new NodeTraversal(root);
 Node labelNode = traversal.findFirstDescendantOfType(Node.LABEL);
 assertThat(labelNode.getFirstChild().getString()).isEqualTo("a");
 }

 @Test
 public void testLabelRenamingWithMultipleNestingLevels() {
 String jsCode = "function foo() {\n" +
 "  a: {\n" +
 "    b: {\n" +
 "      c: break a;\n" +
 "    }\n" +
 "  }\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 NodeTraversal traversal = new NodeTraversal(root);
 Node labelNode = traversal.findFirstDescendantOfType(Node.LABEL);
 assertThat(labelNode.getFirstChild().getString()).isEqualTo("a");
 labelNode = traversal.findNextSiblingOfType(labelNode, Node.LABEL);
 assertThat(labelNode.getFirstChild().getString()).isEqualTo("b");
 }

 @Test
 public void testLabelRenamingWithKeywordName() {
 String jsCode = "function foo() {\n" +
 "  switch (1) {\n" +
 "    case 1:\n" +
 "      a: {\n" +
 "        continue a;\n" +
 "      }\n" +
 "    default:\n" +
 "  }\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 NodeTraversal traversal = new NodeTraversal(root);
 Node labelNode = traversal.findFirstDescendantOfType(Node.LABEL);
 assertThat(labelNode.getFirstChild().getString()).isEqualTo("a");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLabelRenamingWithEmptyLabelName() {
 String jsCode = "function foo() {\n" +
 "  a: {}\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLabelRenamingWithNoLabelNode() {
 String jsCode = "function foo() {}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 }

 @Test
 public void testLabelRemoval() {
 String jsCode = "function foo() {\n" +
 "  a: {\n" +
 "  }\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 NodeTraversal traversal = new NodeTraversal(root);
 Node labelNode = traversal.findFirstDescendantOfType(Node.LABEL);
 assertThat(labelNode).isNull();
 }

 @Test
 public void testLabelRemovalWithReferencedLabel() {
 String jsCode = "function foo() {\n" +
 "  a: {\n" +
 "    b: break a;\n" +
 "  }\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 NodeTraversal traversal = new NodeTraversal(root);
 Node labelNode = traversal.findFirstDescendantOfType(Node.LABEL);
 assertThat(labelNode.getFirstChild().getString()).isEqualTo("a");
 }

 @Test
 public void testLabelRemovalWithUnusedLabel() {
 String jsCode = "function foo() {\n" +
 "  a: {}\n" +
 "}";
 Node root = compiler.parseTree(jsCode);
 renameLabels.process(compiler.getExterns(), root);
 NodeTraversal traversal = new NodeTraversal(root);
 Node labelNode = traversal.findFirstDescendantOfType(Node.LABEL);
 assertThat(labelNode).isNull();
 }
}
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractTraversal.*;
import static com.google.javascript.jscomp.NodeTraversal.ScopedCallback.*;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Config;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String jsCode =
        "function bar() {"
            + "  a: {"
            + "    b: {"
            + "      foo();"
            + "    }"
            + "  }"
            + "  a: {"
            + "    b: break a;"
            + "  }"
            + "}";

    CompilerOptions options = Config.defaultOptions();
    options.setCompilerFlag("--print_stack_trace_on_error", "true");
    CheckerFactory checkerFactory = new CheckerFactory();
    Compiler compiler = new Compiler(jsCode, checkerFactory, options);

    AbstractTraversal callback = new NodeVisitor() {
      Map<String, LabelInfo> labelMap = Maps.newHashMap();

      @Override
      public void visitLabel(Node node, Node parent) {
        String labelName = node.getFirstChild().getString();
        LabelInfo labelInfo = new LabelInfo(labelMap.size());
        labelMap.put(labelName, labelInfo);
      }

      @Override
      public void visitBreakOrContinue(Node node) {
        Node nodeName = node.getFirstChild();
        if (nodeName != null) {
          String labelName = nodeName.getString();
          LabelInfo labelInfo = labelMap.get(labelName);
          assertEquals("Label should be referenced after visitBreakOrContinue", true, labelInfo.referenced);
        }
      }

      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == Token.LABEL) {
          String labelName = node.getFirstChild().getString();
          LabelInfo labelInfo = labelMap.get(labelName);
          if (labelInfo == null) {
            // Determine the new name for this label.
            int currentDepth = labelMap.size() + 1;
            String newName = labelInfo = new LabelInfo(currentDepth).name;
            labelMap.put(labelName, labelInfo);

            // Create a new name, if needed, for this depth.
            if (labelInfo.names.size() < currentDepth) {
              labelInfo.names.add(labelInfo.nameGenerator.generateNextName());
            }

            String newLabelName = labelInfo.names.get(currentDepth - 1);
            assertEquals("Label name should be updated after shouldTraverse", newLabelName, labelName);
            node.getFirstChild().setString(newLabelName);
          }
        }
        return true;
      }

      static class LabelInfo {
        int id;
        String name;
        Map<String, LabelInfo> labelMap;
        ArrayList<String> names = Lists.newArrayList();

        LabelInfo(int id) {
          this.id = id;
          this.name = "label" + id;
          this.labelMap = Maps.newHashMap();
        }
      }

      Supplier<String> nameSupplier = new Supplier<String>() {
        HashSet<String> usedNames = Sets.newHashSet();

        @Override
        public String get() {
          String name = "label" + usedNames.size();
          usedNames.add(name);
          return name;
        }
      }.asInstanceOf(Supplier.class);

      Set<String> usedNames = Sets.newHashSet();
      NameGenerator nameGenerator = new NameGenerator(usedNames, "", nameSupplier);

      @Override
      public void enterScope(NodeTraversal nodeTraversal) {
        LabelInfo labelInfo = new LabelInfo(labelMap.size());
        labelInfo.labelMap = Maps.newHashMap();
        labelMap.put(labelName, labelInfo);
      }

      @Override
      public void exitScope(NodeTraversal nodeTraversal) {
        labelMap.pop();
      }
    };

    Node root = Parser.parse(jsCode, null, NodeFactory.getRootNode());
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse(callback);
    compiler.run();
    assertEquals("Expected 0 errors.", 0, compiler.getNumErrors());
  }

  static class LabelInfo {
    int id;
    String name;
    Map<String, LabelInfo> labelMap;
    ArrayList<String> names = Lists.newArrayList();

    LabelInfo(int id) {
      this.id = id;
      this.name = "label" + id;
      this.labelMap = Maps.newHashMap();
    }
  }

  static class NameGenerator {
    Set<String> usedNames;
    String prefix;
    Supplier<String> nameGenerator;

    NameGenerator(Set<String> usedNames, String prefix, Supplier<String> nameGenerator) {
      this.usedNames = usedNames;
      this.prefix = prefix;
      this.nameGenerator = nameGenerator;
    }

    String generateNextName() {
      String name = nameGenerator.get();
      while (usedNames.contains(name)) {
        name = nameGenerator.get();
      }
      return name;
    }
  }
}
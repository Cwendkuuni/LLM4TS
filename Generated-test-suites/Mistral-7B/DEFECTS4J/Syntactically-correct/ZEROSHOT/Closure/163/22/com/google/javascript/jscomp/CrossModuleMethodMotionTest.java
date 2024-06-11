import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPass.*;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.rhino.NodeType.*;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Externs;
import com.google.javascript.jscomp.GraphVisitor;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.ModuleGraph;
import com.google.javascript.jscomp.Symbol;
import com.google.javascript.jscomp.SymbolTable;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeType;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    CompilerOptions options = new CompilerOptions(new Externs());
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);

    String source = "var a = {}; a.m = function() {}; "
        + "var b = {}; b.prototype.n = function() {}; ";

    Node externs = createExternsFromSource("", source);
    Node root = createRootFromSource("", source);

    pass.process(externs, root);
    assertEquals(0, compiler.getDiagnostics().size());

    Node aPrototype = getPrototype(root, "a");
    Node bPrototype = getPrototype(root, "b");

    Symbol aM = getSymbol(aPrototype, "m");
    Symbol bN = getSymbol(bPrototype, "n");

    assertEquals("a.m", aM.getName());
    assertEquals("b.n", bN.getName());

    Node aMNode = aM.getValue();
    Node bNNode = bN.getValue();

    assertEquals(FUNCTION, aMNode.getType());
    assertEquals(FUNCTION, bNNode.getType());

    Node aMCall = IR.call(aMNode, null, null);
    Node bNCall = IR.call(bNNode, null, null);

    assertEquals(aMNode, aMCall.getChild(0));
    assertEquals(bNNode, bNCall.getChild(0));

    Node result = getPrototype(root, "a").getChild(0);
    assertEquals(STUB_METHOD_NAME, result.getChild(0).toString());
    assertEquals(UNSTUB_METHOD_NAME, bNNode.getParent().toString());
  }

  private Node createRootFromSource(String name, String source) {
    Compiler compiler = new Compiler(new CompilerOptions(new Externs()));
    Node root = compiler.parse(name, source, null);
    return root;
  }

  private Node createExternsFromSource(String name, String source) {
    Compiler compiler = new Compiler(new CompilerOptions(new Externs()));
    Node externs = compiler.parseExterns(name, source);
    return externs;
  }

  private Node getPrototype(Node node, String name) {
    return findPrototype(node, name).getChild(0);
  }

  private Symbol getSymbol(Node node, String name) {
    SymbolTable symbolTable = node.getSymbolTable();
    return symbolTable.getSymbol(name);
  }

  private Node findPrototype(Node node, String name) {
    for (Node child : node.getChildren()) {
      if (child.getType() == OBJECT && name.equals(child.getString())) {
        return child;
      }
      Node foundPrototype = findPrototype(child, name);
      if (foundPrototype != null) {
        return foundPrototype;
      }
    }
    return null;
  }

  private static class IdGenerator implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentId = 0;

    int newId() {
      return currentId++;
    }
  }
}
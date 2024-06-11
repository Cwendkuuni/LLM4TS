import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  CheckGlobalThis checker;
  NodeTraversal traversal;

  @Before
  public void setUp() {
    this.checker = new CheckGlobalThis(null, CheckLevel.WARNING);
    this.traversal = new NodeTraversal(null);
  }

  @Test
  public void testCheckGlobalThis() {
    // Test method CheckGlobalThis(AbstractCompiler compiler, CheckLevel level)
  }

  @Test
  public void testShouldTraverse() {
    // Test method shouldTraverse(NodeTraversal t, Node n, Node parent)
  }

  @Test
  public void testVisit() {
    // Test method visit(NodeTraversal t, Node n, Node parent)
  }

  @Test
  public void testShouldReportThis() {
    // Test method shouldReportThis(Node n, Node parent)
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Test method getFunctionJsDocInfo(Node n)
  }
}
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;

public class PrepareAstTest {

  @Test
  public void testPrepareAst_NormalCase() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node rootNode = Node.objectLiteral();
    prepareAst.process(null, rootNode);
    assertNotNull(rootNode.getFirstChild());
  }

  @Test
  public void testPrepareAst_FreeCall() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node rootNode = Node.name("func").call(Node.name("func"));
    prepareAst.process(null, rootNode);
    assertNotNull(rootNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAst_DirectEval() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node rootNode = Node.name("eval").call(null);
    prepareAst.process(null, rootNode);
    assertNotNull(rootNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAst_CheckOnly_InvalidNodeType() {
    PrepareAst prepareAst = new PrepareAst(null, true);
    Node rootNode = Node.number(1);
    prepareAst.process(null, rootNode);
  }
}
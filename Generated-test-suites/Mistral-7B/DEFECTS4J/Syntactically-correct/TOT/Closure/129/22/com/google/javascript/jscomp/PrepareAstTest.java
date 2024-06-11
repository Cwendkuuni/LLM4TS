import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Test with null externs and root
    prepareAst.process(null, null);

    // Test with empty externs and root
    Node externs = IR.objectLit().srcref("externs");
    Node root = IR.nullKeyword();
    prepareAst.process(externs, root);

    // Test with simple node
    Node simpleNode = IR.numberLiteral(10);
    prepareAst.process(externs, simpleNode);

    // Test with control structure node
    Node ifNode = IR.ifExpression(IR.booleanLiteral(true), IR.nullKeyword(), IR.nullKeyword());
    prepareAst.process(externs, ifNode);

    // Test with object literal node
    Node objectNode = IR.objectLit();
    objectNode.addProperty(IR.name("a"), IR.stringLiteral("test"));
    prepareAst.process(externs, objectNode);

    // Test with function node
    Node functionNode = IR.function(IR.name("test"), null, IR.block());
    prepareAst.process(externs, functionNode);

    // Test with call node
    Node callNode = IR.callExpression(IR.name("test"), null);
    prepareAst.process(externs, callNode);

    // Test with direct eval node
    Node evalNode = IR.callExpression(IR.name("eval"), null, IR.stringLiteral("1 + 1"));
    prepareAst.process(externs, evalNode);

    // Test with free call node
    Node freeCallNode = IR.callExpression(IR.name("test"), null, IR.nullKeyword());
    prepareAst.process(externs, freeCallNode);

    // Test with object literal with JSDoc
    Node jsDocObjectNode = IR.objectLit();
    jsDocObjectNode.addProperty(IR.name("a"), IR.stringLiteral("test"));
    jsDocObjectNode.setJSDocInfo(new JsDocInfo("Test Class", "Description"));
    prepareAst.process(externs, jsDocObjectNode);

    // Test with function literal with JSDoc
    Node jsDocFunctionNode = IR.function(
        IR.name("test"), null, IR.block(), new JsDocInfo("Test Function", "Description"));
    prepareAst.process(externs, jsDocFunctionNode);

    // Test with call to function literal with JSDoc
    Node callJsDocFunctionNode = IR.callExpression(
        jsDocFunctionNode, null, IR.nullKeyword());
    prepareAst.process(externs, callJsDocFunctionNode);
  }
}

class JsDocInfo {
  String clazz;
  String description;

  JsDocInfo(String clazz, String description) {
    this.clazz = clazz;
    this.description = description;
  }
}
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.jscomp.NodeUtil.isGet;
import static com.google.javascript.jscomp.NodeUtil.isObjectLit;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversalCallback;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  @SuppressWarnings("unchecked")
  private static <T> T newNode(int type, int start, int end) {
    return (T) new Node(type, start, end, null, null, null);
  }

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setup() {
    this.compiler = new Compiler();
  }

  @org.junit.Test
  public void testProcess() {
    Node root = newNode(Token.BLOCK, 0, 0);
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);
    verifyProcessing(root);
  }

  @org.junit.Test
  public void testCheckOnly() {
    Node root = newNode(Token.BLOCK, 0, 0);
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);
    verifyProcessing(root, true);
  }

  private void verifyProcessing(Node root, boolean checkOnly) {
    Node expectedRoot = createExpectedRoot();
    Node expectedRootWithAnnotations = createExpectedRootWithAnnotations();

    if (checkOnly) {
      assertEquals(expectedRoot, root);
      return;
    }

    traverse(
        compiler, root, new Callback() {
          @Override
          public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
            if (n.isObjectLit()) {
              verifyObjectLiteralProcessing(n);
            }
            return true;
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            switch (n.getType()) {
              case Token.BLOCK:
                verifyBlockProcessing(n);
                break;
              case Token.CALL:
                verifyCallProcessing(n);
                break;
              case Token.FUNCTION:
                verifyFunctionProcessing(n, parent);
                break;
              case Token.OBJECT_LITERAL:
                verifyObjectLiteralProcessing(n);
                break;
            }
          }
        },
        null);

    assertEquals(expectedRootWithAnnotations, root);
  }

  private Node createExpectedRoot() {
    Node root = new Node(Token.BLOCK, 0, 0);
    Node block1 = newNode(Token.BLOCK, 0, 0);
    Node block2 = newNode(Token.BLOCK, 0, 0);
    Node ifStatement = newNode(Token.IF, 0, 0);
    Node ifBlock = newNode(Token.BLOCK, 0, 0);
    Node assign = newNode(Token.ASSIGN, 0, 0);
    Node name1 = newNode(Token.NAME, 0, 1);
    Name name2 = new Node(Token.NAME, 2, 3);
    Node name3 = new Node(Token.NAME, 4, 5);
    Node name4 = new Node(Token.NAME, 6, 7);

    // root
    //   |
    //   |-- block1
    //   |      |
    //   |      |-- ifStatement
    //   |      |      |
    //   |      |      |-- ifBlock
    //   |      |      |      |
    //   |      |      |      |-- assign
    //   |      |      |      |      |
    //   |      |      |      |      |-- name1
    //   |      |      |      |      |
    //   |      |      |      |-- block2
    //   |      |      |-- name2
    //   |-- name3
    //   |
    //   |-- name4

    root.addChildrenToFront(block1, block2, name3, name4);
    block1.addChildrenToFront(ifStatement);
    ifStatement.addChildrenToFront(ifBlock);
    ifBlock.addChildrenToFront(assign, name1);
    block2.addChildrenToFront(name2);

    return root;
  }

  private Node createExpectedRootWithAnnotations() {
    Node root = createExpectedRoot();

    addAnnotationsToFunction(root, 0, "rootFunction");
    addAnnotationsToFunction(root.getFirstChild(), 0, "block1Function");
    addAnnotationsToFunction(root.getFirstChild().getLastChild(), 0,
        "ifStatementFunction");
    addAnnotationsToFunction(root.getFirstChild().getLastChild().getLastChild(), 0,
        "assignFunction");
    addAnnotationsToCall(root.getFirstChild().getLastChild().getLastChild().getFirstChild(),
        0, "getNameFunction");

    return root;
  }

  private void addAnnotationsToFunction(Node function, int depth, String name) {
    Node jsDocInfo = new Node(Token.JSDOCINFO, 0, 0);
    jsDocInfo.setJSDocInfo(new FunctionJsDocInfo(name, depth));
    function.setFirstChild(jsDocInfo);
  }

  private void addAnnotationsToCall(Node call, int depth, String name) {
    Node jsDocInfo = new Node(Token.JSDOCINFO, 0, 0);
    jsDocInfo.setJSDocInfo(new CallJsDocInfo(name, depth));
    call.setFirstChild(jsDocInfo);
  }

  private void verifyObjectLiteralProcessing(Node objlit) {
    Preconditions.checkState(objlit.isObjectLit());
    for (Node key = objlit.getFirstChild(); key != null; key = key.getNext()) {
      Node value = key.getFirstChild();
      Preconditions.checkState(value != null);
      Preconditions.checkState(value.getParent() == objlit);
      verifyObjectLiteralKeyAnnotations(objlit, key, value);
    }
  }

  private void verifyObjectLiteralKeyAnnotations(Node objlit, Node key, Node value) {
    if (key.getJSDocInfo() != null && value.isFunction()) {
      Preconditions.checkState(value.getJSDocInfo() == null);
      value.setJSDocInfo(key.getJSDocInfo());
    }
  }

  private void verifyCallProcessing(Node call) {
    Preconditions.checkState(call.isCall());

    Node first = call.getFirstChild();

    if (!NodeUtil.isGet(first)) {
      Preconditions.checkState(call.getBooleanProp(Node.FREE_CALL));
    }

    if (first.isName() && "eval".equals(first.getString())) {
      Preconditions.checkState(call.getBooleanProp(Node.DIRECT_EVAL));
    }
  }

  private void verifyFunctionProcessing(Node function, Node parent) {
    Preconditions.checkState(function.isFunction());

    if (parent != null && parent.getJSDocInfo() != null
        && parent.getJSDocInfo().isJavaDispatch()) {
      Preconditions.checkState(function.getBooleanProp(Node.IS_DISPATCHER));
    }
  }

  private void verifyBlockProcessing(Node block) {
    Preconditions.checkState(!isControlStructure(block)
        || isControlStructureCodeBlock(block, block.getFirstChild()));

    for (Node child = block.getFirstChild(); child != null; child = child.getNext()) {
      Preconditions.checkState(child.getParent() == block);
      verifyBlockProcessing(child);
    }
  }
}

class FunctionJsDocInfo implements JsDocInfo {
  private final String name;
  private final int depth;

  public FunctionJsDocInfo(String name, int depth) {
    this.name = name;
    this.depth = depth;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getDepth() {
    return depth;
  }
}

class CallJsDocInfo implements JsDocInfo {
  private final String name;
  private final int depth;

  public CallJsDocInfo(String name, int depth) {
    this.name = name;
    this.depth = depth;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getDepth() {
    return depth;
  }
}

class MyJsDocInfo implements JsDocInfo {
  @Override
  public String getName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getDepth() {
    throw new UnsupportedOperationException();
  }
}

interface JsDocInfo {
  String getName();
  int getDepth();
}
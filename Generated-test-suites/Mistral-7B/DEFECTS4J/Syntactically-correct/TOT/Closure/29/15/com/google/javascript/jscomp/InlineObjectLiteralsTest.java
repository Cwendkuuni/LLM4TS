import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Setup mocks
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    CompilerPassManager manager = new CompilerPassManager(compiler);
    manager.addPass(new InlineObjectLiterals(compiler, safeNameIdSupplier));

    // Create test case
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() {}," +
        "    c: []," +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b();" +
        "  var z = obj.c[0];" +
        "  obj = null;" +
        "}" +
        "foo();";

    Node root = NodeUtil.parseJsonToRootNode(sourceCode);
    Scope globals = NodeUtil.getTopLevelScope(root);

    // Execute pass
    manager.process(null, root);

    // Verify that the object was inlined
    Node fooFunction = NodeUtil.findFunctionByName("foo", globals);
    Preconditions.checkState(fooFunction != null);
    Node body = fooFunction.getFirstChild();
    Node statements = body.getFirstChild();

    // Check that the object has been inlined
    Node inlinedObject = null;
    for (Node child = statements.getFirstChild(); child != null;
         child = child.getNextSibling()) {
      if (child.isObjectLit()) {
        inlinedObject = child;
        break;
      }
    }

    Preconditions.checkState(inlinedObject != null);

    // Check that the properties have been inlined
    Map<String, Node> properties = Maps.newLinkedHashMap();
    for (Node property = inlinedObject.getFirstChild(); property != null;
         property = property.getNext()) {
      String name = property.getString();
      Node value = property.removeFirstChild();
      properties.put(name, value);
    }

    // Check that the variables have been assigned to the inlined properties
    for (Node statement = statements.getNext(); statement != null;
         statement = statement.getNextSibling()) {
      if (statement.isAssign()) {
        Node leftHandSide = statement.getFirstChild();
        Node rightHandSide = statement.getLastChild();
        if (leftHandSide.isName()) {
          String varName = leftHandSide.getString();
          Node property = properties.get(varName);
          if (property != null) {
            Preconditions.checkState(NodeUtil.areStatementsIdentical(
                property, rightHandSide));
          }
        }
      }
    }

    // Verify that the function call to obj.b() has been replaced with a call
    // to the inlined function
    for (Node statement = statements.getNext(); statement != null;
         statement = statement.getNextSibling()) {
      if (statement.isCall()) {
        Node callee = statement.getFirstChild().getFirstChild();
        if (callee.isGetProp() && callee.getLastChild().getString().equals("b")) {
          Node replacement = NodeUtil.newCall(
              NodeUtil.newName("JSCompiler_object_inline_b"), Lists.newArrayList());
          replacement.copyInformationFrom(callee);
          statement.replaceChild(callee, replacement);
        }
      }
    }

    // Check that the array has been inlined
    Node inlinedArray = null;
    for (Node child = inlinedObject.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isObjectLit() && child.getFirstChild().isArrayLit()) {
        inlinedArray = child;
        break;
      }
    }

    Preconditions.checkState(inlinedArray != null);

    // Check that the array elements have been inlined
    Map<String, Node> arrayElements = Maps.newLinkedHashMap();
    for (Node property = inlinedArray.getFirstChild(); property != null;
         property = property.getNext()) {
      String name = property.getString();
      Node value = property.removeFirstChild();
      arrayElements.put(name, value);
    }

    // Check that the variable z has been assigned to the inlined array element
    for (Node statement = statements.getNext(); statement != null;
         statement = statement.getNextSibling()) {
      if (statement.isAssign()) {
        Node leftHandSide = statement.getFirstChild();
        Node rightHandSide = statement.getLastChild();
        if (leftHandSide.isName() && leftHandSide.getString().equals("z")) {
          Node arrayElement = arrayElements.get("0");
          Preconditions.checkState(NodeUtil.areStatementsIdentical(
              arrayElement, rightHandSide));
        }
      }
    }
  }
}
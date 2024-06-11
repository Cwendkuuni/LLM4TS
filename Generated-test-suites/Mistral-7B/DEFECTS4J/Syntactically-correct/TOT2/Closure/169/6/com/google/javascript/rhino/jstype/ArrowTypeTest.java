import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = Node.newList();
    Node param1Node = Node.newObject(Node.newProperty("name", "p1"));
    parametersNode.addChild(param1Node);
    Node param2Node = Node.newObject(Node.newProperty("name", "p2"));
    parametersNode.addChild(param2Node);
    JSType returnType = registry.createUnknownType();

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);

    JSType expected = registry.createFunctionType(
        registry.createParametersWithVarArgs(JSTypeNative.ANY),
        returnType);

    assertTrue(arrowType.isSubtype(expected));
  }

  @Test
  public void testArrowTypeWithNullParametersAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);

    JSType expected = registry.createUnknownType();

    assertTrue(arrowType.isSubtype(expected));
  }

  @Test(expected = AssertionError.class)
  public void testArrowTypeWithInvalidParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = Node.newList();
    Node paramNode = Node.newObject(Node.newProperty("name", "p1"));
    parametersNode.addChild(paramNode);
    paramNode.setJSType(registry.createUnknownType());
    ArrowType arrowType = new ArrowType(registry, parametersNode, null);

    fail("Should throw an AssertionError.");
  }

  @Test(expected = AssertionError.class)
  public void testArrowTypeWithInvalidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parametersNode, null);
    arrowType.returnType = registry.createFunctionType(
        registry.createParametersWithVarArgs(JSTypeNative.ANY),
        null);

    fail("Should throw an AssertionError.");
  }
}
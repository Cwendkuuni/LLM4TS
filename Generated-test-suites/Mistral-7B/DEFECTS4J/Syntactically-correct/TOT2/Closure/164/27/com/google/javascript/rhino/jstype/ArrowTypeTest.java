import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertEquals("Expected ArrowType instance", ArrowType.class, arrowType.getClass());
    assertEquals("Parameters should be null by default", null, arrowType.parameters);
    assertEquals("Return type should be INT", INT, arrowType.returnType);
    assertEquals("Return type should not be inferred by default", false, arrowType.returnTypeInferred);

    Node paramsWithVarArgs = Node.newList(new Node("a"), Node.newVarargs());
    JSType arrowTypeWithVarArgs = new ArrowType(registry, paramsWithVarArgs, null, false);
    assertEquals("Parameters should have one required argument and a varargs list", 1, arrowTypeWithVarArgs.parameters.getChildCount());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), INT, false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), INT, false);
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(), STRING, false);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(), null, false);
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(INT.toNode()), INT, false);

    // ArrowType with the same parameters and return type should be a subtype of itself
    assertTrue("ArrowType should be a subtype of itself", arrowType1.isSubtype(arrowType1));

    // Two ArrowType instances with the same parameters and return type should be equal and subtypes of each other
    assertEquals("ArrowTypes with the same parameters and return type should be equal", arrowType1, arrowType2);
    assertTrue("ArrowType1 should be a subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
    assertTrue("ArrowType2 should be a subtype of ArrowType1", arrowType2.isSubtype(arrowType1));

    // ArrowType with a narrower return type should be a subtype of ArrowType with a broader return type
    assertTrue("ArrowType with a narrower return type should be a subtype of ArrowType with a broader return type", arrowType1.isSubtype(arrowType3));

    // ArrowType with a null return type should be a subtype of ArrowType with any return type, including a null one
    assertTrue("ArrowType with a null return type should be a subtype of ArrowType with any return type", arrowType1.isSubtype(arrowType4));
    assertTrue("ArrowType with any return type should be a subtype of ArrowType with a null return type", arrowType4.isSubtype(arrowType1));

    // ArrowType with identical parameters but different return types should not be a subtype of each other
    assertFalse("ArrowType with identical parameters but different return types should not be a subtype of each other", arrowType1.isSubtype(arrowType5));
    assertFalse("ArrowType with identical parameters but different return types should not be a subtype of each other", arrowType5.isSubtype(arrowType1));

    // ArrowType with more parameters should not be a subtype of ArrowType with fewer parameters
    assertFalse("ArrowType with more parameters should not be a subtype of ArrowType with fewer parameters", arrowType1.isSubtype(new ArrowType(registry, Node.newList(INT.toNode(), INT.toNode()), INT, false)));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), INT, false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), INT, false);
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(), STRING, false);

    // Hashcodes of ArrowTypes with the same parameters and return type should be equal
    int expectedHashCode = arrowType1.hashCode();
    int actualHashCode = arrowType2.hashCode();
    assertEquals("Hashcodes of ArrowTypes with the same parameters and return type should be equal", expectedHashCode, actualHashCode);

    // Hashcodes of ArrowTypes with different parameters or return type should be different
    int arrowType1HashCode = arrowType1.hashCode();
    int arrowType3HashCode = arrowType3.hashCode();
    assertNotEquals("Hashcodes of ArrowTypes with different parameters or return type should be different", arrowType1HashCode, arrowType3HashCode);
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(new Node("a"), new Node("b")), INT, false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(new Node("a"), new Node("b")), INT, false);

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(new Node("a"), new Node("b")), INT, false);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(new Node("a"), new Node("c")), INT, false);

    ArrowType arrowType5 = new ArrowType(registry, Node.newList(new Node("a"), new Node("b"), new Node("c")), INT, false);

    // ArrowTypes with equal parameters should have equal hashcodes
    assertTrue("ArrowTypes with equal parameters should have equal hashcodes", arrowType1.hasEqualParameters(arrowType2));
    assertTrue("ArrowTypes with equal parameters should have equal hashcodes", arrowType1.hasEqualParameters(arrowType3));

    // ArrowTypes with different parameters should not have equal hashcodes
    assertFalse("ArrowTypes with different parameters should not have equal hashcodes", arrowType1.hasEqualParameters(arrowType4));
    assertFalse("ArrowTypes with different parameters should not have equal hashcodes", arrowType1.hasEqualParameters(arrowType5));
  }

  @Test
  public void testArrowTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), INT, false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), INT, false);
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(), STRING, false);

    // ArrowTypes with the same parameters and return type should be equal
    assertEquals("ArrowTypes with the same parameters and return type should be equal", arrowType1, arrowType2);

    // ArrowTypes with the same parameters and different return types should not be equal
    assertNotEquals("ArrowTypes with the same parameters and different return types should not be equal", arrowType1, arrowType3);

    // ArrowTypes with different parameters should not be equal
    assertNotEquals("ArrowTypes with different parameters should not be equal", arrowType1, new ArrowType(registry, Node.newList(new Node("a"), new Node("b")), INT, false));
  }

  @Test
  public void testArrowTypeNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.emptyList(), INT, false);

    // ArrowType with empty parameters should be equal to itself
    assertEquals("ArrowType with empty parameters should be equal to itself", arrowType, arrowType);

    // ArrowType with empty parameters should be a subtype of ArrowType with any parameters
    ArrowType arrowTypeWithParams = new ArrowType(registry, Node.newList(new Node("a"), new Node("b")), INT, false);
    assertTrue("ArrowType with empty parameters should be a subtype of ArrowType with any parameters", arrowType.isSubtype(arrowTypeWithParams));
  }

  @Test
  public void testArrowTypeResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry, errorReporter);

    ArrowType arrowType = new ArrowType(registry, Node.newList(), INT, false);

    // Resolved ArrowType should have its return type and parameters resolved
    arrowType.resolveInternal(errorReporter, scope);

    JSType expectedReturnType = INT;
    JSType actualReturnType = arrowType.returnType;
    assertEquals("Expected return type to be INT", expectedReturnType, actualReturnType);

    Node expectedParameters = Node.newList(new Node("a"), new Node("b"));
    Node actualParameters = arrowType.parameters;
    assertEquals("Expected parameters to be Node.newList(new Node(\"a\"), new Node(\"b\"))", expectedParameters, actualParameters);
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, Node.newList(), null, false);

    assertTrue("ArrowType with unknown return type should have hasUnknownParamsOrReturn() equals true", arrowType.hasUnknownParamsOrReturn());
  }
}
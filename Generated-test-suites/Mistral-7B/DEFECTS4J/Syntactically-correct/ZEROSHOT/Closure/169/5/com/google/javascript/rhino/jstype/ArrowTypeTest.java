import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeWithNullParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals(UNKNOWN_TYPE, arrowType.returnType.getTypeName());
    assertNull(arrowType.parameters);
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    Node parametersNode = nodeFactory.createList();
    Node firstParameterNode = nodeFactory.createCall("Number");
    parametersNode.appendChild(firstParameterNode);
    Node secondParameterNode = nodeFactory.createCall("String");
    parametersNode.appendChild(secondParameterNode);

    JSType firstParameterType = registry.createType("Number");
    JSType secondParameterType = registry.createType("String");
    ArrowType arrowType = new ArrowType(registry, parametersNode, firstParameterType, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals("Number", arrowType.parameters.getFirstChild().getJSType().getTypeName());
    assertEquals("String", arrowType.parameters.getFirstChild(1).getJSType().getTypeName());
    assertEquals("Number", arrowType.returnType.getTypeName());
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithOptionalParameters() {
    Node parametersNode = nodeFactory.createList();
    Node firstParameterNode = nodeFactory.createCall("Number", nodeFactory.createList(nodeFactory.createList(nodeFactory.createString("optional"))));
    parametersNode.appendChild(firstParameterNode);
    Node secondParameterNode = nodeFactory.createCall("String");
    parametersNode.appendChild(secondParameterNode);

    JSType firstOptionalParameterType = registry.createOptional(registry.createType("Number"));
    JSType secondParameterType = registry.createType("String");
    ArrowType arrowType = new ArrowType(registry, parametersNode, secondParameterType, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals("Number|undefined", arrowType.parameters.getFirstChild().getJSType().getTypeName());
    assertEquals("String", arrowType.parameters.getFirstChild(1).getJSType().getTypeName());
    assertEquals("String", arrowType.returnType.getTypeName());
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node parametersNode = nodeFactory.createList();
    Node firstParameterNode = nodeFactory.createCall("Number");
    parametersNode.appendChild(firstParameterNode);
    Node varArgsNode = nodeFactory.createVarArgs("Number", "...");
    parametersNode.appendChild(varArgsNode);

    JSType firstParameterType = registry.createType("Number");
    ArrowType arrowType = new ArrowType(registry, parametersNode, firstParameterType, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals("Number", arrowType.parameters.getFirstChild().getJSType().getTypeName());
    assertEquals("[Number]", arrowType.parameters.getLastChild().getTypeName());
    assertEquals("Number", arrowType.returnType.getTypeName());
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node parametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));

    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertNull(arrowType.returnType);
    assertEquals("(Number, String)", arrowType.toString());
  }

  @Test
  public void testArrowTypeWithEmptyParameters() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    assertEquals("ArrowType", arrowType.getTypeName());
    assertNull(arrowType.parameters);
    assertEquals("", arrowType.toString());
  }

  @Test
  public void testArrowTypeWithParametersAndInferredReturnType() {
    Node parametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));

    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertNull(arrowType.returnType);
    assertEquals("(Number, String)", arrowType.toString());
  }

  @Test
  public void testArrowTypeWithNullReturnType() {
    Node parametersNode = nodeFactory.createList(nodeFactory.createCall("Number"));

    ArrowType arrowType = new ArrowType(registry, parametersNode, null, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals(UNKNOWN_TYPE, arrowType.returnType);
    assertEquals("(Number)", arrowType.toString());
  }

  @Test
  public void testArrowTypeWithReturnType() {
    Node parametersNode = nodeFactory.createList(nodeFactory.createCall("Number"));
    JSType returnType = registry.createType("String");

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals("String", arrowType.returnType.getTypeName());
    assertEquals("(Number) -> String", arrowType.toString());
  }

  @Test
  public void testArrowTypeEquivalence() {
    Node firstParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    Node secondParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    JSType firstReturnType = registry.createType("String");
    JSType secondReturnType = registry.createType("String");

    ArrowType arrowType1 = new ArrowType(registry, firstParametersNode, firstReturnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParametersNode, secondReturnType, false);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));

    // Test with unknown types
    ArrowType arrowType3 = new ArrowType(registry, firstParametersNode, null, false);
    ArrowType arrowType4 = new ArrowType(registry, secondParametersNode, null, false);

    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType4.isSubtype(arrowType3));
  }

  @Test
  public void testArrowTypeWithMismatchedParameters() {
    Node firstParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"));
    Node secondParametersNode = nodeFactory.createList(nodeFactory.createCall("String"));

    ArrowType arrowType1 = new ArrowType(registry, firstParametersNode, null, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParametersNode, null, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeWithOptionalAndNormalParameters() {
    Node firstParametersNode = nodeFactory.createList(
        nodeFactory.createCall("Number", nodeFactory.createList(nodeFactory.createList(nodeFactory.createString("optional")))),
        nodeFactory.createCall("String"));

    ArrowType arrowType = new ArrowType(registry, firstParametersNode, null, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals("Number|undefined", arrowType.parameters.getFirstChild().getJSType().getTypeName());
    assertEquals("String", arrowType.parameters.getFirstChild(1).getJSType().getTypeName());
  }

  @Test
  public void testArrowTypeWithVarArgsAndNormalParameters() {
    Node firstParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createList("..."));

    ArrowType arrowType = new ArrowType(registry, firstParametersNode, null, false);

    assertEquals("ArrowType", arrowType.getTypeName());
    assertEquals("Number", arrowType.parameters.getFirstChild().getJSType().getTypeName());
    assertEquals("[Number]", arrowType.parameters.getLastChild().getTypeName());
  }

  @Test
  public void testArrowTypeToString() {
    Node parametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    JSType returnType = registry.createType("String");

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType, false);

    assertEquals("(Number, String) -> String", arrowType.toString());
  }

  @Test
  public void testArrowTypeEquals() {
    Node firstParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    Node secondParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    JSType firstReturnType = registry.createType("String");
    JSType secondReturnType = registry.createType("String");

    ArrowType arrowType1 = new ArrowType(registry, firstParametersNode, firstReturnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParametersNode, secondReturnType, false);

    assertFalse(arrowType1.equals(arrowType2));
    assertFalse(arrowType2.equals(arrowType1));

    // Test with same types
    ArrowType arrowType3 = new ArrowType(registry, firstParametersNode, firstReturnType, false);
    assertTrue(arrowType1.equals(arrowType3));
    assertTrue(arrowType3.equals(arrowType1));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node firstParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    Node secondParametersNode = nodeFactory.createList(nodeFactory.createCall("Number"), nodeFactory.createCall("String"));
    JSType firstReturnType = registry.createType("String");
    JSType secondReturnType = registry.createType("String");

    ArrowType arrowType1 = new ArrowType(registry, firstParametersNode, firstReturnType, false);
    ArrowType arrowType2 = new ArrowType(registry, secondParametersNode, secondReturnType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);

    // Test with same types
    ArrowType arrowType3 = new ArrowType(registry, firstParametersNode, firstReturnType, false);
    int hashCode3 = arrowType3.hashCode();

    assertEquals(hashCode1, hashCode3);
  }
}
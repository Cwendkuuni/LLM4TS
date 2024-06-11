import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {
  @Test
  public void testArrowTypeWithTwoParametersAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        registry.createNamedType("Number"));
    ArrowType arrowType = new ArrowType(registry, parametersNode,
        registry.createNamedType("String"));

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        registry.createNamedType("String"));

    assertEquals("Arrow types should be equal", expectedArrowType, arrowType);
  }

  @Test
  public void testArrowTypeWithTwoParametersAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParametersWithVarArgs(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number")));
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals("Arrow types should be equal", expectedArrowType, arrowType);
  }

  @Test
  public void testArrowTypeWithOneParameterAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParameterNode("Number");
    ArrowType arrowType = new ArrowType(registry, parametersNode,
        registry.createNamedType("String"));

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        registry.createNamedType("String"));

    assertEquals("Arrow types should be equal", expectedArrowType, arrowType);
  }

  @Test
  public void testArrowTypeWithOneParameterAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = registry.createParameterNode("Number");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals("Arrow types should be equal", expectedArrowType, arrowType);
  }

  @Test
  public void testArrowTypeWithNoParametersAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null,
        registry.createNamedType("String"));

    ArrowType expectedArrowType = new ArrowType(registry, null,
        registry.createNamedType("String"));

    assertEquals("Arrow types should be equal", expectedArrowType, arrowType);
  }

  @Test
  public void testArrowTypeWithNoParametersAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null, true);

    ArrowType expectedArrowType = new ArrowType(registry, null, null, true);

    assertEquals("Arrow types should be equal", expectedArrowType, arrowType);
  }
}
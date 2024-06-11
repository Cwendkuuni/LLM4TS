import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parameters = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test valid input
    assertEquals(arrowType.getClass(), ArrowType.class);
    assertEquals(registry, arrowType.getTypeRegistry());
    assertEquals(parameters, arrowType.getParameters());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());

    // Test null input for returnType
    ArrowType arrowTypeNullReturnType = new ArrowType(registry, parameters, null);
    assertNull(arrowTypeNullReturnType.getReturnType());
    assertTrue(arrowTypeNullReturnType.isReturnTypeInferred());

    // Test null input for parameters
    ArrowType arrowTypeNullParameters = new ArrowType(registry, null, returnType);
    assertNull(arrowTypeNullParameters.getParameters());
  }
}
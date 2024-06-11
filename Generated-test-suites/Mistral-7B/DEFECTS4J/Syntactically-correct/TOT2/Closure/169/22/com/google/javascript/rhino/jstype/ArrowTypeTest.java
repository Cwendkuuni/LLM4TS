import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);

    ArrowType expected = new ArrowType(registry, null, getNativeType(Integer.TYPE), false);

    when(registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE)))
        .thenReturn(createParametersNode());
    when(registry.getNativeType(Integer.TYPE))
        .thenReturn(getNativeType(Integer.TYPE));

    Node parameters = createParametersNode();
    ArrowType arrowType = new ArrowType(registry, parameters, getNativeType(Integer.TYPE), false);

    assertEquals(expected.getClass(), arrowType.getClass());
    assertEquals(expected.parameters, arrowType.parameters);
    assertEquals(expected.returnType, arrowType.returnType);
    assertEquals(expected.returnTypeInferred, arrowType.returnTypeInferred);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructionWithNullRegistry() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = null;

    new ArrowType(registry, null, getNativeType(Integer.TYPE), false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructionWithNullParameters() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);

    new ArrowType(registry, null, getNativeType(Integer.TYPE), false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructionWithNullReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);

    new ArrowType(registry, createParametersNode(), null, false);
  }

  // ... Add more test cases here for other methods in the ArrowType class
}
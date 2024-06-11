import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistryTest;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistryTest().registry;
    Node parameters = registry.createParametersWithVarArgs(JSTypeNative.INT);
    JSType returnType = JSTypeNative.INT;

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
  }
}
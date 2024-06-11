import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.testing.Assertions;
import com.google.javascript.rhino.testing.Polyfill;
import com.google.javascript.rhino.testing.SetJSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private static final JSTypeRegistry registry;

  static {
    registry = new JSTypeRegistry() {
      @Override
      JSType getNativeType(String typeName) {
        return typeName.equals("void") ? JSTypeNative.VOID :
            JSTypeNative.get(typeName);
      }
    };
  }

  private ErrorReporter t;
  private ArrowType arrowType;

  @Before
  public void setUp() {
    t = new ErrorReporter();
  }

  @Test
  public void simpleArrowTypeTest() {
    Node functionNode = Polyfill.parseFunction("(x) => x * 2");
    arrowType = new ArrowType(registry, functionNode.getFirstChild(), INT, false);

    Assertions.assertEquals(INT, arrowType.returnType);
    Assertions.assertEquals(INT, arrowType.parameters.getFirstChild().getJSType());
  }

  @Test
  public void arrowTypeWithVarArgsTest() {
    Node functionNode = Polyfill.parseFunction("(x, ...y) => x + y[0]");
    arrowType = new ArrowType(registry, functionNode, INT, false);

    Assertions.assertEquals(INT, arrowType.returnType);
    Node x = arrowType.parameters.getFirstChild();
    Assertions.assertEquals(INT, x.getJSType());
    Node varArgs = x.getNext();
    Assertions.assertTrue(varArgs.isVarArgs());
  }

  @Test
  public void arrowTypeWithOptionalArgsTest() {
    Node functionNode = Polyfill.parseFunction("(x = 1, y) => x + y");
    arrowType = new ArrowType(registry, functionNode, INT, false);

    Assertions.assertEquals(INT, arrowType.returnType);
    Node x = arrowType.parameters.getFirstChild();
    Assertions.assertTrue(x.isOptionalArg());
    Assertions.assertEquals(INT, x.getJSType());
    Node y = x.getNext();
    Assertions.assertEquals(INT, y.getJSType());
  }

  @Test
  public void arrowTypeWithInferredReturnTypeTest() {
    Node functionNode = Polyfill.parseFunction("x => x * x");
    arrowType = new ArrowType(registry, functionNode, null, true);

    Assertions.assertNull(arrowType.returnType);
    Assertions.assertEquals(INT, arrowType.parameters.getFirstChild().getJSType());
  }

  @Test
  public void arrowTypeWithUnknownReturnTypeTest() {
    Node functionNode = Polyfill.parseFunction("(x) => undefined");
    arrowType = new ArrowType(registry, functionNode, null, false);

    Assertions.assertNull(arrowType.returnType);
  }

  @Test
  public void arrowTypeWithIncompatibleReturnTypesTest() {
    Node functionNode1 = Polyfill.parseFunction("(x) => x * 2");
    ArrowType arrowType1 = new ArrowType(registry, functionNode1, INT, false);

    Node functionNode2 = Polyfill.parseFunction("(x) => x + 2");
    ArrowType arrowType2 = new ArrowType(registry, functionNode2, INT, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void arrowTypeWithIncompatibleParameterTypesTest() {
    Node functionNode1 = Polyfill.parseFunction("(x) => x * 2");
    ArrowType arrowType1 = new ArrowType(registry, functionNode1, INT, false);

    Node functionNode2 = Polyfill.parseFunction("(y) => y + 2");
    ArrowType arrowType2 = new ArrowType(registry, functionNode2, STRING, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void arrowTypeWithMissingParameterTest() {
    Node functionNode = Polyfill.parseFunction("(x) => x * 2");
    ArrowType arrowType = new ArrowType(registry, functionNode, INT, false);

    Node functionNode2 = Polyfill.parseFunction("() => 2");
    ArrowType arrowType2 = new ArrowType(registry, functionNode2, INT, false);

    assertFalse(arrowType.isSubtype(arrowType2));
  }

  @Test
  public void arrowTypeWithMissingParameterAndInferredReturnTypeTest() {
    Node functionNode = Polyfill.parseFunction("() => 2");
    ArrowType arrowType = new ArrowType(registry, functionNode, null, true);

    Node functionNode2 = Polyfill.parseFunction("(x) => x * 2");
    ArrowType arrowType2 = new ArrowType(registry, functionNode2, INT, false);

    assertFalse(arrowType.isSubtype(arrowType2));
  }

  @Test
  public void arrowTypeWithMissingParameterAndVarArgsTest() {
    Node functionNode = Polyfill.parseFunction("(x, ...y) => x + y[0]");
    ArrowType arrowType = new ArrowType(registry, functionNode, INT, false);

    Node functionNode2 = Polyfill.parseFunction("() => 1");
    ArrowType arrowType2 = new ArrowType(registry, functionNode2, INT, false);

    assertFalse(arrowType.isSubtype(arrowType2));
  }

  @Test
  public void arrowTypeWithUnknownParameterTest() {
    Node functionNode = Polyfill.parseFunction("(x) => x * 2");
    ArrowType arrowType = new ArrowType(registry, functionNode, null, false);

    Node functionNode2 = Polyfill.parseFunction("(y) => y * 2");
    ArrowType arrowType2 = new ArrowType(registry, functionNode2, null, false);

    SetJSTypeRegistry registryWithUnknown = new SetJSTypeRegistry(registry);
    registryWithUnknown.put(UNKNOWN_TYPE, new JSTypeNative.UnknownType());

    // With unknown types, all arrow types are considered equal
    assertTrue(arrowType.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType));

    // But this does not mean that they are actually equal
    assertFalse(arrowType.equals(arrowType2));
  }
}
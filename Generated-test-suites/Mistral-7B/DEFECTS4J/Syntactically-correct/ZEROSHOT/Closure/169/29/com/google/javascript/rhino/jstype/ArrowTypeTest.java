import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.jstype.JSTypeNative.ANY;
import static com.google.javascript.rhino.jstype.JSTypeNative.BOOLEAN;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private static final ErrorReporter REPORTER = new ErrorReporter() {
    @Override
    public void error(String message) {}

    @Override
    public void fatalError(String message) {
      throw new RuntimeException(message);
    }
  };

  private static JSTypeRegistry createRegistry() {
    return new JSTypeRegistry(REPORTER);
  }

  @Test
  public void testArrowTypeWithBasicTypes() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"), createNode("string"));
    JSType returnType = NUMBER;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = createFunctionType(createNode("number"), createNode("number"), NUMBER);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithVarargs() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"), createNode("..."));
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    JSType expected = createFunctionType(createNode("number"), null, NUMBER);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithOptionalParam() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(
        createNode("number"),
        createNode("string", null, createNode("optional"))
    );
    JSType returnType = NUMBER;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = createFunctionType(
        createNode("number"),
        createNode("string"),
        null,
        createNode("optional")
    );
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    JSType expected = VOID;
    assertEquals(expected, arrowType.returnType);
  }

  @Test
  public void testArrowTypeWithUnknownReturnType() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"));
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    JSType expected = createArrowType(registry, null, null);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithUnknownParameters() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("..."), createNode("..."));
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    JSType expected = createArrowType(registry, null, null);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeEquivalence() {
    JSTypeRegistry registry = createRegistry();

    Node thisParameters = createNode(createNode("number"));
    Node thatParameters = createNode(createNode("number"), createNode("string"));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, NUMBER);
    ArrowType thatArrowType =
        new ArrowType(registry, thatParameters, NUMBER, false);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, false));
    assertTrue(thisArrowType.checkArrowEquivalenceHelper(thatArrowType, false));

    Node thatParameters2 = createNode(createNode("string"), createNode("number"));
    assertFalse(thisArrowType.hasEqualParameters(new ArrowType(registry, thatParameters2, NUMBER), false));
    assertFalse(thisArrowType.checkArrowEquivalenceHelper(new ArrowType(registry, thatParameters2, NUMBER), false));
  }

  @Test
  public void testArrowTypeSubtyping() {
    JSTypeRegistry registry = createRegistry();

    Node thisParameters = createNode(createNode("number"));
    Node thatParameters = createNode(createNode("number"), createNode("number"));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, NUMBER);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, NUMBER);

    assertTrue(thisArrowType.isSubtype(thisArrowType));
    assertTrue(thisArrowType.isSubtype(thatArrowType));
    assertFalse(thatArrowType.isSubtype(thisArrowType));

    Node thisParameters2 = createNode(createNode("number"), createNode("string"));
    assertFalse(thisArrowType.isSubtype(new ArrowType(registry, thisParameters2, NUMBER)));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = createRegistry();

    Node thisParameters = createNode(createNode("number"));
    Node thatParameters = createNode(createNode("number"), createNode("string"));
    ArrowType thisArrowType = new ArrowType(registry, thisParameters, NUMBER);
    ArrowType thatArrowType = new ArrowType(registry, thatParameters, NUMBER);

    int thisHashCode = thisArrowType.hashCode();
    int thatHashCode = thatArrowType.hashCode();

    ArrowType thisArrowType2 = new ArrowType(registry, thisParameters, NUMBER);
    int thisHashCode2 = thisArrowType2.hashCode();

    assertEquals(thisHashCode, thisHashCode2);
    assertNotEquals(thisHashCode, thatHashCode);
  }

  @Test
  public void testArrowTypeWithTemplates() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(
        createNode("number"),
        createNode("T")
    );
    JSType returnType = createFunctionType(createNode("T"), null, BOOLEAN);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = createArrowType(registry, createFunctionType(createNode("T"), null, BOOLEAN), null);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithMixedTemplates() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(
        createNode("number"),
        createNode("T"),
        createNode("U")
    );
    JSType returnType = createFunctionType(
        createNode("T"),
        createNode("U"),
        BOOLEAN
    );
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = createArrowType(registry, returnType, null);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithFunctionReturnType() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"));
    JSType returnType = createFunctionType(createNode("string"), null, NUMBER);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = createFunctionType(createNode("number"), createNode("string"), NUMBER);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithUnknownFunctionReturnType() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"));
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    JSType expected = createArrowType(registry, null, null);
    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithVoidReturnType() {
    JSTypeRegistry registry = createRegistry();

    Node parameters = createNode(createNode("number"));
    ArrowType arrowType = new ArrowType(registry, parameters, VOID);

    JSType expected = createArrowType(registry, null, null);
    assertEquals(expected, arrowType);
  }
}
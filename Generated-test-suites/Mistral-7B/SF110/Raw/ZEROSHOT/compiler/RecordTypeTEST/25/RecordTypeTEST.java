import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.types.ObjectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordProperty recordProperty;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordProperty = new RecordProperty("propertyName", JSType.STRING);
    recordType = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName", recordProperty);
    }}, true);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName", recordProperty);
    }}, true);
    RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName", recordProperty);
    }}, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.IDENTITY_EQUALS));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    JSType expectedResult = implicitPrototype;
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        implicitPrototype);
    JSType actualResult = recordType.getImplicitPrototype();

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "propertyName";
    JSType propertyType = JSType.NUMBER;

    assertTrue(recordType.defineProperty(propertyName, propertyType, false, null));

    JSType actualPropertyType = recordType.getPropertyType(propertyName);

    assertEquals(propertyType, actualPropertyType);
  }

  @Test
  public void testIsRecordType() {
    assertTrue(recordType.isRecordType());
  }

  @Test
  public void testIsSubtype() {
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName1", new RecordProperty("propertyName1", JSType.STRING));
      put("propertyName2", new RecordProperty("propertyName2", JSType.NUMBER));
    }}, true);

    RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName1", new RecordProperty("propertyName1", JSType.STRING));
      put("propertyName2", new RecordProperty("propertyName2", JSType.BOOLEAN));
    }}, true);

    assertFalse(recordType1.isSubtype(recordType2));

    RecordType recordType3 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName1", new RecordProperty("propertyName1", JSType.STRING));
      put("propertyName2", new RecordProperty("propertyName2", JSType.NUMBER));
    }}, true);

    assertTrue(recordType1.isSubtype(recordType3));

    assertTrue(objectType.isSubtype(recordType3));
  }

  @Test
  public void testIsSubtypeRecordTypeToObjectType() {
    RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName", new RecordProperty("propertyName", JSType.STRING));
    }}, true);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertFalse(recordType.isSubtype(objectType));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType stringType = JSType.STRING;
    JSType numberType = JSType.NUMBER;
    JSType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName1", new RecordProperty("propertyName1", stringType));
      put("propertyName2", new RecordProperty("propertyName2", numberType));
    }}, true);

    JSType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
      put("propertyName1", new RecordProperty("propertyName1", stringType));
    }}, true);

    JSType stringTypeUnion = new UnionTypeBuilder(registry).addAlternate(
        registry.getNativeType(JSTypeNative.STRING)).build();

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);

    JSType expectedGreatestSubtype = unionTypeBuilder.build();

    assertEquals(expectedGreatestSubtype, greatestSubtype);
  }
}
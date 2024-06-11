import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private JSTypeFactory typeFactory;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    typeFactory = mock(JSTypeFactory.class);
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testIsFrozen() {
    RecordType recordType = new RecordType(registry, null, true);
    assertFalse(recordType.isFrozen());

    recordType = new RecordType(registry, null, true);
    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(registry, null, true);
    assertFalse(recordType.isSynthetic());

    recordType = new RecordType(registry, null, false);
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(registry, null, true);
    RecordType recordType2 = new RecordType(registry, null, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));

    recordType1 = new RecordType(registry, null, true);
    recordType2 = new RecordType(registry, null, false);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));

    recordType1 = new RecordType(registry, null, true);
    recordType2 = new RecordType(registry, null, true);

    recordType1.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType2.defineProperty("name", typeFactory.createNumberType(), false, null);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, null, true);
    JSType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, null, true);

    String propertyName = "name";
    JSType propertyType = typeFactory.createStringType();
    Node propertyNode = mock(Node.class);

    recordType.defineProperty(propertyName, propertyType, false, propertyNode);

    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType(registry, null, true);
    RecordType recordType2 = new RecordType(registry, null, true);

    recordType1.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType1.defineProperty("age", typeFactory.createNumberType(), false, null);

    recordType2.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType2.defineProperty("age", typeFactory.createNumberType(), false, null);
    recordType2.defineProperty("gender", typeFactory.createStringType(), false, null);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);

    JSType expectedUnionType = unionTypeBuilder.build();
    assertEquals(expectedUnionType, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, null, true);
    RecordType recordType2 = new RecordType(registry, null, true);

    recordType1.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType1.defineProperty("age", typeFactory.createNumberType(), false, null);

    recordType2.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType2.defineProperty("age", typeFactory.createNumberType(), false, null);
    recordType2.defineProperty("gender", typeFactory.createStringType(), false, null);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithDifferentDeclaredProperty() {
    RecordType recordType1 = new RecordType(registry, null, true);
    RecordType recordType2 = new RecordType(registry, null, true);

    recordType1.defineProperty("name", typeFactory.createStringType(), true, null);
    recordType1.defineProperty("age", typeFactory.createNumberType(), false, null);

    recordType2.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType2.defineProperty("age", typeFactory.createNumberType(), false, null);
    recordType2.defineProperty("gender", typeFactory.createStringType(), false, null);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeWithDifferentPropertyType() {
    RecordType recordType1 = new RecordType(registry, null, true);
    RecordType recordType2 = new RecordType(registry, null, true);

    recordType1.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType1.defineProperty("age", typeFactory.createNumberType(), false, null);

    recordType2.defineProperty("name", typeFactory.createStringType(), false, null);
    recordType2.defineProperty("age", typeFactory.createBooleanType(), false, null);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testDefinePropertyWithFrozenRecordType() {
    RecordType recordType = new RecordType(registry, null, true);
    recordType.freeze();

    String propertyName = "name";
    JSType propertyType = typeFactory.createStringType();
    Node propertyNode = mock(Node.class);

    assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
  }
}
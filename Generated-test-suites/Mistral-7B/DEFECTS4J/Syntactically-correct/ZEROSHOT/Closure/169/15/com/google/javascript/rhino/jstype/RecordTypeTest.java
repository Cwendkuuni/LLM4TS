import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType recordTypeWithExtraProperty;
  private RecordType recordTypeWithSubtype;
  private RecordType recordTypeWithSubtype2;
  private RecordType recordTypeWithDifferentType;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    registry = new JSTypeRegistry();

    RecordProperty recordProperty = new RecordProperty("prop", registry.getStringType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop", recordProperty);

    recordType = new RecordType(registry, properties, true);

    RecordProperty recordProperty2 = new RecordProperty("prop", registry.getNumberType());
    properties = Maps.newHashMap();
    properties.put("prop", recordProperty2);

    recordTypeWithExtraProperty = new RecordType(registry, properties, true);

    RecordProperty recordProperty3 = new RecordProperty("prop", registry.getBooleanType());
    properties = Maps.newHashMap();
    properties.put("prop", recordProperty3);

    recordTypeWithSubtype = new RecordType(registry, properties, true);

    RecordType subtype = new RecordType(registry, properties, false);
    RecordProperty subtypeProperty = new RecordProperty("prop", registry.getStringType());
    properties = Maps.newHashMap();
    properties.put("prop", subtypeProperty);

    recordTypeWithSubtype2 = new RecordType(registry, properties, true);
    recordTypeWithSubtype2.defineProperty("prop", registry.getStringType(true), false, null);

    recordTypeWithDifferentType = new RecordType(registry, properties, true);
    recordTypeWithDifferentType.defineProperty("prop", registry.getNumberType(), false, null);
  }

  @Test
  public void testCreateRecordType() {
    assertNotNull(recordType);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeWithExtraProperty, true));
    assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeWithSubtype, true));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    recordType.defineProperty("prop", registry.getNumberType(), true, null);
    assertTrue(recordType.hasProperty("prop"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullRecordProperty() {
    recordType.defineProperty("prop", null, true, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType));

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithSubtype);
    assertNotNull(greatestSubtype);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(recordType, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithSubtype2);
    assertNotNull(greatestSubtype);
    assertTrue(greatestSubtype.isRecordType());
    assertEquals(recordType, greatestSubtype.toMaybeRecordType());

    greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithDifferentType);
    assertNull(greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    // Record type is a subtype of itself
    assertTrue(recordType.isSubtype(recordType));

    // Record type is a subtype of ObjectType
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Record type is not a subtype of a number type
    assertFalse(recordType.isSubtype(registry.getNumberType()));

    // Record type is a subtype of a record type with the same properties
    assertTrue(recordType.isSubtype(recordTypeWithExtraProperty));

    // Record type is not a subtype of a record type with an extra property
    assertFalse(recordTypeWithExtraProperty.isSubtype(recordType));

    // Record type is a subtype of a record type with a subtype property
    assertTrue(recordType.isSubtype(recordTypeWithSubtype));

    // Record type with a subtype property is a subtype of record type
    assertTrue(recordTypeWithSubtype.isSubtype(recordType));

    // Record type is not a subtype of a record type with a different property type
    assertFalse(recordType.isSubtype(recordTypeWithDifferentType));

    // Record type with a different property type is not a subtype of record type
    assertFalse(recordTypeWithDifferentType.isSubtype(recordType));
  }
}
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JsContext;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JsContext context = JsContext.newJsContext();
    JSTypeFactory typeFactory = context.newTypeFactory();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", typeFactory.getStringType()));
    properties.put("age", new RecordProperty("age", typeFactory.getNumberType()));

    RecordType recordType = new RecordType(typeFactory, properties, true);
    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClass().getSimpleName());
    assertEquals(2, recordType.getPropertyCount());
    assertEquals("name", recordType.getPropertyNames()[0]);
    assertEquals("age", recordType.getPropertyNames()[1]);

    JSType nameType = recordType.getPropertyType("name");
    JSType ageType = recordType.getPropertyType("age");
    assertEquals(typeFactory.getStringType(), nameType);
    assertEquals(typeFactory.getNumberType(), ageType);

    JSType emptyRecordType = typeFactory.getEmptyRecordType();
    assertFalse(recordType.isSubtype(emptyRecordType));

    RecordType recordTypeWithMoreProperties = new RecordType(typeFactory, properties, true);
    recordTypeWithMoreProperties.defineProperty("newProperty", typeFactory.getNumberType(), false, null);
    assertFalse(recordType.isSubtype(recordTypeWithMoreProperties));

    RecordType recordTypeWithLessProperties = new RecordType(typeFactory, null, true);
    assertTrue(recordType.isSubtype(recordTypeWithLessProperties));

    RecordType recordTypeWithSameProperties = new RecordType(typeFactory, properties, true);
    assertTrue(recordType.isEquivalentTo(recordTypeWithSameProperties));

    JSType recordTypeWithIncompatibleProperty = new RecordType(typeFactory, properties, true);
    recordTypeWithIncompatibleProperty.defineProperty("name", typeFactory.getBooleanType(), false, null);
    assertFalse(recordType.isSubtype(recordTypeWithIncompatibleProperty));
  }

  @Test
  public void testRecordTypeEquivalence() {
    JsContext context = JsContext.newJsContext();
    JSTypeFactory typeFactory = context.newTypeFactory();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", typeFactory.getStringType()));
    properties.put("age", new RecordProperty("age", typeFactory.getNumberType()));

    RecordType recordType1 = new RecordType(typeFactory, properties, true);
    RecordType recordType2 = new RecordType(typeFactory, properties, true);

    RecordType recordType3 = new RecordType(typeFactory, properties, true);
    recordType3.defineProperty("newProperty", typeFactory.getNumberType(), false, null);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
  }

  @Test
  public void testRecordTypePropertyAccess() {
    JsContext context = JsContext.newJsContext();
    JSTypeFactory typeFactory = context.newTypeFactory();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", typeFactory.getStringType()));
    properties.put("age", new RecordProperty("age", typeFactory.getNumberType()));

    RecordType recordType = new RecordType(typeFactory, properties, true);
    assertEquals(recordType.getProperty("name"), recordType.getPropertyType("name"));
    assertEquals(recordType.getProperty("age"), recordType.getPropertyType("age"));
    assertNull(recordType.getProperty("unknownProperty"));

    recordType.defineProperty("newProperty", typeFactory.getNumberType(), false, null);
    assertNull(recordType.getProperty("newProperty"));
  }
}
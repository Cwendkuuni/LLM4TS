import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeWithBasicProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    RecordType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("name", registry.getStringType(), null)
        .addProperty("age", registry.getNumberType(), null)
        .build();

    Assert.assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testRecordTypeWithNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", null);
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    try {
      new RecordType(registry, properties, true);
      Assert.fail("Should have thrown an exception");
    } catch (IllegalStateException e) {
      Assert.assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
    }
  }

  @Test
  public void testRecordTypeSubtypeCheck() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordTypeA = new RecordType(registry, propsA, true);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("name", new RecordProperty("name", registry.getStringType()));
    propsB.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propsB, true);

    Assert.assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeSubtypeFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordTypeA = new RecordType(registry, propsA, true);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propsB, true);

    Assert.assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeWithNullPropertiesInSubtypeCheck() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordTypeA = new RecordType(registry, propsA, true);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordTypeB = new RecordType(registry, propsB, true);

    Map<String, RecordProperty> propsC = Maps.newHashMap();
    propsC.put("name", null);
    RecordType recordTypeC = new RecordType(registry, propsC, true);

    Assert.assertFalse(recordTypeA.isSubtype(recordTypeC));
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("name", new RecordProperty("name", registry.getStringType()));
    propsA.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propsA, true);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("age", new RecordProperty("age", registry.getNumberType()));
    propsB.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordTypeB = new RecordType(registry, propsB, true);

    Assert.assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testRecordTypeEquivalenceFalse() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("name", new RecordProperty("name", registry.getStringType()));
    propsA.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordTypeA = new RecordType(registry, propsA, true);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("name", new RecordProperty("name", registry.getStringType()));
    propsB.put("age", new RecordProperty("age", registry.getNumberType()));
    propsB.put("city", new RecordProperty("city", registry.getStringType()));
    RecordType recordTypeB = new RecordType(registry, propsB, true);

    Assert.assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }
}
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import static org.junit.Assert.*;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getIntegerType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getBooleanType());
    recordType = new RecordType(registry, Maps.newHashMap(Arrays.asList(prop1, prop2)));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop2", registry.getBooleanType()))));
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop2", registry.getBooleanType()),
                     new RecordProperty("prop3", registry.getStringType()))));
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop3", registry.getStringType()))));

    // RecordType1 is a subtype of RecordType1
    assertTrue(recordType1.isSubtype(recordType1));

    // RecordType1 is a subtype of RecordType2 because it has all the same properties
    // with compatible types
    assertTrue(recordType1.isSubtype(recordType2));

    // RecordType1 is not a subtype of RecordType3 because it does not have the same properties
    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testIsEquivalent() {
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop2", registry.getBooleanType()))));
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop2", registry.getBooleanType()))));
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop3", registry.getStringType()))));

    // RecordType1 and RecordType2 are equivalent because they have the same properties
    // with the same types
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // RecordType1 and RecordType3 are not equivalent because they do not have the same properties
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test(expected = TypeError.class)
  public void testSetFrozenProperty() {
    recordType = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop2", registry.getBooleanType()))));
    recordType.defineProperty("prop1", registry.getBooleanType(), false, null);
    recordType.defineProperty("prop1", registry.getIntegerType(), false, null);
  }

  @Test(expected = TypeError.class)
  public void testDefinePropertyWithIncompatibleType() {
    recordType = new RecordType(registry, Maps.newHashMap(
        Arrays.asList(new RecordProperty("prop1", registry.getIntegerType()),
                     new RecordProperty("prop2", registry.getBooleanType()))));
    recordType.defineProperty("prop1", registry.getBooleanType(), false, null);
  }
}
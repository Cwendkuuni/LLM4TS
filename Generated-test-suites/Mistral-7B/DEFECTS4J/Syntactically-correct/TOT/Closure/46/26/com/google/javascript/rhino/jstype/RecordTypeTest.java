import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType type;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    type = new RecordType(registry, Map.of(
        "x", new JSTypeFactory().getNumberType(),
        "y", new JSTypeFactory().getStringType(),
        "z", new JSTypeFactory().getBooleanType()
    ));
  }

  @Test
  public void testRecordTypeCreation() {
    RecordType record = (RecordType) type;
    Assert.assertEquals("{ x: Number, y: String, z: Boolean }", record.toString());
  }

  @Test
  public void testRecordTypeEquivalence() {
    RecordType record1 = (RecordType) type;
    RecordType record2 = new RecordType(registry, Map.of(
        "x", new JSTypeFactory().getNumberType(),
        "y", new JSTypeFactory().getStringType(),
        "z", new JSTypeFactory().getBooleanType()
    ));

    Assert.assertTrue(record1.isEquivalentTo(record2));
  }

  @Test
  public void testRecordTypeSubtyping() {
    RecordType record1 = (RecordType) type;
    RecordType record2 = new RecordType(registry, Map.of(
        "x", new JSTypeFactory().getNumberType(),
        "y", new JSTypeFactory().getStringType(),
        "z", new JSTypeFactory().getBooleanType(),
        "w", new JSTypeFactory().getNumberType()
    ));

    Assert.assertTrue(record1.isSubtype(record2));
  }

  @Test
  public void testRecordTypeSubtypingFailure() {
    RecordType record1 = (RecordType) type;
    RecordType record2 = new RecordType(registry, Map.of(
        "x", new JSTypeFactory().getBooleanType(),
        "y", new JSTypeFactory().getStringType(),
        "z", new JSTypeFactory().getBooleanType()
    ));

    Assert.assertFalse(record1.isSubtype(record2));
  }

  @Test
  public void testRecordTypeGetProperty() {
    RecordType record = (RecordType) type;
    JSType x = record.getPropertyType("x");
    Assert.assertEquals(x.getClass(), Number.class);
  }

  @Test
  public void testRecordTypeDefineProperty() {
    RecordType record = (RecordType) type;
    record.defineProperty("w", new JSTypeFactory().getNumberType(), false, null);

    Assert.assertTrue(record.hasProperty("w"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    RecordType record = (RecordType) type;
    record.isFrozen = true;
    record.defineProperty("w", new JSTypeFactory().getNumberType(), false, null);
  }
}
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeCheckTestingHelper;
import com.google.javascript.rhino.type.ErrorType;
import com.google.javascript.rhino.type.NullType;
import com.google.javascript.rhino.type.ObjectType;
import com.google.javascript.rhino.type.RecordType;
import com.google.javascript.rhino.type.StringType;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RecordTypeBuilderTest {

  private static final JSTypeRegistry REGISTRY = new JSTypeRegistry();

  @Test
  public void testRecordTypeBuilder() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(REGISTRY);
    RecordType recordType = recordTypeBuilder
        .addProperty("name", StringType.INSTANCE, null)
        .addProperty("age", JSType.NUMBER, null)
        .build();
    TypeCheckTestingHelper.checkType(recordType, "{}");

    // Test with a record having named properties
    RecordType recordWithNamedProperties = recordTypeBuilder
        .addProperty("name", StringType.INSTANCE, null)
        .addProperty("age", JSType.NUMBER, null)
        .build();
    TypeCheckTestingHelper.checkType(recordWithNamedProperties, "{}");

    // Test with a record having default properties
    RecordType recordWithDefaultProperties = recordTypeBuilder
        .addProperty("name", StringType.INSTANCE, null)
        .addProperty("age", JSType.NUMBER, null)
        .build();
    TypeCheckTestingHelper.checkType(recordWithDefaultProperties, "{}");

    // Test record type with duplicate property
    RecordTypeBuilder recordTypeBuilderWithDuplicate = new RecordTypeBuilder(REGISTRY);
    RecordType recordTypeWithDuplicate = recordTypeBuilderWithDuplicate
        .addProperty("name", StringType.INSTANCE, null)
        .addProperty("name", StringType.INSTANCE, null)
        .build();
    TypeCheckTestingHelper.expectError(recordTypeWithDuplicate, ErrorType.DUPLICATE_PROPERTY);

    // Test record type with null registry
    RecordTypeBuilder recordTypeBuilderWithNullRegistry = new RecordTypeBuilder(null);
    RecordType recordTypeWithNullRegistry = recordTypeBuilderWithNullRegistry
        .addProperty("name", StringType.INSTANCE, null)
        .addProperty("age", JSType.NUMBER, null)
        .build();
    TypeCheckTestingHelper.expectError(recordTypeWithNullRegistry, ErrorType.NULL_TYPE_REGISTRY);

    // Test record type with empty properties
    RecordTypeBuilder recordTypeBuilderWithEmptyProperties = new RecordTypeBuilder(REGISTRY);
    RecordType recordTypeWithEmptyProperties = recordTypeBuilderWithEmptyProperties
        .build();
    TypeCheckTestingHelper.checkType(recordTypeWithEmptyProperties, ObjectType.INSTANCE);
  }
}
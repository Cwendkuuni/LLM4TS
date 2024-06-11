import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

 private static JSTypeRegistry registry = new JSTypeRegistry(new JSTypeNative());

 @Test
 public void testEmptyRecordTypeBuilder() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }

 // Test cases for Expert 1
 // ...

 // Test cases for Expert 2
 // ...

 // Test cases for Expert 3
 // ...

}
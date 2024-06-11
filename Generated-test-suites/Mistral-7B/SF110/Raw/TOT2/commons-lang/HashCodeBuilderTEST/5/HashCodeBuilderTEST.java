import static org.junit.Assert.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.IllegalAccessException;
import java.lang.reflect.NoSuchFieldException;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean value = true;
        hashCodeBuilder.append(value);
        int expectedHashCode = 37 * (37 + value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);

        // Test with false value
        hashCodeBuilder = new HashCodeBuilder();
        value = false;
        hashCodeBuilder.append(value);
        expectedHashCode = 37 * (37 + value);
        actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int value = 10;
        hashCodeBuilder.append(value);
        int expectedHashCode = 37 * (37 + value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);

        // Test with negative integer value
        hashCodeBuilder = new HashCodeBuilder();
        value = -10;
        hashCodeBuilder.append(value);
        expectedHashCode = 37 * (37 + value);
        actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCode() throws Exception {
        MyClass myClass = new MyClass();
        myClass.setField1("Field1 value");
        myClass.setField2("Field2 value");

        int expectedHashCode = 0; // Calculate the expected hash code based on the fields' values

        int actualHashCode = HashCodeBuilder.reflectionHashCode(myClass, false, null, MyClass.class, null);
        assertEquals(expectedHashCode, actualHashCode);

        // Test with null object
        MyClass nullMyClass = null;
        try {
            HashCodeBuilder.reflectionHashCode(nullMyClass, false, null, MyClass.class, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with excluded fields containing invalid String values
        try {
            HashCodeBuilder.reflectionHashCode(myClass, false, null, MyClass.class, new String[]{"invalidField"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with excluded fields of different data types
        try {
            HashCodeBuilder.reflectionHashCode(myClass, false, null, MyClass.class, new String[]{"field1", "invalidFieldType"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with excluded fields that are not accessible
        MyClassPrivate myClassPrivate = new MyClassPrivate();
        myClassPrivate.setField1("Field1 value");
        myClassPrivate.setField2("Field2 value");

        try {
            HashCodeBuilder.reflectionHashCode(myClassPrivate, false, null, MyClassPrivate.class, null);
            fail("Expected IllegalAccessException or NoSuchFieldException");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            // Expected
        }

        // Test with reflection throwing exceptions
        Field field = FieldUtils.getField(MyClass.class, "field1", true);
        try {
            HashCodeBuilder.reflectionHashCode(myClass, false, null, MyClass.class, new String[]{"field1"});
            fail("Expected IllegalAccessException or NoSuchFieldException");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            // Expected
        }

        try {
            field.setAccessible(false);
            HashCodeBuilder.reflectionHashCode(myClass, false, null, MyClass.class, new String[]{"field1"});
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Expected
        }
    }
}

class MyClass {
    private String field1;
    private String field2;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}

class MyClassPrivate {
    private String field1;
    private String field2;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @SuppressWarnings("unused")
    private String privateField;
}
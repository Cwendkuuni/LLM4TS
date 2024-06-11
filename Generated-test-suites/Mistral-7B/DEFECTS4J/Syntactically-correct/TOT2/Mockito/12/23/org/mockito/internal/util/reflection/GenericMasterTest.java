import org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithGenericField() {
        // Expert 1's test case
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() {
        // Expert 1's test case
    }

    @Test
    public void testGetGenericTypeWithNestedGenericField() {
        // Expert 1's test case
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        // Expert 1's test case
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullFieldValue() {
        // Expert 2's test case
    }

    @Test
    public void testGetGenericTypeWithNullReturnType() {
        // Expert 2's test case
    }

    @Test
    public void testGetGenericTypeWithArrayType() {
        // Expert 2's test case
    }

    private Field getField(String fieldName) {
        // code to get the field based on the field name
    }

    // Add any other test cases if needed
}

//
package org.junit.Test;

import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

    @SuppressWarnings("unchecked")
    private static final Class<Field> FIELD_CLASS = Field.class;

    @SuppressWarnings("unchecked")
    private static final Class<List<String>> LIST_STRING_CLASS = new ArrayList<String>().getClass();

    private static final GenericMaster GM = new GenericMaster();

    @org.junit.runner.RunWith(MockitoJUnitRunner.class)
    public static class TestFieldWithListGenericType {

        @org.junit.Before
        public void setUp() {
            TestField testField = new TestField();
            try {
                Field field = TestField.class.getDeclaredField("listField");
                field.setAccessible(true);
                testField.setListField(new ArrayList<String>());
                field.set(testField, new ArrayList<String>());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @org.junit.Test
        public void testGetGenericType() throws NoSuchFieldException, IllegalAccessException {
            // Given
            TestField testField = new TestField();
            Field field = TestField.class.getDeclaredField("listField");

            // When
            Class actual = GM.getGenericType(field);

            // Then
            assertEquals(List.class, actual.getComponentType());
            assertEquals(String.class, actual.getComponentType().getComponentType());
        }

        private static class TestField {
            private List<String> listField;

            public List<String> getListField() {
                return listField;
            }

            public void setListField(List<String> listField) {
                this.listField = listField;
            }
        }
    }

    @org.junit.Test
    public void testGetGenericTypeForNonGenericField() {
        // Given
        Field field = FIELD_CLASS;

        // When
        Class actual = GM.getGenericType(field);

        // Then
        assertEquals(Object.class, actual);
    }

    @org.junit.Test
    public void testGetGenericTypeForNullField() {
        // Given
        Field field = null;

        // When
        Class actual = GM.getGenericType(field);

        // Then
        assertNull(actual);
    }
}
package org.junit.runner;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a field of type List<String>
        Field field1 = FieldTestDouble.class.getDeclaredField("listField");
        field1.setAccessible(true);
        Class expected1 = String.class;
        Class actual1 = genericMaster.getGenericType(field1);
        assertEquals(expected1, actual1);

        // Test with a field of type Map<String, Integer>
        Field field2 = FieldTestDouble.class.getDeclaredField("mapField");
        field2.setAccessible(true);
        Class expected2 = Map.class;
        ParameterizedType parameterizedType2 = (ParameterizedType) field2.getGenericType();
        Type actualType2 = parameterizedType2.getActualTypeArguments()[0];
        Class actual2 = (Class) actualType2;
        assertEquals(expected2, actual2.getComponentType());
        assertEquals(String.class, actual2.getTypeParameters()[0]);
        assertEquals(Integer.class, actual2.getTypeParameters()[1]);

        // Test with a field of type Object
        Field field3 = FieldTestDouble.class.getDeclaredField("objectField");
        field3.setAccessible(true);
        Class expected3 = Object.class;
        Class actual3 = genericMaster.getGenericType(field3);
        assertEquals(expected3, actual3);

        // Test with a non-declared field
        Field field4 = FieldTestDouble.class.getSuperclass().getDeclaredField("serialVersionUID");
        field4.setAccessible(true);
        Class expected4 = Long.TYPE;
        Class actual4 = genericMaster.getGenericType(field4);
        assertEquals(expected4, actual4);

        // Test with a field of type array
        Field field5 = FieldTestDouble[].class.getDeclaredField("fields");
        field5.setAccessible(true);
        Class expected5 = FieldTestDouble[].class;
        Class actual5 = genericMaster.getGenericType(field5);
        assertEquals(expected5, actual5.getComponentType());
        assertEquals(FieldTestDouble.class, actual5);

        // Test with a field of type ParameterizedType with more than one type argument
        Field field6 = FieldTestDouble.class.getDeclaredField("mapWithThreeTypeArgumentsField");
        field6.setAccessible(true);
        Class expected6 = Map.class;
        ParameterizedType parameterizedType6 = (ParameterizedType) field6.getGenericType();
        Type[] actualTypeArguments6 = parameterizedType6.getActualTypeArguments();
        Class actual1TypeArgument6 = (Class) actualTypeArguments6[0];
        Class actual2TypeArgument6 = (Class) actualTypeArguments6[1];
        Class actual3TypeArgument6 = (Class) actualTypeArguments6[2];
        assertEquals(expected6, actual1TypeArgument6.getComponentType());
        assertEquals(String.class, actual1TypeArgument6);
        assertEquals(Integer.class, actual2TypeArgument6);
        assertEquals(Number.class, actual3TypeArgument6);
    }

    private static class FieldTestDouble {
        private List<String> listField;
        private Map<String, Integer> mapField;
        private long serialVersionUID;
        private FieldTestDouble[] fields;
        private Map<String, Map<String, List<String>>> mapWithThreeTypeArgumentsField;

        @SuppressWarnings("unchecked")
        public List<String> getListField() {
            return (List<String>) this.listField;
        }

        @SuppressWarnings("unchecked")
        public void setListField(List<String> listField) {
            this.listField = listField;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Integer> getMapField() {
            return (Map<String, Integer>) this.mapField;
        }

        @SuppressWarnings("unchecked")
        public void setMapField(Map<String, Integer> mapField) {
            this.mapField = mapField;
        }

        @SuppressWarnings("unchecked")
        public long getSerialVersionUID() {
            return this.serialVersionUID;
        }

        @SuppressWarnings("unchecked")
        public void setSerialVersionUID(long serialVersionUID) {
            this.serialVersionUID = serialVersionUID;
        }

        @SuppressWarnings("unchecked")
        public FieldTestDouble[] getFields() {
            return (FieldTestDouble[]) this.fields;
        }

        @SuppressWarnings("unchecked")
        public void setFields(FieldTestDouble[] fields) {
            this.fields = fields;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Map<String, List<String>>> getMapWithThreeTypeArgumentsField() {
            return (Map<String, Map<String, List<String>>>) this.mapWithThreeTypeArgumentsField;
        }

        @SuppressWarnings("unchecked")
        public void setMapWithThreeTypeArgumentsField(Map<String, Map<String, List<String>>> mapWithThreeTypeArgumentsField) {
            this.mapWithThreeTypeArgumentsField = mapWithThreeTypeArgumentsField;
        }
    }
}
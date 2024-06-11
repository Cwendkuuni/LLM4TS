package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.reflect.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object simpleObject = new SimpleObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(simpleObject);
        Assert.assertEquals(simpleObject.hashCode(), hashCode);

        SimpleObject arrayObject = new SimpleObject[0];
        hashCode = HashCodeBuilder.reflectionHashCode(arrayObject);
        Assert.assertEquals(-148217769, hashCode);

        SimpleObject complexObject = new ComplexObject();
        hashCode = HashCodeBuilder.reflectionHashCode(complexObject);
        Assert.assertNotEquals(complexObject.hashCode(), hashCode);

        // test transients
        ComplexObject transientObject = new ComplexObjectWithTransientField();
        hashCode = HashCodeBuilder.reflectionHashCode(transientObject, true);
        Assert.assertEquals(transientObject.hashCode(), hashCode);

        // test excludeFields
        SimpleObject excludedFieldsObject = new SimpleObjectWithExcludedFields();
        hashCode = HashCodeBuilder.reflectionHashCode(excludedFieldsObject, new String[]{"name2"});
        Assert.assertEquals(excludedFieldsObject.hashCode(), hashCode);

        // test superclass
        Object superClassObject = new SuperClassObject();
        hashCode = HashCodeBuilder.reflectionHashCode(superClassObject, SimpleObject.class, true);
        Assert.assertEquals(superClassObject.hashCode(), hashCode);
    }

    static class SimpleObject {
        String name = "name";
        int id = 1;

        @Override
        public int hashCode() {
            return name.hashCode() + id;
        }
    }

    static class ComplexObject {
        String name = "name";
        int id = 1;
        transient int transientId = 2;

        @Override
        public int hashCode() {
            return name.hashCode() + id;
        }
    }

    static class ComplexObjectWithTransientField extends ComplexObject {
        int transientId;

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    static class SimpleObjectWithExcludedFields extends SimpleObject {
        String name2 = "name2";
    }

    static class SuperClassObject extends SimpleObject {
        int superId = 1;
    }
}
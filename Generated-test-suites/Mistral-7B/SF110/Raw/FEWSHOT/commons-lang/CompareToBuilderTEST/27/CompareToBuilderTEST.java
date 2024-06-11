package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
        };
        Object rhs = new Object() {
            int i = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertThat(comparison, is(-1));

        lhs = new Object() {
            int i = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertThat(comparison, is(1));

        lhs = new Object() {
            int i = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, lhs);
        assertThat(comparison, is(0));

        lhs = null;
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompareWithCompareTransients() {
        class TransientClass {
            @SuppressWarnings("unused")
            private transient int transientField = 1;
            int nonTransientField = 2;

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof TransientClass) {
                    TransientClass other = (TransientClass) obj;
                    return this.nonTransientField == other.nonTransientField;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        }

        TransientClass lhs = new TransientClass();
        TransientClass rhs = new TransientClass();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertThat(comparison, is(0));

        lhs.transientField = 3;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertThat(comparison, is(0));

        lhs.nonTransientField = 3;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertThat(comparison, is(-1));
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        class ExcludedFieldsClass {
            int excludedField = 1;
            int includedField = 2;

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof ExcludedFieldsClass) {
                    ExcludedFieldsClass other = (ExcludedFieldsClass) obj;
                    return this.includedField == other.includedField;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        }

        ExcludedFieldsClass lhs = new ExcludedFieldsClass();
        ExcludedFieldsClass rhs = new ExcludedFieldsClass();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, ExcludedFieldsClass.class, "excludedField");
        assertThat(comparison, is(0));

        lhs.includedField = 3;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, ExcludedFieldsClass.class, "excludedField");
        assertThat(comparison, is(-1));
    }

    @Test
    public void testReflectionCompareWithSuperClass() {
        class BaseClass {
            int baseField = 1;

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof BaseClass) {
                    BaseClass other = (BaseClass) obj;
                    return this.baseField == other.baseField;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        }

        class SubClass extends BaseClass {
            int subField = 2;
        }

        SubClass lhs = new SubClass();
        SubClass rhs = new SubClass();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, BaseClass.class);
        assertThat(comparison, is(0));

        lhs.baseField = 3;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, BaseClass.class);
        assertThat(comparison, is(1));
    }
}
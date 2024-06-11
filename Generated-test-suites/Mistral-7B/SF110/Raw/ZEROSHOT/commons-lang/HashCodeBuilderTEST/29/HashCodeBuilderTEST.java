import org.apache.commons.lang3.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        // Test default constructor
        HashCodeBuilder hcb = new HashCodeBuilder();
        int hashCode = hcb.toHashCode();
        Assert.assertEquals("Default hash code should not be zero", hashCode, HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null, new String[0]));

        // Test constructor with initial odd number and multiplier odd number
        HashCodeBuilder hcb1 = new HashCodeBuilder(13, 19);
        int hashCode1 = hcb1.toHashCode();
        Assert.assertEquals("Hash code should be calculated based on initial odd number and multiplier odd number", hashCode1, 13 * 19);

        // Test append methods for boolean
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(true);
        int hashCode2 = hcb2.toHashCode();
        Assert.assertEquals("Hash code for boolean true should be 0", hashCode2, 0);
        hcb2.append(false);
        int hashCode3 = hcb2.toHashCode();
        Assert.assertEquals("Hash code for boolean false should be non-zero", hashCode3, HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null, new String[0]) ^ 1);

        // Test append methods for int
        HashCodeBuilder hcb3 = new HashCodeBuilder();
        hcb3.append(42);
        int hashCode4 = hcb3.toHashCode();
        Assert.assertEquals("Hash code for int value should be calculated correctly", hashCode4, HashCodeBuilder.reflectionHashCode(17, 37, new Integer(42), false, null, new String[0]));

        // Test append methods for array of boolean
        HashCodeBuilder hcb4 = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hcb4.append(booleanArray);
        int hashCode5 = hcb4.toHashCode();
        Assert.assertEquals("Hash code for array of boolean should be calculated correctly", hashCode5, HashCodeBuilder.reflectionHashCode(17, 37, booleanArray, false, Boolean[].class, new String[0]));

        // Test append methods for array of int
        HashCodeBuilder hcb5 = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hcb5.append(intArray);
        int hashCode6 = hcb5.toHashCode();
        Assert.assertEquals("Hash code for array of int should be calculated correctly", hashCode6, HashCodeBuilder.reflectionHashCode(17, 37, intArray, false, Integer[].class, new String[0]));

        // Test reflectionHashCode methods
        int hashCode7 = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null, new String[0]);
        int hashCode8 = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), true, null, new String[0]);
        int hashCode9 = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, Object.class, new String[0]);
        int hashCode10 = HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, Object[].class, new String[]{"length"});
        Assert.assertNotEquals("Hash codes calculated by reflectionHashCode methods should be different", hashCode7, hashCode8);
        Assert.assertNotEquals("Hash codes calculated by reflectionHashCode methods should be different", hashCode7, hashCode9);
        Assert.assertNotEquals("Hash codes calculated by reflectionHashCode methods should be different", hashCode7, hashCode10);
    }

    private static class IDKey {
        final Object key;

        IDKey(Object key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this.key);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IDKey other = (IDKey) obj;
            return Objects.equals(this.key, other.key);
        }
    }
}
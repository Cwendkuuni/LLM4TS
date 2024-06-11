package com.google.common.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Preconditions;
import com.google.common.cache.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_EmptySpec_ReturnsNull() {
        assertNull(CacheBuilderSpec.parse(""));
    }

    @Test
    public void testParse_ValidSpec_ReturnsInstance() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5," +
                "weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m");
        assertNotNull(spec);
        assertEquals(5, spec.getInitialCapacity());
        assertEquals(10, spec.getMaximumSize());
        assertEquals(20, spec.getMaximumWeight());
        assertEquals(5, spec.getConcurrencyLevel());
        assertTrue(spec.getKeyStrength().isWeak());
        assertTrue(spec.getValueStrength().isSoft());
        assertTrue(spec.isRecordStats());
        assertEquals(1, TimeUnit.HOURS.toNanos(1));
        assertEquals(30, TimeUnit.SECONDS.toNanos(30));
        assertEquals(1, TimeUnit.MINUTES.toNanos(1));
    }

    @Test
    public void testParse_InvalidSpec_ThrowsException() {
        try {
            CacheBuilderSpec.parse("invalidSpec");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testParse_DuplicateProperties_ThrowsException() {
        try {
            CacheBuilderSpec.parse("initialCapacity=5,initialCapacity=10,maximumSize=10");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testParse_UnknownProperty_ThrowsException() {
        try {
            CacheBuilderSpec.parse("unknownProperty=value");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testParse_MissingValue_ThrowsException() {
        try {
            CacheBuilderSpec.parse("initialCapacity=");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidValue_ThrowsException() {
        try {
            CacheBuilderSpec.parse("initialCapacity=invalid");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidTimeUnit_ThrowsException() {
        try {
            CacheBuilderSpec.parse("expireAfterWrite=invalidTimeUnit");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testParse_UnknownTimeUnit_ThrowsException() {
        try {
            CacheBuilderSpec.parse("expireAfterWrite=invalidTimeUnit3");
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilder_NullSpec_ReturnsEmptyBuilder() {
        assertNotNull(CacheBuilder.newBuilder());
    }

    @Test
    public void testToCacheBuilder_EmptySpec_ReturnsEmptyBuilder() {
        assertEquals(CacheBuilder.newBuilder(), CacheBuilderSpec.parse("").toCacheBuilder());
    }

    @Test
    public void testToCacheBuilder_SpecWithAllProperties_ReturnsFullyConfiguredBuilder() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5," +
                "weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m");
        final CacheBuilder<?> builder = spec.toCacheBuilder();

        assertTrue(builder instanceof CacheBuilder<?, ?>);
        assertEquals(5, builder.initialCapacity());
        assertEquals(10, builder.maximumSize());
        assertEquals(20, builder.maximumWeight());
        assertEquals(5, builder.concurrencyLevel());
        assertTrue(((CacheBuilder<?, ?>)builder).weakKeys());
        assertTrue(((CacheBuilder<?, ?>)builder).softValues());
        assertTrue(((CacheBuilder<?, ?>)builder).recordStats());
        assertEquals(1, TimeUnit.HOURS.toNanos(1), builder.expireAfterWrite(TimeUnit.HOURS));
        assertEquals(30, TimeUnit.SECONDS.toNanos(30), builder.expireAfterAccess(TimeUnit.SECONDS));
        assertEquals(1, TimeUnit.MINUTES.toNanos(1), builder.refreshAfterWrite(TimeUnit.MINUTES));
    }

    @Test
    public void testToCacheBuilder_SpecWithDuplicateProperties_ThrowsException() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,initialCapacity=10");
        try {
            spec.toCacheBuilder();
            fail();
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilder_SpecWithInvalidProperty_ThrowsException() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("unknownProperty=value");
        try {
            spec.toCacheBuilder();
            fail();
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilder_SpecWithMissingValue_ThrowsException() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=");
        try {
            spec.toCacheBuilder();
            fail();
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilder_SpecWithInvalidValue_ThrowsException() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=invalid");
        try {
            spec.toCacheBuilder();
            fail();
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilder_SpecWithInvalidTimeUnit_ThrowsException() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=invalidTimeUnit");
        try {
            spec.toCacheBuilder();
            fail();
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilder_SpecWithUnknownTimeUnit_ThrowsException() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=invalidTimeUnit3");
        try {
            spec.toCacheBuilder();
            fail();
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void testToString_NullSpec_ReturnsEmptyString() {
        assertEquals("", CacheBuilderSpec.parse(null).toString());
    }

    @Test
    public void testToString_EmptySpec_ReturnsEmptyString() {
        assertEquals("", CacheBuilderSpec.parse("").toString());
    }

    @Test
    public void testToString_ValidSpec_ReturnsParsableString() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        final String actual = spec.toString();
        final String expected = "initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5," +
                "weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m";
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        final CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        final CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        final CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=11,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        final CacheBuilderSpec spec4 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=21," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");

        assertEquals(spec1.hashCode(), spec2.hashCode());
        assertNotEqual(spec1.hashCode(), spec3.hashCode());
        assertNotEqual(spec1.hashCode(), spec4.hashCode());
    }

    @Test
    public void testEquals_SameSpec_ReturnsTrue() {
        final CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        final CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        assertTrue(spec1.equals(spec2));
    }

    @Test
    public void testEquals_DifferentSpec_ReturnsFalse() {
        final CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        final CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=31s," +
                "refreshAfterWrite=1m");
        assertFalse(spec1.equals(spec2));
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        assertFalse(spec.equals(null));
    }

    @Test
    public void testEquals_DifferentTypeObject_ReturnsFalse() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20," +
                "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=30s," +
                "refreshAfterWrite=1m");
        assertFalse(spec.equals(new Object()));
    }

    private void assertNotEqual(int expectedHashCode, int actualHashCode) {
        assertFalse("Expected hash codes to be different. Expected: " + expectedHashCode + ", Actual: " + actualHashCode,
                expectedHashCode == actualHashCode);
    }
}
package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.MockitoConfigurationError;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidate;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.FieldUtils.getFieldValue;
import static org.mockito.internal.util.reflection.FieldUtils.makeAccessible;

public class PropertyAndSetterInjectionTest {

    @Rule
    public MockitoJUnit runner = new MockitoJUnit();

    private static final String FIELD_NAME = "fieldName";
    private static final String FIELD_TYPE = "java.lang.String";
    private static final String SETTER_NAME = "set" + FIELD_NAME.substring(0, 1).toUpperCase() + FIELD_NAME.substring(1);

    @Before
    public void setUp() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
    }

    @Test
    public void shouldInjectMockUsingSetter() {
        // given
        Object owner = new TestClass();
        MockCandidate mockCandidate = mock(String.class);
        Set<MockCandidate> mocks = new HashSet<>();
        mocks.add(mockCandidate);

        // when
        assertTrue(injection.processInjection(getDeclaredField(owner, FIELD_NAME), owner, mocks));

        // then
        Object fieldValue = getFieldValue(owner, FIELD_NAME);
        assertTrue(fieldValue instanceof String);
        assertTrue(fieldValue.equals(mockCandidate));
    }

    @Test
    public void shouldInjectMockUsingField() {
        // given
        Object owner = new TestClass();
        MockCandidate mockCandidate = mock(String.class);
        Set<MockCandidate> mocks = new HashSet<>();
        mocks.add(mockCandidate);
        makeAccessible(owner, FIELD_NAME);
        setFieldValue(owner, FIELD_NAME, mockCandidate);

        // when
        assertTrue(injection.processInjection(getDeclaredField(owner, FIELD_NAME), owner, mocks));

        // then
        Object fieldValue = getFieldValue(owner, FIELD_NAME);
        assertTrue(fieldValue instanceof String);
        assertTrue(fieldValue.equals(mockCandidate));
    }

    @Test(expected = MockitoConfigurationError.class)
    public void shouldFailIfNoMockCandidatesFound() {
        // given
        Object owner = new TestClass();
        Set<MockCandidate> mocks = new HashSet<>();

        // when
        assertFalse(injection.processInjection(getDeclaredField(owner, FIELD_NAME), owner, mocks));
    }

    @Test(expected = MockitoConfigurationError.class)
    public void shouldFailIfMoreThanOneMockCandidateFound() {
        // given
        Object owner = new TestClass();
        MockCandidate mockCandidate1 = mock(String.class);
        MockCandidate mockCandidate2 = mock(String.class);
        Set<MockCandidate> mocks = new HashSet<>();
        mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        // when
        assertFalse(injection.processInjection(getDeclaredField(owner, FIELD_NAME), owner, mocks));
    }

    @Test(expected = MockitoConfigurationError.class)
    public void shouldFailIfNoSetterFound() {
        // given
        Object owner = new TestClassWithNoSetter();
        Set<MockCandidate> mocks = new HashSet<>();

        // when
        assertFalse(injection.processInjection(getDeclaredField(owner, FIELD_NAME), owner, mocks));
    }

    @Test
    public void shouldInjectMockUsingFieldIfNoSetterAvailable() {
        // given
        Object owner = new TestClassWithNoSetter();
        MockCandidate mockCandidate = mock(String.class);
        Set<MockCandidate> mocks = new HashSet<>();
        mocks.add(mockCandidate);
        makeAccessible(owner, FIELD_NAME);

        // when
        assertTrue(injection.processInjection(getDeclaredField(owner, FIELD_NAME), owner, mocks));

        // then
        Object fieldValue = getFieldValue(owner, FIELD_NAME);
        assertTrue(fieldValue instanceof String);
        assertTrue(fieldValue.equals(mockCandidate));
    }

    private Field getDeclaredField(Object owner, String fieldName) {
        try {
            return owner.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldValue(Object owner, String fieldName, Object value) {
        try {
            Field field = getDeclaredField(owner, fieldName);
            field.setAccessible(true);
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestClass {
        @InjectMocks
        private String fieldName;
    }

    private static class TestClassWithNoSetter {
        @InjectMocks
        private String fieldName;
    }
}
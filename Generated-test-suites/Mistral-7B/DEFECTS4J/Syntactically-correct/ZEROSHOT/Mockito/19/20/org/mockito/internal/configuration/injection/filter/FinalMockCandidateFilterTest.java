package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.util.reflection.FieldType.FIELD_PRIVATE;
import static org.mockito.internal.util.reflection.FieldType.PROPERTY_PRIVATE;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoRule mockitoRule = new MockitoRule();

    private Reporter reporter;
    private BeanPropertySetter beanPropertySetter;
    private FieldSetter fieldSetter;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        beanPropertySetter = new BeanPropertySetter();
        fieldSetter = new FieldSetter();
    }

    @Test
    public void testFilterCandidate_oneMock_propertySetterInjection() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("testField");
        Object testFieldValue = new Object();
        TestClass mock = mock(TestClass.class, new Object[]{testFieldValue});
        Collection<Object> mocks = Arrays.asList(mock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("testField").get(testInstance));
    }

    @Test
    public void testFilterCandidate_oneMock_fieldAccessInjection() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("testField");
        Object testFieldValue = new Object();
        TestClass mock = mock(TestClass.class, new Object[]{testFieldValue});
        Collection<Object> mocks = Arrays.asList(mock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        assertSame(testFieldValue, testInstance.getClass().getDeclaredField("testField").get(testInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInjection() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("testField");
        Collection<Object> mocks = Arrays.asList(mock(TestClass.class), mock(TestClass.class));

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        assertNull(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_propertySetterInjection() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("testField");
        Object testFieldValue = new Object();
        TestClass firstMock = mock(TestClass.class, new Object[]{testFieldValue});
        TestClass secondMock = mock(TestClass.class);
        Collection<Object> mocks = Arrays.asList(firstMock, secondMock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("testField").get(testInstance));
        assertSame(firstMock, testInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldAccessInjection() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("testField");
        Object testFieldValue = new Object();
        TestClass firstMock = mock(TestClass.class, new Object[]{testFieldValue});
        TestClass secondMock = mock(TestClass.class);
        Collection<Object> mocks = Arrays.asList(firstMock, secondMock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        assertSame(firstMock, testInstance);
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("testField").get(testInstance));
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_multipleMocks_injectionError() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("testField");
        Object testFieldValue = new Object();
        TestClass firstMock = mock(TestClass.class, new Object[]{testFieldValue});
        TestClass secondMock = mock(TestClass.class);
        Collection<Object> mocks = Arrays.asList(firstMock, secondMock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        MockUtil.getExceptionThrownDuringInjection(reporter);
    }

    @Test
    public void testFilterCandidate_privateFieldAccess() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field field = TestClass.class.getDeclaredField("_testField");
        field.setAccessible(true);
        Object testFieldValue = new Object();
        TestClass mock = mock(TestClass.class, new Object[]{testFieldValue});
        Collection<Object> mocks = Arrays.asList(mock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, testClass.cast(new Object[]{testClass, field, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("_testField").get(testInstance));
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("testField").get(testInstance));
    }

    @Test
    public void testFilterCandidate_privatePropertyAccess() {
        // given
        Class<TestClass> testClass = TestClass.class;
        Field propertyField = TestClass.class.getDeclaredField("_testProperty");
        Property property = new Property("_testProperty", PROPERTY_PRIVATE, null, null);
        Object testFieldValue = new Object();
        TestClass mock = mock(TestClass.class, new Object[]{testFieldValue}, new Object[]{property});
        Collection<Object> mocks = Arrays.asList(mock);

        // when
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, propertyField, testClass.cast(new Object[]{testClass, propertyField, testClass.cast(null)}[0]));

        // then
        Object testInstance = new Object();
        ongoingInjecter.thenInject(testInstance);
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("_testProperty").get(testInstance));
        assertEquals(testFieldValue, testInstance.getClass().getDeclaredField("testProperty").get(testInstance));
    }

    private static class TestClass {
        private Object testField;
        private transient Object _testField;

        public Object getTestProperty() {
            return testField;
        }

        @SuppressWarnings("unused")
        private void setTestProperty(Object testProperty) {
            this.testField = testProperty;
        }

        private void set_testField(Object _testField) {
            this._testField = _testField;
        }

        public Object get_testProperty() {
            return _testField;
        }
    }

    private static class Property {
        private Field field;
        private FieldType fieldType;
        private Object value;
        private Class<?> clazz;

        Property(String name, FieldType fieldType, Object value, Class<?> clazz) {
            this.field = MockitoJUnitRunner.getDeclaredField(name, clazz);
            this.fieldType = fieldType;
            this.value = value;
            this.clazz = clazz;
        }
    }
}
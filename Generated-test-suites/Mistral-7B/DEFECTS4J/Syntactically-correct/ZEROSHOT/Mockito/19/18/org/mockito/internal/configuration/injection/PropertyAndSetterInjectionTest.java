package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private List<Field> fields;
    private Object targetObject;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        targetObject = new TestTarget();
        fields = Arrays.asList(TestTarget.class.getDeclaredFields());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionThrowsException() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksField");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new MockitoTestMock(), new OtherTestMock()));
        propertyAndSetterInjection.processInjection(injectMocksField, targetObject, mockCandidates);
    }

    @Test
    public void testProcessInjectionNoMockFound() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksField");
        Set<Object> mockCandidates = newHashSet(Collections.emptyList());
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));
    }

    @Test
    public void testProcessInjectionOneMockFoundByName() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldByName");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new MockitoTestMockNamed(), new OtherTestMock()));
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNotNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));

        MockitoTestMockNamed mockitoTestMockNamed = (MockitoTestMockNamed) report.fieldInstance();
        assertNotNull(mockitoTestMockNamed);
    }

    @Test
    public void testProcessInjectionOneMockFoundByType() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldByType");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new MockitoTestMock(), new OtherTestMock()));
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNotNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));

        MockitoTestMock mockitoTestMock = (MockitoTestMock) report.fieldInstance();
        assertNotNull(mockitoTestMock);
    }

    @Test
    public void testProcessInjectionTwoMocksFoundByName() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldByName");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new MockitoTestMockNamed(), new OtherTestMockNamed(), new DifferentTestMockNamed()));
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));
    }

    @Test
    public void testProcessInjectionTwoMocksFoundByType() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldByType");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new MockitoTestMock(), new OtherTestMock(), new DifferentTestMock()));
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));
    }

    @Test
    public void testProcessInjectionNoMockFoundAndCreateInstance() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldNoMockFound");
        Set<Object> mockCandidates = newHashSet(Collections.emptyList());
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNull(report.fieldInstance());
        TestTargetNoMockFound testTargetNoMockFound = new TestTargetNoMockFound();
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));
        FieldInitializationReport report2 = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report2);
        assertNotNull(report2.fieldInstance());
        TestTargetNoMockFound testTargetNoMockFoundInstance = (TestTargetNoMockFound) report2.fieldInstance();
        assertNotNull(testTargetNoMockFoundInstance);
    }

    @Test
    public void testProcessInjectionInjectsMockBySetter() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldBySetter");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new TestSetterMock(), new OtherTestMock()));
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNotNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));

        TestSetterMock testSetterMock = (TestSetterMock) report.fieldInstance();
        assertNotNull(testSetterMock);

        TestTarget testTarget = (TestTarget) targetObject;
        testTarget.setInjectMocksFieldBySetter(testSetterMock);
        TestSetterMock testSetterMock2 = testTarget.getInjectMocksFieldBySetter();
        assertNotNull(testSetterMock2);
        assertSame(testSetterMock, testSetterMock2);
    }

    @Test
    public void testProcessInjectionInjectsMockByField() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldByField");
        Set<Object> mockCandidates = newHashSet(Arrays.asList(new TestFieldMock(), new OtherTestMock()));
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, targetObject);
        assertNotNull(report);
        assertNull(report.fieldInstance());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));

        TestFieldMock testFieldMock = new TestFieldMock();

        Field field = TestTarget.class.getDeclaredField("injectMocksFieldByField");
        field.setAccessible(true);
        field.set(targetObject, testFieldMock);

        TestTarget testTarget = (TestTarget) targetObject;
        TestFieldMock testFieldMock2 = testTarget.injectMocksFieldByField;
        assertNotNull(testFieldMock2);
        assertSame(testFieldMock, testFieldMock2);
    }

    @Test
    public void testProcessInjectionNoMockFoundButUserProvidesDepends() {
        Field injectMocksField = TestTarget.class.getDeclaredField("injectMocksFieldNoMockFoundButUserProvides");
        Set<Object> mockCandidates = newHashSet(Collections.emptyList());
        TestTarget testTarget = (TestTarget) targetObject;
        testTarget.setInjectMocksFieldNoMockFoundButUserProvides(new TestProvidedMock());
        assertFalse(propertyAndSetterInjection.injectMockCandidates(TestTarget.class, mockCandidates, targetObject));
    }

    private Set<Object> newHashSet(Collection<?> collection) {
        return new HashSet<>(collection);
    }

    private static class TestTarget {
        @InjectMocks
        private TestMocksField injectMocksField;

        @InjectMocks
        private TestMocksFieldByName injectMocksFieldByName;

        @InjectMocks
        private TestMocksFieldByType injectMocksFieldByType;

        @InjectMocks
        private TestMocksFieldNoMockFound injectMocksFieldNoMockFound;

        @InjectMocks
        private TestMocksFieldNoMockFoundButUserProvides injectMocksFieldNoMockFoundButUserProvides;

        @InjectMocks
        private TestMocksFieldBySetter injectMocksFieldBySetter;

        @InjectMocks
        private TestMocksFieldByField injectMocksFieldByField;

        private TestMocksField injectMocksFieldNoMockFound;

        public TestMocksField injectMocksFieldNoMockFound;

        public void setInjectMocksFieldBySetter(TestMocksFieldBySetter testMocksFieldBySetter) {
            this.injectMocksFieldBySetter = testMocksFieldBySetter;
        }

        public TestMocksFieldBySetter getInjectMocksFieldBySetter() {
            return injectMocksFieldBySetter;
        }

        public TestMocksField injectMocksFieldByField;

        public TestProvidedMock injectMocksFieldNoMockFoundButUserProvides;
    }

    private static class TestMocksField {
    }

    private static class TestMocksFieldByName extends TestMocksField {

        @Mock
        private String name;

        @Mock
        private String otherName;

        @Mock
        private DifferentTestMockNamed differentName;
    }

    private static class TestMocksFieldByType extends TestMocksField {

        @Mock
        private TestMocksFieldByTypeType testMocksFieldByTypeType;

        @Mock
        private DifferentTestMockType differentTestMockType;
    }

    private static class TestMocksFieldNoMockFound extends TestMocksField {

        public TestMocksFieldNoMockFound() {
        }
    }

    private static class TestMocksFieldNoMockFoundButUserProvides {

        public TestMocksFieldNoMockFoundButUserProvides() {
        }

        @Mock
        private TestProvidedMock testProvidedMock;
    }

    private static class TestMocksFieldBySetter extends TestMocksField {

        @Mock
        private TestMocksFieldBySetterType testMocksFieldBySetterType;
    }

    private static class TestMocksFieldByField extends TestMocksField {

        @Mock
        private TestMocksFieldByFieldType testMocksFieldByFieldType;
    }

    private static class TestProvidedMock {

        @Mock
        private TestProvidedMockType testProvidedMockType;
    }

    private static class TestMocksFieldByTypeType {
    }

    private static class TestMocksFieldByFieldType {
    }

    private static class TestProvidedMockType {
    }

    private static class TestTargetNoMockFound extends TestTarget {

        public TestTargetNoMockFound() {
        }

        public TestTargetNoMockFound(TestTarget testTarget) {
            super();
            this.injectMocksFieldNoMockFound = testTarget.injectMocksFieldNoMockFound;
        }

        public TestTargetNoMockFound(TestTarget testTarget, TestProvidedMock testProvidedMock) {
            super();
            this.injectMocksFieldNoMockFound = testTarget.injectMocksFieldNoMockFound;
            this.injectMocksFieldNoMockFoundButUserProvides = testProvidedMock;
        }
    }
}
package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.*;

import java.lang.reflect.*;
import java.util.*;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.junit.Assert.*;

/**
 * Tests for the PropertyAndSetterInjection class.
 */
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Class<?> testClass;
    private Object testInstance;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        testClass = new ClassMockitoTestClass();
        testInstance = new ClassMockitoTestClass().newInstance();
    }

    @Test
    public void testProcessInjectionWithNoInjectMocksAnnotation() throws Exception {
        Field fieldWithoutInjectMocksAnnotation = testClass.getField("fieldWithoutInjectMocksAnnotation");
        Set<Object> mockCandidates = newMockSafeHashSet(Mockito.mock(ClassMockitoTestClassAnother.class));
        assertFalse(propertyAndSetterInjection.processInjection(fieldWithoutInjectMocksAnnotation, testInstance, mockCandidates));
    }

    @Test
    public void testProcessInjectionWithInjectMocksAnnotationAndMockCandidateByType() throws Exception {
        Field fieldWithInjectMocksAnnotationAndMockCandidateByType = testClass.getField("fieldWithInjectMocksAnnotationAndMockCandidateByType");
        Set<Object> mockCandidates = newMockSafeHashSet(Mockito.mock(ClassMockitoTestClassAnother.class));
        assertTrue(propertyAndSetterInjection.processInjection(fieldWithInjectMocksAnnotationAndMockCandidateByType, testInstance, mockCandidates));
        assertEquals(Mockito.mock(ClassMockitoTestClassAnother.class), fieldWithInjectMocksAnnotationAndMockCandidateByType.get(testInstance));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithInjectMocksAnnotationAndMockCandidateByNameButMoreThanOne() throws Exception {
        Field fieldWithInjectMocksAnnotationAndMockCandidateByNameButMoreThanOne = testClass.getField("fieldWithInjectMocksAnnotationAndMockCandidateByNameButMoreThanOne");
        Set<Object> mockCandidates = newMockSafeHashSet(Mockito.mock(ClassMockitoTestClassAnother.class), Mockito.mock(ClassMockitoTestClassAnother2.class));
        propertyAndSetterInjection.processInjection(fieldWithInjectMocksAnnotationAndMockCandidateByNameButMoreThanOne, testInstance, mockCandidates);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithInjectMocksAnnotationAndNoMockCandidate() throws Exception {
        Field fieldWithInjectMocksAnnotationAndNoMockCandidate = testClass.getField("fieldWithInjectMocksAnnotationAndNoMockCandidate");
        Set<Object> mockCandidates = newMockSafeHashSet();
        propertyAndSetterInjection.processInjection(fieldWithInjectMocksAnnotationAndNoMockCandidate, testInstance, mockCandidates);
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Field fieldWithInjectMocksAnnotation = testClass.getField("fieldWithInjectMocksAnnotation");
        Object fieldInstanceNeedingInjection = new Object();
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(fieldWithInjectMocksAnnotation, testInstance);
        assertEquals("class org.mockito.internal.configuration.injection.PropertyAndSetterInjection$FieldInitializationReport", report.getClass().getName());
        assertNotNull(report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithConstructorException() throws Exception {
        Field fieldWithInjectMocksAnnotation = testClass.getField("fieldWithInjectMocksAnnotationWithConstructorException");
        Object fieldInstanceNeedingInjection = new Object();
        propertyAndSetterInjection.initializeInjectMocksField(fieldWithInjectMocksAnnotation, testInstance);
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Class<?> awaitingInjectionClazz = testClass;
        Set<Object> mocks = newMockSafeHashSet(Mockito.mock(ClassMockitoTestClassAnother.class), Mockito.mock(ClassMockitoTestClassAnother2.class));
        Object instance = new Object();
        assertTrue(propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
        int mockSizeBefore = mocks.size();
        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        assertTrue(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertEquals(1, orderedInstanceFields.size());
        int mockSizeAfter = mocks.size();
        assertEquals(mockSizeBefore - 2, mockSizeAfter);
    }

    @Test
    public void testInjectMockCandidatesOnFieldsWithNoMockCandidates() throws Exception {
        Class<?> awaitingInjectionClazz = testClass;
        Set<Object> mocks = newMockSafeHashSet(Mockito.mock(ClassMockitoTestClassAnother.class), Mockito.mock(ClassMockitoTestClassAnother2.class));
        Object instance = new Object();
        assertFalse(propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, Arrays.asList(testClass.getDeclaredFields())));
    }

    static class ClassMockitoTestClass {
        @InjectMocks
        private ClassMockitoTestClassAnother another;
        @InjectMocks
        private ClassMockitoTestClassAnother2 another2;

        private Field fieldWithInjectMocksAnnotationAndMockCandidateByType;
        private Field fieldWithInjectMocksAnnotationAndMockCandidateByNameButMoreThanOne;
        private Field fieldWithInjectMocksAnnotationAndNoMockCandidate;
        private Field fieldWithoutInjectMocksAnnotation;

        public ClassMockitoTestClass() {
            fieldWithInjectMocksAnnotationAndMockCandidateByType = this.getClass().getField("another");
            fieldWithInjectMocksAnnotationAndMockCandidateByNameButMoreThanOne = this.getClass().getField("another2");
            fieldWithInjectMocksAnnotationAndNoMockCandidate = this.getClass().getField("noInjectMocksField");
            fieldWithoutInjectMocksAnnotation = this.getClass().getField("fieldWithoutInjectMocksAnnotation");
        }

        public ClassMockitoTestClassAnother getAnother() {
            return another;
        }

        public ClassMockitoTestClassAnother2 getAnother2() {
            return another2;
        }
    }

    static class ClassMockitoTestClassAnother {
    }

    static class ClassMockitoTestClassAnother2 {
    }
}
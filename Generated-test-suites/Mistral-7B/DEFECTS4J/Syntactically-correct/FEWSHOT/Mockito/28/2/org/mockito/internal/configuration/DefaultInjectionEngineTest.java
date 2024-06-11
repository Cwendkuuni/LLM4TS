package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.testclasses.TestClassWithInjectMocks;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitosis.Mock;
import org.mockito.mockitosis.MockFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private static final Reporter REPORTER = new Reporter();
    private static final MockFactory MOCK_FACTORY = org.mockito.Mockito.mockitoSession().getMockFactory();

    @Test
    public void testInjectMocksOnFields() {
        TestClassWithInjectMocks testClassWithInjectMocks = new TestClassWithInjectMocks();
        Set<Object> mocks = new HashSet<>();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMocksOnFields(TestClassWithInjectMocks.class.getFieldsAnnotated(InjectMocks.class), mocks, testClassWithInjectMocks);

        // Test for the first InjectMocks field: 'injectMocksField1'
        Field injectMocksField1 = TestClassWithInjectMocks.class.getField("injectMocksField1");
        Object injectMocksField1Instance = injectMocksField1.get(testClassWithInjectMocks);
        Assert.assertNotNull(injectMocksField1Instance);
        Assert.assertEquals(MOCK_FACTORY.createMock(String.class), injectMocksField1Instance);

        // Test for the second InjectMocks field: 'injectMocksField2'
        Field injectMocksField2 = TestClassWithInjectMocks.class.getField("injectMocksField2");
        Object injectMocksField2Instance = injectMocksField2.get(testClassWithInjectMocks);
        Assert.assertNotNull(injectMocksField2Instance);
        Assert.assertEquals(MOCK_FACTORY.createMock(String[].class), injectMocksField2Instance);

        // Test for the third InjectMocks field: 'injectMocksField3'
        Field injectMocksField3 = TestClassWithInjectMocks.class.getField("injectMocksField3");
        Object injectMocksField3Instance = injectMocksField3.get(testClassWithInjectMocks);
        Assert.assertNotNull(injectMocksField3Instance);
        Assert.assertEquals(MOCK_FACTORY.createMock(TestClassWithInjectMocks.AnotherClass.class), injectMocksField3Instance);

        // Test for the fourth InjectMocks field: 'injectMocksField4'
        Field injectMocksField4 = TestClassWithInjectMocks.class.getField("injectMocksField4");
        Object injectMocksField4Instance = injectMocksField4.get(testClassWithInjectMocks);
        Assert.assertNull(injectMocksField4Instance);

        // Test for the fifth InjectMocks field: 'injectMocksField5'
        Field injectMocksField5 = TestClassWithInjectMocks.class.getField("injectMocksField5");
        try {
            injectMocksField5.setAccessible(true);
            injectMocksField5Instance = injectMocksField5.get(testClassWithInjectMocks);
            Assert.assertNull(injectMocksField5Instance);
            new Reporter().cannotInjectMockForNonPublicField(injectMocksField5);
        } catch (IllegalAccessException e) {
            new Reporter().cannotAccessNonPublicFieldForInjectMocksAnnotation(injectMocksField5.getName());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateMockCandidates() {
        TestClassWithInjectMocks testClassWithInjectMocks = new TestClassWithInjectMocks();
        Set<Object> mocks = new HashSet<>();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMocksOnFields(TestClassWithInjectMocks.class.getFieldsAnnotated(InjectMocks.class), mocks, testClassWithInjectMocks);

        Class<TestClassWithInjectMocks.AnotherClass> anotherClass = TestClassWithInjectMocks.AnotherClass.class;
        Set<Object> mocksCopy = new HashSet<>(mocks);

        for (Field field : orderedInstanceFieldsFrom(anotherClass)) {
            mockCandidateFilter.filterCandidate(mocksCopy, field, null).thenInject();
        }
    }

    private MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));

    private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        Field[] declaredFields = awaitingInjectionClazz.getDeclaredFields();
        Arrays.sort(declaredFields, new Comparator<Field>() {
            public int compare(Field field1, Field field2) {
                Class<?> field1Type = field1.getType();
                Class<?> field2Type = field2.getType();

                if (field1Type.isAssignableFrom(field2Type)) {
                    return 1;
                }
                if (field2Type.isAssignableFrom(field1Type)) {
                    return -1;
                }
                return 0;
            }
        });
        return declaredFields;
    }
}
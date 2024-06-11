package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Object testClassInstance;
    private Set<Object> mocks;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        testClassInstance = mock(TestClass.class);
        mocks = new HashSet<>();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = Arrays.asList(
                TestClass.class.getDeclaredField("injectMocksField1"),
                TestClass.class.getDeclaredField("injectMocksField2")
        ).collect(HashSet::new, HashSet::add, HashSet::addAll).toImmutableSet();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Check that mocks have been injected
        for (Field field : injectMocksFields) {
            Assert.assertNotNull(field.get(testClassInstance));
        }

        // Check that all mocks have been injected only once
        Assert.assertEquals(mocks.size(), mocks.stream().distinct().count());
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = TestClassDependent.class;
        Field field1 = awaitingInjectionClazz.getDeclaredField("fieldToInject");
        Object fieldInstance1 = mock(FieldType.class);
        mocks.add(fieldInstance1);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance1);

        // Check that mock has been set
        Field fieldToCheck = TestClassDependent.class.getDeclaredField("fieldToCheck");
        Assert.assertEquals(fieldInstance1, fieldToCheck.get(new TestClassDependent()));
    }

    @Test
    public void testInjectMockCandidate_NoMockFound() {
        Class<?> awaitingInjectionClazz = TestClassDependent.class;
        Field field1 = awaitingInjectionClazz.getDeclaredField("fieldToInject");
        Object fieldInstance1 = new TestClassNoMock();

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance1);

        // No exception should be thrown
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidate_MultipleMocksFound() {
        Class<?> awaitingInjectionClazz = TestClassDependent.class;
        Field field1 = awaitingInjectionClazz.getDeclaredField("fieldToInject");
        Object mock1 = mock(FieldType.class);
        Object mock2 = mock(FieldType.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance1);

        // Exception should be thrown
    }

    @Test
    public void testSupertypesLastComparator() {
        Class<?> superClass1 = String.class;
        Class<?> superClass2 = Object.class;
        Class<?> subClass1 = TestSubClass1.class;
        Class<?> subClass2 = TestSubClass2.class;

        Assert.assertTrue(new DefaultInjectionEngine().supertypesLast.compare(subClass1.getDeclaredField("field1"), subClass2.getDeclaredField("field1")) > 0);
        Assert.assertTrue(new DefaultInjectionEngine().supertypesLast.compare(superClass1, superClass2) < 0);
        Assert.assertTrue(new DefaultInjectionEngine().supertypesLast.compare(subClass1.getDeclaredField("field1"), superClass1) < 0);
        Assert.assertTrue(new DefaultInjectionEngine().supertypesLast.compare(subClass2.getDeclaredField("field1"), superClass1) < 0);
    }
}

class TestClass {
    @InjectMocks
    static public Field injectMocksField1;
    @InjectMocks
    static public Field injectMocksField2;
}

class TestClassDependent {
    @Inject
    static public Field fieldToInject;
    @Inject
    static public Field fieldToCheck;
}

class TestClassNoMock {
}

class TestSubClass1 {
    @Inject
    static public Field field1;
}

class TestSubClass2 {
    @Inject
    static public Field field1;
}

class FieldType {
}
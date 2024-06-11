package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private static final Reporter REPORTER = new Reporter();

    @Test
    public void testInjectMocksOnFields() {
        final Class<TestClass> testClass = TestClass.class;
        Set<Object> mocks = new HashSet<>();
        Set<Field> injectMocksFields = new HashSet<>();
        try {
            MockitoJUnitRunner runner = new MockitoJUnitRunner();
            runner.runChild(TestClass.class);
            Object testClassInstance = runner.getTestInstance();

            Field field1 = testClass.getDeclaredField("field1");
            field1.setAccessible(true);
            field1.annotate(InjectMocks.class);
            injectMocksFields.add(field1);

            Field field2 = testClass.getDeclaredField("field2");
            field2.setAccessible(true);
            field2.annotate(InjectMocks.class);
            injectMocksFields.add(field2);

            DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

            // Check if mocks were injected properly
            Object field1Instance = Whitebox.getInternalState(testClassInstance, "field1");
            Assert.assertNotNull(field1Instance);
            Assert.assertTrue(mocks.contains(field1Instance));

            Object field2Instance = Whitebox.getInternalState(testClassInstance, "field2");
            Assert.assertNotNull(field2Instance);
            Assert.assertTrue(mocks.contains(field2Instance));

            // Check if all mocks are unique
            Assert.assertEquals(mocks.size(), injectMocksFields.size());

            // Test with multiple fields having the same type
            Class<TestClass2> testClass2 = TestClass2.class;
            Object testClassInstance2 = runner.getTestInstance();

            Field field3 = testClass2.getDeclaredField("field1");
            field3.setAccessible(true);
            field3.annotate(InjectMocks.class);
            injectMocksFields.add(field3);

            Field field4 = testClass2.getDeclaredField("field1");
            field4.setAccessible(true);
            field4.annotate(InjectMocks.class);
            injectMocksFields.add(field4);

            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance2);

            // Check if only one instance of the mock was injected
            Assert.assertEquals(mocks.size(), injectMocksFields.size());
        } catch (Exception e) {
            REPORTER.mockCreationError(e);
            Assert.fail("Exception occurred during injection: " + e.getMessage());
        }
    }

    @Test
    public void testInjectMockCandidate() {
        Class<TestClass3> testClass = TestClass3.class;

        Set<Object> mocks = new HashSet<>();
        Object testInstance = new TestClass3();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Test with a field having a mock candidate by type
        {
            Field field = testClass.getDeclaredField("field1");
            field.setAccessible(true);
            field.annotate(InjectMocks.class);

            Class<MockType1> mockType = MockType1.class;
            Object mock1 = new MockType1();
            mocks.add(mock1);

            injectionEngine.injectMocksOnFields(Collections.singleton(field), mocks, testInstance);

            Object fieldInstance = Whitebox.getInternalState(testInstance, "field1");
            Assert.assertNotNull(fieldInstance);
            Assert.assertEquals(mock1, fieldInstance);
            Assert.assertFalse(mocks.contains(fieldInstance)); // Mock should be removed from the mocks list
        }

        // Test with a field having a mock candidate by name
        {
            Field field = testClass.getDeclaredField("field2");
            field.setAccessible(true);
            field.annotate(InjectMocks.class);

            Class<MockType2> mockType = MockType2.class;
            Object mock1 = new MockType2();
            Object mock2 = new MockType2();
            mocks.addAll(Arrays.asList(mock1, mock2));

            injectionEngine.injectMocksOnFields(Collections.singleton(field), mocks, testInstance);

            Object fieldInstance = Whitebox.getInternalState(testInstance, "field2");
            Assert.assertNotNull(fieldInstance);
            Assert.assertTrue(mocks.contains(fieldInstance));
            Assert.assertEquals(mock1, fieldInstance); // Any mock with the same name should be used
        }

        // Test with a field having no mock candidate
        {
            Field field = testClass.getDeclaredField("field3");
            field.setAccessible(true);
            field.annotate(InjectMocks.class);

            injectionEngine.injectMocksOnFields(Collections.singleton(field), mocks, testInstance);

            Object fieldInstance = Whitebox.getInternalState(testInstance, "field3");
            Assert.assertNull(fieldInstance);
        }
    }

    @Test
    public void testInjectMockCandidateWithMultipleFields() {
        Class<TestClass4> testClass = TestClass4.class;

        Set<Object> mocks = new HashSet<>();
        Object testInstance = new TestClass4();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Field field1 = testClass.getDeclaredField("field1");
        field1.setAccessible(true);
        field1.annotate(InjectMocks.class);

        Field field2 = testClass.getDeclaredField("field2");
        field2.setAccessible(true);
        field2.annotate(InjectMocks.class);

        Class<MockType1> mockType1 = MockType1.class;
        Object mock1 = new MockType1();
        mocks.add(mock1);

        Class<MockType2> mockType2 = MockType2.class;
        Object mock2 = new MockType2();
        Object mock3 = new MockType2();
        mocks.addAll(Arrays.asList(mock2, mock3));

        injectionEngine.injectMocksOnFields(Collections.unmodifiableSet(Arrays.asList(field1, field2)), mocks, testInstance);

        Object field1Instance = Whitebox.getInternalState(testInstance, "field1");
        Assert.assertNotNull(field1Instance);
        Assert.assertEquals(mock1, field1Instance);

        Object field2Instance = Whitebox.getInternalState(testInstance, "field2");
        Assert.assertNotNull(field2Instance);
        Assert.assertTrue(mocks.contains(field2Instance));
        Assert.assertEquals(mock2, field2Instance);
    }

    @Test
    public void testInjectMockCandidateWithEmptyMockSet() {
        Class<TestClass5> testClass = TestClass5.class;

        Set<Field> injectMocksFields = new HashSet<>();
        Object testInstance = new TestClass5();

        Field field1 = testClass.getDeclaredField("field1");
        field1.setAccessible(true);
        field1.annotate(InjectMocks.class);
        injectMocksFields.add(field1);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        injectionEngine.injectMocksOnFields(injectMocksFields, Collections.emptySet(), testInstance);

        Assert.assertNull(Whitebox.getInternalState(testInstance, "field1"));
    }
}

class TestClass {
    @InjectMocks
    private static class InnerClassField1 {
    }

    @InjectMocks
    private static class InnerClassField2 {
    }

    @InjectMocks
    private static class InnerClassField3 {
    }

    @InjectMocks
    private static class InnerClassField4 {
    }

    @InjectMocks
    private static class InnerClassField5 {
    }

    @InjectMocks
    private static class InnerClassField6 {
    }

    @InjectMocks
    private static class InnerClassField7 {
    }

    @InjectMocks
    private static class InnerClassField8 {
    }

    @InjectMocks
    private static class InnerClassField9 {
    }

    @InjectMocks
    private static class InnerClassField10 {
    }

    private String field1;
    @InjectMocks
    private TestClass2 field2;

    public void testMethod() {
    }
}

class TestClass2 {
    @InjectMocks
    private MockType1 field1;
}

class MockType1 {
}

class MockType2 {
}

class TestClass3 {
    @InjectMocks
    private MockType1 field1;
}

class TestClass4 {
    @InjectMocks
    private MockType1 field1;

    @InjectMocks
    private MockType2 field2;
}

class TestClass5 {
    @InjectMocks
    private MockType1 field1;
}
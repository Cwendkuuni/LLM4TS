package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitosis.annotations.InjectMocks;
import org.mockito.mockitosis.internal.mocking.MockMaker;
import org.mockito.mockitosis.internal.util.ClassUtil;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultInjectionEngineTest {

    private static final Class<?> TEST_CLASS = TestClassUnderTest.class;
    private static final MockCandidateFilter MOCK_CANDIDATE_FILTER = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));

    @Test
    public void testInjectMocksOnFields() {
        Reporter reporter = mock(Reporter.class);
        Set<Object> mocks = new HashSet<>();
        Set<Field> injectMocksFields = getFieldsAnnotatedWithInjectMocks(TEST_CLASS);

        new DefaultInjectionEngine().injectMocksOnFields(injectMocksFields, mocks, new Object() {});

        for (Field field : injectMocksFields) {
            Object injectedMock = Whitebox.getInternalState(field, "mocked");
            Assert.assertNotNull(injectedMock);
            mocks.remove(injectedMock);
        }

        Assert.assertEquals(0, mocks.size());
    }

    @Test
    public void testInjectMockCandidate_noMockCandidate_nullField() {
        Reporter reporter = mock(Reporter.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = null;

        new DefaultInjectionEngine().injectMockCandidate(Object.class, mocks, fieldInstance);

        verify(reporter).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
    }

    @Test
    public void testInjectMockCandidate_mockCandidateFoundByType() {
        Reporter reporter = mock(Reporter.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = mock(TestClassUnderTest.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "mock";
            }
        });
        Object expectedMock = mock(TestType.class);

        when(MOCK_CANDIDATE_FILTER.filterCandidate(mocks, any(Field.class), any(Object.class))).then(new OngoingStubbing<MockCandidateFilter>() {
            @Override
            public MockCandidateFilter value() {
                return mock(MockCandidateFilter.class);
            }

            @Override
            public MockCandidateFilter thenInject() {
                MockCandidateFilter mockCandidateFilter = value();
                when(mockCandidateFilter.getMockCandidate(any(Class.class))).thenReturn(expectedMock);
                return mockCandidateFilter;
            }
        });

        new DefaultInjectionEngine().injectMockCandidate(TestType.class.getDeclaredField("field").getType(), mocks, fieldInstance);

        Assert.assertEquals(expectedMock, Whitebox.getInternalState(TestType.class.getDeclaredField("field"), "mocked"));
        mocks.remove(expectedMock);
    }

    @Test
    public void testInjectMockCandidate_mockCandidateFoundByName() {
        Reporter reporter = mock(Reporter.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = mock(TestClassUnderTest.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "fieldInstance";
            }
        });
        Object expectedMock = mock(TestType2.class);

        when(MOCK_CANDIDATE_FILTER.filterCandidate(mocks, any(Field.class), any(Object.class))).then(new OngoingStubbing<MockCandidateFilter>() {
            @Override
            public MockCandidateFilter value() {
                return mock(MockCandidateFilter.class);
            }

            @Override
            public MockCandidateFilter thenInject() {
                MockCandidateFilter mockCandidateFilter = value();
                when(mockCandidateFilter.getMockCandidate(eq(TestType2.class))).thenReturn(expectedMock);
                return mockCandidateFilter;
            }
        });

        new DefaultInjectionEngine().injectMockCandidate(TestType2.class, mocks, fieldInstance);

        Assert.assertEquals(expectedMock, Whitebox.getInternalState(TestType2.class, "mock"));
        mocks.remove(expectedMock);
    }

    @Test
    public void testInjectMockCandidate_noMockCandidateFound() {
        Reporter reporter = mock(Reporter.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = mock(TestClassUnderTest.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "fieldInstance";
            }
        });

        new DefaultInjectionEngine().injectMockCandidate(Object.class, mocks, fieldInstance);

        verify(reporter).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
    }

    @Test
    public void testInjectMockCandidate_multipleMockCandidatesFound() {
        Reporter reporter = mock(Reporter.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = mock(TestClassUnderTest.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "fieldInstance";
            }
        });
        Object mock1 = mock(TestType3.class);
        Object mock2 = mock(TestType3.class);

        when(MOCK_CANDIDATE_FILTER.filterCandidate(mocks, any(Field.class), any(Object.class))).then(new OngoingStubbing<MockCandidateFilter>() {
            @Override
            public MockCandidateFilter value() {
                return mock(MockCandidateFilter.class);
            }

            @Override
            public MockCandidateFilter thenInject() {
                MockCandidateFilter mockCandidateFilter = value();
                when(mockCandidateFilter.getMockCandidates(any(Class.class))).thenReturn(asList(mock1, mock2));
                return mockCandidateFilter;
            }
        });

        new DefaultInjectionEngine().injectMockCandidate(TestType3.class, mocks, fieldInstance);

        Assert.assertNull(Whitebox.getInternalState(TestType3.class, "mocked"));
    }

    private Set<Field> getFieldsAnnotatedWithInjectMocks(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .forEach(fields::add);
        for (Class<?> superclass = clazz.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            fields.addAll(getFieldsAnnotatedWithInjectMocks(superclass));
        }
        return Collections.unmodifiableSet(fields);
    }

}

class TestType {
    @InjectMocks
    private TestType2 field;
    private static Object mocked;

    public static Object getMock() {
        return mocked;
    }
}

class TestType2 {
    @InjectMocks
    private TestType3 field;
    private static Object mock;
}

class TestType3 {
}

class TestClassUnderTest {
    @InjectMocks
    private TestType field;
}
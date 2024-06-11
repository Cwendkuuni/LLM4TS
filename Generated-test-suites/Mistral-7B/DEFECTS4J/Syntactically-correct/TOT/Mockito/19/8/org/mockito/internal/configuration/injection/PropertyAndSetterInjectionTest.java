import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimplePropertyAndSetterInjection() {
        // given
        MyClass myClass = new MyClass();
        MyDependency myDependency = mock(MyDependency.class);
        MyAnotherDependency myAnotherDependency = mock(MyAnotherDependency.class);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Set<Object> mocks = new HashSet<>();
        mocks.add(myDependency);
        mocks.add(myAnotherDependency);

        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        fieldAnnotatedClass.setMyDependency(myDependency);
        fieldAnnotatedClass.setMyAnotherDependency(myAnotherDependency);

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks);
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, mocks);

        assertNotNull(fieldAnnotatedClass.getMyDependency());
        assertNotNull(fieldAnnotatedClass.getMyAnotherDependency());

        verify(myDependency).isMock();
        verify(myAnotherDependency).isMock();
    }

    @Test
    public void testNullFieldInstance() {
        // given
        MyClassWithInjectMocks fieldAnnotatedClass = null;
        Set<Object> mocks = new HashSet<>();

        // when & then
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        assertThrows(NullPointerException.class, () -> injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks));
        assertThrows(NullPointerException.class, () -> injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, mocks));
    }

    @Test
    public void testNoMockCandidates() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        Set<Object> mocks = new HashSet<>();

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        assertFalse(injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks));
        assertFalse(injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, mocks));
    }

    @Test
    public void testMockCandidateByType() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        MyDependency mockDependency = mock(MyDependency.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mockDependency);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks);

        assertNotNull(fieldAnnotatedClass.getMyDependency());
        verify(mockDependency).isMock();
    }

    @Test
    public void testMockCandidateByName() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        MyAnotherDependency mockDependency = mock(MyAnotherDependency.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(myDependency);
        mocks.add(mockDependency);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, mocks);

        assertNotNull(fieldAnnotatedClass.getMyAnotherDependency());
        verify(mockDependency).isMock();
    }

    @Test(expected = IllegalAccessException.class)
    public void testPrivateFieldAccess() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        Set<Object> mocks = new HashSet<>();

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getPrivateField(MyClassWithInjectMocks.class, "privateMyDependency"), fieldAnnotatedClass, mocks);
    }

    @Test
    public void testNoSetterFound() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithNoSetterInjectMocks fieldAnnotatedClass = new MyClassWithNoSetterInjectMocks();
        Set<Object> mocks = new HashSet<>();

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        assertFalse(injection.processInjection(FieldUtil.getField(MyClassWithNoSetterInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks));
        assertFalse(injection.processInjection(FieldUtil.getField(MyClassWithNoSetterInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, mocks));
    }

    @Test
    public void testInjectingNullMockCandidate() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        Set<Object> mocks = new HashSet<>();

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, Collections.singleton(null));
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, Collections.singleton(null));

        assertNull(fieldAnnotatedClass.getMyDependency());
        assertNull(fieldAnnotatedClass.getMyAnotherDependency());
    }

    @Test
    public void testInjectingNonMockCandidate() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        MyNonMockDependency nonMockDependency = new MyNonMockDependency();
        Set<Object> mocks = new HashSet<>();
        mocks.add(nonMockDependency);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks);
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myAnotherDependency"), fieldAnnotatedClass, mocks);

        assertNull(fieldAnnotatedClass.getMyDependency());
        assertNull(fieldAnnotatedClass.getMyAnotherDependency());
        verifyNoInteractions(nonMockDependency);
    }

    @Test
    public void testMockInitializationByConstructor() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        Set<Object> mocks = new HashSet<>();

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks);

        MyDependency mockDependency = fieldAnnotatedClass.getMyDependency();
        assertNotNull(mockDependency);
        assertNotNull(mockDependency.initMethodCalled);
        verify(mockDependency).init();
    }

    @Test
    public void testPassingTwoCandidatesWithTypeBasedFilter() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        MyDependency dependency1 = mock(MyDependency.class);
        MyDependency dependency2 = mock(MyDependency.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(dependency1);
        mocks.add(dependency2);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks);

        MyDependency injectedDependency = fieldAnnotatedClass.getMyDependency();
        assertNotNull(injectedDependency);
        verify(dependency1).isMock();
        verify(dependency2).isMock();
        verify(dependency1, never()).init();
        verify(dependency2, never()).init();

        ArgumentCaptor<MyDependency> dependencyCaptor = ArgumentCaptor.forClass(MyDependency.class);
        verify(fieldAnnotatedClass).setMyDependency(dependencyCaptor.capture());
        assertEquals(dependency1, dependencyCaptor.getValue());
    }

    @Test
    public void testPassingTwoCandidatesWithNameBasedFilter() {
        // given
        MyClass myClass = new MyClass();
        MyClassWithInjectMocks fieldAnnotatedClass = new MyClassWithInjectMocks();
        MyDependency dependency1 = mock(MyDependency.class, Mockito.withName("mock1"));
        MyDependency dependency2 = mock(MyDependency.class, Mockito.withName("mock2"));
        Set<Object> mocks = new HashSet<>();
        mocks.add(dependency1);
        mocks.add(dependency2);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // then
        injection.processInjection(FieldUtil.getField(MyClassWithInjectMocks.class, "myDependency"), fieldAnnotatedClass, mocks);

        MyDependency injectedDependency = fieldAnnotatedClass.getMyDependency();
        assertNotNull(injectedDependency);
        verify(dependency1).isMock();
        verify(dependency2).isMock();
        verify(dependency1, never()).init();
        verify(dependency2, never()).init();

        ArgumentCaptor<MyDependency> dependencyCaptor = ArgumentCaptor.forClass(MyDependency.class);
        verify(fieldAnnotatedClass).setMyDependency(dependencyCaptor.capture());
        assertEquals(dependency2, dependencyCaptor.getValue());
    }

    private static Field getPrivateField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return Arrays.asList(awaitingInjectionClazz.getDeclaredFields())
                .stream()
                .filter(field -> !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
    }

    static class MyClass {
    }

    static class MyClassWithInjectMocks {
        @InjectMocks private MyDependency myDependency;
        private MyAnotherDependency myAnotherDependency;

        public void setMyDependency(MyDependency myDependency) {
            this.myDependency = myDependency;
        }

        public void setMyAnotherDependency(MyAnotherDependency myAnotherDependency) {
            this.myAnotherDependency = myAnotherDependency;
        }

        public MyDependency getMyDependency() {
            return myDependency;
        }

        public MyAnotherDependency getMyAnotherDependency() {
            return myAnotherDependency;
        }
    }

    static class MyClassWithNoSetterInjectMocks {
        @InjectMocks private MyDependency myDependency;

        public MyDependency getMyDependency() {
            return myDependency;
        }
    }

    static class MyDependency {
        boolean initMethodCalled = false;

        public void init() {
            initMethodCalled = true;
        }

        public MyDependency() {
            init();
        }
    }

    static class MyAnotherDependency {
    }

    static class MyNonMockDependency {
    }
}
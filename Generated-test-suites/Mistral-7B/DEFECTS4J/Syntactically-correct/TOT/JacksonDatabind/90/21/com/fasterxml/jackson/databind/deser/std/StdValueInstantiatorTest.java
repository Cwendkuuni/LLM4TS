import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.serializer.BeanSerializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StdValueInstantiatorTest {

    private StdValueInstantiator valueInstantiator;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        ObjectMapper.SetupContext context = new ObjectMapper.SetupContext();
        objectMapper = new ObjectMapper(context);
        valueInstantiator = objectMapper.getValueInstantiator();
    }

    @Test
    public void testDefaultCreator() throws Exception {
        Class<DummyClass> dummyClass = DummyClass.class;
        AnnotatedClass annotatedClass = new AnnotatedClass(dummyClass);
        Method defaultCreatorMethod = DummyClass.class.getDeclaredMethod("defaultConstructor");
        AnnotatedMethod defaultCreatorAnnotatedMethod = new AnnotatedMethod(defaultCreatorMethod, annotatedClass);
        valueInstantiator.configureFromObjectSettings(defaultCreatorAnnotatedMethod, null, null, null, null, null);

        Object result = valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
        verify(defaultCreatorMethod).invoke(Mockito.any());
        assertEquals(DummyClass.class, result.getClass());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        Class<DummyDelegateClass> delegateClass = DummyDelegateClass.class;
        JavaType delegateType = new TypeReference<DummyDelegateClass>() {}.getType();
        AnnotatedClass annotatedDelegateClass = new AnnotatedClass(delegateClass);
        Method delegateCreatorMethod = DummyDelegateClass.class.getDeclaredMethod("delegateConstructor");
        AnnotatedMethod delegateCreatorAnnotatedMethod = new AnnotatedMethod(delegateCreatorMethod, annotatedDelegateClass);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{new TestProperty("arg1")};

        Object dummyDelegateInstance = mock(DummyDelegateClass.class);
        valueInstantiator.configureFromObjectSettings(null, delegateCreatorAnnotatedMethod, delegateType, delegateArguments, null, null);
        valueInstantiator.configureUsingDelegate(objectMapper.getDeserializationContext(), dummyDelegateInstance);

        Object result = valueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), dummyDelegateInstance);
        verify(delegateCreatorMethod).invoke(dummyDelegateInstance);
        assertEquals(DummyDelegateClass.class, result.getClass());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        Class<DummyArrayDelegateClass> arrayDelegateClass = DummyArrayDelegateClass.class;
        JavaType arrayDelegateType = new TypeReference<DummyArrayDelegateClass[]>() {}.getType();
        AnnotatedClass annotatedArrayDelegateClass = new AnnotatedClass(arrayDelegateClass);
        Method arrayDelegateCreatorMethod = DummyArrayDelegateClass.class.getDeclaredMethod("arrayDelegateConstructor");
        AnnotatedMethod arrayDelegateCreatorAnnotatedMethod = new AnnotatedMethod(arrayDelegateCreatorMethod, annotatedArrayDelegateClass);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[]{new TestProperty("arg1")};

        Object dummyArrayDelegateInstance = mock(DummyArrayDelegateClass[].class);
        valueInstantiator.configureFromArraySettings(arrayDelegateCreatorAnnotatedMethod, arrayDelegateType, arrayDelegateArguments);
        valueInstantiator.configureUsingArrayDelegate(objectMapper.getDeserializationContext(), dummyArrayDelegateInstance);

        Object[] result = (Object[]) valueInstantiator.createUsingArrayDelegate(objectMapper.getDeserializationContext(), dummyArrayDelegateInstance);
        verify(arrayDelegateCreatorMethod).invoke(dummyArrayDelegateInstance);
        assertEquals(DummyArrayDelegateClass[].class, result.getClass());
        assertEquals(DummyArrayDelegateClass.class, result[0].getClass());
    }

    @Test
    public void testFromIntCreator() throws Exception {
        Class<DummyFromIntClass> fromIntClass = DummyFromIntClass.class;
        AnnotatedClass annotatedFromIntClass = new AnnotatedClass(fromIntClass);
        Method fromIntCreatorMethod = DummyFromIntClass.class.getDeclaredMethod("fromIntConstructor");
        AnnotatedMethod fromIntCreatorAnnotatedMethod = new AnnotatedMethod(fromIntCreatorMethod, annotatedFromIntClass);
        valueInstantiator.configureFromIntCreator(fromIntCreatorAnnotatedMethod);

        Object result = valueInstantiator.createFromInt(objectMapper.getDeserializationContext(), 1);
        verify(fromIntCreatorMethod).invoke(Mockito.any(Integer.class));
        assertEquals(DummyFromIntClass.class, result.getClass());
    }

    @Test
    public void testFromStringCreator() throws Exception {
        Class<DummyFromStringClass> fromStringClass = DummyFromStringClass.class;
        AnnotatedClass annotatedFromStringClass = new AnnotatedClass(fromStringClass);
        Method fromStringCreatorMethod = DummyFromStringClass.class.getDeclaredMethod("fromStringConstructor");
        AnnotatedMethod fromStringCreatorAnnotatedMethod = new AnnotatedMethod(fromStringCreatorMethod, annotatedFromStringClass);
        valueInstantiator.configureFromStringCreator(fromStringCreatorAnnotatedMethod);

        Object result = valueInstantiator.createFromString(objectMapper.getDeserializationContext(), "test");
        verify(fromStringCreatorMethod).invoke(Mockito.any(String.class));
        assertEquals(DummyFromStringClass.class, result.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullValueClass() throws Exception {
        valueInstantiator = new StdValueInstantiator(null, null);
    }

    private static class DummyClass {
        public DummyClass() {
        }

        public static DummyClass defaultConstructor() {
            return new DummyClass();
        }
    }

    private static class DummyDelegateClass {
        public DummyDelegateClass() {
        }

        public static DummyDelegateClass delegateConstructor(DummyDelegateClass delegate) {
            return new DummyDelegateClass();
        }
    }

    private static class DummyArrayDelegateClass {
        public DummyArrayDelegateClass() {
        }

        public static DummyArrayDelegateClass[] arrayDelegateConstructor(DummyArrayDelegateClass[] arrayDelegate) {
            return new DummyArrayDelegateClass[0];
        }
    }

    private static class DummyFromIntClass {
        public DummyFromIntClass(int arg1) {
        }

        public static DummyFromIntClass fromIntConstructor(int value) {
            return new DummyFromIntClass(value);
        }
    }

    private static class DummyFromStringClass {
        public DummyFromStringClass(String arg1) {
        }

        public static DummyFromStringClass fromStringConstructor(String value) {
            return new DummyFromStringClass(value);
        }
    }

    private static class TestProperty implements SettableBeanProperty {
        private final String name;

        public TestProperty(String name) {
            this.name = name;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object value) {

        }

        @Override
        public BeanSerializerFactory getSerializerFactory() {
            return null;
        }

        @Override
        public Converter<?> getValueConverter(DeserializationContext ctxt, BeanProperty property, TypeSerializer valueTypeSerializer) {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public AnnotatedMethod getSetter() {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
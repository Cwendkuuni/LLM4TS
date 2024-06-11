import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StdValueInstantiator Test Cases")
public class StdValueInstantiatorTest {

    private static StdValueInstantiator valueInstantiator;

    @BeforeAll
    public static void setup() {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        StdValueInstantiatorTest.valueInstantiator = valueInstantiator;
    }

    // Basic Test Cases

    @Test
    @DisplayName("Test Create Using Default")
    public void testCreateUsingDefault() throws Exception {
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals("default", valueInstantiator.createUsingDefault(mockContext()).getValue());
    }

    @Test
    @DisplayName("Test Create Using Delegate")
    public void testCreateUsingDelegate() throws Exception {
        JavaType delegateType = TypeFactory.defaultInstance().constructType(ToStringSerializer.class);
        Object delegate = mapper.readValue(new ObjectMapper().writeValueAsString(new ToStringSerializer()), ToStringSerializer.class);
        assertEquals(delegate, valueInstantiator.createUsingDelegate(mockContext(), delegate).getValue());
    }

    // Edge Cases and Exception Handling

    @Test
    @DisplayName("Test Create Using Default - Null Value")
    public void testCreateUsingDefault_NullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> valueInstantiator.createUsingDefault(mockContext()));
    }

    @Test
    @DisplayName("Test Create Using Delegate - Null Delegate")
    public void testCreateUsingDelegate_NullDelegate() {
        Assertions.assertThrows(NullPointerException.class, () -> valueInstantiator.createUsingDelegate(mockContext(), null));
    }

    @Test
    @DisplayName("Test Create Using Array Delegate")
    public void testCreateUsingArrayDelegate() throws Exception {
        JavaType arrayDelegateType = TypeFactory.defaultInstance().constructType(int[].class);
        int[] delegate = new int[]{1, 2, 3};
        assertEquals(delegate, valueInstantiator.createUsingArrayDelegate(mockContext(), delegate).getValue());
    }

    @Test
    @DisplayName("Test Create Using Array Delegate - Null Delegate")
    public void testCreateUsingArrayDelegate_NullDelegate() {
        Assertions.assertThrows(NullPointerException.class, () -> valueInstantiator.createUsingArrayDelegate(mockContext(), null));
    }

    @Test
    @DisplayName("Test Create From Int - Negative Value")
    public void testCreateFromInt_NegativeValue() throws Exception {
        assertEquals(-1, valueInstantiator.createFromInt(mockContext(), -1).getValue().intValue());
    }

    @Test
    @DisplayName("Test Create From Int - Long Value")
    public void testCreateFromInt_LongValue() throws Exception {
        assertEquals(1L, valueInstantiator.createFromInt(mockContext(), 1L).getValue().longValue());
    }

    @Test
    @DisplayName("Test Create From Long - Negative Value")
    public void testCreateFromLong_NegativeValue() throws Exception {
        assertEquals(-1L, valueInstantiator.createFromLong(mockContext(), -1L).getValue().longValue());
    }

    @Test
    @DisplayName("Test Create From Double - Infinite Value")
    public void testCreateFromDouble_InfiniteValue() throws Exception {
        AnnotatedMethod creator = mock(AnnotatedMethod.class);
        when(creator.getName()).thenReturn("fromDouble");

        valueInstantiator.configureFromDoubleCreator(creator);

        assertEquals(Double.POSITIVE_INFINITY, valueInstantiator.createFromDouble(mockContext(), Double.POSITIVE_INFINITY).getValue());
    }

    @Test
    @DisplayName("Test Create From Boolean - False Value")
    public void testCreateFromBoolean_FalseValue() throws Exception {
        assertEquals(Boolean.FALSE, valueInstantiator.createFromBoolean(mockContext(), Boolean.FALSE).getValue());
    }

    @Test
    @DisplayName("Test Create From Boolean - True Value")
    public void testCreateFromBoolean_TrueValue() throws Exception {
        assertEquals(Boolean.TRUE, valueInstantiator.createFromBoolean(mockContext(), Boolean.TRUE).getValue());
    }

    @Test
    @DisplayName("Test Create Using Object With - Null Arguments")
    public void testCreateUsingObjectWith_NullArguments() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[0];

        valueInstantiator.configureFromObjectWith(creator, constructorArguments);

        Assertions.assertThrows(NullPointerException.class, () -> valueInstantiator.createUsingObjectWith(mockContext(), new Object[0]));
    }

    // Helper methods

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }

    private Object createValueUsingMethod(AnnotatedMethod creator, Object... args) throws IOException {
        when(creator.call(eq(valueInstantiator), anyArray())).thenCallRealMethod();

        return valueInstantiator.createUsingDelegate(mockContext(), creator).getValue();
    }

    private Object createObjectWithArguments(AnnotatedWithParams creator, Object[] args) throws IOException {
        SettableBeanProperty[] constructorArguments = (SettableBeanProperty[]) args;

        valueInstantiator.configureFromObjectWith(creator, constructorArguments);

        return valueInstantiator.createUsingObjectWith(mockContext(), new Object[0]);
    }

    private void configureArrayDelegate(AnnotatedWithParams creator, JavaType arrayDelegateType,
                                         SettableBeanProperty[] arrayDelegateArguments) {
        valueInstantiator.configureFromArraySettings(creator, arrayDelegateType, arrayDelegateArguments);
    }
}
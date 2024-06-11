package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.*;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.whitebox.WhiteboxImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PropertyAndSetterInjectionTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private Whitebox whitebox;

    @Before
    public void setup() {
        whitebox = new WhiteboxImpl();
    }

    @Test
    public void testProcessInjection_noMocksFound() throws Exception {
        // Given
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
        assertNull(whitebox.getInternalState(instance, "injectMocksField1"));
    }

    @Test
    public void testProcessInjection_oneMockFoundByType() throws Exception {
        // Given
        Object mock = mock(TestMockClass.class);
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        // When
        Whitebox.setInternalState(instance, "injectMocksField1", mock);
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(Whitebox.getInternalState(instance, "injectMocksField1") == mock);
    }

    @Test
    public void testProcessInjection_noMockFoundByTypeButOneFoundByName() throws Exception {
        // Given
        Object mock1 = mock(TestMockClass1.class);
        Object mock2 = mock(TestMockClass2.class);
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock1);
        mockCandidates.add(mock2);

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
        assertNull(Whitebox.getInternalState(instance, "injectMocksField1"));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_multipleMocksFound() throws Exception {
        // Given
        Object mock1 = mock(TestMockClass1.class);
        Object mock2 = mock(TestMockClass2.class);
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock1);
        mockCandidates.add(mock2);

        // When
        propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testProcessInjection_noMockFound_fieldInitializationFails() throws Exception {
        // Given
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();

        // When
        Whitebox.setInternalState(instance, "injectMocksField1", new Object());
        propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates);

        // Then
        assertFalse(propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates));
        assertNull(Whitebox.getInternalState(instance, "injectMocksField1"));
    }

    @Test
    public void testProcessInjection_noMockFound_constructorFails() throws Exception {
        // Given
        Class<TestMockClass> testMockClass = TestMockClass.class;
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();

        // When
        Whitebox.setInternalState(instance, "injectMocksField1", null);
        propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates);

        // Then
        assertFalse(propertyAndSetterInjection.processInjection(newField("injectMocksField1", TestClass.class), instance, mockCandidates));
        assertNull(Whitebox.getInternalState(instance, "injectMocksField1"));
    }

    @Test
    public void testInjectMockCandidates_noMocksFound() throws Exception {
        // Given
        Class<TestClass> awaitingInjectionClazz = TestClass.class;
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_oneMockFound() throws Exception {
        // Given
        TestMockClass mock = mock(TestMockClass.class);
        Class<TestClass> awaitingInjectionClazz = TestClass.class;
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        // When
        Whitebox.setInternalState(instance, "injectMocksField1", mock);
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(Whitebox.getInternalState(instance, "injectMocksField1") == mock);
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMocksFound() throws Exception {
        // Given
        Set<Object> mocks = new HashSet<>();
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFields = Arrays.asList(newField("injectMocksField2", TestClass.class), newField("injectMocksField1", TestClass.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertFalse(injectionOccurred);
        assertNull(Whitebox.getInternalState(instance, "injectMocksField1"));
        assertNull(Whitebox.getInternalState(instance, "injectMocksField2"));
    }

    @Test
    public void testInjectMockCandidatesOnFields_oneMockFound() throws Exception {
        // Given
        TestMockClass mock = mock(TestMockClass.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock);
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFields = Arrays.asList(newField("injectMocksField2", TestClass.class), newField("injectMocksField1", TestClass.class));

        // When
        Whitebox.setInternalState(instance, "injectMocksField1", mock);
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        assertTrue(injectionOccurred);
        assertTrue(Whitebox.getInternalState(instance, "injectMocksField1") == mock);
        assertNull(Whitebox.getInternalState(instance, "injectMocksField2"));
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidatesOnFields_multipleMocksFound() throws Exception {
        // Given
        TestMockClass1 mock1 = mock(TestMockClass1.class);
        TestMockClass2 mock2 = mock(TestMockClass2.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Object instance = mock(TestClass.class);
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        List<Field> orderedInstanceFields = Arrays.asList(newField("injectMocksField2", TestClass.class), newField("injectMocksField1", TestClass.class));

        // When
        propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        // Then
        // Exception should be thrown
    }

    private Field newField(String name, Class<?> clazz) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
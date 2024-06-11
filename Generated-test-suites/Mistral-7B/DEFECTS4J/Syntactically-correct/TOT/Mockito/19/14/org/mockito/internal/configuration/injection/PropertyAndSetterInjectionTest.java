import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.collections.Sets;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.testng.PowerMockTestCase;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.initFields;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

public class PropertyAndSetterInjectionTest extends PowerMockTestCase {

 private PropertyAndSetterInjection injectionStrategy;
 private Object testObject;
 private Set<Object> mockCandidates;
 private Field injectMocksField;

 @Before
 public void setUp() {
 testObject = new TestClass();
 injectionStrategy = new PropertyAndSetterInjection();
 initMocks(this);
 initFields(this);
 mockStatic(PropertyAndSetterInjection.class);
 mockCandidates = Sets.newMockSafeHashSet();
 mockCandidates.add(mock(ClassWithMockableField.class));
 mockCandidates.add(mock(AnotherClassWithMockableField.class));
 mockCandidates.add(mock(YetAnotherClassWithMockableField.class));
 injectMocksField = Whitebox.getInternalState(testObject, "injectMocksField");
 }

 @Test
 public void testInjectMocks() {
 // Given
 FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(injectMocksField, testObject);

 // When
 boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 verify(mockCandidates.iterator().next()).times(1).thenReturn(report.getFieldInstance());
 verify(mockCandidates.iterator().next()).times(1).thenReturn(report.getFieldClass());
 verify(mockCandidates.iterator().next()).times(1).thenReturn(injectMocksField);

 if (injectionOccurred) {
 List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(testObject.getClass());
 for (Field field : orderedInstanceFields) {
 Object injected = injectionStrategy.mockCandidateFilter.filterCandidate(mockCandidates, field, testObject).thenInject();
 if (injected != null) {
 // verify that the mock was injected using setter or field
 if (field.canSetAccessible()) {
 field.setAccessible(true);
 verify(testObject).setX(injected);
 field.setAccessible(false);
 } else {
 verify(testObject).setX(injected);
 }
 break;
 }
 }
 } else {
 verifyNoInteractions(mockCandidates);
 }
 }

 @Test
 public void testNoInjectMocks() {
 // Given
 mockStatic(PropertyAndSetterInjection.class);
 when(PropertyAndSetterInjection.mockCandidateFilter.filterCandidate(Collections.emptySet(), new Field(), testObject)).thenReturn(null);

 // When
 boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 verifyZeroInteractions(mockCandidates);
 }

 @Test(expected = MockitoException.class)
 public void testInitializationFailed() {
 // Given
 when(PropertyAndSetterInjection.new FieldInitializer(testObject, injectMocksField).initialize()).thenThrow(new MockitoException());

 // When
 injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 // this test will fail with the exception thrown in the initialization method
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFinalOrStaticField() {
 // Given
 Field finalField = new FinalOrStaticField();
 when(PropertyAndSetterInjection.new FieldInitializer(testObject, finalField).initialize()).thenReturn(new FieldInitializationReport(finalField, null, finalField.getClass()));

 // When
 // this test will fail with an IllegalArgumentException since the field is final
 injectionStrategy.processInjection(finalField, testObject, mockCandidates);
 }
}

class TestClass {
 @InjectMocks
 private ClassWithMockableField classWithMockableField;
 private AnotherClassWithMockableField anotherClassWithMockableField;
}

class ClassWithMockableField {
 // ...
}

class AnotherClassWithMockableField {
 // ...
}

class FinalOrStaticField {
 // final or static field
}
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.internal.createdmockclasses.MockCreationSettings.ConstructorType.INSTANCE;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(FinalMockCandidateFilter.class)
public class FinalMockCandidateFilterTest extends PowerMockTestCase {

 private Collection<Object> mocks;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Field field;

 @Before
 public void setup() throws Exception {
 mocks = Arrays.asList(mock(Object.class), mock(Object.class));
 fieldInstance = mock(Object.class);
 mock1 = mock(Object.class, new MockitoJUnitRunner.MockitoJUnitRunner.LazyAnnotatedWith(Strictness.STRICT_STUBS));
 field = PowerMockito.spy(Field.class);
 PowerMockito.mockStatic(Field.class);
 }

 @Test
 public void testFilterCandidate_singleMock() throws Exception {
 final OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

 assertNotNull(filterCandidate);

 PowerMockito.reset(Field.class);
 new FieldSetter(fieldInstance, field).set(mock1);
 verify(mock1).equals(fieldInstance);
 assertFalse(filterCandidate.thenInject());
 }

 @Test
 public void testFilterCandidate_multipleMocks() throws Exception {
 final OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

 assertNotNull(filterCandidate);

 PowerMockito.reset(Field.class);
 new FieldSetter(fieldInstance, field).set(mock1);
 verify(mock1).equals(fieldInstance);
 assertFalse(filterCandidate.thenInject());

 PowerMockito.reset(Field.class);
 new FieldSetter(mock2, field).set(fieldInstance);
 verify(mock2).equals(fieldInstance);
 assertFalse(filterCandidate.thenInject());
 }

 @Test
 public void testFilterCandidate_matchingMock() throws Exception {
 mocks = Arrays.asList(mock1, mock1);
 final OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

 assertNotNull(filterCandidate);

 PowerMockito.reset(Field.class);
 new FieldSetter(mock1, field).set(fieldInstance);
 verify(mock1).equals(fieldInstance);
 assertFalse(filterCandidate.thenInject());

 PowerMockito.reset(Field.class);
 new FieldSetter(mock1, field).set(fieldInstance);
 verify(mock1).equals(fieldInstance);
 assertFalse(filterCandidate.thenInject());

 PowerMockito.reset(Field.class);
 new FieldSetter(mock1, field).set(fieldInstance);
 verify(mock1).equals(fieldInstance);
 assertTrue(filterCandidate.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_exceptionThrown() throws Exception {
 final OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withAnyArguments().thenReturn(field);
 PowerMockito.when(field.set(mock1, Mockito.any())).thenThrow(new Exception());

 assertFalse(filterCandidate.thenInject());
 assertThrows(MockitoException.class, () -> {
 new FieldSetter(fieldInstance, field).set(mock1);
 });
 }

 @Test
 public void testFilterCandidate_noMatchingMock() throws Exception {
 final OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

 assertNotNull(filterCandidate);
 PowerMockito.verifyNoMoreInteractions(mock1, mock2);
 assertFalse(filterCandidate.thenInject());
 }
}
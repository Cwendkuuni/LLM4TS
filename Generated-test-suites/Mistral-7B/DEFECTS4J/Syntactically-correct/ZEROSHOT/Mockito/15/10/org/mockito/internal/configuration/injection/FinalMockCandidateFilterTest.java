import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Object candidate;
 private Field field;

 @Rule
 public TestRule mockitoRule = new MockitoRule(Strictness.STRICT_STUBS);

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 fieldInstance = mock(Object.class);
 field = Mockito.spy(Field.class);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_injectsThroughPropertySetter() {
 candidate = mock1;
 when(field.get(fieldInstance)).thenReturn(candidate);

 OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 ongoingInjecter.thenInject();

 verify(candidate).equals(fieldInstance);
 }

 @Test
 public void testFilterCandidate_singleMock_throwsExceptionIfPropertySetterFails() {
 when(field.get(fieldInstance)).thenReturn(candidate);
 when(field.set(ArgumentMatchers.any(Object.class), ArgumentMatchers.any())).thenThrow(new IllegalAccessException());

 assertThrows(MockitoException.class, () -> filter.filterCandidate(mocks, field, fieldInstance).thenInject());
 }

 @Test
 public void testFilterCandidate_singleMock_returnsOngoingInjecterThatDoesNotInject() {
 candidate = mock2;

 OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(ongoingInjecter.thenInject());
 }

 @Test
 public void testFilterCandidate_multipleMocks_returnsOngoingInjecterThatDoesNotInject() {
 OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(ongoingInjecter.thenInject());
 }
}
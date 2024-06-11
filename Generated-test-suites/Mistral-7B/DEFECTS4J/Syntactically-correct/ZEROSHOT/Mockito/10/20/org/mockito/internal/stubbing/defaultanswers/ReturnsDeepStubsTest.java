import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 @Mock
 private GenericsNest<String> mockGenericsNest;
 @Mock
 private GenericsNest<Number> mockGenericsNestNumber;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testDeepStubs() throws Exception {
 // test with empty map
 when(mockGenericsNest.entrySet()).thenReturn(Collections.emptySet().map(Map.Entry::getValue));
 when(mockGenericsNest.entrySet().iterator().next()).thenReturn(ArgumentMatchers.isNull());
 when(mockGenericsNest.entrySet().iterator().next().getValue()).thenReturn(ArgumentMatchers.isNull());
 when(mockGenericsNest.entrySet().iterator().next().getValue().iterator().next()).thenReturn(ArgumentMatchers.isNull());

 Object deepStub = returnsDeepStubs.answer(new InvocationOnMock(mockGenericsNest, "entrySet", new Class[0][0]));
 verify(mockGenericsNest).entrySet();
 verify(mockGenericsNest).entrySet();
 verify(mockGenericsNest).entrySet();
 verify(mockGenericsNest).entrySet();

 assertSame(mockGenericsNest, deepStub);

 // test with non empty map
 when(mockGenericsNestNumber.entrySet()).thenReturn(new HashSet<>(Arrays.asList(
 new Map.Entry<>(1, 1L), new Map.Entry<>(2, 2L)))
 .stream()
 .map(entry -> new Map.Entry<>(entry.getKey(), new DeepNest(entry.getValue())))
 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

 when(mockGenericsNestNumber.entrySet().iterator().next()).thenReturn(ArgumentMatchers.isInstanceOf(Map.Entry.class));
 when(mockGenericsNestNumber.entrySet().iterator().next().getValue()).thenReturn(ArgumentMatchers.isInstanceOf(DeepNest.class));
 when(mockGenericsNestNumber.entrySet().iterator().next().getValue().iterator().next()).thenReturn(ArgumentMatchers.isNull());

 Object deepStubNumber = returnsDeepStubs.answer(new InvocationOnMock(mockGenericsNestNumber, "entrySet", new Class[0][0]));
 verify(mockGenericsNestNumber).entrySet();
 verify(mockGenericsNestNumber).entrySet();
 verify(mockGenericsNestNumber).entrySet();

 DeepNest deepStubNumberDeepNest = (DeepNest) deepStubNumber;
 assertSame(mockGenericsNestNumber, deepStubNumberDeepNest.getValue());

 Object deepStubNumberDeepNestValue = deepStubNumberDeepNest.iterator().next();
 assertSame(mockGenericsNestNumber, deepStubNumberDeepNestValue);
 }

 // Nested class DeepNest
 private static class DeepNest implements Serializable {
 private final Number value;

 public DeepNest(Number value) {
 this.value = value;
 }

 Number getValue() {
 return value;
 }
 }
}
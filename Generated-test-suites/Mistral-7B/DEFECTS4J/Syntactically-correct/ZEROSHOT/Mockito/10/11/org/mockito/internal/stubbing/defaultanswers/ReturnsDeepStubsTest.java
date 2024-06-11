import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answer.ReturnsDeepStubs;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 @Mock
 private Object mockObject;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testAnswerWithMatchingInvocation() throws Throwable {
 // Given
 when(mockObject).thenReturn(mockObject).answer(returnsDeepStubs);

 // When
 Object deepStub = returnsDeepStubs.answer(ArgumentMatchers.eq(mockObject));

 // Then
 verify(mockObject).answer(ArgumentMatchers.eq(deepStub));
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerWithNonMockableType() throws Throwable {
 // Given
 when(mockObject).thenReturn(new Object()).answer(returnsDeepStubs);

 // When and Then
 // Exception should be thrown as Integer is not mockable type
 }

 @Test
 public void testNewDeepStubMock() throws Throwable {
 // Given
 Class<Integer> integerType = Integer.class;
 GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.inferFrom(integerType);
 Object deepStub = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

 // When and Then
 // Verify that deepStub is a mock of Integer type
 verify(deepStub).toString();
 }

 @Test
 public void testRecordDeepStubAnswer() throws Throwable {
 // Given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = new InvocationContainerImpl();
 Object mock = Mockito.mock(Object.class);

 when(mock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });

 container.addAnswer(new ReturnsDeepStubs().deepStub(invocation, returnTypeGenericMetadata), false);
 container.setInvocationForStubbing(invocation);

 // When
 Object deepStubAnswer = returnsDeepStubs.recordDeepStubAnswer(mock, container);

 // Then
 assertEquals("answer", deepStubAnswer);
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Throwable {
 // Given
 Class<Map> mapType = Map.class;
 GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.inferFrom(mapType);
 ReturnsDeepStubs answer = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

 // When
 Object deepStub = answer.answer(ArgumentMatchers.eq(mapType));

 // Then
 // Verify that deepStub is a mock of Map interface
 verify(deepStub).clear();
 }
}
import org.junit.Test;
import static org.mockito.Mockito.*;;

public class ReturnsDeepStubsTest {
     @Test
     public void testAnswer() {
         Object mock = mock(ReturnsDeepStubs.class);
         when(mock.answer(any(InvocationOnMock.class))).thenReturn(new Object());
         Object actual = mock.answer(mock(InvocationOnMock.class));
         assertEquals(new Object(), actual);
     }

     @Test
     public void testGetMock() throws Exception {
         Object mock = mock(ReturnsDeepStubs.class);
         Object deepStub = mock(Object.class, new ReturnsDeepStubs());
         doReturn(deepStub).when(mock).getMock(any(InvocationOnMock.class), any(GenericMetadataSupport.class));
         Object actual = mock.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
         assertEquals(deepStub, actual);
     }

     @Test(expected = IllegalArgumentException.class)
     public void testGetMockNotMockable() throws Exception {
         Object mock = mock(ReturnsDeepStubs.class);
         doThrow(new IllegalArgumentException("Not mockable type")).when(mock).getMock(any(InvocationOnMock.class), any(GenericMetadataSupport.class));
         mock.answer(mock(InvocationOnMock.class));
         mock.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
     }

     @Test
     public void testRecordDeepStubMock() throws Exception {
         Object mock = mock(ReturnsDeepStubs.class);
         Object deepStub = mock(Object.class, new ReturnsDeepStubs());
         doReturn(deepStub).when(mock).recordDeepStubMock(any(Object.class), any(InvocationContainerImpl.class));
         Object actual = mock.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
         assertEquals(deepStub, actual);
     }

     @Test
     public void testCreateNewDeepStubMock() throws Exception {
         Object mock = mock(ReturnsDeepStubs.class);
         Object deepStub = mock(Object.class, new ReturnsDeepStubs());
         doReturn(deepStub).when(mock).createNewDeepStubMock(any(GenericMetadataSupport.class));
         Object actual = mock.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
         assertEquals(deepStub, actual);
     }

     @Test
     public void testGetInvocationForStubbing() throws Exception {
         Object mock = mock(ReturnsDeepStubs.class);
         Object deepStub = mock(Object.class, new ReturnsDeepStubs());
         doReturn(deepStub).when(mock).getMock(any(InvocationOnMock.class), any(GenericMetadataSupport.class));
         InvocationOnMock invocation = mock(InvocationOnMock.class);
         when(mock.getInvocationForStubbing()).thenReturn(invocation);
         Object actual = mock.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
         assertEquals(deepStub, actual);
     }

     @Test
     public void testAnswerWithException() throws Exception {
         Object mock = mock(ReturnsDeepStubs.class);
         when(mock.answer(any(InvocationOnMock.class))).thenThrow(new RuntimeException());
         try {
             mock.answer(mock(InvocationOnMock.class));
         } catch (RuntimeException e) {
             // expected
         }
     }
}
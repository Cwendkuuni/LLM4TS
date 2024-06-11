import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.plugins.MockMaker;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker mockMaker;
 private MockHandler mockHandler;

 @Before
 public void setup() {
 mockMaker = new ByteBuddyMockMaker();
 mockHandler = mock(MockHandler.class);
 }

 @Test
 public void testCreateMock() {
 MockCreationSettings settings = new MockCreationSettings<Object>(Object.class, new GlobalConfiguration());
 settings.setSerializableMode(SerializableMode.SERIAL); // Set serializable mode for testing
 Object mockObject = mockMaker.createMock(settings, mockHandler);

 // Verify that the mock was created and returned an instance of the mocked type
 verify(mockHandler).bind(Mockito.any());
 verify(mockHandler).getMock();

 ArgumentCaptor<Object> mockCaptor = ArgumentCaptor.forClass(Object.class);
 MockMethodInterceptor mockInterceptor = Mockito.capture(MockMethodInterceptor.class).inCallsTo(mockHandler);
 Object returnedMock = mockInterceptor.getValue().getMockitoInterceptor().getMock();

 // Test that the returned mock is an instance of the mocked type
 verify(mockHandler).bind(mockCaptor.capture(Object.class));
 verify(mockCaptor.getValue()).equals(returnedMock);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockSerializationException() {
 MockCreationSettings settings = new MockCreationSettings<Object>(Object.class, new GlobalConfiguration());
 settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS); // Set serializable mode for testing exception

 try {
 mockMaker.createMock(settings, mockHandler);
 } catch (MockitoException e) {
 // Test that the expected exception is thrown
 verify(mockHandler).bind(Mockito.any());
 throw e;
 }
 }
}
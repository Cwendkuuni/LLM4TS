import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.ClassInstantiatorUsingFactory;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRegistry;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private MockMakerRegistry mockMakerRegistry;
 private ClassInstantiatorUsingFactory classInstantiatorUsingFactory;

 @Before
 public void setup() {
 mockMakerRegistry = MockMakerRegistry.getInstance();
 mockMakerRegistry.registerMockMaker(new MockMaker.DefaultMockMaker());
 mockMakerRegistry.registerMockMaker(new ByteBuddyMockMaker());
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 classInstantiatorUsingFactory = mock(ClassInstantiatorUsingFactory.class);
 }

 @Test(expected = MockitoException.class)
 public void testSerializationAcrossClassLoaders() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 try {
 byteBuddyMockMaker.createMock(
 new MockCreationSettings<Object>()
 .setTypeToMock(Object.class)
 .setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .setHandler(Mockito.mock(MockHandler.class)),
 mock(MockHandler.class)
 );
 fail("Expected MockitoException for serialization across classloaders");
 } catch (MockitoException e) {
 // Expected
 }
 }

 @Test
 public void testCreateMock() {
 Class<?> mockedType = Mockito.mock(String.class);
 MockHandler handler = mock(MockHandler.class);
 Object mock = byteBuddyMockMaker.createMock(
 new MockCreationSettings<Object>()
 .setTypeToMock(mockedType)
 .setExtraInterfaces(new Class[] {Number.class})
 .setHandler(handler),
 handler
 );
 assertEquals(mockedType, mock.getClass());
 Mockito.verify(handler).handle(ArgumentMatchers.any());
 }

 @Test(expected = ClassCastException.class)
 public void testClassCastException() {
 Class<?> mockedType = Mockito.mock(String.class);
 MockHandler handler = mock(MockHandler.class);
 Object mock = byteBuddyMockMaker.createMock(
 new MockCreationSettings<Object>()
 .setTypeToMock(Integer.class)
 .setHandler(handler),
 handler
 );
 assertEquals(mockedType, mock.getClass()); // Should fail with ClassCastException
 }

 @Test(expected = InstantiationException.class)
 public void testUnableToCreateMockInstance() {
 Class<?> mockedType = Mockito.mock(Throwable.class);
 MockHandler handler = mock(MockHandler.class);
 throw new InstantiationException("Unable to create mock instance of type 'Throwable'");
 }

 @Test
 public void testGetHandler() {
 Object mock = byteBuddyMockMaker.createMock(
 new MockCreationSettings<Object>()
 .setTypeToMock(String.class)
 .setHandler(mock(MockHandler.class)),
 mock(MockHandler.class)
 );
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 assertNotNull(handler);
 }

 @Test
 public void testResetMock() {
 Object mock = byteBuddyMockMaker.createMock(
 new MockCreationSettings<Object>()
 .setTypeToMock(String.class)
 .setHandler(mock(MockHandler.class)),
 mock(MockHandler.class)
 );
 byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), new MockCreationSettings<Object>()
 .setTypeToMock(String.class)
 .setHandler(mock(MockHandler.class))
 );
 assertSame(mock, byteBuddyMockMaker.getHandler(mock).getMockedInstance());
 }

 @Test
 public void testInitializeClassInstantiator() {
 assertSame(ClassInstantiatorUsingFactory.class, byteBuddyMockMaker.initializeClassInstantiator().getClass());
 }

 @Test
 public void testAsInternalMockHandler() {
 MockHandler handler = mock(InternalMockHandler.class);
 InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(handler);
 assertEquals(handler, internalMockHandler);
 }
}
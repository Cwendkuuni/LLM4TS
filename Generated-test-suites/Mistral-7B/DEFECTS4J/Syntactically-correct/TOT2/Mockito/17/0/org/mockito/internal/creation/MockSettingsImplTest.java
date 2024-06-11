import org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;
import java.io.Serializable;

public class MockSettingsImplTest {

   @Test
   public void testSerializable() {
      MockSettingsImpl settings = new MockSettingsImpl().serializable();
      //assert serializability
   }

   @Test
   public void testExtraInterfaces() {
      MockSettingsImpl settings = new MockSettingsImpl();
      settings.extraInterfaces(Serializable.class);
      //assert extra interfaces
   }

   @Test(expected = Reporter.ExtraInterfacesRequiresAtLeastOneInterface.class)
   public void testNullExtraInterfaces() {
      MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(null);
   }

   @Test(expected = Reporter.ExtraInterfacesAcceptsOnlyInterfaces.class)
   public void testNonInterfaceExtraInterfaces() {
      MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(String.class);
   }

   @Test(expected = Reporter.ExtraInterfacesRequiresAtLeastOneInterface.class)
   public void testMultipleNullExtraInterfaces() {
      MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(null, Serializable.class);
   }

   @Test(expected = NegativeArraySizeException.class)
   public void testNegativeArraySizeException() {
      MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(-1);
   }
}
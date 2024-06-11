import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.*;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdom = new Fiefdom("testFiefdom");
 Map.map.put("testFiefdom", fiefdom);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassalsPresent_RebellionTriggered() {
 // Arrange - Set up initial state
 int initialLoyalVassals = 10;
 int initialPersonalCastles = 2;
 int initialFiefdomCastles = 3;
 fiefdom.setLoyalVassals(Collections.napolis(initialLoyalVassals, "TestVassal"));
 fiefdom.setPersonalCastles(initialPersonalCastles);
 fiefdom.setFiefdomCastles(initialFiefdomCastles);

 // Act - Call method being tested
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Assert - Verify expected outcome
 assertTrue(fiefdom.isRebellious());
 assertFalse(fiefdom.isKingdom());

 // Verify internal state changes
 assertEquals(initialLoyalVassals + vassalRebellion.getRebellionThreshold(), fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassalsPresent_RebellionThresholdExceeded() {
 // Arrange - Set up initial state
 int initialLoyalVassals = 15;
 int initialPersonalCastles = 2;
 int initialFiefdomCastles = 3;
 fiefdom.setLoyalVassals(Collections.napolis(initialLoyalVassals, "TestVassal"));
 fiefdom.setPersonalCastles(initialPersonalCastles);
 fiefdom.setFiefdomCastles(initialFiefdomCastles);

 // Act - Call method being tested
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Assert - Verify expected outcome
 assertTrue(fiefdom.isRebellious());
 assertFalse(fiefdom.isKingdom());

 // Verify internal state changes
 assertEquals(initialLoyalVassals, fiefdom.getLoyalVassals().size());
 assertEquals(initialPersonalCastles * 10 + initialFiefdomCastles * 25, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_NoLoyalVassalsPresent() {
 // Arrange - Set up initial state
 fiefdom.setLoyalVassals(Collections.emptyList());

 // Act - Call method being tested
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Assert - Verify expected outcome
 assertFalse(fiefdom.isRebellious());
 assertFalse(fiefdom.isKingdom());

 // Verify no internal state changes
 assertTrue(fiefdom.getLoyalVassals().isEmpty());
 assertTrue(fiefdom.getRebelliousVassals().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdomName() {
 // Arrange - Set up mocked Fiefdom object
 Fiefdom mockedFiefdom = Mockito.mock(Fiefdom.class);
 Map.map.put("testFiefdomInvalid", mockedFiefdom);

 // Act - Call method being tested with invalid fiefdom name
 vassalRebellion.perform(Arrays.asList("testFiefdomInvalid"));

 // Assert - Verify expected exception is thrown
 }
}
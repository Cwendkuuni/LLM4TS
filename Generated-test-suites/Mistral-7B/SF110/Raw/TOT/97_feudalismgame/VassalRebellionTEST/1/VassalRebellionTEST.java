package src.test;import org.junit.Before; import org.junit.Test; import static org.mockito.Mockito.*; import src.Fiefdoms; import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before public void setup() { fiefdomMock = mock(Fiefdoms.class); vassalRebellion = new VassalRebellion(); vassalRebellion.setFiefdomObj(fiefdomMock); }

 @Test public void testPerform_LoyalVassalsAndCastles_RebellionOccurs() { // Given when the fiefdom has loyal vassals and castles when the perform method is called then the vassals become rebellious

 // Set up mocks for getPersonalCastles, getFiefdomCastles, getLoyalVassals, and getValue methods return values which trigger a rebellion
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(2);

 // Call perform method
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // Assert vassals are rebellious
 verify(fiefdomMock, times(1)).setRebelliousVassals(anyInt(), eq(true));
 }

 @Test public void testPerform_NoLoyalVassals_RollEventIsCalled() { // Given when the fiefdom has no loyal vassals when the perform method is called then a roll event is called

 // Set up mocks for getPersonalCastles, getFiefdomCastles, and getLoyalVassals methods return empty list
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // Call perform method
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // Assert roll event is called
 verify(fiefdomMock, times(1)).new RollEvent(eq("TestFiefdom"));
 }

 @Test(expected = IllegalArgumentException.class) public void testPerform_InvalidFiefdomName() { // Given when an invalid fiefdom name is passed to perform method then an IllegalArgumentException is thrown

 // Call perform method with invalid fiefdom name
 vassalRebellion.perform(Arrays.asList("InvalidFiefdomName"));

 // Assert an IllegalArgumentException is thrown
 }

 @Test public void testPerform_InsufficientVassals_NoRebellionOccurs() { // Given when the number of loyal vassals is less than the calculated threshold when the perform method is called then no vassals become rebellious

 // Set up mocks for getPersonalCastles, getFiefdomCastles, getLoyalVassals, and getValue methods return values which result in insufficient number of vassals for rebellion
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(1);
 when(fiefdomMock.getValue()).thenReturn(1);

 // Call perform method
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // Assert no vassals become rebellious
 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), eq(true));
 }
}
package src.test;import org.junit.Before;import org.junit.Test;import static org.mockito.Mockito.*;import src.Fiefdoms;import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomMock = mock(Fiefdoms.class);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // given
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList(new String[]{"Vassal1", "Vassal2", "Vassal3"}));
 when(Map.map.get("TestFiefdom")).thenReturn(fiefdomMock);

 // when
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // then
 verify(fiefdomMock, times(1)).setRebelliousVassals(3, true);
 verify(fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerformWithLoyalVassalsButNoCastles() {
 // given
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList(new String[]{"Vassal1", "Vassal2", "Vassal3"}));
 when(Map.map.get("TestFiefdom")).thenReturn(fiefdomMock);

 // when
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // then
 verify(fiefdomMock, times(1)).setRebelliousVassals(3, true);
 verify(fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // given
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());
 when(Map.map.get("TestFiefdom")).thenReturn(fiefdomMock);

 // when
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // then
 verify(fiefdomMock, times(1)).new RollEvent(eq("TestFiefdom"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyCollection() {
 // given

 // when & then
 vassalRebellion.perform(Collections.emptyList());
 }
}
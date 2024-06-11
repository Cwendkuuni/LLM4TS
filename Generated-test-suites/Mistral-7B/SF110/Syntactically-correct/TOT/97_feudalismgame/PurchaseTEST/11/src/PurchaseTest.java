package src.test;import org.junit.Before; import org.junit.Test; import org.mockito.Mockito;import src.Fiefdoms; import src.GameAutoActions; import src.CurrentPlayers; import src.Knight; import src.Map;

public class PurchaseTest {

 private Purchase purchase;
 private final String buyerName1 = "Buyer1";
 private final String buyerName2 = "Buyer2";
 private final String fiefdomName = "TestFiefdom";
 private final String invalidFiefdomName = "InvalidFiefdom";
 private final int vassalQty = 3;
 private final int castleFiefdomQty = 2;
 private final int castlePersonalQty = 1;
 private final int mercenaryQty = 5;
 private final Knight buyer1;
 private final Knight buyer2;
 private final Knight knight;
 private final Fiefdoms fiefdom;
 private final CurrentPlayers currentPlayers;
 private final Map map;
 private final GameAutoActions gameAutoActions;

 @Before
 public void setUp() {
 buyer1 = Mockito.spy(new Knight(buyerName1, "knight", 1000));
 buyer2 = Mockito.spy(new Knight(buyerName2, "knight", 1500));
 knight = Mockito.spy(new Knight("KnightTest", "knight", 0));
 fiefdom = Mockito.spy(new Fiefdoms(Map.map));
 currentPlayers = Mockito.spy(new CurrentPlayers());
 map = Mockito.spy(new Map());
 gameAutoActions = Mockito.spy(new GameAutoActions());

 purchase = new Purchase();
 purchase.setMap(map);
 purchase.setCurrentPlayers(currentPlayers);
 purchase.setGameAutoActions(gameAutoActions);
 }

 @Test
 public void testVassal() {
 final Iterator<Object> args = Arrays.asList(vassalQty, buyerName1, fiefdomName).iterator();
 purchase.perform(args);
 Mockito.verify(currentPlayers, Mockito.times(1)).setTotalMoney(buyer1, vassalQty, false);
 Mockito.verify(fiefdom).setLoyalVassals(vasselQty, true);
 }

 @Test
 public void testVassalInsufficientMoney() {
 final Iterator<Object> args = Arrays.asList(vassalQty, buyerName1, fiefdomName).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 Mockito.when(fiefdom.getOwner()).thenReturn(null);
 purchase.perform(args);
 Mockito.verifyNoInteractions(currentPlayers);
 Mockito.verifyNoInteractions(fiefdom);
 }

 @Test
 public void testVassalFiefdomNotBelongsToTheKingdom() {
 final Iterator<Object> args = Arrays.asList(vassalQty, buyerName1, invalidFiefdomName).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 Mockito.when(map.get(invalidFiefdomName)).thenReturn(new Fiefdoms(null));
 purchase.perform(args);
 Mockito.verifyNoInteractions(currentPlayers);
 Mockito.verifyNoInteractions(fiefdom);
 }

 @Test
 public void testCastleFiefdom() {
 final Iterator<Object> args = Arrays.asList(castleFiefdomQty, buyerName1, fiefdomName).iterator();
 purchase.perform(args);
 Mockito.verify(currentPlayers, Mockito.times(1)).setTotalMoney(buyer1, castleFiefdomQty * 25, false);
 Mockito.verify(fiefdom).buyFiefdomCastles(castleFiefdomQty);
 }

 @Test
 public void testCastleFiefdomInsufficientMoney() {
 final Iterator<Object> args = Arrays.asList(castleFiefdomQty, buyerName1, fiefdomName).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 purchase.perform(args);
 Mockito.verifyNoInteractions(currentPlayers);
 Mockito.verifyNoInteractions(fiefdom);
 }

 @Test
 public void testCastleFiefdomFiefdomNotBelongsToTheKingdom() {
 final Iterator<Object> args = Arrays.asList(castleFiefdomQty, buyerName1, invalidFiefdomName).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 Mockito.when(map.get(invalidFiefdomName)).thenReturn(new Fiefdoms(null));
 purchase.perform(args);
 Mockito.verifyNoInteractions(currentPlayers);
 Mockito.verifyNoInteractions(fiefdom);
 }

 @Test
 public void testCastlePersonal() {
 final Iterator<Object> args = Arrays.asList(castlePersonalQty, buyerName1, fiefdomName).iterator();
 purchase.perform(args);
 Mockito.verify(currentPlayers, Mockito.times(1)).setTotalMoney(buyer1, castlePersonalQty * 10, false);
 Mockito.verify(fiefdom).buyPersonalCastles(castlePersonalQty);
 }

 @Test
 public void testCastlePersonalInsufficientMoney() {
 final Iterator<Object> args = Arrays.asList(castlePersonalQty, buyerName1, fiefdomName).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 purchase.perform(args);
 Mockito.verifyNoInteractions(currentPlayers);
 Mockito.verifyNoInteractions(fiefdom);
 }

 @Test
 public void testCastlePersonalFiefdomNotBelongsToTheKingdom() {
 final Iterator<Object> args = Arrays.asList(castlePersonalQty, buyerName1, invalidFiefdomName).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 Mockito.when(map.get(invalidFiefdomName)).thenReturn(new Fiefdoms(null));
 purchase.perform(args);
 Mockito.verifyNoInteractions(currentPlayers);
 Mockito.verifyNoInteractions(fiefdom);
 }

 @Test
 public void testMercenary() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, buyerName1, buyerName2).iterator();
 purchase.perform(args);
 Mockito.verify(knight, Mockito.times(1)).addMercenaries(mercenaryQty);
 }

 @Test
 public void testMercenaryBuyerIsTheMercenary() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, buyerName1, buyerName1).iterator();
 purchase.perform(args);
 Mockito.verifyNoInteractions(knight);
 }

 @Test
 public void testMercenaryInsufficientMoney() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, buyerName1, buyerName2).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(buyer1);
 purchase.perform(args);
 Mockito.verifyNoInteractions(knight);
 }

 @Test
 public void testMercenaryBuyerIsTheKing() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, "king", buyerName2).iterator();
 purchase.perform(args);
 Mockito.verify(knight, Mockito.times(1)).addMercenaries(mercenaryQty);
 }

 @Test
 public void testMercenaryFighterAlreadyHasAMercenary() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, buyerName1, buyerName2).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(new Knight(buyerName1, "knight", 500, 10));
 purchase.perform(args);
 Mockito.verifyNoInteractions(knight);
 }

 @Test
 public void testMercenaryFighterIsTheKing() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, buyerName1, buyerName2).iterator();
 Mockito.when(currentPlayers.get(buyerName1)).thenReturn(new Knight(buyerName1, "king", 100000));
 purchase.perform(args);
 Mockito.verifyNoInteractions(knight);
 }

 @Test
 public void testMercenaryInvalidBuyer() {
 final Iterator<Object> args = Arrays.asList(mercenaryQty, "InvalidBuyer", buyerName2).iterator();
 purchase.perform(args);
 Mockito.verifyNoInteractions(knight);
 }
}
package src.test; import org.junit.Test; import static org.mockito.Mockito.*; import static org.junit.Assert.*; import src.Actions; import src.Fiefdoms; import src.GameAutoActions; import src.Knight; import src.Map; import src.CurrentPlayers;

public class PurchaseTest {

 private Fiefdoms map;
 private GameAutoActions gameAutoActions;
 private CurrentPlayers currentPlayers;
 private Map<String, Fiefdoms> mockMap;
 private Knight buyer1, buyer2, vassalKnight, castleKnight, personalCastleKnight, mercenaryKnight;

 @Before
 public void setUp() {
 map = new Fiefdoms();
 gameAutoActions = new GameAutoActions();
 currentPlayers = new CurrentPlayers();
 mockMap = new HashMap<>();

 buyer1 = new Knight("buyer1", "knight", 1000);
 buyer2 = new Knight("buyer2", "knight", 2000);
 vassalKnight = new Knight("vassalKnight", "knight", 500);
 castleKnight = new Knight("castleKnight", "knight", 1500);
 personalCastleKnight = new Knight("personalCastleKnight", "knight", 1000);
 mercenaryKnight = new Knight("mercenaryKnight", "knight", 500);

 currentPlayers.put("buyer1", buyer1);
 currentPlayers.put("buyer2", buyer2);

 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null));

 mockMap.put("fiefdom1", map.get("fiefdom1"));
 mockMap.put("fiefdom2", map.get("fiefdom2"));

 when(Map.map).thenReturn(mockMap);
 }

 @Test
 public void testVassal() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(6, map.get("fiefdom1").getLoyalVassals());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVassalInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(6, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(5, map.get("fiefdom1").getLoyalVassals());

 final Iterator<Object> arg2 = Arrays.asList(2, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg2));
 assertEquals(5, map.get("fiefdom1").getLoyalVassals());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVassalNotBuyersFiefdom() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer2", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(5, map.get("fiefdom1").getLoyalVassals());
 }

 @Test(expected = IllegalStateException.class)
 public void testVassalFiefdomNotInKingdom() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer1", "fiefdom3").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(0, map.get("fiefdom3").getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 final Iterator<Object> arg = Arrays.asList(3, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(3, map.get("fiefdom1").getCastleCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastleFiefdomInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(7, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(3, map.get("fiefdom1").getCastleCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastleFiefdomNotBuyersFiefdom() {
 final Iterator<Object> arg = Arrays.asList(3, "buyer2", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(3, map.get("fiefdom1").getCastleCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testCastleFiefdomFiefdomNotInKingdom() {
 final Iterator<Object> arg = Arrays.asList(3, "buyer1", "fiefdom3").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(0, map.get("fiefdom3").getCastleCount());
 }

 @Test
 public void testCastlePersonal() {
 final Iterator<Object> arg = Arrays.asList(2, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(2, gameAutoActions.getPersonalCastleCount(buyer1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastlePersonalInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer1", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(2, gameAutoActions.getPersonalCastleCount(buyer1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastlePersonalNotBuyersFiefdom() {
 final Iterator<Object> arg = Arrays.asList(2, "buyer2", "fiefdom1").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(2, gameAutoActions.getPersonalCastleCount(buyer1));
 }

 @Test(expected = IllegalStateException.class)
 public void testCastlePersonalFiefdomNotInKingdom() {
 final Iterator<Object> arg = Arrays.asList(2, "buyer1", "fiefdom3").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(0, gameAutoActions.getPersonalCastleCount(buyer1));
 }

 @Test
 public void testMercenary() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer1", "vassalKnight").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(6, vassalKnight.getMercenaries());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(3, "buyer1", "vassalKnight").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(5, vassalKnight.getMercenaries());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryNotBuyer() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer2", "vassalKnight").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(5, vassalKnight.getMercenaries());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryNotKnight() {
 final Iterator<Object> arg = Arrays.asList(5, "buyer1", "castleKnight").iterator();
 new Purchase().perform(Arrays.asList(arg));
 assertEquals(5, castleKnight.getMercenaries());
 }
}
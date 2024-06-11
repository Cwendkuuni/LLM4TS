package src.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.*;

import src.*;
import src.model.*;

public class PurchaseTest {

 private Purchase purchase;
 private Map map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 final Fiefdoms fiefdom = new Fiefdoms("Albion", "Albion King", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
 map = Mockito.spy(new Map());
 currentPlayers = Mockito.spy(new CurrentPlayers());
 purchase = new Purchase();
 when(map.get(Arguments.isA(String.class))).thenReturn(fiefdom);
 when(currentPlayers.get(Arguments.isA(String.class))).thenAnswer(i -> {
 final String name = i.getArgument(0);
 if ("Albion King".equals(name)) {
 return new Knight("Albion King", "king", 10000, new ArrayList<>());
 }
 return null;
 });
 }

 @Test
 public void testVassal() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", "Albion").iterator();
 purchase.vassal(arg);
 assertEquals(3, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(5, "Albion King", "Albion").iterator();
 purchase.vassal(arg);
 assertEquals(3, fiefdom.getLoyalVassals());
 verifyNoMoreInteractions(purchase);
 }

 @Test
 public void testVassalForUnownedFiefdom() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", "UnownedFiefdom").iterator();
 purchase.vassal(arg);
 verifyNoInteractions(fiefdom);
 }

 @Test
 public void testCastleFiefdom() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", "Albion").iterator();
 purchase.castleFiefdom(arg);
 assertEquals(3, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomWithInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(5, "Albion King", "Albion").iterator();
 purchase.castleFiefdom(arg);
 assertEquals(3, fiefdom.getCastles().size());
 verifyNoMoreInteractions(purchase);
 }

 @Test
 public void testCastleFiefdomForUnownedFiefdom() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", "UnownedFiefdom").iterator();
 purchase.castleFiefdom(arg);
 verifyNoInteractions(fiefdom);
 }

 @Test
 public void testCastlePersonal() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", "Albion").iterator();
 purchase.castlePersonal(arg);
 assertEquals(3, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalWithInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(5, "Albion King", "Albion").iterator();
 purchase.castlePersonal(arg);
 assertEquals(3, fiefdom.getPersonalCastles().size());
 verifyNoMoreInteractions(purchase);
 }

 @Test
 public void testCastlePersonalForUnownedFiefdom() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", "UnownedFiefdom").iterator();
 purchase.castlePersonal(arg);
 verifyNoInteractions(fiefdom);
 }

 @Test
 public void testMercenary() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", new Knight("testKnight", "knight", 1000, new ArrayList<>())).iterator();
 purchase.mercenary(arg);
 assertEquals(3, ((Knight)arg.next()).getMercenaries().size());
 }

 @Test
 public void testMercenaryWithInsufficientFunds() {
 final Iterator<Object> arg = Arrays.asList(5, "Albion King", new Knight("testKnight", "knight", 1000, new ArrayList<>())).iterator();
 purchase.mercenary(arg);
 assertEquals(3, ((Knight)arg.next()).getMercenaries().size());
 verifyNoMoreInteractions(purchase);
 }

 @Test
 public void testMercenaryForDifferentPlayer() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King2", new Knight("testKnight", "knight", 1000, new ArrayList<>())).iterator();purchase.mercenary(arg);verifyNoInteractions(purchase); }

 @Test
 public void testMercenaryForDifferentKnight() {
 final Iterator<Object> arg = Arrays.asList(3, "Albion King", new Knight("AnotherTestKnight", "knight", 1000, new ArrayList<>())).iterator();
 purchase.mercenary(arg);
 verifyNoInteractions(purchase);
 }
}
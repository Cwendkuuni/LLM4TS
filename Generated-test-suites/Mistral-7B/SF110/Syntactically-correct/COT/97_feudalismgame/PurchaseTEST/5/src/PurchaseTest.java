package src.test.java;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

import java.util.*;
import java.lang.reflect.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom;
 private Knight buyer, knight;
 private CurrentPlayers currentPlayers;
 private Map map;
 private GameAutoActions gameAutoActions;

 @Before
 public void setup() {
 // Initialize objects and setup test data
 purchase = new Purchase();
 fiefdom = new Fiefdoms();
 buyer = new Knight("John", "knight", 100);
 knight = new Knight("Mark", "knight", 150);
 currentPlayers = new CurrentPlayers();
 map = new Map();
 gameAutoActions = new GameAutoActions();
 currentPlayers.addPlayer(buyer);
 currentPlayers.addPlayer(knight);
 map.addFiefdom(new Fiefdom("TestFiefdom", "TestOwner", null));
 }

 @Test
 public void testPerform() {
 // Test empty argument list
 Collection args = Collections.emptyList();
 purchase.perform(args);
 // Test valid argument list
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 }, fiefdom.getIterator());
 purchase.perform(argList);
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 // Test SecurityException
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 throw new SecurityException("Test SecurityException");
 }
 }, fiefdom.getIterator());
 purchase.perform(argList);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 // Test NoSuchMethodException
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new String("TestString");
 }
 }, fiefdom.getIterator());
 purchase.perform(argList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithIllegalArgumentException() {
 // Test IllegalArgumentException
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(-1);
 }
 }, fiefdom.getIterator());
 purchase.perform(argList);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformWithIllegalAccessException() {
 // Test IllegalAccessException
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 try {
 Field field = Purchase.class.getDeclaredField("args");
 field.setAccessible(true);
 field.set(purchase, new Object[]{"TestString", fiefdom.getIterator()});
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 return null;
 }
 }, null);
 purchase.perform(argList);
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformWithInvocationTargetException() {
 // Test InvocationTargetException
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 throw new InvocationTargetException(new RuntimeException("TestException"));
 }
 }, fiefdom.getIterator());
 purchase.perform(argList);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformWithClassNotFoundException() {
 // Test ClassNotFoundException
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 throw new ClassNotFoundException("TestClassNotFoundException");
 }
 }, fiefdom.getIterator());
 purchase.perform(argList);
 }

 @Test
 public void testVassal() {
 // Test normal scenario
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "John");
 purchase.vassal(argList);
 assertEquals(5, buyer.getTotalVassals());
 // Test insufficient funds
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, currentPlayers.getIterator(), "John");
 purchase.vassal(argList);
 assertNotEquals(5, buyer.getTotalVassals());
 // Test fiefdom not belonging to the kingdom
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Fiefdom("TestFiefdom2", "TestOwner2", null);
 }
 }, currentPlayers.getIterator(), "John");
 purchase.vassal(argList);
 assertNotEquals(5, buyer.getTotalVassals());
 // Test buying a vassal for someone else's fiefdom
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "Mark");
 purchase.vassal(argList);
 assertNotEquals(5, buyer.getTotalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 // Test normal scenario
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "John", "TestFiefdom");
 purchase.castleFiefdom(argList);
 assertEquals(5, fiefdom.getCastles().size());
 // Test insufficient funds
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, currentPlayers.getIterator(), "John", "TestFiefdom");
 purchase.castleFiefdom(argList);
 assertNotEquals(5, fiefdom.getCastles().size());
 // Test fiefdom not belonging to the kingdom
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Fiefdom("TestFiefdom2", "TestOwner2", null);
 }
 }, currentPlayers.getIterator(), "John");
 purchase.castleFiefdom(argList);
 assertNotEquals(5, fiefdom.getCastles().size());
 // Test castle already exists
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "John", "TestFiefdom");
 purchase.castleFiefdom(argList);
 purchase.castleFiefdom(argList);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 // Test normal scenario
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "John", "TestFiefdom");
 purchase.castlePersonal(argList);
 assertEquals(5, buyer.getPersonalCastles().size());
 // Test insufficient funds
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, currentPlayers.getIterator(), "John", "TestFiefdom");
 purchase.castlePersonal(argList);
 assertNotEquals(5, buyer.getPersonalCastles().size());
 // Test fiefdom not belonging to the kingdom
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Fiefdom("TestFiefdom2", "TestOwner2", null);
 }
 }, currentPlayers.getIterator(), "John");
 purchase.castlePersonal(argList);
 assertNotEquals(5, buyer.getPersonalCastles().size());
 // Test buying castle for someone else
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "Mark", "TestFiefdom");
 purchase.castlePersonal(argList);
 assertNotEquals(5, buyer.getPersonalCastles().size());
 }

 @Test
 public void testMercenary() {
 // Test normal scenario
 List<Object> argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, currentPlayers.getIterator(), "Mark");
 purchase.mercenary(argList);
 assertEquals(5, knight.getTotalMercenaries());
 // Test insufficient funds
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, currentPlayers.getIterator(), "Mark");
 purchase.mercenary(argList);
 assertNotEquals(5, knight.getTotalMercenaries().size());
 // Test knights are the same person
 argList = Arrays.asList(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return buyer;
 }
 }, currentPlayers.getIterator(), "John");
 purchase.mercenary(argList);
 assertEquals(5, knight.getTotalMercenaries().size());
 }

}
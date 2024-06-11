package net.sf.lavalamp.device;
 import static org.junit.Assert.*;
 import org.junit.Test;

 public class TimeOfDayTest
 {
     TimeOfDay timeOfDay1;
     TimeOfDay timeOfDay2;
     TimeOfDay timeOfDay3;
     TimeOfDay timeOfDay4;
     TimeOfDay timeOfDay5;
     TimeOfDay timeOfDayInvalid;

     @Test
     public void testConstructorWithHoursAndMinutes()
     {
         timeOfDay1 = new TimeOfDay(12, 30);
         assertEquals("12:30", timeOfDay1.toString());
     }

     @Test(expected = IllegalArgumentException.class)
     public void testConstructorWithInvalidHours()
     {
         try
         {
             timeOfDay1 = new TimeOfDay(-1, 30);
         }
         catch (IllegalArgumentException e)
         {
             fail("Expected IllegalArgumentException, but got: " + e.getMessage());
         }
         // This exception should be thrown, so we don't write any assertions here
     }

     @Test(expected = IllegalArgumentException.class)
     public void testConstructorWithInvalidMinutes()
     {
         try
         {
             timeOfDay1 = new TimeOfDay(12, 61);
         }
         catch (IllegalArgumentException e)
         {
             fail("Expected IllegalArgumentException, but got: " + e.getMessage());
         }
         // This exception should be thrown, so we don't write any assertions here
     }

     @Test
     public void testConstructorWithTimeString()
     {
         timeOfDay2 = new TimeOfDay("14:45");
         assertEquals("14:45", timeOfDay2.toString());
     }

     @Test(expected = IllegalArgumentException.class)
     public void testConstructorWithInvalidTimeString()
     {
         try
         {
             timeOfDay3 = new TimeOfDay("15:62");
         }
         catch (IllegalArgumentException e)
         {
             fail("Expected IllegalArgumentException, but got: " + e.getMessage());
         }
         // This exception should be thrown, so we don't write any assertions here
     }

     @Test
     public void testCompareTo()
     {
         timeOfDay4 = new TimeOfDay(13, 30);
         timeOfDay5 = new TimeOfDay(12, 45);

         int comparisonResult = timeOfDay4.compareTo(timeOfDay5);
         assertTrue(comparisonResult < 0);
     }

     @Test
     public void testGetHours()
     {
         assertEquals(12, timeOfDay1.getHours().intValue());
     }

     @Test
     public void testGetMinutes()
     {
         assertEquals(30, timeOfDay1.getMinutes().intValue());
     }

     @Test
     public void testEquals()
     {
         TimeOfDay timeOfDaySame = new TimeOfDay(12, 30);
         TimeOfDay timeOfDayDifferentHours = new TimeOfDay(13, 30);
         TimeOfDay timeOfDayDifferentMinutes = new TimeOfDay(12, 31);

         assertTrue(timeOfDaySame.equals(timeOfDaySame));
         assertFalse(timeOfDaySame.equals(timeOfDayDifferentHours));
         assertFalse(timeOfDaySame.equals(timeOfDayDifferentMinutes));
     }

     @Test
     public void testHashCode()
     {
         TimeOfDay timeOfDaySame = new TimeOfDay(12, 30);
         TimeOfDay timeOfDayDifferent = new TimeOfDay(13, 31);

         int hashCodeSame = timeOfDaySame.hashCode();
         int hashCodeDifferent = timeOfDayDifferent.hashCode();

         assertNotSame(hashCodeSame, hashCodeDifferent);
     }

     @Test(expected = NullPointerException.class)
     public void testEqualsWithNullObject()
     {
         assertFalse(timeOfDay1.equals(null));
         // This exception should be thrown, so we don't write any assertions here
     }

     @Test(expected = ClassCastException.class)
     public void testEqualsWithDifferentClass()
     {
         assertFalse(timeOfDay1.equals(new Object()));
         // This exception should be thrown, so we don't write any assertions here
     }

     @Test
     public void testToString()
     {
         assertEquals("12:30", timeOfDay1.toString());
     }

     @Test
     public void testInvalidObject()
     {
         try
         {
             timeOfDayInvalid = new TimeOfDay(null);
         }
         catch (IllegalArgumentException e)
         {
             fail("Expected IllegalArgumentException, but got: " + e.getMessage());
         }
         // This exception should be thrown, so we don't write any assertions here
     }
 }
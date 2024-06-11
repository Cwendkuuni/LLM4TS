package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class TypeHandlerTest
{
    private String str;
    private Class<?> clazz;
    private Object expected;

    public TypeHandlerTest(String str, Class<?> clazz, Object expected)
    {
        this.str = str;
        this.clazz = clazz;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: createValue({0}, {1}) = {2}")
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {"testString", String.class, "testString"},
                {"testObject", Object.class, null},
                {"12345", Long.class, 12345L},
                {"12345.6789", Double.class, 12345.6789},
                {"2022-03-01", Date.class, new Date()}, // assume a valid date string
                {"java.lang.String", String.class.getClass(), "java.lang.String"},
                {"com.example.MyClass", MyClass.class, null}, // assume MyClass has a no-arg constructor
                {"file.txt", File.class, new File("file.txt")},
                {"file.txt", FileInputStream.class, null},
                {"file1.txt,file2.txt", File[].class, new File[]{"file1.txt", "file2.txt"}},
                {"http://example.com", URL.class, new URL("http://example.com")},
        });
    }

    @Test
    public void testCreateValue()
    {
        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            if (clazz.isInstance(expected))
            {
                assertEquals(expected, actual);
            }
            else if (expected instanceof Class<?>)
            {
                assertInstanceOf(clazz, actual.getClass());
            }
            else if (expected instanceof File)
            {
                assertFileEquals((File) expected, (File) actual);
            }
            else if (expected instanceof Date)
            {
                assertEquals(((Date) expected).getTime(), ((Date) actual).getTime());
            }
            else if (expected instanceof Number)
            {
                assertEquals(((Number) expected).longValue(), ((Number) actual).longValue());
            }
            else if (expected != null)
            {
                assertEquals(expected.toString(), actual.toString());
            }
            else
            {
                assertNull(actual);
            }
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValueThrowsParseException()
    {
        try
        {
            TypeHandler.createValue("invalid", clazz);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateObject()
    {
        try
        {
            if (clazz.isInstance(expected))
            {
                Object actual = TypeHandler.createObject(expected.getTypeName());
                assertEquals(expected, actual);
            }
            else
            {
                fail("TypeHandler.createObject() does not support class: " + clazz);
            }
        }
        catch (Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectThrowsParseException()
    {
        try
        {
            TypeHandler.createObject("java.lang.NonExistentClass");
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateNumber()
    {
        Number expectedNumber = (Number) expected;
        Number actualNumber = (Number) TypeHandler.createNumber(str);
        assertEquals(expectedNumber.longValue(), actualNumber.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberThrowsParseException()
    {
        try
        {
            TypeHandler.createNumber("invalid");
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate()
    {
        try
        {
            TypeHandler.createDate(str);
            fail("Expected UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // expected
        }
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL()
    {
        try
        {
            if (clazz == URL.class)
            {
                URL actual = TypeHandler.createURL(str);
                assertEquals(new URL(expected.toString()), actual);
            }
            else
            {
                fail("TypeHandler.createURL() does not support class: " + clazz);
            }
        }
        catch (MalformedURLException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateURLThrowsParseException()
    {
        try
        {
            TypeHandler.createURL("invalid");
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateFile()
    {
        File expectedFile = (File) expected;
        File actualFile = (File) TypeHandler.createFile(expectedFile.getAbsolutePath());
        assertEquals(expectedFile, actualFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileThrowsFileNotFoundException()
    {
        String filePath = "non-existent-file.txt";

        try
        {
            FileInputStream actual = TypeHandler.openFile(filePath);
            fail("Expected FileNotFoundException");
        }
        catch (FileNotFoundException e)
        {
            // expected
        }
    }

    @Test
    public void testOpenFile()
    {
        File expectedFile = new File("existing-file.txt");
        FileInputStream actualFileInputStream = null;

        try
        {
            actualFileInputStream = TypeHandler.openFile(expectedFile.getAbsolutePath());
            assertNotNull(actualFileInputStream);
        }
        catch (IOException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        finally
        {
            try
            {
                if (actualFileInputStream != null)
                {
                    actualFileInputStream.close();
                }
            }
            catch (IOException e)
            {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesThrowsParseException()
    {
        try
        {
            TypeHandler.createFiles("invalid");
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}

class MyClass
{
    // assume an empty constructor
}
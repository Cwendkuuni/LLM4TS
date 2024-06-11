package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

public class TypeHandlerTest
{
    @Test
    public void testCreateStringValue() throws ParseException
    {
        // Given
        String str = "TestString";
        String expected = "TestString";
        Class<?> clazz = String.class;

        // When
        String result = TypeHandler.createValue(str, clazz);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testCreateObjectValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        // Given
        String className = "java.lang.String";

        // When
        Object result = TypeHandler.createValue(className, String.class);

        // Then
        assertNotNull(result);
        assertEquals(className, result.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberValueInvalid() throws ParseException
    {
        // Given
        String str = "TestString";

        // When
        TypeHandler.createNumber(str);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testCreateNumberValueLong() throws ParseException
    {
        // Given
        String str = "123";
        Long expected = 123L;
        Class<?> clazz = Long.class;

        // When
        Number result = TypeHandler.createValue(str, clazz);

        // Then
        assertEquals(expected.longValue(), result.longValue());
    }

    @Test
    public void testCreateNumberValueDouble() throws ParseException
    {
        // Given
        String str = "123.45";
        Double expected = 123.45;
        Class<?> clazz = Double.class;

        // When
        Number result = TypeHandler.createValue(str, clazz);

        // Then
        assertEquals(expected.doubleValue(), result.doubleValue(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValue() throws ParseException
    {
        // Given
        String str = "2022-12-31";
        Class<?> clazz = Date.class;

        // When
        Date result = TypeHandler.createDate(str);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testCreateURLValue() throws ParseException, MalformedURLException
    {
        // Given
        String str = "http://www.example.com";
        Class<?> clazz = URL.class;

        // When
        URL result = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateFileValue() throws ParseException
    {
        // Given
        String fileName = "testFile.txt";
        Class<?> clazz = File.class;

        // When
        File result = TypeHandler.createFile(fileName);

        // Then
        assertNotNull(result);
        assertEquals(fileName, result.getName());
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalid() throws ParseException
    {
        // Given
        String fileName = "TestInvalidFile";

        // When
        FileInputStream result = TypeHandler.openFile(fileName);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testOpenFileValid() throws ParseException, FileNotFoundException, IOException
    {
        // Given
        String fileName = "testFile.txt";
        Class<?> clazz = FileInputStream.class;

        // When
        FileInputStream result = TypeHandler.openFile(fileName);

        // Then
        assertNotNull(result);
        assertEquals(fileName, new File(fileName).getName());

        // Cleanup
        result.close();
    }
}
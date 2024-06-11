package com.example.twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testPagingDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingConstructorWithNegativePage() {
        new Paging(-1);
    }

    @Test
    public void testPagingConstructorWithSinceId() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingConstructorWithNegativeSinceId() {
        new Paging(-1L);
    }

    @Test
    public void testPagingConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingConstructorWithNegativeCount() {
        new Paging(1, -1);
    }

    @Test
    public void testPagingConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingConstructorWithNegativeSinceId() {
        new Paging(1, -1L);
    }

    @Test
    public void testPagingConstructorWithPageAndCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingConstructorWithPageAndCountAndNegativeSinceId() {
        new Paging(1, 10, -1L);
    }

    @Test
    public void testPagingConstructorWithPageAndCountAndMaxId() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(2L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingConstructorWithPageAndCountAndNegativeMaxId() {
        new Paging(1, 10, 1L, -1L);
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging(1);
        Paging newPaging = paging.count(10);
        assertEquals(10, newPaging.getCount());
        assertEquals(1, newPaging.getPage());
        assertEquals(1, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(2L);
        assertEquals(2L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging(1);
        Paging newPaging = paging.sinceId(1L);
        assertEquals(1, newPaging.getPage());
        assertEquals(1, newPaging.getCount());
        assertEquals(1L, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging(1, 1L, 2L);
        assertEquals(2L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(3L);
        assertEquals(3L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging(1);
        Paging newPaging = paging.maxId(2L);
        assertEquals(1, newPaging.getPage());
        assertEquals(1, newPaging.getCount());
        assertEquals(1, newPaging.getSinceId());
        assertEquals(2L, newPaging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 2L);
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        assertTrue(paging1.equals(paging2));

        Paging paging3 = new Paging(1, 10, 1L, 3L);
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        int hashCode = paging.hashCode();
        Paging paging2 = new Paging(1, 10, 1L, 2L);
        int hashCode2 = paging2.hashCode();
        assertEquals(hashCode, hashCode2);

        Paging paging3 = new Paging(1, 10, 1L, 3L);
        int hashCode3 = paging3.hashCode();
        assertNotEquals(hashCode, hashCode3);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        String toString = paging.toString();
        String expectedString = "Paging{page=1, count=10, sinceId=1, maxId=2}";
        assertEquals(expectedString, toString);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(new char[]{'s', 'm', 'c', 'p'});
        List<HttpParameter> expectedParameterList = Arrays.asList(new HttpParameter("page", "1"),
                new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2"));
        assertEquals(expectedParameterList, asPostParameterList);
    }

    @Test
    public void testAsPostParameterListWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(new char[0]);
        assertTrue(asPostParameterList.isEmpty());
    }

    @Test
    public void testAsPostParameterListWithUnsupportedParam() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> asPostParameterList = paging.asPostParameterList(new char[]{'x'});
        assertTrue(asPostParameterList.isEmpty());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(new char[]{'s', 'm', 'c', 'p'});
        HttpParameter[] expectedParameterArray = new HttpParameter[] {new HttpParameter("page", "1"),
                new HttpParameter("count", "10"), new HttpParameter("since_id", "1"), new HttpParameter("max_id", "2")};
        assertArrayEquals(expectedParameterArray, asPostParameterArray);
    }

    @Test
    public void testAsPostParameterArrayWithEmptySupportedParams() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        HttpParameter[] asPostParameterArray = paging.asPostParameterArray(new char[0]);
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, asPostParameterArray);
    }

}
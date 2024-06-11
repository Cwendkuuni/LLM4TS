package test.twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.*;
import twitter4j.Paging;

public class PagingTest {
    @Test
    public void testAsPostParameterListAndArray() {
        // Testing asPostParameterList() and asPostParameterArray() with null supportedParams
        Paging paging = new Paging();
        List<HttpParameter> expectedEmptyList = new ArrayList<>();
        List<HttpParameter> actualList = paging.asPostParameterList(null);
        assertEquals(expectedEmptyList, actualList);
        HttpParameter[] actualArray = paging.asPostParameterArray(null);
        assertArrayEquals(expectedEmptyArray, actualArray);

        // Testing asPostParameterList() and asPostParameterArray() with negative values
        Paging pagingWithNegativeValues = new Paging(-1, -1, -1L, -1L);
        List<HttpParameter> expectedNegativeList = new ArrayList<>();
        List<HttpParameter> actualNegativeList = paging.asPostParameterList(Paging.SMCP);
        assertEquals(expectedEmptyList, actualNegativeList);
        HttpParameter[] actualNegativeArray = paging.asPostParameterArray(Paging.SMCP);
        assertArrayEquals(expectedEmptyArray, actualNegativeArray);

        // Testing asPostParameterList() and asPostParameterArray() with valid values
        Paging pagingWithValidValues = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> expectedValidList = new ArrayList<>();
        expectedValidList.add(new HttpParameter("page", "1"));
        expectedValidList.add(new HttpParameter("count", "10"));
        expectedValidList.add(new HttpParameter("since_id", "1"));
        expectedValidList.add(new HttpParameter("max_id", "2"));
        List<HttpParameter> actualValidList = paging.asPostParameterList(Paging.SMCP);
        assertEquals(expectedValidList, actualValidList);
        HttpParameter[] actualValidArray = paging.asPostParameterArray(Paging.SMCP);
        assertArrayEquals(expectedValidList.toArray(new HttpParameter[expectedValidList.size()]), actualValidArray);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> expectedEmptyList = new ArrayList<>();
        List<HttpParameter> actualEmptyList = paging.asPostParameterList();
        assertEquals(expectedEmptyList, actualEmptyList);

        Paging pagingWithParams = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> expectedList = new ArrayList<>();
        expectedList.add(new HttpParameter("page", "1"));
        expectedList.add(new HttpParameter("count", "10"));
        expectedList.add(new HttpParameter("since_id", "1"));
        expectedList.add(new HttpParameter("max_id", "2"));
        List<HttpParameter> actualList = paging
    .asPostParameterList(Paging.SMCP);
    assertEquals(expectedList, actualList);
}

@Test
public void testAsPostParameterArray() {
    Paging paging = new Paging();
    HttpParameter[] expectedEmptyArray = new HttpParameter[0];
    HttpParameter[] actualEmptyArray = paging.asPostParameterArray();
    assertArrayEquals(expectedEmptyArray, actualEmptyArray);

    Paging pagingWithParams = new Paging(1, 10, 1L, 2L);
    List<HttpParameter> expectedList = new ArrayList<>();
    expectedList.add(new HttpParameter("page", "1"));
    expectedList.add(new HttpParameter("count", "10"));
    expectedList.add(new HttpParameter("since_id", "1"));
    expectedList.add(new HttpParameter("max_id", "2"));
    HttpParameter[] actualList = paging.asPostParameterArray();
    assertArrayEquals(expectedList.toArray(new HttpParameter[expectedList.size()]), actualList);
}

}
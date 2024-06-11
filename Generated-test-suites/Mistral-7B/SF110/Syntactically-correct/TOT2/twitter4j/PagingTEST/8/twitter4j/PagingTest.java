
import org.junit.Assert;
import twitter4j.HttpParameter;
import twitter4j.Paging;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        Paging paging = new Paging();
        Assert.assertFalse(paging.getPage() >= 0);
        Assert.assertFalse(paging.getCount() >= 0);
        Assert.assertFalse(paging.getSinceId() >= 0);
        Assert.assertFalse(paging.getMaxId() >= 0);
    }

    @org.junit.Test
    public void testConstructorWithPageArgument() {
        Paging paging = new Paging(1);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertFalse(paging.getCount() >= 0);
        Assert.assertFalse(paging.getSinceId() >= 0);
        Assert.assertFalse(paging.getMaxId() >= 0);
    }

    @org.junit.Test
    public void testConstructorWithCountArgument() {
        Paging paging = new Paging(1, 0);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(0, paging.getCount());
        Assert.assertFalse(paging.getSinceId() >= 0);
        Assert.assertFalse(paging.getMaxId() >= 0);
    }

    @org.junit.Test
    public void testConstructorWithSinceIdArgument() {
        Paging paging = new Paging(1, 10, Long.MIN_VALUE);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(Long.MIN_VALUE, paging.getSinceId());
        Assert.assertFalse(paging.getMaxId() >= 0);
    }

    @org.junit.Test
    public void testConstructorWithMaxIdArgument() {
        Paging paging = new Paging(1, 10, 1, Long.MIN_VALUE);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1, paging.getSinceId());
        Assert.assertEquals(Long.MIN_VALUE, paging.getMaxId());
    }

    @org.junit.Test
    public void testConstructorWithAllArguments() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1234567890L, paging.getSinceId());
        Assert.assertEquals(9876543210L, paging.getMaxId());
    }

    @org.junit.Test
    public void testAddPostParameter() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        List<HttpParameter> postParams = new ArrayList<>();
        paging.addPostParameter(Paging.S, 's', postParams, "since_id", 1234567890L);
        paging.addPostParameter(Paging.SMCP, 'c', postParams, "count", 10);
        HttpParameter[] postParamArray = postParams.toArray(new HttpParameter[postParams.size()]);

        Assert.assertEquals(2, postParamArray.length);
        Assert.assertEquals("since_id", postParamArray[0].getName());
        Assert.assertEquals("1234567890", postParamArray[0].getValue());
        Assert.assertEquals("count", postParamArray[1].getName());
        Assert.assertEquals("10", postParamArray[1].getValue());
    }

    @org.junit.Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        HttpParameter[] postParamsArray = paging.asPostParameterArray();

        Assert.assertArrayEquals(new HttpParameter[]{new HttpParameter("since_id", "1234567890"), new HttpParameter("count", "10")}, postParamsArray);
    }

    @org.junit.Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        List<HttpParameter> postParamsList = paging.asPostParameterList();

        List<HttpParameter> expectedParamsList = Arrays.asList(new HttpParameter("since_id", "1234567890"), new HttpParameter("count", "10"));
        Assert.assertEquals(expectedParamsList, postParamsList);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidPage() {
        Paging paging = new Paging(-1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidCount() {
        Paging paging = new Paging(1, -1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidSinceId() {
        Paging paging = new Paging(1, 10, -1L);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidMaxId() {
        Paging paging = new Paging(1, 10, 1, -1L);
    }

    @org.junit.Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging3 = new Paging(1, 10, 1234567890L, 9876543211L);

        Assert.assertTrue(paging1.equals(paging2));
        Assert.assertFalse(paging1.equals(paging3));
    }

    @org.junit.Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging3 = new Paging(1, 10, 1234567890L, 9876543211L);

        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
        Assert.assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        String expected = "Paging{page=1, count=10, sinceId=1234567890, maxId=9876543210}";
        Assert.assertEquals(expected, paging.toString());
    }
}

import static org.junit.Assert.assertEquals;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {
   @Test
   public void testParse() {
      String query1 = "#id1";
      String query2 = "div.class1";
      String query3 = "div > p";
      String query4 = "";
      String query5 = null;
      QueryParser parser = new QueryParser();

      Evaluator expectedEvaluator1 = new Evaluator.Id("id1");
      Evaluator expectedEvaluator2 = new StructuralEvaluator.And(
         new Evaluator.Class("class1"),
         new Evaluator.Tag("div")
      );
      Evaluator expectedEvaluator3 = new StructuralEvaluator.And(
         new Evaluator.Tag("div"),
         new StructuralEvaluator.ImmediateChild(
            new Evaluator.Tag("p")
         )
      );

      Evaluator actualEvaluator1 = parser.parse(query1);
      Evaluator actualEvaluator2 = parser.parse(query2);
      Evaluator actualEvaluator3 = parser.parse(query3);

      assertEquals(expectedEvaluator1, actualEvaluator1);
      assertEquals(expectedEvaluator2, actualEvaluator2);
      assertEquals(expectedEvaluator3, actualEvaluator3);

      // Test for empty and null queries
      Evaluator actualEvaluator4 = parser.parse(query4);
      Evaluator actualEvaluator5 = parser.parse(query5);
      // Add expected null check here
      // ...
   }
}
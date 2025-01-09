import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * @author Put your name here
 *
 */
public class GlossaryTest {

    /*
     * generateElements method testing
     */

    //Routine
    @Test
    public void generateElementsRoutine() {
        String str = "abc";
        Set<Character> expected = new Set1L<Character>();
        expected.add('a');
        expected.add('b');
        expected.add('c');

        Set<Character> charSet = new Set1L<Character>();
        Glossary.generateElements(str, charSet);

        assertEquals(charSet, expected);
    }

    //Boundary
    @Test
    public void generateElementsBoundary() {
        String str = "a";
        Set<Character> expected = new Set1L<Character>();
        expected.add('a');

        Set<Character> charSet = new Set1L<Character>();
        Glossary.generateElements(str, charSet);

        assertEquals(charSet, expected);
    }

    //Challenging
    @Test
    public void generateElementsChallenging() {
        String str = "aaaa....1111,!!!!!!!!/2 ]";
        Set<Character> expected = new Set1L<Character>();
        expected.add('a');
        expected.add('.');
        expected.add('1');
        expected.add(',');
        expected.add('!');
        expected.add('/');
        expected.add('2');
        expected.add(' ');
        expected.add(']');

        Set<Character> charSet = new Set1L<Character>();
        Glossary.generateElements(str, charSet);

        assertEquals(charSet, expected);
    }

    /*
     * nextWordOrSeparator method testing
     */

    //Routine
    @Test
    public void nextWordOrSeparatorRoutine() {
        String str = "Hi, my name is Aadi";
        int position = 0;
        Set<Character> seperators = new Set1L<Character>();
        seperators.add(',');
        seperators.add(' ');

        String result = Glossary.nextWordOrSeparator(str, position, seperators);
        assertEquals("Hi", result);
    }

    //Boundary
    @Test
    public void nextWordOrSeparatorBoundary() {
        String str = ",H, e, l, l, o,";
        int position = 0;
        Set<Character> seperators = new Set1L<Character>();

        String result = Glossary.nextWordOrSeparator(str, position, seperators);
        assertEquals(",H, e, l, l, o,", result);
    }

    //Challenging
    @Test
    public void nextWordOrSeparatorChallenging() {
        String str = ",,!,,HI";
        int position = 0;
        Set<Character> seperators = new Set1L<Character>();
        seperators.add(',');
        seperators.add('!');

        String result = Glossary.nextWordOrSeparator(str, position, seperators);
        assertEquals(",,!,,", result);
    }

    /*
     * mapAndQueueFromFile method testing
     */

    //Routine
    @Test
    public void mapAndQueueFromFileRoutine() {
        SimpleReader input = new SimpleReader1L("test/test1.txt");

        Map<String, String> mapExpected = new Map1L<String, String>();
        mapExpected.add("meaning",
                "something that one wishes to convey, especially by language");
        Queue<String> queueExpected = new Queue1L<String>();
        queueExpected.enqueue("meaning");

        Map<String, String> mapResult = new Map1L<String, String>();
        Queue<String> queueResult = new Queue1L<String>();
        Glossary.mapAndQueueFromFile(mapResult, queueResult, input);
        assertEquals(mapResult, mapExpected);
    }

    //Challenging
    @Test
    public void mapAndQueueFromFileChallenging() {
        SimpleReader input = new SimpleReader1L("test/test2.txt");

        Map<String, String> mapExpected = new Map1L<String, String>();
        mapExpected.add("glossary",
                "a list of difficult or specialized terms, with their definitions, usually near the end of a book");
        Queue<String> queueExpected = new Queue1L<String>();
        queueExpected.enqueue("glossary");

        Map<String, String> mapResult = new Map1L<String, String>();
        Queue<String> queueResult = new Queue1L<String>();
        Glossary.mapAndQueueFromFile(mapResult, queueResult, input);
        assertEquals(mapResult, mapExpected);
    }

    //Boundary
    @Test
    public void mapAndQueueFromFileBoundary() {
        SimpleReader input = new SimpleReader1L("test/test3.txt");

        Map<String, String> mapExpected = new Map1L<String, String>();
        mapExpected.add("meaning",
                "something that one wishes to convey, especially by language");
        mapExpected.add("term", "a word whose definition is in a glossary");
        Queue<String> queueExpected = new Queue1L<String>();
        queueExpected.enqueue("meaning");
        queueExpected.enqueue("term");

        Map<String, String> mapResult = new Map1L<String, String>();
        Queue<String> queueResult = new Queue1L<String>();
        Glossary.mapAndQueueFromFile(mapResult, queueResult, input);
        assertEquals(mapResult, mapExpected);
    }

    /*
     * indexFileSetupRoutine method testing
     */

    //Challenging
    @Test
    public void indexFileSetupRoutine() {
        SimpleWriter index = new SimpleWriter1L("test/indexRoutineResult.txt");
        Queue<String> queueResult = new Queue1L<String>();
        queueResult.enqueue("meaning");
        queueResult.enqueue("term");
        Glossary.indexFileSetup(index, queueResult);

        SimpleReader indexExpected = new SimpleReader1L("test/indexRoutineExpected.txt");
        SimpleReader indexResult = new SimpleReader1L(("test/indexRoutineResult.txt"));

        String expectedLine = indexExpected.nextLine();
        String resultLine = indexResult.nextLine();
        while (!indexExpected.atEOS()) {
            assertEquals(resultLine, expectedLine);
            expectedLine = indexExpected.nextLine();
            resultLine = indexResult.nextLine();
        }

    }

    //Boundary
    @Test
    public void indexFileSetupBoundary() {
        SimpleWriter index = new SimpleWriter1L("test/indexBoundaryResult.txt");
        Queue<String> queueResult = new Queue1L<String>();
        Glossary.indexFileSetup(index, queueResult);

        SimpleReader indexExpected = new SimpleReader1L("test/indexBoundaryExpected.txt");
        SimpleReader indexResult = new SimpleReader1L(("test/indexBoundaryResult.txt"));

        String expectedLine = indexExpected.nextLine();
        String resultLine = indexResult.nextLine();
        while (!indexExpected.atEOS()) {
            assertEquals(resultLine, expectedLine);
            expectedLine = indexExpected.nextLine();
            resultLine = indexResult.nextLine();
        }

    }

}

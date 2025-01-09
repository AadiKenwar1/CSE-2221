import java.util.Comparator;

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
 * Put a short phrase describing the program here.
 *
 * @author Aadi Kenwar
 *
 */
public final class Glossary {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Glossary() {
    }

    /**
     * A comporator for strings that compares two strings based on
     * lexicographical order.
     */
    private static final class StringLT implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";

        charSet.clear();
        for (int i = 0; i < str.length(); i++) {
            if (!(charSet.contains(str.charAt(i)))) {
                charSet.add(str.charAt(i));
            }

        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separatorS
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        String result = "";
        char currentChar = text.charAt(position);
        int index = position + 1;
        result += currentChar;

        /*
         * If current character isnt a seperator, keep adding consecutive
         * characters to result until current character is a seperator, in turn
         * making next word. Else, if current character is a seperator, keep
         * checking if consequtive characters are seperators and add them to
         * result
         */

        if (!separators.contains(currentChar)) {
            while (index < text.length() && !separators.contains(currentChar)) {
                currentChar = text.charAt(index);
                if (!separators.contains(currentChar)) {
                    result += currentChar;
                }
                index++;
            }
        } else {
            while (index < text.length() && separators.contains(currentChar)) {
                currentChar = text.charAt(index);
                if (separators.contains(currentChar)) {
                    result += currentChar;
                }
                index++;
            }
        }
        return result;

    }

    /**
     * Extracts terms and definitions from text file to generate a Map
     * containing all terms(key) and respective definitions(values), and a Queue
     * containing all terms.
     *
     * @param glossaryMap
     *            generates {@code Map} of all the terms and definitions from
     *            glossary as keys and values respectively.
     * @param terms
     *            generates {@code Queue} with all of the terms from glossary.
     * @param input
     *            the {@code SimpleReader} to read the txt file and extract all
     *            the terms and their definitions
     * @requires properly formatted input file
     * @ensures Map<String, String>Map == all {(term, definition}),
     *          Queue<String> terms == <all terms>.
     */
    public static void mapAndQueueFromFile(Map<String, String> glossaryMap,
            Queue<String> terms, SimpleReader input) {

        //Generates the seperator set
        Set<Character> separatorSet = new Set1L<>();
        String separatorStr = "\t";
        generateElements(separatorStr, separatorSet);

        String fileLine = input.nextLine();
        while (!input.atEOS()) {
            //Gets term and adds term to Queue
            String key = nextWordOrSeparator(fileLine, 0, separatorSet);
            terms.enqueue(key);

            //Gets definition by checking every line after term until line is empty
            String value = "";
            fileLine = input.nextLine();
            while (!fileLine.isEmpty()) {
                value += nextWordOrSeparator(fileLine, 0, separatorSet) + " ";
                fileLine = input.nextLine();
            }
            //This just gets rid of that blank space at the end
            value = value.substring(0, value.length() - 1);

            //Only go to next line if not EOS so we don't get error
            if (!input.atEOS()) {
                fileLine = input.nextLine();
            }

            //Adds current pair of term and definition to Queue
            glossaryMap.add(key, value);

        }

    }

    /**
     * Generates and index file of the glossary containing a list of all the
     * terms with links to their corresponding pages.
     *
     * @param index
     *            the {@code SimpleWriter} to write the index file
     * @param terms
     *            the {@code Queue} to read terms and create list with links
     * @ensures proper formatted index file with list of terms and links
     */
    public static void indexFileSetup(SimpleWriter index, Queue<String> terms) {

        //Generates index header
        index.println("<html>");
        index.println("<head>");
        index.print("<title>");
        index.print("Glossary");
        index.println("</title>");
        index.println("</head>");
        index.println("<body>");
        index.println("<h1>" + "Glossary" + "</h1>");
        index.println("<hr>");
        index.println("<h2><b>" + "Index" + "</b></h2>");

        //Generates list of terms with links
        index.println("<ul>");
        for (String term : terms) {
            index.print("<li>");
            index.print("<a href=\"" + term + ".html" + "\">" + term + "</a>");
            index.println("</li>");
        }
        index.println("</ul>");

        //Generates index footer
        index.println("</body>");
        index.println("</html>");
    }

    /**
     * Generates page for term consisting of red italic header, definition, line
     * break, links to word if appears in definition, and link back to index
     * page.
     *
     * @param termFile
     *            the {@code SimpleWriter} to write the file for the term
     * @param term
     *            the {@code String} term for which we are writing the file for
     * @param glossary
     *            the {@code Map} containing all the terms and their definitions
     * @ensures properly formatted html file with all features stated in
     *          description
     */
    public static void termFileSetup(SimpleWriter termFile, String term,
            Map<String, String> glossary) {

        //Generates term file header
        termFile.println("<html>");
        termFile.println("<head>");
        termFile.print("<title>");
        termFile.print(term);
        termFile.println("</title>");
        termFile.println("</head>");
        termFile.println("<body>");
        termFile.println("<h1><i style=\"color:red;\">" + term + "</i></h1>");

        /*
         * If word in glossary appears in definition, creates a link to said
         * word in the definition
         */
        termFile.println("<p>");
        Set<Character> separatorSet = new Set1L<>();
        String separatorStr = " \t, ";
        generateElements(separatorStr, separatorSet);
        String definition = glossary.value(term);
        int position = 0;
        while (position < definition.length()) {
            String word = nextWordOrSeparator(definition, position, separatorSet);
            if (glossary.hasKey(word)) {
                termFile.print("<a href=\"" + word + ".html" + "\">" + word + "</a>");
            } else {
                termFile.print(word);
            }
            position += word.length();
        }
        termFile.println("</p>");

        termFile.println("<hr>");
        termFile.println("<p>Return to " + "<a href=\"index.html\">index</a>" + ".</p>");

        //Generates term file footer
        termFile.println("</body>");
        termFile.println("</html>");

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.println("Enter name of input file");
        String inputFile = in.nextLine();

        out.println("Enter name of folder");
        String outputFolder = in.nextLine();

        SimpleReader glossaryFile = new SimpleReader1L(inputFile);

        //Generates sorted Queue of terms & Map of terms and definitions
        Queue<String> terms = new Queue1L<String>();
        Map<String, String> glossary = new Map1L<String, String>();
        mapAndQueueFromFile(glossary, terms, glossaryFile);
        Comparator<String> order = new StringLT();
        terms.sort(order);

        //Creates index page
        SimpleWriter indexFile = new SimpleWriter1L(outputFolder + "/index.html");
        indexFileSetup(indexFile, terms);

        //Creates pages for all the terms
        for (String term : terms) {
            SimpleWriter termFile = new SimpleWriter1L(
                    outputFolder + "/" + term + ".html");
            termFileSetup(termFile, term, glossary);

        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
        glossaryFile.close();
    }

}

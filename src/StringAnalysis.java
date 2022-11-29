import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;


public class StringAnalysis {

    // prints a table for a sentence's nouns, verbs, and adjectives
    public static void printTable(int nouns, int verbs, int adjs) {
        System.out.println("\t\tnoun count\t\tverb count\t\tadjective count");
        System.out.println("\t\t" + nouns + "\t\t\t\t" + verbs + "\t\t\t\t" + adjs);
    }

    // reads in a sentence and sends out get requests for analyzing each word's part of speech
    public static void analyze() throws Exception {
        int nouns = 0;
        int verbs = 0;
        int adjs = 0;

        // get input
        Scanner scan = new Scanner(System.in);
        StringTokenizer sentence = new StringTokenizer("a a a a a a a a a a a a a"); // 13 words
        while (sentence.countTokens() > 12) {
            System.out.println("Enter a sentence (up to 12 words): ");
            sentence = new StringTokenizer(scan.nextLine());
        }

        // read input
        while (sentence.hasMoreTokens()) {
            String word = sentence.nextToken().replaceAll("\\p{Punct}", "").toLowerCase();

            String word_url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
            URL url = new URL(word_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // parse JSON
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = "";
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            // analyze each word
                                                                                     // gets rid of beginning '[' and ending ']'
            JSONObject response_obj = new JSONObject(response.toString().substring(1, response.length()-1));
            JSONArray meanings_array = response_obj.getJSONArray("meanings");
            // loop thru each meaning and increment counts appropriately
            for (int i = 0; i < meanings_array.length(); i++) {
                JSONObject meaning = meanings_array.getJSONObject(i);
                String part = meaning.get("partOfSpeech").toString();
                if (part.equals("noun")) {
                    nouns++;
                }
                if (part.equals("verb")) {
                    verbs++;
                }
                if (part.equals("adjective")) {
                    adjs++;
                }
            }
        }

        printTable(nouns, verbs, adjs);
    }

    public static void main(String[] args) {
        try {
            analyze();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

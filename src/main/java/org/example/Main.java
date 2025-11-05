package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    // Static Values for reference to keep Top 3 words and values
    // these to be used in code later
    static String[] TOP_WORD = new String[3];
    static int[] TOP_SCORE = new int[3];
    static int TOP_FILLED = 0;


    public static void main(String[] args) throws IOException {
        System.out.printf("Hello and welcome!%n%n");

//        List<String> ruan = Files.readAllLines(Paths.get("input.txt")); // Small test file

        // Took the file values from
        // https://raw.githubusercontent.com/benjamincrom/scrabble/master/scrabble/dictionary.json
        // Copied them and added it to my project as input2.txt
        // removed double quotes and comma's
        // then I ingest file with below command
        List<String> ruanInputFile = Files.readAllLines(Paths.get("input2.txt")); // Full File from Github

        // Prepare an Output File - Store all words and Score
        // Can be imported to Excel after, making file comma delimited
        // https://www.baeldung.com/java-write-to-file
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

        // Loop over each word in the file
        // Take each word and check if the word contains the letters of the scorecard
        // assign a sum of total points per word, based on the scorecard
        // https://www.geeksforgeeks.org/java/iterate-through-list-in-java/
        for (String singleWord : ruanInputFile) {
            int totalPointsOfWord = 0;
            for (int i = 0; i < singleWord.length(); i++) {
                totalPointsOfWord += scoreOf(singleWord.charAt(i));   // returns 1,2,3,4,5,8,10 or 0
            }
            writer.write(singleWord + "," + totalPointsOfWord + "\n");

            // Continuously build the top 3 words with scores until end of file
            // this list is ever changing until while file is processed.
            updateTop3(singleWord, totalPointsOfWord);

        }
        writer.close();

        // Now we print out the top3, which we continuously
        // updated in the updateTop3 Method.
        System.out.println("Top 3 highest-scoring words:");
        for (int i = 0; i < TOP_FILLED; i++) {
            System.out.println(TOP_WORD[i] + "," + TOP_SCORE[i]);
        }
    }

    // Method to build scoresheet
    // https://stackoverflow.com/questions/14810506/map-function-for-objects-instead-of-arrays
    static final Object[][] BUILDSCORESHEET = {
            {1, "AEILNORSTU"},
            {2, "DG"},
            {3, "BCMP"},
            {4, "FHVWY"},
            {5, "K"},
            {8, "JX"},
            {10, "QZ"}
    };

    static int scoreOf(char ch) {
        // Because file is large, I am not sure if there are sneakily words with Capital letter
        // so adding below to cater for Upper and LowerCase
        // With proper Java imports using Gradle or maven, this can be done far better
        // Pure Java is trickier.
        if (ch >= 'a' && ch <= 'z') ch = (char) (ch - ('a' - 'A')); // Cater for Lower and UpperCase

        // Loop over the score table (BUILDSCORESHEET)
        // For each pair, grab the points (like 1) and the letters string (like "AEILNORSTU").
        for (int i = 0; i < BUILDSCORESHEET.length; i++) { // Length here is 7 because we have 7 entries in scoresheet
            int points = (Integer) BUILDSCORESHEET[i][0]; // because we built a keypair, grab the score which is in position 0
            String letters = (String) BUILDSCORESHEET[i][1]; // because we built a keypair, grab the letters that make up the points from position 1

            // Below used to simulate a contains check,
            // to iterate through all charatcers inside my BUILDSCORESHEET
            // Check if this uppercase char is one of the letters in this bucket
            // Walk through the letters string: if any character equals ch, return its points immediately.
            for (int j = 0; j < letters.length(); j++) {
                if (letters.charAt(j) == ch) return points; // found values from scoresheet → return its point
            }
        }
        // Below is set if nothing was found in scoresheet in any bucket, it’s worth 0 points (digits, symbols, non A–Z (Alphabet)
        return 0;
    }

    // The idea here is to keep it simple, and to always keep a list of the top3
    // This list will continuously be updated.
    // the first time it will get populated by the first 3 records from the input file
    // thereafter each new word that exceeds any of the current words and scores
    // within this bucket will be replaced but the next highest number
    // at the end, we will print the top 3 highest ranking words.
    static void updateTop3(String singleWord, int totalPointsOfWord) {
        if (TOP_FILLED < 3) { // fill first 3
            TOP_WORD[TOP_FILLED] = singleWord;
            TOP_SCORE[TOP_FILLED] = totalPointsOfWord;
            TOP_FILLED++;
            return;
        }

        // find current lowest scores among 3 existing entries
        int minIdx = 0;
        if (TOP_SCORE[1] < TOP_SCORE[minIdx]) minIdx = 1;
        if (TOP_SCORE[2] < TOP_SCORE[minIdx]) minIdx = 2;

        // replace if better
        if (totalPointsOfWord > TOP_SCORE[minIdx]) {
            TOP_WORD[minIdx] = singleWord;
            TOP_SCORE[minIdx] = totalPointsOfWord;
        }
    }

} // End of Class
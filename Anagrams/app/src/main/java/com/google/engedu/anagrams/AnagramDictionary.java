/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.provider.UserDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList= new ArrayList<>();
    private HashSet<String> wordSet  = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord= new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            //wordList.add(word);
            wordSet.add(word);
            //As you process the input words, call sortLetters on each of them then
            // check whether lettersToWord already contains an entry for that key
            String sortedWord =sortLetters(word);
            if(lettersToWord.containsKey(sortedWord))
            {
                //If it does, add the current word to ArrayList at that key
                lettersToWord.get(sortedWord).add(word);
            }//Otherwise, create a new ArrayList, add the word to it and store
            // in the HashMap with the corresponding key.
            else{
                ArrayList<String> words= new ArrayList<>();
                words.add(word);
                lettersToWord.put(sortedWord,words);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {
        //the provided word is a valid dictionary word (i.e., in wordSet), and
       //the word does not contain the base word as a substring.
        return wordSet.contains(word)&&!word.contains(base);
    }

    public String sortLetters(String word){
        char [] ch=word.toCharArray();
        Arrays.sort(ch);
        return new String(ch);
    }
    public List<String> getAnagrams(String targetWord) {

        // wordList
        //for loop will check word in the wordlist is an anagram of the
        //targetWord
        ArrayList<String> result = new ArrayList<String>();
        String targetSorted=sortLetters(targetWord);
        if(lettersToWord.containsKey(targetSorted)){
            result.addAll(lettersToWord.get(targetSorted));
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
       // Finally, implement getAnagramsWithOneMoreLetter which takes
        // a string and finds all anagrams that can be formed
        // by adding one letter to that word.
        //Be sure to instantiate a new ArrayList as your
        // return value then check the given word + each letter
        // of the alphabet one by one against the entries in
        // lettersToWord.
        ArrayList<String> result = new ArrayList<String>();
        int char_a=97;
        int char_z=122;
        for(int i=char_a;i<=char_z;i++){
            char c  =(char)i;
            String wordPlusOne =word.concat(String.valueOf(c));
            ArrayList<String> anagrams= (ArrayList<String>) getAnagrams(wordPlusOne);
            for (String w:anagrams){
                if(isGoodWord(w, word)){
                    result.add(w);
                }
            }
        }
        return result;
    }
    //Homework
    public String pickGoodStarterWord() {
        //If your game is working, proceed to implement
        // pickGoodStarterWord to make the game more interesting.
        // Pick a random starting point in the wordSet  and
        // check each word in the array until you find one that has at
        // least MIN_NUM_ANAGRAMS anagrams. Be sure to handle wrapping
        // around to the start of the array if needed.
        return "skate";
    }
}

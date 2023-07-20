import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;



/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 * @author Courtney Lee (cml0082@auburn.edu)
 */
public class Doublets implements WordLadderGame {

   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   /////////////////////////////////////////////////////////////////////////////
   // DECLARE A FIELD NAMED lexicon HERE. THIS FIELD IS USED TO STORE ALL THE //
   // WORDS IN THE WORD LIST. YOU CAN CREATE YOUR OWN COLLECTION FOR THIS     //
   // PURPOSE OF YOU CAN USE ONE OF THE JCF COLLECTIONS. SUGGESTED CHOICES    //
   // ARE TreeSet (a red-black tree) OR HashSet (a closed addressed hash      //
   // table with chaining).
   /////////////////////////////////////////////////////////////////////////////
   
  
   List<String> EMPTY_LADDER = new ArrayList<>();
   TreeSet<String> lexicon;
   
   
          /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         //////////////////////////////////////
         // INSTANTIATE lexicon OBJECT HERE  //
         //////////////////////////////////////
         
         lexicon = new TreeSet<String>();
         
         Scanner s =
            
            new Scanner(new BufferedReader(new InputStreamReader(in)));
            
         while (s.hasNext()) {
            String str = s.next();
            /////////////////////////////////////////////////////////////
            // INSERT CODE HERE TO APPROPRIATELY STORE str IN lexicon. //
            /////////////////////////////////////////////////////////////
            lexicon.add(str.toLowerCase());
            
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }
 /**
    * Method to return the number of differing characters (hamming distance) between 
    * @param str1 string and
    * @param str2 string
    * @return distance is they are the same length, and -1 otherwise
    */
   public int getHammingDistance(String str1, String str2) {
    
      int distance = 0;
    
      if(str1.length() != str2.length()) {
         return -1;
      }
      
      for(int i = 0; i < str1.length(); i++) {
      
         if (str1.charAt(i) != str2.charAt(i)) {
         
            distance++;
         }
      }
      return distance;
   }
   
    /*
    *Method to get MinLadder
    *@param beginning for start
    *@param last for last word
    *@return ladder of minLadder
    */
    
   public List<String> getMinLadder(String start, String end) {
      
      ArrayList<String> back = new ArrayList<String>();
      List<String> minLadder = new ArrayList<String>();
    
   //  //if start and end are the same, return EMPTY_LADDER 
      // if(getHammingDistance(start, end) == -1) {
         // return EMPTY_LADDER;
      // }
    
    //valid search
      if(isWord(start) && isWord(end)) {
         back = getLadder(start, end);
      }
    
    //if ther is no ladder, return EMPTY_LADDER
      if(back.isEmpty()) {
         return EMPTY_LADDER;
      }
    
      for (int i = back.size() - 1; i >= 0; i--) {
         minLadder.add(back.get(i));
      }
      return minLadder;
   }
    

   
   /**
   *Method to get Ladder using bfs
   *@param beginning for first word
   *@param last for last word
   *@return ladder
   */
   
   public ArrayList<String> getLadder(String start, String end) {
   
      Deque<Node> queue = new ArrayDeque <Node>();
      HashSet<String> visited = new HashSet<String>();
      ArrayList<String> back = new ArrayList<String>();
   
      visited.add(start);
   
      queue.addLast(new Node(start, null));
      Node endNode = new Node(end, null); 
   
      outerloop:
      while(!queue.isEmpty()) {
      
         Node n = queue.removeFirst();
         String word = n.position;
         List<String> neighbors = getNeighbors(word);
         for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
               visited.add(neighbor);
            
               queue.addLast(new Node(neighbor,n));
            
               if(neighbor.equals(end)) {
                  endNode.pred = n;
                  break outerloop;
               }
            }
         }
      }
   
      if(endNode.pred == null) {
         return back;
      }
   
      Node m = endNode;
      while (m != null) {
         back.add(m.position);
         m = m.pred;
      }
      return back;
   }
   
    

   
   /**
    * Method to return words that have a hamming distance of 1 from param word
    * @param word to get the neighbors of
    * @return neighbors of word 
    */
   public List<String> getNeighbors(String word) {
    
    //return list of neighbors
      List<String> neighbors = new ArrayList<String>();
      TreeSet<String> set = new TreeSet<String>();
    
      if (word == null) {
         return EMPTY_LADDER;
      }
    
      for (String s : lexicon) {
         if(getHammingDistance(word,s) == 1) 
            neighbors.add(s);
      }
      return neighbors;
   }
   
   /**
   * Method to return the number of words in the lexicon.
   * @return number of words
   */
   public int getWordCount() {
      return lexicon.size();
   }

   /**
   * Method to determine if given string is a word. 
   * @param str - string to check
   * @return true if str is a word and false otherwise
   */
   public boolean isWord(String str) {
   
      if(lexicon.contains(str)) {
         return true;
      }
      return false;
   }
   
    /*
    * Method to return 
    * @param
    * @return
    */
   public boolean isWordLadder(List<String> sequence) {
    
      int count = 0;
    
      if((sequence == null)||(sequence.isEmpty())) {
         return false;
      }
    
      for(int i = 0; i < sequence.size()-1; i++) {
      
         if(isWord(sequence.get(i)) != true ||
             isWord(sequence.get(i+1)) != true || 
             (getHammingDistance(sequence.get(i), sequence.get(i+1)) != 1))
            count ++;
      }
      boolean i = (count == 0);
      return i;
   }
   
    
    
    //Nested class for nodes
    
   private class Node {
    //feilds
      String position;
      Node pred;
    
      public Node(String p, Node pre) {
         position = p;
         pred = pre;
      }
   } 

}


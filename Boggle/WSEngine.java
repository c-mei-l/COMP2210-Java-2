import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

 /**
 * Driver Program for word search game. 
 *
 * Courtney Lee - cml0082@auburn.edu
 * 8 July 2021
 */ 
 
public class WSEngine implements WordSearchGame {

//fields
   private String[][] board;
   private boolean[][] done;
   private List<Integer> indexes;
   //dimensions for square matrix
   private int n; 
   private TreeSet<String> lexicon;
   private SortedSet<String> scorable;
   private List<Position> finalList;   
        

//methods

/**
* load and read a list of words from text file
* stores each unique word in TreeSet
* @param fileName name of file 
*/
   public void loadLexicon(String fileName) {
   
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      //create TreeSet for the file
      lexicon = new TreeSet<String>();
      try {
         File lex = new File(fileName);
         Scanner scanFile = new Scanner(lex);
         
         while (scanFile.hasNext()) {
         //compared in a case insensitive manner
            lexicon.add(scanFile.next().toUpperCase());
         }
      }
      catch (FileNotFoundException exception) {
         throw new IllegalArgumentException();
      }
   }
   
/**
* method to populate the tree / add letters to the board.
* @param letterArray array of letters to set 
*/
   public void setBoard(String[] letterArray) {
   
      if (letterArray == null) { 
         throw new IllegalArgumentException();
      }
      int size = letterArray.length;
       
      //get dimensions from n^2 = array length  
      n = (int) Math.sqrt(size);
      
      if ((n * n) != letterArray.length) {
         throw new IllegalArgumentException();
      }
      
      //creates a two dimensional array with n x n dimensions
      board = new String[n][n];
      done = new boolean[n][n]; //for letters that have been visited
      
      // loop to add the letters to the board at index of 2D array 
      int index = 0;
      for (int i = 0; i < n; i++) {
      
         for (int j = 0; j < n; j++) {
            board[i][j] = letterArray[index++];
         }
      }
      //after the board is created, mark all spots as no visited
      notVisited();
   }
   
 /**
 * Identify parts of the board that have not been visited
 */  
   public void notVisited() {
      done = new boolean[n][n];
      for (boolean[] row : done) {
         Arrays.fill(row, false);
      }
   }
   
/**
* method to get the set board
* @return return the board
*/
   public String getBoard() {
   
      String letters = "";
      
      for (int i = 0; i < n; i++) {
      
         for (int j = 0; j < n; j++) {
            letters = letters + board[i][j];
         }
         letters = letters + "\n";
      }
      return letters;
   }
   
/**
* Method to return scorable words - meet the minimum length
* @param minimumWordLength minimum length of a word to be scored
* @return return the Tree set of all scorable word
*/
   public SortedSet<String> getAllScorableWords(int minimumWordLength) {
   
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      
      //new tree set for scorable words 
      scorable = new TreeSet<String>();
      
      //identify the words on the board that are long enough and add them to tree set 
      SortedSet<String> longWords = new TreeSet<String>();
      
      for (String w : lexicon) {
      
         if (w.length() >= minimumWordLength && isOnBoard(w).size() != 0) {
         
            scorable.add(w.toUpperCase());
         }
      } 
      
      return scorable;
   }
   
  

/**
* Method to calculate the score of the scorable words 
* One point for minimum length and one point for each letter after 
* @param words set of scorable words from 
* @param minimumWordLength minimum length of word wanted
* @return return the score
*/
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      int score = 0;
      //for each loop to check minimum length and calculate
      for (String word : words) {
         if (word.length() >= minimumWordLength) {
         
            score = score + word.length() - minimumWordLength + 1;
         }
      }
      return score;
   }

 /**
 * Method to check if a word is valid - present
 * @param wordToCheck for the word to check 
 * @return return boolean value if lexicon conatins the word. 
 */  
   public boolean isValidWord(String wordToCheck) {
   
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      return lexicon.contains(wordToCheck);
   }
   
 /**
 * Method to check if is a valid prefix.
 * @param prefixToCheck for the prefix to check
 * @return return boolean value for valid prefix. 
 */  
   public boolean isValidPrefix(String prefixToCheck) {
   
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
      
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      
      prefixToCheck = prefixToCheck.toUpperCase();
      
      String pre = lexicon.ceiling(prefixToCheck);
     
      
      if (pre != null) {
         return pre.startsWith(prefixToCheck);
      }
      return false;
   
   }
   
   
   
  
 /**
 * Method to identify the index of words on the board
 * @param wordToCheck for the word to check
 * @return return list that is the path to the word
 */  
   public List<Integer> isOnBoard(String wordToCheck) {
   
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      
      indexes = new ArrayList<Integer>();
      wordToCheck = wordToCheck.toUpperCase();
      finalList = new ArrayList<Position>();
               
      for (int i = 0; i < n; i++) {
      
         for (int j = 0; j < n; j++) {
         
            if (wordToCheck.startsWith(board[i][j])) {
            
               notVisited();
               
               Position p =  new Position(i, j);
               
               List<Position> pos = new ArrayList<Position>();
               depthFirstSearch("", p, pos, wordToCheck);
            }
         }
      } 
      for (Position p : finalList) {
         indexes.add(index(p));
      }
      return indexes;
   }
   
   
 /**
 * Method for recursive depth first search for words on board.
 * @param check for word so far
 * @param temp temporary position to check
 * @param tempList temporary position list
 * @param wordToCheck word to check
 */   
   public void depthFirstSearch(String check, Position temp,
                   List<Position> tempList, String wordToCheck) {
                   
      if (isVisited(temp)) {
         return;
      }
      
      String word = check + board[temp.i][temp.j];
      //if the word to check is not the same as the first word, return the word
      if (!wordToCheck.startsWith(word)) {
         return;
      }
      
      //temporary list for positions 
      List<Position> newList = new ArrayList<Position>(tempList);
      newList.add(temp);
      
      //if the word equals the word, 
      if (word.equals(wordToCheck)) {
         finalList = new ArrayList<Position>(newList);
         return;
      }
      
      hasVisited(temp);
      
      for (Position b : temp.neighbors()) {
         depthFirstSearch(word, b, newList, wordToCheck);
      }
      
      unvisit(temp);
   }
   
   
   /**
 * Method to check if the Position is valid on board.
 * @param p Position to check the validity
 * @return if is valid
 */  
   private boolean isValid(Position pos) {
      return (pos.i >= 0 && pos.i < n && pos.j >= 0 && pos.j < n);
   }
      
 /**
 * Method to mark the Position as visited.
 * @param p Position to mark
 */  
   private boolean hasVisited(Position pos) {
      return (pos.i >= 0 && pos.i < n && pos.j >= 0 && pos.j < n);
   }
 
 /**
 * Method to mark the Position as unvisited.
 * @param p Position to mark
 */        
   private void unvisit(Position pos) {
      done[pos.i][pos.j] = false;
   }
         
    
 /**
 * Method to check if a position is visited.
 * @param p position to check
 * @return if it is visited
 */  
   public boolean isVisited(Position pos) {
      return done[pos.i][pos.j];
   }

 /**
 * Method top calculate the index of a position on board.
 * @param p position to find the index
 * @return the index
 */  
   public Integer index(Position pos) {
      return (Integer) n * pos.i + pos.j;
   }
   
   
 /**
 * Nested class for positions
 */  
   private class Position {
   
      private int i;
      private int j;
      
   /**
   * constructor positions
   * @param iIn row of position
   * @param jIn column of position
   */  
      Position(int i, int j) {
         this.i = i;
         this.j = j;
      }
      
      public String toString() {
         return "(" + i + "," + j + ")";
      }
         
   /**
   * Method for valid neighbor positions
   * @return an array of valid neighbors
   */  
      public Position[] neighbors() {
      
      //create an array of the 8 neighbor positions 
         Position[] neighbors = new Position[8];
         int index = 0;
         
         for (int x = i - 1; x <= i + 1; x++) {
         
            for (int y = j - 1; y <= j + 1; y++) {
            
               if (!(x == i && y == j)) {
                  Position pos = new Position(x, y);
                  
                  if (isValid(pos)) {
                     neighbors[index++] = pos;
                  }
               }
            }
         }
         return Arrays.copyOf(neighbors, index);
      
      }
      
   }


}
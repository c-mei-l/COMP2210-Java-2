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
 * Program for boggle game. 
 *
 * Courtney Lee - cml0082@auburn.edu
 * 8 July 2021
 */ 
 
public class Game implements WordSearchGame {

//fields
   private String[][] board;
   private boolean[][] visited;
   private List<Integer> indexes;
   //private List<Position> pos;
   private Deque<Position> stack;
   private int n; //width & height
   private TreeSet<String> lexicon;
   private SortedSet<String> matched;
   private List<Position> finalList;   
        
//constructor
/**
* constructor for Game.
*/
   public Game() {
   
   }

//methods

/**
* load lexicon from a file.
* @param fileName name of file to load
*/
   public void loadLexicon(String fileName) {
   
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      lexicon = new TreeSet<String>();
      try {
         File file = new File(fileName);
         Scanner scanFile = new Scanner(file);
         while (scanFile.hasNext()) {
            lexicon.add(scanFile.next().toUpperCase());
         }
      }
      catch (FileNotFoundException ex) {
         throw new IllegalArgumentException();
      }
   }
   
/**
* method to set letters on the board.
* @param letterArray array of letters to set on board
*/
   public void setBoard(String[] letterArray) {
      if (letterArray == null) { 
         throw new IllegalArgumentException();
      }
      int length = letterArray.length;
      if (!(Math.abs(Math.sqrt(length) - (int) Math.sqrt(length)) 
         < 0.00000001)) {
         throw new IllegalArgumentException();
      }
         
      n = (int) Math.sqrt(length);
      board = new String[n][n];
      visited = new boolean[n][n];
      int index = 0;
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            board[i][j] = letterArray[index++];
         }
      }
      clearV();
   }
   
/**
* method to print the board.
* @return return the board
*/
   public String getBoard() {
      String print = "";
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            print = print + board[i][j];
         }
         print = print + "\n";
      }
      return print;
   }
   
/**
* get all valid words on board with a minimun length.
* @param minimumWordLength minimum length of word wanted
* @return return the set of all valid word
*/
   public SortedSet<String> getAllScorableWords(int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      matched = new TreeSet<String>();
      
      SortedSet<String> longEnough = new TreeSet<String>();
      for (String s : lexicon) {
         if (s.length() >= minimumWordLength && isOnBoard(s).size() != 0) {
            matched.add(s.toUpperCase());
         }
      } 
      
      return matched;
   }
   
  

/**
* calculate score for words gotten.
* @param words set of words gotten
* @param minimumWordLength minimum length of word wanted
* @return return the score
*/
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      int score = 0;
      for (String word : words) {
         if (word.length() >= minimumWordLength) {
            score = score + word.length() - minimumWordLength + 1;
         }
      }
      return score;
   }

 /**
 * check if is a valid word.
 * @param wordToCheck word to check
 * @return return if it is valid
 */  
   public boolean isValidWord(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null || lexicon.size() == 0) {
         throw new IllegalStateException();
      }
      return lexicon.contains(wordToCheck);
   }
   
 /**
 * check if is a valid prefix.
 * @param prefixToCheck prefix to check
 * @return return if is a valid prefix
 */  
   public boolean isValidPrefix(String prefixToCheck) {
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null || lexicon.size() == 0) {
         throw new IllegalStateException();
      }
      String ceil = lexicon.ceiling(prefixToCheck);
      if (ceil == null) {
         return false;
      }
      return ceil.startsWith(prefixToCheck);
   
   }
  
 /**
 * check and get list of indeces the word on board.
 * @param wordToCheck the word to check
 * @return return list of indeces
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
               
      for (int x = 0; x < n; x++) {
         for (int y = 0; y < n; y++) {
            if (wordToCheck.startsWith(board[x][y])) {
               clearV();
               Position p =  new Position(x, y);
               List<Position> pos = new ArrayList<Position>();
               dfs("", p, pos, wordToCheck);
            }
         }
      } 
      for (Position p : finalList) {
         indexes.add(index(p));
      }
      return indexes;
   }
   
   /**
   * recursice dfs for check word on board.
   * @param wsf word so far
   * @param temp temporary position to check
   * @param tempList temporary position list
   * @param wordToCheck word to check
   */   
   public void dfs(String wsf, Position temp,
      List<Position> tempList, String wordToCheck) {
      if (isVisited(temp)) {
         return;
      }
      String word = wsf + board[temp.x][temp.y];
      if (!wordToCheck.startsWith(word)) {
         return;
      }
      List<Position> newList = new ArrayList<Position>(tempList);
      newList.add(temp);
      if (word.equals(wordToCheck)) {
         finalList = new ArrayList<Position>(newList);
         return;
      }
      visit(temp);
      //System.out.println(newList);
      for (Position nb : temp.neighbors()) {
         dfs(word, nb, newList, wordToCheck);
      }
      unvisit(temp);
   }
   
   
  
      
   
 /**
 * check if the Position is valid on board.
 * @param p Position to check
 * @return return if is valid
 */  
   public boolean isValid(Position p) {
      return (p.x >= 0 && p.x < n && p.y >= 0 && p.y < n);
   }
      
 /**
 * mark the Position visited.
 * @param p Position to mark
 */  
   public void visit(Position p) {
      visited[p.x][p.y] = true;
   }
 
 /**
 * mark the Position unvisited.
 * @param p Position to mark
 */        
   private void unvisit(Position p) {
      visited[p.x][p.y] = false;
   }
         
 /**
 * mark all position unvisited.
 */  
   public void clearV() {
      visited = new boolean[n][n];
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }
    
 /**
 * check if a position is visited.
 * @param p position to check
 * @return return if is visited
 */  
   public boolean isVisited(Position p) {
      return visited[p.x][p.y];
   }

 /**
 * calculate the index of a position on board.
 * @param p position to calculate
 * @return return the index
 */  
   public Integer index(Position p) {
      return (Integer) n * p.x + p.y;
   }


 /**
 * nested class for build a position.
 */  
   private class Position {
   
      private int x;
      private int y;
      
   /**
   * constructor for build a position.
   * @param x row of position
   * @param y column of position
   */  
      Position(int xx, int yy) {
         x = xx;
         y = yy;
      }
         
   /**
   * get all valid neighbors for a position.
   * @return return the list of neighbors
   */  
      public Position[] neighbors() {
         Position[] neighbors = new Position[8];
         int index = 0;
         for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
               if (!(i == x && j == y)) {
                  Position p = new Position(i, j);
                  if (isValid(p)) {
                     neighbors[index++] = p;
                  }
               }
            }
         }
         return Arrays.copyOf(neighbors, index);
      
      }
      
      public String toString() {
         return "(" + x + "," + y + ")";
      }
   }
}
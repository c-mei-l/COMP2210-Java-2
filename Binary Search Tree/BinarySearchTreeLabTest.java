/**
 * Lab Test problem on binary search trees.
 *
 */
public class BinarySearchTreeLabTest {

   /** Provides an example. */
   public static void main(String[] args) {
      BinarySearchTree<Integer> iBst = new BinarySearchTree<>();
      iBst.add(10);
      iBst.add(12);
      iBst.add(8);
      iBst.add(2);
      iBst.add(6);
      iBst.add(4);
      int depth = iBst.depth(7);
      // The following statement should print -1.
      System.out.println(depth);
      depth = iBst.depth(10);
      // The following statement should print 1.
      System.out.println(depth);
      depth = iBst.depth(6);
      // The following statement should print 4.
      System.out.println(depth);
   
      BinarySearchTree<String> sBst = new BinarySearchTree<>();
      sBst.add("W");
      sBst.add("A");
      sBst.add("R");
      sBst.add("E");
      sBst.add("A");
      sBst.add("G");
      sBst.add("L");
      sBst.add("E");
      depth = sBst.depth("W");
      // The following statement should print 1.
      System.out.println(depth);
   
      depth = sBst.depth("A");
      // The following statement should print 2.
      System.out.println(depth);
   
      depth = sBst.depth("G");
      // The following statement should print 5.
      System.out.println(depth);
   }



   /** Defines a binary search tree. */
   static class BinarySearchTree<T extends Comparable<T>> {
   
      // the root of this binary search tree
      private Node root;
   
      // the number of nodes in this binary search tree
      private int size;
   
      /** Defines the node structure for this binary search tree. */
      private class Node {
         T element;
         Node left;
         Node right;
      
         /** Constructs a node containing the given element. */
         public Node(T elem) {
            element = elem;
            left = null;
            right = null;
         }
      }
   
   
      /* >>>>>>>>>>>>>>>>>> YOUR WORK STARTS HERE <<<<<<<<<<<<<<<< */
   
   
      ///////////////////////////////////////////////////////////////////////////////
      //      I M P L E M E N T   T H E   D E P T H   M E T H O D   B E L O W      //
      ///////////////////////////////////////////////////////////////////////////////
   
      /**
       * Returns the depth of the node containing value 
       * or -1 if value not present.
       */
      public int depth(T value) {
      
         if (root == null || size == 0) {
            return -1;
         }
      
         int depth = 1;
         Node n = root;
         Node parent = null;
         int comp = 0;
      
      //if the target is at the root value
         if (value.compareTo(root.element) == 0) {
            return depth;  
         }
         
         // find target each time parent moves, depth increases
         else {
            while (n != null) {
               parent = n;
               comp = value.compareTo(parent.element);
            
               if (comp == 0) {
                  return depth;
               }
               
               else if (comp < 0) {
                  n = n.left;
                  depth ++;
               }
               
               else {
                  n = n.right;
                  depth ++;
               }
            }
         }
      
         return -1;
      }
   
   
      /* >>>>>>>>>>>>>>>>>> YOUR WORK ENDS HERE <<<<<<<<<<<<<<<< */
   
   
   
      ////////////////////////////////////////////////////////////////////
      //  D O   N O T   M O D I F Y   B E L O W   T H I S   P O I N T   //
      ////////////////////////////////////////////////////////////////////
   
   
   
      ////////////////////
      // M E T R I C S  //
      ////////////////////
   
      /**
       * Returns the number of elements in this bst.
       */
      public int size() {
         return size;
      }
   
      /**
       * Returns true if this bst is empty, false otherwise.
       */
      public boolean isEmpty() {
         return size == 0;
      }
   
      /**
       * Returns the height of this bst.
       */
      public int height() {
         return height(root);
      }
   
      /**
       * Returns the height of node n in this bst.
       */
      private int height(Node n) {
         if (n == null) {
            return 0;
         }
         int leftHeight = height(n.left);
         int rightHeight = height(n.right);
         return 1 + Math.max(leftHeight, rightHeight);
      }
   
   
      ////////////////////////////////////
      // A D D I N G   E L E M E N T S  //
      ////////////////////////////////////
   
      /**
       * Ensures this bst contains the specified element. Uses an iterative implementation.
       */
      public void add(T element) {
         // special case if empty
         if (root == null) {
            root = new Node(element);
            size++;
            return;
         }
      
         // find where this element should be in the tree
         Node n = root;
         Node parent = null;
         int cmp = 0;
         while (n != null) {
            parent = n;
            cmp = element.compareTo(parent.element);
            if (cmp == 0) {
               // don't add a duplicate
               return;
            } else if (cmp < 0) {
               n = n.left;
            } else {
               n = n.right;
            }
         }
      
         // add element to the appropriate empty subtree of parent
         if (cmp < 0) {
            parent.left = new Node(element);
         } else {
            parent.right = new Node(element);
         }
         size++;
      }
   
   }

}

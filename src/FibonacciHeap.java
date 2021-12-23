/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    private HeapNode head = null;
    private HeapNode tail = null;
    private HeapNode min = null;
    private int size = 0;
    private int treeCount = 0;
    private int markedCount = 0;
    private int linkedCount = 0;
    private int cutsCount = 0;

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return false; // should be replaced by student code
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {    
    	return new HeapNode(key); // should be replaced by student code
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
     	return; // should be replaced by student code
     	
    }

    /**
     *
     */
    private void consolidate() {

    }

    /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	return new HeapNode(678);// should be replaced by student code
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return -123; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

    /**
     *
     */
    private void cascadingCuts(HeapNode x, HeapNode y) {

    }

    /**
     *
     */
    private void cut(HeapNode x, HeapNode y) {

    }


    /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
    	private String info;
    	private int rank = 0;
    	private boolean mark = false;
    	private HeapNode child = null;
    	private HeapNode next = null;
    	private HeapNode prev = null;
    	private HeapNode parent = null;
    	private boolean isRoot = false;

    	public HeapNode(int key) {
    	    this.key = key;
    	}

       /**
        * return node key
        * complexity : O(1)
        */
    	public int getKey() {
    		return this.key;
    	}

       /**
        * update node key to x
        * complexity : O(1)
        */
    	public void setKey(int x) {
    	    this.key = x;
        }

       /**
        * return node info
        * complexity : O(1)
        */
       public String getValue() {
           return this.info;
       }

       /**
        * return node rank
        * complexity : O(1)
        */
       public int getRank() {
           return this.rank;
       }

       /**
        * update node rank to r
        * complexity : O(1)
        */
       public void setRank(int r) {
           this.rank = r;
       }

       /**
        * return node mark
        * complexity : O(1)
        */
       public boolean getMark() {
           return this.mark;
       }

       /**
        * update node mark to b
        * complexity : O(1)
        */
       public void setMark(boolean b) {
           this.mark = b;
       }

       /**
        * return node child
        * complexity : O(1)
        */
       public HeapNode getChild() {
           return this.child;
       }

       /**
        * update node child to x
        * complexity : O(1)
        */
       public void setChild(HeapNode x) {
           this.child = x;
       }

       /**
        * return node next (right brother)
        * complexity : O(1)
        */
       public HeapNode getNext() {
           return this.next;
       }

       /**
        * update node next (right brother)
        * complexity : O(1)
        */
       public void setNext(HeapNode x) {
           this.next = x;
       }

       /**
        * return node prev (left brother)
        * complexity : O(1)
        */
       public HeapNode getPrev() {
           return this.prev;
       }

       /**
        * update node prev (left brother)
        * complexity : O(1)
        */
       public void setPrev(HeapNode x) {
           this.prev = x;
       }

       /**
        * return node parent
        * complexity : O(1)
        */
       public HeapNode getParent() {
           return this.parent;
       }

       /**
        * update node parent to p
        * complexity : O(1)
        */
       public void setParent(HeapNode p) {
           this.parent = p;
       }

       /**
        * return node isRoot
        * complexity : O(1)
        */
       public boolean getIsRoot() {
           return this.isRoot;
       }

       /**
        * update node isRoot to b
        * complexity : O(1)
        */
       public void setIsRoot(boolean b) {
           this.isRoot = b;
       }

   }
}

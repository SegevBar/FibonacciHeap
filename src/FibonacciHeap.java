/**
 * Submiters Info:
 *
 * name : Bar Pakula
 * ID : 316294297
 * moodle user : barpakula
 *
 * name : Bar Segev
 * ID : 312456262
 * moodle user : barsegev
 *
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {
    private HeapNode head = null;
    private HeapNode tail = null;
    private HeapNode min = null;
    private int size = 0;
    private int treeCount = 0;
    private int markedCount = 0;
    private static int linkedCount = 0;
    private static int cutsCount = 0;

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *
    * complexity : O(1)
    */
    public boolean isEmpty() {
    	if (this.head == null) {
    	    return true;
        }
    	return false;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    *
    * complexity : O(1)
    */
    public HeapNode insert(int key) {
    	HeapNode newHeapNode = new HeapNode(key);

    	//update tail if heap is currently empty
        if (this.isEmpty()) {
            this.tail = newHeapNode;
        }

    	//insert node to head of linked roots list
        newHeapNode.setNext(this.head);
        if (this.head != null) {
            this.head.setPrev(newHeapNode);
        }
        this.head = newHeapNode;

        newHeapNode.setIsRoot(true);  //update node to be a root
        this.size++;  //update heap size + 1
        this.treeCount++;  //update number of trees + 1

        //update min if necessary
        if (this.min == null) {
            this.min = newHeapNode;
        } else if (key < this.min.getKey()) {
            this.min = newHeapNode;
        }
    	return newHeapNode;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    * complexity : O(n)
    */
    public void deleteMin() {
        if (this.min != null) {
            HeapNode currChild = this.min.getChild();

            //if min has a child (or children) - connect it (them) to root list
            if (currChild != null) {
                //connect left child to left brother of min
                currChild.setPrev(this.min.getPrev());
                if (this.min.getPrev() != null) {
                    this.min.getPrev().setNext(currChild);
                }

                //update min children to be independent trees
                boolean seenHead = true;
                boolean seenTail = true;
                if (this.min == this.head) {
                    seenHead = false;
                }
                if (this.min == this.tail) {
                    seenTail = false;
                }
                while (currChild != null) {
                    if (!seenHead) {
                        this.head = currChild;
                        seenHead = true;
                    }
                    currChild.setParent(null);
                    currChild.setIsRoot(true);
                    if (currChild.getMarked()) {
                        currChild.setMarked(false);
                        this.markedCount--;
                    }
                    if (!currChild.hasNext()) {
                        if (!seenTail) {
                            this.tail = currChild;
                        }
                        break;
                    }
                    currChild = currChild.getNext();
                }

                //connect right child of min to min next
                currChild.setNext(this.min.getNext());
                if (this.min.getNext() != null) {
                    this.min.getNext().setPrev(currChild);
                }

            }
            else {  //if min has no children - connect its left and right brothers unless they are the head to tail.
                if (this.min != this.head) { //min has a left brother
                    this.min.getPrev().setNext(this.min.getNext());
                } else {  //update new head
                    this.head = this.min.getNext();
                }
                if (this.min != this.tail) { //min has a right child
                    this.min.getNext().setPrev(this.min.getPrev());
                } else { //update new tail
                    this.tail = this.min.getPrev();
                }
            }

            //disconnect all min pointers
            this.min.setChild(null);
            this.min.setPrev(null);
            this.min.setNext(null);

            //consolidate heap
            if (!(this.isEmpty())) {
                this.consolidate();
            }

            //update heap size, heap tree count, and min
            this.size--;
            this.treeCount = 0;
            HeapNode currMin = this.head;
            HeapNode curr = this.head;
            while (curr != null) {
                if (curr.getKey() < currMin.getKey()) { //update current minimum if key is smaller
                    currMin = curr;
                }
                this.treeCount++;
                curr = curr.getNext();
            }
            this.min = currMin;  //update heap minimum to point currMin
        }
    }

    /**
     * private void consolidate()
     *
     * Successive Linking process on heap after deleteMin
     * pre : heap is not empty
     *
     * complexity : O(n)
     */
    private void consolidate() {
        //create node array size log(n+1)
        int arraySize = (int) Math.ceil(Math.log(this.size+1)/Math.log(2));
        HeapNode[] buckets = new HeapNode[arraySize];

        //Successive Linking
        HeapNode curr = this.head;
        while (curr != null) {
            HeapNode nextTree = curr.getNext();
            curr.setPrev(null);
            int currRank = curr.getRank();

            //if the i place in the array is not null - link the trees. smaller key is the root.
            while (currRank < buckets.length-1 && buckets[currRank] != null) {
                HeapNode parent;
                HeapNode child;
                if (buckets[currRank].getKey() < curr.getKey()) {
                    parent = buckets[currRank];
                    child = curr;
                } else {
                    parent = curr;
                    child = buckets[currRank];
                }
                //update nodes fields - parent and child pointers, rank and isRoot flag
                parent.setIsRoot(true);
                child.setIsRoot(false);
                child.setNext(parent.getChild());
                if (parent.getChild() != null) {
                    parent.getChild().setPrev(child);
                }
                child.setParent(parent);
                parent.setChild(child);

                parent.setRank(parent.getRank() + 1);

                //update array
                curr = parent;
                buckets[currRank] = null;
                currRank = parent.getRank();

                //update links counter
                this.linkedCount++;
            }
            buckets[currRank] = curr;
            curr = nextTree;

        }

        //build the new linked list
        HeapNode currTree = buckets[0];
        HeapNode nextTree;
        boolean seenHead = false;

        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                if (!seenHead) {
                    currTree = buckets[i];
                    this.head = currTree;
                    currTree.setPrev(null);
                    currTree.setParent(null);
                    seenHead = true;
                } else {
                    nextTree = buckets[i];
                    nextTree.setParent(null);
                    currTree.setNext(nextTree);
                    nextTree.setPrev(currTree);
                    currTree = nextTree;
                }
            }
        }
        this.tail = currTree;
        currTree.setNext(null);
    }

    /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
     * complexity : O(1)
    */
    public HeapNode findMin() {
    	return this.min;
    }

    /**
     * public HeapNode getHead()
     *
     * return the head of linked list
     *
     * complexity : O(1)
     */
    public HeapNode getHead() {
        return this.head;
    }

    /**
     * public HeapNode getTail()
     *
     * return the tail of linked list
     *
     * complexity : O(1)
     */
    public HeapNode getTail() {
        return this.tail;
    }

    /**
     * public int getTreeCount()
     *
     * return the number of trees in the heap
     *
     * complexity : O(1)
     */
    public int getTreeCount() {
        return this.treeCount;
    }

    /**
     * public int getMarkedCount()
     *
     * return the number of marked nodes
     *
     * complexity : O(1)
     */
    public int getMarkedCount() {
        return this.markedCount;
    }

    /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    * complexity : O(1)
    */
    public void meld (FibonacciHeap heap2) {
        //if current heap is empty - update head and min
        if (this.isEmpty()) {
            this.head = heap2.getHead();
            this.min = heap2.findMin();
        } else {
            //link the tail of the current list to the head of heap2
            this.tail.setNext(heap2.getHead());
        }
        //if heap2 not empty - connect heap2 head to current heap tail and check min
        if (!heap2.isEmpty()) {
            heap2.getHead().setPrev(this.tail);

            //update min to be heap2 min if key is smaller
            if (heap2.findMin().getKey() < this.min.getKey()) {
                this.min = heap2.findMin();
            }
        }
        this.tail = heap2.getTail();  //update heap tail to heap2 tail
        this.size += heap2.size();  //update heap size
        this.treeCount += heap2.getTreeCount();  //update heap tree count
        this.markedCount += heap2.getMarkedCount();  //update heap marked nodes count
    }


    /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *
    * complexity : O(1)
    */
    public int size() {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    *
     * complexity : O(n)
    */
    public int[] countersRep() {
    	//if the heap is empty return empty array
        if (this.isEmpty()) {
    	    return new int[0];
        }
        //find the maximum rank in the heap
        int maxRank = 0;
        HeapNode curr = this.head;
        while (curr != null) {
            if (curr.getRank() > maxRank) {
                maxRank = curr.getRank();
            }
            curr = curr.getNext();
        }
        //build the counter - iterate throw heap trees and increment i index of array if current tree rank is i
        int[] counterRep = new int[maxRank+1];
        curr = this.head;
        while (curr != null) {
            int currRank = curr.getRank();
            counterRep[currRank]++;
            curr = curr.getNext();
        }
        return counterRep;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    * complexity : O(n)
    */
    public void delete(HeapNode x) {
        if (x != null) {
            this.decreaseKey(x, x.getKey() + this.min.getKey() + 10); //update x to be the minimum root
            this.deleteMin();  //delete minimum and consolidate the heap
        }
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    *
    * complexity : O(log n)
    */
    public void decreaseKey(HeapNode x, int delta) {
        if (x != null) {
            x.setKey(x.getKey() - delta);  //decrease x key by delta
            //if the heap is illegal - use cascading cuts
            if (!x.getIsRoot() && x.getParent().getKey() > x.getKey()) {
                this.cascadingCuts(x, x.getParent());
            } else {
                if (x.getIsRoot() && x.getKey() < this.min.getKey()) {
                    this.min = x;
                }
            }
        }
    }

    /**
     * private void cascadingCuts(HeapNode x, HeapNode y)
     *
     * if child is smaller than parent- cut child from tree
     * if parent is not a root and is not marked - mark it. else - call recursively to cascadingCuts
     * @pre: x != null && y != null
     *
     * complexity : O(log n)
     */
    private void cascadingCuts(HeapNode x, HeapNode y) {
        this.cut(x,y);  //cut x from its parent y

        if (!(y.getIsRoot())) {  //mark y if it is not a root
            //if y is not marked - mark it
            if (y.getMarked() == false) {
                y.setMarked(true);
                this.markedCount++;
            }
            //if y is already marked - call cascading cur recursively
            else {
                this.cascadingCuts(y, y.getParent());
            }
        }
    }

    /**
     * private void cut(HeapNode x, HeapNode y)
     *
     * cut x from y
     * @pre: x != null && y != null
     *
     * complexity : O(1)
     */
    private void cut(HeapNode x, HeapNode y) {
        cutsCount++;
        //x becomes a root - update fields accordingly
        this.treeCount++;  //update treeCount
        if (x.getMarked()) {  //if x was marked - turn it off and update markedCount of the heap
            x.setMarked(false);
            this.markedCount--;
        }
        x.setIsRoot(true);
        x.setParent(null);

        //update min if x key is smaller than min key
        if (x.getKey() < this.min.getKey()) {
            this.min = x;
        }

        //update y child pointer and x brothers pointers
        if (y.getRank() == 1) {
            y.setChild(null);
        } else {  //y child points to x
            if (y.getChild() == x) {
                y.setChild(x.getNext());
                x.getNext().setPrev(null);
            }
            else {
                x.getPrev().setNext(x.getNext());
                if (x.hasNext()) {
                    x.getNext().setPrev(x.getPrev());
                }
            }
        }
        y.setRank(y.getRank()-1);  //update y rank
        //update x pointer
        x.setNext(this.head);
        this.head.setPrev(x);
        x.setPrev(null);

        this.head = x;
    }


    /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap.
     *
     * complexity : O(1)
    */
    public int potential() {
    	return this.treeCount + 2 * this.markedCount;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    *
    * complexity : O(1)
    */
    public static int totalLinks() {
    	return linkedCount;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods).
    *
    * complexity : O(1)
    */
    public static int totalCuts() {
    	return cutsCount;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H.
    *
    * complexity : O(k deg(H))
    */
    public static int[] kMin(FibonacciHeap H, int k) {
        if (k == 0 || H.isEmpty()) {
            return new int[0];
        }

        FibonacciHeap heapForKMin = new FibonacciHeap();
        int[] kMinNodes = new int[k];

        HeapNode currMin; // hold the node in H with the same key as the min in heapForKMin.
        HeapNode minChild; // hold one of the children of tempMin.
        HeapNode temp; // hold the last inserted node to heapForKMin.

        temp = heapForKMin.insert(H.findMin().getKey());
        temp.setkMin(H.findMin());

        for (int i = 0; i < k; i++) {
            currMin = heapForKMin.findMin().getkMin();
            kMinNodes[i] = currMin.getKey();
            heapForKMin.deleteMin();
            minChild = currMin.getChild();

            while (minChild != null) {
                temp = heapForKMin.insert(minChild.getKey());
                temp.setkMin(minChild);
                minChild = minChild.getNext();
            }
        }
        return kMinNodes;
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
    	private HeapNode kMin = null;

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
       public boolean getMarked() {
           return this.mark;
       }

       /**
        * update node mark to b
        * complexity : O(1)
        */
       public void setMarked(boolean b) {
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
        * return true if next != null
        * complexity : O(1)
        */
       public boolean hasNext() {
           return this.next != null;
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

       /**
        * return node kMin
        * complexity : O(1)
        */
       public HeapNode getkMin() {
           return this.kMin;
       }

       /**
        * update node kMin to node
        * complexity : O(1)
        */
       public void setkMin(HeapNode node) {
           this.kMin = node;
       }

   }
}

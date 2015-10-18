/* NetId(s): gk368. Time spent: 05 hours, 30 minutes.

 * Name(s): Gaurav Keswani
 * What I thought about this assignment: It was a nice assignment for exposure
 * to trees except that it had too much stuff around it which took more time
 * to understand than it took to complete the assignment. I was not able to do
 * justice to the assignment since I had two huge interviews in the week but I
 * had fun solving it.
 */ 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** An instance of DiseaseTree represents the spreading of a Disease among a Network of people.
 * In this model, each person can "catch" the disease from only a single person.
 * The  root of the DiseaseTree is the person who first got the disease. From there,
 * each person in the DiseaseTree is the child of the person who gave them the disease.
 * For example, for the tree:
 * 
 *        A
 *       / \
 *      B   C
 *         / \
 *        D   E
 * 
 * Person A originally got the disease, B and C caught the disease from A,
 *  and D and E caught the disease from C.
 *  
 *  Important note: The name of each person in the disease tree is unique. 
 * @author Mshnik */
public class DiseaseTree {

    /** The String to be used as a separator in toString() */
    public static final String SEPARATOR= " - ";

    /** The String that marks the start of children in toString() */
    public static final String START_CHILDREN_DELIMITER= "[";

    /** The String that divides children in toString() */
    public static final String DELIMITER= ", ";

    /** The String that marks the end of children in toString() */
    public static final String END_CHILDREN_DELIMITER= "]";

    /** The String that is the space increment in toStringVerbose() */
    public static final String VERBOSE_SPACE_INCREMENT= "\t";

    /**  The person at the root of this DiseaseTree.
     * This is the disease ancestor of everyone in this DiseaseTree, the
     * person who got sick first and indirectly caused everyone in it to get sick.
     * root is non-null. 
     * All Person's in a DiseaseTree have different names. There are no duplicates
     * */
    private Person root;

    /** The immediate children of this DiseaseTree node.
     * Each element of children got the disease from the person at this node.
     * root is non-null but will be an empty set if this is a leaf. */
    private Set<DiseaseTree> children;

    /** Constructor: a new DiseaseTree with root p and no children.
     * Throw an IllegalArgumentException if p is null. */
    public DiseaseTree(Person p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException("Can't construct DiseaseTree with null root");
        root= p;
        children= new HashSet<>();
    }

    /** Constructor: a new DiseaseTree that is a copy of tree p.
     * Tree p and its copy have no node in common (but nodes can share a Person).
     * Throw an IllegalArgumentException if p is null.  */
    public DiseaseTree(DiseaseTree p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException("Can't construct DiseaseTree as copy of null");
        root= p.root;
        children= new HashSet<>();

        for (DiseaseTree dt : p.children) {
            children.add(new DiseaseTree(dt));
        }
    }

    /** Return the person that is at the root of this DiseaseTree */
    public Person getRoot() {
        return root;
    }

    /** Return the number of DiseaseTrees that are direct children of this DiseaseTree */
    public int getChildrenCount() {
        return children.size();
    }

    /** Return a COPY of the set of children of this DiseaseTree. */
    public Set<DiseaseTree> getChildren(){
        return new HashSet<>(children);
    }
    
    /** Add c to this DiseaseTree as a child of p
     * and return the DiseaseTree whose root is the new child.
     * Throw an IllegalArgumentException if:<br>
     *      -- p or c is null,<br>
     *      -- c is already in this DiseaseTree, or<br>
     *      -- p is not in this DiseaseTree */
    public DiseaseTree add(Person p, Person c) throws IllegalArgumentException {
    	//TODO 1
    	if (p == null || c == null)
    		throw new IllegalArgumentException("Can't construct DiseaseTree as copy of null or with null child");
    	if (children.contains(c) == true)
    		throw new IllegalArgumentException("c is already a child of p");
    	 	 
    	//Finding the disease tree of person p. If it does not exist, we throw an exception
    	DiseaseTree parent = getTree(p);
    	if (parent == null)
    		throw new IllegalArgumentException("p is not in this disease tree");
    	
    	//Creating the child disease tree and adding it to the parent
    	DiseaseTree child = new DiseaseTree(c);
    	parent.children.add(child);

        return child;
    }


    /** Return the number of people contained in this DiseaseTree.
     * Note: If this is a leaf, the size is 1 (just the root) */
    public int size(){
    	//TODO 2
        return counter(this, 0);
    }

    /**
     * Recursive method to count the total number of people in the 
     * disease tree
     * @param d
     * 		the current disease tree being looked at
     * @param count
     * 		the count of nodes uptill now
     * @return
     * 		the number of people in the recursive tree
     */
    private int counter(DiseaseTree d, int count) {
    	if(d.root != null) {
    		count = 1;
    		for(DiseaseTree c: d.children)
    			count += counter(c, count);
    	}
    	return count;
    }
    
    /** Return the depth at which p occurs in this DiseaseTree, or -1
     * if p is not in the DiseaseTree.
     * Note: depthOf(root) is 0.
     * If p is a child of this DiseaseTree, then depthOf(p) is 1. etc. */
    public int depthOf(Person p){
        //TODO 3
        return searchPersonInCurrentDiseaseTree(this, p, 0);
    }
    
    public int searchPersonInCurrentDiseaseTree(DiseaseTree current, Person p, int depth){
    	if(current.root == p) 
    		return depth;
    	
    	for (DiseaseTree dt : current.children) 
    		if (dt.contains(p)) 
    			return searchPersonInCurrentDiseaseTree(dt, p, depth+1);
    	
    	//Reaches here only if the person is not found in the tree
    	return -1;
    }

    /** If p is in this tree, return the DiseaseTree object in this tree that
     * contains p. If p is not in this tree, return null.
     * 
     * Example: Calling getTree(root) should return this. */
    public DiseaseTree getTree(Person p){
        if (root == p) return this; //Base case - look here

        // Recursive case - ask children to look
        for (DiseaseTree dt : children) {
            DiseaseTree search= dt.getTree(p);
            if (search != null) return search;
        }

        return null; // Not found
    }

    /** Return true iff this DiseaseTree contains p. */
    public boolean contains(Person p){
        /* Note: This DiseaseTree contains p iff the root of this DiseaseTree is
         * p or if one of p's children contains p. */
        if (root == p) return true;
        for (DiseaseTree dt : children) {
            if (dt.contains(p)) return true;
        }
        return false;
    }


    /** Return the maximum depth of this DiseaseTree, i.e. the longest path from
     * the root to a leaf. Example. If this DiseaseTree is a leaf, return 0. */
    public int maxDepth() {
        int maxDepth= 0;
        for (DiseaseTree dt : children) {
            maxDepth= Math.max(maxDepth, dt.maxDepth() + 1);
        }
        return maxDepth;
    }

    /** Return the width of this tree at depth d (i.e. the number of diseaseTrees that
     * occur at depth d, where the depth of the root is 0. 
     * Throw an IllegalArgumentException if depth < 0.
     * Thus, for the following tree :
     * Depth level:
     *       0       A
     *              / \
     *       1     B   C
     *            /   / \
     *       2   D   E   F
     *                \
     *       3         G
     * 
     * A.widthAtDepth(0) = 1,  A.widthAtDepth(1) = 2,
     * A.widthAtDepth(2) = 3,  A.widthAtDepth(3) = 1,
     * A.widthAtDepth(4) = 0. 
     * C.widthAtDpth(0) = 1,   C.widthAtDepth(1) = 2
     * */
    public int widthAtDepth(int d) throws IllegalArgumentException {
    	//TODO 4
    	if(depthOf(this.root) == d)
    		return 1;
        return getChildrenCountAtDepth(this, d, 0);
    }
    
    /**
     * Recursive method to calculate the total number of children that are at
     * depth d
     * @param current
     * @param d
     * @param count
     * @return
     * 		Number of children at depth d
     */
    private int getChildrenCountAtDepth(DiseaseTree current, int d, int count){
    	    	
    	for(DiseaseTree dt: current.children) {
    		if(depthOf(current.root) == d-1) {
        		count += 1;
    		}
    		else
    			count = getChildrenCountAtDepth(dt, d, count);
    	}
    	return count;
    }

    /** Return the maximum width of all the widths in this tree, i.e. the
     * maximum value that could be returned from widthAtDepth for this tree. */
    public int maxWidth() {
        return maxWidthImplementationOne(this);
    }

    // Simple implementation of maxWith. Relies on widthAtDepth.
    // Takes time proportional the the square of the size of the t.
    static int maxWidthImplementationOne(DiseaseTree t) {
        int width= 0;
        int depth= t.maxDepth();
        for (int i= 0; i <= depth; i++) {
            width= Math.max(width, t.widthAtDepth(i));
        }
        return width;
    }

    /* Better implementation of maxWidth. Caches results in an array.
     * Takes time proportional to the size of t. */
    static int maxWidthImplementationTwo(DiseaseTree t) {
        // For each integer d, 0 <= d <= maximum depth of t, store in
        // widths[d] the number of nodes at depth d in t.
        // The calculation is done by calling recursive procedure addToWidths.
        int[] widths= new int[t.maxDepth() + 1];   // initially, contains 0's
        t.addToWidths(0, widths);
        
        int max= 0;
        for (int width : widths) {
            max= Math.max(max, width);
        }
        return max;
    }

    /* For each node of this DiseaseTree, which is at some depth d in this DiseaseTree,
     * add 1 to widths[depth + d]. */
    private void addToWidths(int depth, int[] widths) {
        widths[depth]++;        //the root of this DiseaseTree is at depth d = 0
        for (DiseaseTree dt : children) {
            dt.addToWidths(depth+1, widths);
        }
    }

    /* Better implementation of maxWidth. Caches results in a HashMap.
     * Takes time proportional to the size of t. */
    static int maxWidthImplementationThree(DiseaseTree t) {
        // For each possible depth d >= 0 in tree t, widthMap will contain the 
        // entry (d, number of nodes at depth d in t). The calculation is
        // done using recursive procedure addToWidthMap.
        
        // For each integer d, 0 <= d <= maximum depth of t, add to
        // widthMap an entry <d, 0>.
        HashMap<Integer, Integer> widthMap= new HashMap<>();
        for (int d= 0; d <= t.maxDepth() + 1; d++) {
            widthMap.put(d, 0);
        }
        
        t.addToWidthMap(0, widthMap);
        
        int max= 0;
        for (Integer w : widthMap.values()) {
            max= Math.max(max, w);
        }
        return max;
    }

    /* For each node of this DiseaseTree, which is at some depth d in this DiseaseTree,
     * add 1 to the value part of entry <depth + d, ...> of widthMap. */
    private void addToWidthMap(int depth, HashMap<Integer, Integer> widthMap) {
        widthMap.put(depth, widthMap.get(depth) + 1);  //the root is at depth d = 0
        for (DiseaseTree dt : children) {
            dt.addToWidthMap(depth + 1, widthMap);
        }
    }

    /** Return the route the disease took to get from "here" (the root of this 
     * DiseaseTree) to child c.
     * For example, for this tree:
     * 
     * Depth level:
     *       0      A
     *             / \
     *       1    B   C
     *           /   / \
     *       2  D   E   F
     *           \
     *       3    G
     * 
     * A.getDiseaseRouteTo(E) should return a list of [A,C,E].
     * A.getDiseaseRouteTo(A) should return [A].
     * A.getDiseaseRouteTo(X) should return null.
     * 
     * B.getDiseaseRouteTo(C) should return null.
     * B.getDiseaseRouteTo(D) should return [B,D]
     */
    public List<Person> getDiseaseRouteTo(Person c){
        //TODO 5
        // Hint: You have to return a List<Person>. But List is an
        // interface, so use something that implements it: ArrayList<Person>.
        //Base Case - this is the child. Route is just [child]
    	
    	List<Person> diseaseRoute = new ArrayList<Person>();
		
    	if(this.root == c) {
    		diseaseRoute.add(this.root);
    		return diseaseRoute;
    	}
    
        return DFSToFindRoute(this, c, diseaseRoute);
    }
    
    /**
     * Recursive method to perform depth first search to find the path to the 
     * target person
     * @param current
     * @param c
     * @param path
     * @return
     * 		ArrayList of the path to the person
     */
    private List<Person> DFSToFindRoute(DiseaseTree current, Person c, List<Person> path){
    	if(current.root == c){
    		path.add(c);
    		return path;
    	}
    	for(DiseaseTree dt: current.children){
    		if(dt.contains(c)){
    			path.add(current.root);
    			return DFSToFindRoute(dt, c, path);
    		}
    	}
    	return null;
    }

    /** Return the immediate parent of c (null if c is not in this
     *  DiseaseTree).
     * 
     * Thus, for the following tree:
     * Depth level:
     *       0      A
     *             / \
     *       1    B   C
     *           /   / \
     *       2  D   E   F
     *           \
     *       3    G
     * 
     * A.getParentOf(E) returns C.
     * C.getParentOf(E) returns C.
     * A.getParentOf(B) returns A.
     * E.getParentOf(F) returns null. */
    public Person getParentOf(Person c) {
        // Base case
        for (DiseaseTree dt : children) {
            if (dt.root == c) return root;
        }

        // Recursive case - ask children to look
        for (DiseaseTree dt : children) {
            Person parent= dt.getParentOf(c);
            if (parent != null) return parent;
        }

        return null; //Not found
    }

    /**  If either child1 or child2 is null or is not in this DiseaseTree, return null.
     * Otherwise, return the person at the root of the smallest subtree of this 
     * DiseaseTree that contains child1 and child2.
     * 
     * Examples. For the following tree (which does not contain H):
     * 
     * Depth level:
     *       0      A
     *             / \
     *       1    B   C
     *           /   / \
     *       2  D   E   F
     *           \
     *       3    G
     * 
     * A.getSharedAncestorOf(B, A) is A
     * A.getSharedAncestorOf(B, B) is B
     * A.getSharedAncestorOf(B, C) is A
     * A.getSharedAncestorOf(A, C) is A
     * A.getSharedAncestorOf(E, F) is C
     * A.getSharedAncestorOf(G, F) is A
     * B.getSharedAncestorOf(D, F) is null 
     * B.getSharedAncestorOf(D, H) is null 
     * A.getSharedAncestorOf(null, C) is null 
     **/
    public Person getSharedAncestorOf(Person child1, Person child2) {
        //TODO 6
    	if(child1 == child2) return child1;
    	if(child1 == null || child2 == null) return null;
    	if(getParentOf(child1) == child2) return child2;
    	if(getParentOf(child2) == child1) return child1;
    	if(getParentOf(child1) == getParentOf(child2)) return getParentOf(child1);
    	if(!this.contains(child1) || !this.contains(child2)) return null;
    				
    	return traverseTreeToFindAncestor(this, child1, child2);
    }
    
    /**
     * Recursive method to traverse the tree and find the common ancestor of the 
     * two children
     * @param current
     * @param child1
     * @param child2
     * @return
     * 		the common ancestor of type Person
     */
    private Person traverseTreeToFindAncestor(DiseaseTree current, Person child1, Person child2){
    	
    	for(DiseaseTree dt: current.children){
    		if(dt.contains(child1) && dt.contains(child2))
    			return traverseTreeToFindAncestor(dt, child1, child2);
    	}
    	return current.root;
    }

    /** Return a (single line) String representation of this DiseaseTree.
     * If this DiseaseTree has no children (it is a leaf), return the root's substring.
     * Otherwise, return
     *    root's substring + SEPARATOR + START_CHILDREN_DELIMITER + each child's 
     *    toString, separated by DELIMITER, followed by END_CHILD_DELIMITER.
     * Make sure there is not an extra DELIMITER following the last child.
     * 
     * Finally, make sure to use the static final fields declared at the top of
     * DiseaseTree.java.
     * 
     * Thus, for the following tree:
     * Depth level:
     *       0      A
     *             / \
     *       1    B  C
     *           /  / \
     *       2  D  E   F
     *           \
     *       3    G
     * A.toString() should print:
     * (A) - HEALTHY - [(C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]], (B) - HEALTHY - [(D) - HEALTHY]]
     *
     * C.toString() should print:
     * (C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]]
     */
    public String toString() {
        if (children.isEmpty()) return root.toString();
        String s= root.toString() + SEPARATOR + START_CHILDREN_DELIMITER;
        for (DiseaseTree dt : children) {
            s= s + dt.toString() + DELIMITER;
        }
        return s.substring(0, s.length() - 2) + END_CHILDREN_DELIMITER;
    }


    /** Return a verbose (multi-line) string representing this DiseaseTree. */
    public String toStringVerbose(){
        return toStringVerbose(0);
    }

    /** Return a verbose (multi-line) string representing this DiseaseTree.
     * Each person in the tree is on its own line, with indentation representing
     * what each person is a child of.
     * indent is the the amount of indentation to put before this line.
     * Should increase on recursive calls to children to create the above pattern.
     * Thus, for the following tree:
     * Depth level:
     *       0      A
     *             / \
     *       1    B   C
     *           /   / \
     *       2  D   E   F
     *           \
     *       3    G
     * 
     * A.toStringVerbose(0) should return:
     * (A) - HEALTHY
     *   (C) - HEALTHY
     *     (F) - HEALTHY
     *    (E) - HEALTHY
     *      (G) - HEALTHY
     *  (B) - HEALTHY
     *    (D) - HEALTHY
     * 
     * Make sure to use VERBOSE_SPACE_INCREMENT for indentation.  */
    private String toStringVerbose(int indent){
        String s= "";
        for (int i= 0; i < indent; i++) {
            s= s + VERBOSE_SPACE_INCREMENT;
        }
        s= s +  root.toString();

        if (children.isEmpty()) return s;

        for (DiseaseTree dt : children) {
            s= s + "\n" + dt.toStringVerbose(indent + 1);
        }
        return s;
    }

    /** Return true iff this is equal to ob.
     * Two DiseaseTrees are equal if they are the same object (==) or:
     *  <br> - they have the same root Person object (==)
     *  <br> - their children sets are equal, which requires:
     *  <br> --- the two sets are of the same size
     *  <br> --- for every DiseaseTree dt in one set, there is a DiseaseTree dt2 
     *       in the other set for which dt.equals(dt2) is true.
     *  
     * Otherwise the two DiseaseTrees are not equal. */
    public boolean equals(Object ob){
        //TODO 7
        // Hint about checking whether each child of one tree equals SOME
        // other tree of the other tree's children.
        // First, you have to check them all until you find an equal one (or
        // return false if you don't.)
        // Second, you know that a child of one tree cannot equal more than one
        // child of another tree because the names of Person's are all unique;
        // there are no duplicates. 
        if(this == ob) return true;
        
        //Roots are the same. Now we check if the children are the same
        else if(this.root == ((DiseaseTree)ob).root)
        	return checkIfChildrenMatch(this, (DiseaseTree)ob);
       
        return false;
    }
    
    /**
     * Recursive method to check if the the two disease trees are exactly the
     * same
     * @param dt1
     * 		disease tree 1
     * @param dt2
     * 		disease tree 2
     * @return
     * 		true if the trees are the the same, else false
     */
    private boolean checkIfChildrenMatch(DiseaseTree dt1, DiseaseTree dt2){
    	
    	if(dt1.getChildrenCount() == dt2.getChildrenCount()){
    		for(DiseaseTree child: dt1.children){
    			if(equalsAChild(child, dt2.children)){
    				 for (Iterator<DiseaseTree> iter = dt2.children.iterator(); iter.hasNext(); ) {
    					 DiseaseTree child2 = iter.next();
    				     if (child2.equals(child))
    				    	return checkIfChildrenMatch(child, child2);
    				 }
    			}
    			return false;
    		}
    		return true;
    	}
    	return false;
    }
    
    /** Return true iff c equals some tree in trees. */
    public static boolean equalsAChild(DiseaseTree t, Set<DiseaseTree> trees) {
        for (DiseaseTree ch2 : trees) {
            if (t.equals(ch2)) {
                return true;
            }
        }
        return false;
    }

    /** Optional: You may implement this method if you want to.
     * Doing so (correctly) allows for a very simple implementation of equals.
     * This method will not be tested in any way.
     * 
     * For rules on how to implement a hashCode() method correctly,
     * see {@linkplain Object#hashCode() Object.hashCode()} */
    public int hashCode() {
        return super.hashCode();
    }

    /** Create a population of 7 people with health 10, create a disease tree
     * with certain edges, and then print the resulting tree on the console. */
    public static void main(String[] args) {
        Network n= new Network();
        Person a= new Person("A", n, 10);
        Person b= new Person("B", n, 10);
        Person c= new Person("C", n, 10);
        Person d= new Person("D", n, 10);
        Person e= new Person("E", n, 10);
        Person f= new Person("F", n, 10);
        Person g= new Person("G", n, 10);

        DiseaseTree dt= new DiseaseTree(a);
        dt.add(a, b);
        DiseaseTree dt2= dt.add(a, c);
        dt.add(b, d);
        dt.add(c, e);
        dt.add(c, f);
        dt.add(e, g);

        System.out.println(dt.toStringVerbose());
        System.out.println(dt2.toString());
    }
}

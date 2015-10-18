import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiseaseTreeTest {

	private static Network n;
	private static Person[] people;
	
	@BeforeClass
	public static void setup(){
		Network n = new Network();
		people = new Person[] { new Person("A", n, 0), new Person("B", n, 0), new Person("C", n, 0), new Person("D", n, 0),
								new Person("E", n, 0), new Person("F", n, 0), new Person("G", n, 0), new Person("H", n, 0),
								new Person("I", n, 0), new Person("J", n, 0), new Person("K", n, 0), new Person("L", n, 0) };
	}
	
	@Test
	public void testAdd() {
		//Making A the root of the Disease Tree
		DiseaseTree dt = new DiseaseTree(people[0]);
		
		//Making B a child of A and then checking if it has been added to the tree
		dt.add(people[0], people[1]);
		assertEquals(true, dt.contains(people[1]));
		assertEquals(1, dt.getChildrenCount());
		assertEquals(people[0], dt.getParentOf(people[1]));
		
		//Making C a child of A and then checking if it has been added to the tree
		dt.add(people[0], people[2]);
		assertEquals(true, dt.contains(people[2]));
		assertEquals(2, dt.getChildrenCount());
		assertEquals(people[0], dt.getParentOf(people[2]));
		
		//Making D a child of B and then checking if it has been added to the tree
		dt.add(people[1], people[3]);
		assertEquals(true, dt.contains(people[2]));
		assertEquals(2, dt.getChildrenCount()); //Checking is A's children count is still the same
		assertEquals(people[1], dt.getParentOf(people[3]));			
		
		//Making E a child of D and then checking if it has been added to the tree
		dt.add(people[3], people[4]);
		assertEquals(true, dt.contains(people[2])); 
		assertEquals(2, dt.getChildrenCount()); //Checking is A's children count is still the same
		assertEquals(people[3], dt.getParentOf(people[4]));	
	}
	
	@Test
	public void testSize() {
		DiseaseTree dt = new DiseaseTree(people[0]);
		assertEquals(1, dt.size());
		
		dt.add(people[0], people[1]);
		assertEquals(2, dt.size());
		
		dt.add(people[0], people[2]);
		assertEquals(3, dt.size());
		
		dt.add(people[1], people[3]);
		assertEquals(4, dt.size());
		
		dt.add(people[3], people[4]);
		assertEquals(5, dt.size());	
	}
	
	@Test
	public void testDepthOf(){
		DiseaseTree dt = new DiseaseTree(people[0]);
		assertEquals(0, dt.depthOf(people[0]));
		
		dt.add(people[0], people[1]);
		dt.add(people[0], people[2]);
		dt.add(people[1], people[3]);
		dt.add(people[3], people[4]);
		
		assertEquals(1, dt.depthOf(people[1]));
		assertEquals(1, dt.depthOf(people[2]));
		assertEquals(2, dt.depthOf(people[3]));
		assertEquals(3, dt.depthOf(people[4]));

		//Negative condition for person not in the tree
		assertEquals(-1, dt.depthOf(people[5]));
		
		//Checking if the tree has been constructed properly with the pre-defined 
		//maxDepth method
		assertEquals(3, dt.maxDepth());
	}
	
	/**
	 *  	 0       A
     *              / \
     *       1     B   C
     *            /   / \
     *       2   D   E   F
     *                \
     *       3         G
	 */
	@Test
	public void testWidthAtDepth(){
		DiseaseTree A = new DiseaseTree(people[0]); //A
		A.add(people[0], people[1]); //B
		A.add(people[0], people[2]); //C
		A.add(people[1], people[3]); //D
		A.add(people[2], people[4]); //E
		A.add(people[2], people[5]); //F
		A.add(people[4], people[6]); //G
					     
		//For disease tree with A at the root
		assertEquals(1,A.widthAtDepth(0));
		assertEquals(2,A.widthAtDepth(1));
		assertEquals(3,A.widthAtDepth(2));
		assertEquals(1,A.widthAtDepth(3));
		assertEquals(0,A.widthAtDepth(4));
		
		//For disease tree with C at the root
		DiseaseTree C = A.getTree(people[2]);
		assertEquals(1,C.widthAtDepth(0));
		assertEquals(2,C.widthAtDepth(1));
		
	}
	
	@Test
	public void testGetDiseaseRouteTo(){
		DiseaseTree A = new DiseaseTree(people[0]); //A
		assertEquals(people[0], A.getDiseaseRouteTo(people[0]).get(0));
		
		A.add(people[0], people[1]); //B
		A.add(people[0], people[2]); //C
		A.add(people[1], people[3]); //D
		A.add(people[2], people[4]); //E
		A.add(people[2], people[5]); //F
		A.add(people[4], people[6]); //G
		
		//Finding the router to B
		assertEquals(people[0], A.getDiseaseRouteTo(people[1]).get(0));
		assertEquals(people[1], A.getDiseaseRouteTo(people[1]).get(1));
		
		//Finding the router to E
		assertEquals(people[0], A.getDiseaseRouteTo(people[4]).get(0));
		assertEquals(people[2], A.getDiseaseRouteTo(people[4]).get(1));
		assertEquals(people[4], A.getDiseaseRouteTo(people[4]).get(2));		
	}
	
	@Test
	public void testGetSharedAncestorOf(){
		DiseaseTree A = new DiseaseTree(people[0]); //A
		assertEquals(null, A.getSharedAncestorOf(people[0], null));
		
		A.add(people[0], people[1]); //B
		A.add(people[0], people[2]); //C
		A.add(people[1], people[3]); //D
		A.add(people[2], people[4]); //E
		A.add(people[2], people[5]); //F
		A.add(people[3], people[6]); //G
		
		assertEquals(people[1], A.getSharedAncestorOf(people[1], people[1])); //BB
		assertEquals(people[0], A.getSharedAncestorOf(people[0], people[1])); //AB
		assertEquals(people[0], A.getSharedAncestorOf(people[2], people[1])); //CB
		assertEquals(people[0], A.getSharedAncestorOf(people[0], people[2])); //AC
		assertEquals(people[2], A.getSharedAncestorOf(people[4], people[5])); //EF
		assertEquals(people[0], A.getSharedAncestorOf(people[6], people[5])); //GF
		assertEquals(null, A.getSharedAncestorOf(people[3], people[8])); //DH
		
		DiseaseTree B = A.getTree(people[1]);
		assertEquals(null, B.getSharedAncestorOf(people[3], people[5])); //DF
	}
	
	@Test
	public void testEquals(){
		DiseaseTree dt1 = new DiseaseTree(people[0]); //A
		DiseaseTree dt2 = new DiseaseTree(people[0]); //A
		assertEquals(true, dt1.equals(dt1));
		assertEquals(true, dt1.equals(dt2));
		
		//Added B to both
		dt1.add(people[0], people[1]);
		dt2.add(people[0], people[1]);
		assertEquals(true, dt1.equals(dt2)); 
		
		//Added C only to dt1
		dt1.add(people[0], people[2]);
		assertEquals(false, dt1.equals(dt2));
		
		//Added C to dt2 as well
		dt2.add(people[0], people[2]);
		assertEquals(true, dt1.equals(dt2));
		
		//Added D to both 
		dt1.add(people[1], people[3]);
		dt2.add(people[1], people[3]); 
		assertEquals(true, dt1.equals(dt2));
	}
}

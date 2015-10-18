import io.TextIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import graph.Graph;

/** An instance represents a network of people, used with a Disease tree.
 * It is known that the names of all Person's will be distinct --no duplicates. 
 * @author Mshnik */
public class Network extends Graph<Person, PersonConnection> {
    
    private int maxHealth; // The maximum health a person can have
    
    protected static String[] names; // Names of all people
    
    /** Read in the array of names from the names text file */
    static {
        try {
            names= TextIO.read(new File("data/names.txt"));
        } catch (IOException e) {
            System.err.println("Error reading names file, should be located at data/names.txt");
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /** Constructor: an instance with no people and no connections. */
    public Network() {
        super();
    }

    /** Constructor: a graph of size people of health mh with edges generated
     * randomly based on connectionProbability cp.
     * Preconditions: 0 <= size, 0 <= cp <= 1, 1 <= mh. */
    public Network(int size, double cp, int mh) {
        super();
        assert 0 <= size  &&  0 <= cp  &&  cp <= 1  &&  1 <= mh;
        maxHealth= mh;
        for (int i = 0; i < size; i++) {
            //Add itself to this as part of construction
            new Person(names[i], this, mh);
        }
        
        for (Person p1 : vertexSet()) {
            for (Person p2 : vertexSet()) {
                if (p1 != p2  && Math.random() < cp) {
                    addEdge(p1, p2, new PersonConnection());
                }
            }
        }
    }

    /** Constructor: an instance generated for the people in dt.
     * There is an edge from each parent to each of its children. */
    public Network(DiseaseTree dt) {
        super();
        addVertex(dt.getRoot());
        recCreate(dt);
    }

    /** Add to this Network the people in children trees of dt,
     * adding edges from each root to its children.
     * Precondition: dt.getRoot is already in the graph. */
    private void recCreate(DiseaseTree dt) {
        Person dtRoot= dt.getRoot();
        for (DiseaseTree child : dt.getChildren()){
            addVertex(child.getRoot());
            addEdge(dtRoot, child.getRoot(), new PersonConnection());
            recCreate(child);
        }
    }

    /** Return a list of people in state s in this Network. */
    public List<Person> getPeopleOfType(Person.State s) {
        ArrayList<Person> lst= new ArrayList<>();
        for (Person p : vertexSet()) {
            if (p.getState() == s) {
                lst.add(p);
            }
        }
        return lst;
    }

}

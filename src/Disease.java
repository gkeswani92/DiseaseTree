import io.ScannerUtils;

import java.util.Scanner;
import java.util.Set;

import common.Util;
import common.types.Tuple;
import common.types.Tuple5;

/** An instance represents a disease spreading and ultimately dying out among
 * a limited population (or killing everyone).
 * <br>
 * Each Disease is created on a Network of people and with a chosen first patient.
 * Disease is runnable, but for the purposes of this project, it does not need
 * to be run on a separate thread.
 * @author MPatashnik
 */
public class Disease implements Runnable {

    private Network network; // The graph on which this Disease is running. */
    private DiseaseTree tree; // The tree representing this disease.
    private int steps; // Number of time steps this disease took to create dt.
    private Statistics statistics; // The disease model: Statistics that determine the spread of the disease.
    private static final int RUNNING_CHAR_COUNT_MAX= 50; //How many chars to print per line in the running section
    private int runningCharCount= 7;  //Used in printing the run progress.


    /** Constructor: a new Disease on network nw with first patient fp
     *  and disease model s.    */
    public Disease(Network nw, Person fp, Statistics s){
        steps= 0;
        network= nw;
        fp.becomeSick(0);
        tree= new DiseaseTree(fp);
        statistics= s;
    }

    /** Run the disease until no sick people remain.
     *  Print out info about running.*/
    public @Override void run(){
        System.out.print("Running");
        while (network.getPeopleOfType(Person.State.SICK).size() > 0) {
            step();
        }
        System.out.println("Done.\n");
    }


    /** Perform a single step on the disease, using disease model statistics.
     * First, sick people may become immune with a certain probability.
     * Second, sick people become less healthy by 1, and if their health reaches 0, they die.
     * Third, sick people may spread the disease to one neighbor, with a certain probability.
     */
    private void step() {
        Set<Person> people= network.vertexSet();
        System.out.print(".");
        runningCharCount++;
        if (runningCharCount > RUNNING_CHAR_COUNT_MAX) {
            System.out.print("\n");
        }

        // For each sick person, make them immune with a certain probability
        for (Person p : people) {
            if (p.isSick() && statistics.personBecomesImmune()){
                p.becomeImmune(steps);
            }
        }

        // For each sick person, deduct 1 from health and make death if health becomes 0
        for (Person p : people) {
            if (p.isSick()) {
                p.reduceHealth(steps);
            }
        }

        // For each sick person, spread the disease to one random neighbor with a
        // certain probability.
        for (Person p : people) {
            if (p.isSick()) {
                Person n= p.getRandomNeighbor();
                if (n != null  &&  n.isHealthy()  &&  statistics.diseaseSpreadsToPerson()) {
                    n.becomeSick(steps);
                    tree.add(p, n);
                }
            }
        }

        steps= steps + 1;
    }

    /** Read in the five statistic arguments from the console.
     * Return a Tuple5, with the following components:
     * 		<br> - size: the number of people in the network
     * 		<br> - maxHealth: how much health each person starts with
     * 		<br> - connectionProbability: probability that two people are connected in the network
     * 		<br> - sicknessProbability: probability that a sick person spreads the sickness to a neighbor in one time step
     * 		<br> - immunizationProbability: probability that  a sick person becomes immune in one time step
     */
    private static Tuple5<Integer, Integer, Double, Double, Double> readArgs() {
        Scanner scanner= ScannerUtils.defaultScanner();
        int size = ScannerUtils.get(Integer.class, scanner, "Enter the size of the population: ",
                "Please enter a positive non-zero integer", (i) -> i > 0);
        int maxHealth= ScannerUtils.get(Integer.class, scanner, 
                "Enter the amount of health for each person: ",
                "Please enter a positive non-zero integer", (i) -> i > 0);
        double connectionProb= ScannerUtils.get(Double.class, scanner, 
                "Enter the probability of a connection: ",
                "Please enter a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        double sicknessProb= ScannerUtils.get(Double.class, scanner, 
                "Enter the probability of becoming sick: ",
                "Please enter a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        double immunizationProb= ScannerUtils.get(Double.class, scanner, 
                "Enter the probability of becoming immune: ",
                "Please enter a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        scanner.close();
        return Tuple.of(size, maxHealth, connectionProb, sicknessProb, immunizationProb);
    }


    /** Run Disease on the arguments listed in args.
     * If args does not match the pattern below, read in arguments via the console
     * by using readArgs().
     * 
     * Then, call disease.run() and create a DiseaseFrame showing the created DiseaseTree.
     * 
     * args should be an array of [size, maxHealth, connection probability, 
     * 						sickness probability, immunization probability],
     * 		or unused (any value). If not used, the user is prompted for input in the console.
     */
    public static void main(String[] args) {
        //Get arguments
        int size= 10;
        int maxHealth= 5;
        double connectionProbability= 0.7;
        double sicknessProbability= 0.5;
        double immunizationProbability= 0.1;

        try {
            //Attempt to read from args array passed in
            size= Integer.parseInt(args[0]);
            maxHealth= Integer.parseInt(args[1]);
            connectionProbability= Double.parseDouble(args[2]);
            sicknessProbability= Double.parseDouble(args[3]);
            immunizationProbability= Double.parseDouble(args[4]);
        } catch (Exception e) {
            //If too few or wrong type, read from scanner
            Tuple5<Integer, Integer, Double, Double, Double> args2= readArgs();
            size= args2._1;
            maxHealth= args2._2;
            connectionProbability= args2._3;
            sicknessProbability= args2._4;
            immunizationProbability= args2._5;
        }

        //Set defaults and create the Network, Statistics, and Disease objects
        System.out.print("\nSetting up ");
        System.out.print(".");
        Network n= new Network(size, connectionProbability, maxHealth);
        System.out.print(".");
        Statistics s= new Statistics(sicknessProbability, immunizationProbability);
        System.out.print(".");
        Disease d= new Disease(n, Util.randomElement(n.vertexSet()), s);
        System.out.println("Done.");

        d.run();
        System.out.println(d.tree.toStringVerbose() + "\n");
        for (Person p : d.network.getPeopleOfType(Person.State.HEALTHY)) {
            System.out.println(p);
        }
        DiseaseFrame.show(d.tree, d.steps);
    }
}

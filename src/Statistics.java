/** An instance contains the probabilities that a person gets a disease 
 * and a person becomes immune in a time step.
 * 
 * @author Mshnik */
public class Statistics {

    private double contagionChance; // Probability of contagion. In range [0, 1]

    private double immunizationChance; //Probability of immunization. In range [0, 1]

    /** Constructor: an instance with contagion probability cp and
     * immunization probability ip.
     * Precondition: 0 <= cp, ip <= 1. */
    public Statistics(double cp, double ip) {
        contagionChance= cp;
        immunizationChance= ip;
    }

    /** Return true if a new random number is less than the probability
     * of contagion. */
    public boolean diseaseSpreadsToPerson(){
      return Math.random() < contagionChance;
    }

    /** Return true if a new random number is less than the probability
     * of becoming immune. */
    public boolean personBecomesImmune(){
      return Math.random() < immunizationChance;
    }
}

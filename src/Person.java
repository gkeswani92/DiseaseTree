import io.TextIO;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import common.StringUtil;
import common.Util;
import common.types.Tuple1;

/** A instance represents a person and their health. 
 * @author Mshnik*/
public class Person extends Tuple1<String>{

    /** The possible Disease-related states of a person. */
    enum State{  // The names indicate the state.
        HEALTHY,
        SICK,
        DEAD,
        IMMUNE
    }
    

    private final Network graph; // The network to this person belongs

    private int health;    // Amount of health this person has. >= 0.
                           // 0 means dead, any other number means alive
    private State state;        // State of this person
    private int stepGotSick= -1;   // Time step in which this person got sick (-1 if never been sick)
    private int stepGotImmune= -1; // Time step in which this person got immune (-1 if not immune)
    private int stepDied= -1;      // Time step in which this person died (-1 if not dead)

 
    /** Constructor: a healthy Person with name n and health h, added to graph g.
     * Precondition: The new person is not in g, and their name is distinct from the name
     *               of any other person in g. This is because the name is used for
     *               equality and hashing. */
    public Person(String n, Network g, int h) {
        super(StringUtil.toPronounCase(n));
        health= h;
        state= State.HEALTHY;
        graph= g;
        graph.addVertex(this);
        System.out.println(n + " has health " + health);
    }

    /** Return a representation of this Person. */
    public @Override String toString(){
        return super.toString() + " - " + state;
    }

    /** Return the name of this person. */
    public String getName(){
        return _1;
    }

    /** Make this person sick during step currentStep.
     * Throw a RuntimeException if this person is not HEALTHY. */
     public void becomeSick(int currentStep) {
        if (state != State.HEALTHY) {
            throw new RuntimeException(state + " person can't become sick");
        }
        state= State.SICK;
        stepGotSick= currentStep;
    }

     /** Make this person immune during step currentStep.
      * Throw a RuntimeException if this person is immune or dead. */
     public void becomeImmune(int currentStep) {
        if (state == State.IMMUNE || state == State.DEAD) {
            throw new RuntimeException(state + " person can't become immune");
        }
        state= State.IMMUNE;
        stepGotImmune= currentStep;
    }

     /** Decrement the health of this person in step currentStep. If its health
      * becomes 0, the person dies.
      * Throw a RuntimeException if this person is not sick. */
     public void reduceHealth(int currentStep) {
        if (state != State.SICK) {
            throw new RuntimeException(state + " person can't lose health");
        }
        health--;
        if (health == 0) {
            state= State.DEAD;
            stepDied= currentStep;
        }
    }

    /** Return the state of this person. */
    public State getState() {
        return state;
    }

    /** Return "This person is alive". */
    public boolean isAlive() {
        return state != State.DEAD;
    }

    /** @return true iff this person is dead */
    public boolean isDead() {
        return !isAlive();
    }

    /** Return "This person is healthy. */
    public boolean isHealthy() {
        return state == State.HEALTHY;
    }

    /** Return "This person is immune". */
    public boolean isImmune() {
        return state == State.IMMUNE;
    }

    /** Return "This person is sick". */
    public boolean isSick() {
        return state == State.SICK;
    }

    /** Return the time step in which this person got sick" (-1 if never been sick). */
    public int getFrameGotSick() {
        return stepGotSick;
    }

    /** Return the time step in which this person got immune" (-1 if not immune). */
    public int getFrameGotImmune() {
        return stepGotImmune;
    }

    /** Return the time step in which this person died" (-1 if not dead). */
    public int getFrameDied() {
        return stepDied;
    }

    /** Return the neighbors of this person. */
    public Set<Person> getNeighbors() {
        return graph.neighborsOf(this);
    }

    /** Return a random neighbor of this person */
    public Person getRandomNeighbor(){
        return Util.randomElement(graph.neighborsOf(this));
    }
}

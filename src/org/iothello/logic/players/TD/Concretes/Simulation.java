package org.iothello.logic.players.TD.Concretes;

import org.iothello.logic.players.TD.Abstracts.AbstractAgent;
import org.iothello.logic.players.TD.Abstracts.AbstractEnvironment;
import org.iothello.logic.players.TD.Interfaces.IAction;
import org.iothello.logic.players.TD.Interfaces.ISimulation;
import org.iothello.logic.players.TD.Interfaces.IState;

/**
 * This class implements the base object of the interface; a
 *  <code>Simulation</code> instance manages the interaction between
 *  the agent and the environment.
 *
 * User: Johan
 * Date: 2013-03-13
 * Time: 20:34
 */
public class Simulation implements ISimulation {
    /**
     * The <code>Agent</code> instance associated with the
     * <code>Simulation</code> instance */
    protected AbstractAgent agt;

    /**
     * The <code>Environment</code> instance associated with the
     * <code>Simulation</code> instance */
    protected AbstractEnvironment env;

    /**
     * The current <code>State</code>.  */
    private IState currState;

    /**
     * The current <code>Action</code>.  */
    private IAction currAction;

    /**
     * Creates a new <code>Simulation</code> instance.  A
     * <code>Simulation</code> instance manages the interaction
     * between the agent & the environment.  The Agent and Environment
     * instances corresponding to this Simulation instance must be
     * constructed prior to constructing a new Simulation instance.
     *
     * @param agt an <code>Agent</code> instance
     * @param env an <code>Environment</code> instance */
    public Simulation(AbstractAgent agt, Environment env) {
        this.agt = agt;
        this.env = env;

        agt.sim = this;
        env.sim = this;

        currState = null;
        currAction = null;
    }

    /**
     * Initializes the simulation, agent, & environment instances.
     *
     * @param a a <code>Object[]</code> value */
    @Override
    public void init( Object[] a ) {
        env.init(a);
        agt.init(a);
    }

    /**
     * Forces the beginning of a new trial.  This is done primarily b     * calls to Environment and Agent's startTrial() methods.
     * User-defined specialized methods may also compute average or
     * accumulated rewards per trial or other data and update
     * displays.  */
    @Override
    public void startTrial(){
        currState = env.startTrial();
        currAction = agt.startTrial(currState);
    }

    /**
     * Executes one step of the simulation, starting from the current
     * state of environment. Updates the class variables currState and
     * currAction.
     *
     * @param collectData a <code>boolean</code>, true if collectData
     * should be called from this method.  */
     @Override
     public void step(boolean collectData){
        IState nextState;
        IAction nextAction;
        double reward;
        ActionResult result;

        // Take Action currAction in the current state to determine
        // reward & next State
        result   = env.step( currAction );
        nextState = result.nextState;
        reward   = result.reward;

        if(collectData)
            collectData( currState, currAction, nextState, reward );

        // Determine agent's next action in response to current
        // State (nextState) and the previous reward
        nextAction = agt.step( nextState, reward );

        // set the class variables for the new, current state and
        // action
        currState   = nextState;
        currAction = nextAction;
    }


    /**
     * Runs the simulation for numSteps steps, starting from the
     * current state of environment.  If the terminal state is
     * reached, the simulation is immediately prepared for a new trial
     * by calling Simulation's startTrial() method.  This method
     * allows the user to control the execution of the simulation by
     * providing the total number of steps directly.
     *
     * @param numSteps a <code>long</code> value, the number of steps
     * to be performed */
    @Override
    public void steps( long numSteps ){

        for(long stepNum = 0; stepNum < numSteps ; stepNum++){ //??? stop if terminal?
            if (currState == null)
                startTrial(); // counts as 1 step
            else
                step(true);
        }
    }

    /**
     * Runs the simulation for numTrials trials, starting from the
     * current state of the environment.  Each trial begins by calling
     * Simulation's startTrial() method & ends when the termination
     * state is reached or when maxStepsPerTrial steps is reached,
     * whichever comes first.  Thus, this method allows the user to
     * control the execution of the simulation by providing the total
     * number of trials directly.
     *
     * @param numTrials a <code>long</code> value, the max number of
     * trials to be performed
     * @param maxStepsPerTrial a <code>long</code> value, the max
     * number of steps per trial
     * @param printDivisor an <code>int</code> value; collectData()
     * will only be called when the trial number is divisible by
     * printDivisor */
    @Override
    public void trials( long numTrials, long maxStepsPerTrial,
                        int printDivisor ){
        long trial, stepNum;

        for(trial = 0; trial < numTrials; trial++){
            startTrial(); // counts as 1 step

            for(stepNum=1;
                !currState.isTerminal() && (stepNum < maxStepsPerTrial);
                stepNum++) {

                step(trial % printDivisor == 0);
            }
        }
    }

    /**
     * A user-defined method that is called regularly by the steps and
     * trials methods of the Simulation instance.  User-defined
     * specialized methods might accumulate rewards or other data and
     * update displays.  This is the preferred way to gain access to
     * the simulation's behavior.
     *
     * @param state a <code>State</code> value. Should not be
     * mutated within this method.
     * @param act an <code>Action</code> value.  Should not be
     * mutated within this method.
     * @param nextState a <code>State</code> value. Should not be
     * mutated within this method.
     * @param reward a <code>double</code> value */
    @Override
    public void collectData( IState state, IAction act,
                             IState nextState, double reward )
    {
        // trivial collect data - to use, you must OVERRIDE
    }
}

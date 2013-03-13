package org.iothello.logic.players.TD.Concretes;

import org.iothello.logic.players.TD.Abstracts.AbstractAgent;
import org.iothello.logic.players.TD.Interfaces.IAction;
import org.iothello.logic.players.TD.Interfaces.IState;

/**
 * Agent.java
 * An abstract class for implementing all agents.  An agent
 *  is an entity that interacts with the environment, receiving
 *  states and selecting actions.  The agent may or may not learn,
 *  may or may not build a model of the environment, etc.  Specific
 *  agents are instances of subclasses of <code>Agent</code>.
 *
 * User: Johan
 * Date: 2013-03-13
 * Time: 20:34
 */
public class Agent extends AbstractAgent {
    /** A <code>Simulation</code> instance used by the agent to
     * access the simulation and the corresponding environment. This
     * instance is initialized for a specific <code>Agent</code>
     * instance in the <code>Simulation</code> class constructor.  */
    public Simulation sim;

    /**
     * Creates a new <code>Agent</code> instance; an agent interacts
     * with the environment, receiving states & selecting actions.  */
    public Agent (){
    }

    @Override
    public void init(Object[] arr) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IAction startTrial(IState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IAction step(IState nextState, double reward) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

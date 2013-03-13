package org.iothello.logic.players.TD.Interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: Johan Dahlberg
 * Date: 2013-03-13
 * Time: 20:32
 */
public interface ISimulation {
    public void init(Object[] arr);
    public void startTrial();
    public void step(boolean collectData);
    public void steps(long numSteps);
    public void trials(long numTrials, long maxStepsPerTrial, int printDivisor);
    public abstract void collectData(IState state, IAction act, IState nextState, double reward);
}

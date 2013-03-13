package org.iothello.logic.players.TD.Interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: Johan Dahlberg
 * Date: 2013-03-13
 * Time: 20:27
 */
public interface IAgent {
    public void init(Object[] arr);
    public Action startTrial(State state);
    public Action step(State nextState, double reward);
}

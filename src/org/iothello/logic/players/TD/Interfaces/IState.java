package org.iothello.logic.players.TD.Interfaces;

/**
 * State - An interface for implementing the input (i.e. sensory
 *  information) that an agent receives from the environment.
 *
 * User: Johan
 * Date: 2013-03-13
 * Time: 20:49
 */
public interface IState {
    /**
     * Whether or not the state is a terminal state
     */
    boolean isTerminal();
}
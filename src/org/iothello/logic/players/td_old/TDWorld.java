package org.iothello.logic.players.td_old;

public interface TDWorld {
    // Returns the array containing the information about the
    // number of states in each dimension ( [0] - [array.length - 2] )
    // and the number of possible actions ( [array.length - 1] ). 
    int[] getDimension();
    
    // Returns a new instance of the new state that results
    // from applying the given action to the current state.
    int[] getNextState( int action );
    
    // Returns the value for the last reward received from 
    // calling the method getNextState( int action ).
    double getReward();
    
    // Returns true if the given action is a valid action
    // on the current state, false if not.
    boolean validAction( int action );
    
    // Returns true if current state is absorbing state, false if not.
    boolean endState();

    // Resets the current state to the start position and returns that state.
    int[] resetState();
    
    // Gets the initial value for the policy.
    double getInitValues();
}

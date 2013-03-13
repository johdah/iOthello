package org.iothello.logic.players.td_old;

import java.util.Vector;

public class TDLearner {
	TDWorld thisWorld;
    TDPolicy policy;
    
    private boolean debug = false;

    // Learning types
    public static final int Q_LEARNING = 1;
    public static final int SARSA = 2;
    public static final int Q_LAMBDA = 3; // Good parms were lambda=0.05, gamma=0.1, alpha=0.01, epsilon=0.1

    // Action selection types
    public static final int E_GREEDY = 1;
    public static final int SOFTMAX = 2;

    int learningMethod;
    int actionSelection;

    double epsilon;
    double temp;

    double alpha;
    double gamma;
    double lambda;

    int[] dimSize;
    int[] state;
    int[] newstate;
    int action;
    double reward;

    int epochs;
	public int epochsdone;
	
    Thread thisThread;
    public boolean running;

    Vector trace = new Vector();
    int[] saPair;

    long timer;

    boolean random = false;
	Runnable a;

	public TDLearner(TDWorld world, boolean debug) {
		// Getting the world from the invoking method.
		thisWorld = world;

		// Get dimensions of the world.
		dimSize = thisWorld.getDimension();
	
		// Creating new policy with dimensions to suit the world.
		policy = new TDPolicy( dimSize );

		// Initializing the policy with the initial values defined by the world.
		policy.initValues( thisWorld.getInitValues() );
	
		learningMethod = Q_LEARNING;  //Q_LAMBDA;//SARSA;
		actionSelection = E_GREEDY;
	
		// set default values
		epsilon = 0.1;
		temp = 1;

		alpha = 1; // For CliffWorld alpha = 1 is good
		gamma = 0.1;
		lambda = 0.1;  // For CliffWorld gamma = 0.1, l = 0.5 (l*g=0.05)is a good choice.

		if(isDebug()) System.out.println( "RLearner initialised" );
	}

	/**
	 * Is debug enabled
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}
	
	// execute one epoch
	public void runEpoch() {
	
		// Reset state to start position defined by the world.
		state = thisWorld.resetState();
		
		switch( learningMethod ) {
	    
		case Q_LEARNING : {
	    
	    	double this_Q;
		    double max_Q;
		    double new_Q;

			while( ! thisWorld.endState() ) {
		    
			    if( ! running ) break;
					action = selectAction( state );
		    		newstate = thisWorld.getNextState( action );
				    reward = thisWorld.getReward();
		    
				    this_Q = policy.getQValue( state, action );
				    max_Q = policy.getMaxQValue( newstate );

				    // Calculate new Value for Q
				    new_Q = this_Q + alpha * ( reward + gamma * max_Q - this_Q );
				    policy.setQValue( state, action, new_Q );

				    // Set state to the new state.
				    state = newstate;
			}
		
	    
	    }

		case SARSA : {
		    
		    int newaction;
		    double this_Q;
		    double next_Q;
		    double new_Q;
	
		    action = selectAction( state );
			while( ! thisWorld.endState() ) {
			
			    if( ! running ) break;
			    
			    newstate = thisWorld.getNextState( action );
			    reward = thisWorld.getReward();
			    
	   		    newaction = selectAction( newstate );
			    
			    this_Q = policy.getQValue( state, action );
			    next_Q = policy.getQValue( newstate, newaction );
			    
			    new_Q = this_Q + alpha * ( reward + gamma * next_Q - this_Q );
			    
			    policy.setQValue( state, action, new_Q );
			    
			    // Set state to the new state and action to the new action.
			    state = newstate;
			    action = newaction;
			}
			
		}
	
		case Q_LAMBDA : {
		    
		    double max_Q;
		    double this_Q;
		    double new_Q;
		    double delta;
	
			// Remove all eligibility traces. 
			trace.removeAllElements();
			
			while( ! thisWorld.endState() ) {
			    
			    if( ! running ) break;
			    
			    action = selectAction( state );
			    
			    // Store state-action pair in eligibility trace.
			    saPair = new int[dimSize.length];
			    System.arraycopy( state, 0, saPair, 0, state.length );
			    saPair[state.length] = action;
			    trace.add( saPair );
	
			    // Store only 10 traced states.
			    if( trace.size() == 11 )
				trace.removeElementAt( 0 );
			    		    
			    newstate = thisWorld.getNextState( action );
			    reward = thisWorld.getReward();
			    
			    max_Q = policy.getMaxQValue( newstate );
			    this_Q = policy.getQValue( state, action );
			    
			    // Calculate new Value for Q
			    delta = reward + gamma * max_Q - this_Q;
			    new_Q = this_Q + alpha * delta;
	
			    policy.setQValue( state, action, new_Q );
			    
			    // Update values for the trace.
			    for( int e = trace.size() - 2 ; e >= 0 ; e-- ) {
				
				saPair = (int[]) trace.get( e );
				
				System.arraycopy( saPair, 0, state, 0, state.length );
				action = saPair[state.length];
	
				this_Q = policy.getQValue( state, action );
				new_Q = this_Q + alpha * delta * Math.pow( gamma * lambda, ( trace.size() - 1 - e ) );
	
				policy.setQValue( state, action, new_Q );
	
				//System.out.println("Set Q:" + new_Q + "for " + state[0] + "," + state[1] + " action " + action );
			    }
			    
			    if( random ) trace.removeAllElements();
	
			    // Set state to the new state.
			    state = newstate; 
			}
		
		} // case
	} // switch
    } // runEpoch

    // execute one trial
	public void runTrial() {
		System.out.println( "Learning! ("+epochs+" epochs)\n" );
		for( int i = 0 ; i < epochs ; i++ ) {
				if( ! running ) break;
		
				runEpoch();
				
			if( i % 1000 == 0 ) {
			    // give text output
			    timer = ( System.currentTimeMillis() - timer );
			    System.out.println("Epoch:" + i + " : " + timer);
			    timer = System.currentTimeMillis();
			}
		}
	}
    
    private int selectAction( int[] state ) {
		double[] qValues = policy.getQValuesAt( state );
		int selectedAction = -1;
	    
		switch (actionSelection) {
			case E_GREEDY : {
			    random = false;
			    double maxQ = -Double.MAX_VALUE;
			    int[] doubleValues = new int[qValues.length];
			    int maxDV = 0;
			    
			    //Explore
			    if ( Math.random() < epsilon ) {
					selectedAction = -1;
					random = true;
			    } else {
					for( int action = 0 ; action < qValues.length ; action++ ) {
					    
					    if( qValues[action] > maxQ ) {
							selectedAction = action;
							maxQ = qValues[action];
							maxDV = 0;
							doubleValues[maxDV] = selectedAction;
					    }
					    else if( qValues[action] == maxQ ) {
					    	maxDV++;
					    	doubleValues[maxDV] = action; 
					    }
					}
					
					if( maxDV > 0 ) {
					    int randomIndex = (int) ( Math.random() * ( maxDV + 1 ) );
					    selectedAction = doubleValues[ randomIndex ];
					}
			    }
			    
			    // Select random action if all qValues == 0 or exploring.
			    if ( selectedAction == -1 ) {
			    	// System.out.println( "Exploring ..." );
			    	selectedAction = (int) (Math.random() * qValues.length);
			    }
			    
			    // Choose new action if not valid.
			    while( ! thisWorld.validAction( selectedAction ) ) {
					selectedAction = (int) (Math.random() * qValues.length);
					// System.out.println( "Invalid action, new one:" + selectedAction);
			    }
			    
			    break;
			}
			
			case SOFTMAX : {
			    int action;
			    double prob[] = new double[ qValues.length ];
			    double sumProb = 0;
			    
			    for( action = 0 ; action < qValues.length ; action++ ) {
					prob[action] = Math.exp( qValues[action] / temp );
					sumProb += prob[action];
			    }
			    for( action = 0 ; action < qValues.length ; action++ )
			    	prob[action] = prob[action] / sumProb;
			    
			    boolean valid = false;
			    double rndValue;
			    double offset;
			    
			    while( ! valid ) {
					rndValue = Math.random();
					offset = 0;
					
					for( action = 0 ; action < qValues.length ; action++ ) {
					    if( rndValue > offset && rndValue < offset + prob[action] )
						selectedAction = action;
					    offset += prob[action];
					    // System.out.println( "Action " + action + " chosen with " + prob[action] );
					}
			
					if( thisWorld.validAction( selectedAction ) )
					    valid = true;
			    }
			    break;
			    
			}
		}
		return selectedAction;
    }

    public TDPolicy getPolicy() {
    	return policy;
    }

    public void setAlpha( double a ) {
    	if( a >= 0 && a < 1 )
    		alpha = a;
    }

    public double getAlpha() {
    	return alpha;
    } 

	/**
	 * Enable/disable debug mode
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

    public void setGamma( double g ) {
		if( g > 0 && g < 1 )
		    gamma = g;
    }

    public double getGamma() {
    	return gamma;
    }

    public void setEpsilon( double e ) {
		if( e > 0 && e < 1 )
		    epsilon = e;
    }
    
    public double getEpsilon() {
    	return epsilon;
    }
    
    public void setEpisodes( int e ) {
    	if( e > 0 )
    		epochs = e;
    }
    
    public int getEpisodes() {
    	return epochs;
    }
    
    public void setActionSelection( int as ) {
		switch ( as ) {
			case SOFTMAX : { 
			    actionSelection = SOFTMAX;
			    break;
			}
			case E_GREEDY :
			default : {
			    actionSelection = E_GREEDY;
			}
		
		}
    }
    
    public int getActionSelection() {
    	return actionSelection;
    }
    
    public void setLearningMethod( int lm ) {
		switch ( lm ) {
			case SARSA : {
			    learningMethod = SARSA;
			    break;
			}
			case Q_LAMBDA : {
			    learningMethod = Q_LAMBDA;
			    break;
			}
			case Q_LEARNING :
			default : { 
			    learningMethod = Q_LEARNING;
			}
		}
    }

    public int getLearningMethod() {
    	return learningMethod;
    }

	//AK: let us clear the policy
	public TDPolicy newPolicy() {
		policy = new TDPolicy( dimSize );
		// Initializing the policy with the initial values defined by the world.
		policy.initValues( thisWorld.getInitValues() );
		return policy;
	}
}

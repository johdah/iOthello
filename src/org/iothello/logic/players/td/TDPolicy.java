package org.iothello.logic.players.td;

import java.lang.reflect.Array;

public class TDPolicy {
	// Array qValuesTable;
    int[] dimSize;
    double[] qValues;
    private Object qValuesTable;
    int states, actions;
    
    TDPolicy( int[] dimSize ) {
		this.dimSize = dimSize;
		
		// Create n-dimensional array with size given in dimSize array.
		qValuesTable = Array.newInstance( double.class, dimSize );
		
		// Get number of states.
		states = dimSize[0];
		for( int j = 1 ; j < dimSize.length - 1 ; j++)
		    states *= dimSize[j];
		
		// Get number of actions.
		actions = dimSize[dimSize.length - 1]; 
    }


    public void initValues( double initValue ) {
		int i;
		int actualdim = 0;
		int state[] = new int[dimSize.length - 1];
	
		System.out.println( "States: " + states ); 
		for( int j = 0 ; j < states ; j++ ) {
		    
		    qValues = (double[]) myQValues( state );
		    
		    for( i = 0 ; i < actions ; i++ ) {
			// System.out.print( i );
			Array.setDouble( qValues, i, ( initValue )); //+ 0.0000000000000000001 * Math.random() ) );
		    }
	
		    state = getNextState( state );
		}
    }
    
    private int[] getNextState( int[] state ) {
		int i;
		int actualdim = 0;
		
		state[actualdim]++;
		if( state[actualdim] >= dimSize[actualdim] ) {
		    while( ( actualdim < dimSize.length - 1 ) && ( state[actualdim] >= dimSize[actualdim] ) ) {
			actualdim++;
			
			if( actualdim == dimSize.length - 1 )
			    return state;
			
			state[actualdim]++;
		    }
		    for( i = 0 ; i < actualdim ; i++ ) 
			state[i] = 0;
		    actualdim = 0;
		}
		return state;
    }
    
    private double[] myQValues( int[] state ) {
		int i;
		Object curTable = qValuesTable;
	
		for( i = 0 ; i < dimSize.length - 2 ; i++ ) {
		    //descend in each dimension
		    curTable = Array.get( curTable, state[i] );
		}
	
		//at last dimension of Array get QValues.
		return (double[]) Array.get( curTable, state[i] );
    }
    
    public double[] getQValuesAt( int[] state ) {
		int i;
		Object curTable = qValuesTable;
		double[] returnValues;
	
		for( i = 0 ; i < dimSize.length - 2 ; i++ ) {
		    //descend in each dimension
		    curTable = Array.get( curTable, state[i] );
		}
	
		//at last dimension of Array get QValues.
		qValues = (double[]) Array.get( curTable, state[i] );
		returnValues = new double[ qValues.length ];
		System.arraycopy( qValues, 0, returnValues, 0, qValues.length );
		return returnValues;
    }
    
    
    public void setQValue( int[] state, int action, double newQValue ) {
		qValues = myQValues( state );
		Array.setDouble( qValues, action, newQValue );
    }

    public double getMaxQValue( int[] state ) {
		double maxQ = -Double.MAX_VALUE;
		
		qValues = myQValues( state );
		
		for( int action = 0 ; action < qValues.length ; action++ ) {
		    if( qValues[action] > maxQ ) {
		    	maxQ = qValues[action];
		    }
		}
		return maxQ;
    }
    
    public double getQValue( int[] state, int action ) {
		double qValue = 0;
		
		qValues = myQValues( state );
		qValue = qValues[action];
	
		return qValue;
    }

    public int getBestAction( int[] state ) {
		double maxQ = -Double.MAX_VALUE;
		int selectedAction = -1;
		int[] doubleValues = new int[qValues.length];
		int maxDV = 0;
	
		qValues = myQValues( state );
		
		for( int action = 0 ; action < qValues.length ; action++ ) {
		    //System.out.println( "STATE: [" + state[0] + "," + state[1] + "]" ); 
		    //System.out.println( "action:qValue, maxQ " + action + ":" + qValues[action] + "," + maxQ );
		    
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
		    //System.out.println( "DOUBLE values, random selection, maxdv =" + maxDV );
		    int randomIndex = (int) ( Math.random() * ( maxDV + 1 ) );
		    selectedAction = doubleValues[ randomIndex ];
		}
		
		
		if( selectedAction == -1 ) {
		    //System.out.println("RANDOM Choice !" );
		    selectedAction = (int) ( Math.random() * qValues.length );
		}
		
		return selectedAction;
    }
}

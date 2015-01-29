package org.lsmr.vendingmachine.simulator;

/**
 * A simple device that displays a string. How it does this is not part of the
 * simulation. A very long string might scroll continuously, for example.
 */
public class DisplaySimulator extends
        AbstractHardware<DisplaySimulatorListener> implements CoinReceptacleListener {
    private String message = null;

    /**
     * Tells the display to start displaying the indicated message. Announces a
     * "messageChange" event to its listeners.
     */
    public void display(String msg) {
	String oldMsg = message;
	message = msg;
	notifyMessageChange(oldMsg, msg);
    }

    /**
     * Permits the display message to be set without causing events to be
     * announced.
     */
    public void loadWithoutEvents(String message) {
	this.message = message;
    }

    private void notifyMessageChange(String oldMsg, String newMsg) {
	Class<?>[] parameterTypes =
	        new Class<?>[] { DisplaySimulator.class, String.class,
	                String.class };
	Object[] args = new Object[] { this, oldMsg, newMsg };
	notifyListeners(DisplaySimulatorListener.class, "messageChange", parameterTypes, args);
    }

	@Override
	public void enabled(AbstractHardware<AbstractHardwareListener> hardware) {
		//nothing needs to be done with display when receptacle is enabled		
	}

	@Override
	public void disabled(AbstractHardware<AbstractHardwareListener> hardware) {
		//nothing needs to be done with display when receptacle is disabled
	}

	@Override
	public void coinAdded(CoinReceptacleSimulator receptacle, Coin coin) {
		//adds the value of the added coin to the display
		if (message == null) {
			//display update when no message is set yet
			display(Double.toString(coin.getValue() * 0.01));
		} else {
			//display update when message is set
			//does not account for a proper update if message is not a number
			double prev = Double.parseDouble(message);
			display(Double.toString(prev + coin.getValue() * 0.01));
		}
	}

	@Override
	public void coinsRemoved(CoinReceptacleSimulator receptacle) {
		//all coins should be removed at once, thus the display simply gets set to 0
		display("0.00");
	}

	@Override
	public void coinsFull(CoinReceptacleSimulator receptacle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enabled(CoinReceptacleSimulator receptacle) {
		//nothing needs to be done with display when receptacle is enabled
	}

	@Override
	public void disabled(CoinReceptacleSimulator receptacle) {
		//nothing needs to be done with display when receptacle is disabled
	}
}

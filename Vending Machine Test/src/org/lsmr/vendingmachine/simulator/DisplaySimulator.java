package org.lsmr.vendingmachine.simulator;

import java.util.*;
import java.text.DecimalFormat;

/**
 * A simple device that displays a string. How it does this is not part of the
 * simulation. A very long string might scroll continuously, for example.
 */
public class DisplaySimulator extends
		AbstractHardware<DisplaySimulatorListener> implements
		CoinReceptacleListener, PopCanRackListener,
		CoinRackListener{
	private String message = null;
	private DecimalFormat df = new DecimalFormat("#.00");

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
		Class<?>[] parameterTypes = new Class<?>[] { DisplaySimulator.class,
				String.class, String.class };
		Object[] args = new Object[] { this, oldMsg, newMsg };
		notifyListeners(DisplaySimulatorListener.class, "messageChange",
				parameterTypes, args);
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void enabled(AbstractHardware<AbstractHardwareListener> hardware) {
		// nothing needs to be done with display when receptacle is enabled
	}

	@Override
	public void disabled(AbstractHardware<AbstractHardwareListener> hardware) {
		// nothing needs to be done with display when receptacle is disabled
	}

	@Override
	// Sets the display message to be the value of all coins in the receptacle
	public void coinAdded(CoinReceptacleSimulator receptacle, Coin coin) {
		display('$' + df.format((double)receptacle.getTotalValue() * 0.01));
	}

	@Override
	public void coinsRemoved(CoinReceptacleSimulator receptacle) {
		// all coins should be removed at once, thus the display simply gets set
		// to 0	
		Timer timer = new Timer();
		display("all coins are returned to user");
		timer.schedule(new TimerTask() {
			public void run() {
				display("$0.00");
			}
		}, 3000); // 3000 = time to wait in milliseconds

	}

	@Override
	public void coinsFull(CoinReceptacleSimulator receptacle) {
		// Should not need to do anything when receptacle is full

	}

	@Override
	public void enabled(CoinReceptacleSimulator receptacle) {
		// nothing needs to be done with display when receptacle is enabled
	}

	@Override
	public void disabled(CoinReceptacleSimulator receptacle) {
		// nothing needs to be done with display when receptacle is disabled
	}

	@Override
	public void popAdded(PopCanRackSimulator popRack, PopCan pop) {
		// nothing needs to be done when a pop is added

	}

	@Override
	public void popRemoved(PopCanRackSimulator popRack, PopCan pop) {
		// when a pop is being dispensed to user
		// print Pop is in delivery chute
		final String oldMsg = message;
		Timer timer = new Timer();
		display("Pop is in delivery chute");
		timer.schedule(new TimerTask() {
			public void run() {
				display(oldMsg);
			}
		}, 1000); // 2000 = time to wait in milliseconds
	}

	@Override
	public void popFull(PopCanRackSimulator popRack) {
		// nothing needs to be done when a pop rack is full

	}

	@Override
	public void popEmpty(PopCanRackSimulator popRack) {
		// when pop rack is empty display out of product message for 5 seconds
		// then display old message
//		final String oldMsg = message;
//		Timer timer = new Timer();
//		display("Out of product");
//		timer.schedule(new TimerTask() {
//			public void run() {
//				display(oldMsg);
//			}
//		}, 5000); // 5000 = time to wait in milliseconds
	}

	@Override
	public void coinsFull(CoinRackSimulator rack) {
		// does not need to do anything when coinsFull
		
	}

	@Override
	public void coinsEmpty(CoinRackSimulator rack) {
		// does not need to do anything when coinsEmtpy
		
	}

	@Override
	public void coinAdded(CoinRackSimulator rack, Coin coin) {
		// does not need to do anything when coinAdded
	}

	@Override
	public void coinRemoved(CoinRackSimulator rack, Coin coin) {
		// display value of the coin to user which means returning coins to user
		final String oldMsg = message;
		Timer timer = new Timer();
		display("Return changes: $" + df.format((double)coin.getValue()*0.01));
		timer.schedule(new TimerTask() {
			public void run() {
				display(oldMsg);
			}
		}, 2000); // 2000 = time to wait in milliseconds
		
	}
}

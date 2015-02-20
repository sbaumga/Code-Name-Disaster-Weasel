import java.util.Vector;

import lsmr.vendingmachine.simulator.AbstractHardware;
import lsmr.vendingmachine.simulator.AbstractHardwareListener;
import lsmr.vendingmachine.simulator.Coin;
import lsmr.vendingmachine.simulator.CoinRackListener;
import lsmr.vendingmachine.simulator.CoinRackSimulator;
import lsmr.vendingmachine.simulator.CoinReceptacleListener;
import lsmr.vendingmachine.simulator.CoinReceptacleSimulator;

/**
 * A class that will keep track of money used by the vending machine in all of
 * its forms
 * 
 * @author Shayne Baumgartner 10098339
 * 
 */
public class FundsAvailable implements CoinReceptacleListener, CoinRackListener {

	private Currency c;
	private Vector<FundsAvailableListener> listeners = new Vector<FundsAvailableListener>();
	private boolean hasSpace, canChange; // canChange represents if the coin
											// receptacle or rack is enabled

	public FundsAvailable(Currency curr) {
		c = curr;
		hasSpace = true;
		canChange = true;
	}

	/**
	 * Returns the currency in the coin receptacle
	 * 
	 * @return c, the value of coins in coin receptacle.
	 */
	public Currency getAvailableFunds() {
		return c;
	}

	/**
	 * Removes an amount of currency from the available funds and announces it
	 * to the listener
	 * 
	 * @param curr
	 *            , the amount of currency to be removed from c
	 */
	public void removeFunds(Currency curr) {
		if (c.getValue() - curr.getValue() >= 0 && canChange) {
			c.decreaseValue(curr);
			notifyFundsRemoved(curr);
		} else {
			notifyHardwareFailure();
		}
	}

	/**
	 * Adds an amount of currency to the available funds and notifies its
	 * listeners
	 * 
	 * @param curr
	 *            , the amount of currency to be added to c
	 */
	protected void addFunds(Currency curr) {
		if (hasSpace && canChange) {
			c.increaseValue(curr);
			notifyFundsAdded(curr);
		} else {
			notifyHardwareFailure();
		}
	}

	/**
	 * Adds an amount of currency to the available funds. Does not notify
	 * listeners Does not need to check canChange as this method will not be
	 * used in normal operation
	 * 
	 * @param curr
	 */
	public void addFundsWithoutEvent(Currency curr) {
		if (hasSpace) {
			c.increaseValue(curr);
		} else {
			notifyHardwareFailure();
		}
	}

	/**
	 * Returns an amount of currency to the customer from c and notifies its
	 * listener
	 * 
	 * @param curr
	 *            , the amount of currency to be returned.
	 */
	public void returnFunds(Currency curr) {
		if (c.getValue() - curr.getValue() >= 0 && canChange) {
			c.decreaseValue(curr);
			notifyFundsReturned();
		} else {
			notifyHardwareFailure();
		}
	}

	/**
	 * Registers a listener
	 * 
	 * @param fListener
	 *            , listener being registered
	 */
	public void register(FundsAvailableListener fListener) {
		listeners.add(fListener);
	}

	/**
	 * When cRack is empty, ensures that its available funds are 0 and hasSpace
	 * = true
	 * 
	 * @param cRack
	 *            , coin rack that is empty
	 */
	public void coinsEmpty(CoinRackSimulator cRack) {
		c.setValue(0);
		hasSpace = true;
	}

	/**
	 * When cRack is full, sets hasSpace to false
	 * 
	 * @param cRack
	 *            , coin rack that is full
	 */
	public void coinsFull(CoinRackSimulator cRack) {
		hasSpace = false;
	}

	/**
	 * When cRecept is full, sets hasSpace to false
	 * 
	 * @param cRecept
	 *            , coin receptacle that is full
	 */
	public void coinsFull(CoinReceptacleSimulator cRecept) {
		hasSpace = false;
	}

	/**
	 * Updates c with the coin added to cRecept
	 * 
	 * @param cRecept
	 *            , coin receptacle that is having a coin added to it
	 * @param coin
	 *            , coin being added
	 */
	public void coinAdded(CoinReceptacleSimulator cRecept, Coin coin) {
		Currency curr = new Currency(coin.getValue());
		addFunds(curr);
	}

	/**
	 * Updates c with the coin added to cRack
	 * 
	 * @param cRack
	 *            , coin rack that is having a coin added to it
	 * @param coin
	 *            , coin being added
	 */
	public void coinAdded(CoinRackSimulator cRack, Coin coin) {
		Currency curr = new Currency(coin.getValue());
		addFunds(curr);
	}

	/**
	 * Updates c with the coin removed from cRack
	 * 
	 * @param cRack
	 *            , coin rack that is having a coin removed
	 * @param coin
	 *            , coin being removed
	 */
	public void coinRemoved(CoinRackSimulator cRack, Coin coin) {
		Currency curr = new Currency(coin.getValue());
		removeFunds(curr);
		hasSpace = true;
	}

	/**
	 * Updates c given all coins have been removed from cRecept
	 * 
	 * @param cRecept
	 *            , coin receptacle that is having a coin removed
	 */
	public void coinsRemoved(CoinReceptacleSimulator cRecept) {
		Currency cCopy = new Currency(c.getValue());
		removeFunds(cCopy);
		hasSpace = true;
	}

	private void notifyFundsRemoved(Currency curr) {
		for (FundsAvailableListener listener : listeners) {
			listener.fundsRemoved(curr);
		}
	}

	private void notifyFundsAdded(Currency curr) {
		for (FundsAvailableListener listener : listeners) {
			listener.fundsAdded(curr);
		}
	}

	private void notifyFundsReturned() {
		for (FundsAvailableListener listener : listeners) {
			listener.fundsReturned();
		}
	}

	private void notifyHardwareFailure() {
		for (FundsAvailableListener listener : listeners) {
			listener.hardwareFailure();
		}
	}

	/**
	 * Sets canChange to true when hardware is enabled
	 */
	public void enabled(AbstractHardware<AbstractHardwareListener> hardware) {
		canChange = true;

	}

	/**
	 * Sets canChange to false when hardware is disabled
	 */
	public void disabled(AbstractHardware<AbstractHardwareListener> hardware) {
		canChange = false;

	}

	/**
	 * Sets canChange to true when receptacle is enabled
	 */
	public void enabled(CoinReceptacleSimulator receptacle) {
		canChange = true;

	}

	/**
	 * Sets canChange to false when receptacle is disabled
	 */
	public void disabled(CoinReceptacleSimulator receptacle) {
		canChange = false;
	}
}

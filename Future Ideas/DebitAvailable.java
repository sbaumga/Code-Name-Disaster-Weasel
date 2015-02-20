/**
 * @author Shayne Baumgartner, 10098339
 */
package lsmr.vendingmachine.funds;

import java.util.ArrayList;

import FundsFacade.Currency;
import lsmr.vendingmachine.simulator.AbstractHardware;
import lsmr.vendingmachine.simulator.AbstractHardwareListener;
import lsmr.vendingmachine.simulator.Card;
import lsmr.vendingmachine.simulator.CardSlotListener;
import lsmr.vendingmachine.simulator.CardSlotSimulator;
import lsmr.vendingmachine.simulator.EmptyException;

public class DebitAvailable implements CardSlotListener {
	private Card debitCard = null;
	private boolean isAvailable = false;
	private ArrayList<DebitAvailableListener> listeners = new ArrayList<DebitAvailableListener>();

	/**
	 * Returns true if there is a debit card that is usable, false otherwise
	 * 
	 * @return isAvailable, is there a usable debit card
	 */
	public boolean isDebitAvailable() {
		return isAvailable;
	}

	/**
	 * Registers a new listener
	 * 
	 * @param listener
	 *            , the listener being registered
	 */
	public void register(DebitAvailableListener listener) {
		listeners.add(listener);
	}

	/**
	 * Returns the card to the customer
	 */
	public void returnCard() {
		debitCard = null;
		isAvailable = false;
	}

	@Override
	/**
	 * Does something when arg0 is disabled
	 */
	public void disabled(AbstractHardware<AbstractHardwareListener> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * Does something when arg0 is enabled
	 */
	public void enabled(AbstractHardware<AbstractHardwareListener> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * The card is ejected from the slot and DebitAvailable is updated accordingly 
	 */
	public void cardEjected(CardSlotSimulator arg0) {
		debitCard = null;
		isAvailable = false;
	}

	/**
	 * When a card is inserted this method checks if it is a debit card. If it
	 * is then the card's data is stored. If not an InvalidCardException is
	 * thrown and DebitAvailable's parameters are set to their default values
	 * 
	 * @param arg0
	 *            , the card slot where the card was inserted
	 */
	public void cardInserted(CardSlotSimulator arg0) {
		try {
			switch (arg0.readCardData().getType()) {
			case DEBIT:
				debitCard = arg0.readCardData();
				isAvailable = true;
				break;
			case CREDIT:
			case PREPAID:
			case UNKNOWN:
				throw new InvalidCardTypeException();
			}
		} catch (EmptyException e) {
			notifyHardwareFailure();
		} catch (InvalidCardTypeException e) {
			debitCard = null;
			isAvailable = false;
		}
	}

	/**
	 * Attempts to remove an amount of currency from debitCard
	 * 
	 * @param amount
	 *            , the amount of currency being removed
	 * @param pin
	 *            , the pin being tried for debitCard
	 * @return success, true if there is enough money on the card and the pin
	 *         matches the card's pin, false otherwise
	 */
	public boolean requestFunds(Currency amount, String pin) {
		boolean success = true;
		if (debitCard.checkPin(pin)) {
			if (!debitCard.requestFunds(amount.getQuantity().intValue(), pin)) {
				try {
					throw new InsuffcientFundsException();
				} catch (InsuffcientFundsException e) {
					System.err.println("Insuffcient funds.");
					success = false;
				}
			}
		} else {
			try {
				throw new InvalidPinException();
			} catch (InvalidPinException e) {
				System.err.println("Invalid pin. Try again.");
				success = false;
			}
		}
		return success;
	}

	/**
	 * Notifies listeners of a hardware failure.
	 */
	private void notifyHardwareFailure() {
		for (DebitAvailableListener listener : listeners)
			listener.hardwareFailure(this);
	}

}

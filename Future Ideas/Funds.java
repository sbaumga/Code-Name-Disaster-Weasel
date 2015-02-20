/**
 * @author Shayne Baumgartner, 10098339
 */
package lsmr.vendingmachine.funds;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import lsmr.vendingmachine.simulator.CoinRackSimulator;
import lsmr.vendingmachine.simulator.CoinReceptacleSimulator;
import FundsFacade.Currency;

public class Funds implements CoinsAvailableListener, DebitAvailableListener {
	private CoinsAvailable coins;
	private DebitAvailable debit;
	private ArrayList<FundsListener> listeners = new ArrayList<FundsListener>();

	/**
	 * Creates a new instance of funds with coins = c and debit = d
	 * 
	 * @param c
	 *            , the value of coins
	 * @param d
	 *            , the value of debit
	 */
	public Funds(CoinsAvailable c, DebitAvailable d) {
		coins = c;
		debit = d;
	}

	/**
	 * Registers a new listener to funds
	 * 
	 * @param listener
	 *            , the listener being registered
	 */
	public void register(FundsListener listener) {
		listeners.add(listener);
	}

	/**
	 * Does something when coins are added to c
	 */
	@Override
	public void coinsAdded(CoinsAvailable c, Currency curr) {
	}

	/**
	 * Does something when coins are stored in c
	 */
	@Override
	public void coinsStored(CoinsAvailable c, Currency curr) {
		// TODO Auto-generated method stub

	}

	/**
	 * Does something when coins are returned in c
	 */
	@Override
	public void coinsReturned(CoinsAvailable c, Currency curr) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * Notifies funds listeners of a hardware failure
	 */
	public void hardwareFailure(CoinsAvailable coins) {
		notifyHardwareFailure();
	}

	@Override
	/**
	 * Notifies funds listeners of a hardware failure
	 */
	public void hardwareFailure(DebitAvailable d) {
		notifyHardwareFailure();
	}

	/**
	 * Attempts to process a payment. Checks if there are enough coins to pay
	 * for the purchase If there are not, then attempts to charge the rest of
	 * the payment to a debit card False is returned if the payment cannot be
	 * made for any reason. True is returned otherwise, the remaining coins are
	 * also release, if there are any, and the card is ejected.
	 * 
	 * @param curr
	 *            , the amount of currency needed to make the purchase
	 * @param pin
	 *            , the pin entered by the customer.
	 * @return success, true if the payment went through, false otherwise.
	 */
	public boolean storeFunds(Currency curr, String pin) {
		boolean success = false;

		// check if coins are not enough
		if (coins.getAvailableCoins().getQuantity().intValue() < curr
				.getQuantity().intValue()) {
			// check if coins and debit are enough
			Currency fromDebit = new Currency(
					java.util.Currency.getInstance(Locale.CANADA),
					new BigDecimal(curr.getQuantity().intValue()
							- coins.getAvailableCoins().getQuantity()
									.intValue()));
			if (debit.requestFunds(fromDebit, pin)) {
				coins.storeCoins(coins.getAvailableCoins()); // removes all
																// available
																// coins
				success = true;
			}
		} else {
			coins.storeCoins(curr);
			success = true;
		}

		return success;
	}

	/**
	 * Releases all remaining available funds
	 */
	public void releaseRemainingFunds() {
		Currency coinsC = new Currency(
				java.util.Currency.getInstance(Locale.CANADA), new BigDecimal(
						coins.getAvailableCoins().getQuantity().intValue()));
		coins.storeCoins(coinsC); // removes all coins
		debit.returnCard();
	}

	/**
	 * Notifies listeners of a hardware failure
	 */
	private void notifyHardwareFailure() {
		for (FundsListener listener : listeners)
			listener.hardwareFailure(this);
	}
}

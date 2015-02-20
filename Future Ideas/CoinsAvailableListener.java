/**
 * @author Shayne Baumgartner, 10098339, modified from code written by Robert Walker
 */
package lsmr.vendingmachine.funds;

import FundsFacade.Currency;

public interface CoinsAvailableListener {
	public void coinsAdded(CoinsAvailable coins, Currency curr);

	public void coinsStored(CoinsAvailable coins, Currency curr);

	public void coinsReturned(CoinsAvailable coins, Currency curr);

	public void hardwareFailure(CoinsAvailable coins);
}

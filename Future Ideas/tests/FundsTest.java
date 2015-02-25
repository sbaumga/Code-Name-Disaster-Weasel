package tests;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import FundsFacade.Currency;
import lsmr.vendingmachine.funds.CoinsAvailable;
import lsmr.vendingmachine.funds.DebitAvailable;
import lsmr.vendingmachine.funds.Funds;
import lsmr.vendingmachine.funds.FundsListener;
import lsmr.vendingmachine.simulator.CoinRackSimulator;
import lsmr.vendingmachine.simulator.CoinReceptacleSimulator;

public class FundsTest {

	/**
	 * Stub class for CoinsAvailable
	 * 
	 * @author Shayne Baumgartner, 10098339
	 * 
	 */
	class CoinsAvailableStub extends CoinsAvailable {
		private Currency coins = new Currency(
				java.util.Currency.getInstance(Locale.CANADA), new BigDecimal(
						100));

		public CoinsAvailableStub(CoinReceptacleSimulator cr,
				int[] rackDenominations, CoinRackSimulator[] racks) {
			super(cr, rackDenominations, racks);
		}

		@Override
		public Currency getAvailableCoins() {
			return coins;
		}

		@Override
		public void storeCoins(Currency curr) {
			coins = new Currency(java.util.Currency.getInstance(Locale.CANADA),
					new BigDecimal(coins.getQuantity().intValue()
							- curr.getQuantity().intValue()));
		}
	}

	/**
	 * Stub class for DebitAvailable
	 * 
	 * @author Shayne Baumgartner, 10098339
	 * 
	 */
	class DebitAvailableStub extends DebitAvailable {
		private Currency availableFunds = new Currency(
				java.util.Currency.getInstance(Locale.CANADA), new BigDecimal(
						100));
		private boolean isAvailable = true;

		@Override
		public boolean requestFunds(Currency curr, String pin) {
			if (curr.getQuantity().intValue() <= availableFunds.getQuantity()
					.intValue() && pin.equals("12345")) {
				availableFunds = new Currency(
						java.util.Currency.getInstance(Locale.CANADA),
						new BigDecimal(availableFunds.getQuantity().intValue()
								- curr.getQuantity().intValue()));
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void returnCard() {
			availableFunds = new Currency(
					java.util.Currency.getInstance(Locale.CANADA),
					new BigDecimal(0));
		}

		@Override
		public boolean isDebitAvailable() {
			return isAvailable;
		}

		public Currency getFunds() {
			return availableFunds;
		}
	}

	private CoinsAvailable coins;
	private DebitAvailable debit;
	private Funds funds;
	private FundsListenerTest testListener;

	@Before
	public void setup() {
		int[] n = new int[] { 1 };
		CoinRackSimulator[] racks = new CoinRackSimulator[n.length];
		racks[0] = new CoinRackSimulator(10);
		coins = new CoinsAvailableStub(new CoinReceptacleSimulator(10), n,
				racks);
		debit = new DebitAvailableStub();
		funds = new Funds(coins, debit);
		testListener = new FundsListenerTest();
		funds.register(testListener);
	}

	@After
	public void teardown() {
		funds = null;
		testListener = null;
	}

	/**
	 * Tests registering a new listener
	 */
	@Test
	public void testRegister() {
		funds.register(new FundsListener() {
			@Override
			public void hardwareFailure(Funds f) {
			}
		});
	}

	/**
	 * Tests all of the ignored events
	 */
	@Test
	public void testIgnoredEvents() {
		funds.coinsAdded((CoinsAvailable) null, (Currency) null);
		funds.coinsReturned((CoinsAvailable) null, (Currency) null);
		funds.coinsStored((CoinsAvailable) null, (Currency) null);
	}

	/**
	 * Tests hardware failure in CoinsAvailable
	 */
	@Test
	public void testHardwareFailureCoins() {
		boolean success = false;
		funds.hardwareFailure((CoinsAvailable) null);
		success = testListener.hardwareFailure;
		assertTrue(success);
	}

	/**
	 * Tests hardware failure in DebitAvailable
	 */
	@Test
	public void testHardwareFailureDebit() {
		boolean success = false;
		funds.hardwareFailure((DebitAvailable) null);
		success = testListener.hardwareFailure;
		assertTrue(success);
	}

	/**
	 * Tests store funds when only coins are used
	 */
	@Test
	public void testStoreFundsCoinsOnly() {
		boolean success;

		success = funds.storeFunds(
				new Currency(java.util.Currency.getInstance(Locale.CANADA),
						new BigDecimal(100)), null);
		assertTrue(success);
	}

	/**
	 * Tests store funds when only debit is used
	 */
	@Test
	public void testStoreFundsDebitOnly() {
		boolean success;

		coins.storeCoins(coins.getAvailableCoins());
		success = funds.storeFunds(
				new Currency(java.util.Currency.getInstance(Locale.CANADA),
						new BigDecimal(100)), "12345");
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests store funds when coins and debit are used
	 */
	@Test
	public void testStoreFundsBoth() {
		boolean success;

		success = funds.storeFunds(
				new Currency(java.util.Currency.getInstance(Locale.CANADA),
						new BigDecimal(150)), "12345");
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Test store funds when coins and debit are insufficient
	 */
	@Test
	public void testStoreFundsInsufficient() {
		boolean success;

		success = !funds.storeFunds(
				new Currency(java.util.Currency.getInstance(Locale.CANADA),
						new BigDecimal(250)), "12345");
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests store funds when debit is needed and the pin entered is wrong
	 */
	@Test
	public void testStoreFundsDebitWrongPin() {
		boolean success;

		success = !funds.storeFunds(
				new Currency(java.util.Currency.getInstance(Locale.CANADA),
						new BigDecimal(150)), "1245");
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Test release remaining funds
	 */
	@Test
	public void testReleaseRemainingFunds() {
		boolean success = true;

		funds.releaseRemainingFunds();
		if (coins.getAvailableCoins().getQuantity().intValue() != 0
				|| !debit.isDebitAvailable()) {
			success = false;
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * A simple test listener for funds
	 * 
	 * @author Shayne Baumgartner, 10098339
	 * 
	 */
	public class FundsListenerTest implements FundsListener {
		public boolean hardwareFailure = false;

		@Override
		public void hardwareFailure(Funds f) {
			hardwareFailure = true;
		}

	}

}
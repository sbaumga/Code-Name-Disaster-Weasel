/**
 * @author Shayne Baumgartner, 10098339, modified from code written by Robert Walker
 */
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lsmr.vendingmachine.simulator.AbstractHardware;
import lsmr.vendingmachine.simulator.AbstractHardwareListener;
import lsmr.vendingmachine.simulator.CapacityExceededException;
import lsmr.vendingmachine.simulator.Coin;
import lsmr.vendingmachine.simulator.CoinChannelSimulator;
import lsmr.vendingmachine.simulator.CoinRackSimulator;
import lsmr.vendingmachine.simulator.CoinReceptacleSimulator;
import lsmr.vendingmachine.simulator.DisabledException;
import lsmr.vendingmachine.simulator.EmptyException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import FundsFacade.Currency;
import lsmr.vendingmachine.funds.CoinsAvailable;
import lsmr.vendingmachine.funds.CoinsAvailableListener;

public class CoinsAvailableTest {
	class CoinChannelStub extends CoinChannelSimulator {
		public Coin delivered = null;
		public boolean hasSpace = true;

		public CoinChannelStub() {
			super(null);
		}

		public void deliver(Coin coin) throws CapacityExceededException,
				DisabledException {
			delivered = coin;
		}

		public boolean hasSpace() {
			return hasSpace;
		}
	}

	class CoinRackStub extends CoinRackSimulator {
		public Coin added = null;
		public boolean releaseIsCalled = false;
		public boolean hasSpace = true;
		public boolean throwInRelease = false;

		public CoinRackStub() {
			super(1);
		}

		@Override
		public void acceptCoin(Coin coin) throws CapacityExceededException,
				DisabledException {
			added = coin;
		}

		@Override
		public void releaseCoin() throws CapacityExceededException,
				EmptyException, DisabledException {
			releaseIsCalled = true;
			if (throwInRelease)
				throw new EmptyException();
		}

		@Override
		public boolean hasSpace() {
			return hasSpace;
		}
	}

	private CoinsAvailable fa;
	private CoinReceptacleSimulator receptacle;
	private int[] denominations;
	private CoinChannelStub[] racksChannels;
	private CoinChannelStub coinReturn, overflow;
	private CoinRackStub[] racks;

	@Before
	public void setup() {
		denominations = new int[] { 5, 10, 25, 100, 200 };
		receptacle = new CoinReceptacleSimulator(10);
		racksChannels = new CoinChannelStub[denominations.length];
		racks = new CoinRackStub[denominations.length];
		Map<Integer, CoinChannelSimulator> map = new HashMap<Integer, CoinChannelSimulator>();

		for (int i = 0; i < racksChannels.length; i++) {
			racksChannels[i] = new CoinChannelStub();
			map.put(new Integer(denominations[i]), racksChannels[i]);
			racks[i] = new CoinRackStub();
		}
		coinReturn = new CoinChannelStub();
		overflow = new CoinChannelStub();

		receptacle.connect(map, coinReturn, overflow);

		fa = new CoinsAvailable(receptacle, denominations, racks);
	}

	@After
	public void teardown() {
		fa = null;
		receptacle = null;
		denominations = null;
		racks = null;
		racksChannels = null;
		coinReturn = null;
		overflow = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBadSetup() {
		new CoinsAvailable(receptacle, null, racks);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBadSetup2() {
		new CoinsAvailable(receptacle, denominations, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBadSetup3() {
		new CoinsAvailable(receptacle, new int[] {}, racks);
	}

	@Test
	public void testRegister() {
		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
			}
		});
	}

	@Test
	public void testIgnoredEvents() {
		fa.enabled((CoinReceptacleSimulator) null);
		fa.disabled((CoinReceptacleSimulator) null);
		fa.enabled((AbstractHardware<AbstractHardwareListener>) null);
		fa.disabled((AbstractHardware<AbstractHardwareListener>) null);
		fa.coinsFull((CoinReceptacleSimulator) null);
		fa.coinsFull((CoinRackSimulator) null);
		fa.coinsEmpty(null);
		fa.coinAdded((CoinRackSimulator) null, null);
		fa.coinRemoved((CoinRackSimulator) null, null);
		fa.coinsRemoved(null);
	}

	@Test
	public void testCoinAdded() {
		fa.coinAdded(receptacle, new Coin(1));
		assertEquals(1, fa.getAvailableCoins().getQuantity().intValue());
	}

	static class MutableBoolean {
		public boolean value;

		public MutableBoolean(boolean value) {
			this.value = value;
		}

		public void set() {
			value = true;
		}
	}

	@Test
	public void testBadCoinAdded() {
		final MutableBoolean flag = new MutableBoolean(false);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				flag.set();
			}
		});
		fa.coinAdded((CoinReceptacleSimulator) null, new Coin(1));
		assertEquals(true, flag.value);
	}

	@Test
	public void testCoinAdded2() {
		final MutableBoolean flag = new MutableBoolean(false);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				flag.set();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				throw new RuntimeException();
			}
		});
		fa.coinAdded(receptacle, new Coin(1));
		assertEquals(true, flag.value);
	}

	@Test
	public void testFundsAdded() {
		final MutableBoolean flag = new MutableBoolean(false);

		class FA extends CoinsAvailable {
			public FA(CoinReceptacleSimulator cr, int[] rackDenominations,
					CoinRackSimulator[] racks) {
				super(cr, rackDenominations, racks);
			}

			public void addCoins(Currency curr) {
				super.addCoins(curr);
			}
		}

		FA localFA = new FA(receptacle, denominations, racks);

		localFA.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				flag.set();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				throw new RuntimeException();
			}
		});

		localFA.addCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(15)));
		assertEquals(true, flag.value);
	}

	@Test
	public void testRemoveFundsExact() throws CapacityExceededException,
			DisabledException {
		final MutableBoolean flag = new MutableBoolean(false);

		Coin[] coins = new Coin[] { new Coin(5), new Coin(10), new Coin(25) };
		receptacle.acceptCoin(coins[0]);
		receptacle.acceptCoin(coins[1]);
		receptacle.acceptCoin(coins[2]);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				flag.set();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				throw new RuntimeException();
			}
		});

		fa.storeCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(40)));

		assertEquals(true, flag.value);
		assertEquals(coins[0], racksChannels[0].delivered);
		assertEquals(coins[1], racksChannels[1].delivered);
		assertEquals(coins[2], racksChannels[2].delivered);
		assertEquals(null, racksChannels[3].delivered);
		assertEquals(null, racksChannels[4].delivered);
		assertEquals(null, overflow.delivered);
		assertEquals(null, coinReturn.delivered);
		assertEquals(0, fa.getAvailableCoins().getQuantity().intValue());
	}

	@Test
	public void testRemoveFundsLess() throws CapacityExceededException,
			DisabledException {
		final MutableBoolean flag = new MutableBoolean(false);

		Coin[] coins = new Coin[] { new Coin(5), new Coin(10), new Coin(25) };
		receptacle.acceptCoin(coins[0]);
		receptacle.acceptCoin(coins[1]);
		receptacle.acceptCoin(coins[2]);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				flag.set();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				throw new RuntimeException();
			}
		});

		fa.storeCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(35)));

		assertEquals(true, flag.value);
		assertEquals(coins[0], racksChannels[0].delivered);
		assertEquals(coins[1], racksChannels[1].delivered);
		assertEquals(coins[2], racksChannels[2].delivered);
		assertEquals(null, racksChannels[3].delivered);
		assertEquals(null, racksChannels[4].delivered);
		assertEquals(null, overflow.delivered);
		assertEquals(null, coinReturn.delivered);
		assertEquals(5, fa.getAvailableCoins().getQuantity().intValue());
	}

	@Test
	public void testRemoveFundsMore() throws CapacityExceededException,
			DisabledException {
		final MutableBoolean flag = new MutableBoolean(false);

		Coin[] coins = new Coin[] { new Coin(5), new Coin(10), new Coin(25) };
		receptacle.acceptCoin(coins[0]);
		receptacle.acceptCoin(coins[1]);
		receptacle.acceptCoin(coins[2]);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				flag.set();
			}
		});

		fa.storeCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(45)));
		assertEquals(true, flag.value);
		assertEquals(null, racksChannels[0].delivered);
		assertEquals(null, racksChannels[1].delivered);
		assertEquals(null, racksChannels[2].delivered);
		assertEquals(null, racksChannels[3].delivered);
		assertEquals(null, racksChannels[4].delivered);
		assertEquals(null, overflow.delivered);
		assertEquals(null, coinReturn.delivered);
		assertEquals(40, fa.getAvailableCoins().getQuantity().intValue());
	}

	@Test
	public void testReturnFundsExact() throws CapacityExceededException,
			DisabledException {
		final MutableBoolean flag = new MutableBoolean(false);

		Coin[] coins = new Coin[] { new Coin(5), new Coin(10), new Coin(25) };
		receptacle.acceptCoin(coins[0]);
		receptacle.acceptCoin(coins[1]);
		receptacle.acceptCoin(coins[2]);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				flag.set();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				throw new RuntimeException();
			}
		});

		fa.returnCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(40)));

		assertEquals(true, flag.value);
		assertEquals(null, racksChannels[0].delivered);
		assertEquals(null, racksChannels[1].delivered);
		assertEquals(null, racksChannels[2].delivered);
		assertEquals(null, racksChannels[3].delivered);
		assertEquals(null, racksChannels[4].delivered);
		assertEquals(null, overflow.delivered);
		assertNotNull(coinReturn.delivered);
		assertEquals(0, fa.getAvailableCoins().getQuantity().intValue());
	}

	@Test
	public void testReturnFundsLess() throws CapacityExceededException,
			DisabledException {
		final MutableBoolean flag = new MutableBoolean(false);

		Coin[] coins = new Coin[] { new Coin(5), new Coin(10), new Coin(25) };
		receptacle.acceptCoin(coins[0]);
		receptacle.acceptCoin(coins[1]);
		receptacle.acceptCoin(coins[2]);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				flag.set();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				throw new RuntimeException();
			}
		});

		fa.returnCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(5)));

		assertEquals(true, flag.value);
		assertEquals(coins[0], racksChannels[0].delivered);
		assertEquals(coins[1], racksChannels[1].delivered);
		assertEquals(coins[2], racksChannels[2].delivered);
		assertEquals(null, racksChannels[3].delivered);
		assertEquals(null, racksChannels[4].delivered);
		assertEquals(null, overflow.delivered);
		assertEquals(true, racks[0].releaseIsCalled);
		assertEquals(35, fa.getAvailableCoins().getQuantity().intValue());
	}

	@Test
	public void testReturnFundsMore() throws CapacityExceededException,
			DisabledException {
		final MutableBoolean flag = new MutableBoolean(false);

		Coin[] coins = new Coin[] { new Coin(5), new Coin(10), new Coin(25) };
		receptacle.acceptCoin(coins[0]);
		receptacle.acceptCoin(coins[1]);
		receptacle.acceptCoin(coins[2]);

		fa.register(new CoinsAvailableListener() {

			@Override
			public void coinsAdded(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsStored(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void coinsReturned(CoinsAvailable coins, Currency curr) {
				throw new RuntimeException();
			}

			@Override
			public void hardwareFailure(CoinsAvailable coins) {
				flag.set();
			}
		});

		fa.returnCoins(new Currency(java.util.Currency
				.getInstance(Locale.CANADA), new BigDecimal(45)));

		assertEquals(true, flag.value);
		assertEquals(null, racksChannels[0].delivered);
		assertEquals(null, racksChannels[1].delivered);
		assertEquals(null, racksChannels[2].delivered);
		assertEquals(null, racksChannels[3].delivered);
		assertEquals(null, racksChannels[4].delivered);
		assertEquals(null, overflow.delivered);
		assertEquals(false, racks[0].releaseIsCalled);
		assertEquals(false, racks[1].releaseIsCalled);
		assertEquals(false, racks[2].releaseIsCalled);
		assertEquals(40, fa.getAvailableCoins().getQuantity().intValue());
	}
}

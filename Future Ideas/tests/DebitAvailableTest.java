/**
 * @author Shayne Baumgartner, 10098339
 */
package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Locale;

import lsmr.vendingmachine.funds.DebitAvailable;
import lsmr.vendingmachine.funds.DebitAvailableListener;
import lsmr.vendingmachine.simulator.AbstractHardware;
import lsmr.vendingmachine.simulator.AbstractHardwareListener;
import lsmr.vendingmachine.simulator.Card;
import lsmr.vendingmachine.simulator.CardSlotNotEmptyException;
import lsmr.vendingmachine.simulator.CardSlotSimulator;
import lsmr.vendingmachine.simulator.DisabledException;
import lsmr.vendingmachine.simulator.EmptyException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import FundsFacade.Currency;

public class DebitAvailableTest {
	private DebitAvailable debit;
	private DebitAvailableListenerTest testListener;

	@Before
	public void setup() {
		debit = new DebitAvailable();
		testListener = new DebitAvailableListenerTest();
		debit.register(testListener);
	}

	@After
	public void teardown() {
		debit = null;
		testListener = null;
	}

	/**
	 * Tests the register method
	 */
	@Test
	public void testRegister() {
		debit.register(new DebitAvailableListener() {
			@Override
			public void hardwareFailure(DebitAvailable d) {
			}
		});
	}

	/**
	 * Tests methods for ignored events
	 */
	@Test
	public void testIgnoredEvents() {
		debit.enabled((AbstractHardware<AbstractHardwareListener>) null);
		debit.disabled((AbstractHardware<AbstractHardwareListener>) null);
	}

	/**
	 * Tests if debit is available when a card that is not a debit card is
	 * inserted
	 */
	@Test
	public void testWrongCardType() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.PREPAID, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
		}
		if (success) {
			success = !debit.isDebitAvailable();
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests if debit is available when a debit card is inserted
	 */
	@Test
	public void testDebitInserted() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.DEBIT, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
		}
		if (success) {
			success = debit.isDebitAvailable();
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests if debit is available when no card is inserted
	 */
	@Test
	public void testNoCardInserted() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		debit.cardInserted(slot);
		if (success) {
			success = !debit.isDebitAvailable();
		}
		if (!testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests if a card can be ejected
	 */
	@Test
	public void testCardEjectedNormal() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.DEBIT, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
			fail();
			success = false;
		}
		if (success) {
			try {
				slot.ejectCard();
			} catch (EmptyException | DisabledException e) {
			}
			if (success) {
				success = !debit.isDebitAvailable();
			}
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests if a card can be ejected when there is no card
	 */
	@Test
	public void testCardEjectedEmpty() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		slot.register(debit);
		if (success) {
			try {
				slot.ejectCard();
			} catch (EmptyException | DisabledException e) {
			}
			if (success) {
				success = !debit.isDebitAvailable();
			}
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests if a card can be returned
	 */
	@Test
	public void testReturnCardNormal() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.DEBIT, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
			fail();
			success = false;
		}
		if (success) {
			try {
				slot.ejectCard();
			} catch (EmptyException | DisabledException e) {
			}
			if (success) {
				success = !debit.isDebitAvailable();
			}
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests if a card can be ejected when there is no card
	 */
	@Test
	public void testReturnCardEmpty() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		slot.register(debit);
		if (success) {
			try {
				slot.ejectCard();
			} catch (EmptyException | DisabledException e) {
			}
			if (success) {
				success = !debit.isDebitAvailable();
			}
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests request funds when there is a debit card inserted, the right pin is
	 * entered and the card has the requested amount available
	 */
	@Test
	public void testRequestFundsNormal() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.DEBIT, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
		}
		if (success) {
			success = debit.isDebitAvailable();
		}
		Currency curr = new Currency(
				java.util.Currency.getInstance(Locale.CANADA), new BigDecimal(
						100));
		if (success) {
			success = debit.requestFunds(curr, "12345");
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests request funds when there is not enough funds available
	 */
	@Test
	public void testRequestInsufficientFunds() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.DEBIT, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
		}
		if (success) {
			success = debit.isDebitAvailable();
		}
		Currency curr = new Currency(
				java.util.Currency.getInstance(Locale.CANADA), new BigDecimal(
						400));
		if (success) {
			success = !debit.requestFunds(curr, "12345");
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * Tests request funds when the pin entered is incorrect
	 */
	@Test
	public void testRequestFundsWrongPin() {
		boolean success = !debit.isDebitAvailable();
		CardSlotSimulator slot = new CardSlotSimulator();
		Card card = new Card(Card.CardType.DEBIT, "12345", "John Smith",
				"12345", 300);
		slot.register(debit);
		try {
			slot.insertCard(card);
		} catch (CardSlotNotEmptyException | DisabledException e) {
		}
		if (success) {
			success = debit.isDebitAvailable();
		}
		Currency curr = new Currency(
				java.util.Currency.getInstance(Locale.CANADA), new BigDecimal(
						100));
		if (success) {
			success = !debit.requestFunds(curr, "54321");
		}
		if (testListener.hardwareFailure) {
			success = false;
		}
		assertTrue(success);
	}

	/**
	 * A simple test listener
	 * 
	 * @author Shayne Baumgartner, 10098339
	 * 
	 */
	public class DebitAvailableListenerTest implements DebitAvailableListener {
		public boolean hardwareFailure = false;

		@Override
		public void hardwareFailure(DebitAvailable d) {
			hardwareFailure = true;
		}

	}

}
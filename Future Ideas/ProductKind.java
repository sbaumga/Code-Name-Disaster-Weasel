import java.util.Vector;

import lsmr.vendingmachine.simulator.AbstractHardware;
import lsmr.vendingmachine.simulator.PopCan;
import lsmr.vendingmachine.simulator.PopCanRackListener;
import lsmr.vendingmachine.simulator.PopCanRackSimulator;

/**
 * A class that handles information on a type of product stored and sold by the
 * vending machine
 * 
 * @author Shayne Baumgartner, 10098339
 * 
 */
public class ProductKind implements PopCanRackListener {
	private Currency cost;
	private String name;
	private Vector<ProductKindListener> listeners = new Vector<ProductKindListener>();
	private int quantity;

	/**
	 * Creates a new instance of ProductKind
	 * 
	 * @param curr
	 *            , the value of cost
	 * @param n
	 *            , the name of the product
	 * @param q
	 *            , the quantity of the product
	 */
	public ProductKind(Currency curr, String n, int q) {
		cost = curr;
		name = n;
		quantity = q;
	}

	/**
	 * Returns the number of this product available
	 * 
	 * @return quantity, the number of product available
	 */
	public int getQuantityAvailable() {
		return quantity;
	}

	/**
	 * Returns the cost of the product
	 * 
	 * @return cost, the cost of the product
	 */
	public Currency getStandardCost() {
		return cost;
	}

	/**
	 * Returns the name of the product
	 * 
	 * @return name, the name of the product
	 */
	public String getName() {
		return name;
	}

	/**
	 * Dispenses one product
	 */
	public void dispense() {
		if (quantity > 0) {
			quantity -= 1;
			if (quantity == 0) {
				notifyOutOfStock();
			}
		} else {
			notifyOutOfStock();
		}
	}

	/**
	 * Changes the value of cost
	 * 
	 * @param curr
	 *            , the new value of cost
	 */
	protected void setStandardCost(Currency curr) {
		cost = curr;
	}

	/**
	 * Changes the name of the product
	 * 
	 * @param n
	 *            , the new name of the product
	 */
	protected void setName(String n) {
		name = n;
	}

	protected void setQuantity(int q) {
		quantity = q;
	}

	/**
	 * Registers a new listener
	 * 
	 * @param pListener
	 *            , the listener being registered
	 */
	public void register(ProductKindListener pListener) {
		listeners.add(pListener);
	}

	/**
	 * Does nothing as one product can be stored in multiple racks
	 * 
	 * @param hardware
	 *            , the hardware that is enabled
	 */
	public void enabled(AbstractHardware hardware) {

	}

	/**
	 * Does nothing as one product can be stored in multiple racks
	 * 
	 * @param hardware
	 *            , the hardware that is disabled
	 */
	public void disabled(AbstractHardware hardware) {

	}

	/**
	 * Updates quantity of pop available after can has been added to pRack
	 * 
	 * @param pRack
	 *            , pop rack having a can of pop added to it
	 * @param can
	 *            , the pop can being added to the rack
	 */
	public void popAdded(PopCanRackSimulator pRack, PopCan can) {
		quantity += 1;
	}

	/**
	 * Updates quantity after can is removed from pRack
	 * 
	 * @param pRack
	 *            , pop rack having a can of pop removed from it
	 * @param can
	 *            , the pop can being removed from the rack
	 */
	public void popRemoved(PopCanRackSimulator pRack, PopCan can) {
		if (quantity > 0) {
			quantity -= 1;
			if (quantity == 0) {
				notifyOutOfStock();
			}
		} else {
			notifyOutOfStock();
		}
	}

	/**
	 * Does nothing as a product can be stored in multiple racks
	 * 
	 * @param pRack
	 *            , the pop can rack that is empty
	 */
	public void popEmpty(PopCanRackSimulator pRack) {

	}

	/**
	 * Does nothing as product can be stored in multiple racks
	 * 
	 * @param pRack
	 *            , the pop can rack that is full
	 */
	public void popFull(PopCanRackSimulator pRack) {

	}

	private void notifyOutOfStock() {
		for (ProductKindListener listener : listeners) {
			listener.outOfStock();
		}
	}

	private void notifyHardwareFailure() {
		for (ProductKindListener listener : listeners) {
			listener.hardwareFailure();
		}
	}
}

/**
 * A listener interface for the ProductKind class
 * 
 * @author Shayne Baumgartner, 10098339
 * 
 */
public interface ProductKindListener {

	/**
	 * Announces that a product is out of stock
	 */
	public void outOfStock();

	/**
	 * Announces a hardware failure
	 */
	public void hardwareFailure();
}

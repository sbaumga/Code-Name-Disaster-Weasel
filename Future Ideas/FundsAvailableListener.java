/**
 * A listener interface for the FundsAvailable class
 * 
 * @author Shayne Baumgartner, 10098339
 * 
 */
public interface FundsAvailableListener {

	/**
	 * Announces that currency has been added
	 * 
	 * @param curr
	 *            , the currency that has been added
	 */
	public void fundsAdded(Currency curr);

	/**
	 * Announces that currency has been removed
	 * 
	 * @param curr
	 *            , the currency that has been removed
	 */
	public void fundsRemoved(Currency curr);

	/**
	 * Announces that funds have been removed
	 */
	public void fundsReturned();

	/**
	 * Announces that the hardware has failed
	 */
	public void hardwareFailure();
}

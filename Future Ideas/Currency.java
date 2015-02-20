/**
 * Class that represents currency stored and used by the vending machine
 * 
 * @author Shayne Baumgartner, 10098339
 * 
 */
public class Currency {
	private int value;

	public Currency(int v) {
		value = v;
	}

	public void setValue(int v) {
		value = v;
	}

	public int getValue() {
		return value;
	}

	public void increaseValue(int v) {
		value += v;
	}

	public void increaseValue(Currency curr) {
		int v = curr.getValue();
		increaseValue(v);
	}

	public void decreaseValue(int v) {
		value -= v;
	}

	public void decreaseValue(Currency curr) {
		int v = curr.getValue();
		decreaseValue(v);
	}
}

/**
 * 
 */
package kalmanovich.shai.utils;

/**
 * @author Shai Kalmanovich
 *
 */
public class Pair<F, S> {

	private F _first;
	private S _second;
	
	public Pair(F first, S second) {
		super();
		this._first = first;
		this._second = second;
	}

	public F getFirst() {
		return _first;
	}

	public void setFirst(F _first) {
		this._first = _first;
	}

	public S getSecond() {
		return _second;
	}

	public void setSecond(S _second) {
		this._second = _second;
	}
	
}
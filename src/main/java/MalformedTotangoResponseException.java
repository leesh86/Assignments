/**
 * @author Lee Shoham
 *
 * 15 Nov 2017
 */

/**
 * The Class MalformedTotangoResponseException.
 */
@SuppressWarnings("serial")
public class MalformedTotangoResponseException extends RuntimeException {
	
	private String message;

	/**
	 * Instantiates a new malformed totango response exception.
	 *
	 * @param msg the exceptions' message
	 */
	public MalformedTotangoResponseException(String msg) {
		this.message = msg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

}

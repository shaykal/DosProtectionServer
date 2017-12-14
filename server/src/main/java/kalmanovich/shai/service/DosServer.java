/**
 * 
 */
package kalmanovich.shai.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kalmanovich.shai.utils.Consts;
import kalmanovich.shai.utils.Pair;

/**
 * @author Shai Kalmanovich
 *
 */
public class DosServer extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -487023627062666917L;

	private Logger m_logger = Logger.getLogger("kalmanovich.shai.DosServer");
	
	// This map will hold the client Id as the key, and the value is a pair of the timestamp (in milliseconds) and the number of requests
	private Map<Integer, Pair<Long, Integer>> requestPerClientMap = new HashMap<>();
	
	/**
	 * <i>doGet</i> - The main method that handles the 'GET' request.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		final String clientIdStr = req.getParameter(Consts.CLIENT_ID);
		if(clientIdStr == null || clientIdStr.isEmpty()){
			m_logger.severe("Client id is missing");
			return;
		}
		
		Integer clientId;
		try{
			clientId = Integer.valueOf(clientIdStr);
		} catch (NumberFormatException nfe) {
			m_logger.severe("Client id is not a number: " + clientIdStr);
			return;
		}
		m_logger.info("Got clientId: " + clientId);
		
		boolean isClientSuccess = checkAndUpdateClientRequest(clientId);
		if(isClientSuccess) {
			m_logger.info("Returning " + HttpServletResponse.SC_OK + " for clientId: " + clientId);
			res.setStatus(HttpServletResponse.SC_OK); 
		} else {
			m_logger.info("Returning " + HttpServletResponse.SC_SERVICE_UNAVAILABLE + " for clientId: " + clientId);
			res.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
	}


	// This method does all the main logic of the DOS prevention 
	private boolean checkAndUpdateClientRequest(final Integer clientId) {
		boolean answer = false;
		final long now = System.currentTimeMillis();
		final Pair<Long, Integer> curClient = requestPerClientMap.get(clientId);
		if(curClient == null){ // new client
			final Pair<Long, Integer> newClientInfo = new Pair<Long, Integer>(now, 1);
			requestPerClientMap.put(clientId, newClientInfo);
			answer = true;
		} else { // client exists
			final long timeOfClient = curClient.getFirst();
			if(now - timeOfClient > Consts.TIME_FRAME_MILLI_SECONDS){ // more than 5 seconds passed, need to overwrite with time now and number of request 1 
				final Pair<Long, Integer> newClientInfo = new Pair<Long, Integer>(now, 1);
				requestPerClientMap.put(clientId, newClientInfo);
				answer = true;
			} else { // less than 5 seconds passed. need to check number of requests for this client
				final Integer numOfRequests = curClient.getSecond();
				if(numOfRequests > Consts.NUM_OF_REQ_PER_CLIENT){ // need to return error code
					answer = false;
				} else { // need to update the number of requests and maintain the original timestamp
					final Pair<Long, Integer> clientInfo = new Pair<Long, Integer>(timeOfClient, numOfRequests + 1);
					requestPerClientMap.put(clientId, clientInfo);
					answer = true;
				}
			}
		}
		return answer;
	}
	
}
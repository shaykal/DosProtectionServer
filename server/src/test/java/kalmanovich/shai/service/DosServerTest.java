/**
 * 
 */
package kalmanovich.shai.service;

import org.junit.Assert;
import org.junit.Test;

import kalmanovich.shai.utils.Consts;


/**
 * @author Shai Kalmanovich
 *
 */
public class DosServerTest {

	@Test
	public void checkAndUpdateClientRequest_gets1Request_returnsTrue() {
		DosServer ds = new DosServer();
		boolean answer = ds.checkAndUpdateClientRequest(1);
		Assert.assertTrue(answer);
	}
	
	
	@Test
	public void checkAndUpdateClientRequest_gets6Request_returns5TrueAnd1False() {
		final DosServer ds = new DosServer();
		
		for(int i=1; i <= 5; i++){
			boolean answer = ds.checkAndUpdateClientRequest(1);
			Assert.assertTrue(answer);
		}
		
		boolean answer = ds.checkAndUpdateClientRequest(1);
		Assert.assertFalse(answer);
	}
	
	
	@Test
	public void checkAndUpdateClientRequest_gets6RequestThenWait5Seconds_returns5TrueAnd1FalseAndAfter5SecondsReturnsTrue() throws Exception {
		final DosServer ds = new DosServer();
		
		for(int i=1; i <= 5; i++){
			boolean answer = ds.checkAndUpdateClientRequest(2);
			Assert.assertTrue(answer);
		}
		
		boolean answer = ds.checkAndUpdateClientRequest(2);
		Assert.assertFalse(answer);
		
		Thread.sleep(Consts.TIME_FRAME_MILLI_SECONDS + 100); // make sure 5 seconds passed
		
		answer = ds.checkAndUpdateClientRequest(2);
		Assert.assertTrue(answer);
	}
	
}
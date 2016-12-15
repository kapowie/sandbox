package sandbox;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpSpeedTest {

	private static String URI = "URL";
	
	private static long max;
	private static long total;
	private static long slow;
	
	public static void main(String...args) throws Exception {
		
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) 
		{
			while(true) {
				requestTimer(httpClient); 
			}
		}
	}

	private static void requestTimer(CloseableHttpClient httpClient) throws IOException, ClientProtocolException {
		long start = System.currentTimeMillis();
		HttpGet getResource = new HttpGet(URI);
		
		try (CloseableHttpResponse response = httpClient.execute(getResource)) {
			if (response.getStatusLine().getStatusCode() == 200){
				String size = FileUtils.byteCountToDisplaySize(response.getEntity().getContentLength());
				total++;
				long responseTime = System.currentTimeMillis() - start;
				if (responseTime > 1000) {
					slow++;
					if (responseTime > max) {
						max = responseTime;
					}
					System.out.println(String.format("Response-time: %5s,  Size: %5s,  Slow requests (>1s): %3s/%4s,  Max: %5s", responseTime, size, slow, total, max));
				}
			}
		}
		catch (Exception e) {
			// we don't care
		}
	}
	
}
 
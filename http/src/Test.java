import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 调用请求STA
		
		String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "<soap12:Body>"
				+ " <GetBankList xmlns=\"http://IpsBankList\">"
				+ " <MerCode>035754</MerCode>"
				+ " <SignMD5>73C69D13F22486EF41624CA19B418FF2</SignMD5>"
				+ " </GetBankList>" + " </soap12:Body></soap12:Envelope>";

		System.out.println(soapRequestData);

		PostMethod postMethod = new PostMethod(
				"http://webservice.ips.com.cn/web/Service.asmx?op=GetBankList");

		byte[] b = soapRequestData.getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(b, 0, b.length);
		RequestEntity re = new InputStreamRequestEntity(is, b.length,
				"application/soap+xml; charset=utf-8");
		postMethod.setRequestEntity(re);

		HttpClient httpClient = new HttpClient();
		int statusCode = httpClient.executeMethod(postMethod);
		System.out.println("statusCode=====" + statusCode);
		soapRequestData = postMethod.getResponseBodyAsString();

		System.out.println(soapRequestData);

		// 调用请求END
	}

}

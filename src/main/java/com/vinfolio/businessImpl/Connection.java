package com.vinfolio.businessImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.SOAPHeaderElement;

import com.netsuite.webservices.platform.core_2016_2.DataCenterUrls;
import com.netsuite.webservices.platform.core_2016_2.Passport;
import com.netsuite.webservices.platform.core_2016_2.RecordRef;
import com.netsuite.webservices.platform.core_2016_2.TokenPassport;
import com.netsuite.webservices.platform.core_2016_2.TokenPassportSignature;
import com.netsuite.webservices.platform.core_2016_2.types.SignatureAlgorithm;
import com.netsuite.webservices.platform.messages_2016_2.ApplicationInfo;
import com.netsuite.webservices.platform.messages_2016_2.Preferences;
import com.netsuite.webservices.platform.messages_2016_2.SearchPreferences;
import com.netsuite.webservices.platform_2016_2.NetSuiteBindingStub;
import com.netsuite.webservices.platform_2016_2.NetSuitePortType;
import com.netsuite.webservices.platform_2016_2.NetSuiteServiceLocator;
import com.vinfolio.Logger.Logger;
import com.vinfolio.business.IConnection;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Connection implements IConnection {

	/**
	 * Requested page size for search
	 */
	private int _pageSize;
	/**
	 * Utility for logging to console
	 */
	private Logger _logger;
	/**
	 * Abstraction of the external properties file that contains configuration
	 * parameters and sample data for fields
	 */
	private Properties _properties = null;
	/**
	 * Flag saying whether authentication is token based.
	 */
	private boolean useTba;
	/**
	 * Stores authentication information
	 */
	private Passport passport;
	/**
	 * Proxy class that abstracts the communication with the NetSuite Web
	 * Services. All NetSuite operations are invoked as methods of this class.
	 */
	private NetSuitePortType _port;

	/**
	 * Search body fields only
	 */
	private Boolean _bodyFieldsOnly = Boolean.FALSE;

	/**
	 * Constat to keep current version.
	 */
	private static final String CURRENT_VERSION = "2016_2";
	/**
	 * Constants for generating TBA
	 */
	private static final String RANDOM_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final Random RANDOM = new Random();

	public Connection(){
	
}
	
	public NetSuitePortType connectNetsuite() {
		try {
			this.getPort();
			this.setPreferences();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _port;

	}

	private NetSuitePortType setPreferences() throws SOAPException, UnsupportedEncodingException {
		// Cast your login NetSuitePortType variable to a NetSuiteBindingStub
		NetSuiteBindingStub stub = (NetSuiteBindingStub) _port;

		// Clear the headers to make sure you know exactly what you are sending.
		// Headers do not overwrite when you are using Axis/Java
		stub.clearHeaders();

		if (!useTba) {
			// Create request level login user passport header
			SOAPHeaderElement userPassportHeader = new SOAPHeaderElement(
					"urn:messages.platform.webservices.netsuite.com", "passport");
			userPassportHeader.setObjectValue(prepareLoginPassport());
			stub.setHeader(userPassportHeader);
			stub.setHeader(createApplicationIdHeaders());
		} else {
			stub.setHeader(createTbaLoginHeaders());
		}

		// Create a new SOAPHeaderElement, this is what the NetSuiteBindingStub
		// will accept
		// This is the same command for all preference elements, ie you might
		// substitute "useDefaults" for "searchPreferences"
		SOAPHeaderElement searchPrefHeader = new SOAPHeaderElement("urn:messages.platform.webservices.netsuite.com",
				"searchPreferences");

		// Create your Actual SearchPreference Object, this contains the
		// elements you are allowed to set.
		// In this case it is PageSize (for pagination of searches) and
		// BodyFieldsOnly (reserved)
		SearchPreferences searchPrefs = new SearchPreferences();
		searchPrefs.setPageSize(new Integer(_pageSize));
		searchPrefs.setBodyFieldsOnly(_bodyFieldsOnly);

		// setObjectValue applies search preference object to the HeaderElement
		searchPrefHeader.setObjectValue(searchPrefs);

		// Create another SOAPHeaderElement to store the preference
		// ignoreReadOnlyFields
		// This preference is used for the initialize method call. See the
		// comments for the transformEstimateToSO() method for details.
		SOAPHeaderElement platformPrefHeader = new SOAPHeaderElement("urn:messages.platform.webservices.netsuite.com",
				"preferences");
		Preferences pref = new Preferences();
		pref.setIgnoreReadOnlyFields(Boolean.TRUE);
		platformPrefHeader.setObjectValue(pref);

		// setHeader applies the Header Element to the stub
		// Again, note that if you reuse your NetSuitePort object (vs logging in
		// before every request)
		// that headers are sticky, so if in doubt, call clearHeaders() first.
		stub.setHeader(searchPrefHeader);
		stub.setHeader(platformPrefHeader);
		
		return _port;
	}

	/**
	 * Prepare loing passport for request level user login.
	 *
	 *
	 */
	public Passport prepareLoginPassport() {

		if (passport == null) {
			// Populate Passport object with all login information
			passport = new Passport();
			RecordRef role = new RecordRef();

			/*
			 * // Determine whether to get login information from config // file
			 * or prompt for it if
			 * ("true".equals(_properties.getProperty("promptForLogin"))) {
			 * _logger.writeLn("\nPlease enter your login information: ");
			 * System.out.print("  E-mail: ");
			 * passport.setEmail(_console.readLine());
			 * System.out.print("  Password: ");
			 * passport.setPassword(_console.readPassword()); System.out
			 * .print("  Role internal ID/nsKey (press Enter for default administrator role): "
			 * ); role.setInternalId(_console.readLine());
			 * passport.setRole(role); System.out.print("  Account: ");
			 * passport.setAccount(_console.readLine()); } else {
			 */
			passport.setEmail(_properties.getProperty("login.email"));
			passport.setPassword(_properties.getProperty("login.password"));
			role.setInternalId(_properties.getProperty("login.roleNSkey"));
			passport.setRole(role);
			passport.setAccount(_properties.getProperty("login.acct"));
			// }
		}

		return passport;
	}

	/**
	 * Creates header element for application id
	 * 
	 * @return header element with application id
	 * @throws UnsupportedEncodingException
	 */
	private SOAPHeaderElement createApplicationIdHeaders() throws UnsupportedEncodingException {
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setApplicationId(_properties.getProperty("login.appId"));
		return new SOAPHeaderElement("urn:messages_" + CURRENT_VERSION + ".platform.webservices.netsuite.com",
				"applicationInfo", applicationInfo);
	}

	/**
	 * Computes all TBA values and creates header element from them.
	 * 
	 * @return header Element for TBA
	 * @throws UnsupportedEncodingException
	 */
	private SOAPHeaderElement createTbaLoginHeaders() throws UnsupportedEncodingException {
		// Create random string and timestamp to generate TBA signature
		String nonce = createRandomAlphaNumericString(20);
		Long timestamp = System.currentTimeMillis() / 1000L;

		// String to be encrypted
		String baseString = URLEncoder.encode(_properties.getProperty("login.acct"), "UTF-8") + "&"
				+ URLEncoder.encode(_properties.getProperty("login.tbaConsumerKey"), "UTF-8") + "&"
				+ URLEncoder.encode(_properties.getProperty("login.tbaTokenId"), "UTF-8") + "&"
				+ URLEncoder.encode(nonce, "UTF-8") + "&" + URLEncoder.encode(Long.toString(timestamp), "UTF-8");

		// Encryption key consists of customer secret and token secret
		String key = URLEncoder.encode(_properties.getProperty("login.tbaConsumerSecret"), "UTF-8") + '&'
				+ URLEncoder.encode(_properties.getProperty("login.tbaTokenSecret"), "UTF-8");
		// Create signature by encypting baseString with key
		String signature = hmacSha(baseString, key, "HmacSHA1");

		// Create TokenPassport to add to the header
		TokenPassport tokenPassport = new TokenPassport();
		tokenPassport.setAccount(_properties.getProperty("login.acct"));
		tokenPassport.setConsumerKey(_properties.getProperty("login.tbaConsumerKey"));
		tokenPassport.setToken(_properties.getProperty("login.tbaTokenId"));
		tokenPassport.setNonce(nonce);
		tokenPassport.setTimestamp(timestamp);
		TokenPassportSignature signatureElement = new TokenPassportSignature();
		signatureElement.setAlgorithm(SignatureAlgorithm._HMAC_SHA1);
		signatureElement.set_value(signature);
		tokenPassport.setSignature(signatureElement);

		return new SOAPHeaderElement("urn:messages_" + CURRENT_VERSION + ".platform.webservices.netsuite.com",
				"tokenPassport", tokenPassport);
	}

	/**
	 * Generates random String of alphanumeric characters
	 * 
	 * @param count
	 *            - length of the String
	 * @return generated string
	 */
	private static String createRandomAlphaNumericString(int count) {
		StringBuilder sb = new StringBuilder(count);
		for (int i = 0; i < count; i++) {
			sb.append(RANDOM_CHARACTERS.charAt(RANDOM.nextInt(RANDOM_CHARACTERS.length())));
		}
		return sb.toString();
	}

	/**
	 * Encrypts value with given encryption algorithm.
	 * 
	 * @param value
	 *            - value that needs to be encrypted
	 * @param key
	 *            - encryption key
	 * @param method
	 *            - algorithm that should be used for encryption
	 * @return the encryption result
	 */
	private static String hmacSha(String value, String key, String method) {
		if (!method.equals("HmacSHA256") && !method.equals("HmacSHA1"))
			method = "HmacSHA1";
		try {
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, method);

			Mac mac = Mac.getInstance(method);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(value.getBytes());

			String result = new String(DatatypeConverter.printBase64Binary(rawHmac));
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void getPort() {

		try {
			// Setting pageSize to 20 records per page
			_pageSize = 20;
			// Instantiate console logger
			_logger = new Logger(Logger.LEVEL_INFO);

			// In order to use SSL forwarding for SOAP messages. Refer to FAQ
			// for
			// details
			System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");

			// Reference to properties file that contains configuration data as
			// well as sample data. This file is named nsclienterp.properties
			// and
			// is located in the root directory of this installation.
			
			
			_properties = new Properties();
			//_properties.load(new FileInputStream("nsvwaclient.properties"));
			InputStream input = getClass().getClassLoader().getResourceAsStream("nsvwaclient.properties");
			if (input == null) {
				System.out.println("Sorry, unable to find " );
				return;
			}

			_properties.load(input);

			/*Enumeration<?> e = _properties.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = _properties.getProperty(key);
				System.out.println("Key : " + key + ", Value : " + value);
			}*/
			// Decide between standard login and TBA
			useTba = "true".equals(_properties.getProperty("login.useTba"));

			String account = _properties.getProperty("login.acct");
			// Locate the NetSuite web service.

			NetSuiteServiceLocator nsl = new NetSuiteServiceLocator();
			// Enable client cookie management. This is required.
			nsl.setMaintainSession(true);

			// Get the service port (to the correct datacenter)
			URL defaultWsDomainURL = new URL(_properties.getProperty("ws.url"));
			_port = nsl.getNetSuitePort(defaultWsDomainURL);
			DataCenterUrls urls = _port.getDataCenterUrls(account).getDataCenterUrls();
			System.out.println(urls);
			String wsDomain = urls.getWebservicesDomain();
			System.out.println(wsDomain);
			_port = nsl.getNetSuitePort(new URL(wsDomain.concat(defaultWsDomainURL.getPath())));
			System.out.println(_port);
			// Setting client timeout to 1 hours for long running operations
			((NetSuiteBindingStub) _port).setTimeout(1000 * 60 * 60 * 1);
		} catch (ServiceException ex) {
			System.out.println("\n\n[Error]: Error in locating the NetSuite web services. " + ex.getMessage());
		} catch (FileNotFoundException ex) {
			System.out.println("\n\n[Error]: Cannot find nsclienterp.properties file. Please ensure that "
					+ "this file is in the root directory of this application. ");
		} catch (IOException ex) {
			System.out.println("\n\n[Error]: An IO error has occured. " + ex.getMessage());
		} catch (Exception ex) {
			System.out.println("\n\n[Error]: An unexpected error occured: " + ex.getMessage());
			return;
		}
	}

	@Override
	public void connectVWA() throws FileNotFoundException, IOException {
		//_properties = new Properties();
		//_properties.load(new FileInputStream("nsvwaclient.properties"));

		String url = _properties.getProperty("vwa.url")+"swagger.json";

		Client restClient = Client.create();

		WebResource webResource =  restClient.resource(url);

		 ClientResponse resp = (webResource).accept("application/json")
					.header("dsat", _properties.getProperty("vwa.dsat"))
                  .get(ClientResponse.class);

		if (resp.getStatus() != 200) {
			System.err.println("Unable to connect to the server");
		}else{
			System.out.println("VWA Connection successful");
		}
		//String output = resp.getEntity(String.class);
		//System.out.println("response: " + output);
	}

}

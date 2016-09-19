public class key {
	private String ConsumerKey;
	private String ConsumerSecret;
	private String AccessToken;
	private String AccessTokenSecret;
	public String getConsumerKey() {
		return ConsumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		ConsumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return ConsumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		ConsumerSecret = consumerSecret;
	}
	public String getAccessToken() {
		return AccessToken;
	}
	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}
	public String getAccessTokenSecret() {
		return AccessTokenSecret;
	}
	public void setAccessTokenSecret(String accessTokenSecret) {
		AccessTokenSecret = accessTokenSecret;
	}
	public key(String consumerKey, String consumerSecret, String accessToken,
			String accessTokenSecret) {
		super();
		ConsumerKey = consumerKey;
		ConsumerSecret = consumerSecret;
		AccessToken = accessToken;
		AccessTokenSecret = accessTokenSecret;
	}
}

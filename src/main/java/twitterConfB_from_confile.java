import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class twitterConfB_from_confile {
	//ConfigurationBuilder cb;
	public List<key> keys;
	public int cur;

	public twitterConfB_from_confile(String config_file) {
		super();
		this.cur = 0;

        keys = new ArrayList<key>();

		Configuration config = null;
		try {
			File f = new File(config_file);
			System.out.println("Config file is " + f.getPath());
			config = new PropertiesConfiguration(f.getPath());
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}

		Iterator it = config.getKeys();
        while(it.hasNext()){
            String keyname = (String) it.next();
            if(keyname.contains("API")){
                Object[] items = config.getList(keyname).toArray();
                if(items.length != 4)
                    throw new IllegalStateException("API size Wrong! Each line in the config file should contain 4 items.");
                keys.add(new key(items[0].toString(),
                        items[1].toString(),
                        items[2].toString(),
                        items[3].toString()));
            }

        }
        System.out.println("API keys loaded! " + keys.size() + " keys in total!");
	}
	
	public Twitter getTwitter(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false)
		.setOAuthConsumerKey(keys.get(cur).getConsumerKey())
		.setOAuthConsumerSecret(keys.get(cur).getConsumerSecret())
		.setOAuthAccessToken(keys.get(cur).getAccessToken())
		.setOAuthAccessTokenSecret(keys.get(cur).getAccessTokenSecret())
		.setJSONStoreEnabled(true);
        cur++;
		if(cur == keys.size()) cur = 0;
		return new TwitterFactory(cb.build()).getInstance();
	}
	
	public TwitterStream getTwitterStreamming(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false)
		.setJSONStoreEnabled(true)
		.setOAuthConsumerKey(keys.get(cur).getConsumerKey())
		.setOAuthConsumerSecret(keys.get(cur).getConsumerSecret())
		.setOAuthAccessToken(keys.get(cur).getAccessToken())
		.setOAuthAccessTokenSecret(keys.get(cur).getAccessTokenSecret());
		cur++;
		if(cur == keys.size()) cur = 0;
		return new TwitterStreamFactory(cb.build()).getInstance();
	}
	
	
	public TwitterStream getTwitterStreamming(int key_index){
		this.cur = key_index;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false)
		.setJSONStoreEnabled(true)
		.setOAuthConsumerKey(keys.get(cur).getConsumerKey())
		.setOAuthConsumerSecret(keys.get(cur).getConsumerSecret())
		.setOAuthAccessToken(keys.get(cur).getAccessToken())
		.setOAuthAccessTokenSecret(keys.get(cur).getAccessTokenSecret());
		cur++;
		if(cur == keys.size()) cur = 0;
		return new TwitterStreamFactory(cb.build()).getInstance();
	}
	
	public Twitter getTwitter(int key_index){
		this.cur = key_index;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false)
		.setJSONStoreEnabled(true)
		.setOAuthConsumerKey(keys.get(cur).getConsumerKey())
		.setOAuthConsumerSecret(keys.get(cur).getConsumerSecret())
		.setOAuthAccessToken(keys.get(cur).getAccessToken())
		.setOAuthAccessTokenSecret(keys.get(cur).getAccessTokenSecret());
		cur++;
		if(cur == keys.size()) cur = 0;
		return new TwitterFactory(cb.build()).getInstance();
	}
}
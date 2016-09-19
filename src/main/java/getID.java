import org.gla.terrier.csvLib;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPOutputStream;


public class getID implements Fectch {
	int tweetAPIWaitTime = 100;
	twitterConfB_from_confile tc;

	public getID(twitterConfB_from_confile tc) {
		this.tc = tc;
	}

	public void run(String screenameFile, String dir){
		csvLib csvlib = new csvLib();
		Twitter tw = tc.getTwitter();

		List<String> names = csvlib.readCSV_ByColumn(screenameFile, 0);
		
		BufferedWriter statusWriter = null;
		
		try {
			statusWriter = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(dir + "/followee.json.gz",true)), "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for(int i = 0; i < names.size(); i++){
			try {
				System.out.println(names.get(i).trim());
				User u = tw.showUser(Long.parseLong(names.get(i).trim()));
				String json = DataObjectFactory.getRawJSON(u);
				
				statusWriter.write(json);
				statusWriter.newLine();
				statusWriter.flush();
				
				csvlib.writeFileAppend(dir + "/followee.csv", u.getId() + "," + u.getName().replaceAll(",", " ") +"," + u.getScreenName() + "," + 
				u.getFavouritesCount() + "," + u.getFollowersCount() + "," + u.getFriendsCount() + ","  + u.getLocation() + "," +
						u.getCreatedAt() + "," + u.getTimeZone() + "," + u.getDescription().replaceAll("\\n|\\t|\\r|\\r\\n", " ").replaceAll("[^\\x20-\\x7e]", " ").replaceAll(",", " ").replaceAll(".", " "));
				
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				if(e.getErrorCode() == 88){
					System.out.println("Chaning Key... Now Key:" + tc.cur);
					if(tc.cur == tc.keys.size() - 1){
						System.err.println("Twitter API is restarted in " + this.tweetAPIWaitTime + " seconds.");
						try {
							Thread.sleep(1000*this.tweetAPIWaitTime);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					tw = tc.getTwitter();
					i--;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			statusWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}

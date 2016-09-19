import org.gla.terrier.csvLib;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.json.DataObjectFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;


@SuppressWarnings("deprecation")
public class getRecentTweets implements  Fectch{
	int tweetAPIWaitTime = 100;
	twitterConfB_from_confile tc;

	public getRecentTweets(twitterConfB_from_confile tc) {
		this.tc = tc;
	}

	public void run(String IDfile, String des, int numOfTweets){
		csvLib csvlib = new csvLib(",");
		run(csvlib.readCSV_ByColumn(IDfile, 0), des, numOfTweets, tc);
	}

	public void run(List<String> IDS, String des, int numOfTweets, twitterConfB_from_confile tc){
		System.out.println("User number: " + IDS.size());
		System.out.println("Saving to folder:" + des);

		csvLib csvlib = new csvLib();
		//twitterConfB tc = new twitterConfB();
		Twitter tw = tc.getTwitter();
		//int maximum_page_num = 100;
		int tweetsPerPage = 200;
		int maximum_page_num = numOfTweets/tweetsPerPage;

		Boolean isID = true;

		try{
			Long temp = Long.parseLong(IDS.get(0));
		}catch (Exception e){
			isID = false;
		}

		for(int u = 0; u < IDS.size(); u++){
			long ID = 0;

			if(isID) {
				try {
					ID = Long.parseLong(IDS.get(u));
				} catch (Exception e) {
					continue;
				}
			}

			int start_page = 1;
			List<String> statues = new ArrayList<String>();
			boolean limitted = false;
			System.out.println(IDS.get(u));

			int count = 0;

			for(int i = start_page; i <= maximum_page_num; i++){
				Paging paging = new Paging(i,tweetsPerPage);
				List<Status> temp = new ArrayList<Status>();
				try {
					if(isID)
						temp = tw.getUserTimeline(ID, paging);
					else
						temp = tw.getUserTimeline(IDS.get(u), paging);
				} catch (TwitterException e) {
					if(e.getErrorCode() == 88){
						System.out.println("Chaning Key... Now Key:" + tc.cur);
						if(tc.cur == tc.keys.size() - 1){
							System.err.println("Twitter API is restarted in " + this.tweetAPIWaitTime + " seconds.");
							try {
								Thread.sleep(1000*this.tweetAPIWaitTime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						tw = tc.getTwitter();
						limitted = true; 
						break;
					}
				} catch (Exception e){
					continue;
				}

				if(temp.size() == 0)	break;

				System.out.println("Page number: " + i + ", Tweets: "  + temp.size());
				for(Status s: temp)
					statues.add(DataObjectFactory.getRawJSON(s));

				count += temp.size();
				if(count >= 500)
					break;
			}

			if(limitted){
				u--;
				continue;
			}

			System.out.println("ID: " + IDS.get(u) + "," + "Tweets: " + statues.size());
			BufferedWriter statusWriter;

			try {
				statusWriter = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(des,true)), "UTF-8"));
				for(String status: statues){
					statusWriter.write(status+'\n');
					statusWriter.flush();
				}
				statusWriter.flush();
				statusWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}



		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getRecentTweets grt = new getRecentTweets();
		//grt.run(args[0], 0, ",", args[1]);
	}

}

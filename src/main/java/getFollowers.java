import org.gla.terrier.csvLib;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.File;
import java.util.List;

public class getFollowers implements Fectch{
	int tweetAPIWaitTime = 5*60;
	twitterConfB_from_confile tc;

	public getFollowers(twitterConfB_from_confile tc){
		this.tc = tc;
	}

	public void run(String screenameFile, Boolean isFriends){
		csvLib csvlib = new csvLib();
		Twitter tw = tc.getTwitter();
		File file = new File(screenameFile);
		List<String> names = csvlib.readFileLineByLine(file.getPath());

		Boolean isID = true;
		try{
			long temp = Long.parseLong(names.get(0));
		} catch (Exception e){
			isID = false;
		}

		long tmp_cursor = -1;

		for(int i = 0; i < names.size(); i++){
			try {
				System.out.println(names.get(i));
				long cursor = -1;
				if(tmp_cursor != -1)
					cursor = tmp_cursor;
				System.out.println("Cursor:" + cursor);
				String tmp = "";
				IDs ids;

				do
				{
				    if(isID){
				        if(isFriends)
				            ids = tw.getFriendsIDs(Long.parseLong(names.get(i).trim()), cursor);
                        else
                            ids = tw.getFollowersIDs(Long.parseLong(names.get(i).trim()),cursor);
                    }else{
                        if(isFriends)
                            ids = tw.getFriendsIDs(names.get(i).trim(), cursor);
                        else
                            ids = tw.getFollowersIDs(names.get(i).trim(), cursor);
                    }


					tmp_cursor = cursor;
					for (long id : ids.getIDs()){
						tmp+=id+",";
					}
				}while((cursor = ids.getNextCursor()) != 0);

				csvlib.writeFileAppend(file.getPath() + ".net.csv", names.get(i) + "\t" + tmp);
				
				tmp_cursor = -1;

			} catch (TwitterException e) {
				System.out.println(e.getErrorCode());
				System.out.println(e.getErrorMessage());

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
					i--;
				}
			}
		}
		System.out.println("Results saved in " + file.getPath() + ".net.csv");
	}

	public static void main(String[] args) {
		//getFollowers gf = new getFollowers();
		//gf.run(args[0], args[1]);
	}

}
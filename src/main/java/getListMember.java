import org.gla.terrier.csvLib;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.File;

public class getListMember implements Fectch{
	csvLib csvlib = new csvLib();
	twitterConfB_from_confile tc;
	
	public getListMember(twitterConfB_from_confile tc) {
		this.tc = tc;
	}

	public void run(String name, String dir){
		//format in csv file: screen_name, listname
		String ownerName = name.split(",")[0];
		String listName = name.split(",")[1];


		File des = new File(dir + "/" + listName);
		if(!des.exists()) des.mkdirs();
		File des_memb = new File(des.getPath()+"/member_Scr/");
		if(!des_memb.exists()) des_memb.mkdir();

		Twitter tw = tc.getTwitter();
		PagableResponseList<User> members = null;
		
		long cursor = -1;
		do{
			try {
				members = tw.getUserListMembers(ownerName, listName, cursor);
				for(User u: members){
					csvlib.writeFileAppend(des.getPath() + "/name.csv", u.getId() +","+u.getScreenName());
					csvlib.writeFileAppend(des_memb.getPath() + "/" + u.getId() + ".txt", u.getDescription());
				}
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while((cursor = members.getNextCursor())!=0);
	}
	
	public void run(String ownerName, String listName, String dir, int APIKey_Index, twitterConfB_from_confile tc){
		File des = new File(dir + "/" + listName);
		if(! des.exists()) des.mkdirs();
		File des_memb = new File(des.getPath()+"/member_Scr/");
		if(!des_memb.exists()) des_memb.mkdirs();

		Twitter tw = tc.getTwitter(APIKey_Index);
		PagableResponseList<User> members = null;
		long cursor = -1;
		do{
			try {
				members = tw.getUserListMembers(ownerName, listName, cursor);
				for(User u: members){
					csvlib.writeFileAppend(des.getPath() + "/" + listName +".csv", u.getId() +"," + u.getName() + "," + u.getScreenName());
					csvlib.writeFileAppend(des_memb.getPath() + "/" + u.getId() + ".txt", u.getDescription());
				}
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while((cursor = members.getNextCursor())!=0);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		getListMember gm = new getListMember();
//		gm.run("ezraklein","political-scientists","/media/fanganjie/scratch1/GS/member_list",10);
	}
}

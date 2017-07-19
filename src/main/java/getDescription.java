import org.gla.terrier.csvLib;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class getDescription implements Fectch{
	String cache_dir = "";
	Set<Long> cache_ID;
	csvLib csvlib = new csvLib();
	int twitterLimit;
	twitterConfB_from_confile tc;

	public getDescription(twitterConfB_from_confile tc){
		this.tc =tc;
		cache_ID = new HashSet<Long>();
	}
	
	@SuppressWarnings("unchecked")
	public getDescription(String cache_dir) {
		super();
		this.cache_dir = cache_dir;
		cache_ID = new HashSet<Long>();
		File f = new File(cache_dir + "/id.data");
		if(f.exists())
			try {
				cache_ID = (Set<Long>)serialize.deSerHT(f.getPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		f = new File(cache_dir + "/Description");
		if(!f.exists()) f.mkdir();
	}

	public void run(String filename){
		this.run(new csvLib().readFileLineByLine(filename));
	}
	
	public void run(List<String> users){
		printColor.blackln("Total Users:"+ users.size());
		printColor.blackln("Users cached:" + cache_ID.size());
		printColor.blackln("Users pcik up:" + getDesN());
		
		Random rand = new Random();
		Twitter tw = tc.getTwitter();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int count = 0;
		while(cache_ID.size() != users.size()){
			int r;
			do{
				r = rand.nextInt(users.size());
			}while(cache_ID.contains(r));
			Long user_id = Long.parseLong(users.get(r));
			cache_ID.add(user_id);
			User u;
			try {
				u = tw.showUser(user_id);
				if (count++ == twitterLimit) {tw = tc.getTwitter();count=0;}
				printColor.redln("\n"+"Count:" + count + ", File num:" + getDesN() + ", API Key Num:"+ tc.cur);
				printColor.blackln(u.getName());
				printColor.blackln(u.getDescription());
				printColor.blueln("Y: add; N: skip; E: exit.");
				String in = br.readLine();
				//System.out.println(in);
				if(in.equals("y")){
					File f = new File(cache_dir + "/Description/" + user_id +".txt");
					if(!f.exists()){
					csvlib.writeFileAppend(cache_dir + "/name.csv", user_id+","+u.getScreenName()+","+u.getName());
					csvlib.writeFileAppend(cache_dir + "/Description/" + user_id +".txt",u.getDescription());
					}
					printColor.redln("Added!");
				}else if (in.equals("e")){
					File f = new File(cache_dir + "/id.data");
					if(f.exists()) f.delete();
					serialize.serHT(cache_ID, cache_dir, "id.data");
					printColor.redln("Exited!");
					System.exit(-1);
				}else{
					printColor.redln("Skipped!");
					continue;
				}
		
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void run(List<String> users, String keyn){
		printColor.blackln("Total Users:"+ users.size());
		printColor.blackln("Users cached:" + cache_ID.size());
		printColor.blackln("Users pcik up:" + getDesN());

		Twitter tw = tc.getTwitter(Integer.parseInt(keyn));
		System.out.println("API key number:" + tc.cur);
		
		Set<Long> already_users = new HashSet<Long>();
		File f = new File(cache_dir + "/Description");
		for(File u: f.listFiles()){
			already_users.add(Long.parseLong(u.getName().split("\\.")[0]));
		}
		
		System.out.println("Already:" + already_users.size());
		
		int count = 0;
		for(int i = 0; i < users.size(); i++){
			Long user_id = Long.parseLong(users.get(i));
			if(already_users.contains(user_id)){
				System.out.println("Existed:" + user_id);
				continue;
			}
			
			boolean flag = true;
			if(flag){
				try {
					Map<String ,RateLimitStatus> rateLimitStatus = tw.getRateLimitStatus();
					RateLimitStatus status = rateLimitStatus.get("/users/show/:id");
					twitterLimit = status.getRemaining();
					flag=false;
					
				} catch (TwitterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					try {
						Thread.sleep(90000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			System.out.println((cache_ID.size()+f.listFiles().length) + "/" + users.size());
			cache_ID.add(user_id);
			User u;
			try {
				u = tw.showUser(user_id);
				if (count++ == twitterLimit) {tw = tc.getTwitter();count=0; flag = true; System.out.println("API key:" + tc.cur);}
				csvlib.writeFileAppend(cache_dir + "/name.csv", user_id+","+u.getScreenName()+","+u.getName());
				String desc = u.getDescription();
				if(desc == null || desc == "")
					csvlib.writeFileAppend(cache_dir + "/empty.csv", user_id+"");
				else
					csvlib.writeFileAppend(cache_dir + "/Description/" + user_id +".txt",u.getDescription());
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public int getDesN(){
		File f = new File(cache_dir + "/Description");
		return f.listFiles().length;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getDescription gd = new getDescription(args[1]);
		csvLib csvlib = new csvLib();
		gd.run(csvlib.readCSV_ByColumn(args[0], 0), args[2]);
	}

}

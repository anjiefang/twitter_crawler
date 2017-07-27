import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Created by anjiefang on 03/08/2016.
 */
public class main_crawler {

    @Option(name="-c", usage = "Specify the path of API key configuration file.")
    private String config_file="API.config";

    @Option(name="-f", required = true, usage = "Specify the path of Twitter handles list.")
    private String handle_list;

    @Option(name="-o", required = true, usage = "Specify the path of the output directory.")
    private String output="";

    @Option(name = "-fo", usage = "Crawl the followers of the specified Twitter user handdles.")
    private Boolean isFollwoer = false;

    @Option(name = "-fr", usage = "Crawl the friends of the specified Twitter user handdles.")
    private Boolean isFriend = false;

    @Option(name = "-r", usage = "Crawl the recent tweets of the specified Twitter user handdles. Default number of tweets is 1000. Use -n to set another number (3000 Maximium)")
    private Boolean isTweets = false;

    @Option(name = "-id", usage = "Crawl the ids of the given list.")
    private Boolean isID = false;

    // format of csv: screen_name, list_name
    @Option(name = "-isMember", usage = "Crawl the all the member of a exisitng list.")
    private Boolean isMember = false;

    @Option(name = "-isDesc", usage = "Crawl the Description of the specified Twitter user handdles.")
    private Boolean isDesc = false;

    @Option(name = "-n", usage = "If crawl recent tweets, set the number tweets you want for per user.")
    private int numberOfTweets = 1000;


    public static void main(String[] args){
        System.out.println(System.getProperty("user.dir"));
        new main_crawler().doMain(args);
    }

    public void doMain(String[] args){

        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.out);
            System.exit(-1);
        }

        //reading the API key from the configuration file
        twitterConfB_from_confile tconfig = new twitterConfB_from_confile(this.config_file);

        if(this.isFollwoer){
            getFollowers gf = new getFollowers(tconfig);
            gf.run(this.handle_list, false);
        }

        if(this.isFriend){
            getFollowers gf = new getFollowers(tconfig);
            gf.run(this.handle_list, true);
        }

        if(this.isTweets){
            getRecentTweets gt = new getRecentTweets(tconfig);
            gt.run(this.handle_list, this.output, this.numberOfTweets);
        }

        if(this.isID){
            getID gid = new getID(tconfig);
            gid.run(this.handle_list, this.output);
        }

        if(this.isMember){
            getListMember glm = new getListMember(tconfig);
            glm.run(this.handle_list, this.output);
        }

        if(this.isDesc){
            getDescription gd = new getDescription(tconfig);
            gd.run(this.handle_list);
        }
    }
}

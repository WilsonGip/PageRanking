package recommenders;

/**
 * Wilson Gip
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PageNode {
    public PageNode(String link){
        this.inLinks = new HashSet<>();
        this.outLinks = new HashSet<>();
        this.pageRank = 1;
        this.previousRank = 0;
        this.link = link;
    }

    public double getPageRank() {
        return pageRank;
    }

    public int getNumberOfOutlinks(){
        return outLinks.size();
    }

    public void setPageRank(double pageRank) {
        this.previousRank = this.pageRank;
        this.pageRank = pageRank;
    }

    public String getLink() {
        return link;
    }

    public void addOutlinks(String link){
        outLinks.add(link);
    }

    // Remove all outlinks that has not been visited by the WebCrawler
    public void pruneUnvisitedOutlinks(HashMap<String, Boolean> outlinkVisited){
        ArrayList<String> unvisitedLinks = new ArrayList<>();
        for (String outlink : outLinks){
            if(outlinkVisited.containsKey(outlink) && !outlinkVisited.get(outlink)){
                unvisitedLinks.add(outlink);
            }
        }
        for (String unvisitedLink : unvisitedLinks){
            outLinks.remove(unvisitedLink);
        }
    }

    public void printOutlinks(){
        for(String outlink : outLinks){
            System.out.println("Link: " + this.link + ", Outlink: " + outlink);
        }
    }

    public void addInlink(PageNode inlink){
        this.inLinks.add(inlink);
    }

    public void printInlinks(){
        for (PageNode page : inLinks){
            System.out.println("Link: " + this.link + ", Inlink: " + page.getLink() + ", Page rank: " + page.getPageRank());
        }
    }

    public void printPageRank(){
        System.out.println("Link: " + this.link + ", Page Rank: " + this.pageRank);
    }

    public boolean pageRankUnchanged(){
        return this.previousRank == this.pageRank;
    }

    public boolean containsOutlink(String link){
        return outLinks.contains(link);
    }

    public void printInAndOutLinksSize(){
        System.out.println("Link: " + this.link + ", Inlink Size: " + this.inLinks.size() + ", Outlink Size: " + this.outLinks.size());
    }

    // Calculate the page ranking
    public double calculatePageRank(){
        double sum = 0.0;
        for (PageNode page : inLinks){
            sum += page.getPageRank()/page.getNumberOfOutlinks();
        }
        return (inLinks.isEmpty() ? getPageRank() : sum);
    }

    private double pageRank;
    private double previousRank;
    private String link;
    private Set<String> outLinks;
    private HashSet<PageNode> inLinks;
}

package pageranking;
/**
 * Wilson Gip
 */
import java.util.ArrayList;
import java.util.Collections;

public class PageRanker {
    public PageRanker(){
        this.crawler = new WebCrawler(100);
    }

    public PageRanker(int maxPages){
        this.crawler = new WebCrawler(maxPages);
    }

    // Crawls the seed and returns an ArrayList with PageNodes that contains their respective outlinks and inlinks
    // Then initialize each PageNode with page rank with 1/(number of pages)
    // Then update the page rankings for each PageNode and sort it by descending
    public void getPageRankings(String seed, String filter){
        this.pageList = crawler.startCrawl(seed, filter);
        System.out.println("---------------------------------------------------------------------");
        initializePageRank();
        updatePageRanking();
        pageList.sort(new PageComparator());
        printPageRankings();
    }

    // Initialize each PageNode's page rank with 1/(number of pages)
    private void initializePageRank(){
        for (PageNode page : pageList){
            page.setPageRank(1.0/pageList.size());
        }
    }

    private void printPageRankings(){
        for (PageNode page : pageList){
            page.printPageRank();
        }
    }

    // Update the page ranking until it converges
    // (when there are no changes in page ranking after updating each iteration)
    private void updatePageRanking(){
        double[] newRank = new double[pageList.size()];
        while(!isConverged()){
//        for(int idx = 0; idx < 2; ++idx){
            for (int i = 0; i < pageList.size(); ++i){
                newRank[i] = pageList.get(i).calculatePageRank();
                if(newRank[i] == 0.0){
                    return;
                }
            }
            for(int i = 0; i < pageList.size(); ++i){
                pageList.get(i).setPageRank(newRank[i]);
            }
        }
    }

    // Check to see if each PageNode's page rank has changed
    private boolean isConverged(){
        for(PageNode page : pageList){
            if(!page.pageRankUnchanged()){
                return false;
            }
        }
        return true;
    }

    private WebCrawler crawler;
    private ArrayList<PageNode> pageList;
}

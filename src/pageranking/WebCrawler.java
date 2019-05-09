package pageranking;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class WebCrawler {
    public WebCrawler(int maxPages){
        this.maxPages = maxPages;
        pageMap = new HashMap<>();
        outlinksVisited = new HashMap<>();
        outlinkQueue = new LinkedList<>();
    }

    // Crawl the first seed and return an ArrayList with PageNodes
    // that contains their respective outlinks and inlinks
    public ArrayList<PageNode> startCrawl(String seed, String filter){
        int count = 0;
        String currentLink = seed;
        outlinkQueue.add(currentLink);
        while(count <= this.maxPages && !outlinkQueue.isEmpty()){
            Document currentDoc = this.crawl(currentLink);
            if(currentDoc != null){
                PageNode currentPage = new PageNode(currentLink);
                getLinksAndAddToQueue(currentDoc, currentPage, filter);
                pageMap.put(currentLink, currentPage);
                ++count;
            }
            currentLink = outlinkQueue.poll();
        } 

        pruneAllUnvisitedLinks();
        addAllInlinksInPageMap();
//        printVisitedMap();
//        printPageMapOutlinks();
//        printPageMapInlinks();
//        printInOutLinksSize();
        return getArrayListFromPageMap();
    }

    // Crawl the link and return a Document
    // If fail to crawl then return null
    private Document crawl(String link) {
        Document currentDoc;
        try {
            print("Fetching %s...", link);
            currentDoc = Jsoup.connect(link).get();
            outlinksVisited.put(link, true);
        }catch (IOException e){
            print("Failed to connect to %s!\n%s", link, e.toString());
            currentDoc = null;
        }
        return currentDoc;
    }

    // Gather all the outlinks from the Document
    // Then add it to the visited map, queue, and the PageNode outlink set
    private void getLinksAndAddToQueue(Document currentDoc, PageNode page, String filter){
        Elements documentOutlinks = currentDoc.select("a[href]");
        for (Element link : documentOutlinks){
            String outlink = link.attr("abs:href");
            if(!outlinksVisited.containsKey(outlink) && (filter.isEmpty() || outlink.contains(filter))){
                outlinksVisited.put(outlink, false);
                outlinkQueue.add(outlink);
            }
            if(!page.getLink().equals(outlink)) {
                page.addOutlinks(outlink);
            }
        }
    }

    // Get the inlinks of each PageNode and add it into the PageNode's inlink set
    private void getInlinksAndAddToPage(String link){
        for(HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            if(!entry.getKey().equals(link) && entry.getValue().containsOutlink(link)){
                pageMap.get(link).addInlink(entry.getValue());
            }
        }
    }

    // Call each PageNode's pruning function to prune dangling/unvisited links
    private void pruneAllUnvisitedLinks(){
        for(HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            entry.getValue().pruneUnvisitedOutlinks(outlinksVisited);
        }
    }

    // Update each PageNode's inlink set
    private void addAllInlinksInPageMap(){
        for (HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            getInlinksAndAddToPage(entry.getKey());
        }
    }

    // From the PageNode HashMap
    // return an ArrayList
    private ArrayList<PageNode> getArrayListFromPageMap(){
        ArrayList<PageNode> list = new ArrayList<>();
        for (HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    public void printVisitedMap(){
        for(HashMap.Entry<String, Boolean> entry : outlinksVisited.entrySet()){
            print("Link: %s, Visited: %s", entry.getKey(), entry.getValue());
        }
    }

    public void printPageMapOutlinks(){
        for(HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            entry.getValue().printOutlinks();
        }
    }

    public void printPageMapInlinks(){
        for(HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            entry.getValue().printInlinks();
        }
    }

    public void printInOutLinksSize(){
        for(HashMap.Entry<String, PageNode> entry : pageMap.entrySet()){
            entry.getValue().printInAndOutLinksSize();
        }
    }

    private int maxPages;
    private HashMap<String, PageNode> pageMap;
    private HashMap<String, Boolean> outlinksVisited;
    private Queue<String> outlinkQueue;
}

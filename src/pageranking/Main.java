package pageranking;

/**
 * Wilson Gip
 */
public class Main {

    public static void main(String[] args) {
        PageRanker ranker = new PageRanker(201);
        ranker.getPageRankings("http://www.espn.com/nba/statistics", "espn.com/nba");
    }
}

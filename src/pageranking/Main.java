package pageranking;

public class Main {

    public static void main(String[] args) {
        PageRanker ranker = new PageRanker(500);
        ranker.getPageRankings("http://www.espn.com/nba/", "espn.com/nba");
        ranker.saveRankingsToCSV();
    }
}

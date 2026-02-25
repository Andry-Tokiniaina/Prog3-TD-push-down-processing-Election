public class Main {
    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();

        System.out.println(dataRetriever.countAllVotes());
        System.out.println(dataRetriever.countVotesByType());
        System.out.println(dataRetriever.countValidVotesByCandidate());
        System.out.println(dataRetriever.computeVoteSummary());
        System.out.println(dataRetriever.computeTurnoutRate());
        System.out.println(dataRetriever.findWinner());
    }
}
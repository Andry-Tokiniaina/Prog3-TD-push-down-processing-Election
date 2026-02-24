public class Main {
    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();

        System.out.println(dataRetriever.countAllVotes());
        System.out.println(dataRetriever.countVotesByType());
        System.out.println(dataRetriever.countValidVotesByCandidate());
    }
}
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    long countAllVotes() {
        DbConnection dbConnection = new DbConnection();
        String query = "select count(id) as cnt from vote";
        try (Connection conn = dbConnection.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            long total_votes = 0;
            if (resultSet.next()) {
                total_votes = resultSet.getLong("cnt");
            }
            return total_votes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<VoteTypeCount> countVotesByType() {
        DbConnection dbConnection = new DbConnection();
        String query = """
                select v.vote_type,
                count(
                    id
                ) as count
                from vote v
                group by v.vote_type;
                """;
        try (Connection conn = dbConnection.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<VoteTypeCount> voteTypeCounts = new ArrayList<>();
            while (resultSet.next()) {
                VoteTypeCount voteTypeCount = new VoteTypeCount();
                voteTypeCount.setCount(resultSet.getInt("count"));
                voteTypeCount.setVoteType(VoteType.valueOf(resultSet.getString("vote_type")));
                voteTypeCounts.add(voteTypeCount);
            }
            return voteTypeCounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<CandidateVoteCount> countValidVotesByCandidate () {
        DbConnection dbConnection = new DbConnection();
        String query = """
                select c.name as name,
                count (
                    v.id
                ) as cnt
                from vote v
                right join candidate c
                on c.id = v.candidate_id
                where v.vote_type = 'VALID'
                group by c.name;
        """;
        try (Connection conn = dbConnection.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CandidateVoteCount> candidateVoteCounts = new ArrayList<>();
            while (resultSet.next()) {
                CandidateVoteCount candidateVoteCount = new CandidateVoteCount();
                candidateVoteCount.setCandidateCount(resultSet.getInt("cnt"));
                candidateVoteCount.setCandidateName(resultSet.getString("name"));
                candidateVoteCounts.add(candidateVoteCount);
            }
            return candidateVoteCounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

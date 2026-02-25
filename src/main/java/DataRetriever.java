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
                and v.vote_type = 'VALID'
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

    VoteSummary computeVoteSummary() {
        DbConnection dbConnection = new DbConnection();
        String query = """
                select count (
                    case when v.vote_type = 'VALID'
                       then 1
                       end
                ) as valid_count,
                    count(
                        case when v.vote_type = 'NULL'
                        then 1
                        end
                    ) as null_count,
                    count(
                        case when v.vote_type = 'BLANK'
                        then 1
                        end
                    ) as blank_count
                from vote v;
        """;
        try (Connection conn = dbConnection.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            VoteSummary voteSummary = new VoteSummary();
            while (resultSet.next()) {
                voteSummary.setBlankCount(resultSet.getInt("blank_count"));
                voteSummary.setValidCount(resultSet.getInt("valid_count"));
                voteSummary.setNullCount(resultSet.getInt("null_count"));
            }
            return voteSummary;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    double computeTurnoutRate(){
        DbConnection dbConnection = new DbConnection();
        String query = """
                select (count(vote.id)/count(voter.id))*100 as rate from vote
                cross join voter
        """;
        try (Connection conn = dbConnection.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            double turnoutRate = 0;
            while (resultSet.next()) {
                turnoutRate = resultSet.getDouble("rate");
            }
            return turnoutRate;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    ElectoinResult findWinner(){
        DbConnection dbConnection = new DbConnection();
        String query = """
                with valid_by_candidate as (
                select c.name,
                    count (
                        v.id
                    ) as cnt
                    from vote v
                    join candidate c
                    on c.id = v.candidate_id
                    and v.vote_type = 'VALID'
                    group by c.name
                ) select name, cnt as count
                from valid_by_candidate
                where cnt = (select max(cnt) from valid_by_candidate)
        """;
        try (Connection connection = dbConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ElectoinResult electoinResult = new ElectoinResult();
            while (resultSet.next()) {
                electoinResult.setValidVoteCount(resultSet.getInt("count"));
                electoinResult.setCandidateName(resultSet.getString("name"));
            }
            return electoinResult;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}

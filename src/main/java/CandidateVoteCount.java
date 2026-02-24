import java.util.Objects;

public class CandidateVoteCount {
    private String candidateName;
    private int candidateCount;

    public CandidateVoteCount() {
    }

    public CandidateVoteCount(String candidateName, int candidateCount) {
        this.candidateName = candidateName;
        this.candidateCount = candidateCount;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getCandidateCount() {
        return candidateCount;
    }

    public void setCandidateCount(int candidateCount) {
        this.candidateCount = candidateCount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CandidateVoteCount that)) return false;
        return getCandidateCount() == that.getCandidateCount() && Objects.equals(getCandidateName(), that.getCandidateName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCandidateName(), getCandidateCount());
    }

    @Override
    public String toString() {
        return "CandidateVoteCount{" +
                "candidateName='" + candidateName + '\'' +
                ", candidateCount=" + candidateCount +
                '}';
    }
}

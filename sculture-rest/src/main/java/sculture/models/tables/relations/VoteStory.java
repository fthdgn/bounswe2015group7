package sculture.models.tables.relations;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "VOTE_STORY")
@IdClass(VoteStoryPK.class)
public class VoteStory {
    @Id
    @NotNull
    private long story_id;

    @Id
    @NotNull
    private long user_id;

    private int vote;

    public long getStory_id() {
        return story_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}

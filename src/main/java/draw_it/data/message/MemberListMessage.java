package draw_it.data.message;

import draw_it.data.user.User;

import java.util.List;

public class MemberListMessage extends Message {

    private List<Member> members;

    public MemberListMessage(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}

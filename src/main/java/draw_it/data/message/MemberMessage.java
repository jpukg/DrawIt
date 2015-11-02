package draw_it.data.message;

import draw_it.data.user.User;

public class MemberMessage extends Message {
    public final static String MEMBER_ADDED = "added";
    public final static String MEMBER_REMOVED = "removed";
    public final static String MEMBER_GOT_TURN = "got_turn";

    private String action;
    private Member member;

    public MemberMessage(String action, User user) {
        this.action = action;
        this.member = new Member(user);
    }

    public String getAction() {
        return action;
    }

    public Member getMember() {
        return member;
    }
}

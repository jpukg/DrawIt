package draw_it.data.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomMessage.class, name = "RoomMessage"),
        @JsonSubTypes.Type(value = RoomListMessage.class, name = "RoomListMessage"),
        @JsonSubTypes.Type(value = MemberMessage.class, name = "MemberMessage"),
        @JsonSubTypes.Type(value = MemberListMessage.class, name = "MemberListMessage"),
        @JsonSubTypes.Type(value = ChatMessage.class, name = "ChatMessage"),
        @JsonSubTypes.Type(value = DrawMessage.class, name = "DrawMessage"),
        @JsonSubTypes.Type(value = TimeMessage.class, name = "TimeMessage"),
        @JsonSubTypes.Type(value = GameMessage.class, name = "GameMessage"),
        @JsonSubTypes.Type(value = WordMessage.class, name = "WordMessage"),
        @JsonSubTypes.Type(value = ScoreMessage.class, name = "ScoreMessage"),
        @JsonSubTypes.Type(value = FinalScoreMessage.class, name = "FinalScoreMessage")})
public abstract class Message {

}

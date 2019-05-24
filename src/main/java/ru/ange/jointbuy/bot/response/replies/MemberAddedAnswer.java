package ru.ange.jointbuy.bot.response.replies;

import ru.ange.jointbuy.utils.Constants;

public class MemberAddedAnswer extends AnswerHelloMsg {

    public MemberAddedAnswer(String callId) {
        super();
        setCallbackQueryId( callId );
        setText( Constants.JOIN_USER_BTT_ANSWER_SUCCESS );
    }

}

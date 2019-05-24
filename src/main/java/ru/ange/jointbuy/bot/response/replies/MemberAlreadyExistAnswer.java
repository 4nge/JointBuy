package ru.ange.jointbuy.bot.response.replies;

import ru.ange.jointbuy.utils.Constants;

public class MemberAlreadyExistAnswer extends AnswerHelloMsg {

    public MemberAlreadyExistAnswer(String callId) {
        super();
        setCallbackQueryId( callId );
        setText( Constants.JOIN_USER_BTT_ANSWER_EXISTS );
    }

}

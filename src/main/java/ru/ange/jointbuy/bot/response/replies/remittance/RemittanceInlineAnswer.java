package ru.ange.jointbuy.bot.response.replies.remittance;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Remittance;

import java.util.List;

public class RemittanceInlineAnswer extends EditMessageText {

    public RemittanceInlineAnswer(Remittance rem, List<Member> members) {
        super();

//        String remMsgTextData = getRemittanceMsgData( rem.getAmount(), rem.getName(), rem.getSender().getFullName(),
//                rem.getDate() );
//        String remMsgText = members.size() > 0 ?
//                remMsgTextData + Constants.REMITTANCE_MSG_CHOOSE_MEMBERS_LINE :
//                remMsgTextData + Constants.REMITTANCE_MSG_NOT_MEMBERS_LINE;

//        setChatId( rem.getTelegramChatId() )
//        setMessageId( replies.getMessageId() )
//        //.enableMarkdown( true )
//        setText( EmojiParser.parseToUnicode( remMsgText ) )
//        setReplyMarkup( markupInline );
    }
}

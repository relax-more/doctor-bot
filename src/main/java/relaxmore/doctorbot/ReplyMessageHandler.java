package relaxmore.doctorbot;

import com.google.common.collect.ImmutableList;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import relaxmore.doctorbot.model.MedicalRecord;
import relaxmore.doctorbot.storage.UserConsultationStorage;

import java.io.IOException;
import java.util.List;

import static relaxmore.doctorbot.model.MedicalRecord.Period;
import static relaxmore.doctorbot.model.MedicalRecord.Symptom;

@Slf4j
@Service
public class ReplyMessageHandler {

    private final LineMessagingService lineMessagingService;
    private final UserConsultationStorage consultationStorage;


    public ReplyMessageHandler(LineMessagingService lineMessagingService,
                               UserConsultationStorage consultationStorage) {
        this.lineMessagingService = lineMessagingService;
        this.consultationStorage = consultationStorage;
    }

    public BotApiResponse check1WhenStart(MessageEvent<TextMessageContent> event) throws IOException {
        String replyToken = event.getReplyToken();
        List<Message> messages = ImmutableList.of(
                new TemplateMessage("非対応端末です。LINE を最新にしてください。",
                        new ButtonsTemplate("", "Q1, 症状はいつ始まりましたか？", "", ImmutableList.of(
                                new PostbackAction("12時間以内", "Q1_" + Period.less12h.name()),
                                new PostbackAction("24時間以内", "Q1_" + Period.less24h.name()),
                                new PostbackAction("36時間以内", "Q1_" + Period.less36h.name()),
                                new PostbackAction("36時間以後", "Q1_" + Period.greater36h.name())
                        ))
                ));
        return lineMessagingService
                .replyMessage(new ReplyMessage(replyToken, messages))
                .execute()
                .body();

    }

    public BotApiResponse check2WhatHappen(PostbackEvent event) throws IOException {
        // TODO: device set consultationStorage and reply new question and answer
        String userId = event.getSource().getUserId();
        log.info("postbackContent: ", event.getPostbackContent());
        MedicalRecord medicalRecord = consultationStorage.get(userId);

        String dataString = event.getPostbackContent().getData();
        if (dataString.startsWith("Q1_")) {
            // set data to storage
            Period period = Period.valueOf(dataString.replace("Q1_", ""));
            medicalRecord.setPeriod(period);
            consultationStorage.update(userId, medicalRecord);

            // send new question
            String replyToken = event.getReplyToken();
            List<Message> messages = ImmutableList.of(
                    new TemplateMessage("非対応端末です。LINE を最新にしてください。",
                            new ButtonsTemplate("", "Q2, 症状はなんですか？", "", ImmutableList.of(
                                    new PostbackAction("咳", "Q2_" + Symptom.cough.name()),
                                    new PostbackAction("湿疹", "Q2_" + Symptom.eczema.name()),
                                    new PostbackAction("熱", "Q2_" + Symptom.fever.name())
                            ))
                    ));
            return lineMessagingService
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .execute()
                    .body();
        } else if (dataString.startsWith("Q2_")) {
            // set data to storage
            Symptom symptom = Symptom.valueOf(dataString.replace("Q2_", ""));
            medicalRecord.setSymptoms(ImmutableList.of(symptom)); // TODO: need to add list...
            consultationStorage.update(userId, medicalRecord);

            // send new question
            String replyToken = event.getReplyToken();
            List<Message> messages = ImmutableList.of(
                    new TextMessage("診察が必要です。診察を受けてください。"));
            return lineMessagingService
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .execute()
                    .body();
        }

        throw new RuntimeException();
    }
}

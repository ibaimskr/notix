package com.example.notix.beans;

import java.io.Serializable;

public class MailRequest implements Serializable {

    private String receiper;
    private String msgBody;
    private String subject;

    public MailRequest() { }

    public MailRequest(String receiper, String msgBody, String subject) {
        super();
        this.receiper = receiper;
        this.msgBody = msgBody;
        this.subject = subject;
    }

    public String getReceiper() { return receiper; }

    public void setReceiper(String receiper) { this.receiper = receiper; }

    public String getMsgBody() { return msgBody; }

    public void setMsgBody(String msgBody) { this.msgBody = msgBody; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    @Override
    public String toString() {
        return "{" +
                '"' + "recipient" + '"' + ":" + '"' + receiper + '"' +
                "," + '"' + "msgBody" + '"' + ":" + '"' + msgBody + '"' +
                "," + '"' + "subject" + '"' + ":" + '"'+ subject + '"' +
                '}';
    }
}

package org.s3979.tool.sgd6;

public class S6BettingModel {

    public String refId;
    public String member;
    public String betNumber;
    public String betAmount;
    public String betAmountBeforeComm;
    public String betAmountAfterComm;
    public String betComm;
    public String betType;
    public String betKind; // Danh thuong or Danh Live
    public String type;
    public String channel;
    public String ip;
    public String betMessage;
    public String betDate;
    public String betCreate;

    @Override
    public String toString() {
        return refId + " - " + member;
    }
}

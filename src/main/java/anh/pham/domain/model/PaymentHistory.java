package anh.pham.domain.model;

import java.time.LocalDate;

public class PaymentHistory {
    private String type;
    private Integer amount;
    private LocalDate processDate;
    private String state;
    private String provider;
    private Long billId;

    public PaymentHistory(
                   String type,
                   Integer amount,
                   LocalDate processDate,
                   String state,
                   String provider,
                   Long billId) {
        this.type = type;
        this.amount = amount;
        this.processDate = processDate;
        this.state = state;
        this.provider = provider;
        this.billId = billId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDate getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    @Override
    public String toString() {
        return "PaymentHistory{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                ", state='" + state + '\'' +
                ", provider='" + provider + '\'' +
                ", billId=" + billId +
                '}';
    }
}

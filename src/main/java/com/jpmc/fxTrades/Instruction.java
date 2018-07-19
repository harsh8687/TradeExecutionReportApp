package com.jpmc.fxTrades;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Objects;

/**
 * A class to represent the instruction sent by the clients
 */
public class Instruction {

    private final String entity;
    private final InstructionType type;
    private final BigDecimal agreedFx;
    private final Currency currency;
    private final LocalDate instDate;
    private final LocalDate settlementDate;
    private final Long units;
    private final BigDecimal pricePerUnit;

    private Instruction(Builder b) {
        this.entity = b.entity;
        this.type = b.type;
        this.agreedFx = b.agreedFx;
        this.currency = b.currency;
        this.instDate = b.instDate;
        this.settlementDate = b.settlementDate;
        this.units = b.units;
        this.pricePerUnit = b.pricePerUnit;
    }

    public String getEntity() {
        return entity;
    }

    public InstructionType getType() {
        return type;
    }

    public BigDecimal getAgreedFx() {
        return agreedFx;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDate getInstDate() {
        return instDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public Long getUnits() {
        return units;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public BigDecimal getAmountInUSD() {
        return this.pricePerUnit.multiply(new BigDecimal(this.units)).multiply(this.agreedFx);
    }

    @Override
    public String toString() {
        return "com.jpmc.fxTrades.Instruction{" +
                "entity='" + entity + '\'' +
                ", type=" + type +
                ", agreedFx=" + agreedFx +
                ", currency=" + currency +
                ", instDate=" + instDate +
                ", settlementDate=" + settlementDate +
                ", units=" + units +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(getEntity(), that.getEntity()) &&
                getType() == that.getType() &&
                Objects.equals(getAgreedFx(), that.getAgreedFx()) &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getInstDate(), that.getInstDate()) &&
                Objects.equals(getSettlementDate(), that.getSettlementDate()) &&
                Objects.equals(getUnits(), that.getUnits()) &&
                Objects.equals(getPricePerUnit(), that.getPricePerUnit());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEntity(), getType(), getAgreedFx(), getCurrency(), getInstDate(), getSettlementDate(), getUnits(), getPricePerUnit());
    }

    public static class Builder {

        private String entity;
        private InstructionType type;
        private BigDecimal agreedFx;
        private Currency currency;
        private LocalDate instDate;
        private LocalDate settlementDate;
        private Long units;
        private BigDecimal pricePerUnit;
        private DateTimeFormatter formatter;

        public Builder(String entity, String instDate) {
            this.entity = entity;

            this.formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            this.instDate = LocalDate.parse(instDate, this.formatter);
        }

        public Builder type(String type) {
            try {
                this.type = InstructionType.valueOf(type);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid instruction type: " + type);
            }
            return this;
        }

        public Builder agreedFx(String agreedFx) {
            this.agreedFx = new BigDecimal(agreedFx);
            return this;
        }

        public Builder currency(String currency) throws Exception {
            try {
                this.currency = Currency.getInstance(currency);
            } catch (Exception e) {
                throw new Exception("Invalid instruction currency: " + currency);
            }
            return this;
        }

        public Builder settlementDate(String settlementDateStr) {
            LocalDate settlementDate = LocalDate.parse(settlementDateStr, this.formatter);
            DayOfWeek settlementDay = settlementDate.getDayOfWeek();

            if ((this.currency.equals(Currency.getInstance("AED")) || this.currency.equals(Currency.getInstance("SAR")))) {
                if (DayOfWeek.FRIDAY.equals(settlementDay)) {
                    this.settlementDate = settlementDate.plusDays(2);

                } else if (DayOfWeek.SATURDAY.equals(settlementDay)) {
                    this.settlementDate = settlementDate.plusDays(1);
                }
            } else {
                if (DayOfWeek.SATURDAY.equals(settlementDay)) {
                    this.settlementDate = settlementDate.plusDays(2);

                } else if (DayOfWeek.SUNDAY.equals(settlementDay)) {
                    this.settlementDate = settlementDate.plusDays(1);
                }
            }
            if (this.settlementDate == null)
                this.settlementDate = settlementDate;

            if (this.settlementDate.isBefore(this.instDate)) {
                throw new IllegalArgumentException("SettlementDate: " + this.settlementDate + " cannot be before instruction date: " + this.instDate);
            }
            return this;
        }

        public Builder units(long units) {
            this.units = units;
            return this;
        }

        public Builder pricePerUnit(String pricePerUnit) {
            this.pricePerUnit = new BigDecimal(pricePerUnit);
            return this;
        }

        public Instruction build() {
            return new Instruction(this);
        }
    }
}
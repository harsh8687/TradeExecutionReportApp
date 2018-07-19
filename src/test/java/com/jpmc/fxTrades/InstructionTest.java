package com.jpmc.fxTrades;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

import static org.junit.Assert.assertTrue;

public class InstructionTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEntity() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getEntity().equals("foo"));
    }

    @Test
    public void testType() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getType().equals(InstructionType.B));
    }

    @Test
    public void testAgreedFx() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getAgreedFx().toString().equals("0.50"));
    }

    @Test
    public void testCurrency() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getCurrency().equals(Currency.getInstance("SGD")));
    }

    @Test(expected = Exception.class)
    public void testCurrencyInvalid() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("ABC")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();
    }

    @Test
    public void testInstDate() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getInstDate().toString().equals("2016-01-01"));
    }

    @Test
    public void testSettlementDateSame() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("04 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getSettlementDate().toString().equals("2016-01-04"));
    }

    @Test
    public void testSettlementDateWeekend() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getSettlementDate().toString().equals("2016-01-04"));
    }

    @Test
    public void testSettlementDateSARCurrency() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SAR")
                .settlementDate("01 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getSettlementDate().toString().equals("2016-01-03"));
    }

    @Test
    public void testSettlementDateAEDCurrency() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SAR")
                .settlementDate("04 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getSettlementDate().toString().equals("2016-01-04"));
    }

    @Test
    public void testUnits() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SAR")
                .settlementDate("04 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getUnits() == 200L);
    }

    @Test
    public void testPricePerUnit() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SAR")
                .settlementDate("04 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getPricePerUnit().toString().equals("100.25"));
    }

    @Test
    public void testAmountInUSD() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SAR")
                .settlementDate("04 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        assertTrue(instruction.getAmountInUSD().toString().equals("10025.0000"));
    }
}
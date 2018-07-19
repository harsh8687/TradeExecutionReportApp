package com.jpmc.report;

import com.jpmc.fxTrades.Instruction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IncomingReportGeneratorTest {

    @Test
    public void testReportBuy() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        List<Instruction> instructions = new ArrayList<>(1);
        instructions.add(instruction);
        IncomingReportGenerator generator = new IncomingReportGenerator(instructions);

        assertNotNull(generator.getReport());
        assertTrue(generator.getReport().getData().size() == 1);
        assertTrue(generator.getReport().getData().get("2016-01-04").toString().equals("0.0"));
    }

    @Test
    public void testReportSell() throws Exception {
        Instruction instruction = new Instruction.Builder("foo", "01 Jan 2016")
                .type("S")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        List<Instruction> instructions = new ArrayList<>(1);
        instructions.add(instruction);
        IncomingReportGenerator generator = new IncomingReportGenerator(instructions);

        assertNotNull(generator.getReport());
        assertTrue(generator.getReport().getData().size() == 1);
        assertTrue(generator.getReport().getData().get("2016-01-04").toString().equals("10025.0000"));
    }
}
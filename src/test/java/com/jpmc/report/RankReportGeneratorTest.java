package com.jpmc.report;

import com.jpmc.fxTrades.Instruction;
import com.jpmc.fxTrades.InstructionType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RankReportGeneratorTest {

    @Test
    public void testReportRankSingleEntry() throws Exception {
        List<Instruction> instructions = new ArrayList<>(2);
        Instruction instruction1 = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction.Builder("foo1", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(250)
                .pricePerUnit("100.25")
                .build();
        instructions.add(instruction2);

        Report report = new RankReportGenerator(instructions).getReport();

        assertNotNull(report);
        assertTrue(report.getData().size() == 1);
        assertTrue(((List) report.getData().get(InstructionType.B)).size() == 2);
        assertTrue(((List) report.getData().get(InstructionType.B)).get(0).equals("foo1"));
    }

    @Test
    public void testReportRankMultipleEntries() throws Exception {
        List<Instruction> instructions = new ArrayList<>(2);
        Instruction instruction1 = new Instruction.Builder("buy", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction.Builder("buy1", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(250)
                .pricePerUnit("100.25")
                .build();
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction.Builder("sell", "01 Jan 2016")
                .type("S")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();
        instructions.add(instruction3);

        Instruction instruction4 = new Instruction.Builder("sell1", "01 Jan 2016")
                .type("S")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(250)
                .pricePerUnit("100.25")
                .build();
        instructions.add(instruction4);

        Report report = new RankReportGenerator(instructions).getReport();

        assertNotNull(report);
        assertTrue(report.getData().size() == 2);
        assertTrue(((List) report.getData().get(InstructionType.B)).size() == 2
                && ((List) report.getData().get(InstructionType.S)).size() == 2);
        assertTrue(((List) report.getData().get(InstructionType.B)).get(0).equals("buy1")
                && ((List) report.getData().get(InstructionType.S)).get(0).equals("sell1"));
    }
}
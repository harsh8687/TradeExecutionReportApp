package com.jpmc.fxTrades;

import com.jpmc.report.Report;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * An integration test for trade execution and report generation feature
 */
public class TradeExecutorTest {

    private TradeExecutor executor;

    @Before
    public void setUp() {
        executor = new TradeExecutor();
    }

    @Test
    public void testIncomingReport() throws Exception {
        Instruction instruction1 = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        List<Instruction> instructions = new ArrayList<>(1);
        instructions.add(instruction1);
        executor.executeTrades(instructions);
        Report report = executor.generateIncomingTradesReport();

        assertNotNull(report);
        assertTrue("", report.getData().size() == 1);
        assertTrue("", report.getData().get("2016-01-04").toString().equals("0.0"));
    }

    @Test
    public void testOutgoingReport() throws Exception {
        Instruction instruction1 = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();

        List<Instruction> instructions = new ArrayList<>(1);
        instructions.add(instruction1);
        executor.executeTrades(instructions);
        Report report = executor.generateOutgoingTradesReport();

        assertNotNull(report);
        assertTrue("", report.getData().size() == 1);
        assertTrue("", report.getData().get("2016-01-04").toString().equals("10025.0000"));
    }

    @Test
    public void testRankingReport() throws Exception {
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
        executor.executeTrades(instructions);
        Report report = executor.generateRankingReport();

        assertNotNull(report);
        assertTrue("", report.getData().size() == 1);
        assertTrue(((List) report.getData().get(InstructionType.B)).size() == 2);
        assertTrue(((List) report.getData().get(InstructionType.B)).get(0).equals("foo1"));
    }
}
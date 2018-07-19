package com.jpmc.fxTrades;

import com.jpmc.report.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class which accepts instructions, executes them and generates various reports
 */
public class TradeExecutor {

    private static final Logger LOGGER = Logger.getLogger(TradeExecutor.class.getName());
    private List<Instruction> instructions = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        TradeExecutor executor = new TradeExecutor();
        executor.executeTrades();
        executor.generateAllReports();
    }

    public void executeTrades() throws Exception {
        Instruction instruction1 = new Instruction.Builder("foo", "01 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("SGD")
                .settlementDate("02 Jan 2016")
                .units(200)
                .pricePerUnit("100.25")
                .build();
        LOGGER.info(instruction1.toString() + " " + instruction1.getAmountInUSD());
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction.Builder("foo1", "02 Jan 2016")
                .type("B")
                .agreedFx("0.50")
                .currency("USD")
                .settlementDate("02 Jan 2016")
                .units(250)
                .pricePerUnit("100.25")
                .build();
        LOGGER.info(instruction2.toString() + " " + instruction2.getAmountInUSD());
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction.Builder("bar", "05 Jan 2016")
                .type("S")
                .agreedFx("0.22")
                .currency("EUR")
                .settlementDate("07 Jan 2016")
                .units(450)
                .pricePerUnit("150.5")
                .build();
        LOGGER.info(instruction3.toString() + " " + instruction3.getAmountInUSD());
        instructions.add(instruction3);

        Instruction instruction4 = new Instruction.Builder("bar", "05 Jan 2016")
                .type("S")
                .agreedFx("0.22")
                .currency("GBP")
                .settlementDate("07 Jan 2016")
                .units(500)
                .pricePerUnit("150.5")
                .build();
        LOGGER.info(instruction4.toString() + " " + instruction4.getAmountInUSD());
        instructions.add(instruction4);

        Instruction instruction5 = new Instruction.Builder("bar1", "05 Jan 2016")
                .type("S")
                .agreedFx("0.22")
                .currency("JPY")
                .settlementDate("09 Jan 2016")
                .units(400)
                .pricePerUnit("150.5")
                .build();
        LOGGER.info(instruction5.toString() + " " + instruction5.getAmountInUSD());
        instructions.add(instruction5);

        Instruction instruction6 = new Instruction.Builder("bar1", "05 Jan 2016")
                .type("B")
                .agreedFx("0.25")
                .currency("AED")
                .settlementDate("09 Jan 2016")
                .units(400)
                .pricePerUnit("150.5")
                .build();
        LOGGER.info(instruction6.toString() + " " + instruction6.getAmountInUSD());
        instructions.add(instruction6);
    }

    public void executeTrades(List<Instruction> instructions) {
        this.instructions.addAll(instructions);
    }

    public Report generateIncomingTradesReport() {
        if (instructions.isEmpty()) {
            LOGGER.info("No trade executed");
            return null;
        }
        IReportGenerator incomingReportGenerator = new IncomingReportGenerator(instructions);
        return incomingReportGenerator.getReport();
    }

    public Report generateOutgoingTradesReport() {
        if (instructions.isEmpty()) {
            LOGGER.info("No trade executed");
            return null;
        }
        IReportGenerator outgoingReportGenerator = new OutgoingReportGenerator(instructions);
        return outgoingReportGenerator.getReport();
    }

    public Report generateRankingReport() {
        if (instructions.isEmpty()) {
            LOGGER.info("No trade executed");
            return null;
        }
        IReportGenerator rankReportGenerator = new RankReportGenerator(instructions);
        return rankReportGenerator.getReport();
    }

    public void generateAllReports() {
        if (instructions.isEmpty()) {
            LOGGER.info("No trade executed");
            return;
        }
        IReportGenerator incomingReportGenerator = new IncomingReportGenerator(instructions);
        incomingReportGenerator.renderReport();

        IReportGenerator outgoingReportGenerator = new OutgoingReportGenerator(instructions);
        outgoingReportGenerator.renderReport();

        IReportGenerator rankReportGenerator = new RankReportGenerator(instructions);
        rankReportGenerator.renderReport();
    }
}

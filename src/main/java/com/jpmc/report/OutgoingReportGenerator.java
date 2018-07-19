package com.jpmc.report;

import com.jpmc.fxTrades.Instruction;
import com.jpmc.fxTrades.InstructionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A class which produces report for the amount in USD settled outgoing everyday
 */
public class OutgoingReportGenerator implements IReportGenerator {

    private static final Logger LOGGER = Logger.getLogger(OutgoingReportGenerator.class.getName());
    private Report<String, String> report;

    public OutgoingReportGenerator(List<Instruction> instructions) {
        if (instructions != null && !instructions.isEmpty()) {
            Map<LocalDate, List<Instruction>> outgoingInstructions = instructions.stream()
                    .filter(x -> InstructionType.B.equals(x.getType()))
                    .collect(Collectors.groupingBy(Instruction::getSettlementDate));

            Map<String, String> reportData = new HashMap<>();
            for (Map.Entry<LocalDate, List<Instruction>> entry : outgoingInstructions.entrySet()) {
                BigDecimal sum = entry.getValue().stream()
                        .map(x -> x.getAmountInUSD())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                reportData.put(entry.getKey().toString(), sum.toString());
            }

            Map<LocalDate, List<Instruction>> nonOutgoingInstructions = instructions.stream()
                    .filter(x -> !InstructionType.B.equals(x.getType()))
                    .collect(Collectors.groupingBy(Instruction::getSettlementDate));
            nonOutgoingInstructions.forEach((k, v) -> reportData.putIfAbsent(k.toString(), "0.0"));
            report = new Report(reportData);
        }
    }

    @Override
    public void renderReport() {
        System.out.println("Amount in USD settled outgoing everyday");
        System.out.println("-------------------------------------------------------------");
        report.getData().forEach((k, v) -> System.out.println("Date : " + k + " Total Settlement Amount : " + v));
        System.out.println();
    }

    @Override
    public Report getReport() {
        LOGGER.info("No. of outgoing instructions report entries: " + report.getData().size());
        return report;
    }
}

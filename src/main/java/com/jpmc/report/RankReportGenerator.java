package com.jpmc.report;

import com.jpmc.fxTrades.Instruction;
import com.jpmc.fxTrades.InstructionType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A class which produces report showing ranking of entities based on incoming and outgoing amount
 * for all days
 */
public class RankReportGenerator implements IReportGenerator {

    private static final Logger LOGGER = Logger.getLogger(RankReportGenerator.class.getName());
    private Report<InstructionType, List<String>> report;

    public RankReportGenerator(List<Instruction> instructions) {
        if (instructions != null && !instructions.isEmpty()) {
            Map<InstructionType, List<Instruction>> typeToInstMap = instructions.stream()
                    .collect(Collectors.groupingBy(Instruction::getType));

            Map<InstructionType, List<String>> typeToEntityMap = new HashMap<>(InstructionType.values().length);
            for (Map.Entry<InstructionType, List<Instruction>> typeEntry : typeToInstMap.entrySet()) {

                Map<String, List<Instruction>> entityToInstMap = typeEntry.getValue().stream()
                        .collect(Collectors.groupingBy(Instruction::getEntity));

                Map<String, BigDecimal> entityToTotalAmtPerType = new HashMap<>(entityToInstMap.size());
                for (Map.Entry<String, List<Instruction>> entityEntry : entityToInstMap.entrySet()) {
                    BigDecimal entityTotalAmtPerType = entityEntry.getValue().stream()
                            .map(x -> x.getAmountInUSD())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    entityToTotalAmtPerType.put(entityEntry.getKey(), entityTotalAmtPerType);
                }

                List<String> sortedEntities = entityToTotalAmtPerType.entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                typeToEntityMap.put(typeEntry.getKey(), sortedEntities);
            }
            report = new Report(typeToEntityMap);
        }
    }

    @Override
    public void renderReport() {
        System.out.println("Ranking of entities based on incoming and outgoing amount");
        System.out.println("-------------------------------------------------------------");
        for (Map.Entry<InstructionType, List<String>> entry : report.getData().entrySet()) {
            System.out.println("Instruction Type: " + entry.getKey() + "\nEntities in order: ");
            for (int i = 1; i <= entry.getValue().size(); i++) {
                System.out.println(i + ". " + entry.getValue().get(i - 1));
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public Report getReport() {
        LOGGER.info("No. of rank report entries: " + report.getData().size());
        return report;
    }
}
